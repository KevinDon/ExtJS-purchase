package com.newaim.purchase.admin.system.service;

import com.google.common.collect.Lists;
import com.newaim.core.jpa.DynamicSpecifications;
import com.newaim.core.jpa.SearchFilter;
import com.newaim.core.mapper.BeanMapper;
import com.newaim.core.service.ServiceBase;
import com.newaim.purchase.admin.system.dao.CronJobDao;
import com.newaim.purchase.admin.system.entity.CronJob;
import com.newaim.purchase.admin.system.vo.CronJobVo;
import com.newaim.purchase.job.service.AbstractJob;
import com.newaim.purchase.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CronJobService extends ServiceBase {

    @Autowired
    private CronJobDao cronJobDao;

    @Autowired
    private JobService jobService;

    // add by lance at 2018-04-10
    private List<String> runMessages = Lists.newArrayList();

    /**
     * 分页查询所有提醒信息
     * @param params
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @return
     */
    public Page<CronJobVo> list(LinkedHashMap<String, Object> params, int pageNumber, int pageSize, Sort sort){
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Specification<CronJob> spec = buildSpecification(params);
        Page<CronJob> p = cronJobDao.findAll(spec, pageRequest);
        Map<String, AbstractJob> jobsMap = jobService.getJobsMap();
        Page<CronJobVo> page = p.map(cronJob -> {
            CronJobVo vo = convertToCronJobVo(cronJob);
            AbstractJob job = jobsMap.get(vo.getCode());
            if(job != null && job.isStarted()){
                vo.setIsStarted(1);
            }else{
                vo.setIsStarted(2);
            }
            return vo;
        });
        return page;
    }

    /**
     * entity转换为Vo
     * @param cronJob
     * @return
     */
    private CronJobVo convertToCronJobVo(CronJob cronJob){
        CronJobVo vo = BeanMapper.map(cronJob, CronJobVo.class);
        return vo;
    }

    private Specification<CronJob> buildSpecification(LinkedHashMap<String, Object> searchParams) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<CronJob> spec = DynamicSpecifications.bySearchFilter(filters.values(), CronJob.class);
        return spec;
    }

    /**
     * 根据
     * @param id
     * @return
     */
    public CronJob getCronJob(String id){
        return cronJobDao.findOne(id);
    }

    public CronJobVo get(String id){
        CronJobVo vo =convertToCronJobVo(getCronJob(id));
        Map<String, AbstractJob> jobsMap = jobService.getJobsMap();
        AbstractJob job = jobsMap.get(vo.getCode());
        if(job != null && job.isStarted()){
            vo.setIsStarted(1);
        }else{
            vo.setIsStarted(2);
        }
        return vo;
    }

    public CronJob getCronJobByCode(String code){
        return cronJobDao.findByCode(code);
    }

    public CronJobVo getByCode(String code){
        return convertToCronJobVo(cronJobDao.findByCode(code));
    }

    @Transactional(rollbackFor = Exception.class)
    public CronJob update(CronJob cronJob){
        cronJob.setUpdatedAt(new Date());
        return cronJobDao.save(cronJob);
    }

    /**
     * 停止任务，系统重启后，会自动启用，如需完全停止，通过状态设置
     * @param code
     */
    public void stopCronJobByCode(String code){
        jobService.stopJobByCode(code);
    }

    /**
     * 启动任务
     * @param code
     */
    public void startCronJobByCode(String code){
        jobService.startJobByCode(code);
    }

    /**
     * 运行任务
     * @param code
     */
    @Transactional(rollbackFor = Exception.class)
    public void runJobByCode(String code){
        jobService.runJobByCode(code);

        this.setRunMessages(jobService.getRunMessages());
    }

    public List<String> getRunMessages() {
        return runMessages;
    }

    public void setRunMessages(List<String> runMessages) {
        this.runMessages = runMessages;
    }
}
