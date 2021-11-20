package com.github.hlam;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;

public class MainProcessApplication {
    public static void main(String[] args) {
        SimpleProcessApplication simpleProcessApplication = deploySimpleApplication();
        // here is no engine, and we must deploy first process with engine configuration
        SimpleProcessApplicationAnnotation simpleProcessApplicationAnnotation = deployAnnotationApplication();
        ProcessEngine processEngine = BpmPlatform.getDefaultProcessEngine();
        processEngine.getRuntimeService().startProcessInstanceByKey("simpleProcess");
        simpleProcessApplication.undeploy();
        simpleProcessApplicationAnnotation.undeploy();
    }

    private static SimpleProcessApplicationAnnotation deployAnnotationApplication() {
        SimpleProcessApplicationAnnotation simpleProcessApplicationAnnotation = new SimpleProcessApplicationAnnotation();
        simpleProcessApplicationAnnotation.deploy();
        return simpleProcessApplicationAnnotation;
    }

    private static SimpleProcessApplication deploySimpleApplication() {
        SimpleProcessApplication simpleProcessApplication = new SimpleProcessApplication();
        simpleProcessApplication.deploy();
        return simpleProcessApplication;
    }
}
