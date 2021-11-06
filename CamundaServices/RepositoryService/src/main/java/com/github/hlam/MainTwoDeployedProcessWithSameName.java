package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.util.List;

public class MainTwoDeployedProcessWithSameName {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        deployProcess(repositoryService);

        processEngine.getRuntimeService().startProcessInstanceByKey("simpleProcess");

        System.out.println("*******************");

        deployProcess(repositoryService);

        processEngine.getRuntimeService().startProcessInstanceByKey("simpleProcess");

        processEngine.close();
    }

    private static void deployProcess(RepositoryService repositoryService) {
        List<Deployment> deployments;

        DeployHelper.deploySimpleProcess(repositoryService, true);

        deployments = repositoryService.createDeploymentQuery()
                .deploymentName("Simple Process")
                .unlimitedList();
        System.out.println("Number of deploys = " + deployments.size());

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().unlimitedList();
        ProcessDefinition deployedProcessLatest = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();

        System.out.println("---");
        System.out.println("LATEST VERSION");
        System.out.println(
                        " | " +
                        "Key = " + deployedProcessLatest.getKey() +
                        " | " +
                        "Version = " + deployedProcessLatest.getVersion() +
                        " | "
        );
        System.out.println("---");

        System.out.println("Number of deployed processes = " + processDefinitions.size());

        for (int i = 0; i < processDefinitions.size(); i++) {
            ProcessDefinition processDefinition = processDefinitions.get(i);
            System.out.println(
                    " | " +
                    i +
                    " | " +
                    "Key = " + processDefinition.getKey() +
                    " | " +
                    "Version = " + processDefinition.getVersion() +
                    " | "
            );
        }

    }
}
