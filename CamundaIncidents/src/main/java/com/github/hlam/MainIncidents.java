package com.github.hlam;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.runtime.Job;

import java.util.Map;

public class MainIncidents {
    public static void main(String[] args) throws InterruptedException {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setJobExecutorActivate(true)
                .buildProcessEngine();
        processEngine.getRepositoryService().createDeployment()
                .name("Incident Process")
                .addClasspathResource("simpleProcess.bpmn")
                .deploy();

        Map<String, Object> exception = Map.of("exception", true, "incident", false);
        processEngine.getRuntimeService().startProcessInstanceByKey("simpleProcess", exception);

        while (processEngine.getRuntimeService().createIncidentQuery().count() == 0) {
            Thread.sleep(500);
        }
        Incident incident;
        incident = processEngine.getRuntimeService().createIncidentQuery().singleResult();
        // you cannot resolve failedJob manually
//        processEngine.getRuntimeService().resolveIncident(incident.getId());
        Job job = processEngine.getManagementService().createJobQuery().singleResult();
        processEngine.getRuntimeService().setVariable(job.getExecutionId(), "exception", false);
        processEngine.getManagementService().executeJob(job.getId());
        while (processEngine.getRuntimeService().createProcessInstanceQuery().count() != 0) {
            Thread.sleep(500);
        }

        Map<String, Object> incidentMap = Map.of("exception", false, "incident", true);
        // in documentation doesn't say what mean argument 'configuration', I don't know what I need to put in
        processEngine.getRuntimeService().startProcessInstanceByKey("simpleProcess", incidentMap);

//        while (processEngine.getRuntimeService().createIncidentQuery().count() == 0) {
//            Thread.sleep(500);
//        }
//        incident = processEngine.getRuntimeService().createIncidentQuery().singleResult();
//        processEngine.getRuntimeService().resolveIncident(incident.getId());
//        while (processEngine.getRuntimeService().createProcessInstanceQuery().count() != 0) {
//            Thread.sleep(500);
//        }

        processEngine.close();
    }
}
