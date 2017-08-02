package com.zhp.bos.web.qp.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.customer.Customers;
import com.zhp.bos.entity.qp.NoticeBill;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class NoticeBillAction extends BaseAction<NoticeBill> {
	@Action(value = "noticeBillAction_findCustomerByTelephone", results = {
			@Result(name = "findCustomerByTelephone", type = "fastJson") })
	public String findCustomerByTelephone() {
		Customers customers = facadeService.getNoticeBillService().findCustomerByTelephone(model);
		push(customers);
		return "findCustomerByTelephone";

	}

	@Action(value = "noticeBillAction_save", results = {
			@Result(name = "save", type = "redirectAction", location = "page_qupai_noticebill_add") })
	public String save() {
		String province = (String) getParameter("province");
		String city = (String) getParameter("city");
		String district = (String) getParameter("district");
		// model.setPickaddress(province + city + district +
		// model.getPickaddress());
		// 因为阿里大于发短信有字数限制
		User user = (User) getSessionAttibute("user");
		model.setUser(user);
		facadeService.getNoticeBillService().save(model, province, city, district);
		return "save";

	}
}
