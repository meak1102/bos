package com.zhp.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.auth.Menu;

public interface MenuDao extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {
	@Query("from Menu where generatemenu=1 order by zindex desc")
	List<Menu> ajaxListHasSonMenus();

	@Query("from Menu m inner join fetch m.roles r where r.id=?1 ")
	List<Menu> findMenuByRoleId(String roleId);

	// 避免一个用户多个角色导致菜单重复
	@Query("select distinct m from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id=?1 order by m.zindex desc")
	List<Menu> menuList(Integer userId);

}
