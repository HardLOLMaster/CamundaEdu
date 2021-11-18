package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class TestDelegate implements JavaDelegate {
    private Expression var;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("----");
        // every time creates new instance
        System.out.println(this);
        if (var != null)
            System.out.println("Injected var = " + var.getValue(execution));

        Object mappedVar = execution.getVariable("mappedVar");
        if (mappedVar != null)
            System.out.println("Mapped var = " + mappedVar);
    }
}
