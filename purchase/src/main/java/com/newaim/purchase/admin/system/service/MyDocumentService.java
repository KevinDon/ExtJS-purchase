package com.newaim.purchase.admin.system.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.AttachmentDao;
import com.newaim.purchase.admin.system.dao.MyDocumentDao;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional(readOnly=true)

public class MyDocumentService extends ServiceBase {

	@Autowired
	private MyDocumentDao myDocumentDao;

	@Autowired
	private AttachmentDao attachmentDao;


	public List<MyDocumentVo> list(Sort sort){
		List<MyDocumentVo> list = null;
		
		List<MyDocument> rows = myDocumentDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, MyDocument.class, MyDocumentVo.class);
		}
		
		return list;
		
	}
	
	public Page<MyDocumentVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MyDocument> spec =buildSpecification(params);
        Page<MyDocument> p = myDocumentDao.findAll(spec, pageRequest);
        Page<MyDocumentVo> page = p.map(new Converter<MyDocument, MyDocumentVo>() {
		    @Override
		    public MyDocumentVo convert(MyDocument dv) {
		        return BeanMapper.map(dv, MyDocumentVo.class);
		    }
		});
        
		return page;
	}

	public Page<MyDocumentVo> list(LinkedHashMap<String, Object> params, Pageable pageable){

		Specification<MyDocument> spec =buildSpecification(params);
		Page<MyDocument> p = myDocumentDao.findAll(spec, pageable);
		Page<MyDocumentVo> page = p.map(new Converter<MyDocument, MyDocumentVo>() {
			@Override
			public MyDocumentVo convert(MyDocument dv) {
				return BeanMapper.map(dv, MyDocumentVo.class);
			}
		});

		return page;
	}
	
	public MyDocumentVo get(String id){
		MyDocumentVo o;
		if(id == null) {id = "";}
		MyDocument row = myDocumentDao.findMyDocumentById(id);
		o = BeanMapper.map(row, MyDocumentVo.class);
		
		return o;
	}

	public MyDocument getMyDocument(String id){
		MyDocument o = myDocumentDao.findMyDocumentById(id);
		return o;
	}

	public MyDocument getMyDocumentByPath(String path){
        MyDocument o = myDocumentDao.findMyDocumentByPath(path);
        return o;
    }
	
	@Transactional
	public MyDocument add(MyDocument o){
		UserVo user = SessionUtils.currentUserVo();
		
		o.setCreatorId(user.getId());
		o.setCreatorCnName(user.getCnName());
		o.setCreatorEnName(user.getEnName());
		o.setDepartmentId(user.getDepartmentId());
		o.setDepartmentCnName(user.getDepartmentCnName());
		o.setDepartmentEnName(user.getDepartmentEnName());
		o.setCreatedAt(new Date());
		o.setId(null);

    	o = myDocumentDao.save(o);

    	return o;
	}

    @Transactional
    public MyDocument saveAs(MyDocument o){
        myDocumentDao.clear();

        return add(o);
    }

    public boolean isExistByEmailCid(String cid) {
        if(null != cid && StringUtils.isNotBlank(cid))
            return myDocumentDao.countMyDocumentByEmailCid(cid)>0;
        return false;
    }
	
	@Transactional
	public MyDocument save(MyDocument o){
		o.setUpdatedAt(new Date());
		o = myDocumentDao.save(o);
		return o;
	}

	@Transactional
	public void delete(ServletRequest request, String id){
        MyDocument md = getMyDocument(id);
        if(StringUtils.isNotBlank(md.getPath())){
            String curProjectPath = request.getServletContext().getRealPath("/");
            File f = new File(curProjectPath + "/" +md.getPath());
            File nf =new File(curProjectPath + "/" +md.getPath() + ".del");
            if(f.exists() && f.isFile()){
                if(!nf.exists()) {
                    try {
                        nf.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                logger.debug("oldF:" + f.getPath() +" / newF:"+ nf.getPath());
                f.renameTo(nf);
                f.delete();
            }
        }
		myDocumentDao.delete(id);
		attachmentDao.deleteByDocumentId(id);
	}
	    
	private Specification<MyDocument> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MyDocument> spec = DynamicSpecifications.bySearchFilter(filters.values(), MyDocument.class);
        return spec;
    }
}
