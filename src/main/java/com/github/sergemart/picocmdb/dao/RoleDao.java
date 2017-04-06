package com.github.sergemart.picocmdb.dao;

import com.github.sergemart.picocmdb.domain.Role;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface RoleDao extends CrudRepository <Role, String> {

	List<Role> findAll();

	Role findById(String id);

}
