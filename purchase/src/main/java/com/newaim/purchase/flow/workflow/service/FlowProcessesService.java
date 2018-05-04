package com.newaim.purchase.flow.workflow.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.flow.workflow.dao.FlowProcessesDao;
import com.newaim.purchase.flow.workflow.entity.FlowProcesses;
import com.newaim.purchase.flow.workflow.vo.FlowProcessesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)
public class FlowProcessesService extends ServiceBase {

	@Autowired
	private FlowProcessesDao flowProcessesDao;

	public Page<FlowProcessesVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowProcesses> spec =buildSpecification(params);
        Page<FlowProcesses> p = flowProcessesDao.findAll(spec, pageRequest);
        Page<FlowProcessesVo> page = p.map(new Converter<FlowProcesses, FlowProcessesVo>() {
		    @Override
		    public FlowProcessesVo convert(FlowProcesses dv) {
		        return BeanMapper.map(dv, FlowProcessesVo.class);
		    }
		});
        
		return page;
	}
	public List<FlowProcesses> lastestList(){
		return flowProcessesDao.lastestList();
	}
	
	public FlowProcessesVo get(String id){
		FlowProcesses row = flowProcessesDao.findFlowProcessesById(id);
		FlowProcessesVo o = BeanMapper.map(row, FlowProcessesVo.class);
		return o;
	}

	public FlowProcesses getFlowProcesses(String id){
		FlowProcesses o = flowProcessesDao.findFlowProcessesById(id);
		return o;
	}
		
	@Transactional
	public FlowProcesses add(FlowProcesses o){
		o.setId(null);
		o.setStatus(1);
		o.setCreatedAt(new Date());
		
    	return flowProcessesDao.save(o);
	}
		
	@Transactional
	public FlowProcesses save(FlowProcesses o){

		o.setUpdatedAt(new Date());
		
		return flowProcessesDao.save(o);
	}

	@Transactional
	public FlowProcesses saveAs(FlowProcesses o){
		flowProcessesDao.clear();
		add(o);
		return flowProcessesDao.save(o);
	}

	@Transactional
	public void delete(String id){
		flowProcessesDao.delete(id);
	}
	
	@Transactional
	public void setDelete(String id){
		FlowProcesses o = getFlowProcesses(id);
		o.setStatus(3);
		flowProcessesDao.save(o);
	}

	private Specification<FlowProcesses> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowProcesses> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowProcesses.class);
        return spec;
    }	
}
