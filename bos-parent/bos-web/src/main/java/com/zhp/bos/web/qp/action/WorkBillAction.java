package com.zhp.bos.web.qp.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.zhp.bos.entity.bc.Staff;
import com.zhp.bos.entity.qp.NoticeBill;
import com.zhp.bos.entity.qp.WorkBill;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class WorkBillAction extends BaseAction<WorkBill> {
	// 追单

	@Action(value = "noticeAction_repeat", results = { @Result(name = "repeat", type = "fastJson") })
	public String repeat() {
		try {
			facadeService.getWorkBillService().repeat(model.getId());
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}

		return "repeat";
	}
	// 销单

	@Action(value = "noticeAction_cancel", results = { @Result(name = "cancel", type = "fastJson") })
	public String cancel() {
		try {
			String ids = (String) getParameter("ids");
			facadeService.getWorkBillService().cancel(ids);
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}

		return "cancel";
	}
	// 条件分页查询

	@Action(value = "WorkBillAction_pageQuery")
	public String pageQuery() {
		Page<WorkBill> pages = facadeService.getWorkBillService().pageQuery(getSpecification(), getPageRequest());
		setPageData(pages);
		return "pageQuery";
	}

	// 查询条件封装
	public Specification<WorkBill> getSpecification() {
		return new Specification<WorkBill>() {
			@Override
			public Predicate toPredicate(Root<WorkBill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (model.getBuildtime() != null) {
					Date buildtime = model.getBuildtime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(buildtime);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					System.out.println(buildtime);
					System.out.println(calendar.getTime());
					list.add(cb.between(root.get("buildtime").as(Date.class), buildtime, calendar.getTime()));
					// list.add(cb.equal(root.get("buildtime").as(Date.class),
					// model.getBuildtime()));
				}
				Join<WorkBill, Staff> join = root.join(root.getModel().getSingularAttribute("staff", Staff.class),
						JoinType.LEFT);
				if (model.getStaff() != null && StringUtils.isNotBlank(model.getStaff().getName())) {
					list.add(cb.equal(join.get("name").as(String.class), model.getStaff().getName()));
				}
				Join<WorkBill, NoticeBill> join2 = root
						.join(root.getModel().getSingularAttribute("noticeBill", NoticeBill.class), JoinType.LEFT);
				if (model.getNoticeBill() != null && StringUtils.isNotBlank(model.getNoticeBill().getTelephone())) {
					list.add(cb.equal(join2.get("telephone").as(String.class), model.getNoticeBill().getTelephone()));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
	}
}
