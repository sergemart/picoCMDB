package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;


@Component
public class ErrorMessages {

	@Autowired
	private MessageSource messageSource;
	private MessageSourceAccessor accessor;


	@PostConstruct
	private void init() {
		this.accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
	}


	public String getErrorMessage(String messageId) {
		return accessor.getMessage(messageId);
	}

}
