package com.github.sergemart.picocmdb.rest;

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
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.domain.ConfigurationItem;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import com.github.sergemart.picocmdb.exception.WrongDataException;
import com.github.sergemart.picocmdb.exception.DependencyExistsException;


public class ConfigurationItemTypeRestControllerIT extends AbstractIntegrationTests {

	private final String baseResourceUrl = "/rest/configurationitemtypes/";


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
		String entityId1 = "DUMMY" + super.getSalt();
		String entityId2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'dummy description')", (Object[]) new String[] {entityId2});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId2});
		// WHEN
		ResponseEntity<List<ConfigurationItemType>> response = super.restTemplate.exchange(baseResourceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ConfigurationItemType>>() {});
		List<ConfigurationItemType> entityList = response.getBody();
		// THEN
	    assertThat(entityList, hasSize(greaterThan(1)));
	}


	@Test
	public void read_Op_Reads_Entity() {
		// GIVEN
			// create an entity; add task to delete this entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		ConfigurationItemType receivedEntity = super.restTemplate.getForObject(baseResourceUrl + entityId1, ConfigurationItemType.class);
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getDescription(), is("Тестовое описание."));
	}

	
	@Test
	public void read_Op_Reports_When_No_Such_Entity() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new NoSuchObjectException("CONFIGURATIONITEMTYPENOTFOUND", ""), Locale.ENGLISH);
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


	@Test
	public void read_Op_Reads_Dependent_Entity_List() {
		// GIVEN
			// create a parent entity
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {entityId1});
			// create child entities
		String childName1 = "DUMMY" + super.getSalt();
		String childName2 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName1, entityId1});
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName2, entityId1});
			// add tasks (in right order) to delete test entities after the test
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {childName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {childName2});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		ResponseEntity<List<ConfigurationItem>> response = super.restTemplate.exchange(baseResourceUrl + entityId1 + "/configurationitems", HttpMethod.GET, null, new ParameterizedTypeReference<List<ConfigurationItem>>() {});
		List<ConfigurationItem> entityList = response.getBody();
		// THEN
		assertThat(entityList, hasSize(2));
	}


	// -------------- CREATE --------------

	@Test
	public void create_Op_Creates_Entity() {
		// GIVEN
			// add task to delete created entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
			// construct a new entity and a HTTP request
		ConfigurationItemType newEntity = new ConfigurationItemType();
		newEntity.setId(entityId1);
		newEntity.setDescription("Тестовое описание.");
		HttpEntity<ConfigurationItemType> request = new HttpEntity<>(newEntity);
		// WHEN
		ConfigurationItemType receivedEntity = super.restTemplate.postForObject(baseResourceUrl, request, ConfigurationItemType.class);
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getDescription(), is("Тестовое описание."));
		// extra check directly in database
		String entityDescription = super.jdbcTemplate.queryForObject("SELECT description FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, String.class);
		assertThat(entityDescription, is( receivedEntity.getDescription()) );
	}

	
	@Test
	public void create_Op_Reports_When_Entity_With_Same_Id_Exists() {
		// GIVEN
			// create an entity; add task to delete this entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new ObjectAlreadyExistsException("CONFIGURATIONITEMTYPEEXISTS", ""), new Locale("ru",  "RU"));
			// construct a new entity
		ConfigurationItemType newEntity = new ConfigurationItemType();
		newEntity.setId(entityId1);
		newEntity.setDescription("Ещё одно описание.");
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<ConfigurationItemType> request = new HttpEntity<>(newEntity, headers);
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
		RestError expectedError = super.systemService.getRestError(new WrongDataException("CONFIGURATIONITEMTYPEBAD", ""), new Locale("ru",  "RU"));
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
			// create an entity to be updated; add task to delete the entity after the test
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
			// construct update data and a HTTP request
		ConfigurationItemType updatingEntity = new ConfigurationItemType();
		updatingEntity.setDescription("Изменённое тестовое описание.");
		HttpEntity<ConfigurationItemType> request = new HttpEntity<>(updatingEntity);
		// WHEN
			// RestTemplate.put() is void for somewhat reason, hence the fallback to RestTemplate.exchange()
		ResponseEntity<ConfigurationItemType> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.PUT, request, new ParameterizedTypeReference<ConfigurationItemType>() {});
		ConfigurationItemType receivedEntity = response.getBody();
		// THEN
		assertThat(receivedEntity.getId(), is(entityId1));
		assertThat(receivedEntity.getDescription(), is("Изменённое тестовое описание."));
			// extra check directly in database
		Map<String, Object> modifiedEntity = super.jdbcTemplate.queryForMap("SELECT id, description FROM configuration_item_type WHERE (id = ?)", (Object[])new String[] {entityId1});
		assertThat(modifiedEntity.get("description"), is("Изменённое тестовое описание."));
	}


	// -------------- DELETE --------------

	@Test
	public void delete_Op_Deletes_Entity() {
		// GIVEN
			// create an entity to be deleted; add task to delete this entity after the test, just in case if delete fails for any reason
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id, description) VALUES (?, 'Тестовое описание.')", (Object[]) new String[] {entityId1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
		// WHEN
		ResponseEntity<Void> response = super.restTemplate.exchange(baseResourceUrl + entityId1, HttpMethod.DELETE, null, new ParameterizedTypeReference<Void>() {});
		// THEN
		assertThat( response.getStatusCode(), is(HttpStatus.OK) );
			// extra check directly in database that the entity is deleted
		Integer entityCount = super.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1}, Integer.class);
		assertThat(entityCount, is(0));
	}


	@Test
	public void delete_Op_Reports_When_No_Such_Entity() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new NoSuchObjectException("CONFIGURATIONITEMTYPENOTFOUND", ""), new Locale("ru",  "RU"));
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<Void> request = new HttpEntity<>(null, headers);
		// WHEN
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


	@Test
	public void delete_Op_Reports_When_Dependent_Entity_Exists() {
		// GIVEN
			// construct expected error object
		RestError expectedError = super.systemService.getRestError(new DependencyExistsException("CONFIGURATIONITEMEXISTS", ""), new Locale("ru",  "RU"));
			// create a parent entity which would be deleted
		String entityId1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item_type(id) VALUES (?)", (Object[]) new String[] {entityId1});
			// create a child entity
		String childName1 = "DUMMY" + super.getSalt();
		super.jdbcTemplate.update("INSERT INTO configuration_item(name, ci_type_id) VALUES (?, ?)", (Object[]) new String[] {childName1, entityId1});
			// add tasks (in right order) to delete test entities after the test
		super.jdbcCleaner.addTask("DELETE FROM configuration_item WHERE (name = ?)", new String[] {childName1});
		super.jdbcCleaner.addTask("DELETE FROM configuration_item_type WHERE (id = ?)", new String[] {entityId1});
			// construct HTTP request
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU"); // switch language; expected message should be in Russian
		HttpEntity<Void> request = new HttpEntity<>(null, headers);
		// WHEN
		ResponseEntity<RestError> response = super.restTemplate.exchange(baseResourceUrl + entityId1,
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
