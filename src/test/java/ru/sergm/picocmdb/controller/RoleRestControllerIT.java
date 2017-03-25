package ru.sergm.picocmdb.controller;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.List;

import ru.sergm.picocmdb.dao.RoleDao;
import ru.sergm.picocmdb.domain.Role;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)		// start embedded tomcat on random free port
public class RoleRestControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private RoleDao roleDao;
	private List<Role> storedList; // all Roles stored in database
	private Role firstStoredRole; // the first Role object stored in database, if any


	@Before
	public void setUp() {
		this.storedList = roleDao.findAll();
		// find an save first store Role from database
		try {
			this.firstStoredRole = storedList.get(0);
		} catch (IndexOutOfBoundsException e) {}
	}


	@Test
	public void there_Is_At_Least_One_Role_Stored() {
		assertNotNull(firstStoredRole);
	}


	@Test
	public void controller_Returns_Role_List() {
		ResponseEntity<List<Role>> response = restTemplate.exchange("/rest/roles", HttpMethod.GET, null, new ParameterizedTypeReference<List<Role>>() {});
		List<Role> receivedList = response.getBody();
		assertEquals(this.storedList.size(), receivedList.size());
		assertEquals(this.firstStoredRole.getName(), receivedList.get(0).getName());
		assertEquals(this.firstStoredRole.getId(), receivedList.get(0).getId());
		assertEquals(this.firstStoredRole.getDescription(), receivedList.get(0).getDescription());
		assertEquals(this.firstStoredRole.isSystem(), receivedList.get(0).isSystem());
	}


	@Test
	public void controller_Returns_Role() {
		Role receivedRole = this.restTemplate.getForObject("/rest/roles/admin", Role.class);
		assertEquals(this.firstStoredRole.getId(), receivedRole.getId());
		assertEquals(this.firstStoredRole.getName(), receivedRole.getName());
		assertEquals(this.firstStoredRole.getDescription(), receivedRole.getDescription());
		assertEquals(this.firstStoredRole.isSystem(), receivedRole.isSystem());
	}


}