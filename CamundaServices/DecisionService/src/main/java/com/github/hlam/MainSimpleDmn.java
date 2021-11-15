package com.github.hlam;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

import java.util.Map;

public class MainSimpleDmn {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Simple DMN")
                .addClasspathResource("simpleDecision.dmn")
                .deploy();

        DecisionService decisionService = processEngine.getDecisionService();
        DmnDecisionResult result = decisionService.evaluateDecisionByKey("decision1")
                .variables(Map.of("input", 1))
                .evaluate();

        for (DmnDecisionResultEntries resultEntries : result) {
            String output = resultEntries.getEntry("output");
            System.out.println("Result = " + output);
        }

        processEngine.close();
    }
}
