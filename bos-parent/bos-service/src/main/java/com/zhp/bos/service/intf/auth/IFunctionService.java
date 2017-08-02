package com.zhp.bos.service.intf.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.zhp.bos.entity.auth.Function;

public interface IFunctionService {
	/**
	 * 功能权限保存
	 * 
	 * @param model
	 */
	void save(Function model);

	/**
	 * 无条件分页查询
	 * 
	 * @param pageRequest
	 * @return
	 */
	Page<Function> pageQuery(PageRequest pageRequest);

	/**
	 * 查询所有权限
	 * 
	 * @return
	 */
	List<Function> ajaxList();

	/**
	 * 功能权限删除
	 * 
	 * @param ids
	 */
	void delete(String ids);

	/**
	 * 通过角色id查询所有的功能权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<Function> findFunctionByRoleId(String roleId);

	List<Function> findAll();

}
