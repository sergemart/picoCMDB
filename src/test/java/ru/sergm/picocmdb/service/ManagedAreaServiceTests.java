package ru.sergm.picocmdb.service;

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

import ru.sergm.picocmdb.AbstractUnitTestSuite;
import ru.sergm.picocmdb.domain.ManagedArea;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.exception.ObjectAlreadyExistsException;
import ru.sergm.picocmdb.exception.WrongDataException;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ManagedAreaServiceTests extends AbstractUnitTestSuite {

	@Autowired
	private ManagedAreaService managedAreaService;	// the CuT
	@Mock
	private ManagedArea managedArea, managedArea2, managedArea3;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Should_Be_Created() {
		assertNotNull(this.managedAreaService);
		assertNotNull(this.managedArea);
		// all MockBeans are defined in super to avoid multiple Spring context loading
		assertNotNull(super.managedAreaDao);
	}

	// -------------- CREATE --------------

	@Test
	public void createManagedArea_Creates_ManagedArea()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		given(managedArea.getId()).willReturn(1L);
		given(managedArea.getName()).willReturn("dummy_name");
		given(managedArea.getDescription()).willReturn("dummy_description");
		given( managedAreaDao.save((ManagedArea) any()) ).willReturn(managedArea);
		// WHEN
		managedAreaService.createManagedArea(managedArea);
		// THEN
		verify(managedAreaDao, times(1)).save(managedArea);
		assertEquals(1L, (long)managedAreaService.createManagedArea(managedArea).getId());
		assertEquals("dummy_name", managedAreaService.createManagedArea(managedArea).getName());
		assertEquals("dummy_description", managedAreaService.createManagedArea(managedArea).getDescription());
	}


	@Test
	public void createManagedArea_Reports_When_ManagedArea_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Managed Area is not provided");
		// WHEN
		managedAreaService.createManagedArea(null);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void createManagedArea_Reports_When_ManagedArea_Name_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Managed Area name is not provided");
		given(managedArea.getName()).willReturn(null);
		// WHEN
		managedAreaService.createManagedArea(managedArea);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void createManagedArea_Reports_When_ManagedArea_With_Same_Name_Exists()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(ObjectAlreadyExistsException.class);
		super.expectedException.expectMessage("Managed Area named 'dummy' already exists");
		given(managedArea.getName()).willReturn("dummy");
		given(managedArea2.getName()).willReturn("dummy");
		given( managedAreaDao.findByName(anyString()) ).willReturn(managedArea2);
		// WHEN
		managedAreaService.createManagedArea(managedArea);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void createManagedArea_Reports_When_ManagedArea_Has_No_Required_Fields()
			throws ObjectAlreadyExistsException, WrongDataException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Managed Area missing required fields");
		given(managedArea.getName()).willReturn("dummy");
		given( managedAreaDao.save((ManagedArea) any()) ).willThrow(DataIntegrityViolationException.class);
		// WHEN
		managedAreaService.createManagedArea(managedArea);
		// THEN
		verify(managedAreaDao, times(1)).save(managedArea);
	}

	// -------------- READ --------------

	@Test
	public void getAllManagedAreas_Returns_ManagedArea_List() {
		// GIVEN
		given(managedArea.getId()).willReturn(1L);
		given(managedArea2.getId()).willReturn(2L);
		List<ManagedArea> result = Arrays.asList(managedArea, managedArea2);
		given(managedAreaDao.findAll()).willReturn(result);
		// WHEN
		managedAreaService.getAllManagedAreas();
		// THEN
		verify(managedAreaDao, times(1)).findAll();
		assertEquals(1L, (long)managedAreaService.getAllManagedAreas().get(0).getId());
		assertEquals(2L, (long)managedAreaService.getAllManagedAreas().get(1).getId());
	}


	@Test
	public void getManagedArea_Returns_ManagedArea()
			throws NoSuchObjectException {
		// GIVEN
		given(managedArea.getId()).willReturn(1L);
		given( managedAreaDao.findById(anyLong()) ).willReturn(managedArea);
		// WHEN
		managedAreaService.getManagedArea(1L);
		// THEN
		verify(managedAreaDao, times(1)).findById(1L);
		assertEquals(1L, (long)managedAreaService.getManagedArea(1L).getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getManagedArea_Reports_When_No_Such_ManagedArea()
			throws NoSuchObjectException {
		// GIVEN
		given(managedAreaDao.findById(anyLong())).willReturn(null);
		// WHEN
		managedAreaService.getManagedArea(1L);
		// THEN
		verify(managedAreaDao, times(1)).findById(1L);
	}

	// -------------- UPDATE --------------

	@Test
	public void updateManagedArea_Updates_ManagedArea()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
			// to stub input update data
		given(managedArea.getId()).willReturn(1L);
		given(managedArea.getName()).willReturn("new_dummy_name");
		given(managedArea.getDescription()).willReturn("new_dummy_description");
			// to stub existing object to be updated
		given(managedArea2.getId()).willReturn(1L);
		given(managedArea2.getName()).willReturn("dummy_name");
			//
		given( managedAreaDao.save((ManagedArea) any()) ).willReturn(managedArea);
		given( managedAreaDao.findById(anyLong()) ).willReturn(managedArea2);
		// WHEN
		managedAreaService.updateManagedArea(1L, managedArea);
		// THEN
		verify(managedAreaDao, times(1)).save(managedArea);
		assertEquals(1L, (long)managedAreaService.updateManagedArea(1L, managedArea).getId());
		assertEquals("new_dummy_name", managedAreaService.updateManagedArea(1L, managedArea).getName());
		assertEquals("new_dummy_description", managedAreaService.updateManagedArea(1L, managedArea).getDescription());
	}


	@Test
	public void updateManagedArea_Reports_When_Current_ManagedArea_Id_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Current Managed Area ID is not provided");
		// WHEN
		managedAreaService.updateManagedArea(null, managedArea);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void updateManagedArea_Reports_When_New_ManagedArea_Data_Is_Null()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("New Managed Area data are not provided");
		// WHEN
		managedAreaService.updateManagedArea(1L, null);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void updateManagedArea_Reports_When_ManagedArea_With_Same_Name_Exists()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(ObjectAlreadyExistsException.class);
		super.expectedException.expectMessage("Managed Area named 'dummy_name' already exists.");
		// to stub input update data, which include new conflicting name
		given(managedArea.getId()).willReturn(1L);
		given(managedArea.getName()).willReturn("dummy_name");
		// to stub existing object to be updated
		given(managedArea2.getId()).willReturn(1L);
		given(managedArea2.getName()).willReturn("old_dummy_name");
		// to stub existing object which name conflicts with update data
		given(managedArea3.getId()).willReturn(2L);
		given(managedArea3.getName()).willReturn("dummy_name");
		//
		given( managedAreaDao.save((ManagedArea) any()) ).willReturn(managedArea);
		given( managedAreaDao.findById(1L) ).willReturn(managedArea2);
		given( managedAreaDao.findByName("dummy_name") ).willReturn(managedArea3);
		// WHEN
		managedAreaService.updateManagedArea(1L, managedArea);
		// THEN
		verify(managedAreaDao, times(0)).save(managedArea);
	}


	@Test
	public void updateManagedArea_Reports_When_ManagedArea_Data_Have_No_Required_Fields()
			throws ObjectAlreadyExistsException, WrongDataException, NoSuchObjectException {
		// GIVEN
		super.expectedException.expect(WrongDataException.class);
		super.expectedException.expectMessage("Managed Area missing required fields");
		// to stub input update data
		given(managedArea.getId()).willReturn(1L);
		given(managedArea.getName()).willReturn("new_dummy_name");
		given(managedArea.getDescription()).willReturn("new_dummy_description");
		// to stub existing object to be updated
		given(managedArea2.getId()).willReturn(1L);
		given(managedArea2.getName()).willReturn("dummy_name");
		//
		given( managedAreaDao.findById(anyLong()) ).willReturn(managedArea2);
		given( managedAreaDao.save((ManagedArea) any()) ).willThrow(DataIntegrityViolationException.class);
		// WHEN
		managedAreaService.updateManagedArea(1L, managedArea);
		// THEN
		verify(managedAreaDao, times(1)).save(managedArea);
	}

	// -------------- DELETE --------------

	@Test
	public void deleteManagedArea_Deletes_ManagedArea()
			throws NoSuchObjectException {
		// WHEN
		managedAreaService.deleteManagedArea(1L);
		// THEN
		verify(managedAreaDao, times(1)).delete(1L);
	}


	@Test
	public void deleteManagedArea_Reports_WhenNo_Such_ManagedArea()
			throws NoSuchObjectException {
		// GIVEN
		doThrow(EmptyResultDataAccessException.class).when(managedAreaDao).delete(anyLong());
		super.expectedException.expect(NoSuchObjectException.class);
		super.expectedException.expectMessage("No Managed Area identified by '1' found");
		// WHEN
		managedAreaService.deleteManagedArea(1L);
		// THEN
		verify(managedAreaDao, times(1)).delete(1L);
	}

}