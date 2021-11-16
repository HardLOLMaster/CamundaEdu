package com.github.hlam.scopes;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class MainDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("-----------");
        System.out.println(getClass().getName());

        printVars(execution);

        System.out.println("-----------");
    }

    public static void delegateExecution(DelegateExecution execution, Class<?> clazz) {
        System.out.println("-----------");
        System.out.println(clazz.getName());

        printVars(execution);
        execution.setVariable("var2", clazz.getSimpleName());
        execution.setVariableLocal("var3", clazz.getSimpleName());
        execution.setVariableLocal("var4", clazz.getSimpleName());

        System.out.println("-----------");
    }

    private static void printVars(DelegateExecution execution) {
        printVar(execution, 1);
        printVar(execution, 2);
        printVar(execution, 3);
        printVar(execution, 4);
    }

    private static void printVar(DelegateExecution execution, int var) {
        System.out.println("Var " + var + " = " + execution.getVariable("var" + var));
        System.out.println("Var " + var + " Local = " + execution.getVariableLocal("var" + var));
    }
}
