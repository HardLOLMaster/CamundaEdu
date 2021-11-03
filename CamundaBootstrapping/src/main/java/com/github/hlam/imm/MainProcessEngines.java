package com.github.hlam.imm;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngines;

public class MainProcessEngines {
    public static void main(String[] args) {
        // ProcessEngines looking for camunda.xml.cfg/activiti.cfg.xml and parse them
        // By default engine without name will be parsed with 'default' name
        // Also if you want to use this method every process configuration bean must have 'processEngineConfiguration' name
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(ProcessEngines.getProcessEngines().size());
        System.out.println(defaultProcessEngine.getProcessEngineConfiguration().getDatabaseSchemaUpdate());
        defaultProcessEngine.close();
    }
}
