package ru.sergm.picocmdb.service;

import java.util.List;

import ru.sergm.picocmdb.domain.ManagedArea;
import ru.sergm.picocmdb.exception.WrongDataException;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.exception.ObjectAlreadyExistsException;


public interface ManagedAreaService {

	List<ManagedArea> getAllManagedAreas();

	ManagedArea createManagedArea(ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException;

	ManagedArea getManagedArea(Long id)
			throws NoSuchObjectException;

	ManagedArea updateManagedArea(Long existingManagedAreaId, ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException;

	void deleteManagedArea(Long managedAreaId)
			throws NoSuchObjectException;

}
