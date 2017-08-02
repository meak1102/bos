package com.zhp.bos.web.auth.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.auth.Role;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class RoleAction extends BaseAction<Role> {
	/**
	 * 功能权限添加
	 * 
	 * @return
	 */
	@Action(value = "roleAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_admin_role") })
	public String save() {
		String[] functionIds = getRequest().getParameterValues("functionIds");
		String menuIds = (String) getParameter("menuIds");
		facadeService.getRoleService().save(model, functionIds, menuIds);
		return "save";

	}

	// 无条件分页查询
	@Action(value = "roleAction_pageQuery", results = { // "includesProperties",
														// "id,name,code,description"
			@Result(name = "pageQuery", type = "fastJson", params = { "root", "pageData", "excludesProperties", "menus,users,functions" }) })
	// @Action(value = "roleAction_pageQuery")
	public String pageQuery() {
		Page<Role> pageData = facadeService.getRoleService().pageQuery(getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}

	// 无条件分页查询
	@Action(value = "roleAction_ajaxList", results = { // "includesProperties",
			// "id,name,code,description"
			@Result(name = "ajaxList", type = "fastJson", params = { "includesProperties", "id,name" }) })
	// @Action(value = "roleAction_pageQuery")
	public String ajaxList() {
		List<Role> list = facadeService.getRoleService().ajaxList();
		push(list);
		return "ajaxList";
	}

	// 无条件分页查询
	@Action(value = "roleAction_findRoleByUserId", results = { // "includesProperties",
			// "id,name,code,description"
			@Result(name = "findRoleByUserId", type = "fastJson", params = { "includesProperties", "id" }) })
	// @Action(value = "roleAction_pageQuery")
	public String findRoleByUserId() {
		String userId = (String) getParameter("userId");
		List<Role> list = facadeService.getRoleService().findRoleByUserId(Integer.parseInt(userId));
		push(list);
		return "findRoleByUserId";
	}

}
