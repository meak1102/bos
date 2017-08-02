<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加业务受理单</title>
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
$(function(){
	$("body").css({visibility:"visible"});
	load(0,province);//  页面加载立刻执行   传递value  0    ,目标对象  province
	$("#telephone").blur(function(){
		if(this.value==""){
			return;
		}
		$.post("${pageContext.request.contextPath}/noticeBillAction_findCustomerByTelephone",{"telephone":this.value},function(data){
			if(data!=null){
				$("#customerId").val(data.id);
				$("#customerName").val(data.name);
			}else{
				$("#customerId").val("");
				$("#customerName").val("");
			}
		});
		
	});
		$("#save").click(function(){
		/* 	var flag=$("#noticebillForm").from("validate");
			if(!flag){
				return;
			} */
			$("#hprovince").val($("#province option:selected").text());
			$("#hcity").val($("#sscity option:selected").text());
			$("#hdistrict").val($("#district option:selected").text());
			$("#noticebillForm").submit();
			
		});
});
//  三级联动 省市区数据获取  target表示目标生成数据的下拉框
function load(pid, target) {
	//标签通过value传递自己标签的输入值，通过标签id传递标签对象
	//$("#city")[0].length=1;
	target.length = 1; // 清空目标下拉框
	district.length = 1; // 同时清空县数据
	if (pid=="none")return;
	//  加载  省市区 所有实现方法
	$.ajax({
		url: "${pageContext.request.contextPath}/loadCityAction_load",
		type:"POST",
		data:{"pid" : pid},  
		success: function(result) {
			$(result).each(function(){
				var opt = document.createElement("option");
				opt.value = this.id;
				opt.innerHTML = this.name;
				$(target).append(opt);
			});
		}
	});
}
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false"
		border="false">
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton"
				plain="true">新单</a>
			<a id="edit" icon="icon-edit" href="${pageContext.request.contextPath }/page_qupai_noticebill.action" class="easyui-linkbutton"
				plain="true">工单操作</a>	
		</div>
	</div>
	<div region="center" style="overflow:auto;padding:5px;" border="false">
		<form id="noticebillForm" action="${pageContext.request.contextPath}/noticeBillAction_save" method="post">
			<table class="table-edit" width="95%" align="center">
				<tr class="title">
					<td colspan="4">客户信息</td>
				</tr>
				<tr>
					<td>来电号码:</td>
					<td><input type="text" class="easyui-validatebox" id ="telephone" name="telephone"
						required="true" /></td>
					<input type="hidden"  id ="customerId" name="customerId"/>
					<td>客户姓名:</td>
					<td><input type="text" class="easyui-validatebox"  id ="customerName" name="customerName"
						required="true" /></td>
				</tr>
				<tr class="title">
					<td colspan="4">货物信息</td>
				</tr>
				<tr>
					<td>品名:</td>
					<td><input type="text" class="easyui-validatebox" name="product"
						required="true" /></td>
					<td>件数:</td>
					<td><input type="text" class="easyui-numberbox" name="num"
						required="true" /></td>
				</tr>
				<tr>
					<td>重量:</td>
					<td><input type="text" class="easyui-numberbox" name="weight"
						required="true" /></td>
					<td>体积:</td>
					<td><input type="text" class="easyui-validatebox" name="volume"
						required="true" /></td>
				</tr>
				
				<tr>
				<input type="hidden"  id ="hprovince" name="province"/>
				<input type="hidden"  id ="hcity" name="city"/>
				<input type="hidden"  id ="hdistrict" name="district"/> 
					<td>取件地址</td>
					<td colspan="3">
					 省&nbsp;
					 <select id="province" onchange="load(value,sscity);">
					  <option value="none">--请选择--</option>
					 </select>&nbsp;
					 市&nbsp;
					 <select id="sscity" onchange="load(value,district);">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;
					 区&nbsp;
					 <select id="district">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;详细地址
					<input type="text" class="easyui-validatebox" name="pickaddress" required="true" size="75"/>
				  </td>
				</tr>
				<tr>
					<td>到达城市:</td>
					<td><input type="text" class="easyui-validatebox" name="arrivecity"
						required="true" /></td>
					<td>预约取件时间:</td>
					<td><input type="text" class="easyui-datebox" name="pickdate"
						data-options="required:true, editable:false" /></td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan="3"><textarea rows="5" cols="80" type="text" class="easyui-validatebox" name="remark"
						required="true" ></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>