package com.newaim.purchase.job.controller;


import com.newaim.core.service.LocaleMessageSource;
import com.newaim.core.utils.RestResult;
import com.newaim.purchase.job.service.EmailReceiveJob;
import com.newaim.purchase.job.service.JobService;
import com.newaim.purchase.job.service.OrderPaymentRemindJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private OrderPaymentRemindJob orderPaymentRemindJob;

    @Autowired
    private EmailReceiveJob emailReceiveJob;

    @Autowired
    private LocaleMessageSource localeMessageSource;

    @Autowired
    private JobService jobService;

    @PostMapping("/start")
    public RestResult startJob(String id){
        RestResult result = new RestResult();
        try {
            jobService.startJob(id);
            result.setSuccess(true).setMsg(localeMessageSource.getMsgOperateSuccess());
        } catch (Exception e) {
            result.setSuccess(false).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
        }
        return result;
    }

    @RequestMapping("/orderJob/start")
    public void startOrderJob(){
        orderPaymentRemindJob.startJob();
    }

    @RequestMapping("/orderJob/stop")
    public void stopOrderJob(){
        orderPaymentRemindJob.stopJob();
    }

    @RequestMapping("/orderJob/restart")
    public void restartOrderJob(){
        orderPaymentRemindJob.restartJob();
    }

    @RequestMapping("/emailReceive/start")
    public void startEmailReceiveJob(){
        emailReceiveJob.startJob();
    }

    @RequestMapping("/emailReceive/stop")
    public void stopEmailReceiveJob(){
        emailReceiveJob.stopJob();
    }

    @RequestMapping("/emailReceive/restart")
    public void restartEmailReceiveJob(){
        emailReceiveJob.restartJob();
    }

}
