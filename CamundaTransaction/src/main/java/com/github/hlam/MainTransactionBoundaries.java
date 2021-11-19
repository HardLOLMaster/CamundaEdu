package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

import static com.github.hlam.BoundaryDelegate.flag;

public class MainTransactionBoundaries {
    public static final Object OBJ = new Object();

    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setJobExecutorActivate(true)
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Transaction Boundaries Process")
                .addClasspathResource("transactionBoundaries.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance;
        List<Execution> executions;
        String transactionBoundaries = "transactionBoundaries";
        {
            runtimeService.startProcessInstanceByKey(transactionBoundaries,
                    Map.of("errorElement", 3, "symbol", "A"));

            runtimeService.signalEventReceived("signal");

            waitUntilProcessDone(runtimeService);
        }
        System.out.println("===========");
        {
            processInstance = runtimeService.startProcessInstanceByKey(transactionBoundaries,
                    Map.of("errorElement", 2, "symbol", "A"));

            runtimeService.signalEventReceived("signal");

            waitUntilProcessDone(runtimeService);

            executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).unlimitedList();
            for (Execution execution : executions) {
                Map<String, Object> variables = runtimeService.getVariables(execution.getId());
                System.out.println(variables);
                System.out.println("*");
            }


            suspendProcesses(runtimeService);
            synchronized (OBJ) {
                OBJ.notifyAll();
            }
            waitUntilProcessDone(runtimeService);
            deleteProcesses(runtimeService);
            flag = 0;
        }
        processEngine.close();
    }

    private static void suspendProcesses(RuntimeService runtimeService) {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().unlimitedList();
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }
    }

    private static void deleteProcesses(RuntimeService runtimeService) {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().unlimitedList();
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.deleteProcessInstance(processInstance.getId(), "Because I can");
        }
    }

    private static void waitUntilProcessDone(RuntimeService runtimeService) {
        while (runtimeService.createProcessInstanceQuery().count() > 0 && flag == 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (flag == 0)
            System.out.println("------");
    }
}
