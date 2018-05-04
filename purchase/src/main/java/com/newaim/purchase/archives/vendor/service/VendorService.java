package com.newaim.purchase.archives.vendor.service;

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
import com.newaim.purchase.admin.system.service.AutoCodeService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.finance.service.BankAccountService;
import com.newaim.purchase.archives.finance.vo.BankAccountVo;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.product.service.ProductForVendorService;
import com.newaim.purchase.archives.vendor.dao.VendorDao;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.entity.VendorCategory;
import com.newaim.purchase.archives.vendor.entity.VendorProductCategoryUnion;
import com.newaim.purchase.archives.vendor.vo.VendorProductCategoryUnionVo;
import com.newaim.purchase.archives.vendor.vo.VendorVo;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.email.entity.Contacts;
import com.newaim.purchase.desktop.email.service.ContactsService;
import com.newaim.purchase.desktop.email.vo.ContactsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * Created by Mark on 2017/9/18.
 * Updated By Lance at 2017/10/28
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VendorService extends ServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.Vendor.code;

    @Autowired
    private VendorDao vendorDao;

    @Autowired
    private VendorProductCategoryUnionService vendorProductCategoryUnionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private ProductForVendorService productForVendorService;

    @Autowired
    private VendorCategoryService vendorCategoryService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private AutoCodeService autoCodeService;


    /**
     * 分页查询List
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<VendorVo> listVendor(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        params.put("type-N-EQ-ADD", Constants.VendorType.VENDOR.code.toString());
        Specification<Vendor> spec = buildSpecification(params);
        Page<Vendor> p = vendorDao.findAll(spec, pageRequest);
        Page<VendorVo> page = p.map(vendor ->{
            VendorVo vo = convertToVendorVo(vendor);
            BankAccountVo bankAccount = bankAccountService.getByVendorId(vo.getId());
            if (bankAccount!=null) {
                vo.setDepositType(bankAccount.getDepositType());
                vo.setDepositRate(bankAccount.getDepositRate());
            }
            return vo;
        });
        return page;
    }

    public Vendor getVendor(String id) {
        Vendor v = new Vendor();
        try{
            Vendor temp = vendorDao.findVendorById(id);
            if(null != temp)  {
                v = temp;
            }

        }finally {
            return v;
        }
    }

    /**
     * 判断是否已有code
     * @param code
     * @return
     */
    public boolean existsVendorCode(String code){
        List<Vendor> vendors = vendorDao.findByCode(code);
        return vendors != null && vendors.size() > 0 ? true : false;
    }

    /**
     * 根据ID获取附件、银行信息、产品类型、图片附件、联系人信息
     *
     * @param id
     * @return
     */
    public VendorVo get(String id) {
        VendorVo vo = convertToVendorVo(getVendor(id));
        vo.setProductCategory(vendorProductCategoryUnionService.listByVendorId(vo.getId()));
        vo.setAttachments(attachmentService.listByBusinessIdAndModuleName(id, FILE_MODULE_NAME));
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
        BankAccountVo bankAccount = bankAccountService.getByVendorId(vo.getId());
        if (bankAccount!=null) {
            vo.setDepositType(bankAccount.getDepositType());
            vo.setDepositRate(bankAccount.getDepositRate());
            vo.setBank(bankAccount);
        }
        return vo;
    }

    /**
     * 根据ID获取产品
     *
     * @param id
     * @return
     */
    public VendorVo getWithProducts(String id) {
        VendorVo vo = get(id);
        vo.setProducts(productForVendorService.listByVendorId(vo.getId()));
        BankAccountVo bankAccount = bankAccountService.getByVendorId(vo.getId());
        if (bankAccount!=null) {
            vo.setDepositType(bankAccount.getDepositType());
            vo.setDepositRate(bankAccount.getDepositRate());
        }
        vo.setOrders(purchaseContractService.findVoByVendorId(id));
        return vo;
    }

    public BankAccountVo getBankAccountByVendorId(String vendorId) {
        return bankAccountService.getByVendorId(vendorId);
    }

    /**
     * 将entity转换为Vo
     *
     * @param vendor
     * @return
     */
    private VendorVo convertToVendorVo(Vendor vendor) {
        VendorVo vo = BeanMapper.map(vendor, VendorVo.class);
        return vo;
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Vendor add(Vendor vendor, List<VendorProductCategoryUnion> productCategory, List<Attachment> attachments, List<Contacts> contactss) {

        vendor.setStatus(1);
        vendor.setId(null);
        vendor.setType(Constants.VendorType.VENDOR.code);
        //设置供应商类别
        setVendorCategoryInfo(vendor);

        //设置创建人相关信息
        setVendorCreatorInfo(vendor);
        generateVendorCode(vendor);
        vendor = vendorDao.save(vendor);

        //插入产品分类
        vendorProductCategoryUnionService.save(productCategory, vendor.getId());

        //TODO 后期调整
        //保存联系人
        //contactsService.save(contactss, vendor.getId(), vendor.getCnName(), vendor.getEnName());

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, vendor.getId());

        //保存图片附件
        setPhotosFromImages(vendor);

        //回存
        vendor = vendorDao.save(vendor);

        return vendor;
    }


    /**
     * 保存
     *
     * @param vendor
     * @param productCategory
     * @param attachments
     * @param contactss
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Vendor vendor, List<VendorProductCategoryUnion> productCategory, List<Attachment> attachments, List<Contacts> contactss) {
        vendor.setType(Constants.VendorType.VENDOR.code);
        //设置供应商类别信息
        setVendorCategoryInfo(vendor);

        if (StringUtils.isNotBlank(vendor.getId())) { //编辑
            //设置更新信息
            vendor.setUpdatedAt(new Date());
        } else { //新增
            //设置创建人相关信息
            setVendorCreatorInfo(vendor);
        }
        generateVendorCode(vendor);
        vendor = vendorDao.save(vendor);

        //插入产品分类
        vendorProductCategoryUnionService.save(productCategory, vendor.getId());

        //TODO 后期调整
        //保存联系人
        //contactsService.save(contactss, vendor.getId(), vendor.getCnName(), vendor.getEnName());

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, vendor.getId());

        //保存图片附件
        setPhotosFromImages(vendor);

        //回存
        vendorDao.save(vendor);

    }

    /**
     * 另外新供应商
     *
     * @param vendor
     * @param productCategory
     * @param attachments
     * @param contactss
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Vendor saveAs(Vendor vendor, List<VendorProductCategoryUnion> productCategory, List<Attachment> attachments, List<Contacts> contactss) {
        vendorDao.clear();
        
        vendor.setId(null);
        vendor.setUpdatedAt(null);
        generateVendorCode(vendor);
        vendor = add(vendor, productCategory, attachments, contactss);

        return vendor;
    }

    private void generateVendorCode(Vendor vendor){
        if(StringUtils.isBlank(vendor.getCode())){
            String code = autoCodeService.generateValue("vendor_code", null);
            List<Vendor> data = vendorDao.findByCode(code);
            if(data != null && data.size() > 0){
                generateVendorCode(vendor);
            }else{
                vendor.setCode(code);
            }
        }
    }

    /**
     * 序列化档案修改申请对象
     *
     * @param o
     * @param productCategory
     * @param attachments
     * @param contactss    @return
     */
    public VendorVo getApplyVoToSave(Vendor o, List<VendorProductCategoryUnion> productCategory, List<Attachment> attachments, List<Contacts> contactss) {
        o.setType(Constants.VendorType.VENDOR.code);
        //设置供应商分类信息
        setVendorCategoryInfo(o);
        //设置创建人相关信息
        setVendorCreatorInfo(o);

        VendorVo vo = BeanMapper.map(o, VendorVo.class);

        if (attachments.size() > 0) {
            vo.setAttachments(BeanMapper.mapList(attachments, Attachment.class, AttachmentVo.class));
        } else {
            vo.setAttachments(null);
        }

        if (contactss.size() > 0) {
            vo.setContacts(BeanMapper.mapList(contactss, Contacts.class, ContactsVo.class));
        } else {
            vo.setContacts(null);
        }

        if (productCategory.size() > 0) {
            vo.setProductCategory(BeanMapper.mapList(productCategory, VendorProductCategoryUnion.class, VendorProductCategoryUnionVo.class));
        } else {
            vo.setProductCategory(null);
        }

        return vo;
    }

    /**
     * 反序列化档案修改申请对象
     *
     * @param vendor
     * @return
     */
    public VendorVo getApplyVoToDisplay(VendorVo vendor) {

        //根据bankId获取收款账号信息
        vendor.setBank(getBankAccountByVendorId(vendor.getId()));

        //根据documentId获取photo
        if (null != vendor.getImages() && StringUtils.isNoneBlank(vendor.getImages())) {
            vendor.setImagesDoc(attachmentService.listByDocumentId(vendor.getImages(), ConstantsAttachment.Status.Vendor_Photo.code, vendor.getId()));
        } else {
            vendor.setImagesDoc(null);
        }

        //根据documentId获取附件
        if (null != vendor.getAttachments() && !ListUtils.isEmpty(vendor.getAttachments())) {
            vendor.setAttachments(attachmentService.listFromAttachmentVos(vendor.getAttachments(), ConstantsAttachment.Status.Vendor.code, vendor.getId()));
        } else {
            vendor.setAttachments(null);
        }

        //根据contactId获取联系人
        if (null != vendor.getContacts() && ListUtils.isEmpty(vendor.getContacts())) {
            vendor.setContacts(contactsService.listByContactId(vendor.getContacts()));
        } else {
            vendor.setContacts(contactsService.listByVendorId(vendor.getId()));
        }

        //根据productId 获取产品信息
        if (null != vendor.getProducts() && !ListUtils.isEmpty(vendor.getProducts())) {
            vendor.setProducts(productForVendorService.listByProductId(vendor.getProducts()));
        } else {
            vendor.setProducts(null);
        }

        //根据product category id 获取供应商产品分类信息
        if (null != vendor.getProductCategory() && !ListUtils.isEmpty(vendor.getProductCategory())) {
            vendor.setProductCategory(vendorProductCategoryUnionService.listByProductCategoryId(vendor.getProductCategory()));
        } else {
            vendor.setProductCategory(null);
        }

        return vendor;
    }

    /**
     * 设置供应商类别信息，用户新增或更新时
     *
     * @param vendor
     */
    private void setVendorCategoryInfo(Vendor vendor) {
        if (StringUtils.isNotBlank(vendor.getCategoryId())) {
            VendorCategory category = vendorCategoryService.getVendorCategory(vendor.getCategoryId());
            if(category != null){
                vendor.setCategoryCnName(category.getCnName());
                vendor.setCategoryEnName(category.getEnName());
            }
        }
    }

    /**
     * 设置供应商创建人信息，同时设置所属部门和采购员信息，用于新增时
     *
     * @param vendor
     */
    private void setVendorCreatorInfo(Vendor vendor) {
        UserVo user = SessionUtils.currentUserVo();
        if (StringUtils.isBlank(vendor.getId())) {
            vendor.setCreatedAt(new Date());
            vendor.setStatus(1);
        } else {
            vendor.setUpdatedAt(new Date());
        }
        vendor.setCreatorId(user.getId());
        vendor.setCreatorCnName(user.getCnName());
        vendor.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if (department != null) {
            vendor.setDepartmentId(department.getId());
            vendor.setDepartmentCnName(department.getCnName());
            vendor.setDepartmentEnName(department.getEnName());
        }
        vendor.setBuyerId(user.getId());
        vendor.setBuyerCnName(user.getCnName());
        vendor.setBuyerEnName(user.getEnName());
    }

    /**
     * 写photos附件
     *
     * @param vendor
     */
    public void setPhotosFromImages(Vendor vendor) {
        if (vendor.getImages() != null && StringUtils.isNotBlank(vendor.getImages())) {
            List<Attachment> attachments = Lists.newArrayList();
            String[] images = vendor.getImages().split(",");
            for (String image : images) {
                Attachment atta = new Attachment();
                atta.setDocumentId(image);
                attachments.add(atta);
            }
            attachments = attachmentService.save(attachments, ConstantsAttachment.Status.Vendor_Photo.code, vendor.getId());

            //回写photos
            if (attachments.size() > 0) {
                List<String> attaIds = Lists.newArrayList();
                for (Attachment atta : attachments) {
                    attaIds.add(atta.getId());
                }
                vendor.setPhotos(StringUtils.join(attaIds.toArray(), ","));
            }

        } else {
            attachmentService.deleteByBusinessId(vendor.getId(), ConstantsAttachment.Status.Vendor_Photo.code);
            vendor.setPhotos(null);
        }
    }

    /**
     * 确认
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmVendor(String id) {
        Vendor vendor = vendorDao.findOne(id);
        vendor.setStatus(1);
        vendor.setUpdatedAt(new Date());
        vendorDao.save(vendor);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteVendor(String id) {
        //删除全部关联附件
        attachmentService.deleteByBusinessId(id);

        vendorDao.delete(id);
    }

    /**
     * 根据ID修改状态为“删除”
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDelete(String id) {
        Vendor o = getVendor(id);
        o.setStatus(Constants.Status.DELETED.code);
        vendorDao.save(o);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Vendor> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Vendor> spec = DynamicSpecifications.bySearchFilter(filters.values(), Vendor.class);
        return spec;
    }

}
