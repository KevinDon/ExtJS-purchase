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
public class AsnCancelSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_asn_cancel_sync";
    }

    @Override
    public void runJob() {
        syncAsn();
    }

    /**
     * 同步取消ASN
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncAsn(){
        FlowAsnDao flowAsnDao = SpringUtil.getBean(FlowAsnDao.class);
        List<FlowAsn> asns = flowAsnDao.findAllCancelAsns();
        WmsApiService wmsApiService = SpringUtil.getBean(WmsApiService.class);
            try {
            	if(asns != null && asns.size() > 0){
            		for(FlowAsn asn:asns){
		                RestResult restResult = wmsApiService.cancelAsn(asn.getAsnNumber());
		                if(restResult != null && restResult.getSuccess()){
		                	asn.setFlagSyncStatus(3);
		                	asn.setFlagSyncDate(new Date());
		                	flowAsnDao.save(asn);
		                }else{
	                        this.addMessages(restResult.getData() + " has failed. [" + restResult.getMsg()+ "]");
		                }
	                }
            	}
            } catch (IOException e) {
                e.printStackTrace();
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
