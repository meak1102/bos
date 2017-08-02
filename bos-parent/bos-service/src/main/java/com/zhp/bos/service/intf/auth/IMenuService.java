package com.zhp.bos.service.intf.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.zhp.bos.entity.auth.Menu;

public interface IMenuService {

	List<Menu> ajaxListHasSonMenus();

	void save(Menu model);

	Page<Menu> pageQuery(PageRequest pageRequest);

	List<Menu> ajaxList();

	List<Menu> findMenuByRoleId(String roleId);

	List<Menu> menuList(Integer userId);

}
