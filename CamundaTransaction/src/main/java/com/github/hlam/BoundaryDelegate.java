package com.github.hlam;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;

import static com.github.hlam.MainTransactionBoundaries.OBJ;

public class BoundaryDelegate implements JavaDelegate {
    public static volatile int flag = 0;

    @Override
    public void execute(DelegateExecution execution) {
        VariableMap variableMap = execution.getVariablesLocalTyped();

        String symbol = variableMap.getValue("symbol", String.class);
        Integer errorElement = variableMap.getValue("errorElement", Integer.class);
        System.out.println("Current symbol = " + symbol + " Error element = " + errorElement);
        symbol = String.valueOf((char) (symbol.charAt(0) + 1));
        execution.setVariable("symbol", symbol);
        execution.setVariable("symbol" + errorElement, symbol);
        if (errorElement == 0) {
            if (flag == 1) {
                synchronized (OBJ) {
                    try {
                        OBJ.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            flag = 1;
            throw new RuntimeException("ROLLBACK TRANSACTION");
        }
        execution.setVariable("errorElement", errorElement - 1);
    }
}
