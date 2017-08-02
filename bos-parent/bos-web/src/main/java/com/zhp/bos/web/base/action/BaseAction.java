package com.zhp.bos.web.base.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.zhp.bos.service.base.FacadeService;
import com.zhp.bos.utils.DownLoadUtils;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	protected T model;

	// 封装model对象
	@Override
	public T getModel() {
		return model;
	}

	@Autowired
	protected FacadeService facadeService;

	@SuppressWarnings("unchecked")
	public BaseAction() {
		// 在子类初始化的时候，默认会调用父类的构造器
		// 反射机制:获取具体的类型
		// 得到带有泛型的类型，如BaseAction<Userinfo>
		// Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
		// Type的实现类Class
		Type superclass = this.getClass().getGenericSuperclass();
		// 转换为参数化类型,
		ParameterizedType parameterizedType = (java.lang.reflect.ParameterizedType) superclass;
		// 获取泛型的第一个参数的类型类，如Userinfo
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		// 实例化数据模型类型
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 数据压入root栈顶
	public void push(Object obj) {
		ActionContext.getContext().getValueStack().push(obj);
	}

	// 数据以map形式压入root栈顶
	public void set(String key, Object obj) {
		ActionContext.getContext().getValueStack().set(key, obj);
	}

	// 数据压入map栈
	public void put(String key, Object obj) {
		ActionContext.getContext().put(key, obj);
	}

	// 获取servlet相关的对象
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public Object getParameter(String name) {
		return getRequest().getParameter(name);
	}

	public Object getSessionAttibute(String name) {
		return getRequest().getSession().getAttribute(name);
	}

	public void removeSessionAttibute(String name) {
		getRequest().getSession().removeAttribute(name);
	}

	public void setSessionAttibute(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}

	// 文件下载
	public void download(String filename, String path) throws Exception {
		String contentType = ServletActionContext.getServletContext().getMimeType(filename);
		String agent = getRequest().getHeader("user-agent");
		filename = DownLoadUtils.getAttachmentFileName(filename, agent);
		String contentDisposition = "attachment;filename=" + filename;
		InputStream inputStream = new FileInputStream(path);
		put("contentType", contentType);
		put("contentDisposition", contentDisposition);
		put("inputStream", inputStream);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	protected int page;// 分页 ---当前页
	protected int rows;// 当前页最大显示数
	// 分页时封装页码数据

	public PageRequest getPageRequest() {
		return new PageRequest(page - 1, rows);
	}

	// 分页查询返回的数据
	Page<T> pageData;

	// 数据由子类传递给父类
	public void setPageData(Page pageData) {
		this.pageData = pageData;
	}

	// 分页查询返回的数据封装并放入值栈
	public Object getPageData() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		return map;
	}

}
