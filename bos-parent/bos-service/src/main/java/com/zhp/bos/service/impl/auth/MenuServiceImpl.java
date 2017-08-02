package com.zhp.bos.service.impl.auth;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.auth.MenuDao;
import com.zhp.bos.entity.auth.Menu;
import com.zhp.bos.service.intf.auth.IMenuService;

@Service
@Transactional
public class MenuServiceImpl implements IMenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> ajaxListHasSonMenus() {
		return menuDao.ajaxListHasSonMenus();

	}

	@Override
	public void save(Menu model) {
		menuDao.save(model);

	}

	@Override
	public Page<Menu> pageQuery(PageRequest pageRequest) {
		Page<Menu> pages = menuDao.findAll(pageRequest);
		List<Menu> content = pages.getContent();
		if (content != null && content.size() != 0) {
			for (Menu menu : content) {
				// Hibernate.initialize(menu.getMenu());
				init(menu);
			}
		}
		return pages;
	}

	@Override
	public List<Menu> ajaxList() {
		return menuDao.findAll();

	}

	public void init(Menu menu) {
		if (menu != null) {
			Hibernate.initialize(menu.getMenu());
			init(menu.getMenu());
		}
	}

	@Override
	public List<Menu> findMenuByRoleId(String roleId) {

		return menuDao.findMenuByRoleId(roleId);
	}

	@Override
	public List<Menu> menuList(Integer userId) {

		List<Menu> menuList = menuDao.menuList(userId);
		if (menuList != null && menuList.size() != 0) {
			for (Menu menu : menuList) {
				init(menu);
			}
		}
		return menuList;
	}

}
