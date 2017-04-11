package com.github.sergemart.picocmdb.service;

import java.util.List;

import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;


public interface ManagedAreaService {

	List<ManagedArea> getAllManagedAreas();

	ManagedArea getManagedArea(Long id)
			throws NoSuchObjectException;

	ManagedArea createManagedArea(ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException;

	ManagedArea updateManagedArea(Long existingManagedAreaId, ManagedArea managedArea)
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException;

	void deleteManagedArea(Long managedAreaId)
			throws NoSuchObjectException;

}
