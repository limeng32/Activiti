package org.activiti.account.statics;

import org.junit.Assert;
import org.junit.Test;

public class AccountStatusTest {

	@Test
	public void test() {
		Assert.assertTrue(true);
		AccountStatus as = AccountStatus.forId("sleep");
		Assert.assertEquals("休眠", as.text());
	}

}
