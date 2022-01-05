package com.github.hlam;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.variable.value.StringValue;

public class BeanClass implements ActivityBehavior {
    @Override
    public void execute(ActivityExecution execution) {
        StringValue var = execution.getVariableTyped("var");
        method(var.getValue());
    }

    public String method(String var) {
        System.out.println(getClass() + " " + var);
        return "Val";
    }
}
