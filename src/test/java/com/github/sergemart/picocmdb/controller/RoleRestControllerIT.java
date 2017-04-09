package com.github.sergemart.picocmdb.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Locale;

import com.github.sergemart.picocmdb.AbstractIntegrationTests;
import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.rest.RestError;


public class RoleRestControllerIT extends AbstractIntegrationTests {

	private String baseResourceUrl = "/rest/roles/";


	@Test
	public void test_Suite_Prerequisites_Initialized() {
		assertThat(super.jdbcTemplate, not(is(nullValue())));
		assertThat(super.jdbcCleaner, not(is(nullValue())));
	}


	// -------------- READ --------------

	@Test
	public void read_Op_Reads_Entity_List() {
		// GIVEN: at least firstStoredRole exists in storage
			// create entities, just in case if the database is empty; add tasks to delete these entities after the test
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId1});
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'dummy description', true)", (Object[]) new String[] {entityId2});
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId2});
		// WHEN
		ResponseEntity<List<Role>> response = super.restTemplate.exchange(baseResourceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Role>>() {});
		List<Role> body = response.getBody();
		// THEN
	    assertThat(body, hasSize(greaterThan(1)));
	}


	@Test
	public void read_Op_Reads_Entity() {
		// GIVEN
			// create entity; add task to delete this entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO role(id, description, is_system) VALUES (?, 'Тестовое описание.', true)", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM role WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		Role receivedEntity = super.restTemplate.getForObject(baseResourceUrl + entityId1.toLowerCase(), Role.class);
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getDescription(), is("Тестовое описание."));
		assertThat(receivedEntity.isSystem(), is(true));
	}


	@Test
	public void read_Op_Reports_When_No_Such_Entity() {
		// GIVEN
		RestError expectedError = super.systemService.getRestError(new NoSuchObjectException("ROLENOTFOUND", ""), Locale.ENGLISH);
		// WHEN
		RestError receivedError = super.restTemplate.getForObject(baseResourceUrl + "nosuchid", RestError.class);
		// THEN
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), not(is(nullValue())) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
	}

}
