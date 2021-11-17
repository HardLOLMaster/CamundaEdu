package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

public class MainProcessModification {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Process Modification")
                .addClasspathResource("processModification.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance;

        System.out.println("Modification 1");
        processInstance = runtimeService.startProcessInstanceByKey("processModification");
        printNumberOfRunningProcesses(runtimeService);
        runtimeService.createProcessInstanceModification(processInstance.getId())
                .cancelAllForActivity("activityA")
                .startBeforeActivity("activityBSub")
                .execute();
        completeTask(processEngine.getTaskService());
        printNumberOfRunningProcesses(runtimeService);
        System.out.println();

        System.out.println("Modification 2");
        processInstance = runtimeService.startProcessInstanceByKey("processModification");
        printNumberOfRunningProcesses(runtimeService);
        runtimeService.createProcessInstanceModification(processInstance.getId())
                .cancelAllForActivity("activityA")
                .startBeforeActivity("activityASub")
                .cancelAllForActivity("activityA")
                .startBeforeActivity("activityBSub")
                .cancelAllForActivity("activityB")
                .startAfterActivity("activityA")
                .setVariable("b", false)
                .cancelAllForActivity("activityC")
                .startAfterActivity("activityB")
                .execute();
        printNumberOfRunningProcesses(runtimeService);
        System.out.println();

        System.out.println("Modification 3");
        runtimeService.createProcessInstanceByKey("processModification")
                .startBeforeActivity("activityB")
                .execute();
        printNumberOfRunningProcesses(runtimeService);
        System.out.println("Complete task");
        completeTask(processEngine.getTaskService());
        printNumberOfRunningProcesses(runtimeService);
        System.out.println();

        System.out.println("Restart");
        processInstance = runtimeService.startProcessInstanceByKey("processModification");
        printNumberOfRunningProcesses(runtimeService);
        System.out.println("Delete task");
        runtimeService.deleteProcessInstance(processInstance.getId(), "Because I can");
        printNumberOfRunningProcesses(runtimeService);
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .latestVersion()
                .processDefinitionKey("processModification")
                .singleResult();
        runtimeService.restartProcessInstances(processDefinition.getId())
                .initialSetOfVariables()
                .startBeforeActivity("activityBSub")
                .processInstanceIds(processInstance.getId())
                .execute();
        printNumberOfRunningProcesses(runtimeService);
        System.out.println("Complete task");
        completeTask(processEngine.getTaskService());
        printNumberOfRunningProcesses(runtimeService);

        processEngine.close();
    }

    private static void printNumberOfRunningProcesses(RuntimeService runtimeService) {
        System.out.println("Number of running processes = " + runtimeService.createProcessInstanceQuery().count());
    }

    private static void completeTask(TaskService taskService) {
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }
}
