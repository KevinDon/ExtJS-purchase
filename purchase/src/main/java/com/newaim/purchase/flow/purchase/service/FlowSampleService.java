package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowSampleDao;
import com.newaim.purchase.flow.purchase.dao.FlowSampleDetailDao;
import com.newaim.purchase.flow.purchase.dao.FlowSampleOtherDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowSample;
import com.newaim.purchase.flow.purchase.entity.FlowSampleDetail;
import com.newaim.purchase.flow.purchase.entity.FlowSampleOtherDetail;
import com.newaim.purchase.flow.purchase.vo.FlowSampleVo;
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
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowSampleService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowSample.code;

     @Autowired
     private FlowSampleDao flowSampleDao;

     @Autowired
     private FlowSampleDetailDao flowSampleDetailDao;

     @Autowired
     private FlowSampleOtherDetailDao flowSampleOtherDetailDao;

     @Autowired
     private VendorService vendorService;

     @Autowired
     private AttachmentService attachmentService;

     @Autowired
     private FlowSampleDetailService flowSampleDetailService;

     @Autowired
     private FlowSampleOtherDetailService flowSampleOtherDetailService;

    /**
     * 分页查询List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowSampleVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowSample> spec = buildSpecification(params);
        Page<FlowSample> p = flowSampleDao.findAll(spec, pageRequest);
        Page<FlowSampleVo> page = p.map(new Converter<FlowSample, FlowSampleVo>() {
            @Override
            public FlowSampleVo convert(FlowSample flowSample) {
                return convertToFlowSampleVo(flowSample);
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     * @param flowSample
     * @return
     */
    private FlowSampleVo convertToFlowSampleVo(FlowSample flowSample){
        FlowSampleVo vo = BeanMapper.map(flowSample, FlowSampleVo.class);
        return vo;
    }

    public FlowSample getFlowSample(String id){
        return flowSampleDao.findOne(id);
    }

    /**
     * 根据ID获得附件、供应商、details信息
     * @param id
     * @return
     */
    public FlowSampleVo get(String id){
        FlowSampleVo vo =convertToFlowSampleVo(getFlowSample(id));
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowSampleDetailService.findDetailsVoByBusinessId(id));
        vo.setOtherDetails(flowSampleOtherDetailService.findOtherDetailsVoByBusinessId(id));
        return vo;
    }

    private Specification<FlowSample> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowSample> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowSample.class);
        return spec;
    }

    /**
     * 新建，需要设置创建人信息、供应商、价格、附件
     * @param flowSample
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSample add(FlowSample flowSample, List<FlowSampleDetail> details, List<FlowSampleOtherDetail> otherDetails, List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowSample);

        //设置采购员信息
        flowSample.setBuyerId(flowSample.getCreatorId());
        flowSample.setBuyerCnName(flowSample.getCreatorCnName());
        flowSample.setBuyerEnName(flowSample.getCreatorEnName());

        //设置供应商信息
        setFlowVendorInfo(flowSample);
        flowSample = flowSampleDao.save(flowSample);
        saveDetails(flowSample.getId(), details, otherDetails);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowSample.getId());
        return flowSample;
    }

    /**
     * 更新，重新绑定details
     * @param o
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSample update(FlowSample o, List<FlowSampleDetail> details, List<FlowSampleOtherDetail> otherDetails,List<Attachment> attachments){
        //设置供应商信息
        setFlowVendorInfo(o);
        //设置更新时间
        o.setUpdatedAt(new Date());

        //删除之前明细，重新绑定数据
        flowSampleDetailDao.deleteByBusinessId(o.getId());
        flowSampleOtherDetailDao.deleteByBusinessId(o.getId());
        saveDetails(o.getId(),details, otherDetails);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, o.getId());
        return flowSampleDao.save(o);
    }

    /**
     * 更新流程状态
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSample updateFlowCompleteStatus(String id){
        FlowSample flowSample = getFlowSample(id);
        flowSample.setFlowStatus(Constants.FlowStatus.PASS.code);
        flowSample.setStatus(Constants.Status.NORMAL.code);
        return flowSampleDao.save(flowSample);
    }


    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowSample saveAs(FlowSample flowSample, List<FlowSampleDetail> details, List<FlowSampleOtherDetail> otherDetails,List<Attachment> attachments){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setId(null);
                details.get(i).setBusinessId(null);
            }
        }
        if(otherDetails != null){
            for (int i = 0; i < otherDetails.size(); i++) {
                otherDetails.get(i).setId(null);
                otherDetails.get(i).setId(null);
            }
        }
        if(attachments != null){
            for (int i = 0; i <attachments.size(); i++) {
                attachments.get(i).setId(null);
                attachments.get(i).setBusinessId(null);
            }
        }
        flowSampleDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowSample);

        //设置采购员信息
        flowSample.setBuyerId(flowSample.getCreatorId());
        flowSample.setBuyerCnName(flowSample.getCreatorCnName());
        flowSample.setBuyerEnName(flowSample.getCreatorEnName());

        //设置供应商信息
        setFlowVendorInfo(flowSample);
        //清理信息
        cleanInfoForSaveAs(flowSample);
        //保存
        flowSample = flowSampleDao.save(flowSample);
        //保存明细
        saveDetails(flowSample.getId(), details, otherDetails);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowSample.getId());
        return flowSample;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowSampleDetail> details, List<FlowSampleOtherDetail> otherDetails){
        if(details != null && details.size() > 0){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowSampleDetailDao.save(details);
        }
        if(otherDetails != null && otherDetails.size() > 0){
            for (int i = 0; i < otherDetails.size(); i++) {
                otherDetails.get(i).setBusinessId(businessId);
            }
            flowSampleOtherDetailDao.save(otherDetails);
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowSample flow = flowSampleDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowSampleDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowSample flow = flowSampleDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowSampleDao.save(flow);
    }


    /**
     * 标记为删除状态，并非物流删除
     * @param flowSample
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowSample flowSample){
        flowSample.setUpdatedAt(new Date());
        flowSample.setStatus(Constants.Status.DELETED.code);
        flowSampleDao.save(flowSample);
    }

}
