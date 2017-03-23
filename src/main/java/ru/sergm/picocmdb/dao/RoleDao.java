package ru.sergm.picocmdb.dao;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

import ru.sergm.picocmdb.domain.Role;


public interface RoleDao extends CrudRepository <Role, Long> {

	List<Role> findAll();

	Role findByName(String name);

	void deleteByName(String name);

}
