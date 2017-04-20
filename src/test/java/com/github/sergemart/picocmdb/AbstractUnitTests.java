package com.github.sergemart.picocmdb;

import com.github.sergemart.picocmdb.dao.ConfigurationItemRelationTypeDao;
import com.github.sergemart.picocmdb.dao.ConfigurationItemTypeDao;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import org.junit.runner.RunWith;
import org.junit.Rule;

import com.github.sergemart.picocmdb.dao.ManagedAreaDao;
import com.github.sergemart.picocmdb.dao.RoleDao;


/**
 * A place to define all MockBeans used throughout all tests, to avoid multiple Spring context loading. Also common rules are here.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractUnitTests {

	// create and inject common mocks

	@MockBean
	protected Environment env;
	@MockBean
	protected ResourceBundleMessageSource errorMessageSource;

	// create and inject DAO mocks

	@MockBean
	protected RoleDao roleDao;
	@MockBean
	protected ManagedAreaDao managedAreaDao;
	@MockBean
	protected ConfigurationItemTypeDao configurationItemTypeDao;
	@MockBean
	protected ConfigurationItemRelationTypeDao configurationItemRelationTypeDao;

	// JUnit rules

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); // there is no public constructor
}
