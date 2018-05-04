package com.newaim.purchase.flow.shipping.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.entity.Attachment;
import com.newaim.purchase.admin.system.service.AttachmentService;
import com.newaim.purchase.archives.flow.shipping.dao.ServiceProviderQuotationDao;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotation;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationChargeItem;
import com.newaim.purchase.archives.flow.shipping.entity.ServiceProviderQuotationPort;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationChargeItemService;
import com.newaim.purchase.archives.flow.shipping.service.ServiceProviderQuotationPortService;
import com.newaim.purchase.constants.ConstantsAttachment;
import com.newaim.purchase.flow.shipping.dao.FlowServiceProviderQuotationChargeItemDao;
import com.newaim.purchase.flow.shipping.dao.FlowServiceProviderQuotationDao;
import com.newaim.purchase.flow.shipping.dao.FlowServiceProviderQuotationPortDao;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotation;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationChargeItem;
import com.newaim.purchase.flow.shipping.entity.FlowServiceProviderQuotationPort;
import com.newaim.purchase.flow.shipping.vo.FlowServiceProviderQuotationChargeItemVo;
import com.newaim.purchase.flow.shipping.vo.FlowServiceProviderQuotationPortVo;
import com.newaim.purchase.flow.shipping.vo.FlowServiceProviderQuotationVo;
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

/**
 * @author Mark
 * @date 2017/9/18
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FlowServiceProviderQuotationService extends FlowServiceBase {

    @Autowired
    private FlowServiceProviderQuotationDao flowServiceProviderQuotationDao;

    @Autowired
    private ServiceProviderQuotationDao serviceProviderQuotationDao;

    @Autowired
    private FlowServiceProviderQuotationPortDao flowServiceProviderQuotationPortDao;

    @Autowired
    private FlowServiceProviderQuotationPortService flowServiceProviderQuotationPortService;

    @Autowired
    private FlowServiceProviderQuotationChargeItemDao flowServiceProviderQuotationChargeItemDao;

    @Autowired
    private FlowServiceProviderQuotationChargeItemService flowServiceProviderQuotationChargeItemService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ServiceProviderQuotationPortService serviceProviderQuotationPortService;

    @Autowired
    private ServiceProviderQuotationChargeItemService serviceProviderQuotationChargeItemService;


    public Page<FlowServiceProviderQuotationVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<FlowServiceProviderQuotation> spec = buildSpecification(params);
        Page<FlowServiceProviderQuotation> p = flowServiceProviderQuotationDao.findAll(spec, pageRequest);
        Page<FlowServiceProviderQuotationVo> page = p.map(new Converter<FlowServiceProviderQuotation, FlowServiceProviderQuotationVo>() {
		    @Override
		    public FlowServiceProviderQuotationVo convert(FlowServiceProviderQuotation flowServiceProviderQuotation) {
		        return convertToFlowServiceProviderQuotationVo(flowServiceProviderQuotation);
		    }
		});
        return page;
    }

    private FlowServiceProviderQuotationVo convertToFlowServiceProviderQuotationVo(FlowServiceProviderQuotation flowServiceProviderQuotation){
        FlowServiceProviderQuotationVo vo = BeanMapper.map(flowServiceProviderQuotation, FlowServiceProviderQuotationVo.class);
        return vo;
    }

    public FlowServiceProviderQuotation getFlowServiceProviderQuotation(String id){
        return flowServiceProviderQuotationDao.findOne(id);
    }

    public FlowServiceProviderQuotationVo get(String id){
        FlowServiceProviderQuotationVo vo =convertToFlowServiceProviderQuotationVo(getFlowServiceProviderQuotation(id));
        if(StringUtils.isNotBlank(vo.getServiceProviderId())){
            vo.setServiceProvider(serviceProviderService.get(vo.getServiceProviderId()));
        }
        vo.setAttachments(attachmentService.listByBusinessId(id) );
        vo.setPorts(flowServiceProviderQuotationPortService.findPostsVoByBusinessId(id));
        vo.setChargeItems(flowServiceProviderQuotationChargeItemService.findChargeItemsVoByBusinessId(id));
//        if(StringUtils.equals(vo.getType(), "3")){
//            //订单
//            getLatestPriceTemplate(vo, vo.getServiceProviderId(), vo.getType(), vo.getFlowOrderShippingPlanId());
//        }else{
//            getLatestPriceTemplate(vo, vo.getServiceProviderId(), vo.getType());
//        }
        return vo;
    }

    public FlowServiceProviderQuotationVo getPriceTemplate(String serviceProviderId, String type, String orderShippingPlanId){
        if(StringUtils.equals(type, "3")){
            //订单
            return getLatestPriceTemplate(new FlowServiceProviderQuotationVo(), serviceProviderId, type, orderShippingPlanId);
        }else {
            return getLatestPriceTemplate(new FlowServiceProviderQuotationVo(), serviceProviderId, type);
        }
    }


    /**
     *
     * @param vo
     * @param serviceProviderId
     * @param type
     * @param flowOrderShippingPlanId
     * @return
     */
    private FlowServiceProviderQuotationVo getLatestPriceTemplate(FlowServiceProviderQuotationVo vo, String serviceProviderId, String type, String flowOrderShippingPlanId){
        // 港口
        List<ServiceProviderQuotationPort> ports = serviceProviderQuotationPortService.listPorts(serviceProviderId, type);
        List<FlowServiceProviderQuotationPortVo> portVos = vo.getPorts();
        if(StringUtils.isBlank(vo.getId())){
            //新增时
            portVos = flowServiceProviderQuotationPortService.findPortVosForQuotation(serviceProviderId, flowOrderShippingPlanId);
        }
        BigDecimal totalGp20Qty = BigDecimal.ZERO;
        BigDecimal totalGp40Qty = BigDecimal.ZERO;
        BigDecimal totalHq40Qty = BigDecimal.ZERO;
        BigDecimal totalLclCbm = BigDecimal.ZERO;
        if(portVos != null){
            for (int i = 0; i < portVos.size(); i++) {
                FlowServiceProviderQuotationPortVo portVo = portVos.get(i);
                for(ServiceProviderQuotationPort port: ports){
                    if(port.getOriginPortId().equals(portVo.getOriginPortId())){
                        totalGp20Qty = totalGp20Qty.add(portVo.getGp20Qty());
                        totalGp40Qty = totalGp40Qty.add(portVo.getGp40Qty());
                        totalHq40Qty = totalHq40Qty.add(portVo.getHq40Qty());
                        totalLclCbm = totalLclCbm.add(portVo.getLclCbm());
                        setPortVoPrevPrice(portVo, port);
                        break;
                    }
                }
            }
        }
        vo.setPorts(portVos);
        // 收费项目
        List<ServiceProviderQuotationChargeItem> chargeItems = serviceProviderQuotationChargeItemService.listChargeItems(serviceProviderId, type);
        List<FlowServiceProviderQuotationChargeItemVo> chargeItemVos = Lists.newArrayList();
        if(chargeItems != null){
            for (int i = 0; i < chargeItems.size(); i++) {
                ServiceProviderQuotationChargeItem chargeItem = chargeItems.get(i);
                FlowServiceProviderQuotationChargeItemVo chargeItemVo = new FlowServiceProviderQuotationChargeItemVo();
                if(StringUtils.isNotBlank(vo.getId())){
                    //编辑时
                    List<FlowServiceProviderQuotationChargeItemVo> temp = vo.getChargeItems();
                    for(FlowServiceProviderQuotationChargeItemVo tempChargeItemVo: temp){
                        if(tempChargeItemVo.getItemId().equals(chargeItem.getItemId())){
                            chargeItemVo = tempChargeItemVo;
                            break;
                        }
                    }
                }
                chargeItemVo.setGp20Qty(totalGp20Qty);
                chargeItemVo.setGp40Qty(totalGp40Qty);
                chargeItemVo.setHq40Qty(totalHq40Qty);
                chargeItemVo.setLclCbm(totalLclCbm);
                setChargeItemVoPrevPrice(chargeItemVo, chargeItem);
                chargeItemVos.add(chargeItemVo);
            }
        }
        vo.setChargeItems(chargeItemVos);
        return vo;
    }

    private FlowServiceProviderQuotationVo getLatestPriceTemplate(FlowServiceProviderQuotationVo vo, String serviceProviderId, String type){

        List<ServiceProviderQuotationPort> ports = serviceProviderQuotationPortService.listPorts(serviceProviderId, type);
        List<FlowServiceProviderQuotationPortVo> portVos = Lists.newArrayList();
        if(ports != null){
            for (int i = 0; i < ports.size(); i++) {
                ServiceProviderQuotationPort port = ports.get(i);
                FlowServiceProviderQuotationPortVo portVo = new FlowServiceProviderQuotationPortVo();
                if(StringUtils.isNotBlank(vo.getId())){
                    //编辑时
                    List<FlowServiceProviderQuotationPortVo> temp = vo.getPorts();
                    for(FlowServiceProviderQuotationPortVo tempPortVo: temp){
                        if(tempPortVo.getOriginPortId().equals(port.getOriginPortId())){
                            portVo = tempPortVo;
                            break;
                        }
                    }
                }
                setPortVoPrevPrice(portVo, port);
                portVos.add(portVo);
            }
        }
        vo.setPorts(portVos);
        List<ServiceProviderQuotationChargeItem> chargeItems = serviceProviderQuotationChargeItemService.listChargeItems(serviceProviderId, type);
        List<FlowServiceProviderQuotationChargeItemVo> chargeItemVos = Lists.newArrayList();
        if(chargeItems != null){
            for (int i = 0; i < chargeItems.size(); i++) {
                ServiceProviderQuotationChargeItem chargeItem = chargeItems.get(i);
                FlowServiceProviderQuotationChargeItemVo chargeItemVo = new FlowServiceProviderQuotationChargeItemVo();
                if(StringUtils.isNotBlank(vo.getId())){
                    //编辑时
                    List<FlowServiceProviderQuotationChargeItemVo> temp = vo.getChargeItems();
                    for(FlowServiceProviderQuotationChargeItemVo tempChargeItemVo: temp){
                        if(tempChargeItemVo.getItemId().equals(chargeItem.getItemId())){
                            chargeItemVo = tempChargeItemVo;
                            break;
                        }
                    }
                }
                setChargeItemVoPrevPrice(chargeItemVo, chargeItem);
                chargeItemVos.add(chargeItemVo);
            }
        }
        vo.setChargeItems(chargeItemVos);

        return vo;
    }

    /**
     * 设置vo的上次价格
     * @param portVo
     * @param port
     */
    private void setPortVoPrevPrice(FlowServiceProviderQuotationPortVo portVo, ServiceProviderQuotationPort port){
        portVo.setOriginPortId(port.getOriginPortId());
        portVo.setOriginPortCnName(port.getOriginPortCnName());
        portVo.setOriginPortEnName(port.getOriginPortEnName());
        portVo.setDestinationPortId(port.getDestinationPortId());
        portVo.setDestinationPortCnName(port.getDestinationPortCnName());
        portVo.setDestinationPortEnName(port.getDestinationPortEnName());
        portVo.setPrevSailingDays(port.getSailingDays());
        portVo.setPrevFrequency(port.getFrequency());
        portVo.setPrevPriceGp20Aud(port.getPriceGp20Aud());
        portVo.setPrevPriceGp20Rmb(port.getPriceGp20Rmb());
        portVo.setPrevPriceGp20Usd(port.getPriceGp20Usd());
        portVo.setPrevPriceGp40Aud(port.getPriceGp40Aud());
        portVo.setPrevPriceGp40Rmb(port.getPriceGp40Rmb());
        portVo.setPrevPriceGp40Usd(port.getPriceGp40Usd());
        portVo.setPrevPriceHq40Aud(port.getPriceHq40Aud());
        portVo.setPrevPriceHq40Rmb(port.getPriceHq40Rmb());
        portVo.setPrevPriceHq40Usd(port.getPriceHq40Usd());
        portVo.setPrevPriceLclAud(port.getPriceLclAud());
        portVo.setPrevPriceLclRmb(port.getPriceLclRmb());
        portVo.setPrevPriceLclUsd(port.getPriceLclUsd());
        portVo.setPrevPriceGp20InsuranceAud(port.getPriceGp20InsuranceAud());
        portVo.setPrevPriceGp20InsuranceRmb(port.getPriceGp20InsuranceRmb());
        portVo.setPrevPriceGp20InsuranceUsd(port.getPriceGp20InsuranceUsd());
        portVo.setPrevPriceGp40InsuranceAud(port.getPriceGp40InsuranceAud());
        portVo.setPrevPriceGp40InsuranceRmb(port.getPriceGp40InsuranceRmb());
        portVo.setPrevPriceGp40InsuranceUsd(port.getPriceGp40InsuranceUsd());
        portVo.setPrevPriceHq40InsuranceAud(port.getPriceHq40InsuranceAud());
        portVo.setPrevPriceHq40InsuranceRmb(port.getPriceHq40InsuranceRmb());
        portVo.setPrevPriceHq40InsuranceUsd(port.getPriceHq40InsuranceUsd());
        portVo.setPrevPriceLclInsuranceAud(port.getPriceLclInsuranceAud());
        portVo.setPrevPriceLclInsuranceRmb(port.getPriceLclInsuranceRmb());
        portVo.setPrevPriceLclInsuranceUsd(port.getPriceLclInsuranceUsd());
        portVo.setPrevGp20Qty(port.getGp20Qty());
        portVo.setPrevGp40Qty(port.getGp40Qty());
        portVo.setPrevHq40Qty(port.getHq40Qty());
        portVo.setPrevLclCbm(port.getLclCbm());
        portVo.setPrevRateAudToRmb(port.getRateAudToRmb());
        portVo.setPrevRateAudToUsd(port.getRateAudToUsd());
    }

    /**
     * 设置收费项目上次价格
     * @param chargeItemVo
     * @param chargeItem
     */
    private void setChargeItemVoPrevPrice(FlowServiceProviderQuotationChargeItemVo chargeItemVo, ServiceProviderQuotationChargeItem chargeItem){
        chargeItemVo.setItemId(chargeItem.getItemId());
        chargeItemVo.setItemCnName(chargeItem.getItemCnName());
        chargeItemVo.setItemEnName(chargeItem.getItemEnName());
        chargeItemVo.setFeeType(chargeItem.getFeeType());
        chargeItemVo.setUnitId(chargeItem.getUnitId());
        chargeItemVo.setUnitCnName(chargeItem.getUnitCnName());
        chargeItemVo.setUnitEnName(chargeItem.getUnitEnName());
        chargeItemVo.setPrevPriceGp20Aud(chargeItem.getPriceGp20Aud());
        chargeItemVo.setPrevPriceGp20Rmb(chargeItem.getPriceGp20Rmb());
        chargeItemVo.setPrevPriceGp20Usd(chargeItem.getPriceGp20Usd());
        chargeItemVo.setPrevPriceGp40Aud(chargeItem.getPriceGp40Aud());
        chargeItemVo.setPrevPriceGp40Rmb(chargeItem.getPriceGp40Rmb());
        chargeItemVo.setPrevPriceGp40Usd(chargeItem.getPriceGp40Usd());
        chargeItemVo.setPrevPriceHq40Aud(chargeItem.getPriceHq40Aud());
        chargeItemVo.setPrevPriceHq40Rmb(chargeItem.getPriceHq40Rmb());
        chargeItemVo.setPrevPriceHq40Usd(chargeItem.getPriceHq40Usd());
        chargeItemVo.setPrevPriceLclAud(chargeItem.getPriceLclAud());
        chargeItemVo.setPrevPriceLclRmb(chargeItem.getPriceLclRmb());
        chargeItemVo.setPrevPriceLclUsd(chargeItem.getPriceLclUsd());
        chargeItemVo.setPrevPriceOtherAud(chargeItem.getPriceOtherAud());
        chargeItemVo.setPrevPriceOtherRmb(chargeItem.getPriceOtherRmb());
        chargeItemVo.setPrevPriceOtherUsd(chargeItem.getPriceOtherUsd());
        chargeItemVo.setPrevGp20Qty(chargeItem.getGp20Qty());
        chargeItemVo.setPrevGp40Qty(chargeItem.getGp40Qty());
        chargeItemVo.setPrevHq40Qty(chargeItem.getHq40Qty());
        chargeItemVo.setPrevLclCbm(chargeItem.getLclCbm());
        chargeItemVo.setPrevRateAudToRmb(chargeItem.getRateAudToRmb());
        chargeItemVo.setPrevRateAudToUsd(chargeItem.getRateAudToUsd());
    }
    
    @Transactional(rollbackFor = Exception.class)
	public FlowServiceProviderQuotation add(FlowServiceProviderQuotation flowServiceProviderQuotation
            , List<FlowServiceProviderQuotationPort> ports, List<FlowServiceProviderQuotationChargeItem> chargeItems
            , List<Attachment> attachments){
        //设置创建人信息
        setFlowCreatorInfo(flowServiceProviderQuotation);
        //设置服务商信息
        setFlowServiceProviderInfo(flowServiceProviderQuotation);
        //设置价格信息
        setTotalPriceInfo(flowServiceProviderQuotation, ports, chargeItems);
        flowServiceProviderQuotation = flowServiceProviderQuotationDao.save(flowServiceProviderQuotation);
        saveDetails(flowServiceProviderQuotation, ports, chargeItems);
        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowProductQuotation.code, flowServiceProviderQuotation.getId());

    	return flowServiceProviderQuotation;
	}

    @Transactional(rollbackFor = Exception.class)
    public FlowServiceProviderQuotation update(FlowServiceProviderQuotation flowServiceProviderQuotation
            , List<FlowServiceProviderQuotationPort> ports, List<FlowServiceProviderQuotationChargeItem> chargeItems
            , List<Attachment> attachments){
        //设置服务商信息
        setFlowServiceProviderInfo(flowServiceProviderQuotation);
        //设置总价信息
        setTotalPriceInfo(flowServiceProviderQuotation, ports, chargeItems);
        //设置更新时间
        flowServiceProviderQuotation.setUpdatedAt(new Date());

        //删除之前明细，重新绑定数据
        flowServiceProviderQuotationPortDao.deleteByBusinessId(flowServiceProviderQuotation.getId());
        flowServiceProviderQuotationChargeItemDao.deleteByBusinessId(flowServiceProviderQuotation.getId());
        saveDetails(flowServiceProviderQuotation, ports, chargeItems);

        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowProductQuotation.code, flowServiceProviderQuotation.getId());

        return flowServiceProviderQuotationDao.save(flowServiceProviderQuotation);
    }

    /**
     * 复制另存
     */
    @Transactional(rollbackFor = Exception.class)
    public FlowServiceProviderQuotation saveAs(FlowServiceProviderQuotation flowServiceProviderQuotation
            , List<FlowServiceProviderQuotationPort> ports, List<FlowServiceProviderQuotationChargeItem> chargeItems
            , List<Attachment> attachments){

        if(ports != null && ports.size() > 0){
            for (int i = 0; i < ports.size(); i++) {
                FlowServiceProviderQuotationPort port = ports.get(i);
                port.setId(null);
                port.setBusinessId(null);
            }
        }

        if(chargeItems != null && chargeItems.size() > 0){
            for (int i = 0; i < chargeItems.size(); i++) {
                FlowServiceProviderQuotationChargeItem chargeItem = chargeItems.get(i);
                chargeItem.setId(null);
                chargeItem.setBusinessId(null);
            }
        }

        if(attachments != null && attachments.size() > 0){
            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);
                attachment.setId(null);
                attachment.setBusinessId(null);
            }
        }

        flowServiceProviderQuotationDao.clear();
        // 设置创建人信息
        setFlowCreatorInfo(flowServiceProviderQuotation);
        // 设置服务商信息
        setFlowServiceProviderInfo(flowServiceProviderQuotation);
        // 设置总价信息
        setTotalPriceInfo(flowServiceProviderQuotation, ports, chargeItems);
        //清理信息
        cleanInfoForSaveAs(flowServiceProviderQuotation);
        flowServiceProviderQuotation = flowServiceProviderQuotationDao.save(flowServiceProviderQuotation);
        saveDetails(flowServiceProviderQuotation, ports, chargeItems);

        //保存附件
        attachmentService.save(attachments, ConstantsAttachment.Status.FlowProductQuotation.code, flowServiceProviderQuotation.getId());

    	return flowServiceProviderQuotation;
    }

    /**
     * 保存时建立关联关系
     */
    private void saveDetails(FlowServiceProviderQuotation flowServiceProviderQuotation, List<FlowServiceProviderQuotationPort> ports, List<FlowServiceProviderQuotationChargeItem> chargeItems){
        BigDecimal gp20Qty = BigDecimal.ZERO;
        BigDecimal gp40Qty = BigDecimal.ZERO;
        BigDecimal hq40Qty = BigDecimal.ZERO;
        BigDecimal lclCbm = BigDecimal.ZERO;
        if(ports != null && ports.size() > 0){
            for (int i = 0; i < ports.size(); i++) {
                FlowServiceProviderQuotationPort port = ports.get(i);
                port.setBusinessId(flowServiceProviderQuotation.getId());
                port.setRateAudToRmb(flowServiceProviderQuotation.getRateAudToRmb());
                port.setRateAudToUsd(flowServiceProviderQuotation.getRateAudToUsd());
                if(port.getGp20Qty() != null){
                    gp20Qty = gp20Qty.add(port.getGp20Qty());
                }
                if(port.getGp40Qty() != null){
                    gp40Qty = gp40Qty.add(port.getGp40Qty());
                }
                if(port.getHq40Qty() != null){
                    hq40Qty = hq40Qty.add(port.getHq40Qty());
                }
                if(port.getLclCbm() != null){
                    lclCbm = lclCbm.add(port.getLclCbm());
                }
            }
            flowServiceProviderQuotationPortDao.save(ports);
        }
        if(chargeItems != null && chargeItems.size() > 0){
            for (int i = 0; i < chargeItems.size(); i++) {
                FlowServiceProviderQuotationChargeItem chargeItem = chargeItems.get(i);
                chargeItem.setBusinessId(flowServiceProviderQuotation.getId());
                chargeItem.setRateAudToRmb(flowServiceProviderQuotation.getRateAudToRmb());
                chargeItem.setRateAudToUsd(flowServiceProviderQuotation.getRateAudToUsd());
                chargeItem.setGp20Qty(gp20Qty);
                chargeItem.setGp40Qty(gp40Qty);
                chargeItem.setHq40Qty(hq40Qty);
                chargeItem.setLclCbm(lclCbm);
            }
            flowServiceProviderQuotationChargeItemDao.save(chargeItems);
        }
    }

    /**
     * 设置总价信息
     */
    private void setTotalPriceInfo(FlowServiceProviderQuotation quotation, List<FlowServiceProviderQuotationPort> ports, List<FlowServiceProviderQuotationChargeItem> chargeItems){
        //总价
        BigDecimal totalPriceAud = BigDecimal.ZERO;
        BigDecimal totalPriceRmb = BigDecimal.ZERO;
        BigDecimal totalPriceUsd = BigDecimal.ZERO;
        //单项总价
        BigDecimal totalPriceGp20Aud = BigDecimal.ZERO;
        BigDecimal totalPriceGp20Rmb = BigDecimal.ZERO;
        BigDecimal totalPriceGp20Usd = BigDecimal.ZERO;
        BigDecimal totalPriceGp40Aud = BigDecimal.ZERO;
        BigDecimal totalPriceGp40Rmb = BigDecimal.ZERO;
        BigDecimal totalPriceGp40Usd = BigDecimal.ZERO;
        BigDecimal totalPriceHq40Aud = BigDecimal.ZERO;
        BigDecimal totalPriceHq40Rmb = BigDecimal.ZERO;
        BigDecimal totalPriceHq40Usd = BigDecimal.ZERO;

        BigDecimal totalPriceLclAud = BigDecimal.ZERO;
        BigDecimal totalPriceLclRmb = BigDecimal.ZERO;
        BigDecimal totalPriceLclUsd = BigDecimal.ZERO;

        BigDecimal totalPriceOtherAud = BigDecimal.ZERO;
        BigDecimal totalPriceOtherRmb = BigDecimal.ZERO;
        BigDecimal totalPriceOtherUsd = BigDecimal.ZERO;
        //港口
        if(ports != null) {
            for(FlowServiceProviderQuotationPort port : ports){
                if(port.getPriceGp20Aud() != null){
                    totalPriceGp20Aud = totalPriceGp20Aud.add(port.getPriceGp20Aud());
                }
                if(port.getPriceGp20Rmb() != null){
                    totalPriceGp20Rmb = totalPriceGp20Rmb.add(port.getPriceGp20Rmb());
                }
                if(port.getPriceGp20Usd() != null){
                    totalPriceGp20Usd = totalPriceGp20Usd.add(port.getPriceGp20Usd());
                }
                if(port.getPriceGp40Aud() != null){
                    totalPriceGp40Aud = totalPriceGp40Aud.add(port.getPriceGp40Aud());
                }
                if(port.getPriceGp40Rmb() != null){
                    totalPriceGp40Rmb = totalPriceGp40Rmb.add(port.getPriceGp40Rmb());
                }
                if(port.getPriceGp40Usd() != null){
                    totalPriceGp40Usd = totalPriceGp40Usd.add(port.getPriceGp40Usd());
                }
                if(port.getPriceHq40Aud() != null){
                    totalPriceHq40Aud = totalPriceHq40Aud.add(port.getPriceHq40Aud());
                }
                if(port.getPriceHq40Rmb() != null){
                    totalPriceHq40Rmb = totalPriceHq40Rmb.add(port.getPriceHq40Rmb());
                }
                if(port.getPriceHq40Usd() != null){
                    totalPriceHq40Usd = totalPriceHq40Usd.add(port.getPriceHq40Usd());
                }
                if(port.getPriceLclAud() != null){
                    totalPriceLclAud = totalPriceLclAud.add(port.getPriceLclAud());
                }
                if(port.getPriceLclRmb() != null){
                    totalPriceLclRmb = totalPriceLclRmb.add(port.getPriceLclRmb());
                }
                if(port.getPriceLclUsd() != null){
                    totalPriceLclUsd = totalPriceLclUsd.add(port.getPriceLclUsd());
                }
                if (port.getCurrency() == null){
                    port.setCurrency(quotation.getCurrency());
                }
            }
        }
        //收费项目
        if(chargeItems != null) {
            for(FlowServiceProviderQuotationChargeItem chargeItem : chargeItems){
                if(chargeItem.getPriceGp20Aud() != null){
                    totalPriceGp20Aud = totalPriceGp20Aud.add(chargeItem.getPriceGp20Aud());
                }
                if(chargeItem.getPriceGp20Rmb() != null){
                    totalPriceGp20Rmb = totalPriceGp20Rmb.add(chargeItem.getPriceGp20Rmb());
                }
                if(chargeItem.getPriceGp20Usd() != null){
                    totalPriceGp20Usd = totalPriceGp20Usd.add(chargeItem.getPriceGp20Usd());
                }
                if(chargeItem.getPriceGp40Aud() != null){
                    totalPriceGp40Aud = totalPriceGp40Aud.add(chargeItem.getPriceGp40Aud());
                }
                if(chargeItem.getPriceGp40Rmb() != null){
                    totalPriceGp40Rmb = totalPriceGp40Rmb.add(chargeItem.getPriceGp40Rmb());
                }
                if(chargeItem.getPriceGp40Usd() != null){
                    totalPriceGp40Usd = totalPriceGp40Usd.add(chargeItem.getPriceGp40Usd());
                }
                if(chargeItem.getPriceHq40Aud() != null){
                    totalPriceHq40Aud = totalPriceHq40Aud.add(chargeItem.getPriceHq40Aud());
                }
                if(chargeItem.getPriceHq40Rmb() != null){
                    totalPriceHq40Rmb = totalPriceHq40Rmb.add(chargeItem.getPriceHq40Rmb());
                }
                if(chargeItem.getPriceHq40Usd() != null){
                    totalPriceHq40Usd = totalPriceHq40Usd.add(chargeItem.getPriceHq40Usd());
                }
                if(chargeItem.getPriceLclAud() != null){
                    totalPriceLclAud = totalPriceLclAud.add(chargeItem.getPriceLclAud());
                }
                if(chargeItem.getPriceLclRmb() != null){
                    totalPriceLclRmb = totalPriceLclRmb.add(chargeItem.getPriceLclRmb());
                }
                if(chargeItem.getPriceLclUsd() != null){
                    totalPriceLclUsd = totalPriceLclUsd.add(chargeItem.getPriceLclUsd());
                }
                if(chargeItem.getPriceOtherAud() != null){
                    totalPriceOtherAud = totalPriceOtherAud.add(chargeItem.getPriceOtherAud());
                }
                if(chargeItem.getPriceOtherRmb() != null){
                    totalPriceOtherRmb = totalPriceOtherRmb.add(chargeItem.getPriceOtherRmb());
                }
                if(chargeItem.getPriceOtherUsd() != null){
                    totalPriceOtherUsd = totalPriceOtherUsd.add(chargeItem.getPriceOtherUsd());
                }
                if (chargeItem.getCurrency() == null){
                    chargeItem.setCurrency(quotation.getCurrency());
                }
            }
        }
        //总价
        totalPriceAud = totalPriceGp20Aud.add(totalPriceGp40Aud).add(totalPriceHq40Aud).add(totalPriceLclAud);
        totalPriceRmb = totalPriceGp20Rmb.add(totalPriceGp40Rmb).add(totalPriceHq40Rmb).add(totalPriceLclRmb);
        totalPriceUsd =  totalPriceGp20Usd.add(totalPriceGp40Usd).add(totalPriceHq40Usd).add(totalPriceLclUsd);
        quotation.setTotalPriceAud(totalPriceAud);
        quotation.setTotalPriceUsd(totalPriceUsd);
        quotation.setTotalPriceRmb(totalPriceRmb);
        //各柜型总价
        quotation.setTotalPriceGp20Aud(totalPriceGp20Aud);
        quotation.setTotalPriceGp20Rmb(totalPriceGp20Rmb);
        quotation.setTotalPriceGp20Usd(totalPriceGp20Usd);
        quotation.setTotalPriceGp40Aud(totalPriceGp40Aud);
        quotation.setTotalPriceGp40Rmb(totalPriceGp40Rmb);
        quotation.setTotalPriceGp40Usd(totalPriceGp40Usd);
        quotation.setTotalPriceHq40Aud(totalPriceHq40Aud);
        quotation.setTotalPriceHq40Rmb(totalPriceHq40Rmb);
        quotation.setTotalPriceHq40Usd(totalPriceHq40Usd);
        quotation.setTotalPriceLclAud(totalPriceLclAud);
        quotation.setTotalPriceLclAud(totalPriceLclRmb);
        quotation.setTotalPriceLclAud(totalPriceLclUsd);
        quotation.setTotalPriceOtherAud(totalPriceOtherAud);
        quotation.setTotalPriceOtherRmb(totalPriceOtherRmb);
        quotation.setTotalPriceOtherUsd(totalPriceOtherUsd);
    }
	
    @Transactional(rollbackFor = Exception.class)
	public void delete(FlowServiceProviderQuotation flowServiceProviderQuotation){
        flowServiceProviderQuotation.setUpdatedAt(new Date());
        flowServiceProviderQuotation.setStatus(Constants.Status.DELETED.code);
        flowServiceProviderQuotationDao.save(flowServiceProviderQuotation);
	}

    /**
     * 冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void hold(String flowId){
        FlowServiceProviderQuotation flow = flowServiceProviderQuotationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.HOLD.code);
        flowServiceProviderQuotationDao.save(flow);
    }

    /**
     * 解除冻结
     * @param flowId
     */
    @Transactional(rollbackFor = Exception.class)
    public void unHold(String flowId){
        FlowServiceProviderQuotation flow = flowServiceProviderQuotationDao.findOne(flowId);
        flow.setHold(Constants.HoldStatus.UN_HOLD.code);
        flowServiceProviderQuotationDao.save(flow);
    }

    private Specification<FlowServiceProviderQuotation> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<FlowServiceProviderQuotation> spec = DynamicSpecifications.bySearchFilter(filters.values(), FlowServiceProviderQuotation.class);
        return spec;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String businessId){
        FlowServiceProviderQuotation flowServiceProviderQuotation = flowServiceProviderQuotationDao.findOne(businessId);
        flowServiceProviderQuotation.setStatus(Constants.Status.CANCELED.code);
        flowServiceProviderQuotationDao.save(flowServiceProviderQuotation);
        List<ServiceProviderQuotation> serviceProviderQuotations = serviceProviderQuotationDao.findByBusinessId(businessId);
        if(serviceProviderQuotations != null && serviceProviderQuotations.size() > 0){
            for (int i = 0; i < serviceProviderQuotations.size(); i++) {
                serviceProviderQuotations.get(i).setStatus(Constants.Status.CANCELED.code);
            }
            serviceProviderQuotationDao.save(serviceProviderQuotations);
        }
    }
}
