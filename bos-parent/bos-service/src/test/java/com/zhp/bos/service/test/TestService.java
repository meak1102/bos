package com.zhp.bos.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhp.bos.service.intf.user.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-entity.xml", "classpath:applicationContext-dao.xml",
		"classpath:applicationContext-service.xml" })
public class TestService {

	@Autowired
	private IUserService userService;

	// private BaseDao userDao;
	@Test
	public void test1() {
		/*
		 * User user = new User(); user.setEmail("102353451@QQ.COM");
		 * user.setPassword("1231"); user.setTelephone("12423531656");
		 * userService.save(user); //
		 */ // userDao.save(user);
	}

}
