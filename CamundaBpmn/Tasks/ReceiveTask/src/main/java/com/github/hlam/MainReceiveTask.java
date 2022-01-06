package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class MainReceiveTask implements JavaDelegate {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("receive.bpmn")
            .deploy();

        processEngine.getRuntimeService().startProcessInstanceByKey("receive");

        processEngine.getRuntimeService().correlateMessage("testMessage");

        processEngine.close();
    }

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(getClass());
    }
}
