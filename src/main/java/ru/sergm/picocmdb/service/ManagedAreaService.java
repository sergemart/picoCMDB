package ru.sergm.picocmdb.service;

import java.util.List;

import ru.sergm.picocmdb.domain.ManagedArea;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.exception.ObjectAlreadyExistsException;


public interface ManagedAreaService {

	List<ManagedArea> getAllManagedAreas();

	ManagedArea getManagedArea(Long id) throws NoSuchObjectException;

	void createManagedArea(ManagedArea managedArea) throws ObjectAlreadyExistsException;

	void deleteManagedAreaByName(String name) throws NoSuchObjectException;
}
