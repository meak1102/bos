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
	// 工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查看',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '新增',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}];
	//定义冻结列，但单独书写
	var frozenColumns = [ [ {
		field : 'id',
		checkbox : true,
		rowspan : 2
	}, {
		field : 'email',
		title : '账号',
		width : 160,
		rowspan : 2
	} ] ];


	// 定义标题栏
	var columns = [ [ {
		field : 'gender',
		title : '性别',
		width : 60,
		rowspan : 2,
		align : 'center'
	}, {
		field : 'birthday',
		title : '生日',
		width : 120,
		rowspan : 2,//行合并
		align : 'center',
		formatter : function(data,row ,index){
				return new Date(row.birthday).toLocaleDateString();
		}
	}, {
		title : '其他信息',
		colspan : 2//列合并
	}, {
		field : 'telephone',
		title : '电话',
		width : 800,
		colspan : 2
	} ], [ {//合并列
		field : 'station',
		title : '单位',
		width : 80,
		align : 'center'
	}, {
		field : 'salary',
		title : '工资',
		width : 80,
		align : 'right'
	} ] ];
	$(function(){
		// 初始化 datagrid
		// 创建grid
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			toolbar : toolbar,
			pageList: [3,5,10],
			pagination : true,
			url : "${pageContext.request.contextPath}/userAction_pageQuery",
			idField : 'id', 
			frozenColumns : frozenColumns,
			columns : columns,
			onClickRow : onClickRow,
			onDblClickRow : doDblClickRow
		});
		
		$("body").css({visibility:"visible"});
		
	});
	var userRoles;
	// 双击
	function doDblClickRow(rowIndex, rowData) {
		$("#userWindow").window("open");
		$("#email").val(rowData.email);
		$("#id").val(rowData.id);
			//  权限列表回显
		  $("#grantRoles").empty();
	//  角色数据checkbox生成
		 $.post("${pageContext.request.contextPath}/roleAction_ajaxList",function(data){
			 $(data).each(function(){
				 //  List<Role>
				 $("#grantRoles").append("<input name='roleIds' type='checkbox' value='"+this.id+"'>"+this.name+"</input><br>");
			 });
			 //数据回显
				  $.post("${pageContext.request.contextPath}/roleAction_findRoleByUserId",{"userId":rowData.id},function(data){
					 var ro = $("input[name='roleIds']");
						$(data).each(function(){
							 //  List<Function>
							      for(var i=0;i<ro.length;i++){
							    	  if($(ro[i]).val()==this.id){
										    $(ro[i]).attr("checked","checked");
									   }
							      }
							 
						 });
				 }); 
		 });
		
	}
	// 单击
	function onClickRow(rowIndex){

	}
	
	function doAdd() {
		location.href="${pageContext.request.contextPath}/page_admin_userinfo.action";
	}

	function doView() {
		alert("编辑用户");
		var item = $('#grid').datagrid('getSelected');
		console.info(item);
		//window.location.href = "edit.html";
	}

	function doDelete() {
		alert("删除用户");
		var ids = [];
		var items = $('#grid').datagrid('getSelections');
		for(var i=0; i<items.length; i++){
		    ids.push(items[i].id);	    
		}
			
		console.info(ids.join(","));
		
		$('#grid').datagrid('reload');
		$('#grid').datagrid('uncheckAll');
	}
	$(function(){
		$("#save").click(function(){
			$("#updateUserForm").submit();
		});
	})
	
</script>		
</head>
<body class="easyui-layout" style="visibility:hidden;">
    <div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="用户信息修改"   modal= "true"  closed="true"
	id="userWindow" collapsible="false" minimizable="false" maximizable="false" style="width:500px;height:500px;top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >更新</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="updateUserForm" method="post" 
			action="${pageContext.request.contextPath }/userAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">修改用户信息</td>
					</tr>
					<tr>
						<td>用户名称</td>
						<td>
						<!-- 修改操作 添加一个隐藏域  -->
						<input type="hidden" name="id" id="id"/>
						<input type="text" name="email" id="email" class="easyui-validatebox" 
						data-options="required:true"/></td>
					</tr>
					<tr>
						<td>用户角色</td>
					<td colspan="3" id="grantRoles">
						
					</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>