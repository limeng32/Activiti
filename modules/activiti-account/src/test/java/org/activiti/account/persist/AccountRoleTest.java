package org.activiti.account.persist;

import javax.sql.DataSource;

import org.activiti.account.service.AccountRoleService;
import org.activiti.account.service.AccountService;
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
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource" })
public class AccountRoleTest {

	@Autowired
	private DataSource dataSourceAccount;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRoleService accountRoleService;

	@Autowired
	private RoleService roleService;

	@Test
	public void test() {
		Assert.assertNotNull(dataSourceAccount);
		Assert.assertNotNull(accountRoleService);
	}

	@Test
	@DatabaseSetup(connection = "dataSource", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/account/service/accountRoleTest/testAccountRole.xml")
	@ExpectedDatabase(connection = "dataSource", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/account/service/accountRoleTest/testAccountRole.result.xml")
	@DatabaseTearDown(connection = "dataSource", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/account/service/accountRoleTest/testAccountRole.result.xml")
	public void testAccountRole() {
		AccountRole accountRole = accountRoleService.select("ar1");
		Assert.assertEquals("alice", accountRole.getAccount().getName());
		Assert.assertEquals(RoleStatus.a, accountRole.getRole().getValue());

		Account account = accountService.select("a2");
		Role role = roleService.select("r2");
		accountRole.setAccount(account);
		accountRole.setRole(role);
		accountRoleService.update(accountRole);
	}
}
