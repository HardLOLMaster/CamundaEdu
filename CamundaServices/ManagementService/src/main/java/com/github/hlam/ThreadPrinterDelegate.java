package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ThreadPrinterDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(execution.getBusinessKey() + ": Delegate Thread Name = " + Thread.currentThread().getName());
        long count = ProcessEngines.getDefaultProcessEngine().getManagementService().createJobQuery().count();
        System.out.println(execution.getBusinessKey() + ": Number of jobs = " + count);
    }
}
