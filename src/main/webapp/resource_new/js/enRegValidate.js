$(function(){


	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var qiye = fsn.qiye = fsn.qiye || {}; // qiye命名空间
	fsn.qiye.provinces = {};
	fsn.qiye.location = "";
	fsn.qiye.reg = {};
	fsn.qiye.userNameUnique = false;
	fsn.qiye.EnName = false;
	fsn.qiye.aryProAttachments = new Array();
	fsn.qiye.aryLiAttachments = new Array();
    
    //初始化
    fsn.qiye.initialize = function(){
        $("#password").bind("blur",fsn.qiye.VerifyPwd);
    };
    
	fsn.qiye.reg =$(".regForm").Validform({
        tiptype:function(msg,o,cssctl){
            //msg：提示信息;
            //o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），
            //type指示提示的状态，值为1、2、3、4，
            //1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
            //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
            if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
                var objtip=o.obj.siblings(".tips");
                cssctl(objtip,o.type);
                objtip.text(msg);
            }
        },
        datatype:{
//        	"location" : /^[\u4E00-\u9FA5\uf900-\ufa2d-]{1,}$/,
        	"location" : /^[\u4E00-\u9FA5]{1,}(\-)[\u4E00-\u9FA5]{1,}(\-)[\u4E00-\u9FA5]{1,}$|^[\u4E00-\u9FA5]{1,}(\-)[\u4E00-\u9FA5]{1,}$/,
            "szzm2-20" : /^[a-z0-9]{2,20}$/,
            "profession":/^[\S]{1,100}$/,///^[A-Za-z|\u4E00-\u9FA5\uf900-\ufa2d|0-9]{1,20}$/, 
            "psw6-20":/^[^\s]{6,20}$/,
            "orgcode":/^[A-Z0-9]{8}(\-)[A-Z0-9]{1}$/,
            "liccode":/^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/,
            "telePhone":/^(^(\d{3,4}-)?\d{7,8})$|(^(0|86|17951)?(13[0-9]|15[0123456789]|18[0123456789]|14[57])[0-9]{8}$)/,
            "e":/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
        },
        usePlugin:{
            passwordstrength:{
                minLen:6,//设置密码长度最小值，默认为0;
                maxLen:20,//设置密码长度最大值，默认为30;
                trigger:function(obj,error){
                    //该表单元素的keyup和blur事件会触发该函数的执行;
                    //obj:当前表单元素jquery对象;
                    //error:所设密码是否符合验证要求，验证不能通过error为true，验证通过则为false;
                    if(error){
                        obj.parent().find(".Validform_checktip").show();
                        obj.parent().find(".passwordStrength").hide();
                    }else{
                        obj.parent().find(".Validform_checktip").hide();
                        obj.parent().find(".passwordStrength").show();
                    }
                }
            }
        },
        ajaxurl:{
        	success: function(data, obj){
        		if (data.status == 'true'){
        			 var objtip = obj.siblings(".tips");
                     objtip.addClass('Validform_checktip Validform_right').text("通过信息验证！");
                     if(data.type=="userName"){
                    	 fsn.qiye.userNameUnique = true;                    	 
                     }else{
                    	 fsn.qiye.EnName = true;
                     }
        		}else{
        			var objtip = obj.siblings(".tips");
                    objtip.addClass('Validform_checktip Validform_wrong').text(data.msg);
                    if(data.type=="userName"){                    	 
                   	 fsn.qiye.userNameUnique = false;
                    }else{
                   	 fsn.qiye.EnName = false;
                    }
        		}
        	}
        }
        
    });
	
	function resetForm(){
		fsn.qiye.reg.resetForm();
		fsn.qiye.reg.check();
		fsn.qiye.reg.resetForm();
		fsn.qiye.reg.check(true);
		$("#strength").hide();
		$("#pwdTips").show();
		for(var i=0; i<fsn.qiye.aryProAttachments.length; i++){
			fsn.qiye.aryProAttachments.pop();
		}for(var i=0; i<fsn.qiye.aryLiAttachments.length; i++){
			fsn.qiye.aryLiAttachments.pop();
		}	
		$("#fileEroMsg").html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
		$("#fileEroMsg1").html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
	}
    
    fsn.qiye.sendValidate = function(url,id){
        $.ajax({
        		url:url,
        		type : "GET",
			    dataType: "json",
        		success: function(result){
        			if(result.isExist == false){
        				$("#"+id).parent().find('.txt-tip').addClass('Validform_checktip Validform_right').text("验证通过");
        				//fsn.qiye.userNameUnique = true;
        			}else{
        				message =  result.msg;
        				$("#"+id).parent().find('.txt-tip').removeClass("Validform_checktip Validform_right");
        				$("#"+id).parent().find('.txt-tip').addClass('Validform_checktip Validform_wrong').text(message);
        				//fsn.qiye.userNameUnique = false;
        			};
        		}
        	});
    }
    
    //验证营业执照
    $("#licvalue").blur(function(){
        var url = "/fsn-core/service/business/verificationEnLicenseNo?licenseNo=" + $("#licvalue").val().trim();
        fsn.qiye.sendValidate(url,"licvalue_msg");
    });
    //验证组织机构
    $("#orgvalue").blur(function(){
        var url = "/fsn-core/service/business/verificationEnOrgCode?orgCode=" + $("#orgvalue").val().trim();
        fsn.qiye.sendValidate(url,"orgvalue_msg");
        
    });
	$("#regSubmit").on("click",function(){
		var userName = $('#username').val();
		var apt = $("#accept");
		var validFlag =fsn.qiye.reg.check();
		var validFlag =true;
        var validPWD = fsn.qiye.VerifyPwd();
		if (validFlag === true && userName != '' && fsn.qiye.userNameUnique && fsn.qiye.EnName && validPWD) {
			fsn.qiye.submitRegInfo();
		};
	});
	
	fsn.qiye.submitRegInfo = function(){
		
		if($("#type").val()=="请选择"){
			$("#show-type-msg").click();
			return;
		}
		var filter=/^[A-Z0-9]{8}(\-)[A-Z0-9]{1}$/;
		if(!filter.test($('#orgvalue').val())){
			$("#show-org1-msg").click();
			return;
		}
		if(fsn.qiye.aryProAttachments.length==0){
			$("#show-org2-msg").click();
			return;
		}
		
		var filter2=/^[A-Za-z0-9]{15}$|^[A-Za-z0-9]{13}$|^[A-Za-z0-9]{18}$/;
		if(!filter2.test($('#licvalue').val())){
			$("#show-lic1-msg").click();
			return;
		}	
		if(fsn.qiye.aryLiAttachments.length==0){
			$("#show-lic2-msg").click();
			return;
		}	

		var ids = document.getElementById("accept");  
		var b=ids.checked;
		if(!b){
			$("#show-acc-msg").click();
			return;
		}
		//$("#show-sub-msg").click();
		$("#winMsg").html("正在提交，请稍后....");
		$("#k_window").data("kendoWindow").open().center();
		var name=$("#username").val();		
		var email=$("#email").val();
		var pwd=$("#password").val();
		var enName=$("#enName").val();	
		var enAddress=$("#location").val()+"--"+$("#enAddress").val();	
		var enCharge=$("#enCharge").val();	
		var type=$("#type").val();		
		var licvalue=$("#licvalue").val();	
		var orgvalue=$("#orgvalue").val();	
		var telePhone=$("#telePhone").val();	
		var enRegiste = {
			"userName":name,
			"password":pwd,
			"email":email,
			"enterpriteName":enName,
			"enterptiteAddress":enAddress,
			"legalPerson":enCharge,
			"enterpriteType":type,
			"orgAttachments":fsn.qiye.aryProAttachments,
			"licAttachments":fsn.qiye.aryLiAttachments,
			"licenseNo":licvalue,
			"organizationNo":orgvalue,
			"telephone":telePhone
			};
            
		
		$.ajax({
			url :"/fsn-core/service//business/enterpriseRegiste",
			type : "POST",
			dataType: "json",
			data : JSON.stringify(enRegiste),
			contentType:"application/json;charset=UTF-8",
			success : function(data){
				if (data.status == "true") {
					$("#k_window").data("kendoWindow").close();
					$("#show-reg-msg").hide();
					resetForm();
					$("#show-reg-msg").click();
				}else {
					$("#k_window").data("kendoWindow").close();
					if (data.msg != null){
						$("#regfailed-msg").find('label').text(data.msg);
					}
					$("#show-reg-msg").hide();

					$("#show-regfailed-msg").click();					
				}

			}
		});
	};
	
	$("#location").change(function(){
		fsn.qiye.reg.check(false,"#location");
	});
	/*$("#enName").blur(function(){
		 var enName=$("#enName").val().trim();
		$.ajax({
			url : _HTTP_PREFIX +"/user/verificationenEnName/"+enName,
			type : "GET",
			dataType: "json",
			contentType:"application/json;charset=UTF-8",
			success : function(data){
				if (data.status == "true") {				
					$("#verificationName").show();
					$("#verificationOName").hide();	
				}else {
					$("#verificationName").hide();				
				}
			}
		});
	});*/
	/*$("#enName").blur(function(){
		fsn.qiye.reg.check(false,"#location");
	});*/
	$('a[rel*=leanModal]').leanModal({closeButton: ".close-dialog"});
	
	fsn.qiye.buildUpload = function(id, attachments, msg, flag){
   	 $("#"+id).kendoUpload({
       	 async: {
                saveUrl:  "/fsn-core/service/resource/kendoUI/addResources/" + flag,
                removeUrl: "/fsn-core/service/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select:"选择图片",
                retry:"重试",
                remove:"删除",
                cancel:"取消",
                dropFilesHere:"drop files here to upload",
                statusFailed:"失败",
                statusUploaded: "上传成功",
                statusUploading: "上传中",
                uploadSelectedFiles: "已上传文件",
            },
            //前台上传文件验证
            upload: function(e){
                 var files = e.files;
                  $.each(files, function () {
                	  if(this.name.length > 100){
                		    if(id=="upload_prodPhoto_btn"){
                		    	$("#fileEroMsg").html("上传的文件名称应该小于50个汉字!");
                            }else{
                            	$("#fileEroMsg1").html("上传的文件名称应该小于50个汉字!");
                            }
		                    e.preventDefault();
		                    return;
           	  	  	  }
                      if(id =="upload_prodPhoto_btn" ||id =="upload_licenseNo_btn" ){
                          var extension = this.extension.toLowerCase();
                          if(extension != ".png" && extension != ".bmp" && extension != ".jpeg"&&extension != ".jpg"){
                              if(id=="upload_prodPhoto_btn"){
                                  $("#fileEroMsg").html("文件类型有误，请上传png、bmp、jpeg、jpg格式的文件!");
                              }else{
                                  $("#fileEroMsg1").html("文件类型有误，请上传png、bmp、jpeg、jpg格式的文件!");
                              }
                            e.preventDefault();
                          }
                      }
                });
             },
            multiple:true,
            success: function(e){
           	 if(e.response.typeEror){
           		 if(msg=="repFileMsg"){
           			 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传正确文件！"); 
           		 }else{
           			 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除重新上传png、bmp、jpeg、jpg格式的文件!");
           		 }
           		 return;
           	 }
           	 if(e.response.morSize){
           		 if(msg=="repFileMsg"){
           			 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传png、bmp、jpeg 、jpg格式的文件!"); 
           		 }else{
           			 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传png、bmp、jpeg 、jpg格式的文件!");
           		 }
           		 return;
           	 }
                if (e.operation == "upload") {
                    attachments.push(e.response.results[0]);
                    $("#"+msg).html("文件识别成功，可以保存！");
                   /* $("#liccode_height").html("<div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div>");
                    $("#licpic_height").html("<div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div>");*/
                }else if(e.operation == "remove"){
 
                    for(var i=0; i<attachments.length; i++){
                   	 if(attachments[i].name == e.files[0].name){
                   		 while((i+1)<attachments.length){
                   			 attachments[i]=attachments[i+1];
                   			 i++;
                   		 }
                   		 attachments.pop();
                   		 break;
                   	 }
                    }
                }
            },
            remove:function(e){
           	 if(msg=="repFileMsg"){
           		 $("#"+msg).html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg、jpg！)");
           	 }else{
           		 $("#"+msg).html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg、jpg！)");
           	 }
            },
            error:function(e){
           	 $("#"+msg).html("上传文件时出现异常！请稍后再试...");
            },
        });
    };
    
    /**
     * 企业注册时密码输入规则
     * 1.须同时包含字母、数字
     * 2.不得包含连续3个以上相同字符；
     * 3.密码长度为6-20个字符；
     * 4.不得包含账户名与空格。
     */
    fsn.qiye.VerifyPwd = function(){
        var tempPWD = $("#password").val().trim();
        var enUserName = $("#username").val().trim();
        var regex1 = /^(?=.*[a-zA-Z])(?=.*[0-9]).*$/ ; //验证须同时包含字母、数字
        var regex2 = /^.*?([a-zA-Z\d])\1\1.*?$/ ; //包涵三个连续相同的字符
        if(tempPWD.length >5 && tempPWD.length<21){
            if(!tempPWD.match(regex1)){
                $("#strength").hide();
    		    $("#pwdTips").show();
                $("#pwdTips").attr("class","tips Validform_checktip Validform_loading Validform_wrong");
                $("#pwdTips").html("密码不能全是数字或字母");
                return false;
        }else if(tempPWD.match(regex2)){
                $("#strength").hide();
    		    $("#pwdTips").show();
                $("#pwdTips").attr("class","tips Validform_checktip Validform_loading Validform_wrong");
                $("#pwdTips").html("密码不能包含三个连续相同的字符");
                return false;
        }else if(tempPWD.indexOf(enUserName)!=-1){
                $("#strength").hide();
    		    $("#pwdTips").show();
                $("#pwdTips").attr("class","tips Validform_checktip Validform_loading Validform_wrong");
                $("#pwdTips").html("密码不能包括用户名");
                return false;
            }
        return true;
        }else {
             $("#strength").hide();
		     $("#pwdTips").show();
             $("#pwdTips").attr("class","tips Validform_checktip Validform_loading Validform_wrong");
             $("#pwdTips").html("密码必须为6~20 为字符");
            return false;
        }
    };
    
    fsn.qiye.initKendoWindow=function(winId,title,width,height,isVisible){
    	$("#"+winId).kendoWindow({
			title:title,
			width:width,
			height:height,
			modal:true,
			visible:isVisible,
			actions:[""]
		});
    };
    fsn.qiye.initialize();
    fsn.qiye.initKendoWindow("k_window","","300px","60px",false);
    fsn.qiye.buildUpload("upload_prodPhoto_btn",fsn.qiye.aryProAttachments,"fileEroMsg", "product");
    fsn.qiye.buildUpload("upload_licenseNo_btn",fsn.qiye.aryLiAttachments,"fileEroMsg1", "product");
});
