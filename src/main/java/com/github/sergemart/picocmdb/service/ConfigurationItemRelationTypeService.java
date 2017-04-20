package com.github.sergemart.picocmdb.service;

import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;

import java.util.List;


public interface ConfigurationItemRelationTypeService {

	List<ConfigurationItemRelationType> getAllRelationTypes();

	ConfigurationItemRelationType getRelationType(String id)
			throws NoSuchObjectException;
}
