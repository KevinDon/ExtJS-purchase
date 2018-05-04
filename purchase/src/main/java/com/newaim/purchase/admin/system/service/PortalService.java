package com.newaim.purchase.admin.system.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.PortalDao;
import com.newaim.purchase.admin.system.entity.Portal;
import com.newaim.purchase.admin.system.vo.PortalVo;
import com.newaim.purchase.admin.system.vo.PortalsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=true)

public class PortalService extends ServiceBase {

	@Autowired
	private PortalDao portalDao;

	public Page<PortalVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
		
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Portal> spec =buildSpecification(params);
        Page<Portal> p = portalDao.findAll(spec, pageRequest);
        Page<PortalVo> page = p.map(new Converter<Portal, PortalVo>() {
		    @Override
		    public PortalVo convert(Portal dv) {
		        return BeanMapper.map(dv, PortalVo.class);
		    }
		});
        
		return page;
	}

	public PortalVo get(String id){
		Portal row = portalDao.findPortalById(id);
		PortalVo o = BeanMapper.map(row, PortalVo.class);

		return o;
	}

	public PortalVo get(String id, String[] params){
		PortalVo o = get(id);

		return o;
	}

	public Portal getPortal(String id){
		Portal o = portalDao.findPortalById(id);
		return o;
	}
		
	@Transactional
	public Portal add(Portal o){
		UserVo user = SessionUtils.currentUserVo();
		o.setCreatorId(user.getId());

		o.setId(null);
		o.setStatus(1);

		o = portalDao.save(o);

    	return o;
	}

	@Transactional
	public Portal save(Portal o){
		o = portalDao.save(o);

		return o;
	}

    @Transactional
    public void save(PortalsVo portals) {
        for (Portal o : portals.getItems()){
            add(o);
        }

    }

	@Transactional
	public void delete(String id){
		portalDao.delete(id);
	}

    @Transactional
	public void deletePortalsByCreatorId(String creatorId){
		portalDao.deletePortalsByCreatorId(creatorId);
	}

	@Transactional
	public void setDelete(String id){
		Portal o = getPortal(id);
		o.setStatus(3);
		portalDao.save(o);
	}

	private Specification<Portal> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Portal> spec = DynamicSpecifications.bySearchFilter(filters.values(), Portal.class);
        return spec;
    }

}
