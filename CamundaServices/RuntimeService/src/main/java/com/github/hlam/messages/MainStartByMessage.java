package com.github.hlam.messages;

import org.camunda.bpm.engine.*;

import java.util.Map;

public class MainStartByMessage {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        try {
            runtimeService.startProcessInstanceByMessage("startMessage",
                    "BusinessKey",
                    Map.of("InputVariable", "Value"));
        } catch (MismatchingMessageCorrelationException ignored) {
            // exception because there is no one process with this start message name
        }
        try {
            runtimeService.correlateMessage("startMessage",
                    "NewKey",
                    Map.of("InputVariable", "Value"));
        } catch (MismatchingMessageCorrelationException ignored) {
            // exception because there is no one process with this start message name
        }

        repositoryService.createDeployment()
                .name("Start Process By Message")
                .addClasspathResource("messages/startMessage.bpmn")
                .deploy();

        // can't deploy 2 process with same a message event subscription for the message with name 'startMessage'
//        repositoryService.createDeployment()
//                .name("Start Process By Message 1")
//                .addClasspathResource("messages/startMessage1.bpmn")
//                .deploy();

        runtimeService.startProcessInstanceByMessage("startMessage",
                "BusinessKey",
                Map.of("InputVariable", "Value"));

        runtimeService.correlateMessage("startMessage",
                "NewKey",
                Map.of("InputVariable", "Value"));

        processEngine.close();
    }
}
