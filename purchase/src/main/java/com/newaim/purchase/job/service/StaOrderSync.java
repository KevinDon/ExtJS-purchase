package com.newaim.purchase.job.service;

import com.newaim.purchase.desktop.sta.service.StaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaOrderSync extends AbstractJob {

    @Autowired
    private StaOrderService staOrderService;

    /**
     * 同步订单报表数据
     */

    public void synOrder() {
        staOrderService.copyFromView();
    }

    @Override
    public Runnable getTask() {
        return new staOrderSyncRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "sta_order_sync";
    }

    @Override
    public void runJob() {
        synOrder();
    }

    private class staOrderSyncRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]订单报表开始同步", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            synOrder();
            logger.info("[Job run start][{}][{}:{}]订单报表同步结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
