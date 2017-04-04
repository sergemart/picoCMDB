package ru.sergm.picocmdb.dao;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import ru.sergm.picocmdb.AbstractIntegrationTestSuite;
import ru.sergm.picocmdb.domain.Role;


public class RoleDaoIT extends AbstractIntegrationTestSuite {

	@Autowired
	private RoleDao roleDao; // the CuT


	@Test
	public void cuT_And_Other_Autowired_Components_Are_Injected() {
		assertNotNull(this.roleDao);
		assertNotNull(super.jdbcTemplate);
	}


	@Test
	public void roleDao_findAll_Finds_Correct_Number_Of_Entities() {
		// GIVEN
		String sqlQuery = "SELECT COUNT(*) FROM role";
		Long sqlCount = jdbcTemplate.queryForObject(sqlQuery, Long.class);
		// WHEN
		List<Role> daoResult = roleDao.findAll();
		// THEN
		assertEquals((long)sqlCount, (long)daoResult.size());
	}


	@Test
	public void roleDao_findAll_Finds_All_Stored_Entities() {
		// GIVEN
		String sqlQuery = "SELECT * FROM role";
		//List<Role> sqlResult = jdbcTemplate.query(sqlQuery, new Object[] {}, new BeanPropertyRowMapper<Role>(Role.class));
		List<Role> sqlResult = jdbcTemplate.query(sqlQuery, new Object[] {}, (rs, rowNum) -> {
				// custom lambda row mapper; Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name
				Role role = new Role();
				role.setId(rs.getString("id"));
				role.setDescription(rs.getString("description"));
				role.setSystem(rs.getBoolean("is_system"));
				return role;
			}
		);
		// WHEN
		List<Role> daoResult = roleDao.findAll();
		// THEN
		assertEquals(sqlResult, daoResult); // uses overloaded Role.equals()
	}


}
