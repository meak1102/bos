package com.zhp.bos.web.auth.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.auth.Function;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class FunctionAction extends BaseAction<Function> {
	/**
	 * 功能权限添加
	 * 
	 * @return
	 */
	@Action(value = "functionAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_admin_function") })
	public String save() {
		facadeService.getFunctionService().save(model);
		return "save";

	}

	/**
	 * 功能权限添加
	 * 
	 * @return
	 */
	@Action(value = "functionAction_delete", results = { @Result(name = "delete", type = "fastJson") })
	public String delete() {
		String ids = (String) getParameter("ids");
		facadeService.getFunctionService().delete(ids);
		return "delete";

	}

	/**
	 * 添加角色市添加功能值功能查询
	 * 
	 * @return
	 */
	@Action(value = "functionAction_ajaxList", results = {
			@Result(name = "ajaxList", type = "fastJson", params = { "includesProperties", "id,name" }) })
	public String ajaxList() {
		List<Function> list = facadeService.getFunctionService().ajaxList();
		push(list);
		return "ajaxList";
	}

	// 无条件分页查询
	@Action(value = "functionAction_pageQuery", results = {
			@Result(name = "pageQuery", type = "fastJson", params = { "root", "pageData", "excludesProperties", "roles" }) })
	public String pageQuery() {
		Page<Function> pageData = facadeService.getFunctionService().pageQuery(getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}

	// 无条件分页查询
	@Action(value = "functionAction_findFunctionByRoleId", results = {
			@Result(name = "findFunctionByRoleId", type = "fastJson", params = { "includesProperties", "id" }) })
	public String findFunctionByRoleId() {
		String roleId = (String) getParameter("roleId");
		List<Function> list = facadeService.getFunctionService().findFunctionByRoleId(roleId);
		push(list);
		return "findFunctionByRoleId";
	}

}
