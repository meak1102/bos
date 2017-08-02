package com.zhp.bos.service.intf.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.zhp.bos.entity.auth.Role;

public interface IRoleService {

	void save(Role model, String[] functionIds, String menuIds);

	Page<Role> pageQuery(PageRequest pageRequest);

	List<Role> ajaxList();

	List<Role> findAll();

	List<Role> findRoleByUserId(Integer id);

}
