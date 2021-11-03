package com.github.hlam.def;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.BeanDefinitionStoreException;

public class MainBuilderConfiguration {
    public static void main(String[] args) {
        ProcessEngineConfiguration processEngineConfiguration;

        processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        System.out.println("Default standalone configuration");
        printProcessEngineConfigurationInfo(processEngineConfiguration);

        ProcessEngine processEngine;
        {
            // Will be thrown ProcessEngineException, if no one H2 Database is running on jdbc:h2:tcp://localhost/activiti
            //        processEngine = processEngineConfiguration.buildProcessEngine();
            // But we can set manually in-memory database using
            processEngineConfiguration.setJdbcUrl("jdbc:h2:mem:my-own-db");
            // Setting the database schema update option to true needs to create tables in the database
            processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
            // And then process engine will be created successfully
            processEngine = processEngineConfiguration.buildProcessEngine();

            processEngine.close();
        }

        // In-memory database prefect for testing but this works only with h2 database
        processEngineConfiguration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        System.out.println("Default standalone in-memory configuration");
        printProcessEngineConfigurationInfo(processEngineConfiguration);
        {
            processEngine = processEngineConfiguration.buildProcessEngine();
            processEngine.close();
        }

        // Looks for camunda.cfg.xml or activiti.cfg.xml file parse one of them and then build the process engine
        // But these file needs to be filled by right way
        try {
            processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
            System.out.println("Default resource configuration");
            printProcessEngineConfigurationInfo(processEngineConfiguration);
            {
                processEngine = processEngineConfiguration.buildProcessEngine();
                processEngine.close();
            }
        } catch (BeanDefinitionStoreException ignored) {
            // If no one files not found exception will be thrown
        }
    }

    private static void printProcessEngineConfigurationInfo(ProcessEngineConfiguration processEngineConfiguration) {
        System.out.println("Standalone JDBC URL = '" + processEngineConfiguration.getJdbcUrl() + "'");
        System.out.println("Standalone Database Schema = '" + processEngineConfiguration.getDatabaseSchemaUpdate() + "'");
    }
}
