package com.zhp.bos.web.base.interceptor;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@Component("loginInterceptor")
public class LoginMethodInterceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		Object user = ServletActionContext.getRequest().getSession().getAttribute("user");
		if (user == null) {
			ActionSupport action = (ActionSupport) invocation.getAction();
			action.addFieldError("error", "您未登录，请登录使用");
			return "noLogin";
		} else {
			return invocation.invoke();
		}
	}

}
