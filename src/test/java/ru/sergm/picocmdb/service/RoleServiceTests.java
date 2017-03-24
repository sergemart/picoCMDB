package ru.sergm.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.sergm.picocmdb.dao.RoleDao;
import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchRoleException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTests {

	@Autowired
	private RoleService roleService;	// the CuT
	@Mock
	private Role role, role2;
	@MockBean							// to create and inject mock
	private RoleDao roleDao;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Should_Be_Created() {
		assertNotNull(roleService);
		assertNotNull(role);
		assertNotNull(roleDao);
	}


	@Test
	public void getAllRoles_Returns_Role_List() {
		// ARRANGE: to stub methods in the mocks
		given(role.getName()).willReturn("dummy");
		given(role2.getName()).willReturn("dummy2");
		List<Role> result = Arrays.asList(role, role2);
		given(roleDao.findAll()).willReturn(result);
		//ACT
		roleService.getAllRoles();
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findAll();
		assertEquals("dummy", roleService.getAllRoles().get(0).getName());
		assertEquals("dummy2", roleService.getAllRoles().get(1).getName());
	}


	@Test
	public void getRole_Returns_Role() throws NoSuchRoleException {
		// ARRANGE: to stub methods in the mocks
		given(role.getName()).willReturn("dummy");
		given(roleDao.findByName("dummy")).willReturn(role);
		//ACT
		roleService.getRole("dummy");
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findByName("dummy");
		assertEquals("dummy", roleService.getRole("dummy").getName());
	}


	@Test(expected = NoSuchRoleException.class)
	public void getRole_Reports_When_No_Such_Role() throws NoSuchRoleException {
		// ARRANGE: to stub methods in the mocks
		given(roleDao.findByName("dummy")).willReturn(null);
		// ACT
		roleService.getRole("dummy");
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findByName("dummy");
	}

}