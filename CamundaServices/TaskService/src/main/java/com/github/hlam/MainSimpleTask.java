package com.github.hlam;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskCountByCandidateGroupResult;
import org.camunda.bpm.engine.task.TaskReport;

public class MainSimpleTask {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setAuthorizationEnabled(true)
                .buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
        processEngine.getRepositoryService().createDeployment()
                .name("Task BPM")
                .addClasspathResource("taskSimple.bpmn")
                .deploy();

        createAuthorization(processEngine);

        ProcessInstance processInstance;
        startProcess(runtimeService);
        System.out.println("***\nTask Report");
        TaskReport taskReport = taskService.createTaskReport();
        for (TaskCountByCandidateGroupResult taskCountByCandidateGroupResult : taskReport.taskCountByCandidateGroup()) {
            System.out.println(taskCountByCandidateGroupResult.getGroupName() + " " + taskCountByCandidateGroupResult.getTaskCount());
        }
        System.out.println("***");

        System.out.println("#1");
        taskService.complete(getTask(taskService).getId());

        processInstance = startProcess(runtimeService);
        System.out.println("#2");
        Task task = getTask(taskService);
        System.out.println("Delete process with task");
        runtimeService.deleteProcessInstance(processInstance.getId(), "Because I need");
        taskService.deleteTask(task.getId());

        startProcess(runtimeService);
        System.out.println("#3");
        processEngine.getIdentityService().setAuthenticatedUserId("1");
        taskService.setOwner(getTask(taskService).getId(), "1");
        taskService.delegateTask(getTask(taskService).getId(), "2");
        if (!DelegationState.PENDING.equals(getTask(taskService).getDelegationState()))
            throw new RuntimeException("Task must be in DelegationState.PENDING");
        processEngine.getIdentityService().setAuthenticatedUserId("3");
        // https://forum.camunda.org/t/taskservice-complete-method-documentation-problem/31193
        taskService.complete(getTask(taskService).getId());
        processEngine.getIdentityService().clearAuthentication();
        processEngine.close();
    }

    private static void createAuthorization(ProcessEngine processEngine) {
        AuthorizationService authorizationService = processEngine.getAuthorizationService();
        IdentityService identityService = processEngine.getIdentityService();
        {
            User user = identityService.newUser("1");
            user.setEmail("email1@email.email");
            user.setFirstName("FirstName1");
            user.setLastName("LastName1");
            user.setPassword("password");
            identityService.saveUser(user);
        }
        {
            User user = identityService.newUser("2");
            user.setEmail("email2@email.email");
            user.setFirstName("FirstName2");
            user.setLastName("LastName2");
            user.setPassword("password");
            identityService.saveUser(user);
        }
        {
            User user = identityService.newUser("3");
            user.setEmail("email3@email.email");
            user.setFirstName("FirstName3");
            user.setLastName("LastName3");
            user.setPassword("password");
            identityService.saveUser(user);
        }

        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
        authorization.addPermission(Permissions.ALL);
        authorization.setUserId("*");
        authorization.setResource(Resources.TASK);
        authorization.setResourceId("*");
        authorizationService.saveAuthorization(authorization);
    }

    private static Task getTask(TaskService taskService) {
        return taskService.createTaskQuery().singleResult();
    }

    private static ProcessInstance startProcess(RuntimeService runtimeService) {
        return runtimeService.startProcessInstanceByKey("taskProcess");
    }
}
