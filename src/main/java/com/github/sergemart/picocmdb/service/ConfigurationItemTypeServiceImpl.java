package com.github.sergemart.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.sergemart.picocmdb.dao.ConfigurationItemTypeDao;
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;

import java.util.List;


@Service
public class ConfigurationItemTypeServiceImpl implements ConfigurationItemTypeService {

	@Autowired
	private ConfigurationItemTypeDao configurationItemTypeDao;

	// -------------- READ --------------
	
	/**
	 * Reads all stored ConfigurationItemType objects.
	 */
	public List<ConfigurationItemType> getAllConfigurationItemTypes() {
		return configurationItemTypeDao.findAll();
	}


	/**
	 * Reads stored ConfigurationItemType object.
	 */
	public ConfigurationItemType getConfigurationItemType(String configurationItemTypeId)
			throws NoSuchObjectException {
		ConfigurationItemType result =  configurationItemTypeDao.findById(configurationItemTypeId);
		if (result == null) throw new NoSuchObjectException("CONFIGURATIONITEMTYPENOTFOUND", "No Configuration Item Type identified by '" + configurationItemTypeId + "' found.");
		return result;
	}

	// -------------- CREATE --------------

	/**
	 * Creates new ConfigurationItemType object.
	 */
	public ConfigurationItemType createConfigurationItemType(ConfigurationItemType configurationItemType)
			throws ObjectAlreadyExistsException, WrongDataException {
		if (configurationItemType == null) throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "Configuration Item Type is not provided.");
		// check if the new id doesn't conflict with ids of existing objects
		String newObjectId = configurationItemType.getId();
		if (newObjectId == null) throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "Configuration Item Type ID is not provided.");
		if (configurationItemTypeDao.findById(newObjectId) != null) throw new ObjectAlreadyExistsException("CONFIGURATIONITEMTYPEEXISTS", "Configuration Item Type identified by '" + newObjectId + "' already exists.");
		try { // to persist
			return configurationItemTypeDao.save(configurationItemType);
		} catch (DataIntegrityViolationException e) {
			throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "Configuration Item Type missing required fields.");
		}
	}

	// -------------- UPDATE --------------

	/**
	 * Updates stored ConfigurationItemType object.
	 * @param currentConfigurationItemTypeId Stored ConfigurationItemType object ID.
	 * @param newConfigurationItemTypeData Data to be updated.
	 */
	@Transactional(rollbackFor = WrongDataException.class)
	public ConfigurationItemType updateConfigurationItemType(String currentConfigurationItemTypeId, ConfigurationItemType newConfigurationItemTypeData)
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		if (currentConfigurationItemTypeId == null) throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "Current Configuration Item Type ID is not provided.");
		if (newConfigurationItemTypeData == null) throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "New Configuration Item Type data are not provided.");

		ConfigurationItemType currentConfigurationItemType = this.getConfigurationItemType(currentConfigurationItemTypeId);
		try { // to persist
			newConfigurationItemTypeData.setId(currentConfigurationItemTypeId); // enrich new data with ID, making them an object to get JPA call 'merge' instead of 'persist'
			return configurationItemTypeDao.save(newConfigurationItemTypeData);
		} catch (DataIntegrityViolationException e) {
			throw new WrongDataException("CONFIGURATIONITEMTYPEBAD", "Configuration Item Type missing required fields.");
		}
	}

	// -------------- DELETE --------------

	/**
	 * Deletes stored ConfigurationItemType object.
	 */
	public void deleteConfigurationItemType(String configurationItemTypeId)
			throws NoSuchObjectException {
		try { // to persist
			configurationItemTypeDao.delete(configurationItemTypeId);
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchObjectException("CONFIGURATIONITEMTYPENOTFOUND", "No Configuration Item Type identified by '" + configurationItemTypeId + "' found.");
		}
	}


}
