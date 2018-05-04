package com.newaim.purchase.flow.purchase.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.purchase.dao.ProductQuotationDao;
import com.newaim.purchase.archives.flow.purchase.entity.ProductQuotation;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.purchase.dao.FlowProductQuotationDao;
import com.newaim.purchase.flow.purchase.dao.FlowProductQuotationDetailDao;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotation;
import com.newaim.purchase.flow.purchase.entity.FlowProductQuotationDetail;
import com.newaim.purchase.flow.purchase.vo.FlowProductQuotationVo;
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

/**
 * @author Mark
 * @author change By Lance at 2017/10/28
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowProductQuotationService extends FlowServiceBase {

    private static final String FILE_MODULE_NAME = ConstantsAttachment.Status.FlowProductQuotation.code;

    @Autowired
    private FlowProductQuotationDao flowProductQuotationDao;

    @Autowired
    private ProductQuotationDao productQuotationDao;

    @Autowired
    private FlowProductQuotationDetailDao flowProductQuotationDetailDao;

    @Autowired
    private FlowProductQuotationDetailService flowProductQuotationDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 分页查询List
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowProductQuotationVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowProductQuotation> spec = buildSpecification(params);
        Page<FlowProductQuotation> p = flowProductQuotationDao.findAll(spec, pageRequest);
        Page<FlowProductQuotationVo> page = p.map(new Converter<FlowProductQuotation, FlowProductQuotationVo>() {
		    @Override
		    public FlowProductQuotationVo convert(FlowProductQuotation flowProductQuotation) {
		        return convertToFlowProductQuotationVo(flowProductQuotation);
		    }
		});
        return page;
    }

    /**
     * 将entity转换成Vo
     * @param flowProductQuotation
     * @return
     */
    private FlowProductQuotationVo convertToFlowProductQuotationVo(FlowProductQuotation flowProductQuotation){
        FlowProductQuotationVo vo = BeanMapper.map(flowProductQuotation, FlowProductQuotationVo.class);
        return vo;
    }

    public FlowProductQuotation getFlowProductQuotation(String id){
        return flowProductQuotationDao.findOne(id);
    }

    /**
     * 根据ID获得附件、供应商、detail信息
     * @param id
     * @return
     */
    public FlowProductQuotationVo get(String id){
        FlowProductQuotationVo vo = convertToFlowProductQuotationVo(getFlowProductQuotation(id));
        vo.setAttachments(attachmentService.listByBusinessId(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowProductQuotationDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }

    /**
     * 新建，需要设置创建人，供应商，价格信息，绑定details、附件
     * @param flowProductQuotation
     * @param details
     * @param attachments
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
	public FlowProductQuotation add(FlowProductQuotation flowProductQuotation, List<FlowProductQuotationDetail> details,List<Attachment> attachments){
        //设置采购询价创建人信息
        setFlowCreatorInfo(flowProductQuotation);
        //设置供应商信息
        setFlowVendorInfo(flowProductQuotation);
        //保存
    	flowProductQuotationDao.save(flowProductQuotation);
    	saveDetails(flowProductQuotation.getId(), details);
        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductQuotation.getId());

        return flowProductQuotation;
	}

    /**
     * 更新,details需重新绑定
     * @param flowProductQuotation
     * @param details
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowProductQuotation update(FlowProductQuotation flowProductQuotation, List<FlowProductQuotationDetail> details,List<Attachment> attachments){
        flowProductQuotationDao.clear();
        //设置供应商信息
        setFlowVendorInfo(flowProductQuotation);
        //设置更新时间
        flowProductQuotation.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowProductQuotationDetailDao.deleteByBusinessId(flowProductQuotation.getId());
        saveDetails(flowProductQuotation.getId(), details);
        //更新附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductQuotation.getId());

        return flowProductQuotationDao.save(flowProductQuotation);
    }

    /**
     * 另存
     * @param flowProductQuotation
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowProductQuotation saveAs(FlowProductQuotation flowProductQuotation, List<FlowProductQuotationDetail> details,List<Attachment> attachments){
        flowProductQuotationDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowProductQuotation);
        //设置供应商信息
        setFlowVendorInfo(flowProductQuotation);
        //清理信息
        cleanInfoForSaveAs(flowProductQuotation);
        //保存
    	flowProductQuotationDao.save(flowProductQuotation);
    	saveDetails(flowProductQuotation.getId(), details);

        //保存附件
        attachmentService.save(attachments, FILE_MODULE_NAME, flowProductQuotation.getId());
    	return flowProductQuotation;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowProductQuotationDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowProductQuotationDetailDao.save(details);
        }
    }

    /**
     * 标记为删除状态
     * @param flowProductQuotation
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowProductQuotation flowProductQuotation){
        flowProductQuotation.setUpdatedAt(new Date());
        flowProductQuotation.setStatus(Constants.Status.DELETED.code);
        flowProductQuotationDao.save(flowProductQuotation);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowProductQuotation flow = flowProductQuotationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowProductQuotationDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowProductQuotation flow = flowProductQuotationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowProductQuotationDao.save(flow);
    }


    private Specification<FlowProductQuotation> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowProductQuotation> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowProductQuotation.class);
        return spec;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowProductQuotation flowProductQuotation = flowProductQuotationDao.findOne(businessId);
        flowProductQuotation.setStatus(Constants.Status.CANCELED.code);
        flowProductQuotationDao.save(flowProductQuotation);
        List<ProductQuotation> productQuotations = productQuotationDao.findByBusinessId(businessId);
        if(productQuotations != null && productQuotations.size() > 0){
            for (int i = 0; i < productQuotations.size(); i++) {
                productQuotations.get(i).setStatus(Constants.Status.CANCELED.code);
            }
            productQuotationDao.save(productQuotations);
        }
    }
}
