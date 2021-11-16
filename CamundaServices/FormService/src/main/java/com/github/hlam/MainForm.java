package com.github.hlam;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class MainForm {
    public static void main(String[] args) throws IOException {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Form Process")
                .addClasspathResource("form.html")
                .addClasspathResource("formProcess.bpmn")
                .deploy();

        processEngine.getRuntimeService().startProcessInstanceByKey("formProcess");

        FormService formService = processEngine.getFormService();

        Task task = processEngine.getTaskService().createTaskQuery().singleResult();
        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        System.out.println(renderedTaskForm);

        InputStream deployedTaskForm = formService.getDeployedTaskForm(task.getId());
        InputStreamReader inputStreamReader = new InputStreamReader(deployedTaskForm);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        bufferedReader.lines().forEach(System.out::println);
        bufferedReader.close();
        inputStreamReader.close();
        deployedTaskForm.close();

        System.out.println();
        {
            ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().singleResult();
            formService.submitTaskForm(task.getId(), Map.of("var", "val"));
            List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .unlimitedList();
            for (HistoricVariableInstance variableInstance : historicVariableInstances) {
                System.out.println(variableInstance.getName() + " = " + variableInstance.getValue());
            }
        }
        System.out.println();
        {
            ProcessInstance processInstance =
                    processEngine.getRuntimeService().startProcessInstanceByKey("formProcess");
            task = processEngine.getTaskService().createTaskQuery().singleResult();
            formService.submitTaskForm(task.getId(), Map.of());
            List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstance.getId())
                    .unlimitedList();
            for (HistoricVariableInstance variableInstance : historicVariableInstances) {
                System.out.println(variableInstance.getName() + " = " + variableInstance.getValue());
            }
        }

        processEngine.close();
    }
}
