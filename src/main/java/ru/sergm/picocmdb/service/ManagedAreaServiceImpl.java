package ru.sergm.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.sergm.picocmdb.dao.ManagedAreaDao;
import ru.sergm.picocmdb.domain.ManagedArea;
import ru.sergm.picocmdb.exception.WrongDataException;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.exception.ObjectAlreadyExistsException;

import java.util.List;


@Service
public class ManagedAreaServiceImpl implements ManagedAreaService {

	@Autowired
	private ManagedAreaDao managedAreaDao;


	public List<ManagedArea> getAllManagedAreas() {
		return managedAreaDao.findAll();
	}


	@Transactional
	public ManagedArea createManagedArea(ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException {
		if (managedArea == null) throw new WrongDataException("MANAGEDAREABAD", "Managed Area is not provided.");
		// to check if the new name doesn't conflict with names of existing objects
		String newObjectName = managedArea.getName();
		if (newObjectName == null) throw new WrongDataException("MANAGEDAREABAD", "Managed Area name is not provided.");
		if (managedAreaDao.findByName(newObjectName) != null) throw new ObjectAlreadyExistsException("MANAGEDAREAEXISTS", "Managed Area named '" + newObjectName + "' already exists.");
		// to save data
		try {
			return managedAreaDao.save(managedArea);
		} catch (DataIntegrityViolationException e) {
			throw new WrongDataException("MANAGEDAREABAD", "Managed Area missing required fields.");
		}
	}


	@Transactional
	public ManagedArea updateManagedArea(Long currentManagedAreaId, ManagedArea newManagedAreaData)
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		if (currentManagedAreaId == null) throw new WrongDataException("MANAGEDAREABAD", "Current Managed Area ID is not provided.");
		if (newManagedAreaData == null) throw new WrongDataException("MANAGEDAREABAD", "New Managed Area data are not provided.");

		ManagedArea currentManagedArea = getManagedArea(currentManagedAreaId);
		// to check if the new name, when provided, doesn't conflict with names of existing objects
		String currentManagedAreaName = currentManagedArea.getName();
		String newManagedAreaName = newManagedAreaData.getName();
		if (newManagedAreaName != null) {
			if ( !newManagedAreaName.equals(currentManagedAreaName) ) { // when old and new names are the same, that's OK
				if (managedAreaDao.findByName(newManagedAreaName) != null)
					throw new ObjectAlreadyExistsException("MANAGEDAREAEXISTS", "Managed Area named '" + newManagedAreaName + "' already exists.");
			}
		}
		// to save data
		try {
			currentManagedArea.setName(newManagedAreaData.getName());
			currentManagedArea.setDescription(newManagedAreaData.getDescription());
			return managedAreaDao.save(currentManagedArea);
		} catch (DataIntegrityViolationException e) {
			throw new WrongDataException("MANAGEDAREABAD", "Managed Area missing required fields.");
		}
	}


	public ManagedArea getManagedArea(Long managedAreaId)
			throws NoSuchObjectException {
		ManagedArea result =  managedAreaDao.findById(managedAreaId);
		if (result == null) throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		return result;
	}


	public void deleteManagedArea(Long managedAreaId)
			throws NoSuchObjectException {
		try {
			managedAreaDao.delete(managedAreaId);
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		}
	}


}
