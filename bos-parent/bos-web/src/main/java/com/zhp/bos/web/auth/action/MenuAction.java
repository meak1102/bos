package com.zhp.bos.web.auth.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.auth.Menu;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class MenuAction extends BaseAction<Menu> {
	/**
	 * 功能权限添加
	 * 
	 * @return
	 */
	@Action(value = "menuAction_ajaxListHasSonMenus", results = {
			@Result(name = "ajaxListHasSonMenus", type = "fastJson", params = { "includesProperties", "id,name" }) })
	public String ajaxListHasSonMenus() {
		List<Menu> list = facadeService.getMenuService().ajaxListHasSonMenus();
		push(list);
		return "ajaxListHasSonMenus";

	}

	/*
	 * 功能权限添加
	 *
	 * @return
	 */
	@Action(value = "menuAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_admin_menu") })
	public String save() {
		if (StringUtils.isBlank(model.getMenu().getId())) {
			model.setMenu(null);
		}
		facadeService.getMenuService().save(model);
		return "save";

	}

	// 无条件分页查询
	@Action(value = "menuAction_pageQuery", results = {
			@Result(name = "pageQuery", type = "fastJson", params = { "root", "pageData", "excludesProperties", "menus,roles" }) })
	public String pageQuery() {
		String page = (String) getParameter("page");
		setPage(Integer.parseInt(page));// 与model类的page重名了，model对象在栈顶
		Page<Menu> pageData = facadeService.getMenuService().pageQuery(getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}

	// 无条件分页查询
	@Action(value = "menuAction_ajaxList", results = {
			@Result(name = "ajaxList", type = "fastJson", params = { "excludesProperties", "menus,roles" }) })
	public String ajaxList() {
		List<Menu> list = facadeService.getMenuService().ajaxList();
		push(list);
		return "ajaxList";
	}

	// 无条件分页查询
	@Action(value = "menuAction_findMenuByRoleId", results = {
			@Result(name = "findMenuByRoleId", type = "fastJson", params = { "includesProperties", "id" }) })
	public String findMenuByRoleId() {
		String roleId = (String) getParameter("roleId");
		List<Menu> list = facadeService.getMenuService().findMenuByRoleId(roleId);
		push(list);
		return "findMenuByRoleId";
	}

	// 无条件分页查询
	@Action(value = "menuAction_menuList", results = {
			@Result(name = "menuList", type = "fastJson", params = { "includesProperties", "id,pId,name,page" }) })
	public String menuList() {
		Subject subject = SecurityUtils.getSubject();
		User exitUser = (User) subject.getPrincipal();
		List<Menu> list = facadeService.getMenuService().menuList(exitUser.getId());
		push(list);
		return "menuList";
	}

}
