package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Map;

public class MainProcessVariables {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        repositoryService.createDeployment()
                .name("Process Control")
                .addClasspathResource("messages/controlProcess.bpmn")
                .deploy();

        System.out.println("Start process");
        runtimeService.startProcessInstanceByKey("controlProcess", Map.of("InputVariable", "val"));

        Execution execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("continueSignal")
                .singleResult();

        runtimeService.setVariable(execution.getId(), "var", "val");

        Map<String, Object> variables;
        variables = runtimeService.getVariables(execution.getId());
        System.out.println("***");
        System.out.println("Variables after set 'var'");
        for (Map.Entry<String, Object> vars : variables.entrySet()) {
            System.out.println("Variable name = " + vars.getKey()
                    + "\t"
                    + "Variable value = " + vars.getValue());
        }
        System.out.println("***");

        runtimeService.removeVariable(execution.getId(), "var");

        variables = runtimeService.getVariables(execution.getId());
        System.out.println("***");
        System.out.println("Variables after remove 'var'");
        for (Map.Entry<String, Object> vars : variables.entrySet()) {
            System.out.println("Variable name = " + vars.getKey()
                    + "\t"
                    + "Variable value = " + vars.getValue());
        }
        System.out.println("***");

        System.out.println("Send signal");
        runtimeService.signalEventReceived("continueSignal");

        processEngine.close();
    }
}
