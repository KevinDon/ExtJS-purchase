package com.newaim.purchase.archives.service_provider.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.archives.product.service.ProductForVendorService;
import com.newaim.purchase.archives.service_provider.dao.ServiceProviderDao;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderCategory;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderChargeItem;
import com.newaim.purchase.archives.service_provider.entity.ServiceProviderPort;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderChargeItemVo;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderPortVo;
import com.newaim.purchase.archives.service_provider.vo.ServiceProviderVo;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.email.entity.Contacts;
import com.newaim.purchase.desktop.email.service.ContactsService;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Updated By Lance at 2017/10/28
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ServiceProviderService extends ServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.Vendor.code;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private ServiceProviderCategoryService spCategoryService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private ProductForVendorService productForVendorService;

    @Autowired
    private ServiceProviderPortService serviceProviderPortService;

    @Autowired
    private ServiceProviderChargeItemService serviceProviderChargeItemService;


    /**
     * 分页查询
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<ServiceProviderVo> listServiceProvider(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        params.put("type-N-EQ-ADD", Constants.VendorType.SERVICE_PROVIDER.code.toString());
        Specification<ServiceProvider> spec = buildSpecification(params);
        Page<ServiceProvider> p = serviceProviderDao.findAll(spec, pageRequest);
        Page<ServiceProviderVo> page = p.map(new Converter<ServiceProvider, ServiceProviderVo>() {
            @Override
            public ServiceProviderVo convert(ServiceProvider serviceProvider) { return convertToServiceProviderVo(serviceProvider); }
        });
        return page;
    }

    public ServiceProvider getServiceProvider(String id) {
        return serviceProviderDao.findOne(id);
    }

    /**
     * 根据ID获取附件、银行信息、产品类型、图片附件、联系人信息
     *
     * @param id
     * @return
     */
    public ServiceProviderVo get(String id) {
        ServiceProviderVo vo = convertToServiceProviderVo(getServiceProvider(id));
        vo.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME));
        vo.setBank(getBankAccountByVendorId(vo.getId()));
        //获取photo附件
        vo.setImagesDoc(attachmentService.listByBusinessIdAndModuleName(id, ConstantsAttachment.Status.Vendor_Photo.code));
        if (vo.getImagesDoc() != null) {
            List<String> list = Lists.newArrayList();
            for (AttachmentVo atta : vo.getImagesDoc()) {
                list.add(atta.getDocumentId());
            }
            vo.setImages(StringUtils.join(list.toArray(), ","));
        }
        //联系人
        vo.setContacts(contactsService.listByVendorId(id));

        //港口
        vo.setPorts(serviceProviderPortService.listPortsByServiceProviderId(id));
        //收费项目
        vo.setChargeItems(serviceProviderChargeItemService.listChargeItemsByServiceProviderId(id));
        return vo;
    }

    /**
     * 根据ID获取产品
     *
     * @param id
     * @return
     */
    public ServiceProviderVo getWithProducts(String id) {
        ServiceProviderVo vo = get(id);
        vo.setProducts(productForVendorService.listByVendorId(vo.getId()));
        return vo;
    }


    public BankAccountVo getBankAccountByVendorId(String vendorId) {
        return bankAccountService.getByVendorId(vendorId);

    }

    /**
     * 将entity转换为Vo
     *
     * @param serviceProvider
     * @return
     */
    private ServiceProviderVo convertToServiceProviderVo(ServiceProvider serviceProvider) {
        ServiceProviderVo vo = BeanMapper.map(serviceProvider, ServiceProviderVo.class);
        return vo;
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(ServiceProvider serviceProvider, List<Attachment> attachments, List<Contacts> contactss,
                    List<ServiceProviderPort> ports, List<ServiceProviderChargeItem> chargeItems) {

        serviceProvider.setType(Constants.VendorType.SERVICE_PROVIDER.code);
        //设置服务商类别
        setServiceProviderCategoryInfo(serviceProvider);
        //设置创建人相关信息
        setServiceProviderCreatorInfo(serviceProvider);

        serviceProvider = serviceProviderDao.save(serviceProvider);

        //TODO 后期调整
        //保存联系人
        //contactsService.save(contactss, vendor.getId(), vendor.getCnName(), vendor.getEnName());

        //保存图片附件
        setPhotosFromImages(serviceProvider);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, serviceProvider.getId());

        //保存港口
        serviceProviderPortService.saveServiceProviderPort(ports, serviceProvider);
        //保存收费项目
        serviceProviderChargeItemService.saveServiceProviderChargeItem(chargeItems, serviceProvider);

        //回存
        serviceProviderDao.save(serviceProvider);
    }

    /**
     * 保存
     *
     * @param serviceProvider 服务商
     * @param attachments     附件
     * @param contactss       联系人
     * @param ports           港口
     * @param chargeItems     收费项目
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(ServiceProvider serviceProvider, List<Attachment> attachments, List<Contacts> contactss,
                     List<ServiceProviderPort> ports, List<ServiceProviderChargeItem> chargeItems) {
        serviceProvider.setType(Constants.VendorType.SERVICE_PROVIDER.code);
        //设置服务商类别信息
        setServiceProviderCategoryInfo(serviceProvider);

        if (StringUtils.isNotBlank(serviceProvider.getId())) {
            //编辑、设置更新信息
            serviceProvider.setUpdatedAt(new Date());
        } else {
            //设置创建人相关信息
            setServiceProviderCreatorInfo(serviceProvider);
        }
        serviceProvider = serviceProviderDao.save(serviceProvider);

        //TODO 后期调整
        //保存联系人
        //contactsService.save(contactss, vendor.getId(), vendor.getCnName(), vendor.getEnName());

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, serviceProvider.getId());

        //保存港口
        serviceProviderPortService.saveServiceProviderPort(ports, serviceProvider);
        //保存收费项目
        serviceProviderChargeItemService.saveServiceProviderChargeItem(chargeItems, serviceProvider);

        //保存图片附件
        setPhotosFromImages(serviceProvider);
        //回存
        serviceProviderDao.save(serviceProvider);
    }

    /**
     * 复制另存
     *
     * @param serviceProvider
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServiceProvider saveAs(ServiceProvider serviceProvider, List<Attachment> attachments, List<Contacts> contactss, List<ServiceProviderPort> ports, List<ServiceProviderChargeItem> chargeItems) {
        serviceProviderDao.clear();

        serviceProvider.setId(null);
        serviceProvider.setUpdatedAt(null);

        add(serviceProvider, attachments, contactss, ports, chargeItems);

        return serviceProvider;
    }


    /**
     * 序列化档案修改申请对象
     *
     * @param o
     * @param attachments
     * @param contactss
     * @param ports
     * @param chargeItems
     * @return
     */
    public ServiceProviderVo getApplyVoToSave(ServiceProvider o, List<Attachment> attachments, List<Contacts> contactss, List<ServiceProviderPort> ports, List<ServiceProviderChargeItem> chargeItems) {

        o.setType(Constants.VendorType.SERVICE_PROVIDER.code);
        //设置服务商类别信息
        setServiceProviderCategoryInfo(o);
        //设置创建人相关信息
        setServiceProviderCreatorInfo(o);

        ServiceProviderVo vo = BeanMapper.map(o, ServiceProviderVo.class);
        //附件
        if (attachments.size() > 0) {
            vo.setAttachments(BeanMapper.mapList(attachments, Attachment.class, AttachmentVo.class));
        } else {
            vo.setAttachments(null);
        }
        //联系人
        if (contactss.size() > 0) {
            vo.setContacts(BeanMapper.mapList(contactss, Contacts.class, ContactsVo.class));
        } else {
            vo.setContacts(null);
        }
        //港口
        if (ports.size() > 0) {
            vo.setPorts(BeanMapper.mapList(ports, ServiceProviderPort.class, ServiceProviderPortVo.class));
        } else {
            vo.setPorts(null);
        }
        //收费项目
        if (chargeItems.size() > 0) {
            vo.setChargeItems(BeanMapper.mapList(chargeItems, ServiceProviderChargeItem.class, ServiceProviderChargeItemVo.class));
        } else {
            vo.setChargeItems(null);
        }
        return vo;
    }

    /**
     * 反序列化档案修改申请对象
     *
     * @param serviceProvider
     * @return
     */
    public ServiceProviderVo getApplyVoToDisplay(ServiceProviderVo serviceProvider) {

        //根据bankId获取收款账号信息
        serviceProvider.setBank(getBankAccountByVendorId(serviceProvider.getId()));


        //根据documentId获取photo
        if (null != serviceProvider.getImages() && StringUtils.isNoneBlank(serviceProvider.getImages())) {
            serviceProvider.setImagesDoc(attachmentService.listByDocumentId(serviceProvider.getImages(), ConstantsAttachment.Status.Vendor_Photo.code, serviceProvider.getId()));
        } else {
            serviceProvider.setImagesDoc(null);
        }

        //根据documentId获取附件
        if (null != serviceProvider.getAttachments() && !ListUtils.isEmpty(serviceProvider.getAttachments())) {
            serviceProvider.setAttachments(attachmentService.listFromAttachmentVos(serviceProvider.getAttachments(), ConstantsAttachment.Status.Vendor.code, serviceProvider.getId()));
        } else {
            serviceProvider.setAttachments(null);
        }

        //根据contactId获取联系人
        if (null != serviceProvider.getContacts() && !ListUtils.isEmpty(serviceProvider.getContacts())) {
            serviceProvider.setContacts(contactsService.listByContactId(serviceProvider.getContacts()));
        } else {
            serviceProvider.setContacts(contactsService.listByVendorId(serviceProvider.getId()));
        }
        //根据serviceProviderId获取港口
        if (null != serviceProvider.getPorts() && !ListUtils.isEmpty(serviceProvider.getPorts())) {
            serviceProvider.setPorts(serviceProviderPortService.listPortsByPortId(serviceProvider.getPorts()));
        } else {
            serviceProvider.setPorts(null);
        }

        //根据serviceProviderId获取收费项目
        if (null != serviceProvider.getChargeItems() && !ListUtils.isEmpty(serviceProvider.getChargeItems())) {
            serviceProvider.setChargeItems(serviceProviderChargeItemService.listChargeItemByChargeItemIds(serviceProvider.getChargeItems()));
        } else {
            serviceProvider.setChargeItems(null);
        }


        return serviceProvider;
    }


    /**
     * 设置服务商类别信息，用户新增或更新时
     *
     * @param serviceProvider
     */
    private void setServiceProviderCategoryInfo(ServiceProvider serviceProvider) {
        if (StringUtils.isNotBlank(serviceProvider.getCategoryId())) {
            ServiceProviderCategory category = spCategoryService.getServiceProviderCategory(serviceProvider.getCategoryId());
            serviceProvider.setCategoryCnName(category.getCnName());
            serviceProvider.setCategoryEnName(category.getEnName());
        }
    }


    /**
     * 设置服务商创建人信息，同时设置所属部门和采购员信息，用于新增时
     *
     * @param serviceProvider
     */
    private void setServiceProviderCreatorInfo(ServiceProvider serviceProvider) {
        UserVo user = SessionUtils.currentUserVo();
        if (StringUtils.isBlank(serviceProvider.getId())) {
            serviceProvider.setCreatedAt(new Date());
            serviceProvider.setStatus(1);
        } else {
            serviceProvider.setUpdatedAt(new Date());
        }
        serviceProvider.setCreatorId(user.getId());
        serviceProvider.setCreatorCnName(user.getCnName());
        serviceProvider.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if (department != null) {
            serviceProvider.setDepartmentId(department.getId());
            serviceProvider.setDepartmentCnName(department.getCnName());
            serviceProvider.setDepartmentEnName(department.getEnName());
        }
        serviceProvider.setBuyerId(user.getId());
        serviceProvider.setBuyerCnName(user.getCnName());
        serviceProvider.setBuyerEnName(user.getEnName());
    }

    /**
     * 写photo附件
     *
     * @param serviceProvider
     */
    public void setPhotosFromImages(ServiceProvider serviceProvider) {
        if (serviceProvider.getImages() != null && StringUtils.isNotBlank(serviceProvider.getImages())) {
            List<Attachment> attachments = Lists.newArrayList();
            String[] images = serviceProvider.getImages().split(",");
            for (String image : images) {
                Attachment atta = new Attachment();
                atta.setDocumentId(image);
                attachments.add(atta);
            }
            attachments = attachmentService.save(attachments, ConstantsAttachment.Status.Vendor_Photo.code, serviceProvider.getId());

            //回写photos
            if (attachments.size() > 0) {
                List<String> attaIds = Lists.newArrayList();
                for (Attachment atta : attachments) {
                    attaIds.add(atta.getId());
                }
                serviceProvider.setPhotos(StringUtils.join(attaIds.toArray(), ","));
            }

        } else {
            attachmentService.deleteByBusinessId(serviceProvider.getId(), ConstantsAttachment.Status.Vendor_Photo.code);
            serviceProvider.setPhotos(null);
        }
    }


    /**
     * 确认
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmServiceProvider(String id) {
        ServiceProvider serviceProvider = serviceProviderDao.findOne(id);
        serviceProvider.setStatus(1);
        serviceProvider.setUpdatedAt(new Date());
        serviceProviderDao.save(serviceProvider);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteServiceProvider(String id) {
        //删除全部关联附件
        attachmentService.deleteByBusinessId(id);
        serviceProviderDao.delete(id);
    }

    /**
     * 根据ID修改状态为“删除”
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDelete(String id) {
        ServiceProvider sp = getServiceProvider(id);
        sp.setStatus(3);
        serviceProviderDao.save(sp);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<ServiceProvider> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ServiceProvider> spec = DynamicSpecifications.bySearchFilter(filters.values(), ServiceProvider.class);
        return spec;
    }
}
