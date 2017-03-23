package ru.sergm.picocmdb.dao;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

import ru.sergm.picocmdb.domain.Dummy;


public interface DummyDao extends CrudRepository <Dummy, Long> {

	List<Dummy> findAll();

	Dummy findByName(String name);

	void deleteByName(String name);

}
