package com.github.sergemart.picocmdb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.exception.DependencyExistsException;
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import com.github.sergemart.picocmdb.service.ConfigurationItemTypeService;


@RestController
@RequestMapping("/rest/configurationitemtypes")
public class ConfigurationItemTypeRestController {

    @Autowired
    private ConfigurationItemTypeService configurationItemTypeService;

	// -------------- READ --------------

	/**
	 * Lists all stored ConfigurationItemType objects when client calls GET /[collection]
	 * @return All ConfigurationItemType objects.
	 */
	@RequestMapping(method = RequestMethod.GET)
    public List<ConfigurationItemType> getAllConfigurationItemTypes() {
        return configurationItemTypeService.getAllConfigurationItemTypes();
    }


	/**
	 * Returns ConfigurationItemType object when client calls GET /[collection]/[object_ID]
	 * @return ConfigurationItemType object identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{configurationItemTypeId}")
    public ConfigurationItemType getConfigurationItemType(@PathVariable("configurationItemTypeId") String configurationItemTypeId)
			throws NoSuchObjectException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return configurationItemTypeService.getConfigurationItemType(configurationItemTypeId);
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Configuration Item Type identified by '" + configurationItemTypeId + "' found.");
		}
    }


	/**
	 * Returns ConfigurationItems objects of the ConfigurationItemType when client calls GET /[collection]/[object_ID]/[collection of dependants]
	 * @return ConfigurationItem objects of the ConfigurationItemType identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{configurationItemTypeId}/configurationitems")
	public List<ConfigurationItem> getConfigurationItemsOfType(@PathVariable("configurationItemTypeId") String configurationItemTypeId)
			throws NoSuchObjectException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return new ArrayList<>(configurationItemTypeService.getConfigurationItemType(configurationItemTypeId).getConfigurationItems());
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Configuration Item Type identified by '" + configurationItemTypeId + "' found.");
		}
	}

	// -------------- CREATE --------------

	/**
	 * Creates new ConfigurationItemType object when client calls POST /[collection] with JSON in a request body.
	 * @return Newly created ConfigurationItemType object.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ConfigurationItemType createConfigurationItemType(@RequestBody ConfigurationItemType configurationItemType)
			throws ObjectAlreadyExistsException, WrongDataException {
		return configurationItemTypeService.createConfigurationItemType(configurationItemType);
	}

	// -------------- UPDATE --------------

	/**
	 * Updates ConfigurationItemType object when client calls PUT /[collection]/[object_ID]
	 * @return Updated ConfigurationItemType object identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/{currentConfigurationItemTypeId}")
	public ConfigurationItemType updateConfigurationItemType(@PathVariable("currentConfigurationItemTypeId") String currentConfigurationItemTypeId, @RequestBody ConfigurationItemType newConfigurationItemTypeData)
			throws NoSuchObjectException, ObjectAlreadyExistsException, WrongDataException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return configurationItemTypeService.updateConfigurationItemType(currentConfigurationItemTypeId, newConfigurationItemTypeData);
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Configuration Item Type identified by '" + currentConfigurationItemTypeId + "' found.");
		}
	}

	// -------------- DELETE --------------

	/**
	 * Deletes ConfigurationItemType object when client calls DELETE /[collection]/[object_ID]
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/{configurationItemTypeId}")
	public void deleteConfigurationItemType(@PathVariable("configurationItemTypeId") String configurationItemTypeId)
			throws NoSuchObjectException, DependencyExistsException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			configurationItemTypeService.deleteConfigurationItemType(configurationItemTypeId);
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Configuration Item Type identified by '" + configurationItemTypeId + "' found.");
		}

	}

}
