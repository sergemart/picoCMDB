package ru.sergm.picocmdb;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import org.junit.runner.RunWith;

import ru.sergm.picocmdb.dao.ManagedAreaDao;
import ru.sergm.picocmdb.dao.RoleDao;


/**
 * A place to define all MockBeans used throughout all tests, to avoid multiple Spring context loading
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AbstractTest {

	@MockBean							// to create and inject mock
	protected Environment env;
	@MockBean							// to create and inject mock
	protected ResourceBundleMessageSource errorMessageSource;

	@MockBean							// to create and inject mock
	protected RoleDao roleDao;
	@MockBean                            // to create and inject mock
	protected ManagedAreaDao managedAreaDao;

}
