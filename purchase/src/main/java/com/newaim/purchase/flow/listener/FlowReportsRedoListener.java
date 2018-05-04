package com.newaim.purchase.flow.listener;

import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报告监听器：
 * 反审时，修改报告状态为禁用。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowReportsRedoListener extends CommonEndListener{

    @Autowired
    private ReportsService reportsService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //禁用报告
        reportsService.saveDisabledByBusinessId(businessId);
    }
}
