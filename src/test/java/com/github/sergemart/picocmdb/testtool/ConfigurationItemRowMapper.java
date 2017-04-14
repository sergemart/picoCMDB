package com.github.sergemart.picocmdb.testtool;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;


/**
 * Custom row mapper for JDBC requests for 'ConfigurationItem'; used to proper handle reference attributes.
 */
public class ConfigurationItemRowMapper implements RowMapper<ConfigurationItem> {

	@Override
	public ConfigurationItem mapRow(ResultSet rs, int rowNum)
			throws SQLException{
		// make shallow entity
		ConfigurationItem ci = new ConfigurationItem();
		ci.setId(rs.getLong("i.id"));
		ci.setName(rs.getString("i.name"));
		ci.setDescription(rs.getString("i.description"));

		// handle ConfigurationItemType reference attribute
		ConfigurationItemType ciType = new ConfigurationItemType();
		ciType.setId(rs.getString("t.id"));
		ciType.setDescription(rs.getString("t.description"));
		ci.setType(ciType);

		return ci;
	}

}
