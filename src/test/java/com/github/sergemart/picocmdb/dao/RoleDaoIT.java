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


/**
 * The test suite uses JDBC to prepare data for tests and to produce expected results.
 */
public class RoleDaoIT extends AbstractIntegrationTests {

	@Autowired
	private RoleDao roleDao; // the CuT


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Entity_List() {
		// GIVEN
			// create entities, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
			// just in case if the table is empty; (!) table names are case-sensitive on case-sensitive filesystems
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'Тестовое описание.', false)", (Object[]) new String[]{entityId2});
		Long sqlCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role", Long.class);
		// WHEN
		List<Role> daoResult = this.roleDao.findAll();
		// THEN
		assertThat(daoResult, hasSize(greaterThan(1)));
		assertThat((long)daoResult.size(), is(sqlCount)); // valid check because changes are isolated by transaction
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
		List<Role> sqlResult = super.jdbcTemplate.query("SELECT * FROM role", new Object[] {},
				// custom lambda implementation for RowMapper.mapRow(); Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
				(rs, rowNum) -> {
					Role role = new Role();
					role.setId(rs.getString("id"));
					role.setDescription(rs.getString("description"));
					role.setSystem(rs.getBoolean("is_system"));
					return role;
				}
		);
		// WHEN
		List<Role> daoResult = this.roleDao.findAll();
		// THEN
		assertThat(daoResult, is (sqlResult)); // uses overloaded Role.equals(); valid check because changes are isolated by transaction
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
			// create an entity, just in case if the database is empty; the entities will be deleted on rollback after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		List<Role> sqlResult = super.jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1},
				// custom lambda implementation for RowMapper.mapRow(); Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
				(rs, rowNum) -> {
					Role role = new Role();
					role.setId(rs.getString("id"));
					role.setDescription(rs.getString("description"));
					role.setSystem(rs.getBoolean("is_system"));
					return role;
				}
		);
		// WHEN
		Role daoResult = this.roleDao.findById(entityId1);
		// THEN
		assertThat(daoResult, is(sqlResult.get(0))); // uses overloaded Role.equals()
	}


	@Test
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
			// create entity to delete; add task to delete this entity after the test just in case if delete failed (JPA transaction would be isolated from JDBC)
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		this.roleDao.delete(entityId1);
		// THEN
		List<Role> sqlResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(Role.class));
		assertThat(sqlResult.size(), is(0));
	}


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
		this.roleDao.save(entity);
		// THEN
		List<Role> sqlResult = super.jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1},
				// custom lambda implementation for RowMapper.mapRow(); Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
				(rs, rowNum) -> {
					Role role = new Role();
					role.setId(rs.getString("id"));
					role.setDescription(rs.getString("description"));
					role.setSystem(rs.getBoolean("is_system"));
					return role;
				}
		);
		assertThat(sqlResult.size(), is(1));
		assertThat(sqlResult.get(0), is(entity));
	}


}
