<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib  uri="http://shiro.apache.org/tags" prefix="p"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宅急送bos主界面</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"></script>
<SCRIPT type="text/javascript">
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			callback:{
				onClick: function(event, treeId, treeNode, clickFlag){
					if(treeNode.page==null){
						return;
					}
					 if($("#denamicTabs").tabs("exists",treeNode.name)){						 
						 $("#denamicTabs").tabs("select",treeNode.name); 
					 }else{	 
					//  treeNode 每一个菜单json 对象
					$("#denamicTabs").tabs("add",{title:treeNode.name,closable:true,
					content:'<div style="width:100%;height:100%;overflow:hidden;">'
						+ '<iframe src="'
						+ treeNode.page
						+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>',

					});
					
					 };
					
				}
			}
		};
		
		$(function(){	
			  $.post("${pageContext.request.contextPath}/menuAction_menuList",function(data){
				  	var basic= $.fn.zTree.init($("#basic"), setting, data);
					basic.expandAll(true);
			},"json"); 

			  <p:hasRole name="admin">
			  //只有超级管理员才会发送ajax请求
			  $.post("${pageContext.request.contextPath}/json/admin.json",function(data){
				 $.fn.zTree.init($("#sys"), setting, data);
			},"json"); 
			  </p:hasRole>
			//$.fn.zTree.init($("#basic"), setting, zBasic);
		});
		$(function(){
			// 显示消息窗口顶部中心 
			$.messager.show({  	
			  title:'My Title',  	
			  msg:'<p:principal property="email"></p:principal>，您好，欢迎您登录本系统',  	
			  showType:'slide', 
			  timeout:5000
			});  
			$("#quit").click(function (){
				$.messager.confirm('Confirm', '您确定要退出本系统吗?',
				function(r){
					if(r){
						location.href="${pageContext.request.contextPath }/userAction_quit.action";
					}else{
						return false;
					}
				}		
				
				);
			});
			$("#updatepwd").click(function(){
				$('#confirmpwd').window("open");			
			});
			function checkPassword(){
				var txtNewPass= $("#txtNewPass").val();
				var txtRePass= $("#txtRePass").val();
				var regex=/^\d{6,10}$/;
				if(txtNewPass==null||txtNewPass==""){
					$.messager.alert("警告","密码不能为空","warning");
					return false;
				}
				if(!(regex.test(txtNewPass))){
					$.messager.alert("警告","密码为6~10位数字","warning");
					return false;
				}
				if(txtNewPass!=txtRePass){
					$.messager.alert("警告","密码不一致，请重新输入","warning");
					return false;
				}
				return true;
			};
			$("#btnconCommit").click(function(){
				if(checkPassword()){
					var password= $("#txtNewPass").val();
					$('#confirmpwd').window("close");
					location.href="${pageContext.request.contextPath }/userAction_updatePassword2.action?password="+password;
				}
			});
			$("#btnCancel").click(function(){
					$('#confirmpwd').window("close");	
			});
					
		});
	</SCRIPT>
</head>
<body class="easyui-layout">

	<div data-options="region:'north',border:false" style="height:100px;background:url('${pageContext.request.contextPath }/images/header_bg.png') no-repeat right">
	<div  style="text-align:right;height:10px;"><font size="2px" color="green">
	[<strong><p:principal property="email"></p:principal></strong>]，欢迎你！您使用[<strong>${pageContext.request.remoteAddr}</strong>]IP登录！
	</font></div>
	<img src="${pageContext.request.contextPath }/images/logo.png" height="50px">
		<div style="text-align:right; margin-bottom:10px; margin-right:5px" height="20">
			<a href="#" class="easyui-menubutton" data-options="menu:'#mm1',iconCls:'icon-ok'">更换皮肤</a>
			<a href="#" class="easyui-menubutton" data-options="menu:'#mm2',iconCls:'icon-help'">控制面板</a>
		</div>
	</div>
	<div class="easyui-accordion" data-options="region:'west',split:false,title:'菜单导航'" style="width:200px">
		<div title="基本功能" data-options="iconCls:'icon-ok'" style="overflow:auto" >
			<ul id="basic" class="ztree"></ul>
		</div>
		<p:hasRole name="admin">
		<div title="系统功能" data-options="iconCls:'icon-ok'" style="overflow:auto">
			<ul id="sys" class="ztree"></ul>
		</div>
	</p:hasRole>
	</div>
	<div data-options="region:'south',border:false" style="height:60px;background:url('${pageContext.request.contextPath }/images/header_bg.png') no-repeat right;padding:10px;">south region</div>
	<div class="easyui-tabs"  data-options="region:'center'" id="denamicTabs">
			<div  data-options="title:'消息中心'"></div>
	</div>
	<div id="mm1" style="width:100px;">
		<div>default</div>
		<div>gray</div>
		<div>black</div>
		<div>bootstrap</div>
		<div>metro</div>
	</div>
	<div id="mm2" style="width:100px;">
		<div id="updatepwd">修改秘密</div>
		<div>联系管理员</div>
		<div id="quit">退出系统</div>
	</div>
	<div id="confirmpwd" class="easyui-window" title="修改密码"  
		collapsible="false" minimizable="false" modal="true" closed="true" resizable="false"
        maximizable="false" icon="icon-save"  style="width: 400px; height: 180px; padding: 5px;
        background: #fafafa">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <form  id="updlpwd" method="post" action="${pageContext.request.contextPath }/userAction_updatePassword.action">
                <table cellpadding=3>
                    <tr>
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="password" name="password" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="password" name="repassword" class="txt01" /></td>
                    </tr>
                </table>
                </form>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnconCommit" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >提交新密码</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消重置</a>
            </div>
        </div>
    </div>
</body>

</html>