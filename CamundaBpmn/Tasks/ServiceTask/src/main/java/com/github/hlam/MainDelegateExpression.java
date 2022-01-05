package com.github.hlam;

import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;

public class MainDelegateExpression {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResourceDefault()
            .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
            .addClasspathResource("delegateBean.bpmn")
            .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("delegateBean",
            Map.of("var", "StartDelegateExpression"));

        processEngine.close();
    }
}
