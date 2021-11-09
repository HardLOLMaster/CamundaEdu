package com.github.hlam.signals;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;

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
        repositoryService.createDeployment()
                .name("Signal Process Suspended With Two events")
                .tenantId("TenantId")
                .addClasspathResource("signals/signalProcessSuspended1.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("signalProcessSuspended");

        Execution execution;
        execution = getExecution(runtimeService, "signal");
        System.out.println("Sending signal");
        runtimeService.signal(execution.getId());

        runtimeService.startProcessInstanceByKey("signalProcessSuspended1");

        execution = getExecution(runtimeService, "signalA");
        runtimeService.signal(execution.getId());
        execution = getExecution(runtimeService, "signalB");
        runtimeService.signal(execution.getId());

        System.out.println("**********");
        System.out.println("MULTIPLE PROCESS START");
        System.out.println("**********");

        runtimeService.startProcessInstanceByKey("signalProcessSuspended1", "BusinessKey");
        runtimeService.startProcessInstanceByKey("signalProcessSuspended1");
        runtimeService.startProcessInstanceByKey("signalProcessSuspended1");

        runtimeService.createSignalEvent("signalA")
                .tenantId("TenantId")
                .send();

        execution = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey("BusinessKey")
                .signalEventSubscriptionName("signalB")
                .singleResult();
        System.out.println("***");
        System.out.println("Ending 1 process");
        runtimeService.signalEventReceived("signalB", execution.getId());
        System.out.println("***");
        System.out.println("Ending others");
        runtimeService.signalEventReceived("signalB");

        processEngine.close();
    }

    private static Execution getExecution(RuntimeService runtimeService, String signalName) {

        return runtimeService.createExecutionQuery()
                //this will return execution with id 'eventId'
//                .activityId("eventId")
                //this will return signal events that are subscribed to 'signal'
                .signalEventSubscriptionName(signalName)
                .singleResult();
    }
}
