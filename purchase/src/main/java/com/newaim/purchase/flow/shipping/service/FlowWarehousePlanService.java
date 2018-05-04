package com.newaim.purchase.flow.shipping.service;

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
import com.newaim.purchase.Constants;
import com.newaim.purchase.flow.shipping.dao.FlowWarehousePlanDao;
import com.newaim.purchase.flow.shipping.dao.FlowWarehousePlanDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlan;
import com.newaim.purchase.flow.shipping.entity.FlowWarehousePlanDetail;
import com.newaim.purchase.flow.shipping.vo.FlowWarehousePlanVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowWarehousePlanService extends FlowServiceBase {

    @Autowired
    private FlowWarehousePlanDao flowWarehousePlanDao;

    @Autowired
    private FlowWarehousePlanDetailDao flowWarehousePlanDetailDao;

    @Autowired
    private FlowWarehousePlanDetailService flowWarehousePlanDetailService;


    /**
     * 分页查询所有信息
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowWarehousePlanVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowWarehousePlan> spec = buildSpecification(params);
        Page<FlowWarehousePlan> p = flowWarehousePlanDao.findAll(spec, pageRequest);
        Page<FlowWarehousePlanVo> page = p.map(new Converter<FlowWarehousePlan, FlowWarehousePlanVo>() {
            @Override
            public FlowWarehousePlanVo convert(FlowWarehousePlan flowWarehousePlan) {
                return convertToFlowWarehousePlanVo(flowWarehousePlan);
            }
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     *
     * @param flowWarehousePlan
     * @return
     */
    private FlowWarehousePlanVo convertToFlowWarehousePlanVo(FlowWarehousePlan flowWarehousePlan) {
        FlowWarehousePlanVo vo = BeanMapper.map(flowWarehousePlan, FlowWarehousePlanVo.class);
        return vo;
    }

    public FlowWarehousePlan getFlowWarehousePlan(String id) {
        return flowWarehousePlanDao.findOne(id);
    }

    /**
     * 根据ID获取主表和details信息
     *
     * @param id
     * @return
     */
    public FlowWarehousePlanVo get(String id) {
        FlowWarehousePlanVo vo = convertToFlowWarehousePlanVo(getFlowWarehousePlan(id));
        vo.setDetails(flowWarehousePlanDetailService.findDetailsVoByBusinessId(id));
        if(vo.getDetails() != null && vo.getDetails().size() > 0){
            vo.setPackingOrders(flowWarehousePlanDetailService.findDetailsVoByContainerNumber(vo.getDetails().get(0).getContainerNumber()));
        }
        return vo;
    }

    private Specification<FlowWarehousePlan> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowWarehousePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowWarehousePlan.class);
        return spec;
    }

    /**
     * 新建，设置创建人信息、绑定details数据
     *
     * @param flowWarehousePlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowWarehousePlan add(FlowWarehousePlan flowWarehousePlan, List<FlowWarehousePlanDetail> details) {
        //設置創建人信息
        setFlowCreatorInfo(flowWarehousePlan);
        //设置仓库信息
        flowWarehousePlan = flowWarehousePlanDao.save(flowWarehousePlan);
        saveDetails(flowWarehousePlan.getId(), details);
        return flowWarehousePlan;
    }

    /**
     * 更新，设置更新时间、处理人信息，重新绑定details数据
     *
     * @param flowWarehousePlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowWarehousePlan update(FlowWarehousePlan flowWarehousePlan, List<FlowWarehousePlanDetail> details) {
        //设置更新时间
        flowWarehousePlan.setUpdatedAt(new Date());
        //设置仓库信息
        //删除之前明细，重新绑定数据
        flowWarehousePlanDetailDao.deleteByBusinessId(flowWarehousePlan.getId());
        saveDetails(flowWarehousePlan.getId(),details);
        return flowWarehousePlanDao.save(flowWarehousePlan);
    }

    /**
     * 复制另存，初始化数据，设置创建人信息，处理人、绑定details数据
     *
     * @param flowWarehousePlan
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowWarehousePlan saveAs(FlowWarehousePlan flowWarehousePlan, List<FlowWarehousePlanDetail> details) {
        flowWarehousePlanDao.clear();
        //設置創建人信息
        setFlowCreatorInfo(flowWarehousePlan);
        //设置仓库信息
        //清理信息
        cleanInfoForSaveAs(flowWarehousePlan);
        flowWarehousePlan = flowWarehousePlanDao.save(flowWarehousePlan);
        saveDetails(flowWarehousePlan.getId(), details);

        return flowWarehousePlan;
    }

    /**
     * 更新流程状态
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowWarehousePlan updateFlowCompleteStatus(String id) {
        FlowWarehousePlan flowWarehousePlan = getFlowWarehousePlan(id);
        flowWarehousePlan.setFlowStatus(Constants.FlowStatus.PASS.code);
        flowWarehousePlan.setStatus(Constants.Status.NORMAL.code);
        return flowWarehousePlanDao.save(flowWarehousePlan);
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(String businessId, List<FlowWarehousePlanDetail> details) {
    	List<String> titleList= Lists.newArrayList();
    	List<String> numberList= Lists.newArrayList();
        if (details != null) {
            for (int i = 0; i < details.size(); i++) {
                details.get(i).setBusinessId(businessId);
                titleList.add(details.get(i).getOrderTitle());
                numberList.add(details.get(i).getOrderNumber());
            }
            flowWarehousePlanDetailDao.save(details);
        }
        FlowWarehousePlan flowWarehousePlan = flowWarehousePlanDao.getOne(businessId);
        String titless = StringUtils.join(titleList.toArray(), ",");  
        String numbers = StringUtils.join(numberList.toArray(), ",");  
        flowWarehousePlan.setOrderTitle(titless);
        flowWarehousePlan.setOrderNumber(numbers);
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowWarehousePlan flow = flowWarehousePlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowWarehousePlanDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowWarehousePlan flow = flowWarehousePlanDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowWarehousePlanDao.save(flow);
    }

    /**
     * 刪除（改变状态并非真正删除）
     * @param flowWarehousePlan
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowWarehousePlan flowWarehousePlan){
        flowWarehousePlan.setUpdatedAt(new Date());
        flowWarehousePlan.setStatus(Constants.Status.DELETED.code);
        flowWarehousePlanDao.save(flowWarehousePlan);
    }

}
