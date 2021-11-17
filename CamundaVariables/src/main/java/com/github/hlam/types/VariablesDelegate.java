package com.github.hlam.types;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class VariablesDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws IOException {
        printVar(execution, "intVar");
        printVar(execution, "strVar");
        printVar(execution, "objVar");
        printVar(execution, "fileVar");

        printVar(execution.getVariableTyped("intVar"));
        printVar(execution.getVariableTyped("strVar"));
        printVar(execution.getVariableTyped("objVar", false));
        printVar(execution.getVariableTyped("fileVar"));
    }

    private void printVar(DelegateExecution execution, String varName) {
        System.out.println("Var " + varName + " = " + execution.getVariable(varName));
    }

    private void printVar(TypedValue value) throws IOException {
        if (value instanceof ObjectValue) {
            ObjectValue objectValue = (ObjectValue) value;
            System.out.println("Deserialized: " + objectValue.isDeserialized() +
                    " - " + objectValue.getType() + " var = " + objectValue.getValue());
        } else {
            Object typeValue = value.getValue();
            if (typeValue instanceof ByteArrayInputStream) {
                ByteArrayInputStream inputStream = (ByteArrayInputStream) typeValue;
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                System.out.println(value.getType() + " var:");
                bufferedReader.lines().forEach(System.out::println);
                System.out.println("FILE END");
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } else {
                System.out.println(value.getType() + " var = " + typeValue);
            }
        }
    }
}
