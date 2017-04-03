package ru.sergm.picocmdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import ru.sergm.picocmdb.AbstractIntegrationTestSuite;
import ru.sergm.picocmdb.domain.Role;


//@RunWith(SpringRunner.class)
//@DataJpaTest
@Ignore
public class RoleDaoIT extends AbstractIntegrationTestSuite {

	//@Autowired
	//private TestEntityManager em; // lightweight alternative to JPA/Hibernate EM

	@Autowired
	private RoleDao roleDao; // the CuT


	@Test
	public void roleDao_Finds_No_Entities_When_Repository_Is_Empty() {
		List<Role> result = roleDao.findAll();

		assertThat(result).isEmpty();
	}

}
