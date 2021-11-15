package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.task.Task;

import java.util.List;

public class MainSimpleManagement {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setJobExecutorActivate(true)
//                .setJobExecutorActivate(false)
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Simple Job Process")
                .addClasspathResource("simpleJobDiagram.bpmn")
                .deploy();
        processEngine.getRepositoryService().createDeployment()
                .name("Advanced Job Process")
                .addClasspathResource("advancedJobDiagram.bpmn")
                .deploy();

        processEngine.getRuntimeService().startProcessInstanceByKey("simpleJobProcess");

        // Timer job
        System.out.println("Jobs count = " + processEngine.getManagementService().createJobQuery().count());
        processEngine.getTaskService().complete(processEngine.getTaskService().createTaskQuery().singleResult().getId());
        // Transaction job
        System.out.println("Jobs count = " + processEngine.getManagementService().createJobQuery().count());

        while (processEngine.getRuntimeService().createProcessInstanceQuery().count() > 0
                && processEngine.getTaskService().createTaskQuery().count() != 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        processEngine.getTaskService().complete(processEngine.getTaskService().createTaskQuery().singleResult().getId());
        System.out.println("*****");
        processEngine.getRuntimeService().startProcessInstanceByKey("advancedJobProcess");

        // Timer job and transaction
        System.out.println("Jobs count = " + processEngine.getManagementService().createJobQuery().count());
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().unlimitedList();
        for (Task task : tasks) {
            processEngine.getTaskService().complete(task.getId());
        }
        // Transaction job
        System.out.println("Jobs count = " + processEngine.getManagementService().createJobQuery().count());

        while (processEngine.getRuntimeService().createProcessInstanceQuery().count() > 0
                && processEngine.getTaskService().createTaskQuery().count() != 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        processEngine.getTaskService().complete(processEngine.getTaskService().createTaskQuery().singleResult().getId());

        processEngine.close();
    }
}
