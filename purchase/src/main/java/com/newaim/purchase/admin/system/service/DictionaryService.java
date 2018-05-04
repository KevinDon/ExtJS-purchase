package com.newaim.purchase.admin.system.service;

/**
 * @author lance.hu
 */

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.DictionaryCallDao;
import com.newaim.purchase.admin.system.dao.DictionaryDao;
import com.newaim.purchase.admin.system.entity.Dictionary;
import com.newaim.purchase.admin.system.entity.DictionaryCall;
import com.newaim.purchase.admin.system.entity.DictionaryValue;
import com.newaim.purchase.admin.system.vo.DictionaryCallVo;
import com.newaim.purchase.admin.system.vo.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Transactional
@Service
public class DictionaryService extends ServiceBase {

	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Autowired
	private DictionaryCallDao dictionaryCallDao;
	

	@Autowired
	private DictionaryCategoryDescService dictionaryCategoryDescService;

	/**
	 * 获取字典
	 * @param sort
	 * @return
	 */
	public List<DictionaryVo> list(Sort sort){
		List<DictionaryVo> list = null;
		
		List<Dictionary> rows = dictionaryDao.findAll(sort);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, Dictionary.class, DictionaryVo.class);
		}
		
		return list;
		
	}

	/**
	 * 分页获取字典主项
	 * @param params
	 * @param pageNumber
	 * @param pageSize
	 * @param sort
	 * @return
	 */
	public Page<DictionaryVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){

		UserVo user = SessionUtils.currentUserVo();
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Dictionary> spec =buildSpecification(params);
        
        Page<Dictionary> p = dictionaryDao.findAll(spec, pageRequest);
        //在分类数量较小的情况下，一次查找
		final Map<String, String> categoryDescMap = dictionaryCategoryDescService.listMapByLang(user.getLang());
        Page<DictionaryVo> page = p.map(new Converter<Dictionary, DictionaryVo>() {

		    @Override
		    public DictionaryVo convert(Dictionary dv) {
		        DictionaryVo vo = BeanMapper.map(dv, DictionaryVo.class);
		        vo.setCategoryName(categoryDescMap.get(vo.getCategoryId()));
		        return vo;
		    }
		});
        
		return page;
	}

	/**
	 * 根据主编码获取数据字典项
	 * @param code
	 * @return
	 */
	public List<DictionaryCallVo> getByCodemain(String code){
		List<DictionaryCallVo> list = null;
		
		List<DictionaryCall> rows = dictionaryCallDao.findDictionaryByCodeMainOrderBySort(code);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryCall.class, DictionaryCallVo.class);
		}
		
		return list;
	}

	/**
	 * 根据主编码和子编码获取数据字典项
	 * @param code
	 * @param codeSub
	 * @return
	 */
	public List<DictionaryCallVo> getByCodemainAndCodeSub(String code, String codeSub){
		List<DictionaryCallVo> list = null;
//        List<DictionaryCall>  tempRows = Lists.newArrayList();

        List<DictionaryCall> rows = dictionaryCallDao.findDictionaryByCodeMainAndCodeSubAndStatusOrderBySort(code, codeSub, 1);

//		if(rows.size()>0) {
//            for (int i = 0; i < rows.size(); i++) {
//                tempRows.add(rows.get(i));
//                List<DictionaryValue> options = Lists.newArrayList();
//                for (int j = 0; j < rows.get(i).getOptions().size(); j++) {
//                    if (rows.get(i).getOptions().get(j).getStatus() != 1) {
//                        options.add(rows.get(i).getOptions().get(j));
//                    }
//                }
//                tempRows.get(i).setOptions(options);
//            }
//        }
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryCall.class, DictionaryCallVo.class);
		}
		
		return list;
	}

	/**
	 * 根据分类获取数据字典
	 * @param categoryId
	 * @return
	 */
	public List<DictionaryCallVo> getByCategoryId(String categoryId){
		List<DictionaryCallVo> list = null;
		
		List<DictionaryCall> rows = dictionaryCallDao.findDictionaryByCategoryId(categoryId);
		
		if(rows.size() >0 ){
			list =BeanMapper.mapList(rows, DictionaryCall.class, DictionaryCallVo.class);
		}
		
		return list;
	}
	
	public DictionaryVo get(String id){
		DictionaryVo o;
		if(id == null) {
			id = "";
		}
		Dictionary row = dictionaryDao.findDictionaryById(id);
		o = BeanMapper.map(row, DictionaryVo.class);
		
		return o;
		
	}
	
	public void save(DictionaryVo o){
		Dictionary r = BeanMapper.map(o, Dictionary.class);
		dictionaryDao.saveAndFlush(r);
		o.setId(r.getId());
	}
	

	public void delete(String id){
		dictionaryDao.delete(id);
	}
	    
	private Specification<Dictionary> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Dictionary> spec = DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class);
        return spec;
    }
	
}
