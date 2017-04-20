package com.github.sergemart.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.github.sergemart.picocmdb.dao.ConfigurationItemRelationTypeDao;
import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


@Service
public class ConfigurationItemRelationTypeServiceImpl implements ConfigurationItemRelationTypeService {

	@Autowired
	private ConfigurationItemRelationTypeDao entityDao;

	// -------------- READ --------------

	public List<ConfigurationItemRelationType> getAllRelationTypes() {
		return entityDao.findAll();
	}


	public ConfigurationItemRelationType getRelationType(String entityId) throws NoSuchObjectException {
		ConfigurationItemRelationType result =  entityDao.findById(entityId);
		if (result == null) throw new NoSuchObjectException("RELATIONTYPENOTFOUND", "No Relation Type identified by '" + entityId + "' found.");
		return result;
	}

}
