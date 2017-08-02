package com.zhp.bos.web.base.result;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class FastJonResult implements Result {
	// 以下属性在struts运行时会自动通过set方法注入
	private String root;// 判断数据是否配有root属性，如果存在就去值栈中查找，如果不存在就拿栈顶对象
	private Set<String> includesProperties;// 需要序列化的属性集合
	private Set<String> excludesProperties;// 不需要序列化的属性集合

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public Set<String> getIncludesProperties() {
		return includesProperties;
	}

	public void setIncludesProperties(String includesProperties) {
		// 该TextParseUtil 由struts2 框架提供 用于切割 逗号分隔的字符串 该方法来源 struts2 自带的拦截器源码
		// MethodFilterIntercepter
		this.includesProperties = TextParseUtil.commaDelimitedStringToSet(includesProperties);
	}

	public Set<String> getExcludesProperties() {
		return excludesProperties;
	}

	public void setExcludesProperties(String excludesProperties) {
		this.excludesProperties = TextParseUtil.commaDelimitedStringToSet(excludesProperties);
	}

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		// 获取响应对象，并设置响应编
		ActionContext actionContext = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) actionContext.get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("test/json;charset=utf-8");
		// 查找序列化对象
		Object rootObject = findRootObject(invocation);
		// 阿里过滤器 配置哪些属性需要梳序列化 哪些属性 不需要序列化
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		if (includesProperties != null && includesProperties.size() != 0) {
			filter.getIncludes().addAll(includesProperties);// 将需要的属性添加即可
		}
		if (excludesProperties != null && excludesProperties.size() != 0) {
			filter.getExcludes().addAll(excludesProperties);// 不需要序列化json字符串的集合添加
		}
		// SerializerFeature.DisableCircularReferenceDetect
		// 数据重复引用重复序列化，关闭循环检测，但关闭后就会出现关联的对象都回被查询出来，造成连锁查询，最后死循环
		// 解决办法：1.实体类排除，2.方法上参数注入排除
		String jsonString = JSON.toJSONString(rootObject, filter, SerializerFeature.DisableCircularReferenceDetect);// 阿里提供的序列化json字符串
		response.getWriter().println(jsonString);

	}

	// 搜索值栈获取 序列化java对象
	protected Object findRootObject(ActionInvocation invocation) {
		Object rootObject;
		if (this.root != null) {
			ValueStack stack = invocation.getStack();
			rootObject = stack.findValue(root);
		} else {
			rootObject = invocation.getStack().peek(); // model overrides action
		}
		return rootObject;
	}

}
