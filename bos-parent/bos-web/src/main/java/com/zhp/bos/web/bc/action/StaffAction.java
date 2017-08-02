package com.zhp.bos.web.bc.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.bc.Staff;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class StaffAction extends BaseAction<Staff> {
	/**
	 * 取派员添加合修改
	 * 
	 * @return
	 */
	@Action(value = "staffAction_save", results = {
			@Result(name = "save", type = "redirectAction", location = "page_base_staff") })
	public String save() {

		System.out.println(model.getDeltag());
		facadeService.getStaffService().save(model);
		return "save";

	}

	/**
	 * 查询所有在职的取派员
	 * 
	 * @return
	 */
	@Action(value = "staffAction_nameListInUse", results = {
			@Result(name = "nameListInUse", type = "fastJson", params = { "includesProperties", "id,name" }) })
	public String nameListInUse() {
		List<Staff> list = facadeService.getStaffService().nameListInUse();
		push(list);
		return "nameListInUse";

	}

	/**
	 * 条件分页查询
	 * 
	 * @return
	 */
	@Action(value = "staffAction_pageQuery")
	public String pageQuery() {
		System.out.println(model.getHaspda());
		Specification<Staff> spec = new Specification<Staff>() {
			public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(model.getName())) {
					list.add(cb.like(root.get("name").as(String.class), "%" + model.getName() + "%"));
				}
				if (StringUtils.isNotBlank(model.getStandard())) {
					list.add(cb.equal(root.get("standard").as(String.class), model.getStandard()));
				}
				if (StringUtils.isNotBlank(model.getTelephone())) {
					list.add(cb.like(root.get("telephone").as(String.class), "%" + model.getTelephone() + "%"));
				}
				if (StringUtils.isNotBlank(model.getStation())) {
					list.add(cb.like(root.get("station").as(String.class), "%" + model.getStation() + "%"));
				}
				if (model.getHaspda() != null) {
					System.out.println(11111111);
					list.add(cb.equal(root.get("haspda").as(Integer.class), model.getHaspda()));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Page<Staff> pageData = facadeService.getStaffService().pageQuery(spec, getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}

	// 取派员变为在职
	@Action(value = "staffAction_usedtag", results = { @Result(name = "usedtag", type = "json") })
	public String usedtag() {
		String idString = (String) getParameter("ids");

		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");

				facadeService.getStaffService().usedtag(idsarr);
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

	// 取派员变为离职
	@Action(value = "staffAction_deltag", results = { @Result(name = "deltag", type = "json") })
	public String deltag() {
		String idString = (String) getParameter("ids");

		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");

				facadeService.getStaffService().deltag(idsarr);
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

	// 电话号码校验
	@Action(value = "staffAction_checkTelephone", results = { @Result(name = "checkTelephone", type = "json") })
	public String checkTelephone() {
		Staff staff = facadeService.getStaffService().checkTelephone(model.getTelephone());
		if (staff == null) {
			push(false);
		} else {
			push(staff.getId());
		}
		return "checkTelephone";
	}
}
