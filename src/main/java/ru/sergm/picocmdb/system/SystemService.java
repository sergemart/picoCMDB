package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

import ru.sergm.picocmdb.exception.BaseException;
import ru.sergm.picocmdb.rest.RestError;


@Service
@PropertySource("classpath:errorcodes.properties")
public class SystemService {

	@Autowired
	private Environment env;
	@Autowired
	private ResourceBundleMessageSource errorMessageSource;


	public String getErrorCode(String exceptionName) {
		String result = env.getProperty(exceptionName);
		if (result == null) return ""; else return result;
	}


	public String getLocalizedErrorMessage(String exceptionName, String errorName, Locale locale) {
		String result = "Undefined error occurred."; // default localized error message

		// empty parameters yield default message
		if (Objects.equals(exceptionName, "") || exceptionName == null) return result;
		if (Objects.equals(errorName, "") || errorName == null) return result;
		if (locale == null) return result;

		String messageId = exceptionName + "." + errorName;
		try {
			result = errorMessageSource.getMessage(messageId, null, locale);
		} catch (NoSuchMessageException e) {
			// 'no such message' yields default message
			return result;
		}
		return result;
	}


	public RestError getRestError(BaseException e, Locale locale) {
		if (e == null) return new RestError(null, "", this.getLocalizedErrorMessage(null, null, null));
		String errorCode = this.getErrorCode(e.getExceptionName());
		String localizedErrorMessage = this.getLocalizedErrorMessage(e.getExceptionName(), e.getErrorName(), locale);
		return new RestError(e, errorCode, localizedErrorMessage);
	}

}
