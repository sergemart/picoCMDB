package ru.sergm.picocmdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import ru.sergm.picocmdb.AbstractTest;
import ru.sergm.picocmdb.domain.ManagedArea;


@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {PicocmdbApplication.class})
public class ManagedAreaServiceTests extends AbstractTest {

	@Autowired
	private ManagedAreaService managedAreaService;	// the CuT
	@Mock
	private ManagedArea managedArea, managedArea2;


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


	@Test
	public void getAllManagedAreas_Returns_ManagedArea_List() {
		// GIVEN: to stub methods in the mocks
		given(managedArea.getId()).willReturn(1L);
		given(managedArea2.getId()).willReturn(2L);
		List<ManagedArea> result = Arrays.asList(managedArea, managedArea2);
		given(managedAreaDao.findAll()).willReturn(result);
		// WHEN
		managedAreaService.getAllManagedAreas();
		// THEN: to verify that correct DAO method is called req num of times and with req arguments
		verify(managedAreaDao, times(1)).findAll();
		assertEquals(1L, (long)managedAreaService.getAllManagedAreas().get(0).getId());
		assertEquals(2L, (long)managedAreaService.getAllManagedAreas().get(1).getId());
	}

    /*
	@Test
	public void getRole_Returns_Role() throws NoSuchObjectException {
		// GIVEN: to stub methods in the mocks
		given(managedArea.getId()).willReturn("dummy");
		given(managedAreaDao.findById(anyString())).willReturn(managedArea);
		// WHEN
		managedAreaService.getRole("dummy");
		// THEN: to verify that correct DAO method is called req num of times and with req arguments
		verify(managedAreaDao, times(1)).findById("dummy");
		assertEquals("dummy", managedAreaService.getRole("dummy").getId());
	}


	@Test(expected = NoSuchObjectException.class)
	public void getRole_Reports_When_No_Such_Role() throws NoSuchObjectException {
		// GIVEN: to stub methods in the mocks
		given(managedAreaDao.findById(anyString())).willReturn(null);
		// WHEN
		managedAreaService.getRole("dummy");
		// THEN: to verify that correct DAO method is called req num of times and with req arguments
		verify(managedAreaDao, times(1)).findById("dummy");
	}
    */
}