package org.activiti.account.persist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:activiti-account-test.xml", "classpath:activiti-account-redis.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = { "dataSource" })
public class RedisTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplateString;

	@Autowired
	private RedisTemplate<String, Integer> redisTemplateInteger;

	@Test
	@IfProfileValue(name = "REDIS", value = "true")
	public void test() {
		String paramK = "中6";
		String paramV = "英6";
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

}
