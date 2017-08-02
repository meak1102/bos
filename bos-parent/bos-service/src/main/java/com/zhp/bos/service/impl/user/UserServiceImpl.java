package com.zhp.bos.service.impl.user;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.user.UserDao;
import com.zhp.bos.entity.auth.Role;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.service.intf.user.IUserService;

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

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.findUserByEmail(email);
	}

	@Override
	public void save(User model, String[] roleIds) {
		if (model.getId() == null) {
			if (roleIds != null && roleIds.length != 0) {
				for (String roleId : roleIds) {
					Role role = new Role();
					role.setId(roleId);
					model.getRoles().add(role);
				}
				userDao.save(model);
			}
		} else {
			User user = userDao.getOne(model.getId());
			Set<Role> roles = user.getRoles();
			roles.clear();
			if (roleIds != null && roleIds.length != 0) {
				for (String roleId : roleIds) {
					Role role = new Role();
					role.setId(roleId);
					roles.add(role);
				}
				userDao.save(user);
			}
		}

	}

	@Override
	public Page<User> pageQuery(PageRequest pageRequest) {
		return userDao.findAll(pageRequest);
	}

}
