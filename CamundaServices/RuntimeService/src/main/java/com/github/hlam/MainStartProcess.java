package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;

public class MainStartProcess {
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
        System.out.println("Number of deploys = " + repositoryService.createDeploymentQuery().count());
        System.out.println("Number of deployed process = " + repositoryService.createProcessDefinitionQuery().count());

        RuntimeService runtimeService = processEngine.getRuntimeService();

        System.out.println("***************");
        System.out.println("Start process 1");
        runtimeService.startProcessInstanceByKey("simpleProcess");
        System.out.println("***************");

        System.out.println("***************");
        System.out.println("Start process 2");
        runtimeService.createProcessInstanceByKey("simpleProcess")
                .execute();
        System.out.println("***************");
        {
            System.out.println("***************");
            System.out.println("Start process 3");
            ProcessInstanceWithVariables process = runtimeService.createProcessInstanceByKey("simpleProcess")
                    .businessKey("BusinessKey")
                    .processDefinitionTenantId("TenantId")
                    .setVariable("InputVariable", "InputValue")
                    .executeWithVariablesInReturn();
            System.out.println("InputVariable value = " + process.getVariables().get("InputVariable"));
            System.out.println("OutputVariable value = " + process.getVariables().get("OutputVariable"));
            System.out.println("***************");
        }

        processEngine.close();
    }
}
