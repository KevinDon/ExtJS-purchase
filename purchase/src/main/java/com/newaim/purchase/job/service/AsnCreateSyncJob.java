package com.newaim.purchase.job.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.api.dto.WmsAsnCreateResultDto;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnDao;
import com.newaim.purchase.flow.shipping.dao.FlowAsnPackingDetailDao;
import com.newaim.purchase.flow.shipping.entity.FlowAsn;

/**
 * @author Mark
 * @date 2017/12/27
 */
@Component
public class AsnCreateSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_asn_create_sync";
    }

    @Override
    public void runJob() {
        syncAsn();
    }

    /**
     * 同步新建的ASN
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncAsn(){
        FlowAsnDao flowAsnDao = SpringUtil.getBean(FlowAsnDao.class);
        List<FlowAsn> asns = flowAsnDao.findAllCreateAsns();
        WmsApiService wmsApiService = SpringUtil.getBean(WmsApiService.class);
            try {
            	if(asns != null && asns.size() > 0){
            		for(FlowAsn asn:asns){
		                RestResult restResult = wmsApiService.createAsn(asn.getId());
		                if(restResult != null && restResult.getSuccess()){
		                	asn.setFlagSyncStatus(3);
		                	asn.setFlagSyncDate(new Date());
		                    WmsAsnCreateResultDto resultDto = (WmsAsnCreateResultDto) restResult.getData();
		                	asn.setAsnNumber(resultDto.getAsnNo());
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
