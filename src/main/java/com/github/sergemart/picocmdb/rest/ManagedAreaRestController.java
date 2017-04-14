package com.github.sergemart.picocmdb.rest;

import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.service.ManagedAreaService;


@RestController
@RequestMapping("/rest/managedareas")
public class ManagedAreaRestController {

    @Autowired
    private ManagedAreaService managedAreaService;

	// -------------- READ --------------

	/**
	 * Lists all stored ManagedArea objects when client calls GET /[collection]
	 * @return All ManagedArea objects.
	 */
	@RequestMapping(method = RequestMethod.GET)
    public List<ManagedArea> getAllManagedAreas() {
        return this.managedAreaService.getAllManagedAreas();
    }


	/**
	 * Returns ManagedArea object when client calls GET /[collection]/[object_ID]
	 * @return ManagedArea object identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{managedAreaId}")
    public ManagedArea getManagedArea(@PathVariable("managedAreaId") String managedAreaId)
			throws NoSuchObjectException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return this.managedAreaService.getManagedArea(Long.valueOf(managedAreaId));
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		}
    }


	/**
	 * Returns ConfigurationItems objects linked to the ManagedArea when client calls GET /[collection]/[object_ID]/[collection_of_linked_objects]
	 * @return ConfigurationItem objects linked to the ManagedArea, which is identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{managedAreaId}/configurationitems")
	public List<ConfigurationItem> getConfigurationItemsOfManagedArea(@PathVariable("managedAreaId") String managedAreaId)
			throws NoSuchObjectException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return new ArrayList<>(this.managedAreaService.getManagedArea(Long.valueOf(managedAreaId)).getConfigurationItems());
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		}
	}


	// -------------- CREATE --------------

	/**
	 * Creates new ManagedArea object when client calls POST /[collection] with JSON in a request body.
	 * @return Newly created ManagedArea object.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ManagedArea createManagedArea(@RequestBody ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException {
		return this.managedAreaService.createManagedArea(managedArea);
	}

	// -------------- UPDATE --------------

	/**
	 * Updates ManagedArea object when client calls PUT /[collection]/[object_ID]
	 * @return Updated ManagedArea object identified by URL subpart
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/{currentManagedAreaId}")
	public ManagedArea updateManagedArea(@PathVariable("currentManagedAreaId") String currentManagedAreaId, @RequestBody ManagedArea newManagedAreaData)
			throws NoSuchObjectException, ObjectAlreadyExistsException, WrongDataException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			return this.managedAreaService.updateManagedArea(Long.valueOf(currentManagedAreaId), newManagedAreaData);
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + currentManagedAreaId + "' found.");
		}
	}

	// -------------- DELETE --------------

	/**
	 * Deletes ManagedArea object when client calls DELETE /[collection]/[object_ID]
	 */
	@RequestMapping(method = RequestMethod.DELETE, path = "/{managedAreaId}")
	public void deleteManagedArea(@PathVariable("managedAreaId") String managedAreaId)
			throws NoSuchObjectException {
		try { // to more precise format checking; general handler of last resort is in RestExceptionHandler class
			this.managedAreaService.deleteManagedArea(Long.valueOf(managedAreaId));
		} catch (NumberFormatException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		}

	}

}
