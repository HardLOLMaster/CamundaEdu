package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CommonDelegate implements JavaDelegate {
    private String delegate;

    public CommonDelegate(String delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Delegate " + delegate);
    }
}
