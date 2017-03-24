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


@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTests {

	@Autowired
	private RoleService roleService;	// the CuT
	@MockBean
	private Role role;
	@MockBean							// to create and inject mock
	private RoleDao roleDao;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Are_Created() {
		assertNotNull(roleService);
		assertNotNull(role);
		assertNotNull(roleDao);
	}

	
	@Test
	public void getRole_Returns_Role() throws NoSuchRoleException {
		// to stub methods in the mocks
		given(role.getName()).willReturn("dummy");
		given(roleDao.findByName("dummy")).willReturn(role);

		roleService.getRole("dummy");
		// to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findByName("dummy");
		assertEquals("dummy", roleService.getRole("dummy").getName());
	}


	@Test(expected = NoSuchRoleException.class)
	public void getRole_Reports_No_Such_Role() throws NoSuchRoleException {
		// to stub methods in the mocks
		given(roleDao.findByName("dummy")).willReturn(null);

		roleService.getRole("dummy");
		// to verify that correct DAO method is called req num of times and with req arguments
		verify(roleDao, times(1)).findByName("dummy");
	}

}