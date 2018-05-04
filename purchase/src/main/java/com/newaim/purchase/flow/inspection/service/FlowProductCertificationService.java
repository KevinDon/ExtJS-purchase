package com.newaim.purchase.flow.inspection.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.product.entity.ProductCertificateUnion;
import com.newaim.purchase.archives.product.service.ProductCertificateService;
import com.newaim.purchase.archives.product.service.ProductCertificateUnionService;
import com.newaim.purchase.archives.product.vo.ProductCertificateUnionVo;
import com.newaim.purchase.archives.product.vo.ProductCertificateVo;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.inspection.dao.FlowProductCertificationDao;
import com.newaim.purchase.flow.inspection.dao.FlowProductCertificationDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertification;
import com.newaim.purchase.flow.inspection.entity.FlowProductCertificationDetail;
import com.newaim.purchase.flow.inspection.vo.FlowProductCertificationDetailVo;
import com.newaim.purchase.flow.inspection.vo.FlowProductCertificationVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowProductCertificationService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowProductCertification.code;

    @Autowired
    private FlowProductCertificationDao flowProductCertificationDao;

    @Autowired
    private FlowProductCertificationDetailDao flowProductCertificationDetailDao;

    @Autowired
    private FlowProductCertificationDetailService flowProductCertificationDetailService;

    @Autowired
    private ProductCertificateService certificateService;

    @Autowired
    private ProductCertificateUnionService unionService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    public Page<FlowProductCertificationVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowProductCertification> spec = buildSpecification(params);
        Page<FlowProductCertification> p = flowProductCertificationDao.findAll(spec, pageRequest);
        Page<FlowProductCertificationVo> page = p.map(new Converter<FlowProductCertification, FlowProductCertificationVo>() {
            @Override
            public FlowProductCertificationVo convert(FlowProductCertification flowProductCertification) {
                return convertToFlowProductCertificationVo(flowProductCertification);
            }
        });
        return page;
    }

    private FlowProductCertificationVo convertToFlowProductCertificationVo(FlowProductCertification flowProductCertification){
        FlowProductCertificationVo vo = BeanMapper.map(flowProductCertification, FlowProductCertificationVo.class);
        return vo;
    }

    /**
     * 根据Id查询所有产品认证
     * @param id
     * @return
     */
    public FlowProductCertification getFlowProductCertification(String id){
        return flowProductCertificationDao.findOne(id);
    }

    public FlowProductCertificationVo get(String id){
        FlowProductCertificationVo vo =convertToFlowProductCertificationVo(getFlowProductCertification(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setAttachments(attachmentService.listByBusinessId(id));
        vo.setDetails(flowProductCertificationDetailService.findDetailsVoByBusinessId(id));
       //根据产品Id获取产品证书
       List<FlowProductCertificationDetailVo> detailVos = vo.getDetails();
        List<ProductCertificateVo> certificateVosList = new ArrayList<>();
        if (detailVos!=null){
            for (int i =0;i<detailVos.size();i++){
                FlowProductCertificationDetailVo detailVo = detailVos.get(i);
                //根据产品ID查找证书关联表
                List<ProductCertificateUnionVo> unionVoList =unionService.findDetailsVoByProductId(detailVo.getProductId());
                if (null != unionVoList) {
                    for (int j = 0; j < unionVoList.size(); j++) {
                        ProductCertificateUnionVo productCertificateUnionVo = unionVoList.get(j);
                        //根据证书ID查找产品证书
                        ProductCertificateVo certificateVo = certificateService.getProductCertificateVo(productCertificateUnionVo.getCertificateId());
                        certificateVo.setCertificateAttach(attachmentService.getByBusinessIdAndModuleName(certificateVo.getId(), ConstantsAttachment.Status.ProductCertificate.code));
                        certificateVosList.add(certificateVo);
                    }
                }
            }
        }
        vo.setCertificates(certificateVosList);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowProductCertification add(FlowProductCertification flowProductCertification, List<FlowProductCertificationDetail> details, List<Attachment> attachments){
        setFlowCreatorInfo(flowProductCertification);
        setFlowVendorInfo(flowProductCertification);

        flowProductCertification = flowProductCertificationDao.save(flowProductCertification);
        saveDetails(flowProductCertification.getId(), details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductCertification.getId());
        return flowProductCertification;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowProductCertification update(FlowProductCertification flowProductCertification, List<FlowProductCertificationDetail> details, List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowProductCertification);
        //设置更新时间
        flowProductCertification.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowProductCertificationDetailDao.deleteByBusinessId(flowProductCertification.getId());
        flowProductCertificationDetailDao.save(details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductCertification.getId());

        return flowProductCertificationDao.save(flowProductCertification);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowProductCertification saveAs(FlowProductCertification flowProductCertification, List<FlowProductCertificationDetail> details, List<Attachment> attachments){
        flowProductCertificationDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowProductCertification);
        //设置供应商信息
        setFlowVendorInfo(flowProductCertification);
        //清理信息
        cleanInfoForSaveAs(flowProductCertification);
        //保存
        flowProductCertification = flowProductCertificationDao.save(flowProductCertification);
        saveDetails(flowProductCertification.getId(), details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductCertification.getId());

        return flowProductCertification;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowProductCertificationDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowProductCertificationDetailDao.save(details);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowProductCertification flowProductCertification){
        flowProductCertification.setUpdatedAt(new Date());
        flowProductCertification.setStatus(Constants.Status.DELETED.code);
        flowProductCertificationDao.save(flowProductCertification);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowProductCertification flow = flowProductCertificationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowProductCertificationDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowProductCertification flow = flowProductCertificationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowProductCertificationDao.save(flow);
    }

    private Specification<FlowProductCertification> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowProductCertification> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowProductCertification.class);
        return spec;
    }

}
