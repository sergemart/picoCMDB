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
import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;


public class ConfigurationItemRelationTypeServiceTests extends AbstractUnitTests {

	@Autowired
	private ConfigurationItemRelationTypeService entityService;	// the CuT
	@Mock
	private ConfigurationItemRelationType entity, entity2;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Are_Injected() {
		assertNotNull(this.entityService);
		assertNotNull(this.entity);
		assertNotNull(this.entity2);
		// all MockBeans are defined in super to avoid multiple Spring context loading
		assertNotNull(super.configurationItemRelationTypeDao);
	}

	// -------------- READ --------------
	
	@Test
	public void getAllRelationTypes_Returns_RelationType_List() {
		// GIVEN
		given(entity.getId()).willReturn("dummy");
		given(entity2.getId()).willReturn("dummy2");
		List<ConfigurationItemRelationType> result = Arrays.asList(entity, entity2);
		given(super.configurationItemRelationTypeDao.findAll()).willReturn(result);
		// WHEN
		this.entityService.getAllRelationTypes();
		// THEN
		verify(super.configurationItemRelationTypeDao, times(1)).findAll();
		assertEquals("dummy", entityService.getAllRelationTypes().get(0).getId());
		assertEquals("dummy2", entityService.getAllRelationTypes().get(1).getId());
	}


	@Test
	public void getRelationType_Returns_RelationType()
			throws NoSuchObjectException {
		// GIVEN
		given(entity.getId()).willReturn("dummy");
		given(super.configurationItemRelationTypeDao.findById(anyString())).willReturn(entity);
		// WHEN
		this.entityService.getRelationType("dummy");
		// THEN
		verify(super.configurationItemRelationTypeDao, times(1)).findById("dummy");
		assertEquals("dummy", entityService.getRelationType("dummy").getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getRelationType_Reports_When_No_Such_RelationType()
			throws NoSuchObjectException {
		// GIVEN
		given(super.configurationItemRelationTypeDao.findById(anyString())).willReturn(null);
		// WHEN
		this.entityService.getRelationType("dummy");
		// THEN
		verify(super.configurationItemRelationTypeDao, times(1)).findById("dummy");
	}

}