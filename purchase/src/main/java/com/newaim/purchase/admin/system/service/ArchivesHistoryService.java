package com.newaim.purchase.admin.system.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.entity.RoleResourceUnion;
import com.newaim.purchase.admin.account.entity.User;
import com.newaim.purchase.admin.account.service.RoleResourceUnionService;
import com.newaim.purchase.admin.account.service.UserRoleUnionService;
import com.newaim.purchase.admin.account.service.UserService;
import com.newaim.purchase.admin.account.vo.UserRoleUnionVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.dao.ArchivesHistoryDao;
import com.newaim.purchase.admin.system.entity.ArchivesHistory;
import com.newaim.purchase.admin.system.vo.ArchivesHistoryVo;
import com.newaim.purchase.archives.product.service.ProductCertificateService;
import com.newaim.purchase.archives.product.service.ProductCombinedService;
import com.newaim.purchase.archives.product.service.ProductService;
import com.newaim.purchase.archives.product.service.TariffService;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.desktop.message.helper.Msg;

@Service
@Transactional(readOnly = true)
public class ArchivesHistoryService extends ServiceBase {

    @Autowired
    private ArchivesHistoryDao archivesHistoryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    private PreparedStatement pres;

    @Autowired
    private RoleResourceUnionService roleResourceUnionService;
    
    @Autowired
    private UserRoleUnionService userRoleUnionService;
    
    @Autowired
    private VendorService vendorService;
    
    @Autowired
    private ServiceProviderService serviceProviderService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductCombinedService productCombinedService;
    
    @Autowired
    private ProductCertificateService productCertificateService;
    
    @Autowired
    private TariffService tariffService;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    
    public Page<ArchivesHistoryVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {

        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ArchivesHistory> spec = buildSpecification(params);
        Page<ArchivesHistory> p = archivesHistoryDao.findAll(spec, pageRequest);
        Page<ArchivesHistoryVo> page = p.map(new Converter<ArchivesHistory, ArchivesHistoryVo>() {
            @Override
            public ArchivesHistoryVo convert(ArchivesHistory dv) {
                return BeanMapper.map(dv, ArchivesHistoryVo.class);
            }
        });

        return page;
    }

    public ArchivesHistoryVo get(String id) {
        ArchivesHistory row = archivesHistoryDao.findArchivesHistoryById(id);
        ArchivesHistoryVo o = BeanMapper.map(row, ArchivesHistoryVo.class);

        return o;
    }

    public ArchivesHistoryVo get(String id, String[] params) {
        ArchivesHistoryVo o = get(id);
        return o;
    }


    public ArchivesHistory getArchivesHistory(String id) {
        ArchivesHistory o = archivesHistoryDao.findArchivesHistoryById(id);
        return o;
    }

    public ArchivesHistory getArchivesHistoryByCreatorId(String userId, String businessId) {
        ArchivesHistory o = archivesHistoryDao.getLastByCreatorIdAndBusinessIdAndStatusAndIsApplied(userId, businessId);

        return o;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(ArchivesHistory o) {
        UserVo user = SessionUtils.currentUserVo();
        o.setCreatorId(user.getId());
        o.setCreatorCnName(user.getCnName());
        o.setCreatorEnName(user.getEnName());
        o.setDepartmentId(user.getDepartmentId());
        o.setDepartmentCnName(user.getDepartmentCnName());
        o.setDepartmentEnName(user.getDepartmentEnName());

        o.setId(null);
        o.setStatus(1);
        o.setIsApplied(2);
        o.setCreatedAt(new Date());

        archivesHistoryDao.save(o);
    }

    @Transactional
    public void saveAs(ArchivesHistory o) {
        archivesHistoryDao.clear();
        o.setUpdatedAt(null);

        add(o);
    }

    @Transactional
    public void save(ArchivesHistory o) {
        o.setUpdatedAt(new Date());
        archivesHistoryDao.save(o);
    }

    @Transactional
    public void delete(String id) {
        //删除全部关联附件
        attachmentService.deleteByBusinessId(id);
        archivesHistoryDao.delete(id);
    }

    @Transactional
    public void setDelete(String id) {
        ArchivesHistory o = getArchivesHistory(id);
        o.setStatus(3);
        archivesHistoryDao.save(o);
    }

    @Transactional
    public <O extends Serializable> void save(String moduleName, String businessId, O newObj) throws
            IllegalAccessException, IntrospectionException, InvocationTargetException, IOException {

        UserVo user = SessionUtils.currentUserVo();

        ArchivesHistory o = getArchivesHistoryByCreatorId(user.getId(), businessId);
        if (null == o) o = new ArchivesHistory();

        o.setNewContent(SerializationUtils.serialize(newObj));
        o.setBusinessId(businessId);
        o.setModuleName(moduleName);
        o.setVer(getLastVerByBusinessId(businessId) + 1);

        sentConfirmMsg(moduleName, businessId,user.getId(),"edit");
        
        if (StringUtils.isBlank(o.getId()) || !o.getCreatorId().equals(user.getId())) {
            saveAs(o);
        } else {
            save(o);
        }
    }

    /**
     * 获取相同档案的最后版本号
     *
     * @param businessId
     * @return
     */
    private int getLastVerByBusinessId(String businessId) {
        return archivesHistoryDao.getLastVerByBusinessId(businessId);
    }

    @Transactional
    public Boolean confirm(String ahid) {
        Boolean result = false;

        ArchivesHistory o = getArchivesHistory(ahid);
        Date updatedAt = new Date();
        UserVo assignee = SessionUtils.currentUserVo();

        o.setIsApplied(1);
        o.setUpdatedAt(updatedAt);
        o.setAssigneeAt(updatedAt);
        o.setAssigneeId(assignee.getId());
        o.setAssigneeCnName(assignee.getCnName());
        o.setAssigneeEnName(assignee.getEnName());

        String moduleName = o.getModuleName();
        String businessId = o.getBusinessId();
        sentConfirmMsg(moduleName, businessId,o.getCreatorId(), "confirm");
        
        save(o);


        return result;
    }

    private Specification<ArchivesHistory> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ArchivesHistory> spec = DynamicSpecifications.bySearchFilter(filters.values(), ArchivesHistory.class);
        return spec;
    }

    public void clean(){
        archivesHistoryDao.clear();
    }

	public void sentConfirmMsg(String moduleName,String businessId, String creatorId, String type) {
		UserVo user = SessionUtils.currentUserVo();
        List<String> userIdList = Lists.newArrayList();
        if(type.equals("confirm")){
        	userIdList.add(creatorId);
        }
        String userName = user.getName();
        String projectType="";
        String signId = " " + businessId + " ";
        if("Vendor".equals(moduleName)){
        	projectType = " Vendor ";
        }else if("Service".equals(moduleName)){
          	projectType = " Service Provider ";
	    }else if("Product".equals(moduleName)){
	      	projectType = " Product ";
		}else if("ProductCombination".equals(moduleName)){
	      	projectType = " Product Combination ";
		}else if("ProductCertificate".equals(moduleName)){
	      	projectType = " Product Certificate";
		}else if("Tariff".equals(moduleName)){
	      	projectType = " Tariff ";
		}

        List<RoleResourceUnion> roleResourceUnionList = roleResourceUnionService.findRoleResourceUnionByModelAndAction(moduleName, "normal:confirm");
        for(RoleResourceUnion obj : roleResourceUnionList){
        	List<UserRoleUnionVo> rdlist = userRoleUnionService.listByRoleId(obj.getRoleId());
        	if(null!=rdlist&&!rdlist.isEmpty()){
        		for(UserRoleUnionVo vo: rdlist){
        			if(!userIdList.contains(vo.getUserId())&&checkUserNotEmpty(vo.getUserId())){
        				userIdList.add(vo.getUserId());
        			}
        		}
        	}
        }
        String userIds = StringUtils.join(userIdList.toArray(), ",");  
        if(type.equals("edit")){
            Msg.send(userIds, localeMessageSource.getMsgArchivesChangeTitle(new Object[]{projectType}),
                    localeMessageSource.getMsgArchivesEditContent(new Object[]{userName,projectType,signId}));
        }
        if(type.equals("confirm")){
            Msg.send(userIds, localeMessageSource.getMsgArchivesConfirmTitle(new Object[]{projectType}),
                    localeMessageSource.getMsgArchivesConfirmContent(new Object[]{userName,projectType,signId}));
        }
	}
	private boolean checkUserNotEmpty(String userId){
		boolean result = false;
		User val=userService.getUser(userId);
		if(val!=null&&val.getStatus()==1){
			result = true;
		}
		return result;
	}
	
}
