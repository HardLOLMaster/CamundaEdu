package com.github.hlam.scopes;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import java.util.Map;

public class MainVariablesScope {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Scopes Process")
                .addClasspathResource("scopes/scopes.bpmn")
                .deploy();

        processEngine.getRepositoryService().createDeployment()
                .name("Scopes Process 1")
                .addClasspathResource("scopes/scopes1.bpmn")
                .deploy();

        processEngine.getRepositoryService().createDeployment()
                .name("Scopes Process 2")
                .addClasspathResource("scopes/scopes2.bpmn")
                .addClasspathResource("scopes/subProcess.bpmn")
                .deploy();

        processEngine.getRuntimeService()
                .startProcessInstanceByKey("scopeProcess", Map.of("var1", "val1"));

        System.out.println("\n\n");

        processEngine.getRuntimeService()
                .startProcessInstanceByKey("scopeProcess1", Map.of("var1", "val1"));

        System.out.println("\n\n");

        processEngine.getRuntimeService()
                .startProcessInstanceByKey("scopeProcess2", Map.of("var1", "val1"));

        processEngine.close();
    }
}
