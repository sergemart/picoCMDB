package com.github.sergemart.picocmdb.dao;

import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface ConfigurationItemRelationTypeDao extends CrudRepository <ConfigurationItemRelationType, String> {

	List<ConfigurationItemRelationType> findAll();

	ConfigurationItemRelationType findById(String id);

}
