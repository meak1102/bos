<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript">
$(function(){
	$('#tb').datagrid({ 

		url:'${pageContext.request.contextPath}/json/data.json', 
		pageList:[3,6,9],
		pagination:true,singleSelect:true,collapsible:true,
		width:600,height:300,
		columns:[[ 

		{field:'code',title:'Code',width:200,align:'center'}, 

		{field:'name',title:'Name',width:200,align:'center'}, 

		{field:'price',title:'Price',width:100,align:'right'} 

		]] 

		});

	
})
</script>
</head>
<body>
<table id="tb" title="shuju">
</table>
</body>
</html>