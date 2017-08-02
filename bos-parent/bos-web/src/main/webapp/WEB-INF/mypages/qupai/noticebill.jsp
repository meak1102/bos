<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>业务通知单</title>
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
$.fn.serializeJson=function(){  
    var serializeObj={};  
    var array=this.serializeArray();  
    var str=this.serialize();  
    $(array).each(function(){  
        if(serializeObj[this.name]){  
            if($.isArray(serializeObj[this.name])){  
                serializeObj[this.name].push(this.value);  
            }else{  
                serializeObj[this.name]=[serializeObj[this.name],this.value];  
            }  
        }else{  
            serializeObj[this.name]=this.value;   
        }  
    });  
    return serializeObj;  
}; 
	
	function doRepeat(){
		var arr=$("#tb").datagrid("getSelections");
		 if(arr==null||arr.length!=1){
				$.messager.alert("警告！","请选择一行","warning");
				return;
			}
			var id=arr[0].id;
			$.post("${pageContext.request.contextPath}/noticeAction_repeat",{"id":id},function(data){
				if(data){
					$("#tb").datagrid("reload");//数据重新加载，但选中的数据无法清除
					$("#tb").datagrid("clearChecked");//清除之前选中的行
				}else{
					$.messager.alert("警告！","追单失败，系统维护中。。。","warning");
				}
			});
	}
	
	function doCancel(){
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
			$.post("${pageContext.request.contextPath}/noticeAction_cancel",{"ids":ids},function(data){
				if(data){
					$.messager.alert("信息","工单已销","info");
					$("#tb").datagrid("reload");//数据重新加载，但选中的数据无法清除
					$("#tb").datagrid("clearChecked");//清除之前选中的行
				}else{
					$.messager.alert("警告！","暂无法销单，系统维护中。。。","warning");
				}
			});
	}
	
	function doSearch(){
		$('#searchWindow').window("open");
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-repeat',
		text : '追单',
		iconCls : 'icon-redo',
		handler : doRepeat
	}, {
		id : 'button-cancel',	
		text : '销单',
		iconCls : 'icon-cancel',
		handler : doCancel
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	}, {
		field : 'noticebill_id',
		title : '通知单号',
		width : 220,
		align : 'center',
		formatter: function(value,row,index){
			return row.noticeBill.id;	
			}
	},{
		field : 'type',
		title : '工单类型',
		width : 120,
		align : 'center'
	}, {
		field : 'pickstate',
		title : '取件状态',
		width : 120,
		align : 'center'
	}, {
		field : 'buildtime',
		title : '工单生成时间',
		width : 120,
		align : 'center',
		formatter: function(value,row,index){
			return new Date(row.buildtime).toLocaleDateString();	
			}
	}, {
		field : 'attachbilltimes',
		title : '追单次数',
		width : 120,
		align : 'center'
	}, {
		field : 'staff.name',
		title : '取派员',
		width : 100,
		align : 'center',
		formatter: function(value,row,index){
		return row.staff.name;	
		}
	}, {
		field : 'staff.telephone',
		title : '联系方式',
		width : 100,
		align : 'center',
		formatter: function(value,row,index){
			return row.staff.telephone;	
			}
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
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			url :  "${pageContext.request.contextPath}/WorkBillAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
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
		$("#btn").click(function(){
			var jsonData=$("#searchForm").serializeJson();
			$("#tb").datagrid("load",jsonData);
			$("#searchForm").get(0).reset();// 重置查询表单
			$("#searchWindow").window("close"); // 关闭窗口
		});
	});

	function doDblClickRow(){
		alert("双击表格数据...");
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="tb"></table>
	</div>
	
	<!-- 查询分区 -->
	<div class="easyui-window" title="查询窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="searchForm">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>客户电话</td>
						<td><input type="text" name="noticeBill.telephone"/></td>
					</tr>
					<tr>
						<td>取派员</td>
						<td><input type="text" name="staff.name" /></td>
					</tr>
					<tr>
						<td>受理时间</td>
						<td><input type="text" name="buildtime" class="easyui-datebox" /></td>
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