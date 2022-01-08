package com.github.hlam;

import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.test.mock.Mocks;

public class MainTransaction {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .buildProcessEngine();

        Mocks.register("cancelDelegate", (JavaDelegate) execution -> System.out.println("cancelDelegate"));
        Mocks.register("successfulDelegate", (JavaDelegate) execution -> System.out.println("successfulDelegate"));
        Mocks.register("transactionDelegate", (JavaDelegate) execution -> System.out.println("transactionDelegate"));
        Mocks.register("compensationDelegateInner", (JavaDelegate) execution -> System.out.println("compensationDelegateInner"));
        Mocks.register("compensationDelegateEvent", (JavaDelegate) execution -> System.out.println("compensationDelegateEvent"));

        processEngine.getRepositoryService().createDeployment()
            .addClasspathResource("transaction.bpmn")
            .deploy();

        processEngine.getRuntimeService().startProcessInstanceByKey("transaction", Map.of("cancel", true));
        System.out.println("***");
        processEngine.getRuntimeService().startProcessInstanceByKey("transaction", Map.of(
            "cancel", false,
            "compensation", true
        ));

        processEngine.close();
    }
}
