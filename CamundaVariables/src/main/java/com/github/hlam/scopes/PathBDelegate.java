package com.github.hlam.scopes;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import static com.github.hlam.scopes.MainDelegate.delegateExecution;

public class PathBDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        delegateExecution(execution, getClass());
    }
}
