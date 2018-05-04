package com.newaim.purchase.job.service;

import com.newaim.core.mapper.JsonMapper;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.api.dto.SyncBarcodeDto;
import com.newaim.purchase.api.service.WmsApiService;
import com.newaim.purchase.archives.product.dao.ProductDao;
import com.newaim.purchase.archives.product.entity.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mark
 * @date 2017/12/27
 */
@Component
public class BarcodeSyncJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new ProductSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_barcode_sync";
    }

    @Override
    public void runJob() {
        syncBarcode();
    }

    /**
     * 同步产品
     */
    public void saveSyncProduct(String skus){
        WmsApiService wmsApiService = SpringUtil.getBean(WmsApiService.class);
        ProductDao productDao = SpringUtil.getBean(ProductDao.class);
        try {
            String resultStr = wmsApiService.syncBarcode(skus);
            JsonMapper jsonMapper = JsonMapper.INSTANCE;
            List<SyncBarcodeDto> data = jsonMapper.fromJson(resultStr, jsonMapper.buildCollectionType(List.class, SyncBarcodeDto.class));
            if(data != null && data.size() > 0){
                for (int i = 0; i < data.size(); i++) {
                    SyncBarcodeDto dto = data.get(i);
                    productDao.updateProductBarcode(dto.getSku(), dto.getBarcode());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncBarcode(){
        ProductDao productDao = SpringUtil.getBean(ProductDao.class);
        List<String> skuList = productDao.findAllSkusByEmptyBarcode();
        if(skuList != null && skuList.size() > 0){
            StringBuilder skus = new StringBuilder();
            for (int i = 0; i < skuList.size(); i++) {
                if(skus.length() > 0){
                    skus.append(",");
                }
                skus.append(skuList.get(i));
                if(i > 0 && i % 15 == 0){
                    saveSyncProduct(skus.toString());
                    skus.setLength(0);
                }
            }
            if(skus.length() > 0){
                saveSyncProduct(skus.toString());
            }
        }
    }

    private class ProductSyncRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]barcode同步任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            syncBarcode();
            logger.info("[Job run start][{}][{}:{}]barcode同步任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
