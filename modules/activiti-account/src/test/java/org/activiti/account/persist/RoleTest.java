package org.activiti.account.persist;

import javax.sql.DataSource;

import org.activiti.account.service.RoleService;
import org.activiti.account.statics.RoleStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-account-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSourceAccount" })
public class RoleTest {

	@Autowired
	private DataSource dataSourceAccount;

	@Autowired
	private RoleService roleService;

	@Test
	public void test() {
		Assert.assertNotNull(dataSourceAccount);
		Assert.assertNotNull(roleService);
	}

	@Test
	@DatabaseSetup(connection = "dataSourceAccount", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/account/service/roleTest/testRole.xml")
	@ExpectedDatabase(connection = "dataSourceAccount", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/account/service/roleTest/testRole.result.xml")
	@DatabaseTearDown(connection = "dataSourceAccount", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/account/service/roleTest/testRole.result.xml")
	public void testRole() {
		Role role = roleService.select("1");
		Assert.assertEquals("管理员", role.getValue().text());

		role.setName("Admin");
		roleService.update(role);

		Role r = new Role();
		r.setName("cool");
		r.setValue(RoleStatus.forId("USER"));
		roleService.insert(r);

		Role r2 = new Role();
		int i = roleService.count(r2);
		Assert.assertEquals(3, i);
	}
}
