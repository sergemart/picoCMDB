package com.github.sergemart.picocmdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import java.util.Locale;


@Configuration
public class SystemConfig {

	/*
	@Bean
	public AbstractLocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ENGLISH);
		return slr;
	}
    */

	@Bean
	public ResourceBundleMessageSource errorMessageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("i18n/errormessages");
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

}
