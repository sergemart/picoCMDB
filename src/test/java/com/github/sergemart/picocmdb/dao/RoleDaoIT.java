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
import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.testtool.RoleRowMapper;


public class RoleDaoIT extends AbstractIntegrationTests {

	@Autowired
	private RoleDao entityDao; // the CuT

	// -------------- READ --------------

	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Entity_List() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'Тестовое описание.', false)", (Object[]) new String[]{entityId2});
			// get a number of entities via JDBC
		Long jdbcCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role", Long.class);
		// WHEN
		List<Role> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult, hasSize(greaterThan(1)));
		assertThat((long)daoResult.size(), is(jdbcCount)); // valid check because changes are isolated by transaction
		assertThat(daoResult.get(0), instanceOf(Role.class));
		assertThat(daoResult.get(1), instanceOf(Role.class));
	}


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', false)", (Object[]) new String[]{entityId2});
			// get the entity instances via JDBC
		List<Role> jdbcResult = super.jdbcTemplate.query("SELECT * FROM role", new Object[] {}, new RoleRowMapper()); // used custom RowMapper; Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
		// WHEN
		List<Role> daoResult = this.entityDao.findAll();
		// THEN
		assertThat(daoResult.toArray(), is( arrayContainingInAnyOrder(jdbcResult.toArray()) )); // uses overloaded Role.equals(); valid check because changes are isolated by transaction
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
			// create an entity; this entity will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
			// get the entity instance via JDBC
		List<Role> jdbcResult = super.jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1}, new RoleRowMapper()); // used custom RowMapper; Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
		// WHEN
		Role daoResult = this.entityDao.findById(entityId1);
		// THEN
		assertThat(daoResult, is(jdbcResult.get(0))); // uses overloaded Role.equals()
	}

	// -------------- CREATE --------------

	@Test
	public void save_Creates_Entity() {
		// GIVEN
			// add task to delete created entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId1});
			// construct an entity
		Role entity = new Role();
		entity.setId(entityId1);
		entity.setDescription("dummy description");
		entity.setSystem(true);
		// WHEN
		this.entityDao.save(entity);
		// THEN
			// get the entity instance via JDBC
		List<Role> jdbcResult = super.jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1}, new RoleRowMapper()); // used custom RowMapper; Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
		assertThat(jdbcResult.size(), is(1));
		assertThat(jdbcResult.get(0), is(entity));
	}

	// -------------- DELETE --------------

	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed (JPA transaction would be isolated from JDBC)
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		this.entityDao.delete(entityId1);
		// THEN
		List<Role> jdbcResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(Role.class));
		assertThat(jdbcResult.size(), is(0));
	}


}
