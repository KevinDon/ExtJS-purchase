package com.newaim.purchase.flow.inspection.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDao;
import com.newaim.purchase.flow.inspection.dao.FlowComplianceApplyDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApplyDetail;
import com.newaim.purchase.flow.inspection.vo.FlowComplianceApplyVo;
import com.newaim.purchase.flow.purchase.entity.FlowNewProduct;
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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowComplianceApplyService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowComplianceApply.code;

    @Autowired
    private FlowComplianceApplyDao flowComplianceApplyDao;

    @Autowired
    private FlowComplianceApplyDetailDao flowComplianceApplyDetailDao;

    @Autowired
    private FlowComplianceApplyDetailService flowComplianceApplyDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    public Page<FlowComplianceApplyVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowComplianceApply> spec = buildSpecification(params);
        Page<FlowComplianceApply> p = flowComplianceApplyDao.findAll(spec, pageRequest);
        Page<FlowComplianceApplyVo> page = p.map(new Converter<FlowComplianceApply, FlowComplianceApplyVo>() {
            @Override
            public FlowComplianceApplyVo convert(FlowComplianceApply flowComplianceApply) {
                return convertToFlowComplianceApplyVo(flowComplianceApply);
            }
        });
        return page;
    }

    private FlowComplianceApplyVo convertToFlowComplianceApplyVo(FlowComplianceApply flowComplianceApply){
        FlowComplianceApplyVo vo = BeanMapper.map(flowComplianceApply, FlowComplianceApplyVo.class);
        return vo;
    }

    public FlowComplianceApply getFlowComplianceApply(String id){
        return flowComplianceApplyDao.findOne(id);
    }

    public FlowComplianceApplyVo get(String id){
        FlowComplianceApplyVo vo =convertToFlowComplianceApplyVo(getFlowComplianceApply(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowComplianceApplyDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowComplianceApply add(FlowComplianceApply flowComplianceApply, List<FlowComplianceApplyDetail> details,List<Attachment> attachments){
        setFlowCreatorInfo(flowComplianceApply);
        setFlowVendorInfo(flowComplianceApply);

        flowComplianceApply = flowComplianceApplyDao.save(flowComplianceApply);
        saveDetails(flowComplianceApply.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowComplianceApply.getId());
        return flowComplianceApply;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowComplianceApply update(FlowComplianceApply flowComplianceApply, List<FlowComplianceApplyDetail> details,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(flowComplianceApply);
        //设置更新时间
        flowComplianceApply.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowComplianceApplyDetailDao.deleteByBusinessId(flowComplianceApply.getId());
        flowComplianceApplyDetailDao.save(details);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowComplianceApply.getId());
        return flowComplianceApplyDao.save(flowComplianceApply);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowComplianceApply saveAs(FlowComplianceApply flowComplianceApply, List<FlowComplianceApplyDetail> details,List<Attachment> attachments){
        flowComplianceApplyDao.clear();
        //设置创建信息
        setFlowCreatorInfo(flowComplianceApply);
        //设置供应商信息
        setFlowVendorInfo(flowComplianceApply);
        //清理信息
        cleanInfoForSaveAs(flowComplianceApply);
        flowComplianceApply = flowComplianceApplyDao.save(flowComplianceApply);
        saveDetails(flowComplianceApply.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowComplianceApply.getId());
        return flowComplianceApply;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowComplianceApplyDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowComplianceApplyDetailDao.save(details);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowComplianceApply flowComplianceApply){
        flowComplianceApply.setUpdatedAt(new Date());
        flowComplianceApply.setStatus(Constants.Status.DELETED.code);
        flowComplianceApplyDao.save(flowComplianceApply);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowComplianceApply flow = flowComplianceApplyDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowComplianceApplyDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowComplianceApply flow = flowComplianceApplyDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowComplianceApplyDao.save(flow);
    }

    private Specification<FlowComplianceApply> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowComplianceApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowComplianceApply.class);
        return spec;
    }

}
