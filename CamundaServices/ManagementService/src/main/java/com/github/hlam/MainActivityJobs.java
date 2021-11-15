package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public class MainActivityJobs {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setJobExecutorActivate(true)
//                .setJobExecutorActivate(false)
                .buildProcessEngine();

        deploy(processEngine, "Simple Activity Process", "simpleActivityJobDiagram.bpmn");
        deploy(processEngine, "Simple Activity Process 1", "simpleActivityJobDiagram1.bpmn");
        deploy(processEngine, "Simple Activity Process 2", "simpleActivityJobDiagram2.bpmn");
        deploy(processEngine, "Simple Activity Process 3", "simpleActivityJobDiagram3.bpmn");

        System.out.println("Main Thread Name = " + Thread.currentThread().getName());

        // asynchronous after -> none
        startProcess(processEngine, "simpleActivityProcess", "1");
        // asynchronous none -> before
        startProcess(processEngine, "simpleActivityProcess1", "2");
        // asynchronous before -> none
        startProcess(processEngine, "simpleActivityProcess2", "3");
        // asynchronous before-after -> none
        startProcess(processEngine, "simpleActivityProcess3", "4");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        processEngine.close();
    }

    private static ProcessInstance startProcess(ProcessEngine processEngine, String simpleActivityProcess, String s) {
        return processEngine.getRuntimeService()
                .startProcessInstanceByKey(simpleActivityProcess, s);
    }

    private static void deploy(ProcessEngine processEngine, String s, String s2) {
        processEngine.getRepositoryService().createDeployment()
                .name(s)
                .addClasspathResource(s2)
                .deploy();
    }
}
