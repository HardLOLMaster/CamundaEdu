package com.github.hlam;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;

import java.util.List;
import java.util.Set;

public class MainExternalVarious {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("External Process")
                .addClasspathResource("externamProcessSimple.bpmn")
                .deploy();

        String complete = "Complete";
        startProcess(processEngine, complete);
        String bpmnError = "BpmnError";
        startProcess(processEngine, bpmnError);
        String runtimeError = "RuntimeError";
        startProcess(processEngine, runtimeError);
        String extendLock = "ExtendLock";
        startProcess(processEngine, extendLock);

        ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
        List<LockedExternalTask> tasks = externalTaskService.fetchAndLock(10, "WorkerId")
                .topic("testTopic", 1000)
                .execute();

        System.out.println("Count of locked tasks = " + tasks.size());
        for (LockedExternalTask task : tasks) {
            System.out.println("Task " + task.getId()
                    + " " + task.getTopicName()
                    + " " + task.getBusinessKey()
            );
            if (complete.equals(task.getBusinessKey())) {
                externalTaskService.complete(task.getId(), "WorkerId");
            }
            if (bpmnError.equals(task.getBusinessKey())) {
                externalTaskService.handleBpmnError(task.getId(), "WorkerId", "ErrCode");
            }
            if (runtimeError.equals(task.getBusinessKey())) {
                ExternalTask externalTask;
                externalTask = externalTaskService.createExternalTaskQuery()
                        .externalTaskIdIn(Set.of(task.getId()))
                        .singleResult();
                System.out.println("Number of reties before failure = " + externalTask.getRetries());
                externalTaskService.handleFailure(task.getId(),
                        "WorkerId",
                        "RE",
                        task.getRetries() != null ? task.getRetries() - 1 : 3,
                        1000);
                externalTask = externalTaskService.createExternalTaskQuery()
                        .externalTaskIdIn(Set.of(task.getId()))
                        .singleResult();
                System.out.println("Number of reties after failure = " + externalTask.getRetries());
            }
            if (extendLock.equals(task.getBusinessKey())) {
                externalTaskService.extendLock(task.getId(), "WorkerId", 3000);
            }
            System.out.println("***");
        }

        processEngine.close();
    }

    private static void startProcess(ProcessEngine processEngine, String businessKey) {
        processEngine.getRuntimeService()
                .startProcessInstanceByKey("externalProcess", businessKey);
    }
}
