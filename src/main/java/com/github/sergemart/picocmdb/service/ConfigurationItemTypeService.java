package com.github.sergemart.picocmdb.service;

import java.util.List;

import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.exception.DependencyExistsException;


public interface ConfigurationItemTypeService {

	List<ConfigurationItemType> getAllConfigurationItemTypes();

	ConfigurationItemType getConfigurationItemType(String id)
			throws NoSuchObjectException;

	ConfigurationItemType createConfigurationItemType(ConfigurationItemType configurationItemType)
			throws ObjectAlreadyExistsException, WrongDataException;

	ConfigurationItemType updateConfigurationItemType(String existingConfigurationItemType, ConfigurationItemType configurationItemType)
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException;

	void deleteConfigurationItemType(String configurationItemTypeId)
			throws NoSuchObjectException, DependencyExistsException;

}
