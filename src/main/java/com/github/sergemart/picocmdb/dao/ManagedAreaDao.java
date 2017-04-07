package com.github.sergemart.picocmdb.dao;

import com.github.sergemart.picocmdb.domain.ManagedArea;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ManagedAreaDao extends CrudRepository <ManagedArea, Long> {

	List<ManagedArea> findAll();

	ManagedArea findById(Long id);

	ManagedArea findByName(String name);

	void deleteByName(String name);

}
