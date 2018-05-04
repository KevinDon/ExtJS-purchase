package com.newaim.purchase.flow.finance.service;


import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.archives.flow.finance.dao.BalanceRefundDao;
import com.newaim.purchase.archives.flow.finance.entity.BalanceRefund;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.vendor.service.VendorService;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDao;
import com.newaim.purchase.flow.finance.dao.FlowBalanceRefundDetailDao;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefund;
import com.newaim.purchase.flow.finance.entity.FlowBalanceRefundDetail;
import com.newaim.purchase.flow.finance.vo.FlowBalanceRefundVo;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowBalanceRefundService extends FlowServiceBase {

    @Autowired
    private FlowBalanceRefundDao flowBalanceRefundDao;

    @Autowired
    private FlowBalanceRefundDetailDao flowBalanceRefundDetailDao;

    @Autowired
    private FlowBalanceRefundDetailService flowBalanceRefundDetailService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BalanceRefundDao balanceRefundDao;

    @Autowired
    private PurchaseContractDao purchaseContractDao;


    /**
     * 分页查询
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowBalanceRefundVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowBalanceRefund> spec = buildSpecification(params);
        Page<FlowBalanceRefund> p = flowBalanceRefundDao.findAll(spec, pageRequest);
        Page<FlowBalanceRefundVo> page = p.map(new Converter<FlowBalanceRefund, FlowBalanceRefundVo>() {
            @Override
            public FlowBalanceRefundVo convert(FlowBalanceRefund flowBalanceRefund) {
                return convertToFlowBalanceRefundVo(flowBalanceRefund);
            }
        });
        return page;
    }

    /**
     * 转换实体类为Vo
     * @param flowBalanceRefund
     * @return
     */
    private FlowBalanceRefundVo convertToFlowBalanceRefundVo(FlowBalanceRefund flowBalanceRefund){
        FlowBalanceRefundVo vo = BeanMapper.map(flowBalanceRefund, FlowBalanceRefundVo.class);
        return vo;
    }

    private Specification<FlowBalanceRefund> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowBalanceRefund> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowBalanceRefund.class);
        return spec;
    }

    public FlowBalanceRefund getFlowBalanceRefund(String id){
        return flowBalanceRefundDao.findOne(id);
    }

    /**
     * 获取差额退款信息
     * @param id
     * @return
     */
    public FlowBalanceRefundVo get(String id){
        FlowBalanceRefundVo vo =convertToFlowBalanceRefundVo(getFlowBalanceRefund(id));
        if(StringUtils.isNotBlank(vo.getVendorId())){
            vo.setVendor(vendorService.get(vo.getVendorId()));
        }
        vo.setDetails(flowBalanceRefundDetailService.findDetailsVoByBusinessId(id));
        return vo;
    }

    /**
     * 新建，设置创建人，供应商，价格，订单信息
     * @param flowBalanceRefund
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBalanceRefund add(FlowBalanceRefund flowBalanceRefund, List<FlowBalanceRefundDetail> details){
        //设置创建人信息
        setFlowCreatorInfo(flowBalanceRefund);
        //设置扣款状态
        flowBalanceRefund.setChargebackStatus(2);
        //设置供应商信息
        setFlowVendorInfo(flowBalanceRefund);
        //设置差额金额信息
        setFlowBalanceRefundPriceInfo(flowBalanceRefund, details);
        flowBalanceRefund = flowBalanceRefundDao.save(flowBalanceRefund);
        saveDetails(flowBalanceRefund.getId(), details);
        return flowBalanceRefund;
    }
    /**
     * 新建，设置创建人，供应商，价格，订单信息,用于收货通知流程结束后的监听器触发新建差额退款
     * @param flowBalanceRefund
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBalanceRefund addForAsn(FlowBalanceRefund flowBalanceRefund, List<FlowBalanceRefundDetail> details){
    	//设置创建人信息
    	setFlowCreatorInfo(flowBalanceRefund);
    	//设置扣款状态
    	flowBalanceRefund.setChargebackStatus(2);
    	//设置供应商信息
    	setFlowVendorInfo(flowBalanceRefund);
    	flowBalanceRefund = flowBalanceRefundDao.save(flowBalanceRefund);
    	saveDetails(flowBalanceRefund.getId(), details);
    	return flowBalanceRefund;
    }

    /**
     * 更新，设置创建人，供应商，价格，订单信息
     * @param flowBalanceRefund
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBalanceRefund update(FlowBalanceRefund flowBalanceRefund, List<FlowBalanceRefundDetail> details){
        //设置供应商信息
        setFlowVendorInfo(flowBalanceRefund);
        //设置扣款状态
        flowBalanceRefund.setChargebackStatus(2);
        //设置差额金额信息
        setFlowBalanceRefundPriceInfo(flowBalanceRefund, details);
        //设置更新时间
        flowBalanceRefund.setUpdatedAt(new Date());
        //删除之前明细，重新绑定数据
        flowBalanceRefundDetailDao.deleteByBusinessId(flowBalanceRefund.getId());
        saveDetails(flowBalanceRefund.getId(), details);
        return flowBalanceRefundDao.save(flowBalanceRefund);
    }

    /**
     * 复制另存，设置创建人，供应商，价格，订单信息
     * @param flowBalanceRefund
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowBalanceRefund saveAs(FlowBalanceRefund flowBalanceRefund, List<FlowBalanceRefundDetail> details){
        flowBalanceRefundDao.clear();
        //设置创建人信息
        setFlowCreatorInfo(flowBalanceRefund);
        //设置扣款状态
        flowBalanceRefund.setChargebackStatus(2);
        //设置供应商信息
        setFlowVendorInfo(flowBalanceRefund);
        //设置费用信息
        setFlowBalanceRefundPriceInfo(flowBalanceRefund, details);
        //清理信息
        cleanInfoForSaveAs(flowBalanceRefund);
        flowBalanceRefund = flowBalanceRefundDao.save(flowBalanceRefund);
        saveDetails(flowBalanceRefund.getId(), details);
        return flowBalanceRefund;
    }

    /**
     * 设置差额金额信息
     * @param flowBalanceRefund
     * @param details
     */
    private void setFlowBalanceRefundPriceInfo(FlowBalanceRefund flowBalanceRefund,List<FlowBalanceRefundDetail> details) {
        BigDecimal totalFeeAud = BigDecimal.ZERO;
        BigDecimal totalFeeRmb = BigDecimal.ZERO;
        BigDecimal totalFeeUsd = BigDecimal.ZERO;
        if (details != null) {
            for (FlowBalanceRefundDetail detail: details) {
                if (detail.getPriceAud() != null) {
                    totalFeeAud = totalFeeAud.add(detail.getDiffAud());
                }
                if (detail.getPriceRmb() != null) {
                    totalFeeRmb = totalFeeRmb.add(detail.getDiffRmb());
                }
                if (detail.getPriceUsd() != null) {
                    totalFeeUsd = totalFeeUsd.add(detail.getDiffUsd());
                }
            }
        }
        flowBalanceRefund.setTotalFeeAud(totalFeeAud);
        flowBalanceRefund.setTotalFeeRmb(totalFeeRmb);
        flowBalanceRefund.setTotalFeeUsd(totalFeeUsd);
    }

    /**
     * 标记为删除状态
     * @param flowBalanceRefund
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowBalanceRefund flowBalanceRefund){
        flowBalanceRefund.setUpdatedAt(new Date());
        flowBalanceRefund.setStatus(Constants.Status.DELETED.code);
        flowBalanceRefundDao.save(flowBalanceRefund);
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowBalanceRefundDetail> details){
        if(details != null){
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
            }
            flowBalanceRefundDetailDao.save(details);
        }
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowBalanceRefund flow = flowBalanceRefundDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowBalanceRefundDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowBalanceRefund flow = flowBalanceRefundDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowBalanceRefundDao.save(flow);
    }
    /**
     * 作废
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String flowId) {
        FlowBalanceRefund flowBalanceRefund = flowBalanceRefundDao.findOne(flowId);
        flowBalanceRefund.setStatus(Constants.Status.CANCELED.code);
        flowBalanceRefundDao.save(flowBalanceRefund);
        List<BalanceRefund> balanceRefunds = balanceRefundDao.findByBusinessId(flowId);
        if (balanceRefunds != null && balanceRefunds.size() > 0) {
            for (int i = 0; i < balanceRefunds.size(); i++) {
                BalanceRefund balanceRefund = balanceRefunds.get(i);
                balanceRefund.setStatus(Constants.Status.CANCELED.code);
                balanceRefundDao.save(balanceRefund);
                if (balanceRefund.getType() == 1) {
                    //青空订单标记位
                    PurchaseContract order = purchaseContractDao.findOne(balanceRefund.getOrderId());
                    if (order != null) {
                        order.setFlagBalanceRefundId(null);
                        order.setFlagBalanceRefundStatus(2);
                        order.setFlagBalanceRefundTime(null);
                        purchaseContractDao.save(order);
                    }
                }
            }
        }
    }

}
