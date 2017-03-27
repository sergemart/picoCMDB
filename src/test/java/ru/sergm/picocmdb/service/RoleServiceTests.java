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

import java.util.Arrays;
import java.util.List;

import ru.sergm.picocmdb.dao.RoleDao;
import ru.sergm.picocmdb.domain.Role;
import ru.sergm.picocmdb.exception.NoSuchObjectException;



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
	public void setUp() {
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
		given(role.getId()).willReturn("dummy");
		given(role2.getId()).willReturn("dummy2");
		List<Role> result = Arrays.asList(role, role2);
		given(roleDao.findAll()).willReturn(result);
		//ACT
		roleService.getAllRoles();
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findAll();
		assertEquals("dummy", roleService.getAllRoles().get(0).getId());
		assertEquals("dummy2", roleService.getAllRoles().get(1).getId());
	}


	@Test
	public void getRole_Returns_Role() throws NoSuchObjectException {
		// ARRANGE: to stub methods in the mocks
		given(role.getId()).willReturn("dummy");
		given(roleDao.findById("dummy")).willReturn(role);
		//ACT
		roleService.getRole("dummy");
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findById("dummy");
		assertEquals("dummy", roleService.getRole("dummy").getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getRole_Reports_When_No_Such_Role() throws NoSuchObjectException {
		// ARRANGE: to stub methods in the mocks
		given(roleDao.findById("dummy")).willReturn(null);
		// ACT
		roleService.getRole("dummy");
		// ASSERT: to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findById("dummy");
	}

}