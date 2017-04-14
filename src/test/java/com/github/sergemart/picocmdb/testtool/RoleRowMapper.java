package com.github.sergemart.picocmdb.testtool;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.sergemart.picocmdb.domain.Role;


/**
 * Custom row mapper for JDBC requests for 'Role'; used to proper handle boolean attributes (Spring BeanPropertyRowMapper can not properly map 'is_system' field, due to its 'non-standard' name)
 */
public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs, int rowNum)
			throws SQLException{
		Role role = new Role();
		role.setId(rs.getString("id"));
		role.setDescription(rs.getString("description"));
		role.setSystem(rs.getBoolean("is_system"));
		return role;
	}

}
