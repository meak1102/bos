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
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : function(){
			$("#queryStaffWindow").window("open");
		}
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : function(){
			$("#addStaffWindow").window("open");
		}
	}, {
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : function(){
				var arr=  $("#tb").datagrid("getSelections");
	    		if(arr==""&&arr.length==0){
	    			$.messager.alert("警告！","请至少选择一条数据","warning");
	    	 		return;
	    		}
	    	 	var idsarr=new Array();
	    	 	for(var i=0;i<arr.length;i++){
	    	 			if(arr[i].deltag=="0"){
	    	 				$.messager.alert("信息","你所选的取派员中含有已离职的，请选择在职的取派员","info");
	    	 				return;
	    	 			}
	    	 			idsarr.push(arr[i].id);
	    	 	}
	    		var ids=idsarr.join(",");
	    		$.post("${pageContext.request.contextPath}/staffAction_deltag",{"ids":ids},function(data){
	    			if(data){
	    				$.messager.alert("信息","恭喜您，数据作废成功","info");
	    				$("#tb").datagrid("reload");
	    			}else{
	    				$.messager.alert("警告！","系统维护中。。。","warning");
	    			}
	    		});
	    	  
			}
	},{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : function(){
			//获取表格中被选中的行
    		var arr=  $("#tb").datagrid("getSelections");
    		if(arr==""&&arr.length==0){
    			$.messager.alert("警告！","请至少选择一条数据","warning");
    	 		return;
    		}
    	 	var idsarr=new Array();
    	 	for(var i=0;i<arr.length;i++){
    	 			if(arr[i].deltag=="1"){
    	 				$.messager.alert("信息","你所选的取派员中含有已在职的，请选择已离职的取派员","info");
    	 				return;
    	 			}
    	 			idsarr.push(arr[i].id);
    	 	}
    		var ids=idsarr.join(",");
    		$.post("${pageContext.request.contextPath}/staffAction_usedtag",{"ids":ids},function(data){
    			if(data){
    				$.messager.alert("信息","恭喜您，数据启用成功","info");
    				$("#tb").datagrid("reload");
    				$("#tb").datagrid("clearChecked");//清除之前选中的行
    			}else{
    				$.messager.alert("警告！","系统维护中。。。","warning");
    			}
    		});
    	  } 
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',checkbox : true,},
		{field : 'name',title : '姓名',width : 120,align : 'center'},
		{field : 'telephone',title : '手机号',width : 120,align : 'center'},
		{field : 'haspda',title : '是否有PDA',width : 120,align : 'center',  formatter: function(value,row,index){
	        if(value=="1"){
	        	return "有";
	        }else{
	        	return "无";
	        }
	      }},
		{field : 'deltag',title : '是否在职',width : 120,align : 'center',formatter: function(value,row,index){
	        if(value=="1"){
	        	return "在职";
	        }else{
	        	return "离职";
	        }
	      }}, 
		{field : 'standard',title : '取派标准',width : 120,align : 'center'}, 
		{field : 'station',title : '所属单位',width : 200,align : 'center'} 
		] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#tb').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pageSize:3,
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath }/staffAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow:function(rowIndex, rowData){
				$("#addStaffWindow").window("open");
				$("#staffForm").form("load",rowData);
			}
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '添加取派员',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });		
	});
	$.extend($.fn.validatebox.defaults.rules, { 
		telStandard: { 
		validator: function(value, param){ 
		var	regex=/^1[3578]\d{9}$/;
		return regex.test(value);
		}, 
		message: '电话号码为13、15、17、18开头的11位数字' 
		}, 
		uniquePhone: { 
			validator: function(value, param){ 
				var flag;
				$.ajax({
					url:"${pageContext.request.contextPath }/staffAction_checkTelephone",
					type:"POST",
					async:false,
					data:"telephone="+value,
					success:function(data){
						if(data){
							var staffId=$("#staffId").val();
							//alert(staffId);
							//alert(data==staffId); 
							if(data==staffId){
								flag=true
							}else{								
								flag=false;						
							}
						}else{
							flag=true;
						}
					}			
				});
				return flag;
			}, 
			message: '电话号码已存在' 
		}
	}); 
	$(function(){
		$("#save").click(function(){
			var flag=$("#staffForm").form("validate");
			if(flag){
				$("#staffForm").submit();
				$("#addStaffWindow").window("close");
			}		
		});
		//查询点击触发条件分页查询
		$("#query").click(function(){
			var jsonFormData={
					"name":$("#qname").val(),
					"telephone":$("#qtelephone").val(),
					"station":$("#qstation").val(),
					"haspda":$("#qhaspda:checked").val(),
					"standard":$("#qstandard").combobox("getValue")
					};
			$('#tb').datagrid('load',jsonFormData); 
			$("#queryStaffWindow").window("close");	
			});	
	});
	//窗体关闭时清空表单
	function clearForm(){
		$("#staffForm")[0].reset();
		$("#staffId").val("");
		$("#deltag").val("");		
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="tb"></table>
	</div>
	<div class="easyui-window" title="对收派员进行添加"data-options="onBeforeClose:clearForm"
	 id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form  id="staffForm" action="${pageContext.request.contextPath }/staffAction_save" method="post">
				<input type="hidden" name="id" id="staffId">
				 <input type="hidden" name="deltag" value="1"> 
				<table class="table-edit" width="80%" align="center">
					<tr class="title" align="center">
						<th colspan="2">收派员信息</th>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<tr>
						<td>姓名</td>
						<td><input type="text" name="name" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" class="easyui-validatebox"
						data-options="validType:['telStandard','uniquePhone']" required="true"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
						<input type="text" name="standard" class="easyui-combobox" required="true"   value="标准1"
						data-options="editable:false,valueField:'name',textField:'name',url:'${pageContext.request.contextPath }/standardAction_findAllInUse'"/>
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	<div class="easyui-window" title="查询收派员"data-options="onBeforeClose:clearForm" closed=true mdal=true
	 id="queryStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="width:350px;top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form  id="queryStaffForm" action="#" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title" align="center">
						<th colspan="2">收派员信息</th>
					</tr>
					<tr>
						<td>姓名</td>
						<td><input type="text" name="name"  id="qname"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="qtelephone"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" id="qstation" /></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1"  id="qhaspda"/>
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
						<input type="text" name="standard" class="easyui-combobox"  id="qstandard"
						data-options="editable:false,valueField:'name',textField:'name',url:'${pageContext.request.contextPath }/standardAction_findAllInUse'"/>
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>	