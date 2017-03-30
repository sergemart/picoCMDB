package ru.sergm.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.sergm.picocmdb.dao.ManagedAreaDao;
import ru.sergm.picocmdb.domain.ManagedArea;
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


	public ManagedArea getManagedArea(Long managedAreaId) throws NoSuchObjectException {
		ManagedArea result =  managedAreaDao.findById(managedAreaId);
		if (result == null) throw new NoSuchObjectException("MANAGEDAREANOTFOUND", "No Managed Area identified by '" + managedAreaId + "' found.");
		return result;
	}


	public void createManagedArea(ManagedArea managedArea) throws ObjectAlreadyExistsException {
		managedAreaDao.save(managedArea);
	}


	public void deleteManagedArea(ManagedArea managedArea) throws NoSuchObjectException {
		managedAreaDao.delete(managedArea);
	}


	public void deleteManagedAreaByName(String managedAreaName) throws NoSuchObjectException {
		managedAreaDao.deleteByName(managedAreaName);
	}


}
