package com.github.hlam;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;

public class DeployHelper {
    public static DeploymentWithDefinitions deploySimpleProcess(RepositoryService repositoryService,
                                                                boolean duplicateDeploy) {
        DeploymentWithDefinitions simpleProcessDeploy;
        DeploymentBuilder deploymentBuilder = simpleProcessDefaultBuilder(repositoryService);
        if (!duplicateDeploy)
            deploymentBuilder.enableDuplicateFiltering(true);
        simpleProcessDeploy = deploymentBuilder.deployWithResult();
        return simpleProcessDeploy;
    }

    public static DeploymentBuilder simpleProcessDefaultBuilder(RepositoryService repositoryService) {
        return repositoryService.createDeployment()
                .name("Simple Process")
                .addClasspathResource("simpleProcess_diagram.bpmn");
    }
}
