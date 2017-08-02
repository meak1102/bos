package com.zhp.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zhp.bos.entity.user.User;

public interface UserDao extends JpaRepository<User, Integer> {
	// 1. 根据方法名自动生成sql语句（方法名按照规范命名）
	public User findUserByEmailAndPassword(String email, String password);

	// 2.jpql类似于hql，面向对象编写jpql语句
	@Query("from User Where email=?1 and password=?2")
	public User login(String email, String password);

	// 3.jpql语句优化，都写在dao中代码集中，统一写在对应实体类上（命名查询）
	public User login2(String email, String password);

	// 4. 纯sql查询
	@Query(value = "select * from t_user where email=? and password=?", nativeQuery = true)
	public User login3(String email, String password);

	// 5.占位查询
	@Query(" from User where email=:email and password=:password")
	public User login4(@Param("email") String email, @Param("password") String password);

	@Query("from User Where telephone=?")
	public User findUserByTelephone(String telephone);

	@Modifying
	@Query("update User set password=?2 where telephone=?1")
	public void updatePassword(String telephone, String password);

	public User findUserByEmail(String email);

}
