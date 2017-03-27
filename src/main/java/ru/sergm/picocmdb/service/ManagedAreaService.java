package ru.sergm.picocmdb.service;

import ru.sergm.picocmdb.domain.ManagedArea;
import ru.sergm.picocmdb.exception.NoSuchObjectException;

import java.util.List;


public interface ManagedAreaService {

	List<ManagedArea> getAllManagedAreas();

	ManagedArea getManagedArea(Long id) throws NoSuchObjectException;
}
