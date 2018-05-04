package com.newaim.purchase.job.service;

import com.google.common.collect.Lists;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.Constants;
import com.newaim.purchase.admin.system.dao.CronJobDao;
import com.newaim.purchase.admin.system.entity.CronJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Mark
 * @date 2017/12/26
 */
public abstract class AbstractJob {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractJob.class);

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> future;

    private Runnable task;

    private Trigger trigger;

    private String cronJobCode;

    // add by lance at 2018-04-10
    private List<String> runMessages = Lists.newArrayList();

    public AbstractJob(){
        this.task = getTask();
        this.cronJobCode = getCronJobCode();
        this.trigger = getTrigger();

        // add by lance at 2018-04-10
        this.runMessages = Lists.newArrayList();
    }

    /**
     * 设置的任务
     * @return
     */
    public abstract Runnable getTask();

    /**
     * 获取任务定义code
     * @return
     */
    public abstract String getCronJobCode();

    /**
     * 获取定时触发器
     * @return
     */
    private Trigger getTrigger(){
        CronJobDao cronJobDao = SpringUtil.getBean(CronJobDao.class);
        CronJob cronJob = cronJobDao.findByCode(this.cronJobCode);
        if(cronJob != null && Constants.Status.NORMAL.code.equals(cronJob.getStatus())){
            return new CronTrigger(cronJob.getCronStr());
        }
        return null;
    }

    /**
     * 启动任务(已启动的做重启操作)
     */
    public boolean startJob(){
        if(future == null || future.isCancelled()){
            if(trigger != null){
                future = threadPoolTaskScheduler.schedule(task, trigger);
                logger.info("[Job start][{}][{}:{}]",getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            }
        }else{
            restartJob();
        }
        return false;
    }

    /**
     * 终止任务
     */
    public void stopJob(){
        if(future != null){
            future.cancel(true);
            logger.info("[Job already stopped][{}][{}:{}]", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }

    /**
     * 重启任务
     */
    public void restartJob(){
        stopJob();
        if(trigger != null){
            future = threadPoolTaskScheduler.schedule(task, trigger);
            logger.info("[Job restart][{}][{}:{}]", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }

    /**
     * 判断任务是否启动
     * @return
     */
    public boolean isStarted(){
        return future != null && !future.isCancelled();
    }

    /**
     * 运行任务
     */
    public abstract void runJob();

    /**
     * add by lance at 2018-04-10
     * 加入执行过程提示内容
     * @param message
     */
    public void addMessages(String message){
        this.runMessages.add(message);
    }

    public List<String> getRunMessages() {
        return runMessages;
    }

    public void setRunMessages(List<String> runMessages) {
        this.runMessages = runMessages;
    }
}
