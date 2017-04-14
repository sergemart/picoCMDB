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
import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import com.github.sergemart.picocmdb.testtool.ConfigurationItemRowMapper;


public class ManagedAreaDaoIT extends AbstractIntegrationTests {

	@Autowired
	private ManagedAreaDao entityDao; // the CuT

	// -------------- READ --------------

	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Entity_List() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[]{entityName2});
			// get a number of entities via JDBC
		Long jdbcCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM managed_area", Long.class);
		// WHEN
		List<ManagedArea> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, hasSize(greaterThan(1)));
		assertThat((long)daoResult.size(), is(jdbcCount)); // valid check because changes are isolated by transaction
		assertThat(daoResult.get(0), instanceOf(ManagedArea.class));
		assertThat(daoResult.get(1), instanceOf(ManagedArea.class));
	}


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[]{entityName2});
			// get the entity instances via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area", new Object[] {}, new BeanPropertyRowMapper(ManagedArea.class));
		// WHEN
		List<ManagedArea> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, is (jdbcResult)); // uses overloaded ManagedArea.equals(); valid check because changes are isolated by transaction
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
		// create an entity; this entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		// get the entity instance via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
		// WHEN
		ManagedArea daoResult = this.entityDao.findById( jdbcResult.get(0).getId() );
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ManagedArea.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findByName_Finds_Entity() {
		// GIVEN
			// create an entity; this entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
			// get the entity instance via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
		// WHEN
		ManagedArea daoResult = this.entityDao.findByName(entityName1);
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ManagedArea.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findByName_Finds_Entity_With_Linked_Entities() {
		// GIVEN
			// create a tested entity; this entity will be deleted on rollback after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name) VALUES (?)", (Object[]) new String[] {entityName1});
			// get auto-generated ID of the created tested entity
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
			// create parent (classifier) entity for linked entities; the entity will be deleted on rollback after the test
		String parentId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {parentId1});
			// create will-be-linked entities; the entities will be deleted on rollback after the test
		String linkedName1 = "DUMMY" + super.getSalt();
		String linkedName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{linkedName1, parentId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[]{linkedName2, parentId1});
			// get auto-generated IDs of created will-be-linked entities
		Long linkedId1 = super.jdbcTemplate.queryForObject("SELECT id FROM configuration_item WHERE (name = ?)", new String[] {linkedName1}, Long.class);
		Long linkedId2 = super.jdbcTemplate.queryForObject("SELECT id FROM configuration_item WHERE (name = ?)", new String[] {linkedName2}, Long.class);
			// create links between the tested entity and will-be-linked entities; these links will be deleted on rollback after the test
		super.jdbcTemplate.update("INSERT INTO ci_marea_link(ci_id, marea_id) VALUES (?, ?)", (Object[]) new Long[] {linkedId1, entityId1});
		super.jdbcTemplate.update("INSERT INTO ci_marea_link(ci_id, marea_id) VALUES (?, ?)", (Object[]) new Long[] {linkedId2, entityId1});
			// get the tested entity via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
			// get the linked entities via JDBC
		List<ConfigurationItem> jdbcLinked = super.jdbcTemplate.query("SELECT * FROM configuration_item i, configuration_item_type t WHERE (i.name = ? OR i.name = ?) AND (i.ci_type_id = t.id)", new String[] {linkedName1, linkedName2}, new ConfigurationItemRowMapper());
		// WHEN
		ManagedArea daoResult = this.entityDao.findByName(entityName1);
		// THEN
			// shallow check the tested entity
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded ManagedArea.equals()
			// check the linked entities
		assertThat(daoResult.getConfigurationItems().toArray(), is( arrayContainingInAnyOrder( jdbcLinked.toArray()) )); // uses overloaded ConfigurationItem.equals()
	}

	// -------------- CREATE --------------

	@Test
	public void save_Creates_Entity() {
		// GIVEN
			// add task to delete created entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// construct an entity
		ManagedArea entity = new ManagedArea();
		entity.setName(entityName1);
		entity.setDescription("dummy description");
		// WHEN
		this.entityDao.save(entity);
		// THEN
			// get the entity instance via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
		assertThat(jdbcResult.size(), is(1));
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
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
		// WHEN
		this.entityDao.deleteByName(entityName1);
		// THEN
		assertThat(this.entityDao.findByName(entityName1), is(nullValue())); // cannot use JDBC; delete([not_key_type]) auto-implementation requires explicit transaction
	}


	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[]{entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// get the entity instance via JDBC
		List<ManagedArea> jdbcResult = super.jdbcTemplate.query("SELECT * FROM managed_area WHERE (name = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
		// WHEN
		this.entityDao.delete(jdbcResult.get(0).getId());
		// THEN
		jdbcResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityName1}, new BeanPropertyRowMapper(ManagedArea.class));
		assertThat(jdbcResult.size(), is(0));
	}


}
