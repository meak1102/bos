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
var editIndex;
var flag=true;
function doEdit(){
	$('#tb').datagrid("endEdit",editIndex);
	flag=true;
}
function doCancel(){
	$('#tb').datagrid("cancelEdit",editIndex);
	flag=true;
}
//用来装配行内编辑的显示数据
var menuZindex=[{"zindex":"1"},{"zindex":"2"},
                {"zindex":"3"},{"zindex":"4"},{"zindex":"5"}];
function doDblClickRow(rowIndex, rowData){
	if(flag){	
		editIndex=rowIndex;
		$("#tb").datagrid("beginEdit",rowIndex);
		/* $.post("${pageContext.request.contextPath}/menuAction_zindexList",function(data){
			menuZindex=data;//后台抓取数据作为行内编辑的数据基础
		}); */
		flag=false;
	}
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
			onDblClickRow:doDblClickRow,
			onAfterEdit:function(rowIndex, rowData, changes){
				$.post("${pageContext.request.contextPath}/menuAction_zindexEdit",rowData,function(data){
					if(data){
						$.messager.alert("恭喜","恭喜您！文件数据更改成功","info");
						
					}else{
						$.messager.alert("警告","系统升级中，请稍后修改","warning");
					}
				});
			},
			toolbar : [
				{
					id : 'add',
					text : '添加菜单',
					iconCls : 'icon-add',
					handler : function(){
						location.href='${pageContext.request.contextPath}/page_admin_menu_add.action';
					}
				},{id : 'button-edit',	text : '行业修改保存',iconCls : 'icon-edit',handler:doEdit},
				{id : 'button-delete',text : '取消',iconCls : 'icon-cancel',handler : doCancel}           
			],
			url : '${pageContext.request.contextPath}/menuAction_pageQuery',
			columns : [[
			  {
				  field : 'id',
				  title : '菜单编号',
				  width : 120
			  },
			  {
				  field : 'name',
				  title : '菜单名称',
				  width : 120
			  },  
			  {
				  field : 'description',
				  title : '菜单描述',
				  width : 120
			  },  
			  {
				  field : 'generatemenu',
				  title : '是否生成菜单',
				  width : 120,
				  formatter:function(value,row,index){
					  if(value=="1"){
						  return "是";
					  }else{
						  return "否";
					  }
				  }
			  },  
			  {
				  field : 'menu.name',
				  title : '父菜单名称',
				  width : 120,
				  formatter:function(value,row,index){
					  if(row.menu==null){
						  return "没有父菜单";
					  }else{
						  return row.menu.name;
					  }
				  }
			  },  
			  {
				  field : 'zindex',
				  title : '优先级',
				  width : 120,
				  editor:{
						type:'combobox',
						options:{
							valueField:'zindex',//复选框的value值
							textField:'zindex',//选择框的显示值
							data:menuZindex,//数据来源
							required:true							
						}
				  }
			  },  
			  {
				  field : 'page',
				  title : '路径',
				  width : 200
			  }
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