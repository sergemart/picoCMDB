package com.github.sergemart.picocmdb.dao;

import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ConfigurationItemDao extends CrudRepository <ConfigurationItem, Long> {

	List<ConfigurationItem> findAll();

	ConfigurationItem findById(Long id);

	ConfigurationItem findByName(String name);

	void deleteByName(String name);

}
