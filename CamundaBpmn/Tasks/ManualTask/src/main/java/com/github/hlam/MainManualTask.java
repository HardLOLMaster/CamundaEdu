package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class MainManualTask {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("manual.bpmn")
            .deploy();

        processEngine.getRuntimeService().startProcessInstanceByKey("manual");

        long count = processEngine.getRuntimeService().createProcessInstanceQuery().count();
        System.out.println(count);

        processEngine.close();
    }
}
