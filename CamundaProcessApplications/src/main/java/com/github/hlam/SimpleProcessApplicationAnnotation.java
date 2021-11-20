package com.github.hlam;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.EmbeddedProcessApplication;

@ProcessApplication(name = "Simple Application", deploymentDescriptors = "dir/processes.xml")
public class SimpleProcessApplicationAnnotation extends EmbeddedProcessApplication {
}
