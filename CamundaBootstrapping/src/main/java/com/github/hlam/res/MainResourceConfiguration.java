package com.github.hlam.res;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class MainResourceConfiguration {
    public static void main(String[] args) {
        ProcessEngineConfiguration processEngineConfiguration;
        // By default, resource configuration looks for camunda.cfg.xml/activiti.cfg.xml (this is spring xml config)
        // and looks for processEngineConfiguration bean
//        processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        processEngineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("my-camunda.cfg.xml",
                        "myProcessEngineConfiguration");

        ProcessEngine processEngine;
        processEngine = processEngineConfiguration.buildProcessEngine();

        processEngine.close();
    }
}
