package ru.sergm.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.sergm.picocmdb.dao.RoleDao;
import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchRoleException;


@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;


	public Role getRole(String roleName) throws NoSuchRoleException {
		Role result =  roleDao.findByName(roleName);
		if (result == null) throw new NoSuchRoleException();
		return result;
	}

}
