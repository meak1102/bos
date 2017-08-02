<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
function doSearch(){
	
	
}
function doEdit(){
	
}
function doDelete(){
	 var arr=$("#tb").datagrid("getSelections");
	 if(arr==null||arr.length==0){
			$.messager.alert("警告！","请至少选择一行","warning");
			return;
		}
		var arrIds=new Array();
		for(var i=0;i<arr.length;i++){
			arrIds.push(arr[i].id);
		}
		var ids=arrIds.join(",");
		$.post("${pageContext.request.contextPath}/functionAction_delete",{"ids":ids},function(data){
			if(data){
				$.messager.alert("信息","恭喜您，数据删除成功","info");
				$("#tb").datagrid("reload");//数据重新加载，但选中的数据无法清除
				$("#tb").datagrid("clearChecked");//清除之前选中的行
			}else{
				$.messager.alert("警告！","数据删除失败，系统维护中。。。","warning");
			}
		});
}

	$(function(){
		$("#tb").datagrid({
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
			toolbar : [{id : 'button-search',	text : '查询',iconCls : 'icon-search',handler:doSearch},
				{id : 'add',text : '添加权限',iconCls : 'icon-add',
					handler : function(){
						location.href='${pageContext.request.contextPath}/page_admin_function_add.action';
					}
				}, 
				{id : 'button-edit',	text : '修改',iconCls : 'icon-edit',handler:doEdit},
				{id : 'button-delete',text : '删除',iconCls : 'icon-cancel',handler:doDelete}			],
			url : '${pageContext.request.contextPath}/functionAction_pageQuery',
			columns : [[
			  {
				  field : 'id',
				  checkbox:true,
				  title : '编号',
				  width : 200
			  },
			  {
				  field : 'name',
				  title : '名称',
				  width : 200
			  },  
			  {
				  field : 'code',
				  title : '权限关键字',
				  width : 200
			  }, 
			  {
				  field : 'description',
				  title : '描述',
				  width : 200
			  },  
			]]
		});
	});
</script>	
</head>
<body class="easyui-layout">
<div data-options="region:'center'">
	<table id="tb"></table>
</div>
</body>
</html>