package ru.sergm.picocmdb.dao;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.sergm.picocmdb.AbstractIntegrationTestSuite;
import ru.sergm.picocmdb.domain.Role;


public class RoleDaoIT extends AbstractIntegrationTestSuite {

	@Autowired
	private RoleDao roleDao; // the CuT

	
	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Correct_Number_Of_Entities() {
		// GIVEN
			// just in case if the table is empty
		this.jdbcTemplate.execute("INSERT INTO role(id, description, is_system) VALUES ('dummy_role_fafcnoe_1', 'dummy description 1', true)");
		this.jdbcTemplate.execute("INSERT INTO role(id, description, is_system) VALUES ('dummy_role_fafcnoe_2', 'dummy description 2', false)");
		String sqlQuery = "SELECT COUNT(*) FROM role"; // table names are case-sensitive on case-sensitive filesystems
		Long sqlCount = this.jdbcTemplate.queryForObject(sqlQuery, Long.class);
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
			// just in case if the table is empty
		this.jdbcTemplate.execute("INSERT INTO role(id, description, is_system) VALUES ('dummy_role_fafase_1', 'dummy description 1', true)");
		this.jdbcTemplate.execute("INSERT INTO role(id, description, is_system) VALUES ('dummy_role_fafase_2', 'dummy description 2', false)");
		String sqlQuery = "SELECT * FROM role"; // table names are case-sensitive on case-sensitive filesystems
		List<Role> sqlResult = this.jdbcTemplate.query(sqlQuery, new Object[] {}, (rs, rowNum) -> {
				// custom lambda RowMapper.mapRow(); Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
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
		this.jdbcTemplate.execute("INSERT INTO role(id, description, is_system) VALUES ('dummy_role_fbife_1', 'dummy description 1', true)");
		String sqlQuery = "SELECT * FROM role WHERE (id = 'dummy_role_fbife_1')"; // table names are case-sensitive on case-sensitive filesystems
		//List<Role> sqlResult = jdbcTemplate.query(sqlQuery, new Object[] {}, new BeanPropertyRowMapper<Role>(Role.class));
		List<Role> sqlResult = this.jdbcTemplate.query(sqlQuery, new Object[] {}, (rs, rowNum) -> {
					// custom lambda RowMapper.mapRow(); Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
					Role role = new Role();
					role.setId(rs.getString("id"));
					role.setDescription(rs.getString("description"));
					role.setSystem(rs.getBoolean("is_system"));
					return role;
				}
		);
		// WHEN
		Role daoResult = this.roleDao.findById("dummy_role_fbife_1");
		// THEN
		assertEquals(sqlResult.get(0), daoResult); // uses overloaded Role.equals()
	}



}
