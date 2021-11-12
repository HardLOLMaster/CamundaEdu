package com.github.hlam;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainHistory {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("Simple Process")
                .tenantId("TenantId")
                .addClasspathResource("simpleProcess_diagram.bpmn")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Random random = new Random(37);
        for (int i = 0; i < 10; i++) {
            startProcessInstance(runtimeService, random);
        }

        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceId()
                .asc()
                .unlimitedList();

        for (HistoricProcessInstance processInstance : historicProcessInstances) {
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .variableNameLike("%Variable")
                    .unlimitedList();
            for (HistoricVariableInstance variableInstance : variableInstances) {
                System.out.println(
                        processInstance.getId()
                                + " " + processInstance.getProcessDefinitionKey()
                                + " " + processInstance.getProcessDefinitionName()
                                + " " + processInstance.getBusinessKey()
                                + " " + processInstance.getStartActivityId()
                                + " " + processInstance.getDurationInMillis()
                                + " " + variableInstance.getValue()
                );
            }
        }

        processEngine.close();
    }

    private static ProcessInstance startProcessInstance(RuntimeService runtimeService, Random random) {
        return runtimeService.startProcessInstanceByKey("simpleProcess",
                "BusinessKey_" + random.nextInt(),
                Map.of("InputVariable", "Value_" + random.nextInt()));
    }
}
