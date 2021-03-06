package com.github.sergemart.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import com.github.sergemart.picocmdb.AbstractUnitTests;
import com.github.sergemart.picocmdb.domain.Role;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


public class RoleServiceTests extends AbstractUnitTests {

	@Autowired
	private RoleService roleService;	// the CuT
	@Mock
	private Role role, role2;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Are_Injected() {
		assertNotNull(this.roleService);
		assertNotNull(this.role);
		assertNotNull(this.role2);
		// all MockBeans are defined in super to avoid multiple Spring context loading
		assertNotNull(super.roleDao);
	}

	// -------------- READ --------------

	@Test
	public void getAllRoles_Returns_Role_List() {
		// GIVEN
		given(role.getId()).willReturn("dummy");
		given(role2.getId()).willReturn("dummy2");
		List<Role> result = Arrays.asList(role, role2);
		given(super.roleDao.findAll()).willReturn(result);
		// WHEN
		this.roleService.getAllRoles();
		// THEN
		verify(super.roleDao, times(1)).findAll();
		assertEquals("dummy", roleService.getAllRoles().get(0).getId());
		assertEquals("dummy2", roleService.getAllRoles().get(1).getId());
	}


	@Test
	public void getRole_Returns_Role()
			throws NoSuchObjectException {
		// GIVEN
		given(role.getId()).willReturn("dummy");
		given(super.roleDao.findById(anyString())).willReturn(role);
		// WHEN
		this.roleService.getRole("dummy");
		// THEN
		verify(super.roleDao, times(1)).findById("dummy");
		assertEquals("dummy", roleService.getRole("dummy").getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getRole_Reports_When_No_Such_Role()
			throws NoSuchObjectException {
		// GIVEN
		given(super.roleDao.findById(anyString())).willReturn(null);
		// WHEN
		this.roleService.getRole("dummy");
		// THEN
		verify(super.roleDao, times(1)).findById("dummy");
	}

}