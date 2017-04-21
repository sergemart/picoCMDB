package com.github.sergemart.picocmdb.config;

import com.github.sergemart.picocmdb.service.ConfigurationItemRelationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.github.sergemart.picocmdb.exception.NoSuchObjectException;
import com.github.sergemart.picocmdb.service.ManagedAreaService;
import com.github.sergemart.picocmdb.system.CurrentSessionSettings;


@Configuration
public class SystemConfig {

	@Autowired
	private ManagedAreaService managedAreaService;

	@Autowired
	private ConfigurationItemRelationTypeService ciRelationTypeService;


	/**
	 * A bean source for a MessageSource of localized error messages
	 */
	@Bean
	public ResourceBundleMessageSource errorMessageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("i18n/errormessages");
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

	/**
	 * A bean source for current user effective settings container
	 */
	@Bean
	@Scope(proxyMode= ScopedProxyMode.TARGET_CLASS, value="session")
	public CurrentSessionSettings currentSessionSettings()
			throws NoSuchObjectException {
		CurrentSessionSettings settings = new CurrentSessionSettings();
		settings.setCurrentManagedArea(managedAreaService.getAllManagedAreas().get(0)); // temporary stub
		settings.setCurrentCiRelationType(ciRelationTypeService.getRelationType("DEPENDENCY")); // temporary stub
		return settings;
	}

}
