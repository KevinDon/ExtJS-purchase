package com.newaim.purchase.flow.workflow.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.RoleResourceVo;
import com.newaim.purchase.flow.workflow.dao.MyAgentResourceDao;
import com.newaim.purchase.flow.workflow.entity.MyAgentResource;
import com.newaim.purchase.flow.workflow.vo.MyAgentResourceVo;

@Service
@Transactional(readOnly=true)
public class MyAgentResourceService {

    @Autowired
    private MyAgentResourceDao myAgentResourceDao;

    @Transactional(readOnly = true)
    public Page<MyAgentResourceVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<MyAgentResource> spec = buildSpecification(params);
        Page<MyAgentResource> p = myAgentResourceDao.findAll(spec, pageRequest);
        Page<MyAgentResourceVo> page = p.map(new Converter<MyAgentResource, MyAgentResourceVo>() {
		    @Override
		    public MyAgentResourceVo convert(MyAgentResource resource) {
		        return BeanMapper.map(resource, MyAgentResourceVo.class);
		    }
		});
        return page;
    }
    
    /**
     * 根据userId返回所有流程代理有效授权
     * @param userId 代理人id
     * @return List<>
     */
    public List<MyAgentResource> findMyAgentResourcesByUserId(String userId){
    	Date now = new Date();
    	return myAgentResourceDao.findMyAgentResourcesByUserId(userId,now);
    }
    
	public RoleResourceVo get(String id) {
		return BeanMapper.map(myAgentResourceDao.findOne(id), RoleResourceVo.class);
	}
	
	@Transactional
	public void save(MyAgentResourceVo o){
		MyAgentResource r =(MyAgentResource)BeanMapper.map(o, MyAgentResource.class);
    	myAgentResourceDao.saveAndFlush(r);
		o.setId(r.getId());
	}
	
	@Transactional
	public void delete(String id){
		myAgentResourceDao.delete(id);
	}
	
	@Transactional
	public void deleteMyAgentResourceByBusinessId(String businessId){
		myAgentResourceDao.deleteMyAgentResourceByBusinessId(businessId);
	}
	/**
     * 创建动态查询条件组合.
     */
    private Specification<MyAgentResource> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<MyAgentResource> spec = DynamicSpecifications.bySearchFilter(filters.values(), MyAgentResource.class);
        return spec;
    }

}
