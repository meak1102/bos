package com.zhp.bos.web.user.action;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.zhp.bos.entity.user.User;
import com.zhp.bos.utils.RandStringUtils;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class UserAction extends BaseAction<User> {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private JmsTemplate JmsTemplate;

	/**
	 * struts2的web层用户登录方法
	 * 
	 * @return
	 */
	// @Action(value = "userAction_login", results = { @Result(name =
	// "loginError", location = "/login.jsp"),
	// @Result(name = "loginSuccess", type = "redirect", location =
	// "/index.jsp"),
	// @Result(name = "login_param_error", location = "/login.jsp") })
	// @InputConfig(resultName = "login_param_error")
	// public String login() {
	// User existUser =
	// facadeService.getUserService().findUserByEmailAndPassword(model);
	// removeSessionAttibute("key");
	// if (existUser == null) {
	// addActionError(getText("loginEmailorPasswordError"));
	// return "loginError";
	// } else {
	// setSessionAttibute("user", existUser);
	// return "loginSuccess";
	// }
	// }
	// shiro框架管理认证
	@Action(value = "userAction_login", results = { @Result(name = "loginError", location = "/login.jsp"),
			@Result(name = "loginSuccess", type = "redirect", location = "/index.jsp"),
			@Result(name = "login_param_error", location = "/login.jsp") })
	@InputConfig(resultName = "login_param_error") // 校验器失败返回页面
	public String login() {
		try {
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(model.getEmail(), model.getPassword());
			subject.login(token);
			return "loginSuccess";
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			addActionError(getText("loginEmailError"));
			return "loginError";
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			addActionError(getText("loginPasswordError"));
			return "loginError";
		}
	}

	// 登录界面验证码ajax进行验证
	@Action(value = "userAction_checkCode", results = { @Result(name = "checkCode", type = "json") })
	public String checkCode() {
		String checkCode = (String) getParameter("checkCode");
		String sessionCode = (String) getSessionAttibute("key");
		if (checkCode.equalsIgnoreCase(sessionCode)) {
			push(true);
		} else {
			push(false);
		}
		return "checkCode";

	}

	/**
	 * web层退出登录方法
	 * 
	 * @return
	 */
	@Action(value = "userAction_quit", results = { @Result(name = "quit", type = "redirect", location = "/login.jsp") })
	public String quit() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "quit";

	}

	/**
	 * 找回密码界面-- 获取获取验证码，并把数据存放再redis中，并把信息交给mq发送短信给用户
	 * 
	 * @return
	 */
	@Action(value = "userAction_sendSms", results = { @Result(name = "sendSms", type = "json") })
	public String sendSms() {
		final String telephone = model.getTelephone();
		final String code = RandStringUtils.getCode();
		try {
			redisTemplate.opsForValue().set(telephone, code, 120, TimeUnit.SECONDS);
			JmsTemplate.send("bos", new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					MapMessage message = session.createMapMessage();
					message.setString("telephone", telephone);
					message.setString("code", code);
					return message;
				}
			});
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "sendSms";
	}

	/**
	 * 找回密码界面：验证码验证
	 * 
	 * @return
	 */
	@Action(value = "userAction_checkSmsCode", results = { @Result(name = "checkSmsCode", type = "json") })
	public String checkSmsCode() {
		String code = (String) getParameter("code");
		System.out.println(code);
		String telephone = (String) getParameter("telephone");
		User user = facadeService.getUserService().findUserByTelephone(telephone);
		if (user == null) {// 电话号码错误
			push("telephoneError");
		} else {
			String redisCode = redisTemplate.opsForValue().get(telephone);
			System.out.println(redisCode);
			if (redisCode == null || redisCode == "") {
				// 验证码失效
				push("codeTimeOut");
			} else if (!(redisCode.equals(code))) {// 验证码错误
				push("codeError");
			} else {// 验证码正确
				push("success");
			}
		}
		return "checkSmsCode";
	}

	/**
	 * 密码找回中修改密码
	 * 
	 * @return
	 */
	@Action(value = "userAction_updatePassword", results = { @Result(name = "updatePasswordSuccess", type = "redirect", location = "/login.jsp"),
			@Result(name = "updatePasswordError", location = "/smspassword.jsp") })
	public String updatePassword() {
		try {
			facadeService.getUserService().updatePassword(model);
			return "updatePasswordSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("系统升级中，请稍后访问");
			return "updatePasswordError";
		}
	}

	/**
	 * 用户登录之后修改密码
	 * 
	 * @return
	 */
	@Action(value = "userAction_updatePassword2", results = { @Result(name = "updatePasswordSuccess", type = "redirect", location = "/login.jsp"),
			@Result(name = "updatePasswordError", location = "/index.jsp") })
	public String updatePassword2() {
		try {
			User user = (User) getSessionAttibute("user");
			model.setTelephone(user.getTelephone());
			facadeService.getUserService().updatePassword(model);
			return "updatePasswordSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			addActionError("系统升级中，请稍后访问");
			return "updatePasswordError";
		}
	}

	@Action(value = "userAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_admin_userlist") })
	public String save() {
		String[] roleIds = getRequest().getParameterValues("roleIds");
		facadeService.getUserService().save(model, roleIds);
		return "save";
	}

	// 无条件分页查询
	@Action(value = "userAction_pageQuery", results = {
			@Result(name = "pageQuery", type = "fastJson", params = { "root", "pageData", "excludesProperties", "noticeBills,roles" }) })
	public String pageQuery() {
		Page<User> pageData = facadeService.getUserService().pageQuery(getPageRequest());
		setPageData(pageData);
		return "pageQuery";
	}
}
