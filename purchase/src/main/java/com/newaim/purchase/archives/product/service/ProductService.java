package com.newaim.purchase.archives.product.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.admin.system.service.AutoCodeService;
import com.newaim.purchase.admin.system.vo.AttachmentVo;
import com.newaim.purchase.archives.flow.purchase.service.ProductQuotationService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.product.dao.ProductCertificateDao;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.dao.TariffDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.archives.product.entity.ProductCategory;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.archives.product.entity.ProductVendorProp;
import com.newaim.purchase.archives.product.entity.Tariff;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionVo;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.product.vo.ProductVendorPropVo;
import com.newaim.purchase.archives.product.vo.ProductVo;
import com.newaim.purchase.archives.service_provider.entity.ServiceProvider;
import com.newaim.purchase.archives.service_provider.service.ServiceProviderService;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.desktop.reports.service.ReportsProductService;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductService extends ServiceBase {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductCertificateDao productCertificateDao;
    
    @Autowired
    private TariffDao tariffDao;

    @Autowired
    private ProductCertificateUnionService unionService;

    @Autowired
    private ProductCertificateService certificateService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    @Autowired
    private ProductQuotationService productQuotationService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ReportsProductService reportsProductService;

    @Autowired
    private AutoCodeService autoCodeService;

    public Page<ProductVo> listProduct(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<Product> spec = buildSpecification(params);
        Page<Product> p = productDao.findAll(spec, pageRequest);
        Page<ProductVo> page = p.map(product -> convertToProductVo(product));
        return page;
    }

    public List<ProductVo> listProductsByHsCode(String hsCode){
        List<Product> data = productDao.findProductsByHsCode(hsCode);
        return BeanMapper.mapList(data, Product.class, ProductVo.class);
    }

    public Product getProduct(String id){
        return productDao.findProductById(id);
    }

    public Product getProductBySku(String sku){
        return productDao.findProductBySku(sku);
    }

    /**
     * 获取完整数据
     * @param id
     * @return
     */
    public ProductVo get(String id){
        ProductVo vo = convertToProductVo(getProduct(id));
        vo.setOrders(purchaseContractService.listByProductId(id, 20));
        vo.setQuotations(productQuotationService.findProductQuotationsVoByProductId(id));
        //获取photo附件
        ProductVendorPropVo prop = vo.getProp();
        if(prop != null){
            prop.setImagesDoc(attachmentService.listByBusinessIdAndModuleName(id, ConstantsAttachment.Status.Product_Photo.code));
            if(prop.getImagesDoc() != null){
                List<String> list = Lists.newArrayList();
                for(AttachmentVo atta: prop.getImagesDoc()){
                    list.add(atta.getDocumentId());
                }
                prop.setImages(StringUtils.join(list.toArray(), ","));
            }
            if(StringUtils.isNotBlank(prop.getHsCode())){
                Tariff tariff = tariffDao.findTopByItemCode(prop.getHsCode());
                if(tariff != null){
                    prop.setDutyRate(tariff.getDutyRate());
                }
            }
        }

        //证书
        List<ProductCertificateVo> certificateVosList = new ArrayList<>();
        List<ProductCertificateUnionVo> unionVos = unionService.findDetailsVoByProductId(vo.getId());
        if (unionVos!=null){
            for (ProductCertificateUnionVo union : unionVos){
                ProductCertificateVo certificateVo = certificateService.getProductCertificateVo(union.getCertificateId());
                certificateVosList.add(certificateVo);
            }
        }
        vo.setCertificates(certificateVosList);

        //报告
        vo.setReports(reportsProductService.listByProductId(id));

        return vo;
    }

    /**
     * 获取基础参数
     * @param id
     * @return
     */
    public ProductVo getBase(String id){
        Product o = getProduct(id);
        if(null == o) {
            return null;
        }

        ProductVo vo = convertToProductVo(o);
        return vo;
    }

    public ProductVo getBySku(String sku){
        Product o = getProductBySku(sku);
        if(null == o) {
            return null;
        }

        ProductVo vo = convertToProductVo(o);
        return vo;
    }


    /**
     * @TODO 以后优化productId使用In方法
     * 根据多个产品Id获取相对应的产品证书
     * @param productIds
     * @return
     */
    public List<ProductCertificateVo> getProductCertificateByProductId(List<String> productIds){
        //加载相关证书
        List<ProductCertificateVo> certificateVosList = new ArrayList<>();
        List<ProductCertificate> productCertificates = productCertificateDao.findByProductIds(productIds);
        if (productCertificates!=null){
            for (ProductCertificate productCertificate :productCertificates){
                ProductCertificateVo vo = BeanMapper.map(productCertificate,ProductCertificateVo.class);
                certificateVosList.add(vo);
            }
        }
        return certificateVosList;
    }

    private ProductVo convertToProductVo(Product product){
        if(null == product) {
            return null;
        }

        ProductVo vo = BeanMapper.map(product, ProductVo.class);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product){
        product.getProp().setProduct(product);
        //设置供应商类别/服务商类别信息
        setProductCategoryInfo(product);
        setProductVendorPropInfo(product);
        setProductServiceProviderPropInfo(product);
        //编辑
        if(StringUtils.isNotBlank(product.getId())){
            //设置更新信息
            product.setUpdatedAt(new Date());
            if(Constants.Status.DISABLED.code.equals(product.getStatus())){
                //禁用产品时更新采购询价单中的产品状态
                productQuotationService.disableProductStatus(product.getId());
            }
        }else{ //新增
            //设置创建人相关信息
            setProductCreatorInfo(product);
            product.setFlagFirst(1);
        }
        generateEanAndBarcode(product);
        save(product);
        //保存图片附件
        setPhotosFromImages(product.getProp());
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Product product){
        productDao.clear();

        //设置供应商类别
        setProductCategoryInfo(product);
        setProductVendorPropInfo(product);
        setProductServiceProviderPropInfo(product);
        //设置创建人相关信息
        setProductCreatorInfo(product);
        product.setFlagFirst(1);
        generateEanAndBarcode(product);
        productDao.save(product);

        //保存图片附件
        setPhotosFromImages(product.getProp());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Product product){

        //设置供应商类别信息
        setProductCategoryInfo(product);
        setProductVendorPropInfo(product);
        setProductServiceProviderPropInfo(product);
        //编辑
        if(StringUtils.isNotBlank(product.getId())){
            //设置操作人信息
            product.setUpdatedAt(new Date());
            if(Constants.Status.DISABLED.code.equals(product.getStatus())){
                //禁用产品时更新采购询价单中的产品状态
                productQuotationService.disableProductStatus(product.getId());
            }
        }else{ //新增
            //设置创建人相关信息
            setProductCreatorInfo(product);
            product.setFlagFirst(1);
        }
        generateEanAndBarcode(product);
        productDao.save(product);

        //保存图片附件
        setPhotosFromImages(product.getProp());
    }

    private void generateEanAndBarcode(Product product){
        if(StringUtils.isBlank(product.getEan())){
            product.setEan(autoCodeService.generateValue("ean", null));
        }
        if(StringUtils.isBlank(product.getBarcode())){
            product.setBarcode(autoCodeService.generateValue("barcode", null));
        }
    }

    /**
     * 判断是否已有sku
     * @param sku
     * @return
     */
    public boolean existsSku(String sku,int existSkuNum){
        List<Product> products = productDao.findProductsBySku(sku);
        List<Product> existSkuProducts = Lists.newArrayList();
        for(Product obj : products){
        	if(obj.getStatus()!=3){
        		existSkuProducts.add(obj);
        	}
        }
        if(existSkuProducts.size()>existSkuNum){
        	return true;
        }else{
        	return false;
        }
    }

    /**
     * 判断是否已有barcode
     * @param barcode\
     * @return
     */
    public boolean existsBarcode(String barcode){
        List<Product> products = productDao.findProductsByBarcode(barcode);
        return products != null && products.size() > 0 ? true : false;
    }

    /**
     * 判断是否已有ean
     * @param ean
     * @return
     */
    public boolean existsEan(String ean){
        List<Product> products = productDao.findProductsByEan(ean);
        return products != null && products.size() > 0 ? true : false;
    }

    /**
     * 通过sku查找产品
     * @param sku
     * @return
     */
    public Product findProductBySku(String sku){
        List<Product> products = productDao.findProductsBySku(sku);
        return products != null && products.size() > 0 ? products.get(0) : null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void convertToProduct(Product product, String sku){
        if(product != null){
            product.setSku(sku);
            product.setNewProduct(Product.NORMAL_PRODUCT);
            //设置初次标识
            product.setFlagFirst(1);
            //设置同步标识
            product.setIsSync(Constants.ProductSyncFlag.ADD.code);
            generateEanAndBarcode(product);
            productDao.save(product);
        }
    }

    /**
     * 设置产品供应商属性的供应商信息
     * @param product
     */
    private void setProductVendorPropInfo(Product product){
        ProductVendorProp prop = product.getProp();
        if(prop != null && StringUtils.isNotEmpty(prop.getVendorId())){
            prop.setSku(product.getSku());
            Vendor vendor = vendorService.getVendor(prop.getVendorId());
            if(vendor != null){
                prop.setVendorCnName(vendor.getCnName());
                prop.setVendorEnName(vendor.getEnName());
                prop.setVendorCode(vendor.getCode());
                prop.setCurrency(vendor.getCurrency());
            }
            if(StringUtils.isBlank(product.getId())){
                //新增时设置开发状态、安检状态和质检状态
                prop.setFlagDevStatus(Constants.NewProductStatus.DEV_STATUS_NOT_PASS.code);
                prop.setFlagComplianceStatus(Constants.NewProductStatus.COMPLIANCE_STATUS_NOT_PASS.code);
                prop.setFlagQcStatus(Constants.NewProductStatus.QC_STATUS_NOT_PASS.code);
            }
        }
    }

    /**
     * 写photos附件
     * @param prop
     */
    public void setPhotosFromImages (ProductVendorProp prop){
        if(StringUtils.isNotBlank(prop.getImages())){
            List<Attachment> attachments = Lists.newArrayList();
            String[] images = prop.getImages().split(",");
            for(String image : images){
                Attachment atta = new Attachment();
                atta.setDocumentId(image);
                attachments.add(atta);
            }
            attachments = attachmentService.save(attachments,  ConstantsAttachment.Status.Product_Photo.code, prop.getProduct().getId());

            //回写photos
            if(attachments.size()>0){
                List<String> attaIds = Lists.newArrayList();
                for(Attachment atta: attachments){
                    attaIds.add(atta.getId());
                }
                prop.setPhotos(StringUtils.join(attaIds.toArray(), ","));
            }

        }else{
            attachmentService.deleteByBusinessId(prop.getProduct().getId(),  ConstantsAttachment.Status.Product_Photo.code);
            prop.setPhotos(null);
        }
    }

    /**
     * 设置产品服务商属性的服务商信息
     * @param product
     */
    private void setProductServiceProviderPropInfo(Product product){
        ProductVendorProp prop = product.getProp();
        if(prop != null && StringUtils.isNotEmpty(prop.getServiceProviderId())){
            ServiceProvider serviceProvider = serviceProviderService.getServiceProvider(prop.getServiceProviderId());
            if(serviceProvider != null){
                prop.setServiceProviderCnName(serviceProvider.getCnName());
                prop.setServiceProviderEnName(serviceProvider.getEnName());
            }
        }
    }

    /**
     *  设置产品类别信息，用户新增或更新时
     * @param product
     */
    private void setProductCategoryInfo(Product product){
        if(StringUtils.isNotBlank(product.getCategoryId())){
            ProductCategory category = productCategoryService.getProductCategory(product.getCategoryId());
            product.setCategoryCnName(category.getCnName());
            product.setCategoryEnName(category.getEnName());
        }
    }

    /**
     * 设置产品创建人信息，同时设置所属部门，用于新增时
     * @param product
     */
    private void setProductCreatorInfo(Product product){
        UserVo user = SessionUtils.currentUserVo();
        product.setCreatedAt(new Date());
        product.setCreatorId(user.getId());
        product.setCreatorCnName(user.getCnName());
        product.setCreatorEnName(user.getEnName());
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            product.setDepartmentId(department.getId());
            product.setDepartmentCnName(department.getCnName());
            product.setDepartmentEnName(department.getEnName());
        }
    }

    
    
    /**
     * 序列化档案修改申请对象
     * @param o
     * @return
     */
    public ProductVo getApplyVoToSave(Product o) {

        o.setUpdatedAt(new Date());

    	ProductVo vo = BeanMapper.map(o, ProductVo.class);

        return vo;

    }

    /**
     * 反序列化档案修改申请对象
     * @param vo
     * @param oMap
     * @return
     */
    public ProductVo getApplyVoToDisplay(ProductVo vo, Map<String, ?> oMap) {

        if(null != oMap.get("prop") && ObjectUtils.anyNotNull(oMap)) {
            ProductVendorPropVo pvpv = new ProductVendorPropVo();
            BeanMapper.copyProperties(oMap.get("prop"), pvpv, true);
            //根据图片IDs加载附件List
            if (null != pvpv.getImages() && pvpv.getImages().length()>0){
                pvpv.setImagesDoc(attachmentService.listByDocumentId(pvpv.getImages(), pvpv.getId(), ConstantsAttachment.Status.Product_Photo.code));
            }
            vo.setProp(pvpv);
        }

        //加载相关证书
        List<ProductCertificateVo> certificateVosList = new ArrayList<>();
        List<ProductCertificateUnionVo> unionVos = unionService.findDetailsVoByProductId(vo.getId());
        if (unionVos!=null){
            for (ProductCertificateUnionVo union : unionVos){
                ProductCertificateVo certificateVo = certificateService.getProductCertificateVo(union.getCertificateId());
                certificateVosList.add(certificateVo);
            }
        }
        vo.setCertificates(certificateVosList);

        //加载相关订单
        vo.setOrders(purchaseContractService.listByProductId(vo.getId(), 20));

        //加载相关报告厅
        vo.setQuotations(productQuotationService.findProductQuotationsVoByProductId(vo.getId()));


        return vo;
    }

    /**
     * 确认
     * @param id 产品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmProduct(String id){
        updateProductStatus(id, Constants.Status.NORMAL.code);
    }

    /**
     * 标识为删除
     * @param id 产品ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(String id){
        updateProductStatus(id, Constants.Status.DELETED.code);
    }

    /**
     * 更新产品状态
     * @param id
     * @param status
     */
    private void updateProductStatus(String id, Integer status){
        Product product = getProduct(id);
        if(product != null){
            product.setStatus(status);
            product.setUpdatedAt(new Date());
            productDao.save(product);
        }
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Product> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Product> spec = DynamicSpecifications.bySearchFilter(filters.values(), Product.class);
        return spec;
    }
}
