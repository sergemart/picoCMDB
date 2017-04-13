package com.github.sergemart.picocmdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import org.junit.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import com.github.sergemart.picocmdb.AbstractIntegrationTests;
import com.github.sergemart.picocmdb.domain.ConfigurationItem;


public class ConfigurationItemDaoIT extends AbstractIntegrationTests {

	@Autowired
	private ConfigurationItemDao entityDao; // the CuT

	// -------------- READ --------------

	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Entity_List() {
		// GIVEN
			// create parent (classifier) entity, just in case if the database is empty; the entity will be deleted on rollback after the test
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName2, parentId1});
			// get a number of entities via JDBC
		Long jdbcCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM configuration_item", Long.class);
		// WHEN
		List<ConfigurationItem> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, hasSize(greaterThan(1)));
		assertThat((long)daoResult.size(), is(jdbcCount)); // valid check because changes are isolated by transaction
		assertThat(daoResult.get(0), instanceOf(ConfigurationItem.class));
		assertThat(daoResult.get(1), instanceOf(ConfigurationItem.class));
	}

	/*
	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[]{entityName2});
			// get the entity instances via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item", new Object[] {}, new BeanPropertyRowMapper(ConfigurationItem.class));
		// WHEN
		List<ConfigurationItem> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, is (jdbcResult)); // uses overloaded ConfigurationItem.equals(); valid check because changes are isolated by transaction
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
		// create an entity; this entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		// WHEN
		ConfigurationItem daoResult = this.entityDao.findById( jdbcResult.get(0).getId() );
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ConfigurationItem.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findByName_Finds_Entity() {
		// GIVEN
			// create an entity; this entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		// WHEN
		ConfigurationItem daoResult = this.entityDao.findByName(entityName1);
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ConfigurationItem.equals()
	}

	// -------------- CREATE --------------

	@Test
	public void save_Creates_Entity() {
		// GIVEN
		// add task to delete created entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
		// construct an entity
		ConfigurationItem entity = new ConfigurationItem();
		entity.setName(entityName1);
		entity.setDescription("dummy description");
		// WHEN
		this.entityDao.save(entity);
		// THEN
		// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));		assertThat(jdbcResult.size(), is(1));
		assertThat(jdbcResult.get(0), is(entity));
	}

	// -------------- DELETE --------------

	@Test
	@Transactional
	@Rollback
	public void deleteByName_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed (JPA transaction would be isolated from JDBC)
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
		// WHEN
		this.entityDao.deleteByName(entityName1);
		// THEN
		assertThat(this.entityDao.findByName(entityName1), is(nullValue())); // cannot use JDBC; delete([not_key_type]) auto-implementation requires explicit transaction
	}


	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed (JPA transaction would be isolated from JDBC)
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		// WHEN
		this.entityDao.delete(jdbcResult.get(0).getId());
		// THEN
		jdbcResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		assertThat(jdbcResult.size(), is(0));
	}
    */

}
