package com.github.hlam;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.identity.Authentication;

import java.util.List;

public class MainIdentity {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        IdentityService identityService = processEngine.getIdentityService();

        printIdentityInfo(identityService);

        Tenant tenant = identityService.newTenant("1");
        tenant.setName("TenantName");
        identityService.saveTenant(tenant);

        User user = identityService.newUser("1");
        user.setPassword("password");
        user.setLastName("LastName");
        user.setFirstName("FirstName");
        user.setEmail("email@email.email");
        identityService.saveUser(user);

        Group group = identityService.newGroup("1");
        group.setType("Type");
        group.setName("GroupName");
        identityService.saveGroup(group);

        identityService.createMembership(user.getId(), group.getId());
        identityService.createTenantUserMembership(tenant.getId(), user.getId());
        identityService.createTenantGroupMembership(tenant.getId(), group.getId());

        printIdentityInfo(identityService);

        User user1 = identityService.createUserQuery()
                .memberOfTenant(tenant.getId())
                .singleResult();
        System.out.println(user1.getId() + ": " + user1.getFirstName() + " | " + user1.getLastName());

        List<String> userInfoKeys = identityService.getUserInfoKeys(user.getId());
        System.out.println("---");
        System.out.println("User info keys");
        for (String userInfoKey : userInfoKeys) {
            System.out.println(userInfoKey + " = " + identityService.getUserInfo(user.getId(), userInfoKey));
        }
        System.out.println("---");

        identityService.deleteGroup(group.getId());
        identityService.deleteTenant(tenant.getId());
        identityService.deleteUser(user.getId());

        printIdentityInfo(identityService);

        processEngine.close();
    }

    private static void printIdentityInfo(IdentityService identityService) {
        System.out.println("***");
        System.out.println("Users");
        List<User> users = identityService.createUserQuery().unlimitedList();
        if (users.isEmpty())
            System.out.println("EMPTY");
        for (User user : users) {
            System.out.println(user.getId() + ": " + user.getFirstName() + " | " + user.getLastName());
        }
        System.out.println("***");
        System.out.println("Groups");
        List<Group> groups = identityService.createGroupQuery().unlimitedList();
        if (groups.isEmpty())
            System.out.println("EMPTY");
        for (Group group : groups) {
            System.out.println(group.getId() + ": " + group.getType() + " | " + group.getName());
        }
        System.out.println("***");
        System.out.println("Tenants");
        List<Tenant> tenants = identityService.createTenantQuery().unlimitedList();
        if (tenants.isEmpty())
            System.out.println("EMPTY");
        for (Tenant tenant : tenants) {
            System.out.println(tenant.getId() + ": " + tenant.getName());
        }
        System.out.println("***");
    }
}
