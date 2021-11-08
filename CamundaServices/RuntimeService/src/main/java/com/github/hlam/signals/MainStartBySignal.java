package com.github.hlam.signals;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;

import java.util.Map;

public class MainStartBySignal {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("Signal Process")
                .addClasspathResource("signals/signalProcess.bpmn")
                .deploy();
        System.out.println("Number of deploys = " + repositoryService.createDeploymentQuery().count());
        System.out.println("Number of deployed process = " + repositoryService.createProcessDefinitionQuery().count());

        repositoryService.createDeployment()
                .name("Signal Process 1")
                .addClasspathResource("signals/signalProcess1.bpmn")
                .deploy();
        System.out.println("Number of deploys = " + repositoryService.createDeploymentQuery().count());
        System.out.println("Number of deployed process = " + repositoryService.createProcessDefinitionQuery().count());

        RuntimeService runtimeService = processEngine.getRuntimeService();

        runtimeService.signalEventReceived("startSignal", Map.of("InputVariable", "InputValue"));

        runtimeService.createSignalEvent("startSignal")
            .setVariables(Map.of("InputVariable", "InputValue"))
            .send();

        processEngine.close();
    }
}
