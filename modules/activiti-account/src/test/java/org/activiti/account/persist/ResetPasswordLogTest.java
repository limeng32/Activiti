package org.activiti.account.persist;

import javax.sql.DataSource;

import org.activiti.account.service.ResetPasswordLogService;
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
public class ResetPasswordLogTest {

	@Autowired
	private DataSource dataSourceAccount;

	@Autowired
	private ResetPasswordLogService resetPasswordLogService;

	@Test
	public void test() {
		Assert.assertNotNull(dataSourceAccount);
		Assert.assertNotNull(resetPasswordLogService);
	}

	@Test
	@DatabaseSetup(connection = "dataSourceAccount", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/account/service/resetPasswordLogTest/testResetPasswordLog.xml")
	@ExpectedDatabase(connection = "dataSourceAccount", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/account/service/resetPasswordLogTest/testResetPasswordLog.result.xml")
	@DatabaseTearDown(connection = "dataSourceAccount", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/account/service/resetPasswordLogTest/testResetPasswordLog.result.xml")
	public void testResetPasswordLog() {
		ResetPasswordLog resetPasswordLog = resetPasswordLogService.select("rp1");
		Assert.assertEquals("aaa", resetPasswordLog.getUrl());
		Assert.assertEquals("alice", resetPasswordLog.getAccount().getName());

		ResetPasswordLog resetPasswordLog2 = resetPasswordLogService.select("rp2");
		resetPasswordLog2.setUrl("ccc");
		resetPasswordLog2.setAccount(resetPasswordLog.getAccount());
		resetPasswordLogService.update(resetPasswordLog2);
	}
}
