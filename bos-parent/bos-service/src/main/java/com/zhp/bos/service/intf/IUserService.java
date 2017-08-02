package com.zhp.bos.service.intf;

import com.zhp.bos.entity.user.User;

public interface IUserService {

	public void save(User user);

	/**
	 * 用户登录：根据用户名和密码查询用户
	 * 
	 * @param model
	 * @return
	 */
	public User findUserByEmailAndPassword(User model);

	/**
	 * 根据用户电话号码查找用户
	 * 
	 * @param telephone
	 * @return
	 */
	public User findUserByTelephone(String telephone);

	/**
	 * 修改用户密码
	 * 
	 * @param model
	 */
	public void updatePassword(User model);
}
