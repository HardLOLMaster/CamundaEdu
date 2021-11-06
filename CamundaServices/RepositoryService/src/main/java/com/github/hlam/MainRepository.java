package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;

import java.util.List;

public class MainRepository {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentWithDefinitions simpleProcessDeploy;
        List<Deployment> deployments;

        simpleProcessDeploy = DeployHelper.deploySimpleProcess(repositoryService, false);

        System.out.println("Deployed process name " + simpleProcessDeploy.getName());
        deployments = repositoryService.createDeploymentQuery()
                .deploymentName("Simple Process")
                .unlimitedList();
        System.out.println("Number of deploys = " + deployments.size());

        simpleProcessDeploy = DeployHelper.deploySimpleProcess(repositoryService, false);

        System.out.println("Deployed process name " + simpleProcessDeploy.getName());
        deployments = repositoryService.createDeploymentQuery()
                .deploymentName("Simple Process")
                .unlimitedList();
        System.out.println("Number of deploys = " + deployments.size());

        processEngine.close();
    }
}
