package org.activiti.account.persist;

import javax.sql.DataSource;

import org.activiti.account.service.AccountBucketService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class AccountBucketTest {

	@Autowired
	private DataSource dataSourceAccount;

	@Autowired
	private AccountBucketService accountBucketService;

	@Autowired
	private RedisTemplate<String, String> redisTemplateString;

	@Autowired
	private RedisTemplate<String, Integer> redisTemplateInteger;

	@Test
	public void test() {
		Assert.assertNotNull(dataSourceAccount);
		Assert.assertNotNull(accountBucketService);
		String paramK = "中5";
		String paramV = "英5";
		redisTemplateString.opsForValue().set(paramK, paramV);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("::" + redisTemplateString.opsForValue().get(paramK));
		redisTemplateInteger.opsForValue().set("asd", 12);
		redisTemplateInteger.delete("asd");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("::" + redisTemplateString.opsForValue().get("asd"));
	}

	@Test
	@DatabaseSetup(connection = "dataSourceAccount", type = DatabaseOperation.CLEAN_INSERT, value = "/org/activiti/account/service/accountBucketTest/testAccountBucket.xml")
	@ExpectedDatabase(connection = "dataSourceAccount", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/org/activiti/account/service/accountBucketTest/testAccountBucket.result.xml")
	@DatabaseTearDown(connection = "dataSourceAccount", type = DatabaseOperation.DELETE_ALL, value = "/org/activiti/account/service/accountBucketTest/testAccountBucket.result.xml")
	public void testAccountBucket() {
		AccountBucket accountBucket = accountBucketService.select("ab1");
		Assert.assertEquals("a", accountBucket.getNickname());
		Assert.assertEquals("alice", accountBucket.getAccount().getName());

		AccountBucket accountBucket2 = accountBucketService.select("ab2");
		accountBucket2.setUploadedSize(200L);
		accountBucketService.update(accountBucket2);
	}
}
