package com.github.hlam.messages;

import org.camunda.bpm.engine.*;

import java.util.Map;

public class MainMessageCorrelation {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        repositoryService.createDeployment()
                .name("Message Correlation")
                .addClasspathResource("messages/correlateMessage.bpmn")
                .deploy();

        run3Process(runtimeService);

        printProcessCount(runtimeService);

        runtimeService.correlateMessage("continueMessage",
                Map.of("InputVariable", "process1"));
        printProcessCount(runtimeService);

        runtimeService.correlateMessage("continueMessage",
                "Process2");
        printProcessCount(runtimeService);

        runtimeService.correlateMessage("continueMessage",
                "Process3",
                Map.of("InputVariable", "process3"));
        printProcessCount(runtimeService);

        System.out.println("***");

        run3Process(runtimeService);
        printProcessCount(runtimeService);
        System.out.println("Calling correlateMessage");
        try {
            runtimeService.correlateMessage("continueMessage");
        } catch (MismatchingMessageCorrelationException ignored) {
            // Exception because we can send message to only one execution
            System.out.println("Exception");
        }
        printProcessCount(runtimeService);
        System.out.println("Calling correlateAll");
        runtimeService.createMessageCorrelation("continueMessage").correlateAll();
        printProcessCount(runtimeService);

        processEngine.close();
    }

    private static void run3Process(RuntimeService runtimeService) {
        runtimeService.startProcessInstanceByKey("correlateMessageProcess",
                "Process1",
                Map.of("InputVariable", "process1", "var", "val"));
        runtimeService.startProcessInstanceByKey("correlateMessageProcess",
                "Process2",
                Map.of("InputVariable", "process2", "var", "val"));
        runtimeService.startProcessInstanceByKey("correlateMessageProcess",
                "Process3",
                Map.of("InputVariable", "process3", "var", "val"));
    }

    private static void printProcessCount(RuntimeService runtimeService) {
        System.out.println("Number of running process = " + runtimeService.createProcessInstanceQuery()
                .variableValueEquals("var", "val")
                .count());
    }
}
