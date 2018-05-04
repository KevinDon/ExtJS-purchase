package com.newaim.purchase.job.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.admin.system.dao.CronJobDao;
import com.newaim.purchase.admin.system.entity.CronJob;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mark
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {

    @Autowired
    private CronJobDao cronJobDao;

    // add by lance at 2018-04-10
    private List<String> runMessages = Lists.newArrayList();

    /**
     * 系统启动时自动启动所有任务
     */
    @PostConstruct
    public void startAllJobs(){
        Set<Class<? extends AbstractJob>> jobClasses = getAllJobClasses();
        if(jobClasses != null){
            for (Class<? extends AbstractJob> jobClass: jobClasses ) {
                AbstractJob job = SpringUtil.getBean(jobClass);
                job.startJob();
            }
        }
    }
    /**
     * 获取所有任务执行类（需与AbstractJob同包或包下）code->执行类实例
     * @return
     */
    public Map<String, AbstractJob> getJobsMap(){
        Map<String, AbstractJob> map = Maps.newHashMap();
        Set<Class<? extends AbstractJob>> jobClasses = getAllJobClasses();
        for (Class<? extends AbstractJob> jobClass : jobClasses) {
            AbstractJob job = SpringUtil.getBean(jobClass);
            map.put(job.getCronJobCode(), job);
        }
        return map;
    }

    /**
     * 获取所有job类
     * @return
     */
    private Set<Class<? extends AbstractJob>> getAllJobClasses(){
        Reflections reflections = new Reflections(AbstractJob.class.getPackage().getName());
        return reflections.getSubTypesOf(AbstractJob.class);
    }

    /**
     * 启动任务
     * @param code
     */
    public void startJobByCode(String code){
        Map<String, AbstractJob> jobsMap = getJobsMap();
        AbstractJob job = jobsMap.get(code);
        if(job != null){
            job.startJob();
        }
    }

    /**
     * 启动任务
     * @param code
     */
    public void runJobByCode(String code){
        Map<String, AbstractJob> jobsMap = getJobsMap();
        AbstractJob job = jobsMap.get(code);

        if(job != null){
            // add by lance at 2018-04-10
            job.setRunMessages(Lists.newArrayList());

            job.runJob();

            // add by lance at 2018-04-10
            this.runMessages = job.getRunMessages();
        }
    }

    /**
     * 停止任务
     * @param code
     */
    public void stopJobByCode(String code){
        Map<String, AbstractJob> jobsMap = getJobsMap();
        AbstractJob job = jobsMap.get(code);
        if(job != null){
            job.stopJob();
        }
    }

    /**
     * 启动任务
     * @param id
     */
    public void startJob(String id){
        CronJob cronJob = cronJobDao.findOne(id);
        if(cronJob != null){
            startJobByCode(cronJob.getCode());
        }
    }

    // add by lance at 2018-04-10

    public List<String> getRunMessages() {
        return runMessages;
    }

    public void setRunMessages(List<String> runMessages) {
        this.runMessages = runMessages;
    }
}
