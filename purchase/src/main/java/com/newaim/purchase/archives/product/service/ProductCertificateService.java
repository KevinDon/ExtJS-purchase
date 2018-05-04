package com.newaim.purchase.archives.product.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.newaim.core.service.ServiceBase;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserDepartmentVo;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.product.dao.ProductCertificateDao;
import com.newaim.purchase.archives.product.dao.ProductCertificateUnionDao;
import com.newaim.purchase.archives.product.entity.ProductCertificate;
import com.newaim.purchase.archives.product.entity.ProductCertificateUnion;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionVo;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.vendor.entity.Vendor;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductCertificateService extends ServiceBase{


    @Autowired
    private ProductCertificateDao productCertificateDao;

    @Autowired
    private ProductCertificateUnionDao productCertificateUnionDao;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ProductCertificateUnionService productCertificateUnionService;



    public Page<ProductCertificateVo> listProduct(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<ProductCertificate> spec = buildSpecification(params);
        Page<ProductCertificate> p = productCertificateDao.findAll(spec, pageRequest);
        Page<ProductCertificateVo> page = p.map(new Converter<ProductCertificate, ProductCertificateVo>() {
            @Override
            public ProductCertificateVo convert(ProductCertificate productCertificate) {
                return convertToProductCertificateVo(productCertificate);
            }
        });
        return page;
    }

    public ProductCertificate getProductCertificate(String id){
        return productCertificateDao.findOne(id);
    }

    /**
     * 单独获取产品证书Vo
     * @param id
     * @return
     */
    public ProductCertificateVo getProductCertificateVo(String id){
        ProductCertificateVo vo =convertToProductCertificateVo(getProductCertificate(id));
        return vo;
    }

    /**
     * 获取证书Vo和对应的附件、供应商、明细信息
     * @param id
     * @return
     */
    public ProductCertificateVo get(String id){
        ProductCertificateVo vo =convertToProductCertificateVo(getProductCertificate(id));

        //证书文件附件
        vo.setCertificateAttach(attachmentService.getByBusinessIdAndModuleName(id, ConstantsAttachment.Status.ProductCertificate.code));

        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(productCertificateUnionService.findDetailsVoByBusinessId(id));
        return vo;
    }

    private ProductCertificateVo convertToProductCertificateVo(ProductCertificate productCertificate){
        ProductCertificateVo vo = BeanMapper.map(productCertificate, ProductCertificateVo.class);
        return vo;
    }

    /**
     * 添加
     * @param productCertificate
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductCertificate add(ProductCertificate productCertificate, List<ProductCertificateUnion> details){
        //设置供应商信息
        setVendorInfo(productCertificate);
        //设置创建人相关信息
        setProductCertificateCreatorInfo(productCertificate);
        productCertificate = productCertificateDao.save(productCertificate);
        saveDetails(productCertificate.getId(),details);
        //保存附件
        setCertificateFile(productCertificate);
        return productCertificate;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductCertificate update(ProductCertificate productCertificate,List<ProductCertificateUnion> details){
        //设置供应商信息
        setVendorInfo(productCertificate);
        //设置更新时间
        productCertificate.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        productCertificateUnionDao.deleteByCertificateId(productCertificate.getId());
        productCertificateUnionDao.save(details);
        saveDetails(productCertificate.getId(),details);
        //保存附件
        setCertificateFile(productCertificate);
        return productCertificateDao.save(productCertificate);
    }

    /**
     * 复制另存
     * @param productCertificate
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ProductCertificate saveAs(ProductCertificate productCertificate,List<ProductCertificateUnion> details){
        productCertificateDao.clear();
        //设置供应商信息
        setVendorInfo(productCertificate);
        //设置创建人相关信息
        setProductCertificateCreatorInfo(productCertificate);
        productCertificate.setId(null);
        productCertificate = productCertificateDao.save(productCertificate);
        saveDetails(productCertificate.getId(),details);
        //保存附件
        setCertificateFile(productCertificate);
        return productCertificate;
    }

    /**
     * 序列化档案修改申请对象
     * @param o
     * @param details
     * @return
     */
    public ProductCertificateVo getApplyVoToSave(ProductCertificate o, List<ProductCertificateUnion> details) {

        ProductCertificateVo vo = BeanMapper.map(o, ProductCertificateVo.class);

        //产品明细表
        if (details.size() > 0) {
            vo.setDetails(BeanMapper.mapList(details, ProductCertificateUnion.class, ProductCertificateUnionVo.class));
        } else {
            vo.setDetails(null);
        }

        //相关供应商
        if(vo.getVendorId() != null){
            vo.setVendor(vendorService.get(vo.getVendorId()));
            vo.setVendorCnName(vo.getVendor().getCnName());
            vo.setVendorEnName(vo.getVendor().getEnName());
        }else{
            vo.setVendor(null);
        }

        //证书附件
        if(vo.getCertificateFile() != null){
            vo.setCertificateAttach(attachmentService.get(vo.getCertificateFile()));
        }else{
            vo.setCertificateAttach(null);
        }

        return vo;

    }


    /**
     * 反序列化档案修改申请对象
     * @param vo
     * @return
     */
    public ProductCertificateVo getApplyVoToDisplay(ProductCertificateVo vo) {

        //产品明细
        if(null != vo.getDetails() && vo.getDetails().size()>0) {
            List<ProductCertificateUnionVo> temp = Lists.newArrayList();
            for (int i=0; i<vo.getDetails().size(); i++ ){
                ProductCertificateUnionVo pcfuv = new ProductCertificateUnionVo();
                BeanMapper.copyProperties(vo.getDetails().get(i), pcfuv, true);
                pcfuv.setProduct(productService.get(pcfuv.getProductId()));
                temp.add(pcfuv);
            }
            vo.setDetails(temp);
//            vo.setDetails(productCertificateUnionService.findProductCertificateUnionsByProductIdIn(temp.toArray(new String[temp.size()])));
        }

        //证书文件附件
        if(null != vo.getCertificateFile()) {
            vo.setCertificateAttach(attachmentService.get(vo.getCertificateFile()));
        }

        //相关供应商
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }

        return vo;
    }
    /**
     * 设置认证文件
     * @param productCertificate
     * @return
     */
    private void setCertificateFile(ProductCertificate productCertificate){
        if(StringUtils.isNotBlank(productCertificate.getFileId())){
        	attachmentService.deleteByBusinessId(productCertificate.getId(), ConstantsAttachment.Status.ProductCertificate.code);
            Attachment attachment = new Attachment();
            attachmentService.deleteByBusinessId(productCertificate.getId(), ConstantsAttachment.Status.ProductCertificate.code);
            attachment.setDocumentId(productCertificate.getFileId());
            attachment = attachmentService.add(attachment, ConstantsAttachment.Status.ProductCertificate.code, productCertificate.getId());
            productCertificate.setCertificateFile(attachment.getId());
        }
    }



    /**
     * 设置产品证书供应商信息
     * @param productCertificate
     */
    private void setVendorInfo(ProductCertificate productCertificate){
        Vendor vendor = vendorService.getVendor(productCertificate.getVendorId());
        if(vendor != null){
            productCertificate.setVendorCnName(vendor.getCnName());
            productCertificate.setVendorEnName(vendor.getEnName());
        }else{
            productCertificate.setVendorId(null);
        }
    }

    /**
     * 设置证书创建人信息，同时设置所属部门，用于新增时
     * @param productCertificate
     */
    private void setProductCertificateCreatorInfo(ProductCertificate productCertificate){
        UserVo user = SessionUtils.currentUserVo();
        productCertificate.setCreatedAt(new Date());
        productCertificate.setCreatorId(user.getId());
        productCertificate.setCreatorCnName(user.getCnName());
        productCertificate.setCreatorEnName(user.getEnName());
        //新建时为草稿状态
        productCertificate.setStatus(Constants.Status.DRAFT.code);
        UserDepartmentVo department = user.getDepartment();
        if(department != null){
            productCertificate.setDepartmentId(department.getId());
            productCertificate.setDepartmentCnName(department.getCnName());
            productCertificate.setDepartmentEnName(department.getEnName());
        }
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String certificateId, List<ProductCertificateUnion> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setCertificateId(certificateId);
            }
            productCertificateUnionDao.save(details);
        }
    }

    /**
     * 确认
     * @param id 产品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmProductCertificate(String id){
        updateProductCertificateStatus(id, Constants.Status.NORMAL.code);
    }

    /**
     * 标识为删除
     * @param id 产品ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductCertificate(String id){
        updateProductCertificateStatus(id, Constants.Status.DELETED.code);
    }

    /**
     * 更新产品状态
     * @param id
     * @param status
     */
    private void updateProductCertificateStatus(String id, Integer status){
        ProductCertificate productCertificate = getProductCertificate(id);
        if(productCertificate != null){
            productCertificate.setStatus(status);
            productCertificate.setUpdatedAt(new Date());
            productCertificateDao.save(productCertificate);
        }
    }

    
    public List<ProductCertificateVo> findByBusinessId(String businessId){
        //加载相关证书
        List<ProductCertificateVo> certificateVosList = new ArrayList<>();
        List<ProductCertificate> productCertificates = productCertificateDao.findByBusinessId(businessId);
        if (productCertificates!=null){
            for (ProductCertificate productCertificate :productCertificates){
                ProductCertificateVo vo = BeanMapper.map(productCertificate,ProductCertificateVo.class);
                certificateVosList.add(vo);
            }
        }
        return certificateVosList;
    }
    
    /**
     * 创建动态查询条件组合.
     */
    private Specification<ProductCertificate> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductCertificate> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductCertificate.class);
        return spec;
    }



}
