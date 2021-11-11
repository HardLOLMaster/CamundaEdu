package com.github.hlam;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.Collections;
import java.util.List;

public class MainAuthorization {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .setAuthorizationEnabled(true)
                .buildProcessEngine();

        IdentityService identityService = processEngine.getIdentityService();
        AuthorizationService authorizationService = processEngine.getAuthorizationService();

        Group group = identityService.newGroup(Groups.CAMUNDA_ADMIN);
        group.setType(Groups.CAMUNDA_ADMIN);
        group.setName(Groups.CAMUNDA_ADMIN);
        identityService.saveGroup(group);
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
            identityService.createMembership(user.getId(), group.getId());
        }
        {
            Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
            // GroupId cannot be used in global auth
//        authorization.setGroupId(group.getId());
            authorization.setUserId("*");
            authorization.setResource(Resources.USER);
//        authorization.setResourceType(Resources.AUTHORIZATION.resourceType());
            authorization.setResourceId("*");
            authorization.addPermission(Permissions.READ);
//            authorization.addPermission(Permissions.DELETE);
            authorizationService.saveAuthorization(authorization);
        }

        printUsers(identityService);

        identityService.setAuthenticatedUserId("1");

        try {
            identityService.deleteUser("2");
            throw new RuntimeException("User cannon be deleted");
        } catch (AuthorizationException ignored) {
        }

        printUsers(identityService);

        // work
//        identityService.setAuthentication(null, Collections.singletonList(group.getId()));
        // work
//        identityService.clearAuthentication();
        // work
        identityService.setAuthenticatedUserId("2");
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        if (processEngineConfiguration instanceof ProcessEngineConfigurationImpl) {
            ProcessEngineConfigurationImpl engineConfiguration = (ProcessEngineConfigurationImpl) processEngineConfiguration;
            engineConfiguration.setAdminUsers(List.of("1", "2"));
        }
        // we cannot close engine without admin privileges
        processEngine.close();
    }

    private static void printUsers(IdentityService identityService) {
        System.out.println("***");
        System.out.println("Users");
        List<User> users = identityService.createUserQuery().unlimitedList();
        if (users.isEmpty())
            System.out.println("EMPTY");
        for (User user : users) {
            System.out.println(user.getId() + ": " + user.getFirstName() + " | " + user.getLastName());
        }
        System.out.println("***");
    }
}
