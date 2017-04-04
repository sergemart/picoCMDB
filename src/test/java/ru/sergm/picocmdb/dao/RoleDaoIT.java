package ru.sergm.picocmdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import org.junit.*;
import static org.junit.Assert.assertEquals;

import java.util.List;

import ru.sergm.picocmdb.AbstractIntegrationTestSuite;
import ru.sergm.picocmdb.domain.Role;

/**
 * The test suite uses JDBC to prepare data for tests and to produce expected results.
 */
public class RoleDaoIT extends AbstractIntegrationTestSuite {

	@Autowired
	private RoleDao roleDao; // the CuT


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Correct_Number_Of_Entities() {
		// GIVEN
		String entityId1 = "ROLE" + super.getSalt();
		String entityId2 = "ROLE" + super.getSalt();
			// just in case if the table is empty; (!) table names are case-sensitive on case-sensitive filesystems
		this.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		this.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', false)", (Object[]) new String[]{entityId2});
		Long sqlCount = this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role", Long.class);
		// WHEN
		List<Role> daoResult = this.roleDao.findAll();
		// THEN
		assertEquals((long)sqlCount, (long)daoResult.size());
	}


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_All_Stored_Entities() {
		// GIVEN
		String entityId1 = "ROLE" + super.getSalt();
		String entityId2 = "ROLE" + super.getSalt();
			// just in case if the table is empty; (!) table names are case-sensitive on case-sensitive filesystems
		this.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		this.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', false)", (Object[]) new String[]{entityId2});
		List<Role> sqlResult = this.jdbcTemplate.query("SELECT * FROM role", new Object[] {},
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
		assertEquals(sqlResult, daoResult); // uses overloaded Role.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
		String entityId1 = "ROLE" + super.getSalt();
		this.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		//List<Role> sqlResult = jdbcTemplate.query(sqlQuery, new Object[] {}, new BeanPropertyRowMapper<Role>(Role.class));
		List<Role> sqlResult = this.jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1},
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
		assertEquals(sqlResult.get(0), daoResult); // uses overloaded Role.equals()
	}



}
