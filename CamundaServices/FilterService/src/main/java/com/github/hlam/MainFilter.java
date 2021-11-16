package com.github.hlam;

import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.filter.Filter;

import java.util.Map;

public class MainFilter {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Filter Process")
                .addClasspathResource("filterProcess.bpmn")
                .deploy();

        //true
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "1", "var2", "1val"));
        //true
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "1", "var2", "2val"));
        //true
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "1", "var2", "val"));
        //true
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "1", "var2", "v2al"));
        //true
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "2", "var2", "2val"));
        //false
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "2", "var2", "2val2"));
        //false
        processEngine.getRuntimeService().startProcessInstanceByKey("filterProcess",
                Map.of("var1", "2", "var2", "2va2l2"));

        FilterService filterService = processEngine.getFilterService();
        Filter taskFilter = filterService.newTaskFilter("TaskFilter");
        taskFilter.setQuery(processEngine.getTaskService().createTaskQuery()
                .or()
                .processVariableValueEquals("var1", "1")
                .processVariableValueLike("var2", "%val")
                .endOr());
        filterService.saveFilter(taskFilter);

        System.out.println("Count of filtered tasks = " + filterService.count(taskFilter.getId()));

        processEngine.close();
    }
}
