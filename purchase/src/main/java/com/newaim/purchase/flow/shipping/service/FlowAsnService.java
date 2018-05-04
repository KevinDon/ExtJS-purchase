package com.newaim.purchase.flow.shipping.service;

import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePacking;
import com.newaim.purchase.archives.flow.purchase.entity.CustomClearancePackingDetail;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingDetailService;
import com.newaim.purchase.archives.flow.purchase.service.CustomClearancePackingService;
import com.newaim.purchase.archives.flow.purchase.service.PurchaseContractService;
import com.newaim.purchase.archives.flow.purchase.vo.CustomClearancePackingVo;
import com.newaim.purchase.archives.flow.shipping.dao.AsnDao;
import com.newaim.purchase.archives.flow.shipping.entity.Asn;
import com.newaim.purchase.archives.flow.shipping.entity.WarehousePlan;
import com.newaim.purchase.archives.flow.shipping.service.WarehousePlanService;
import com.newaim.purchase.flow.shipping.dao.FlowAsnDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPacking;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import com.newaim.purchase.flow.shipping.vo.FlowAsnPackingVo;
import com.newaim.purchase.flow.shipping.vo.FlowAsnVo;
import com.newaim.purchase.flow.workflow.service.FlowServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowAsnService extends FlowServiceBase {

    @Autowired
    private FlowAsnDao flowAsnDao;

    @Autowired
    private AsnDao asnDao;

    @Autowired
    private FlowAsnPackingDetailDao flowAsnPackingDetailDao;

    @Autowired
    private CustomClearancePackingDetailService customClearancePackingDetailService;

    @Autowired
    private WarehousePlanService warehousePlanService;

    @Autowired
    private FlowAsnPackingDao flowAsnPackingDao;

    @Autowired
    private FlowAsnPackingService flowAsnPackingService;

    @Autowired
    private WmsApiService wmsApiService;

    @Autowired
    private CustomClearancePackingService customClearancePackingService;

    @Autowired
    private PurchaseContractService purchaseContractService;

    /**
     * 分页查询所有信息
     *
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<FlowAsnVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowAsn> spec = buildSpecification(params);
        Page<FlowAsn> p = flowAsnDao.findAll(spec, pageRequest);
        Page<FlowAsnVo> page = p.map(flowAsn ->{
            FlowAsnVo vo = convertToFlowAsnVo(flowAsn);
            vo.setDetails(flowAsnPackingService.findPackingsVoByBusinessId(vo.getId()));
            return vo;
        });
        return page;
    }

    /**
     * 将entity转换为Vo
     *
     * @param flowAsn
     * @return
     */
    private FlowAsnVo convertToFlowAsnVo(FlowAsn flowAsn) {
        FlowAsnVo vo = BeanMapper.map(flowAsn, FlowAsnVo.class);
        return vo;
    }

    public FlowAsn getFlowAsn(String id) {
        return flowAsnDao.findOne(id);
    }

    /**
     * 根据ID获取主表和details信息
     *
     * @param id
     * @return
     */
    public FlowAsnVo get(String id) {
        FlowAsnVo vo = convertToFlowAsnVo(getFlowAsn(id));
        vo.setDetails(flowAsnPackingService.findPackingsVoByBusinessId(id));
        //根据送仓计划ID查送仓计划信息
        WarehousePlan warehousePlan = warehousePlanService.getWarehousePlan(vo.getWarehousePlanId());
        //从送仓计划获取起始、目的地点
        vo.setOriginPlace(warehousePlan.getOriginPlace());
        vo.setDestinationPlace(warehousePlan.getDestinationPlace());
        List<FlowAsnPackingVo> details = vo.getDetails();
        //根据柜编号查找清关的柜信息
        if (details !=null){
            for (int i = 0;i<details.size();i++){
                FlowAsnPackingVo asnPacking= details.get(i);
                CustomClearancePackingVo packing =customClearancePackingService.get(asnPacking.getCcPackingId());
                //设置集装箱编码、封印、货柜类型
                asnPacking.setSealsNumber(packing.getSealsNumber());
                asnPacking.setContainerType(packing.getContainerType());
                asnPacking.setContainerNumber(packing.getContainerNumber());
            }
        }
        return vo;
    }

    private Specification<FlowAsn> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowAsn> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowAsn.class);
        return spec;
    }

    /**
     * 判断是否已有asnNumber
     * @param asnNumber
     * @return
     */
    public boolean existsAsnNumber(String asnNumber){
        List<FlowAsn> flowAsns = flowAsnDao.findByAsnNumber(asnNumber);
        if(flowAsns != null && flowAsns.size() > 0){
            return true;
        }else{
            List<Asn> asns = asnDao.findByAsnNumber(asnNumber);
            if(asns != null && asns.size() > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 新建，设置创建人信息、绑定details数据
     *
     * @param flowAsn
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowAsn add(FlowAsn flowAsn, List<FlowAsnPacking> details) {
        //設置創建人信息
        setFlowAsnCreatorInfo(flowAsn);
        //设置订单信息
        PurchaseContract order = purchaseContractService.getPurchaseContract(flowAsn.getOrderId());
        flowAsn.setOrderNumber(order.getOrderNumber());
        flowAsn.setOrderTitle(order.getOrderTitle());
        flowAsn = flowAsnDao.save(flowAsn);
        saveDetails(flowAsn, details);
        return flowAsn;
    }

    /**
     * 更新，设置更新时间、处理人信息，重新绑定details数据
     * @param flowAsn
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowAsn update(FlowAsn flowAsn, List<FlowAsnPacking> details) {
        //设置更新时间
        flowAsn.setUpdatedAt(new Date());
        //设置订单信息
        PurchaseContract order = purchaseContractService.getPurchaseContract(flowAsn.getOrderId());
        flowAsn.setOrderNumber(order.getOrderNumber());
        flowAsn.setOrderTitle(order.getOrderTitle());

        //先删除packingList明细
        List<FlowAsnPackingVo> packings = flowAsnPackingService.findPackingsVoByBusinessId(flowAsn.getId());
        if (packings != null){
            for (int i = 0;i<packings.size();i++){
                FlowAsnPackingVo packingVo = packings.get(i);
                flowAsnPackingDetailDao.deleteByAsnPackingId(packingVo.getId());
            }
        }
        //再删detail明细
        flowAsnPackingDao.deleteByBusinessId(flowAsn.getId());
        saveDetails(flowAsn,details);
        //flowAsn.setFlagSyncStatus(2);
        return flowAsnDao.save(flowAsn);
    }

    /**
     * 复制另存，初始化数据，设置创建人信息，处理人、绑定details数据
     *
     * @param flowAsn
     * @param details
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowAsn saveAs(FlowAsn flowAsn, List<FlowAsnPacking> details) {
        flowAsnDao.clear();
        //設置創建人信息
        setFlowAsnCreatorInfo(flowAsn);
        //清理信息
        cleanInfoForSaveAs(flowAsn);
        //设置订单信息
        PurchaseContract order = purchaseContractService.getPurchaseContract(flowAsn.getOrderId());
        flowAsn.setOrderNumber(order.getOrderNumber());
        flowAsn.setOrderTitle(order.getOrderTitle());
        //flowAsn.setFlagSyncStatus(1);
        flowAsn = flowAsnDao.save(flowAsn);

        saveDetails(flowAsn, details);
        return flowAsn;
    }


    /**
     * 更新流程状态
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowAsn updateFlowCompleteStatus(String id) {
        FlowAsn flowAsn = getFlowAsn(id);
        flowAsn.setFlowStatus(Constants.FlowStatus.PASS.code);
        flowAsn.setStatus(Constants.Status.NORMAL.code);
        return flowAsnDao.save(flowAsn);
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(FlowAsn flowAsn, List<FlowAsnPacking> details) {
        if (details != null){
            for (int i = 0;i<details.size();i++){
                FlowAsnPacking packing = details.get(i);
                //设置装柜单的流水ID
                packing.setBusinessId(flowAsn.getId());
                packing.setOrderId(flowAsn.getOrderId());
                packing.setOrderNumber(flowAsn.getOrderNumber());
                packing.setOrderTitle(flowAsn.getOrderTitle());
                flowAsnPackingDao.save(packing);
                //grady
                CustomClearancePacking ccPacking = customClearancePackingService.findOne(packing.getCcPackingId());
                ccPacking.setFlagAsnStatus(1);
                customClearancePackingService.save(ccPacking);
                flowAsn.setContainerNumber(ccPacking.getContainerNumber());
                flowAsn.setSealsNumber(ccPacking.getSealsNumber());
                flowAsn.setContainerType(ccPacking.getContainerType());
                flowAsnDao.save(flowAsn);
                
                for (int j = 0;j<packing.getPackingDetails().size();j++){
                    FlowAsnPackingDetail packingDetail = packing.getPackingDetails().get(j);
                    CustomClearancePackingDetail customClearancePackingDetail = customClearancePackingDetailService.findPackingDetailByPackingIdAndProductId(packing.getCcPackingId(), packingDetail.getProductId());
                    if(customClearancePackingDetail != null){
                        packingDetail.setCcPackingDetailId(customClearancePackingDetail.getId());
                    }
                    packingDetail.setAsnPackingId(packing.getId());
                    packingDetail.setBusinessId(flowAsn.getId());
                    packingDetail.setOrderId(packing.getOrderId());
                    packingDetail.setOrderNumber(packing.getOrderNumber());
                    flowAsnPackingDetailDao.save(packingDetail);
                }
            }
        }
    }

    /**
     * 设置创建人相关信息
     */
    private void setFlowAsnCreatorInfo(FlowAsn flowAsn) {
        UserVo user = SessionUtils.currentUserVo();
        //设置创建时间、状态
        flowAsn.setCreatedAt(new Date());
        flowAsn.setStatus(Constants.Status.DRAFT.code);
        flowAsn.setHold(Constants.HoldStatus.UN_HOLD.code);
        //设置创建人信息
        flowAsn.setCreatorId(user.getId());
        flowAsn.setCreatorCnName(user.getCnName());
        flowAsn.setCreatorEnName(user.getEnName());
        //设置创建部门信息
        flowAsn.setDepartmentId(user.getDepartmentId());
        flowAsn.setDepartmentCnName(user.getDepartmentCnName());
        flowAsn.setDepartmentEnName(user.getDepartmentEnName());
    }

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowAsn flow = flowAsnDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowAsnDao.save(flow);
    }
    /**
     * 保存
     * @param FlowAsn flow
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(FlowAsn flow){
    	flowAsnDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowAsn flow = flowAsnDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowAsnDao.save(flow);
    }

    /**
     * 刪除（改变状态并非真正删除）
     * @param flowAsn
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FlowAsn flowAsn){
        flowAsn.setUpdatedAt(new Date());
        flowAsn.setStatus(Constants.Status.DELETED.code);
        flowAsnDao.save(flowAsn);
        
        //grady
        List<FlowAsnPacking> asnPackings = flowAsnPackingService.findPackingsByBusinessId(flowAsn.getId());
        FlowAsnPacking asnPacking = asnPackings.get(0);
        CustomClearancePacking ccPacking = customClearancePackingService.findOne(asnPacking.getCcPackingId());
        ccPacking.setFlagAsnStatus(2);
        customClearancePackingService.save(ccPacking);
        
/*        try {
            wmsApiService.cancelAsn(flowAsn.getAsnNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    
    public List<FlowAsn> findPassFlowAsnByOrderId(String orderId){
    	return flowAsnDao.findPassFlowAsnByOrderId(orderId);
    }

}
