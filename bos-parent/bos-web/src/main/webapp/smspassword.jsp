<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回密码主页</title>
<script src="${pageContext.request.contextPath }/js/jquery-1.8.3.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/style.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/style_grey.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<style>
input[type=text] {
	width: 80%;
	height: 25px;
	font-size: 12pt;
	font-weight: bold;
	margin-left: 45px;
	padding: 3px;
	border-width: 0;
}

input[type=password] {
	width: 80%;
	height: 25px;
	font-size: 12pt;
	font-weight: bold;
	margin-left: 45px;
	padding: 3px;
	border-width: 0;
}

#loginform\:codeInput {
	margin-left: 1px;
	margin-top: 1px;
}

#loginform\:vCode {
	margin: 0px 0 0 60px;
	height: 34px;
}
</style>
<script type="text/javascript">
	if(window.self != window.top){
		window.top.location = window.location;
	}
 	var active = true;
	var second = 120; // 倒计时120秒
	var secondInterval;//计时器
	$(function(){
		function checkTelelphone(){
			var telephone =$("input[name='telephone']").val();
			var regex = /^1(3|5|7|8)\d{9}$/;
			if(telephone==""){
				$("#span1").css("color","red").html("电话号码不能为空");
				return false;
			}
			if(!(regex.test(telephone))){
				$("#span1").css("color","red").html("电话号码11数的整数");
				return false;
			}
			$("#span1").html("");
			return true;
		}
		$("input[name='telephone']").blur(checkTelelphone);
		
		$("#go").click(function(){
			//1.判断按钮是否可点击
			if(active==false){
				return;
			}
			var telephone =$("input[name='telephone']").val();
			if(checkTelelphone()){//2.判断电话号码是否合法
				//合法发送ajax请求到后台生成验证码并发送给客户
				active=false;//防止后台运行时间长，客户重复点击
				$.post("${pageContext.request.contextPath }/userAction_sendSms.action",
						{"telephone":telephone},function(data){
					if(data){
						alert("信息发送成功，请等待查收");
					secondInterval=setInterval(function(){
						if(second<=0){
							active=true;
							second=120;
							$("#go").html("获取验证码");
							clearInterval(secondInterval);
						}else{
						active=false;
						$("#go").html("倒计时"+second+"秒");
						second--;
						}
						
					}, 1000);
					}else{
					alert("消息发送失败，请联系管理员");
					active=true;	
					}
					
				});
			}
		});
		$("#con").click(function(){
			if(checkTelelphone()){							
			var code=$("input[name='checkcode']").val();
				if(code==""){
					$("#span2").css("color","red").html("密码为四位整数");
					return;
				}else{
					var telephone =$("input[name='telephone']").val();
					$("#span2").html("");
					//发送ajax请求对验证码电话号码进行验证
					$.post("${pageContext.request.contextPath }/userAction_checkSmsCode.action",
						{"telephone":telephone,"code":code},function(data){
							//alert(data);
							if(data=="success"){
								//弹出窗体进行新密码输入
								$('#confirmpwd').window("open");	
								return;
							}
							if(data=="telephoneError"){
								active=true;
								$("#span1").css("color","red").html("电话号码不存在，请输入真实有效的电话号码");
							 return;
							}
							if(data=="codeTimeOut"){
								active=true;
								$("#span1").css("color","red").html("验证码已过时，请重新输入");
								return;
							}
							if(data=="codeError"){
								$("#span1").css("color","red").html("验证码错误，请重新输入");
								return;
							}
						
					});
				}	
			}
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
				var telephone =$("input[name='telephone']").val();
				var password= $("#txtNewPass").val();
				$('#confirmpwd').window("close");
				location.href="${pageContext.request.contextPath }/userAction_updatePassword.action?telephone="+
						telephone+"&password="+password;
			}
		});
		$("#btnCancel").click(function(){
				$('#confirmpwd').window("close");	
		});
	}); 
</script>
</head>
<body>
	<div
		style="width: 900px; height: 50px; position: absolute; text-align: left; left: 50%; top: 50%; margin-left: -450px;; margin-top: -280px;">
		<img src="${pageContext.request.contextPath }/images/logo.png" style="border-width: 0; margin-left: 0;" />
		<span style="float: right; margin-top: 35px; color: #488ED5;">新BOS系统以宅急送开发的ERP系统为基础，致力于便捷、安全、稳定等方面的客户体验</span>
	</div>
	<div class="main-inner" id="mainCnt"
		style="width: 900px; height: 440px; overflow: hidden; position: absolute; left: 50%; top: 50%; margin-left: -450px; margin-top: -220px; background-image: url('${pageContext.request.contextPath }/images/bg_login.jpg')">
		<div id="loginBlock" class="login"
			style="margin-top: 80px; height: 255px;">
			<div class="loginFunc">
				<div id="lbNormal" class="loginFuncMobile">密码找回</div>
			</div>
			<div class="loginForm">
				<form id="newsmsform" name="loginform" method="post" class="niceform"
					action="#">
					<div id="idInputLine" class="loginFormIpt showPlaceholder"
						style="margin-top: 5px;">
						<input id="loginform:idInput" type="text" name="telephone"
							class="loginFormTdIpt" maxlength="50"/>
						<label for="idInput" class="placeholder" id="idPlaceholder">手机号：</label>
					</div>
					<center><span id="span1"></span><s:actionerror></s:actionerror></center>
					<div class="forgetPwdLine"></div>
					<div id="pwdInputLine" class="loginFormIpt showPlaceholder">
						<input id="loginform:pwdInput" class="loginFormTdIpt" type="text"
							name="checkcode"/>
						<label for="pwdInput" class="placeholder" id="pwdPlaceholder">验证码：</label>
						
					</div>
					<center><span id="span2"></span></center>
						<div class="loginFormIpt loginFormIptWiotTh"
						style="margin-top:58px;">
						<a href="#" id="loginform:j_id19" name="loginform:j_id19">
						<span
							id="go" class="btn btn-login"
							style="margin-top:-36px;margin-right: 155px">获取验证码</span>
						<span
							id="con" class="btn btn-login"
							style="margin-top:-36px;">确认</span>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div id="confirmpwd" class="easyui-window" title="找回密码"  
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
	<div
		style="width: 900px; height: 50px; position: absolute; text-align: left; left: 50%; top: 50%; margin-left: -450px;; margin-top: 220px;">
		<span style="color: #488ED5;">Powered By www.itcast.cn</span><span
			style="color: #488ED5;margin-left:10px;">推荐浏览器（右键链接-目标另存为）：<a
			href="http://download.firefox.com.cn/releases/full/23.0/zh-CN/Firefox-full-latest.exe">Firefox</a>
		</span><span style="float: right; color: #488ED5;">宅急送BOS系统</span>
	</div>
</body>
</html>