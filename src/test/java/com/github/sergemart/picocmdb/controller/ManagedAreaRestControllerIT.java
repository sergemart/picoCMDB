package com.github.sergemart.picocmdb.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import net.minidev.json.JSONObject;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.sergemart.picocmdb.AbstractIntegrationTests;
import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.rest.RestError;


public class ManagedAreaRestControllerIT extends AbstractIntegrationTests {

	private final String baseResourceUrl = "/rest/managedareas/";


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
		List<ManagedArea> entityList = response.getBody();
		// THEN
	    assertThat(entityList, hasSize(greaterThan(1)));
	}


	@Test
	public void read_Op_Reads_Entity() {
		// GIVEN
			// create an entity; add task to delete this entity after the test
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
			// construct a new entity and a HTTP request
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
			// create an entity; add task to delete this entity after the test
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

	// -------------- UPDATE --------------

	@Test
	public void update_Op_Updates_Entity() {
		// GIVEN
			// create an entity to be updated
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
			// get auto-generated entity ID
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
			// add task to delete the entity by ID (name will be changed) after the test
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (id = ?)", new Long[] {entityId1});
			// construct update data and a HTTP request
		ManagedArea updatingEntity = new ManagedArea();
		updatingEntity.setName(entityName1 + "_modified");
		updatingEntity.setDescription("Изменённое тестовое описание.");
		HttpEntity<ManagedArea> request = new HttpEntity<>(updatingEntity);
		// WHEN
			// RestTemplate.put() is void for somewhat reason, hence the fallback to RestTemplate.exchange()
		ResponseEntity<ManagedArea> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.PUT, request, new ParameterizedTypeReference<ManagedArea>() {});
		ManagedArea receivedEntity = response.getBody();
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getName(), is(entityName1 + "_modified"));
		assertThat(receivedEntity.getDescription(), is("Изменённое тестовое описание."));
			// extra check directly in database
		Map<String, Object> modifiedEntity = super.jdbcTemplate.queryForMap("SELECT name, description FROM managed_area WHERE (id = ?)", (Object[])new Long[] {entityId1});
		assertThat(modifiedEntity.get("name"), is(entityName1 + "_modified"));
		assertThat(modifiedEntity.get("description"), is("Изменённое тестовое описание."));
	}


	@Test
	public void update_Op_Reports_When_Entity_With_Same_Name_Exists() {
		// GIVEN
			// create an entity to be updated; add task to delete this entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// get auto-generated entity ID
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
			// create an entity what will be a conflicting existing entity; add task to delete this entity after the test
		String entityName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName2});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName2});
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new ObjectAlreadyExistsException("MANAGEDAREAEXISTS", ""), new Locale("ru",  "RU"));
			// construct update data
		ManagedArea updatingEntity = new ManagedArea();
		updatingEntity.setName(entityName2); // try to rename the entity to the existing name
		updatingEntity.setDescription("Изменённое тестовое описание.");
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<ManagedArea> request = new HttpEntity<>(updatingEntity, headers);
		// WHEN
			// RestTemplate.put() is void for somewhat reason, hence the fallback to RestTemplate.exchange()
		ResponseEntity<RestError> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.PUT, request, new ParameterizedTypeReference<RestError>() {});
		RestError receivedError = response.getBody();
		// THEN
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), is(expectedError.getLocalizedMessage()) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
			// extra check directly in database that the entity that would be modified remains unmodified
		Map<String, Object> unmodifiedEntity = super.jdbcTemplate.queryForMap("SELECT name, description FROM managed_area WHERE (id = ?)", (Object[])new Long[] {entityId1});
		assertThat(unmodifiedEntity.get("name"), is(entityName1));
		assertThat(unmodifiedEntity.get("description"), is("Тестовое описание."));
	}


	@Test
	public void update_Op_Reports_When_JSON_Has_Wrong_Schema() {
		// GIVEN
			// create an entity to be updated; add task to delete this entity after the test
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// get auto-generated entity ID
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new WrongDataException("MANAGEDAREABAD", ""), new Locale("ru",  "RU"));
			// construct a bad entity
		JSONObject updatingEntity = new JSONObject();
		updatingEntity.put("badfield1", "badfieldvalue");
		updatingEntity.put("badfield2", "Некое значение.");
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<JSONObject> request = new HttpEntity<>(updatingEntity, headers);
		// WHEN
			// RestTemplate.put() is void for somewhat reason, hence the fallback to RestTemplate.exchange()
		ResponseEntity<RestError> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.PUT, request, new ParameterizedTypeReference<RestError>() {});
		RestError receivedError = response.getBody();
		// THEN
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), is(expectedError.getLocalizedMessage()) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
			// extra check directly in database that the entity that would be modified remains unmodified
		Map<String, Object> unmodifiedEntity = super.jdbcTemplate.queryForMap("SELECT name, description FROM managed_area WHERE (id = ?)", (Object[])new Long[] {entityId1});
		assertThat(unmodifiedEntity.get("name"), is(entityName1));
		assertThat(unmodifiedEntity.get("description"), is("Тестовое описание."));
	}

	// -------------- DELETE --------------

	@Test
	public void delete_Op_Deletes_Entity() {
		// GIVEN
			// create an entity to be deleted; add task to delete this entity after the test, just in case if delete fails for any reason
		String entityName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO managed_area(name, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityName1});
		super.jdbcCleaner.addTask("DELETE FROM managed_area WHERE (name = ?)", new String[] {entityName1});
			// get auto-generated entity ID
		Long entityId1 = super.jdbcTemplate.queryForObject("SELECT id FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Long.class);
		// WHEN
		ResponseEntity<Void> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.DELETE, null, new ParameterizedTypeReference<Void>() {});
		// THEN
		assertThat( response.getStatusCode(), is(HttpStatus.OK) );
			// extra check directly in database that the entity is deleted
		Integer entityCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM managed_area WHERE (name = ?)", new String[] {entityName1}, Integer.class);
		assertThat(entityCount, is(0));
	}


	@Test
	public void delete_Op_Reports_When_No_Such_Entity() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new NoSuchObjectException("MANAGEDAREANOTFOUND", ""), new Locale("ru",  "RU"));
		// WHEN
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<Void> request = new HttpEntity<>(null, headers);
		ResponseEntity<RestError> response = super.restTemplate.exchange(baseResourceUrl + "nosuchid", // no such entity
				HttpMethod.DELETE, request, new ParameterizedTypeReference<RestError>() {});
		RestError receivedError = response.getBody();
		// THEN
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat( receivedError.getExceptionName(), is(expectedError.getExceptionName()) );
		assertThat( receivedError.getErrorName(), is(expectedError.getErrorName()) );
		assertThat( receivedError.getErrorCode(), is(expectedError.getErrorCode()) );
		assertThat( receivedError.getMessage(), not(is(nullValue())) );
		assertThat( receivedError.getLocalizedMessage(), is(expectedError.getLocalizedMessage()) );
		assertThat( receivedError.getTimestamp(), not(is(nullValue())) );
	}


}
