package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public class MainControlProcess {
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

        ProcessInstance processInstance;
        System.out.println("Start process");
        processInstance = runtimeService.startProcessInstanceByKey("controlProcess");

        System.out.println("Number of active processes = " + runtimeService.createProcessInstanceQuery()
                .active()
                .count());
        System.out.println("Number of suspended processes = " + runtimeService.createProcessInstanceQuery()
                .suspended()
                .count());

        System.out.println("Suspend process");
        runtimeService.suspendProcessInstanceById(processInstance.getId());

        System.out.println("Number of active processes = " + runtimeService.createProcessInstanceQuery()
                .active()
                .count());
        System.out.println("Number of suspended processes = " + runtimeService.createProcessInstanceQuery()
                .suspended()
                .count());

        System.out.println("Send signal");
        runtimeService.signalEventReceived("continueSignal");

        System.out.println("Activate process");
        runtimeService.activateProcessInstanceById(processInstance.getId());

        System.out.println("Number of active processes = " + runtimeService.createProcessInstanceQuery()
                .active()
                .count());
        System.out.println("Number of suspended processes = " + runtimeService.createProcessInstanceQuery()
                .suspended()
                .count());

        System.out.println("Delete process");
        runtimeService.deleteProcessInstance(processInstance.getId(), "Because I can");

        System.out.println("Number of active processes = " + runtimeService.createProcessInstanceQuery()
                .active()
                .count());
        System.out.println("Number of suspended processes = " + runtimeService.createProcessInstanceQuery()
                .suspended()
                .count());

        processEngine.close();
    }
}
