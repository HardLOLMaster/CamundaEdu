package com.github.hlam.messages;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;

public class MainContinueByMessage {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("Continue By Message")
                .addClasspathResource("messages/messageContinue.bpmn")
                .deploy();
        repositoryService.createDeployment()
                .name("Continue By Message 1")
                .addClasspathResource("messages/messageContinue1.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("messageContinueProcess");

        runtimeService.correlateMessage("continueMessage");

        runtimeService.startProcessInstanceByKey("messageContinueProcess1");

        runtimeService.correlateMessage("continueMessagePathA");
        runtimeService.correlateMessage("continueMessagePathB");

        processEngine.close();
    }
}
