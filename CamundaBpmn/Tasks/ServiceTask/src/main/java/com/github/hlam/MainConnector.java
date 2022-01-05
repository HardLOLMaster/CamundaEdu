package com.github.hlam;

import java.util.List;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.connect.plugin.impl.ConnectProcessEnginePlugin;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.camunda.spin.plugin.variable.value.JsonValue;

public class MainConnector {
    public static void main(String[] args) {
        ProcessEngineConfigurationImpl processEngineConfiguration;
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration();
        processEngineConfiguration.setProcessEnginePlugins(
            List.of(new ConnectProcessEnginePlugin(), new SpinProcessEnginePlugin()));
        ProcessEngine processEngine = processEngineConfiguration
            .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("connector.bpmn")
            .deploy();

        ProcessInstanceWithVariables connector = processEngine.getRuntimeService().createProcessInstanceByKey("connector")
            .executeWithVariablesInReturn();

        JsonValue result = connector.getVariables().getValueTyped("result");
        System.out.println(result);

        processEngine.close();
    }
}
