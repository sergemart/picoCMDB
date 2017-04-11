package com.github.sergemart.picocmdb.service;

import java.util.List;

import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


public interface RoleService {

	List<Role> getAllRoles();

	Role getRole(String id)
			throws NoSuchObjectException;
}
