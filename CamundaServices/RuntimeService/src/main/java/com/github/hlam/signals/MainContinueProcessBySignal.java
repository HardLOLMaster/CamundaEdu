package com.github.hlam.signals;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;

import java.util.List;

public class MainContinueProcessBySignal {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("Signal Process Suspended")
                .addClasspathResource("signals/signalProcessSuspended.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("signalProcessSuspended");

        // Здесь 2 исполнения. Один относится к эвенту сигнала, а второй описывает процесс?
        List<Execution> executions = runtimeService.createExecutionQuery().unlimitedList();

        Execution execution = runtimeService.createExecutionQuery()
                .activityId("waitEvent")
                .singleResult();
        System.out.println("Sending signal");
        runtimeService.signal(execution.getId());

        processEngine.close();
    }
}
