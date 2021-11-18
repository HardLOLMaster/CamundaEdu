package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import java.util.Map;

public class MainDelegate {
    public static void main(String[] args) {
        //TODO expand on Tasks and Errors
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();
        processEngine.getRepositoryService().createDeployment()
                .name("Delegate Process")
                .addClasspathResource("subProcess.bpmn")
                .addClasspathResource("delegateProcess.bpmn")
                .deploy();
        processEngine.getRuntimeService()
                .startProcessInstanceByKey("delegateProcess", Map.of("strVar", "abc"));
        processEngine.close();
    }
}
