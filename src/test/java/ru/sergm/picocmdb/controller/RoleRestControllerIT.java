package ru.sergm.picocmdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.List;

import ru.sergm.picocmdb.domain.Role;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoleRestControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;
	

	@Test
	public void controller_Returns_Role_List() {
		//List<Role> result = this.restTemplate.getForObject("/rest/roles", List<Role>.class);
		//assertEquals(result.get(0).getName(), "admin");
	}


	@Test
	public void controller_Returns_Role() {
		Role role = this.restTemplate.getForObject("/rest/roles/admin", Role.class);
		assertEquals(role.getName(), "admin");
	}


}
