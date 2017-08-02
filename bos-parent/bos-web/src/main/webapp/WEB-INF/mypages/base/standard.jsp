<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"></script>
		<title>取派标准</title>
		<script type="text/javascript">
			  $(function(){
					// 先将body隐藏，再显示，不会出现页面刷新效果
				  	$("body").css({visibility:"visible"});
					//数据表格
			    	 $('#tb').datagrid({ 
			    		 pagination:true,//分页
			    		 pageList:[3,6,9],
			    		 rownumbers:true,
			    		 striped:true,
			    		 url:'${pageContext.request.contextPath }/standardAction_pageQuery', 
			             fit:true,//数据填充父窗口
			             onDblClickRow:function(rowIndex, rowData){
							 $("#standardWindow").window("open");
			            	 $("#standardForm").form("load",rowData);
			            	 
			             },
			             toolbar: [{  		
			            	  iconCls: 'icon-add', 
			            	  text:'增加',
			            	  handler: function(){
			            		  $("#standardWindow").window("open");
			            		  }  	
			            	  },{  		
			            	  iconCls: 'icon-search',  	
			            	  text:'查询',
			            	  handler: function(){alert('edit')}  	
			            	  },{  		
				            	  iconCls: 'icon-cancel',  	
				            	  text:'作废',
				            	  handler: function(){
				            		  //获取表格中被选中的行
				            		var arr=  $("#tb").datagrid("getSelections");
				            		if(arr==""&&arr.length==0){
				            			$.messager.alert("警告！","请至少选择一条数据","warning");
				            	 		return;
				            		}
				            	 	var idsarr=new Array();
				            	 	for(var i=0;i<arr.length;i++){
				            	 			if(arr[i].deltag=="0"){
				            	 				$.messager.alert("信息","你所选的标准中含有已作废的，请选择已启用的标准","info");
				            	 				return;
				            	 			}
				            	 			idsarr.push(arr[i].id);
				            	 	}
				            		var ids=idsarr.join(",");
				            		$.post("${pageContext.request.contextPath}/standardAction_deltag",{"ids":ids},function(data){
				            			if(data){
				            				$.messager.alert("信息","恭喜您，数据作废成功","info");
				            				$("#tb").datagrid("reload");
				            				$("#tb").datagrid("clearChecked");//清除之前选中的行
				            			}else{
				            				$.messager.alert("警告！","系统维护中。。。","warning");
				            			}
				            		});
				            	  }  	
				            	  },{  		
					            	  iconCls: 'icon-save',  	
					            	  text:'还原',
					            	  handler: function(){
					            		  //获取表格中被选中的行
						            		var arr=  $("#tb").datagrid("getSelections");
						            		if(arr==""&&arr.length==0){
						            			$.messager.alert("警告！","请至少选择一条数据","warning");
						            	 		return;
						            		}
						            	 	var idsarr=new Array();
						            	 	for(var i=0;i<arr.length;i++){
						            	 			if(arr[i].deltag=="1"){
						            	 				$.messager.alert("信息","你所选的标准中含有已启用的，请选择已作废的标准","info");
						            	 				return;
						            	 			}
						            	 			idsarr.push(arr[i].id);
						            	 	}
						            		var ids=idsarr.join(",");
						            		$.post("${pageContext.request.contextPath}/standardAction_usedtag",{"ids":ids},function(data){
						            			if(data){
						            				$.messager.alert("信息","恭喜您，数据启用成功","info");
						            				$("#tb").datagrid("reload");
						            			}else{
						            				$.messager.alert("警告！","系统维护中。。。","warning");
						            			}
						            		});
						            	  }  	
					            	  }] ,
			    		 columns:[[ 
			    		 {field:'id',width:60,checkbox:true}, 
			    		 {field:'name',title:'标准名称',width:100,align:'center'}, 
			    		 {field:'minweight',title:'最小重量',width:120,align:'center'}, 
			    		 {field:'maxweight',title:'最大重量',width:120,align:'center'}, 
			    		 {field:'minlength',title:'最小长度',width:120,align:'center'}, 
			    		 {field:'maxlength',title:'最大长度',width:120,align:'center'}, 
			    		 {field:'operator',title:'操作人',width:140,align:'center'}, 
			    		 {field:'operationtime',title:'操作时间',width:140,align:'center'}, 
			    		 {field:'operatorcompany',title:'操作单位',width:120,align:'center'},
			    		 {field:'deltag',title:'是否作废',width:100,align:'center', formatter: function(value,row,index){
			    	       if(value=="1"){
			    	    	   return '已启用';
			    	       }else{
			    	    	   return '已作废';
			    	       }			
			    	      } },
			    		 ]] 

			    		 }); 
			     });
		$(function(){		
			$("#save").click(function(){
				var flag=$("#standardWindow").form("validate");
				if(flag){
					 $("#standardForm").submit();
					 $("#standardWindow").window("close");
				}
			});
		})
		function clearData(){
			//方式1
			//$("#standardForm").form("clear");
			//方式2
			$("#standardForm")[0].reset();
			$("#id").val();
			$("#deltag").val();
			
		}
		</script>
	</head>
	<body  style="visibility:hidden;">
		<table id="tb"></table>	
	<div id="standardWindow" class="easyui-window" title="收派标准"  data-options="onBeforeClose:clearData"
		collapsible="false" minimizable="false" modal="true" closed="true" resizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 300px; padding: 5px;
        background: #fafafa">
        <div class="easyui-layout" fit="true">
        <div region="north"   style="padding: 8px; background: #ffa; border: 1px solid #ccc">
        	<a id="save" icon="icon-save"  class="easyui-linkbutton" href="javascript:void(0)">保存</a>
        </div>
            <div region="center"  style="padding: 10px; background: #ffc; border: 1px solid #ccc;">
               <center><span>收派标准信息</span></center>
                <form  id="standardForm" method="post" action="${pageContext.request.contextPath }/standardAction_save.action" target="_self">
                <input  type="hidden" name="id" id="id">
                <input  type="hidden" name="deltag" id="deltag" value="1">
                <table cellpadding=3>
                    <tr>
                        <td>标准名称：</td>
                        <td><input  type="text" name="name" class="easyui-validatebox" data-options="required:true"></td>
                    </tr>
                    <tr>
                        <td>最小重量：</td>
                        <td><input  type="text" name="minweight" class="easyui-numberbox"  data-options="required:true,min:0,suffix:'kg'"></td>
                    </tr>
                    <tr>
                        <td>最大重量：</td>
                        <td><input  type="text" name="maxweight" class="easyui-numberbox"  data-options="required:true,min:0,suffix:'kg'"></td>
                    </tr>
                    <tr>
                        <td>最小长度：</td>
                        <td><input  type="text" name="minlength" class="easyui-numberbox" data-options="required:true,min:0,suffix:'cm'"></td>
                    </tr>
                    <tr>
                        <td>最大长度：</td>
                        <td><input  type="text" name="maxlength" class="easyui-numberbox"  data-options="required:true,min:0,suffix:'cm'"></td>
                    </tr>
                </table>
                </form>
            </div>
        </div>
    </div>	
	</body>

</html>