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
<!-- 导入ztree类库 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css"
	type="text/css" />
<script
	src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"
	type="text/javascript"></script>	
<script type="text/javascript">
		
	    var setting = {
	        check: {
	          enable: true
	        },
	        data: {
	          simpleData: {
	            enable: true
	          }
	        }
	      };
	    function doEdit(){
	    	
	    }
	    function doDelete(){
	    	
	    }
	    var checknodes;// 接受 指定角色对应菜单对象的
	$(function(){		
		// 数据表格属性
		$("#grid").datagrid({
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
			onDblClickRow:function(rowIndex,rowData){
				$("#name").val(rowData.name);
				$("#id").val(rowData.id);
				$("#code").val(rowData.code);
				$("#description").val(rowData.description);
				//  权限列表回显
				  $("#functionIds").empty();
					$.ajax({
					url : '${pageContext.request.contextPath}/functionAction_ajaxList',
					type : 'POST',
					dataType : 'json',
					success : function(data) {
							 $(data).each(function(){
								 $("#functionIds").append("<input name='functionIds' type='checkbox' value='"+this.id+"'>"+this.name+"</input>&nbsp;&nbsp;");
							 		
							 });
							 
								$.ajax({
									url : '${pageContext.request.contextPath}/functionAction_findFunctionByRoleId',
									type : 'POST',
									data:{"roleId":rowData.id},
									dataType : 'json',
									success : function(data) {
										     var pa = $("input[name='functionIds']");
											$(data).each(function(){
												 //  List<Function>
												      for(var i=0;i<pa.length;i++){
												    	  if($(pa[i]).val()==this.id){
															    $(pa[i]).attr("checked","checked");
														   }
												      }
												 
											 });
											 
										}
									});
							 
						}
					});
				
				
				
				//  双击事件 完成 角色修改回显操作....菜单树制作
				$.ajax({
					url : '${pageContext.request.contextPath}/menuAction_ajaxList',
					type : 'POST',
					dataType : 'text',
					success : function(data) {
						var zNodes = eval("(" + data + ")");
						var treeObj = $.fn.zTree.init($("#functionTree"), setting, zNodes);
						treeObj.expandAll(true);//展开所有节点
						var array = treeObj.transformToArray(treeObj.getNodes());//  所有树节点对象 转换对应 数组  
						loadNodes(rowData.id);//  ajax  根据角色id 查询所有菜单
						// checknodes
						// var treeObj = $.fn.zTree.getZTreeObj("functionTree");
							for (var i=0, l=array.length; i < l; i++) {
									for (var j=0, m=checknodes.length; j <m; j++) {
								      if(array[i].id ==checknodes[j].id){
								        treeObj.checkNode(array[i], true, false);//  勾选 菜单节点
								      }
									}
							}
						
					},
					error : function(msg) {
						alert('树加载异常!');
					}
				});
				
				       $('#roleWindow').window("open");
			},
			toolbar : [
				{
					id : 'add',
					text : '添加角色',
					iconCls : 'icon-add',
					handler : function(){
						location.href='${pageContext.request.contextPath}/page_admin_role_add.action';
					}
				} ,
				{id : 'button-edit',	text : '修改',iconCls : 'icon-edit',handler:doEdit},
				{id : 'button-delete',text : '删除',iconCls : 'icon-cancel',handler:doDelete}
			],
			url : '${pageContext.request.contextPath}/roleAction_pageQuery',
			columns : [[
				{
					field : 'id',
					title : '编号',
					width : 300
				},
				{
					field : 'name',
					title : '名称',
					width : 200
				}, 
				{
					field : 'code',
					title : '角色关键字',
					width : 200
				}, 
				{
					field : 'description',
					title : '描述',
					width : 200
				} 
			]]
		});
		
		// 收派标准窗口
		$('#roleWindow').window({
	        title: '修改角色',
	        width: 500,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 300,
	        resizable:false
	    });
		$("#save").click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("functionTree");
			var nodes = treeObj.getCheckedNodes(true);
			  if(nodes!=null&&nodes.length!=0){
            	  var arr = new Array();
            	   for(var i=0;i<nodes.length;i++){
            		     arr.push(nodes[i].id);
            	   }
            	   //  隐藏域设值
            	   $("#menuIds").val(arr.join(","));
            }
			$("#saveRoleForm").submit();
		});
	});
	 function loadNodes(roleId){
         $.ajax({
           type:"post",
           url:"${pageContext.request.contextPath}/menuAction_findMenuByRoleId",
           data:{"roleId":roleId},
           async:false,
           dataType:"json",
           success:function(data){
        	   checknodes=data;
           }
         });  
   }
</script>	
</head>
<body class="easyui-layout">
	<div data-options="region:'center'">
		<table id="grid"></table>
	</div>
	<div class="easyui-window" title="收派标准进行添加或者修改" id="roleWindow" collapsible="false" minimizable="false" maximizable="false" style="width:500px;height:500px;top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >更新</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="saveRoleForm" method="post" 
			action="${pageContext.request.contextPath }/roleAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">修改角色信息</td>
					</tr>
					<tr>
						<td>角色名称</td>
						<td>
						<!-- 修改操作 添加一个隐藏域  -->
						<input type="hidden" name="menuIds" id="menuIds"/>
						<input type="hidden" name="id" id="id"/>
						<input type="hidden" name="code" id="code"/>
						<input type="hidden" name="description" id="description"/>
						<input type="text" name="name" id="name" class="easyui-validatebox" 
						data-options="required:true"/></td>
					</tr>
					<tr>
						<td>权限列表</td>
						<td id="functionIds">
						 </td>
					</tr>
					<tr>
						<td>菜单树</td>
						<td>
							<ul id="functionTree" class="ztree"></ul>
						</td>
					</tr>
			
					</table>
			</form>
		</div>
	</div>
</body>
</html>