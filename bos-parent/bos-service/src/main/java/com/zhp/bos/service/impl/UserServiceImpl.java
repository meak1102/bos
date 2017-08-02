package com.zhp.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.UserDao;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.service.intf.IUserService;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private JmsTemplate JmsTemplate;

	@Override
	public void save(User user) {
		userDao.save(user);

	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByEmailAndPassword(User model) {

		// return userDao.findUserByEmailAndPassword(model.getEmail(),
		// model.getPassword());
		// return userDao.login(model.getEmail(), model.getPassword());
		// return userDao.login2(model.getEmail(), model.getPassword());
		// return userDao.login3(model.getEmail(), model.getPassword());
		return userDao.login4(model.getEmail(), model.getPassword());

	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByTelephone(String telephone) {

		return userDao.findUserByTelephone(telephone);
	}

	@Override
	public void updatePassword(User model) {
		userDao.updatePassword(model.getTelephone(), model.getPassword());
	}

}
