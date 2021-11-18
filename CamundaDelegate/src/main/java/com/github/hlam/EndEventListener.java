package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class EndEventListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println(this + " " + execution.getEventName());
    }
}
