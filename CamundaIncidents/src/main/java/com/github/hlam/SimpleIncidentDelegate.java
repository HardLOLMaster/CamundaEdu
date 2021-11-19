package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SimpleIncidentDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        Boolean exception = (Boolean) execution.getVariableTyped("exception").getValue();
        if (exception)
            throw new RuntimeException("I need this");
        Boolean incident = (Boolean) execution.getVariableTyped("incident").getValue();
        if (incident)
            ProcessEngines.getDefaultProcessEngine().getRuntimeService()
                    .createIncident("myType", execution.getId(), "bar", "message");
    }
}
