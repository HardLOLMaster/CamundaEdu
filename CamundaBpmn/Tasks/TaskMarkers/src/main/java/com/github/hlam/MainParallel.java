package com.github.hlam;

import static java.lang.Thread.sleep;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.Incident;

public class MainParallel implements JavaDelegate {

    public static void main(String[] args) throws InterruptedException {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .setJobExecutorActivate(true)
            .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("parallel.bpmn")
            .deploy();

        List<String> list = IntStream.range(0, 1000).mapToObj(value -> String.valueOf(value).repeat(3)).collect(Collectors.toList());
        processEngine.getRuntimeService().startProcessInstanceByKey("parallel",
            Map.of("list", list));

        while (processEngine.getRuntimeService().createProcessInstanceQuery().count() != 0) {
            sleep(200);
            long count = processEngine.getRuntimeService().createIncidentQuery().count();
            if (count != 0) {
                break;
            }
        }

        processEngine.close();
    }

    @Override
    public void execute(DelegateExecution execution) {

//        Object element = execution.getVariable("element");
//        if (!("ccc".equals(element))) {
//            throw new RuntimeException();
//        }
        System.out.println("Executor " + Thread.currentThread().getName()
//            + " "
//            + element
            + " "
            + execution.getVariable("loopCounter"));
    }
}
