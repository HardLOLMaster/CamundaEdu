package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class JavaTestDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("---------------------");
        System.out.println("Java Delegate invoked");
        System.out.println("Business Key = " + execution.getBusinessKey());
        System.out.println("InputVariable value = " + execution.getVariable("InputVariable"));
        execution.setVariable("OutputVariable", "OutputValue");
        System.out.println("---------------------");
    }
}
