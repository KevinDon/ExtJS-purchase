package com.newaim.purchase.flow.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Mark on 2017/10/3.
 */
@Service
public class CommonExecutionListener implements Serializable, ExecutionListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Expression todo;

    public Expression getTodo() {
        return todo;
    }

    public void setTodo(Expression todo) {
        this.todo = todo;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("todo", todo.getExpressionText());
        logger.debug("流程监听器： " + todo.getExpressionText());
    }
}
