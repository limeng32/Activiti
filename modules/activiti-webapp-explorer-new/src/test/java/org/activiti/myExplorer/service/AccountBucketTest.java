package org.activiti.myExplorer.service;

import org.activiti.myExplorer.persist.AccountBucket;
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
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseTearDowns;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-explorer-test.xml", "classpath:activiti-account-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource",
		"dataSourceAccount" })
public class AccountBucketTest {

	@Autowired
	private AccountBucketService accountBucketService;

	@Test
	public void test() {
		Assert.assertNotNull(accountBucketService);
	}

	@Test
	@DatabaseSetups({
			@DatabaseSetup(connection = "dataSource", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.xml"),
			@DatabaseSetup(connection = "dataSourceAccount", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.dataSourceAccount.xml"), })
	@ExpectedDatabases({
			@ExpectedDatabase(connection = "dataSource", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.result.xml"),
			@ExpectedDatabase(connection = "dataSourceAccount", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.dataSourceAccount.result.xml") })
	@DatabaseTearDowns({
			@DatabaseTearDown(connection = "dataSource", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.result.xml"),
			@DatabaseTearDown(connection = "dataSourceAccount", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.dataSourceAccount.result.xml") })
	public void testAccountBucket() {
		AccountBucket accountBucket = accountBucketService.select("ab1");
		Assert.assertEquals("a", accountBucket.getName());
		Assert.assertEquals("alice", accountBucket.getAccount().getName());
		Assert.assertEquals(2, accountBucket.getUnreadNotifyCount().intValue());
		Assert.assertEquals("asd", accountBucket.getAvatar());
	}
}
