package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;
import ru.sergm.picocmdb.exception.NoSuchObjectException;
import ru.sergm.picocmdb.rest.RestError;

import java.util.Locale;


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
	@MockBean							// to create and inject mock
	private ResourceBundleMessageSource errorMessageSource;
	private String defaultLocalizedErrorMessage = "Undefined error occured.";


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
		assertNotNull(errorMessageSource);
	}


	@Test
	public void getErrorCode_Returns_Error_Code() {
		// GIVEN: to stub methods in the mocks
		given(this.env.getProperty(anyString())).willReturn("dummy_error_code");
		// WHEN
		this.systemService.getErrorCode("known.DummyException");
		// THEN: to verify that correct Environment method is called req num of times and with req arguments
		verify(env, times(1)).getProperty("known.DummyException");
		assertEquals("dummy_error_code", this.systemService.getErrorCode("known.DummyException"));
	}


	@Test
	public void getErrorCode_Returns_Empty_String_On_Unknown_Exception() {
		// GIVEN: to stub methods in the mocks
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
		// GIVEN: to stub methods in the mocks
		given(this.errorMessageSource.getMessage(anyString(), any(), any())).willThrow(NoSuchMessageException.class);
		// WHEN
		String localizedErrorMessage = this.systemService.getLocalizedErrorMessage("unknown.DummyException", "UNKNOWNERROR", Locale.ENGLISH);
		// THEN
		verify(this.errorMessageSource, times(2)).getMessage("unknown.DummyException.UNKNOWNERROR", null, Locale.ENGLISH);
		assertEquals(this.defaultLocalizedErrorMessage, localizedErrorMessage);
	}


	@Test
	public void getRestError_Returns_RestError() {
		// GIVEN: to stub methods in the mocks
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