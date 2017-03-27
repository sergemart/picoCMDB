package ru.sergm.picocmdb.dao;

import org.springframework.data.repository.CrudRepository;
import ru.sergm.picocmdb.domain.ManagedArea;

import java.util.List;


public interface ManagedAreaDao extends CrudRepository <ManagedArea, Long> {

	List<ManagedArea> findAll();

	ManagedArea findById(Long id);

	ManagedArea findByName(String name);

}
