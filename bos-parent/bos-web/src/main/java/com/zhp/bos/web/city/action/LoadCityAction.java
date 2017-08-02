package com.zhp.bos.web.city.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.city.City;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class LoadCityAction extends BaseAction<City> {
	@Action(value = "loadCityAction_load")

	public String load() {
		String jsonString = facadeService.getLoadCityService().load(model);
		HttpServletResponse response = getResponse();
		response.setContentType("text/json;charset=utf-8");
		try {
			response.getWriter().println(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;

	}

}
