package com.github.sergemart.picocmdb.dao;

import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ConfigurationItemTypeDao extends CrudRepository <ConfigurationItemType, String> {

	List<ConfigurationItemType> findAll();

	ConfigurationItemType findById(String id);

}
