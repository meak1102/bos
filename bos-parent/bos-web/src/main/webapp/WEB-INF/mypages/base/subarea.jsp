<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理分区</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
		<!-- 导入一键上传脚本 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery.ocupload.js"></script>
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
	function doAdd(){
		$('#addSubareaWindow').window("open");
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
			$.post("${pageContext.request.contextPath}/subareaAction_delete",{"ids":ids},function(data){
				if(data){
					$.messager.alert("信息","恭喜您，数据删除成功","info");
					$("#tb").datagrid("reload");//数据重新加载，但选中的数据无法清除
					$("#tb").datagrid("clearChecked");//清除之前选中的行
				}else{
					$.messager.alert("警告！","数据删除失败，系统维护中。。。","warning");
				}
			});
	}
	function doSearch(){
		$('#searchWindow').window("open");
	}
	
	function doExport(){
		$("#searchForm").submit();
	}
	function doUpdate(){
		 var arr=$("#tb").datagrid("getSelections");
		 if(arr.length==1){
			 var rowData=arr[0];
		$("#addSubareaWindow").window("open");
		$("#subareaForm").form("load",rowData);
		$("#sid").val(rowData.sid);
		//$("#region").combobox("select",rowData.region.province+rowData.region.city+rowData.region.district);
		$('#region').combobox('setValue',rowData.region.id);  
		$('#region').combobox("setText",rowData.region.province+rowData.region.city+rowData.region.district);
		 }else{
			 $.messager.alert("警告！","请选择一行修改","warning");
		 }
	}
	var editIndex;
	var flag=true;
	function doAddLine(){
		/* $('#tb').datagrid('insertRow',{
		  	index: 0,
		  	row:{}
		  });
		$("#tb").datagrid('beginEdit',0);
		flag=true; */
	}
	function doEdit(){
		$('#tb').datagrid("endEdit",editIndex);
		flag=true;
	}
	function doCancel(){
		$('#tb').datagrid("cancelEdit",editIndex);
		flag=true;
	}
	function doDblClickRow(rowIndex, rowData){
		if(flag){	
			editIndex=rowIndex;
			$("#tb").datagrid("beginEdit",rowIndex);
			flag=false;
		}
	}
	
	
	//工具栏
	var toolbar = [ {id : 'button-search',	text : '查询',iconCls : 'icon-search',handler : doSearch},
					{id : 'button-add',text : '增加',iconCls : 'icon-add',handler : doAdd},
					{id : 'button-edit',	text : '修改',iconCls : 'icon-edit',handler:doUpdate},
					{id : 'button-add',text : '新增一行',iconCls : 'icon-add',handler : doAddLine},
					{id : 'button-edit',	text : '行业修改保存',iconCls : 'icon-edit',handler:doEdit},
					{id : 'button-delete',text : '取消',iconCls : 'icon-cancel',handler : doCancel},
					{id : 'button-delete',text : '删除',iconCls : 'icon-cancel',handler : doDelete},
					{id : 'button-import',text : '导入',iconCls : 'icon-redo'},
					{id : 'button-export',text : '导出',iconCls : 'icon-undo',handler : doExport}
					];
	// 定义列
	var columns = [ [ {
		field : 'id',
		width : 100,
		align : 'center',
		checkbox : true,
	}, {
		field : 'showid',
		title : '分拣编号',
		width : 250,
		align : 'center',
		formatter : function(data,row ,index){
			return row.sid;
		}
	},{
		field : 'province',
		title : '省',
		width : 100,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.province;
		}
	}, {
		field : 'city',
		title : '市',
		width : 100,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.city;
		}
	}, {
		field : 'district',
		title : '区',
		width : 100,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.district;
		}
	}, {
		field : 'addresskey',
		title : '关键字',
		width : 120,
		align : 'center',
		editor:{
			type:"validatebox",
			options:{
				required:true
			}
		}
	}, {
		field : 'startnum',
		title : '起始号',
		width : 80,
		align : 'center'
	}, {
		field : 'endnum',
		title : '终止号',
		width : 80,
		align : 'center'
	} , {
		field : 'single',
		title : '单双号',
		width : 80,
		align : 'center',
		formatter : function(data,row ,index){
			if(data==0){
				return "单双号";
			}else{
				if(data==1){
					
				return "单号";
				}
			return "双号";
			}
		}
	} , {
		field : 'position',
		title : '位置',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#tb').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [3,5,8],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/subareaAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,
			onAfterEdit:function(rowIndex, rowData, changes){
				$.post("${pageContext.request.contextPath}/subareaAction_edit",rowData,function(data){
					if(data){
						$.messager.alert("恭喜","恭喜您！文件数据更改成功","info");
						
					}else{
						$.messager.alert("警告","系统升级中，请稍后修改","warning");
					}
				});
			}
		});
		
		// 添加、修改分区
		$('#addSubareaWindow').window({
	        title: '添加修改分区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 查询分区
		$('#searchWindow').window({
	        title: '查询分区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
	});
	$(function(){
		//数据导入，一键上传
		$("#button-import").upload({
			name:'upload',
			action:"${pageContext.request.contextPath}/subareaAction_oneClickUpload",
			enctype:'multipart/form-data',
			onSelect:function(){
				this.autoSubmit=false;//先不提交，先进行验证
				var re=/^(.+\.xls|.+\.xlsx)$/;
				if(re.test(this.filename())){
					this.submit();
				}else{
					$.messager.alert("警告","请选择一个有效的excel文件","warning");
				}
			},
			onComplete: function(data) {
		       	//  服务器接受上传文件 之后  回送数据 ajax发送请求完成上传功能
		       	if(data=="true"){//返回的data为字符串类型
		       		$.messager.alert("恭喜","恭喜您！文件数据上传成功","info");
		       		$("#tb").datagrid("load",{});
		       	}else{
		       		$.messager.alert("警告！","文件数据上传失败，系统更新中。。。","warning");
		       	}
		       }		
		});
		//分区添加
		$("#save").click(function(){
			var flag=$("#subareaForm").form("validate");
			if(flag){
				$("#subareaForm").submit();
				$("#addSubareaWindow").window("close");
				
			}
		});
		//条件查询
		$("#btn").click(function(){
			var predicate={"region.province":$("#qprovince").val(),"region.city":$("#qcity").val(),
					"region.district":$("#qdistrict").val(),"decidedzone.id":$("#qdecidedzone.id").val(),
					"addresskey":$("#qaddresskey").val()};
				$("#tb").datagrid("load",predicate);//数据表格中数据带条件加载数据，并且上下页时还是可以把条件带上
				$("#searchWindow").window("close");
		});
	})
	
	function closeClear(){//窗体关闭时清空
		$("#subareaForm")[0].reset();
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="tb"></table>
	</div>
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="分区添加修改" id="addSubareaWindow" collapsible="false" minimizable="false"
	 maximizable="false" style="top:20px;left:200px" data-options="onBeforeClose:closeClear">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form action="${pageContexet.request.contextPath }/subareaAction_save" method="post" id="subareaForm">
				<input type="hidden" id="sid" name="id" value=>
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">分区信息</td>
					</tr>
					<tr>
						<td>选择区域</td>
						<td>
							<input class="easyui-combobox" name="region.id"  id="region"
    							data-options="mode:'remote',valueField:'id',textField:'region',url:'${pageContext.request.contextPath }/regionAction_regionList'" />  
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>起始号</td>
						<td><input type="text" name="startnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>终止号</td>
						<td><input type="text" name="endnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>单双号</td>
						<td>
							<select class="easyui-combobox" name="single" style="width:150px;">  
							    <option value="0">单双号</option>  
							    <option value="1">单号</option>  
							    <option value="2">双号</option>  
							</select> 
						</td>
					</tr>
					<tr>
						<td>位置信息</td>
						<td><input type="text" name="position" class="easyui-validatebox" required="true" style="width:250px;"/></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 查询分区 -->
	<div class="easyui-window" title="查询分区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="searchForm" method="post" action="${pageContext.request.contextPath }/subareaAction_importData" >
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="region.province" id="qprovince" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="region.city"  id="qcity" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>区（县）</td>
						<td><input type="text" name="region.district" id="qdistrict" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" name="decidedzone.id" id="qdecidedzone.id" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey" id="qaddresskey" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>