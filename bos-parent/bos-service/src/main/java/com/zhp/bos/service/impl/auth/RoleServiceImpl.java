package com.zhp.bos.service.impl.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.auth.FunctionDao;
import com.zhp.bos.dao.auth.MenuDao;
import com.zhp.bos.dao.auth.RoleDao;
import com.zhp.bos.entity.auth.Function;
import com.zhp.bos.entity.auth.Menu;
import com.zhp.bos.entity.auth.Role;
import com.zhp.bos.service.intf.auth.IRoleService;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private FunctionDao functionDao;
	@Autowired
	private MenuDao menuDao;

	@Override
	public void save(Role model, String[] functionIds, String menuIds) {
		// 角色修改或添加
		if (model.getId() != null) {
			// 修改
			Role role = roleDao.findOne(model.getId());
			role.setMenus(new HashSet<Menu>());
			role.setFunctions(new HashSet<Function>());
		}

		// 关联功能权限
		if (functionIds != null && functionIds.length != 0) {
			for (String functionId : functionIds) {

				// Function function = functionDao.findOne(functionId);
				Function function = new Function();// 没有配置级联才可以，否则会默认保存functon，而function是脱管态对象
				function.setId(functionId);
				model.getFunctions().add(function);
			}
		}
		// 关联菜单
		String[] arrMenuIds = menuIds.split(",");
		if (arrMenuIds != null && arrMenuIds.length != 0) {
			for (String menuId : arrMenuIds) {
				// Menu menu = menuDao.findOne(menuId);
				Menu menu = new Menu();
				menu.setId(menuId);
				model.getMenus().add(menu);
			}
		}
		roleDao.save(model);

	}

	@Override
	public Page<Role> pageQuery(PageRequest pageRequest) {
		return roleDao.findAll(pageRequest);
	}

	@Override
	public List<Role> ajaxList() {
		return roleDao.findAll();
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Override
	public List<Role> findRoleByUserId(Integer id) {
		List<Role> list = roleDao.findRoleByUserId(id);
		if (list != null && list.size() != 0) {
			for (Role role : list) {
				Set<Function> functions = role.getFunctions();
				if (functions != null && functions.size() != 0) {
					for (Function function : functions) {
						Hibernate.initialize(function);
					}
				}
			}
		}
		return list;
	}
}
