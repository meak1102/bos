package com.zhp.bos.web.bc.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.bc.Staff;
import com.zhp.bos.entity.customer.Customers;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class DecidedzoneAction extends BaseAction<Decidedzone> {
	@Action(value = "decidedzoneAction_save", results = {
			@Result(name = "save", type = "redirectAction", location = "page_base_decidedzone") })
	public String save() {
		String[] sids = getRequest().getParameterValues("sid");
		// 页面提交为input标签，没有数据时为空字符串，保存时会出现保存外键异常
		if (StringUtils.isBlank(model.getStaff().getId())) {
			model.setStaff(null);
		}
		facadeService.getDecidedzoneService().save(model, sids);
		return "save";

	}

	/**
	 * 远程查询指定定区关联的客户
	 * 
	 * @return
	 */
	@Action(value = "decidedzoneAction_getAssociationCustomersByDecided", results = {
			@Result(name = "getAssociationCustomersByDecided", type = "fastJson") })
	public String getAssociationCustomersByDecided() {
		Collection<? extends Customers> collection = facadeService.getDecidedzoneService()
				.getAssociationCustomersByDecided(model.getId());
		if (collection == null) {// 返回为null时页面数据表格异常，而从数据库查询不会为null，只会为空
			push(new ArrayList<>());
		} else {
			push(collection);
		}
		return "getAssociationCustomersByDecided";

	}

	/**
	 * 远程查询未被关联的客户
	 * 
	 * @return
	 */
	@Action(value = "decidedzoneAction_getNoAssociationCustomers", results = {
			@Result(name = "getNoAssociationCustomers", type = "fastJson") })
	public String getNoAssociationCustomers() {
		Collection<? extends Customers> collection = facadeService.getDecidedzoneService().getNoAssociationCustomers();
		if (collection == null) {// 返回为null时页面数据表格异常，而从数据库查询不会为null，只会为空
			push(new ArrayList<>());
		} else {
			push(collection);
		}
		return "getNoAssociationCustomers";

	}

	/**
	 * 远程查询未被关联的客户
	 * 
	 * @return
	 */
	@Action(value = "decidedzoneAction_assignedCustomerToDecidedZone", results = {
			@Result(name = "assignedCustomerToDecidedZone", type = "redirectAction", location = "page_base_decidedzone") })
	public String assignedCustomerToDecidedZone() {
		String[] cids = getRequest().getParameterValues("customerIds");
		facadeService.getDecidedzoneService().assignedCustomerToDecidedZone(model.getId(), cids);
		return "assignedCustomerToDecidedZone";

	}

	/**
	 * 检验定区id
	 * 
	 * @return
	 */
	@Action(value = "decidedzoneAction_checkdecidedzoneId", results = {
			@Result(name = "checkdecidedzoneId", type = "fastJson") })
	public String checkdecidedzoneId() {
		System.out.println(model.getId());
		Decidedzone decidedzone = facadeService.getDecidedzoneService().checkdecidedzoneId(model);
		if (decidedzone == null) {
			push(false);
		} else {
			push(true);
		}
		return "checkdecidedzoneId";
	}

	// 区域删除
	@Action(value = "decidedAction_delete", results = { @Result(name = "delete", type = "json") })

	public String delete() {
		String idString = (String) getParameter("ids");
		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");
				facadeService.getDecidedzoneService().delete(idsarr);
				push(true);
			} else {
				push(false);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "delete";
	}
	// 条件分页查询

	@Action(value = "decidedzoneAction_pageQuery")
	public String pageQuery() {
		Page<Decidedzone> pages = facadeService.getDecidedzoneService().pageQuery(getSpecification(), getPageRequest());
		setPageData(pages);
		return "pageQuery";
	}

	// 查询条件封装
	public Specification<Decidedzone> getSpecification() {
		return new Specification<Decidedzone>() {
			@Override
			public Predicate toPredicate(Root<Decidedzone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(model.getId())) {
					list.add(cb.like(root.get("id").as(String.class), "%" + model.getId() + "%"));
				}

				if (model.getStaff() != null && StringUtils.isNotBlank(model.getStaff().getStation())) {
					Join<Decidedzone, Staff> staffJoin = root
							.join(root.getModel().getSingularAttribute("staff", Staff.class), JoinType.LEFT);
					list.add(cb.like(staffJoin.get("station").as(String.class),
							"%" + model.getStaff().getStation() + "%"));
				}
				String isAssociationSubarea = (String) getParameter("isAssociationSubarea");

				if ("1".equals(isAssociationSubarea)) {
					list.add(cb.isNotEmpty(root.get("subareas").as(Set.class)));
				} else if ("0".equals(isAssociationSubarea)) {
					list.add(cb.isEmpty(root.get("subareas").as(Set.class)));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
	}
}
