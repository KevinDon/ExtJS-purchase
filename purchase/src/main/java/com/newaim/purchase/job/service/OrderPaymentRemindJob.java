package com.newaim.purchase.job.service;

import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.archives.flow.purchase.dao.PurchaseContractDao;
import com.newaim.purchase.archives.flow.purchase.entity.PurchaseContract;
import com.newaim.purchase.desktop.message.helper.Msg;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Mark
 */
@Component
public class OrderPaymentRemindJob extends AbstractJob {

    @Override
    public Runnable getTask() {
        return new OrderPaymentRemindRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_order";
    }

    @Override
    public void runJob() {
        remindOrderPayment();
    }

    @Transactional(rollbackFor = Exception.class)
    public void remindOrderPayment(){
        //查找所有未支付订单
        PurchaseContractDao purchaseContractDao = SpringUtil.getBean(PurchaseContractDao.class);

        List<PurchaseContract> orders = purchaseContractDao.findAllNotPaymentOrders();
        if(orders != null && orders.size() > 0){
            for (int i = 0; i < orders.size(); i++) {
                PurchaseContract order = orders.get(i);
                Integer balancePaymentTerm = order.getBalancePaymentTerm();
                //到岸前：开船日期后　－　到岸日期前（准确的到岸时间在 服务商发票中录入）
                if(balancePaymentTerm == 3 && order.getReta() != null){
                    Date remindDate = DateUtils.addDays(order.getReta(), order.getBalanceCreditTerm() - 2);
                    if(System.currentTimeMillis() >= remindDate.getTime()){
                        Date endDate = DateUtils.addDays(order.getReta(), order.getBalanceCreditTerm());
                        long days = (endDate.getTime() - System.currentTimeMillis()) / (1000 * 3600 * 24);
                        Msg.send(order.getCreatorId(), "订单尾款支付提醒", "订单尾款支付提醒, 订单【" + order.getOrderNumber() + "】尾款支付时间还剩" + days + "天");
                    }
                }
            }
        }
    }

    private class OrderPaymentRemindRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]订单尾款支付提醒任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            remindOrderPayment();
            logger.info("[Job run start][{}][{}:{}]订单尾款支付提醒任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }
}
