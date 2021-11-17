package com.github.hlam.types;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class MainTypes {
    public static void main(String[] args) throws IOException {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        processEngine.getRepositoryService().createDeployment()
                .name("Variable Types Process")
                .addClasspathResource("types/typeProcess.bpmn")
                .deploy();

        Pojo pojo = new Pojo(1, "cde");
        ObjectValue objVal = Variables.objectValue(pojo)
                .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                .create();

        ClassPathResource classPathResource = new ClassPathResource("types" + File.separatorChar + "file.txt");
        FileValue fileVal = Variables.fileValue("file.txt")
                .encoding("UTF-8")
                .file(classPathResource.getFile())
                .mimeType("text/plain")
                .create();
        processEngine.getRuntimeService().createProcessInstanceByKey("typeProcess")
                .setVariable("intVar", Variables.integerValue(5))
                .setVariable("strVar", "abc")
                .setVariable("objVar", objVal)
                .setVariable("fileVar", fileVal)
                .execute();

        Execution userActivity = processEngine.getRuntimeService().createExecutionQuery()
                .activityId("userActivity").singleResult();
        TypedValue objVar;
        objVar = processEngine.getRuntimeService()
                .getVariableTyped(userActivity.getId(), "objVar", false);
        printObjVar(objVar);
        objVar = processEngine.getRuntimeService()
                .getVariableTyped(userActivity.getId(), "objVar");
        printObjVar(objVar);

        processEngine.close();
    }

    private static void printObjVar(TypedValue objVar) {
        if (objVar instanceof ObjectValue) {
            ObjectValue objectValue = (ObjectValue) objVar;
            try {
                System.out.println("Deserialized: " + objectValue.isDeserialized() +
                        " - " + objectValue.getType() + " var = " + objectValue.getValue());
            } catch (IllegalStateException ignored) {
                System.out.println("OBJ VAR MUST BE DESERIALIZED");
            }
        }
    }
}
