package com.github.sergemart.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.github.sergemart.picocmdb.dao.RoleDao;
import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	// -------------- READ --------------

	public List<Role> getAllRoles() {
		return roleDao.findAll();
	}


	public Role getRole(String entityId) throws NoSuchObjectException {
		Role result =  roleDao.findById(entityId);
		if (result == null) throw new NoSuchObjectException("ROLENOTFOUND", "No Role identified by '" + entityId + "' found.");
		return result;
	}

}
