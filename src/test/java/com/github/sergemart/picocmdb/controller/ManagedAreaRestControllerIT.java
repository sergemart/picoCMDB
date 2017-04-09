package com.github.sergemart.picocmdb.controller;

import net.minidev.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.github.sergemart.picocmdb.AbstractIntegrationTests;
import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.rest.RestError;

import javax.persistence.Entity;


public class ManagedAreaRestControllerIT extends AbstractIntegrationTests {

	private String baseResourceUrl = "/rest/managedareas/";


	@Test
	public void test_Suite_Prerequisites_Initialized() {
		assertThat(super.jdbcTemplate, not(is(nullValue())));
		assertThat(super.jdbcCleaner, not(is(nullValue())));
	}


	// -------------- READ --------------

	@Test
	public void read_Op_Reads_Entity_List() {
		// GIVEN
			// create entities, just in case if the database is empty; add tasks to delete these entities after the test
		String entityName1 = "DUMMY" + super.getSalt();
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityName1});
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityName2});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName2});
		// WHEN
		ResponseEntity<List<ManagedArea>> response = super.restTemplate.exchange(baseResourceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ManagedArea>>() {});
		List<ManagedArea> body = response.getBody();
		// THEN
	    assertThat(body, hasSize(greaterThan(1)));
	}


	@Test
	public void read_Op_Reads_Entity() {
		// GIVEN
			// create entity; add task to delete this entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// get auto-generated entity ID
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
		// WHEN
		ManagedArea receivedEntity = super.restTemplate.getForObject(baseResourceUrl + entityId1, ManagedArea.class);
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getName(), is(entityName1));
		assertThat(receivedEntity.getDescription(), is("Тестовое описание."));
	}


	@Test
	public void read_Op_Reports_When_No_Such_Entity() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new NoSuchObjectException("MANAGEDAREANOTFOUND", ""), Locale.ENGLISH);
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

	// -------------- CREATE --------------

	@Test
	public void create_Op_Creates_Entity() {
		// GIVEN
			// add task to delete created entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// construct HTTP request
		ManagedArea newEntity = new ManagedArea();
		newEntity.setName(entityName1);
		newEntity.setDescription("Тестовое описание.");
		HttpEntity<ManagedArea> request = new HttpEntity<>(newEntity);
		// WHEN
		ManagedArea receivedEntity = super.restTemplate.postForObject(baseResourceUrl, request, ManagedArea.class);
		// THEN
		assertThat( receivedEntity.getId(), not(is( nullValue() )) );
		assertThat(receivedEntity.getName(), is(entityName1));
		assertThat(receivedEntity.getDescription(), is("Тестовое описание."));
		// extra check directly in database
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
		assertThat(entityId1, is( receivedEntity.getId()) );
	}


	@Test
	public void create_Op_Reports_When_Entity_With_Same_Name_Exists() {
		// GIVEN
			// create entity; add task to delete this entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new ObjectAlreadyExistsException("MANAGEDAREAEXISTS", ""), new Locale("ru",  "RU"));
			// construct a new entity
		ManagedArea newEntity = new ManagedArea();
		newEntity.setName(entityName1);
		newEntity.setDescription("Ещё одно описание.");
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<ManagedArea> request = new HttpEntity<>(newEntity, headers);
		// WHEN
		RestError receivedError = super.restTemplate.postForObject(baseResourceUrl, request, RestError.class);
		// THEN
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), is(expectedError.getLocalizedMessage()) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
	}


	@Test
	public void create_Op_Reports_When_JSON_Has_Wrong_Schema() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new WrongDataException("MANAGEDAREABAD", ""), new Locale("ru",  "RU"));
			// construct a bad entity
		JSONObject newEntity = new JSONObject();
		newEntity.put("badfield1", "badfieldvalue");
		newEntity.put("badfield2", "Некое значение.");
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<JSONObject> request = new HttpEntity<>(newEntity, headers);
		// WHEN
		RestError receivedError = super.restTemplate.postForObject(baseResourceUrl, request, RestError.class);
		// THEN
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), is(expectedError.getLocalizedMessage()) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
	}


}
