package com.sharafutdinov.aircraft_maintenance.service.scheduled_task;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public Map<UserRepresentation, List<RoleRepresentation>> getUsersWithRoles(String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.list()
                .stream()
                .filter(UserRepresentation::isEnabled)
                .toList();

        return users.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> getUserRoles(user.getId(), roleName)
                ));
    }

    public List<UserRepresentation> getUserRepresentation(Map<UserRepresentation, List<RoleRepresentation>> usersKeycloak) {
        List<UserRepresentation> users = new ArrayList<>();

        usersKeycloak.forEach((key1, value1) -> {
            List<RoleRepresentation> value = value1;

            if (!value.isEmpty())
                users.add(key1);
        });

        return users;
    }

    private List<RoleRepresentation> getUserRoles(String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userId);
        RoleMappingResource roleMappingsResource = userResource.roles();

        List<RoleRepresentation> allRoles = roleMappingsResource.getAll().getRealmMappings();

        return allRoles.stream()
                .filter(role -> roleName.equals(role.getName()))
                .collect(Collectors.toList());
    }
}
