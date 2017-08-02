package com.zhp.bos.web.bc.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.bc.Standard;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class StandardAction extends BaseAction<Standard> {
	// 标准添加或修改
	@Action(value = "standardAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_base_standard") })
	public String save() {
		User user = (User) getSessionAttibute("user");
		model.setOperator(user.getEmail());
		System.out.println(model.getOperationtime());
		model.setOperationtime(new Date());
		model.setOperatorcompany(user.getStation());
		facadeService.getStandardService().save(model);
		return "save";

	}

	// 无条件分页查询
	@Action(value = "standardAction_pageQuery")
	public String pageQuery() {
		Page<Standard> pages = facadeService.getStandardService().pageQuery(getPageRequest());
		setPageData(pages);
		return "pageQuery";

	}

	// 标准作废
	@Action(value = "standardAction_deltag", results = { @Result(name = "deltag", type = "json") })
	public String deltag() {
		String idString = (String) getParameter("ids");

		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");

				facadeService.getStandardService().deltag(idsarr);
				push(true);

			} else {
				push(false);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "deltag";
	}

	// 标准启用
	@Action(value = "standardAction_usedtag", results = { @Result(name = "usedtag", type = "json") })
	public String usedtag() {
		String idString = (String) getParameter("ids");

		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");

				facadeService.getStandardService().usedtag(idsarr);
				push(true);

			} else {
				push(false);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "usedtag";
	}

	/**
	 * 查询所有正在使用的标准
	 * 
	 * @return
	 */
	@Action(value = "standardAction_findAllInUse", results = {
			@Result(name = "findAllInUse", type = "fastJson", params = { "includesProperties", "name" }) })
	public String findAllInUse() {
		List<Standard> list = facadeService.getStandardService().findAllInUse();
		push(list);
		return "findAllInUse";
	}
}
