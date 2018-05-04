package com.newaim.purchase.job.service;

import com.newaim.core.utils.SpringUtil;
import com.newaim.purchase.desktop.email.dao.EmailSettingDao;
import com.newaim.purchase.desktop.email.entity.EmailSetting;
import com.newaim.purchase.desktop.email.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mark
 */
@Component
public class EmailReceiveJob extends AbstractJob {


    @Override
    public Runnable getTask() {
        return new EmailReceiveRunnable();
    }

    @Override
    public String getCronJobCode() {
        return "coj_email_receive";
    }

    @Override
    public void runJob() {
        receiveEmail();
    }

    @Transactional(rollbackFor = Exception.class)
    private void receiveEmail(){
        EmailService emailService = SpringUtil.getBean(EmailService.class);
        EmailSettingDao emailSettingDao = SpringUtil.getBean(EmailSettingDao.class);
        List<EmailSetting> settings = emailSettingDao.findAllUsefulEmailSettings();
        if(settings != null && settings.size() > 0){
            for (int i = 0; i < settings.size(); i++) {
                emailService.receive(settings.get(i).getId());
            }
        }
    }

    private class EmailReceiveRunnable implements Runnable{

        @Override
        public void run() {
            logger.info("[Job run start][{}][{}:{}]邮件收取任务开始执行", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
            receiveEmail();
            logger.info("[Job run end][{}][{}:{}]邮件收取任务执行结束", getCronJobCode(), Thread.currentThread().getName(), Thread.currentThread().getId());
        }

    }
}
