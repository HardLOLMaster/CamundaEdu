package com.github.hlam;

import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;

public class MainExpression {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResourceDefault()
            .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
            .addClasspathResource("expression.bpmn")
            .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("expression", Map.of("var", "StartExpression"));

        processEngine.close();
    }
}
