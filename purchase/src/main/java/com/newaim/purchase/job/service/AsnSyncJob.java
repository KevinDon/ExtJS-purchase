package com.newaim.purchase.job.service;

import com.google.common.collect.Maps;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.api.dto.WmsAsnGetAsnReceivingResultDto;
import com.newaim.purchase.api.dto.WmsAsnReceivedDetailDto;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import com.newaim.purchase.flow.shipping.dao.FlowAsnDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;
import com.newaim.purchase.flow.shipping.entity.FlowAsnPackingDetail;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mark
 * @date 2017/12/27
 */
@Component
public class AsnSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_asn_receive_sync";
    }

    @Override
    public void runJob() {
        syncAsn();
    }

    /**
     * 获取实收数，同步ASN
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncAsn(){
        FlowAsnDao flowAsnDao = SpringUtil.getBean(FlowAsnDao.class);
        FlowAsnPackingDetailDao flowAsnPackingDetailDao = SpringUtil.getBean(FlowAsnPackingDetailDao.class);
        ProductDao productDao = SpringUtil.getBean(ProductDao.class);
        List<FlowAsn> asns = flowAsnDao.findAllReceiveAsns();
        WmsApiService wmsApiService = SpringUtil.getBean(WmsApiService.class);
        if(asns != null && asns.size() > 0){
            StringBuilder asnNumbers = new StringBuilder();
            Map<String, FlowAsn> asnMap = Maps.newHashMap();
            for (int i = 0; i < asns.size(); i++) {
                FlowAsn asn = asns.get(i);
                if(i > 0){
                    asnNumbers.append(",");
                }
                asnNumbers.append(asn.getAsnNumber());
                asnMap.put(asn.getAsnNumber(), asn);
            }
            try {
                RestResult restResult = wmsApiService.getASNReceivingResult(asnNumbers.toString());
                if(restResult != null && restResult.getSuccess()){
                    List<WmsAsnGetAsnReceivingResultDto> dtos = (List<WmsAsnGetAsnReceivingResultDto>) restResult.getData();
                    if(dtos != null && dtos.size() > 0){
                        for (int i = 0; i < dtos.size(); i++) {
                            WmsAsnGetAsnReceivingResultDto dto = dtos.get(i);
                            List<WmsAsnReceivedDetailDto> details = dto.getReceivedDetail();
                            FlowAsn asn = asnMap.get(dto.getAsnNumber());
                            if(details != null && details.size() > 0){
                                for (int j = 0; j < details.size(); j++) {
                                    WmsAsnReceivedDetailDto detailDto = details.get(j);
                                    FlowAsnPackingDetail detail = flowAsnPackingDetailDao.getFlowAsnPackingDetail(asn.getId(), detailDto.getSku());
//                                    FlowAsnPackingDetail detail = flowAsnPackingDetailDao.getFlowAsnPackingDetail(asn.getAsnNumber(), asn.getOrderNumber(), asn.getWarehouseId(), detailDto.getSku());
                                    if(detail != null){
                                        detail.setReceivedQty(detailDto.getQty());
                                        if(detailDto.getQty() != null){
                                            Product product = productDao.findOne(detail.getProductId());
                                            if(detail != null){
                                                detail.setReceivedCartons(detailDto.getQty() / product.getProp().getPcsPerCarton());
                                            }
                                        }
                                        flowAsnPackingDetailDao.save(detail);
                                    }
                                }
                                asn.setFlagSyncStatus(3);
                                asn.setFlagSyncDate(new Date());
                                asn.setFlagCompleteStatus(1);
                                asn.setFlagCompleteTime(new Date());
                                flowAsnDao.save(asn);
                            }
                        }
                    }
                }else{
                    this.addMessages(restResult.getData() + " has failed. [" + restResult.getMsg()+ "]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ProductSyncRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]邮件收取任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            syncAsn();
            logger.info("[Job run start][{}][{}:{}]邮件收取任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
