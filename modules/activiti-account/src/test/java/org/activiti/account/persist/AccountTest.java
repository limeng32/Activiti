package org.activiti.account.persist;

import java.util.Collection;

import javax.sql.DataSource;

import org.activiti.account.service.AccountService;
import org.activiti.account.statics.AccountStatus;
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
public class AccountTest {

	@Autowired
	private DataSource dataSourceAccount;

	@Autowired
	private AccountService accountService;

	@Test
	public void test() {
		Assert.assertNotNull(dataSourceAccount);
		Assert.assertNotNull(accountService);
	}

	@Test
	@DatabaseSetup(connection = "dataSource", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/account/service/accountTest/testAccount.xml")
	@ExpectedDatabase(connection = "dataSource", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/account/service/accountTest/testAccount.result.xml")
	@DatabaseTearDown(connection = "dataSource", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/account/service/accountTest/testAccount.result.xml")
	public void testAccount() {
		Assert.assertNotNull(accountService);
		Account a = new Account();
		a.setName("alice");
		Collection<Account> accountC = accountService.selectAll(a);
		Assert.assertEquals(1, accountC.size());

		Account account = accountService.selectOne(a);
		Assert.assertEquals("a@l.c", account.getEmail());
		Assert.assertEquals("休眠", account.getStatus().text());

		account.setEmail("a2@l.c");
		Assert.assertEquals("1", account.getId());
		int i = accountService.update(account);
		Assert.assertEquals(1, i);

		Account a2 = new Account();
		a2.setName("carl");
		a2.setEmail("c@l.c");
		a2.setStatus(AccountStatus.forId("AWAKE"));
		accountService.insert(a2);
	}
}
