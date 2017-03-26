package ru.sergm.picocmdb.dao;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

import ru.sergm.picocmdb.domain.Role;


public interface RoleDao extends CrudRepository <Role, String> {

	List<Role> findAll();

	Role findById(String id);

	void deleteById(String id);

}
