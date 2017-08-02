<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
		$('#addRegionWindow').window("open");
		$("#id").attr("readonly",null);
	}
	
	function doJasExport(){
		location.href="${pageContext.request.contextPath}regionAction_doJasExport";
	}
	
	function doItextExport(){
		location.href="${pageContext.request.contextPath}regionAction_doItextExport";
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
		$.post("${pageContext.request.contextPath}/regionAction_delete",{"ids":ids},function(data){
			if(data){
				$.messager.alert("信息","恭喜您，数据删除成功","info");
				$("#tb").datagrid("reload");//数据重新加载，但选中的数据无法清除
				$("#tb").datagrid("clearChecked");//清除之前选中的行
			}else{
				$.messager.alert("警告！","数据删除失败，系统维护中。。。","warning");
			}
		});
	}
	
	//工具栏
	var toolbar = [{
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : function(){
			$("#queryRegionWindow").window("open");
		}
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	},{id : 'button-export',text : 'Itext导出',iconCls : 'icon-undo',handler : doItextExport},
	{id : 'button-export',text : 'jas导出',iconCls : 'icon-undo',handler : doJasExport}];
	// 定义列
	var columns = [ [ {field : 'id',checkbox : true,},
	                  {field : 'province',title : '省',width : 120,align : 'center'}, 
	                  {field : 'city',title : '市',width : 120,align : 'center'}, 
	                  {field : 'district',title : '区',width : 120,align : 'center'}, 
	                  {field : 'postcode',title : '邮编',width : 120,align : 'center'},
	                  {field : 'shortcode',title : '简码',width : 120,align : 'center'}, 
	                  {field : 'citycode',title : '城市编码',width : 200,align : 'center'}
	                  ] ];	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#tb').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/regionAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加、修改区域窗口
		$('#queryRegionWindow,#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
	});

	function doDblClickRow(rowIndex, rowData){
		$("#addRegionWindow").window("open");
		$("#regionRegion").form("load",rowData);
		
		$("#id").attr("readonly","readonly");
	}
	$(function(){	
		$("#button-import").upload({//点击时触发这个函数加载数据
			name:'upload',
			action:"${pageContext.request.contextPath}/regionAction_oneClickUpload",
			enctype:'multipart/form-data',
			onSelect:function(){
				this.autoSubmit=false;
				var re=/^(.+\.xls|.+\.xlsx)$/;
				/* alert(this.filename);
				alert(re.test(this.filename())); */
				if(re.test(this.filename())){
					this.submit();
				}else{
					$.messager.alert("警告","请选择一个有效的excel文件","warning");
				}
			},
			onComplete: function(data) {
		       	//  服务器接受上传文件 之后  回送数据 ajax发送请求完成上传功能
		       	if(eval("("+data+")")){//返回的data为字符串类型
		       		$.messager.alert("恭喜","恭喜您！文件数据上传成功","info");
		       		$("#tb").datagrid("load",{});
		       	}else{
		       		$.messager.alert("警告！","文件数据上传失败，系统更新中。。。","warning");
		       	}
		       }		
		});
		$("#query").click(function(){//对查询按钮绑定点击事件
			var predicate={"province":$("#qprovince").val(),"city":$("#qcity").val(),
				"district":$("#qdistrict").val(),"postcode":$("#qpostcode").val(),
				"shortcode":$("#qshortcode").val(),"citycode":$("#qcitycode").val(),"tag":"1"};
			$("#tb").datagrid("load",predicate);//数据表格中数据带条件加载数据，并且上下页时还是可以把条件带上
			$("#queryRegionWindow").window("close");
		});
		$("#save").click(function(){
			var flag=$("#regionRegion").form("validate");
			if(flag){
				$("#regionRegion").submit();
			}
		});
	})
	function closeClear(){
		$("#regionRegion")[0].reset();
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="tb"></table>
	</div>
	<div class="easyui-window" title="区域查询" id="queryRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false"  >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form >
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" id="qprovince" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" id="qcity" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district"  id="qdistrict" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode" id="qpostcode" class="easyui-validatebox" /></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" id="qshortcode" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode" id="qcitycode"  class="easyui-validatebox" /></td>
					</tr>
					</table>
			</form>
		</div>
		<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" 
		data-options="onBeforeClose:closeClear" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id ="regionRegion" action="${pageContext.request.contextPath}/regionAction_save" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>区域编号</td>
						<td><input type="text" name="id"  id="id" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province"  class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city"  class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district"   class="easyui-validatebox" required="true" /></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode"  class="easyui-validatebox" required="true" /></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode"  class="easyui-validatebox" required="true"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	</div>
</body>
</html>