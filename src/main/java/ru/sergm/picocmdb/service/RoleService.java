package ru.sergm.picocmdb.service;

import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchRoleException;


public interface RoleService {

	Role getRole(String name) throws NoSuchRoleException;
}
