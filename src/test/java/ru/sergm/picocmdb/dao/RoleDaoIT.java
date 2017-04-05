package ru.sergm.picocmdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.List;

import ru.sergm.picocmdb.AbstractIntegrationTestSuite;
import ru.sergm.picocmdb.domain.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * The test suite uses JDBC to prepare data for tests and to produce expected results.
 */
public class RoleDaoIT extends AbstractIntegrationTestSuite {

	@Autowired
	private RoleDao roleDao; // the CuT

	@PersistenceContext
	private EntityManager entityManager;


	@Test
	@Transactional
	@Rollback
	public void findAll_Finds_Correct_Number_Of_Entities() {
		// GIVEN
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
			// just in case if the table is empty; (!) table names are case-sensitive on case-sensitive filesystems
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[]{entityId1});
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', false)", (Object[]) new String[]{entityId2});
		Long sqlCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role", Long.class);
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
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
			// just in case if the table is empty; (!) table names are case-sensitive on case-sensitive filesystems
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
		assertEquals(sqlResult, daoResult); // uses overloaded Role.equals()
	}


	@Test
	@Transactional
	@Rollback
	public void findById_Finds_Entity() {
		// GIVEN
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		//List<Role> sqlResult = jdbcTemplate.query(sqlQuery, new Object[] {}, new BeanPropertyRowMapper<Role>(Role.class));
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
		assertEquals(sqlResult.get(0), daoResult); // uses overloaded Role.equals()
	}


	@Test
	@Transactional(propagation = Propagation.NEVER) // otherwise JPA changes are not visible by JdbcTemplate
	@Rollback
	public void delete_By_Id_Deletes_Entity() {
		// GIVEN
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		// WHEN
		this.roleDao.delete(entityId1);
		// THEN
		List<Role> sqlResult = jdbcTemplate.query("SELECT * FROM role WHERE (id = ?)", new String[] {entityId1}, new BeanPropertyRowMapper(Role.class));
		assertEquals(0, sqlResult.size());
	}


	@Test
	@Transactional(propagation = Propagation.NEVER) // otherwise JPA changes are not visible by JdbcTemplate
	@Rollback
	public void save_Creates_Entity() {
		// GIVEN
		String entityId1 = "DUMMY" + super.getSalt();
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
		assertEquals(entity, sqlResult.get(0));
	}


}
