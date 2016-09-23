 $(document).ready(function() {
	 var fsn = window.fsn = window.fsn || {};
	 var subsidiary = window.fsn.subsidiary = window.fsn.subsidiary || {};
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	 portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	 var isNew = true;
	 subsidiary.edit_id = null;
	 subsidiary.editName=null;
	 subsidiary.editLic=null;
	 subsidiary.flagN=null;
	 subsidiary.flagLic=false;
	 subsidiary.editLic=null;
	 subsidiary.initialize=function(){
		 subsidiary.initKendoWindow("k_window","保存状态","300px","60px",false);
		 subsidiary.initView();
		 $("#save").bind("click",subsidiary.save);
		 $("#reset").bind("click",subsidiary.reset);
		 $("#address").bind("blur",function(){subsidiary.verifyAddress("address");});
	 };
	 
	//校验地址格式是否正确
	 subsidiary.verifyAddress = function(id){
	 	//判断地址选择窗口是否打开，若是没开打则检验
		if($(".provinceCityAll").is(":hidden")){
				var text = $("#"+id).val().trim();
				if(text==""){return;}			//若为空则不进行校验
				var strs= new Array(); 
				strs=text.split("-");				//分割字符串
				if(strs.length<2||strs.length>3){				//判断字符串数是否为3位
					lims.initNotificationMes('格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！',false);
					$("#"+id).val("");
					return;
				}else{
					//判断每个字符串是否有为空
					for(var i=0;i<strs.length;i++){
						if(strs[i].trim()==""){
							lims.initNotificationMes('省、市、区（县）不能为空</br>请重新填写！',false);
							$("#"+id).val("");
							return;
						}
					}
					var reg = /^[\u4e00-\u9fa5]{1,10}$/g; 				//校验是否全为汉字
					var str = "";
					if(strs.length<2){
						str = strs[0].trim()+strs[1].trim()+strs[2].trim();
					}else{
						str = strs[0].trim()+strs[1].trim();
					}
					if(!reg.test(str)){
						lims.initNotificationMes('只能输入汉字字符，请重新填写！',false);
						$("#"+id).val("");
						return;
					}
				}
			}
	};
	
	 subsidiary.initView=function(){
		 var params = fsn.getUrlVars();
		 subsidiary.edit_id=params[params[0]];
		 if(subsidiary.edit_id){
			 isNew=false;
			 $.ajax({
					url:portal.HTTP_PREFIX + "/business/business-unit/" + subsidiary.edit_id,
					type:"GET",
					dataType:"json",
					success:function(returnValue){						
						 $("#name").val(returnValue.businessUnit.name);
						 var address = returnValue.businessUnit.address;
						 var otherAddress = returnValue.businessUnit.otherAddress;
						 setAddressValue(address,otherAddress,"address","streetAddress");
						 $("#licenseNo").val(returnValue.businessUnit.license.licenseNo);
						 subsidiary.editName=returnValue.businessUnit.name;
						 subsidiary.editLic=returnValue.businessUnit.license.licenseNo;
					}
			});
		 }else{
			
		 }
	 };
	 subsidiary.save=function(){
		 var name=$("#name").val().trim();
		 if(name==""){
			 lims.initNotificationMes("企业名称不能为空", false);
			 return;
		 }
		/*var flagName =*///subsidiary.verificationName();				
		var flagAddress=subsidiary.verificationAddress();
		var flaLic=subsidiary.verificationLicenseNo();
		var flag=flagAddress&&flaLic;
		if(!flag){
			return;
		}
		if(isNew){
			$("#winMsg").html("正在保存数据，请稍候....");
		}else{
			$("#winMsg").html("正在修改数据，请稍候....");
		}
		$("#k_window").data("kendoWindow").open().center();
		var license = {
				licenseNo: $("#licenseNo").val().trim(),
		};
		var subBusiness={
				 id:subsidiary.edit_id,
				 name: $("#name").val().trim(),
				 address: $("#address").val().trim().replace(/-/g,"")+$("#streetAddress").val().trim(),
		         otherAddress: $("#address").val().trim()+"--"+$("#streetAddress").val().trim(),
				 license: license,
		 };
		 if(isNew){
			 $.ajax({
					url:portal.HTTP_PREFIX + "/business/subsidiary/add",
					type:"POST",
					dataType:"json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(subBusiness),
					success:function(data){						
						 	if(data.result.success){
						 		$("#k_window").data("kendoWindow").close();
						 		 lims.initNotificationMes("旗下企业新增成功", true);
						 		//fsn.msg("msg_success",data.result,fsn.l("旗下企业"+$("#name").val().trim()+"新增成功"));
						 		setTimeout('returnView()',2000);
						 	}else{
						 		$("#k_window").data("kendoWindow").close();
						 		lims.initNotificationMes("旗下企业新增失败", false);
						 		//fsn.msg("msg_success",data.result,fsn.l("旗下企业"+$("#name").val().trim()+"新增失败"));
						 	}
					}
			});
		 }else{
			 $.ajax({
					url:portal.HTTP_PREFIX + "/business/subsidiary/edit",
					type:"PUT",
					dataType:"json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(subBusiness),
					success:function(data){						
						if(data.result.success){
							$("#k_window").data("kendoWindow").close();
							 lims.initNotificationMes("旗下企业修改成功", true);
					 		//fsn.msg("msg_success",data.result,fsn.l("旗下企业"+$("#name").val().trim()+"修改成功"));
					 		setTimeout('returnView()',2000);
						}else{
							$("#k_window").data("kendoWindow").close();
							lims.initNotificationMes("旗下企业修改失败", false);
					 		//fsn.msg("msg_success",data.result,fsn.l("旗下企业"+$("#name").val().trim()+"修改失败"));
					 	}
					}
			});
		 }
	 };
	 subsidiary.verificationName=function(){
		 var name=$("#name").val().trim();
		 if(subsidiary.edit_id&&name==subsidiary.editName){
			 $("#verificationName").html("");
			 subsidiary.flagN=true;
			 return;
		 }
		 if(name!=""){
			 $("#verificationName").html("");
			 $.ajax({
					url:portal.HTTP_PREFIX + "/business/verificationNameOrLic/"+name+"/name",
					type:"GET",
					async:false,
					dataType:"json",
					contentType: "application/json; charset=utf-8",
					success:function(data){						
						 	if(data.result.success&&data.isExist){
						 		$("#verificationName").html("该企业已存在");
						 		subsidiary.flagN=false;
						 	}else{
						 		 $("#verificationName").html("");
						 		subsidiary.flagN=true;
						 	}
					}
			});
		 }else{
			 $("#verificationName").html("名称不能为空");
			 subsidiary.flagN = false;
		 }
	 };
	 subsidiary.verificationAddress=function(){
		 var val=$("#address").val().trim();
		 var val2=$("#streetAddress").val().trim();
			if(val==""||val2==""){
				lims.initNotificationMes("企业地址不能为空",false);
				return false;
			}else{
				$("#addressMsg").html("");
				return true;
			}
	 };
     
     //验证营业执照号
    subsidiary.verificationLicenseNo = function(){
    	var	val=$("#licenseNo").val().trim();
        $("#licMsg").html("");
        if(val==""){
        	lims.initNotificationMes("营业执照号不能为空!",false);
        	return false;
        }
		var licenNo = val.replace(/\s+/g, "").replace(/-/g,"");
		var VlicnNo = /^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/.test(licenNo);
		if(!VlicnNo){
			//$("#licenseNo").attr("style","width:300px;border:2px solid red;");
			lims.initNotificationMes("营业执照只能是13或15位的数字和字母",false);
			return false;
		}
  /*      if( subsidiary.editLic!=null&& subsidiary.editLic==val){
            return true;
        }
        if(val==subsidiary.editLic){
			 $("#licMsg").html("");
			 return true;
		 }*/
        var isLic=".*?[\u4E00-\u9FFF]+.*";
        if($("#licenseNo").val().trim()!=""){
            if(($("#licenseNo").val().trim()).match(isLic)){
            	lims.initNotificationMes("营业执照号不能有中文",false);
            	return false;
            }/*else if($("#licenseNo").val().trim()){
            	 $.ajax({
						url:portal.HTTP_PREFIX + "/business/verificationNameOrLic/"+$("#licenseNo").val().trim()+"/lic",
						type:"GET",
						dataType:"json",
                        async:false,
						contentType: "application/json; charset=utf-8",
						success:function(data){						
							 	if(data.result.success&&data.isExist){
							 		$("#licMsg").html("该营业执照号已存在");
							 		subsidiary.flagLic=false;
							 	}else{
							 		subsidiary.flagLic=true;
							 	}
						}
				});
            	 return subsidiary.flagLic;
            }*/
            return true;
        }     
    };
	 subsidiary.reset=function(){
		 $("#name").val("");	
		 $("#address").val("");
		 $("#streetAddress").val("");
		 $("#licenseNo").val("");
		 $("#verificationName").html("");
		 $("#addressMsg").html("");
		 $("#licMsg").html("");
	 };
	 returnView=function(){
		 subsidiary.reset();
		 window.location.href="/fsn-core/views/business/subsidiary-list.html";
	 };
	 subsidiary.initKendoWindow=function(winId,title,width,height,isVisible){
	    	$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:["Close"]
			});
	    };
	 subsidiary.initialize();
 });