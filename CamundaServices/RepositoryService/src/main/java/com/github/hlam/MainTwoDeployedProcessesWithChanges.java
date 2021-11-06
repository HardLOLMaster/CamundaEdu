package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.io.File;
import java.util.List;

public class MainTwoDeployedProcessesWithChanges {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        List<Deployment> deployments;

        RepositoryService repositoryService = processEngine.getRepositoryService();
        {
            DeploymentBuilder deploymentBuilder = DeployHelper.simpleProcessDefaultBuilder(repositoryService);
            deploymentBuilder.deploy();
            deployments = repositoryService.createDeploymentQuery().unlimitedList();
            System.out.println("Number of deploys = " + deployments.size());
        }
        System.out.println("******************");
        {
            DeploymentBuilder deploymentBuilder = DeployHelper.simpleProcessDefaultBuilder(repositoryService);
            deploymentBuilder.enableDuplicateFiltering(true);
            deploymentBuilder.deploy();
            deployments = repositoryService.createDeploymentQuery()
                    .unlimitedList();
            System.out.println("Number of deploys = " + deployments.size());
        }
        System.out.println("******************");
        {
            DeploymentBuilder deploymentBuilder = DeployHelper.simpleProcessDefaultBuilder(repositoryService);
            deploymentBuilder.enableDuplicateFiltering(false);
            deploymentBuilder.deploy();
            deployments = repositoryService.createDeploymentQuery().unlimitedList();
            System.out.println("Number of deploys = " + deployments.size());
        }
        System.out.println("******************");
        {
            DeploymentBuilder deploymentBuilder = DeployHelper.simpleProcessDefaultBuilder(repositoryService);
            String canonicalName = MainTwoDeployedProcessesWithChanges.class.getCanonicalName();
            String resource = canonicalName.replace('.', File.separatorChar) + ".class";
            deploymentBuilder.addClasspathResource(resource);
            // If Duplicate Filtering enabled will be downloaded only changed files
            // If changed file is model (bpmn/dmn) then deployed process also will be updated
            deploymentBuilder.enableDuplicateFiltering(true);
//            deploymentBuilder.enableDuplicateFiltering(false);
            deploymentBuilder.deploy();
            deployments = repositoryService.createDeploymentQuery().unlimitedList();
            System.out.println("Number of deploys = " + deployments.size());
        }

        System.out.println("Number of deployed process = " + repositoryService.createProcessDefinitionQuery().count());
        System.out.println("Latest version = "
                + repositoryService.createProcessDefinitionQuery().latestVersion().singleResult().getVersion());
        System.out.println("******************");

        {
            repositoryService.createDeployment()
                    .name("Simple Process")
                    .addClasspathResource("simpleProcess_diagram_for_change.bpmn")
//                    .enableDuplicateFiltering(true)
                    .enableDuplicateFiltering(false)
                    .deploy();
            deployments = repositoryService.createDeploymentQuery().unlimitedList();
            System.out.println("Number of deploys = " + deployments.size());
        }

        System.out.println("Number of deployed process = " + repositoryService.createProcessDefinitionQuery().count());
        System.out.println("Latest version = "
                + repositoryService.createProcessDefinitionQuery().latestVersion().singleResult().getVersion());

        processEngine.close();
    }
}
