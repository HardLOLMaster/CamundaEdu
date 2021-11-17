package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
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
        processInstance = runtimeService.startProcessInstanceByKey("processModification");

        runtimeService.createProcessInstanceModification(processInstance.getId())
                .cancelAllForActivity("activityA")
                .startBeforeActivity("activityBSub")
                .execute();

        completeTask(processEngine.getTaskService());
        System.out.println(runtimeService.createProcessInstanceQuery().count());
        System.out.println();
        processInstance = runtimeService.startProcessInstanceByKey("processModification");
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

//        completeTask(processEngine.getTaskService());
        System.out.println(runtimeService.createProcessInstanceQuery().count());

        processEngine.close();
    }

    private static void completeTask(TaskService taskService) {
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }
}
