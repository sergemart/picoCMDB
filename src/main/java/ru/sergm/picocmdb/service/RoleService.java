package ru.sergm.picocmdb.service;

import java.util.List;

import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchObjectException;


public interface RoleService {

	List<Role> getAllRoles();

	Role getRole(String id) throws NoSuchObjectException;
}
