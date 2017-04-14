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
import com.github.sergemart.picocmdb.testtool.ConfigurationItemRowMapper;


public class ConfigurationItemDaoIT extends AbstractIntegrationTests {

	@Autowired
	private ConfigurationItemDao entityDao; // the CuT

	@Autowired
	private ConfigurationItemTypeDao classifierDao; // the CuT's classifier (parent) DAO


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


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
			// create parent (classifier) entity, just in case if the database is empty; the entity will be deleted on rollback after the test
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName2, parentId1});
			// get the entity instances via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.ci_type_id = t.id)", new Object[] {}, new ConfigurationItemRowMapper());
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
			// create parent (classifier) entity; the entity will be deleted on rollback after the test
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create an entity; the entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.name = ?) AND (i.ci_type_id = t.id)", new String[] {entityName1}, new ConfigurationItemRowMapper());
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
			// create parent (classifier) entity; the entity will be deleted on rollback after the test
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create an entity; the entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.name = ?) AND (i.ci_type_id = t.id)", new String[] {entityName1}, new ConfigurationItemRowMapper());
		// WHEN
		ConfigurationItem daoResult = this.entityDao.findByName(entityName1);
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ConfigurationItem.equals()
	}

	// -------------- CREATE --------------

	@Test
	public void save_Creates_Entity() {
		// GIVEN
			// create parent (classifier) entity
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// add tasks (in right order) to delete would-be-created and classifier entities after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {parentId1});
				// construct an entity
		ConfigurationItem entity = new ConfigurationItem();
		entity.setName(entityName1);
		entity.setDescription("dummy description");
		entity.setType( this.classifierDao.findById(parentId1) ); // use JPA to get context-attached parent entity
		// WHEN
		this.entityDao.save(entity);
		// THEN
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.name = ?) AND (i.ci_type_id = t.id)", new String[] {entityName1}, new ConfigurationItemRowMapper());
		assertThat(jdbcResult.size(), is(1));
		assertThat(jdbcResult.get(0), is(entity));
	}

	// -------------- DELETE --------------

	@Test
	@Transactional
	@Rollback
	public void deleteByName_Deletes_Entity() {
		// GIVEN
			// create parent (classifier) entity
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create entity to delete
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
			// add tasks (in right order) to delete would-be-deleted entity (in case if delete failed) and the classifier entity after the test (JPA transaction would be isolated from JDBC)
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {parentId1});
		// WHEN
		this.entityDao.deleteByName(entityName1);
		// THEN
		assertThat(this.entityDao.findByName(entityName1), is(nullValue())); // cannot use JDBC; delete([not_key_type]) auto-implementation requires explicit transaction
	}


	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create parent (classifier) entity
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create entity to delete
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{entityName1, parentId1});
			// add tasks (in right order) to delete would-be-deleted entity (in case if delete failed) and the classifier entity after the test
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {parentId1});
			// get the entity instance via JDBC
		List<ConfigurationItem> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		// WHEN
		this.entityDao.delete(jdbcResult.get(0).getId());
		// THEN
		jdbcResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ConfigurationItem.class));
		assertThat(jdbcResult.size(), is(0));
	}


}
