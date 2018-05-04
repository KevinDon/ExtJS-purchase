package com.newaim.purchase.flow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Mark on 2017/9/28.
 */
@Service("taskUserListener")
@Transactional
public class TaskUserListener implements TaskListener{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void notify(DelegateTask delegateTask) {

    }

}
