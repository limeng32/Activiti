package org.activiti.myExplorer.service;

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
@ContextConfiguration({ "classpath:activiti-account-test.xml", "classpath:activiti-explorer-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource" })
public class AccountBucketTest {


	@Autowired
	private AccountBucketService accountBucketService;

	@Test
	public void test() {
		Assert.assertNotNull(accountBucketService);
	}

	@Test
	@DatabaseSetup(connection = "dataSource", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.xml")
	@ExpectedDatabase(connection = "dataSource", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.result.xml")
	@DatabaseTearDown(connection = "dataSource", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/myExplorer/service/accountBucketTest/testAccountBucket.result.xml")
	public void testAccountBucket() {
	}
}
