package com.zhp.bos.entity.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-entity.xml" })
public class TestDao {
	@Test
	public void test1() {
		System.out.println(123);
	}
}
