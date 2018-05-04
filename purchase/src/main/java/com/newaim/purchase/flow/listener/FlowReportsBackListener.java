package com.newaim.purchase.flow.listener;

import com.newaim.purchase.desktop.reports.service.ReportsService;
import com.newaim.purchase.flow.workflow.listeners.CommonEndListener;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报告退回监听器：
 * 报告退回时，修改报告状态为草稿。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowReportsBackListener extends CommonEndListener{

    @Autowired
    private ReportsService reportsService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String businessId = execution.getProcessBusinessKey();
        //设置报告为草稿
        reportsService.saveDraftByBusinessId(businessId);
    }
}
