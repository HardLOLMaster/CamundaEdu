package com.github.hlam;

import java.util.List;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;

public class MainExternal {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
            .addClasspathResource("external.bpmn")
            .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("external");

        ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
        String workerId = "WorkerId";
        List<LockedExternalTask> externalTasks = externalTaskService
            .fetchAndLock(10, workerId)
            .topic("topic", 1000)
            .execute();
        for (LockedExternalTask task : externalTasks) {
            System.out.println(task.getId() + " " + task.getTopicName());
            externalTaskService.complete(task.getId(), workerId);
        }

        processEngine.close();
    }
}
