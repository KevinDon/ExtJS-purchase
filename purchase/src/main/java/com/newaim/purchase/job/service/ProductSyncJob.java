package com.newaim.purchase.job.service;

import com.newaim.core.utils.RestResult;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.Constants;
import com.newaim.purchase.api.service.OmsApiService;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Mark
 * @date 2017/12/27
 */
@Component
public class ProductSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_product_sync";
    }

    @Override
    public void runJob() {
        syncProduct();
    }

    /**
     * 同步产品
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncProduct(){
        OmsApiService omsApiService = SpringUtil.getBean(OmsApiService.class);
        ProductDao productDao = SpringUtil.getBean(ProductDao.class);
        List<Product> products = productDao.findAllSycnProducts();
        if(products != null && products.size() > 0){
            // add by lance at 2018-04-10
            this.addMessages("There are "+products.size()+ " SKUs that are synchronized.");

            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                try {
                    RestResult result = null;
                    if(Constants.ProductSyncFlag.ADD.code.equals(product.getIsSync())){
                        //添加
                        result = omsApiService.goodsAdd(product.getId());
                    }else if(Constants.ProductSyncFlag.UPDATE.code.equals(product.getIsSync())){
                        //更新
                        result = omsApiService.goodsUpdate(product.getId());
                    }
                    if(result != null && result.getSuccess()){
                        product.setIsSync(Constants.ProductSyncFlag.NORMAL.code);
                        product.setSyncTime(new Date());
                        productDao.save(product);
                    }else if(result !=null && !result.getSuccess()){
                        // add by lance at 2018-04-10
                        this.addMessages(product.getSku() + " has failed. [" + result.getMsg()+ "]");
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

    private class ProductSyncRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]邮件收取任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            syncProduct();
            logger.info("[Job run start][{}][{}:{}]邮件收取任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
