package com.newaim.purchase.flow.inspection.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDao;
import com.newaim.purchase.flow.inspection.dao.FlowSampleQcDetailDao;
import com.newaim.purchase.flow.inspection.entity.FlowComplianceApply;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQc;
import com.newaim.purchase.flow.inspection.entity.FlowSampleQcDetail;
import com.newaim.purchase.flow.inspection.vo.FlowSampleQcVo;
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
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSampleQcService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowSampleQc.code;

    @Autowired
    private FlowSampleQcDao flowSampleQcDao;

    @Autowired
    private FlowSampleQcDetailDao flowSampleQcDetailDao;

    @Autowired
    private FlowSampleQcDetailService flowSampleQcDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    public Page<FlowSampleQcVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowSampleQc> spec = buildSpecification(params);
        Page<FlowSampleQc> p = flowSampleQcDao.findAll(spec, pageRequest);
        Page<FlowSampleQcVo> page = p.map(new Converter<FlowSampleQc, FlowSampleQcVo>() {
            @Override
            public FlowSampleQcVo convert(FlowSampleQc flowSampleQc) {
                return convertToFlowSampleQcVo(flowSampleQc);
            }
        });
        return page;
    }

    private FlowSampleQcVo convertToFlowSampleQcVo(FlowSampleQc flowSampleQc){
        FlowSampleQcVo vo = BeanMapper.map(flowSampleQc, FlowSampleQcVo.class);
        return vo;
    }

    public FlowSampleQc getFlowSampleQc(String id){
        return flowSampleQcDao.findOne(id);
    }

    public FlowSampleQcVo get(String id){
        FlowSampleQcVo vo =convertToFlowSampleQcVo(getFlowSampleQc(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowSampleQcDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }

    private Specification<FlowSampleQc> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowSampleQc> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowSampleQc.class);
        return spec;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowSampleQc add(FlowSampleQc flowSampleQc, List<FlowSampleQcDetail> details,List<Attachment> attachments){
        setFlowCreatorInfo(flowSampleQc);
        setFlowVendorInfo(flowSampleQc);
        flowSampleQc = flowSampleQcDao.save(flowSampleQc);
        saveDetails(flowSampleQc.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowSampleQc.getId());
        return flowSampleQc;
    }

    @Transactional(rollbackFor = Exception.class)
    public FlowSampleQc update(FlowSampleQc o, List<FlowSampleQcDetail> details,List<Attachment> attachments){
        flowSampleQcDao.clear();
        //设置供应商信息
        setFlowVendorInfo(o);
        //设置更新时间
        o.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowSampleQcDetailDao.deleteByBusinessId(o.getId());
        flowSampleQcDetailDao.save(details);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());
        return flowSampleQcDao.save(o);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSampleQc saveAs(FlowSampleQc flowSampleQc, List<FlowSampleQcDetail> details,List<Attachment> attachments){
        flowSampleQcDao.clear();
        setFlowCreatorInfo(flowSampleQc);
        setFlowVendorInfo(flowSampleQc);
        //清理信息
        cleanInfoForSaveAs(flowSampleQc);
        flowSampleQc = flowSampleQcDao.save(flowSampleQc);
        saveDetails(flowSampleQc.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowSampleQc.getId());
        return flowSampleQc;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowSampleQcDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowSampleQcDetailDao.save(details);
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowSampleQc flow = flowSampleQcDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowSampleQcDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowSampleQc flow = flowSampleQcDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowSampleQcDao.save(flow);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowSampleQc flowSampleQc){
        flowSampleQc.setUpdatedAt(new Date());
        flowSampleQc.setStatus(Constants.Status.DELETED.code);
        flowSampleQcDao.save(flowSampleQc);
    }
}
