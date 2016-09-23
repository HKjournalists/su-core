$(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	fsn.portal.provinces = {};
	fsn.portal.location = "";
	fsn.portal.reg = {};
	fsn.portal.userNameUnique = false;
	fsn.portal.EnName = false;
	fsn.portal.aryProAttachments = new Array();
	fsn.portal.aryLiAttachments = new Array();
	fsn.portal.reg =$(".regForm").Validform({
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
            "szzm2-20" : /^[a-z0-9]{2,20}$/,
            "profession":/^[\S]{1,100}$/,///^[A-Za-z|\u4E00-\u9FA5\uf900-\ufa2d|0-9]{1,20}$/, 
            "psw6-20":/^[^\s]{6,20}$/,
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
                    	 fsn.portal.userNameUnique = true;                    	 
                     }else{
                    	 fsn.portal.EnName = true;
                     }
        		}else{
        			var objtip = obj.siblings(".tips");
                    objtip.addClass('Validform_checktip Validform_wrong').text(data.msg);
                    if(data.type=="userName"){                    	 
                   	 fsn.portal.userNameUnique = false;
                    }else{
                   	 fsn.portal.EnName = false;
                    }
        		}
        	}
        }
        
    });
	
	function resetForm(){
		fsn.portal.reg.resetForm();
		fsn.portal.reg.check();
		fsn.portal.reg.resetForm();
		fsn.portal.reg.check(true);
		$("#strength").hide();
		$("#pwdTips").show();
		for(var i=0; i<fsn.portal.aryProAttachments.length; i++){
			fsn.portal.aryProAttachments.pop();
		}for(var i=0; i<fsn.portal.aryLiAttachments.length; i++){
			fsn.portal.aryLiAttachments.pop();
		}	
		$("#fileEroMsg").html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
		$("#fileEroMsg1").html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
	}
	
	$("#regSubmit").on("click",function(){
		var userName = $('#username').val();
		var validFlag =fsn.portal.reg.check();
		if (validFlag === true && userName != '' && fsn.portal.userNameUnique && fsn.portal.EnName) {
			fsn.portal.submitRegInfo();
		};
	});
	
	fsn.portal.submitRegInfo = function(){
		
		if($("#type").val()=="请选择"){
			$("#show-type-msg").click();
			return;
		}
		if(fsn.portal.aryProAttachments.length==0){
			$("#show-org-msg").click();
			return;
		}
		if(fsn.portal.aryLiAttachments.length==0){
			$("#show-lic-msg").click();
			return;
		}	
		var ac=document.getElementById("accept1").checked;
		if(ac!=true){
			$("#show-accept-msg").click();
			return;
		}
		//$("#show-sub-msg").click();
		$("#winMsg").html("正在提交，请稍后....");
		$("#k_window").data("kendoWindow").open().center();
		var name=$("#username").val();		
		var email=$("#email").val();
		var pwd=$("#password").val();
		var enName=$("#enName").val();	
		var enAddress=$("#enAddress").val();	
		var enCharge=$("#enCharge").val();	
		var type=$("#type").val();	
		var enRegiste = {
			"userName":name,
			"password":pwd,
			"email":email,
			"enterpriteName":enName,
			"enterptiteAddress":enAddress,
			"legalPerson":enCharge,
			"enterpriteType":type,
			"orgAttachments":fsn.portal.aryProAttachments,
			"licAttachments":fsn.portal.aryLiAttachments
			};
		
		$.ajax({
			url : fsn.HTTP_PREFIX+"/business/enterpriseRegiste",
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
		fsn.portal.reg.check(false,"#location");
	});
	/*$("#enName").blur(function(){
		 var enName=$("#enName").val().trim();
		$.ajax({
			url : portal.HTTP_PREFIX+"/user/verificationenEnName/"+enName,
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
		fsn.portal.reg.check(false,"#location");
	});*/
	$('a[rel*=leanModal]').leanModal({closeButton: ".close-dialog"});
	
	fsn.portal.buildUpload = function(id, attachments, msg, flag){
   	 $("#"+id).kendoUpload({
       	 async: {
                saveUrl:"/fsn-core/service/user/kendoUI/addResources/" + flag,
                removeUrl:"/fsn-core/service/user/kendoUI/removeResources",
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
            multiple:true,
            success: function(e){
           	 if(e.response.typeEror){
           		 if(msg=="repFileMsg"){
           			 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传正确文件！"); 
           		 }else{
           			 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除重新上传png、bmp、jpeg格式的文件!");
           		 }
           		 return;
           	 }
           	 if(e.response.morSize){
           		 if(msg=="repFileMsg"){
           			 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传png、bmp、jpeg格式的文件!"); 
           		 }else{
           			 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传png、bmp、jpeg格式的文件!");
           		 }
           		 return;
           	 }
                if (e.operation == "upload") {
                    attachments.push(e.response.results[0]);
                    $("#"+msg).html("文件识别成功，可以保存！");
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
           		 $("#"+msg).html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
           	 }else{
           		 $("#"+msg).html("(注意:文件大小不能超过10M！  可支持的文件格式：png、bmp、jpeg！)");
           	 }
            },
            error:function(e){
           	 $("#"+msg).html("上传文件时出现异常！请稍后再试...");
            },
        });
    };
    fsn.portal.initKendoWindow=function(winId,title,width,height,isVisible){
    	$("#"+winId).kendoWindow({
			title:title,
			width:width,
			height:height,
			modal:true,
			visible:isVisible,
			actions:[""]
		});
    };
    fsn.portal.initKendoWindow("k_window","","300px","60px",false);
    fsn.portal.buildUpload("upload_prodPhoto_btn",fsn.portal.aryProAttachments,"fileEroMsg", "product");
    fsn.portal.buildUpload("upload_licenseNo_btn",fsn.portal.aryLiAttachments,"fileEroMsg1", "product");
    
});
