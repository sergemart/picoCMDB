package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.sergm.picocmdb.exception.NoSuchObjectException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemServiceTests {

	@Autowired
	private SystemService systemService;	// the CuT
	@Mock
	private RestError restError;
	@Mock
	private NoSuchObjectException e;
	@MockBean							// to create and inject mock
	private Environment env;


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Should_Be_Created() {
		assertNotNull(systemService);
		assertNotNull(e);
		assertNotNull(restError);
		assertNotNull(env);
	}


	@Test
	public void getErrorCode_Returns_Error_Code() {
		// ARRANGE: to stub methods in the mocks
		given(env.getProperty("known.DummyException")).willReturn("dummy_error_code");
		//ACT
		systemService.getErrorCode("known.DummyException");
		// ASSERT: to verify that correct Environment method is called req num of times and with req arguments
		verify(env, times(1)).getProperty("known.DummyException");
		assertEquals("dummy_error_code", systemService.getErrorCode("known.DummyException"));
	}


	@Test
	public void getErrorCode_Returns_Empty_String_On_Unknown_Exception() {
		// ARRANGE: to stub methods in the mocks
		given(env.getProperty("unknown.DummyException")).willReturn(null);
		//ACT
		systemService.getErrorCode("unknown.DummyException");
		// ASSERT: to verify that correct Environment method is called req num of times and with req arguments
		verify(env, times(1)).getProperty("unknown.DummyException");
		assertEquals("", systemService.getErrorCode("unknown.DummyException"));
	}


	@Test
	public void getRestError_Returns_RestError() {
		// ARRANGE: to stub methods in the mocks
		String mockedExceptionClassCanonicalName = e.getClass().getCanonicalName();
		given(env.getProperty(mockedExceptionClassCanonicalName)).willReturn("dummy_error_code");
		given(e.getExceptionName()).willReturn(mockedExceptionClassCanonicalName);
		given(e.getMessage()).willReturn("Dummy message.");
		given(e.getLocalizedMessage()).willReturn("Произошла ошибка.");
		//ACT
		systemService.getRestError(e);
		// ASSERT: to verify that correct Environment method is called req num of times and with req arguments
		assertEquals("dummy_error_code", systemService.getRestError(e).getErrorCode());
		assertEquals(mockedExceptionClassCanonicalName, systemService.getRestError(e).getExceptionName());
		assertEquals("Dummy message.", systemService.getRestError(e).getMessage());
		assertEquals("Произошла ошибка.", systemService.getRestError(e).getLocalizedMessage());
	}


}