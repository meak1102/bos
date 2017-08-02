package com.zhp.bos.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-redis.xml" })
public class TestRedis {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void test1() {
		redisTemplate.opsForValue().set("天道", "圣人");
		redisTemplate.opsForValue().set("地道", "圣皇");
		redisTemplate.opsForValue().set("人道", "大帝");
		System.out.println(redisTemplate.opsForHash().get("map1", "道祖"));
	}
}
