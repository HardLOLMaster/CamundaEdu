package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

public class MainStandaloneTask {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.newTask("TaskId");
        System.out.println("Task Id = " + task.getId());
        taskService.saveTask(task);
        taskService.setOwner(task.getId(), "Owner");
//        taskService.delegateTask(task.getId(), "Delegate");
        taskService.claim(task.getId(), "Delegate");
        taskService.complete(task.getId());

        processEngine.close();
    }
}
