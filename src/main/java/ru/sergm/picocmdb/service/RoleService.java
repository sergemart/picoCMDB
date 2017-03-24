package ru.sergm.picocmdb.service;

import java.util.List;

import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchRoleException;


public interface RoleService {

	List<Role> getAllRoles();

	Role getRole(String name) throws NoSuchRoleException;
}
