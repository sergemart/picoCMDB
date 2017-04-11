package com.github.sergemart.picocmdb.service;

import com.github.sergemart.picocmdb.AbstractUnitTests;
import com.github.sergemart.picocmdb.domain.ConfigurationItemType;
import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.exception.ObjectAlreadyExistsException;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import com.github.sergemart.picocmdb.exception.WrongDataException;


public class ConfigurationItemTypeServiceTests extends AbstractUnitTests {

	@Autowired
	private ConfigurationItemTypeService configurationItemTypeService;	// the CuT
	@Mock
	private ConfigurationItemType configurationItemType, configurationItemType2, configurationItemType3;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Are_Injected() {
		assertNotNull(this.configurationItemTypeService);
		assertNotNull(this.configurationItemType);
		// all MockBeans are defined in super to avoid multiple Spring context loading
		assertNotNull(super.configurationItemTypeDao);
	}

	// -------------- READ --------------

	@Test
	public void getAllConfigurationItemTypes_Returns_ConfigurationItemType_List() {
		// GIVEN
		given(configurationItemType.getId()).willReturn("dummy");
		given(configurationItemType2.getId()).willReturn("dummy2");
		List<ConfigurationItemType> result = Arrays.asList(configurationItemType, configurationItemType2);
		given(configurationItemTypeDao.findAll()).willReturn(result);
		// WHEN
		configurationItemTypeService.getAllConfigurationItemTypes();
		// THEN
		verify(configurationItemTypeDao, times(1)).findAll();
		assertEquals("dummy", configurationItemTypeService.getAllConfigurationItemTypes().get(0).getId());
		assertEquals("dummy2", configurationItemTypeService.getAllConfigurationItemTypes().get(1).getId());
	}


	@Test
	public void getConfigurationItemType_Returns_ConfigurationItemType()
			throws NoSuchObjectException {
		// GIVEN
		given(configurationItemType.getId()).willReturn("dummy");
		given( configurationItemTypeDao.findById(anyString()) ).willReturn(configurationItemType);
		// WHEN
		configurationItemTypeService.getConfigurationItemType("dummy");
		// THEN
		verify(configurationItemTypeDao, times(1)).findById("dummy");
		assertEquals("dummy", configurationItemTypeService.getConfigurationItemType("dummy").getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getConfigurationItemType_Reports_When_No_Such_ConfigurationItemType()
			throws NoSuchObjectException {
		// GIVEN
		given(configurationItemTypeDao.findById(anyString())).willReturn(null);
		// WHEN
		configurationItemTypeService.getConfigurationItemType("dummy");
		// THEN
		verify(configurationItemTypeDao, times(1)).findById("dummy");
	}

	// -------------- CREATE --------------

	@Test
	public void createConfigurationItemType_Creates_ConfigurationItemType()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		given(configurationItemType.getId()).willReturn("dummy");
		given(configurationItemType.getDescription()).willReturn("dummy_description");
		given( configurationItemTypeDao.save((ConfigurationItemType) any()) ).willReturn(configurationItemType);
		// WHEN
		configurationItemTypeService.createConfigurationItemType(configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(1)).save(configurationItemType);
		assertEquals("dummy", configurationItemTypeService.createConfigurationItemType(configurationItemType).getId());
		Assert.assertEquals("dummy_description", configurationItemTypeService.createConfigurationItemType(configurationItemType).getDescription());
	}


	@Test
	public void createConfigurationItemType_Reports_When_ConfigurationItemType_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Configuration Item Type is not provided");
		// WHEN
		configurationItemTypeService.createConfigurationItemType(null);
		// THEN
		verify(configurationItemTypeDao, times(0)).save(configurationItemType);
	}


	@Test
	public void createConfigurationItemType_Reports_When_ConfigurationItemType_Id_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Configuration Item Type ID is not provided");
		given(configurationItemType.getId()).willReturn(null);
		// WHEN
		configurationItemTypeService.createConfigurationItemType(configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(0)).save(configurationItemType);
	}


	@Test
	public void createConfigurationItemType_Reports_When_ConfigurationItemType_With_Same_Id_Exists()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(ObjectAlreadyExistsException.class);
		super.expectedException.expectMessage("Configuration Item Type identified by 'dummy' already exists");
		given(configurationItemType.getId()).willReturn("dummy");
		given(configurationItemType2.getId()).willReturn("dummy");
		given( configurationItemTypeDao.findById(anyString()) ).willReturn(configurationItemType2);
		// WHEN
		configurationItemTypeService.createConfigurationItemType(configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(0)).save(configurationItemType);
	}


	@Test
	public void createConfigurationItemType_Reports_When_ConfigurationItemType_Has_No_Required_Fields()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Configuration Item Type missing required fields");
		given(configurationItemType.getId()).willReturn("dummy");
		given( configurationItemTypeDao.save((ConfigurationItemType) any()) ).willThrow(DataIntegrityViolationException.class);
		// WHEN
		configurationItemTypeService.createConfigurationItemType(configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(1)).save(configurationItemType);
	}

	// -------------- UPDATE --------------

	@Test
	public void updateConfigurationItemType_Updates_ConfigurationItemType()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
			// to stub input update data
		given(configurationItemType.getId()).willReturn("dummy");
		given(configurationItemType.getDescription()).willReturn("new_dummy_description");
			// to stub existing object to be updated
		given(configurationItemType2.getId()).willReturn("dummy");
		given(configurationItemType2.getDescription()).willReturn("dummy_description");
			//
		given( configurationItemTypeDao.save((ConfigurationItemType) any()) ).willReturn(configurationItemType);
		given( configurationItemTypeDao.findById(anyString()) ).willReturn(configurationItemType2);
		// WHEN
		configurationItemTypeService.updateConfigurationItemType("dummy", configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(1)).save(configurationItemType);
		assertEquals("dummy", configurationItemTypeService.updateConfigurationItemType("dummy", configurationItemType).getId());
		assertEquals("new_dummy_description", configurationItemTypeService.updateConfigurationItemType("dummy", configurationItemType).getDescription());
	}


	@Test
	public void updateConfigurationItemType_Reports_When_Current_ConfigurationItemType_Id_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Current Configuration Item Type ID is not provided");
		// WHEN
		configurationItemTypeService.updateConfigurationItemType(null, configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(0)).save(configurationItemType);
	}


	@Test
	public void updateConfigurationItemType_Reports_When_New_ConfigurationItemType_Data_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("New Configuration Item Type data are not provided");
		// WHEN
		configurationItemTypeService.updateConfigurationItemType("dummy", null);
		// THEN
		verify(configurationItemTypeDao, times(0)).save(configurationItemType);
	}



	@Test
	public void updateConfigurationItemType_Reports_When_ConfigurationItemType_Data_Have_No_Required_Fields()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Configuration Item Type missing required fields");
		// to stub input update data
		given(configurationItemType.getId()).willReturn("dummy");
		given(configurationItemType.getDescription()).willReturn("new_dummy_description");
		// to stub existing object to be updated
		given(configurationItemType2.getId()).willReturn("dummy");
		//
		given( configurationItemTypeDao.findById(anyString()) ).willReturn(configurationItemType2);
		given( configurationItemTypeDao.save((ConfigurationItemType) any()) ).willThrow(DataIntegrityViolationException.class);
		// WHEN
		configurationItemTypeService.updateConfigurationItemType("dummy", configurationItemType);
		// THEN
		verify(configurationItemTypeDao, times(1)).save(configurationItemType);
	}

	// -------------- DELETE --------------

	@Test
	public void deleteConfigurationItemType_Deletes_ConfigurationItemType()
			throws NoSuchObjectException {
		// WHEN
		configurationItemTypeService.deleteConfigurationItemType("dummy");
		// THEN
		verify(configurationItemTypeDao, times(1)).delete("dummy");
	}


	@Test
	public void deleteConfigurationItemType_Reports_WhenNo_Such_ConfigurationItemType()
			throws NoSuchObjectException {
		// GIVEN
		doThrow(EmptyResultDataAccessException.class).when(configurationItemTypeDao).delete(anyString()); //BDDMockito.given() could not be applied to void method
		super.expectedException.expect(NoSuchObjectException.class);
		super.expectedException.expectMessage("No Configuration Item Type identified by 'dummy' found");
		// WHEN
		configurationItemTypeService.deleteConfigurationItemType("dummy");
		// THEN
		verify(configurationItemTypeDao, times(1)).delete("dummy");
	}


}