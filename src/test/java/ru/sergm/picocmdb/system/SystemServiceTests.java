package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Locale;

import ru.sergm.picocmdb.AbstractTest;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.rest.RestError;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemServiceTests extends AbstractTest {

	@Autowired
	private SystemService systemService;	// the CuT
	@Mock
	private RestError restError;
	@Mock
	private NoSuchObjectException e;
	private final String defaultLocalizedErrorMessage = "Undefined error occurred.";


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // to init generic Mockito's @Mock(s), if any
	}


	@Test
	public void cuT_And_Mocks_Should_Be_Created() {
		assertNotNull(this.systemService);
		assertNotNull(this.e);
		assertNotNull(this.restError);
		// all MockBeans are defined in super to avoid multiple Spring context loading
		assertNotNull(super.env);
		assertNotNull(super.errorMessageSource);
	}


	@Test
	public void getErrorCode_Returns_Error_Code() {
		// GIVEN
		given(this.env.getProperty(anyString())).willReturn("dummy_error_code");
		// WHEN
		this.systemService.getErrorCode("known.DummyException");
		// THEN: to verify that correct Environment method is called req num of times and with req arguments
		verify(env, times(1)).getProperty("known.DummyException");
		assertEquals("dummy_error_code", this.systemService.getErrorCode("known.DummyException"));
	}


	@Test
	public void getErrorCode_Returns_Empty_String_On_Unknown_Exception() {
		// GIVEN
		given(this.env.getProperty(anyString())).willReturn(null);
		// WHEN
		systemService.getErrorCode("unknown.DummyException");
		// THEN: to verify that correct Environment method is called req num of times and with req arguments
		verify(env, times(1)).getProperty("unknown.DummyException");
		assertEquals("", this.systemService.getErrorCode("unknown.DummyException"));
	}


	@Test
	public void getLocalizedErrorMessage_Returns_Default_Error_Message_On_Empty_Parameters() {
		// WHEN
		String localizedErrorMessage = this.systemService.getLocalizedErrorMessage("", "", Locale.ENGLISH);
		// THEN
		assertEquals(this.defaultLocalizedErrorMessage, localizedErrorMessage);
	}


	@Test
	public void getLocalizedErrorMessage_Returns_Default_Error_Message_On_Null_Parameters() {
		// WHEN
		String localizedErrorMessage = this.systemService.getLocalizedErrorMessage(null, null, null);
		// THEN
		assertEquals(this.defaultLocalizedErrorMessage, localizedErrorMessage);
	}


	public void getLocalizedErrorMessage_Returns_Default_Error_Message_On_Unknown_Error() {
		// GIVEN
		given(this.errorMessageSource.getMessage(anyString(), any(), any())).willThrow(NoSuchMessageException.class);
		// WHEN
		String localizedErrorMessage = this.systemService.getLocalizedErrorMessage("unknown.DummyException", "UNKNOWNERROR", Locale.ENGLISH);
		// THEN
		verify(this.errorMessageSource, times(2)).getMessage("unknown.DummyException.UNKNOWNERROR", null, Locale.ENGLISH);
		assertEquals(this.defaultLocalizedErrorMessage, localizedErrorMessage);
	}


	@Test
	public void getRestError_Returns_RestError() {
		// GIVEN
		given(env.getProperty(anyString())).willReturn("dummy_error_code");
		given(e.getExceptionName()).willReturn("known.DummyException");
		given(e.getMessage()).willReturn("Dummy message.");
		// WHEN
		systemService.getRestError(e, Locale.ENGLISH);
		// THEN
		assertEquals("dummy_error_code", systemService.getRestError(e, Locale.ENGLISH).getErrorCode());
		assertEquals("known.DummyException", systemService.getRestError(e, Locale.ENGLISH).getExceptionName());
		assertEquals("Dummy message.", systemService.getRestError(e, Locale.ENGLISH).getMessage());
	}


}