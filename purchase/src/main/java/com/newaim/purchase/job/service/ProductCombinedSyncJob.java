package com.newaim.purchase.job.service;

import com.google.gson.Gson;
import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.Constants;
import com.newaim.purchase.api.service.OmsApiService;
import com.newaim.purchase.archives.product.dao.ProductCombinedDao;
import com.newaim.purchase.archives.product.entity.ProductCombined;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mark
 * @date 2017/12/27
 */
@Component
public class ProductCombinedSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductCombinedSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_product_combined_sync";
    }

    @Override
    public void runJob() {
        syncProductCombined();
    }

    /**
     * 同步产品
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncProductCombined(){
        OmsApiService omsApiService = SpringUtil.getBean(OmsApiService.class);
        ProductCombinedDao productCombinedDao = SpringUtil.getBean(ProductCombinedDao.class);
        List<ProductCombined> productCombineds = productCombinedDao.findAllSycnProductCombined();
        if(productCombineds != null && productCombineds.size() > 0){
            // add by lance at 2018-04-10
            this.addMessages("There are "+productCombineds.size()+ " SKUs that are synchronized.");

            for (int i = 0; i < productCombineds.size(); i++) {
                ProductCombined productCombined = productCombineds.get(i);
                try {
                    RestResult result = null;
                    if(Constants.ProductSyncFlag.ADD.code.equals(productCombined.getFlagSyncStatus())){
                        //添加
                        result = omsApiService.packageAdd(productCombined.getId());
                    }else if(Constants.ProductSyncFlag.UPDATE.code.equals(productCombined.getFlagSyncStatus())){
                        //更新
                        result = omsApiService.packageUpdate(productCombined.getId());
                    }

                    if(result != null && result.getSuccess()){
                        productCombined.setFlagSyncStatus(Constants.ProductSyncFlag.NORMAL.code);
                        productCombined.setFlagSyncDate(new Date());
                        productCombinedDao.save(productCombined);
                    }else if(result !=null && !result.getSuccess()){
                        // add by lance at 2018-04-10
                        this.addMessages(productCombined.getCombinedSku()+ " has failed. [" + result.getMsg() + "]");
                    }

                } catch (Exception e) {
                    this.addMessages("Synchronization has failed.["+e.getMessage()+"]");
                    e.printStackTrace();
                }
            }
        }else{
            // add by lance at 2018-04-10
            this.addMessages("No SKU is synchronized.");
        }
    }

    private class ProductCombinedSyncRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]产品组合同步任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            syncProductCombined();
            logger.info("[Job run start][{}][{}:{}]产品组合同步任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
