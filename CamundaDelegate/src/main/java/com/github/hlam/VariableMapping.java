package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;

public class VariableMapping implements DelegateVariableMapping {
    @Override
    public void mapInputVariables(DelegateExecution superExecution, VariableMap subVariables) {
        subVariables.put("mappedVar", superExecution.getVariable("strVar") + "Mapped");
    }

    @Override
    public void mapOutputVariables(DelegateExecution superExecution, VariableScope subInstance) {

    }
}
