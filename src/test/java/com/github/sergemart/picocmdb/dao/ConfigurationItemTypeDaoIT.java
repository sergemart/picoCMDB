package com.github.sergemart.picocmdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import org.junit.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import com.github.sergemart.picocmdb.AbstractIntegrationTests;
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import com.github.sergemart.picocmdb.testtool.ConfigurationItemRowMapper;


public class ConfigurationItemTypeDaoIT extends AbstractIntegrationTests {

	@Autowired
	private ConfigurationItemTypeDao entityDao; // the CuT

	// -------------- READ --------------

	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Entity_List() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[]{entityId2});
			// get a number of entities via JDBC
		Long jdbcCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM configuration_item_type", Long.class);
		// WHEN
		List<ConfigurationItemType> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, hasSize(greaterThan(1)));
		assertThat((long)daoResult.size(), is(jdbcCount)); // valid check because changes are isolated by transaction
		assertThat(daoResult.get(0), instanceOf(ConfigurationItemType.class));
		assertThat(daoResult.get(1), instanceOf(ConfigurationItemType.class));
	}


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityId2});
			// get the entity instances via JDBC
		List<ConfigurationItemType> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item_type", new Object[] {}, new BeanPropertyRowMapper(ConfigurationItemType.class));
		// WHEN
		List<ConfigurationItemType> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult.toArray(), is( arrayContainingInAnyOrder(jdbcResult.toArray()) )); // uses overloaded ConfigurationItemType.equals(); valid check because changes are isolated by transaction
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
			// create an entity; this entity will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityId1});
			// get the entity instance via JDBC
		List<ConfigurationItemType> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(ConfigurationItemType.class));
		// WHEN
		ConfigurationItemType daoResult = this.entityDao.findById(entityId1);
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ConfigurationItemType.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity_With_Child_Entities() {
		// GIVEN
			// create a parent entity; this entity will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {entityId1});
			// create child entities; these entities will be deleted on rollback after the test
		String childName1 = "DUMMY" + super.getSalt();
		String childName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName1, entityId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName2, entityId1});
			// get the parent entity via JDBC
		List<ConfigurationItemType> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(ConfigurationItemType.class));
			// get the child entities via JDBC
		List<ConfigurationItem> jdbcChildren = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.ci_type_id = ?) AND (i.ci_type_id = t.id)", new String[] {entityId1}, new ConfigurationItemRowMapper());
		// WHEN
		ConfigurationItemType daoResult = this.entityDao.findById(entityId1);
		// THEN
			// shallow check the parent
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ConfigurationItemType.equals()
			// check the children
		assertThat(daoResult.getConfigurationItems().toArray(), is( arrayContainingInAnyOrder( jdbcChildren.toArray()) )); // uses overloaded ConfigurationItem.equals()
	}


	// -------------- CREATE --------------

	@Test
	public void save_Creates_Entity() {
		// GIVEN
			// add task to delete created entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
			// construct an entity
		ConfigurationItemType entity = new ConfigurationItemType();
		entity.setId(entityId1);
		entity.setDescription("dummy description");
		// WHEN
		this.entityDao.save(entity);
		// THEN
		// get the entity instance via JDBC
		List<ConfigurationItemType> jdbcResult = super.jdbcTemplate.query("SELECT * FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(ConfigurationItemType.class));
		assertThat(jdbcResult.size(), is(1));
		assertThat(jdbcResult.get(0), is(entity));
	}

	// -------------- DELETE --------------

	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed (JPA transaction would be isolated from JDBC)
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		this.entityDao.delete(entityId1);
		// THEN
		List<ConfigurationItemType> jdbcResult = jdbcTemplate.query("SELECT * FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(ConfigurationItemType.class));
		assertThat(jdbcResult.size(), is(0));
	}


	@Test
	public void delete_By_Id_Reports_When_Entity_Has_Child_Entities() {
		// GIVEN
		super.expectedException.expect(DataIntegrityViolationException.class);
			// create a parent entity which would be deleted
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {entityId1});
			// create a child entity
		String childName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName1, entityId1});
			// add tasks (in right order) to delete test entities after the test
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {childName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		this.entityDao.delete(entityId1);
		// THEN check the exception
	}


}
