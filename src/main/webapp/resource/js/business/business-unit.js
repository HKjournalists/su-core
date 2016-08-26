$(document).ready(function(){
    var fsn = window.fsn = window.fsn ||{};
    var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    business_unit.id = null;
    business_unit.name=null;
    fsn.business_send = false;
    business_unit.editLic=null;
    business_unit.flagLic=false;
    business_unit.aryOrgAttachments = new Array();
    business_unit.aryLicenseAttachments = new Array();
    
    business_unit.initialize = function(){
    	/* 初始化上传控件 */
    	business_unit.buildUpload("upload_orgnization_files",business_unit.aryOrgAttachments,"fileEroMsg", "product");
    	business_unit.buildUpload("upload_license_files",business_unit.aryLicenseAttachments,"fileEroMsg", "product");
    	$("#btn_clearOrgFiles").bind("click",business_unit.clearOrgFiles);
		$("#btn_clearLicenseFiles").bind("click",business_unit.clearLicenseFiles);
		$("#btn_clearOrgFiles").hide();
		$("#btn_clearLicenseFiles").hide();
    	
    	business_unit.initKendoWindow("k_window","保存状态","300px","60px",false);
        business_unit.initView();
        $("#save").bind("click", business_unit.save);
        $("#reset").bind("click", business_unit.reset);
        $("#name").kendoValidator().data("kendoValidator");
        $("#address").kendoValidator().data("kendoValidator");
        $("#licenseNo").kendoValidator().data("kendoValidator");
        $("#personInCharge").kendoValidator().data("kendoValidator");
        
        //失去焦点做提示
        // $("#name").bind("blur",business_unit.verificationName);
        $("#telephone").bind("blur",business_unit.validaTetelephone);
        $("#postalCode").bind("blur",business_unit.validaPostalCode);
        //$("#licenseNo").bind("blur",business_unit.validaLicenseNo);
    };
    
    business_unit.initView = function(){
        isNew = false;
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/getBusinessByOrg",
            type: "GET",
            dataType: "json",
            success: function(returnValue){
                business_unit.name = returnValue.data.name;
                business_unit.editLic = returnValue.data.licenseNo;
                $("#name").val(returnValue.data.name);
                $("#address").val(returnValue.data.address);
                $("#licenseNo").val(returnValue.data.license.licenseNo);
                $("#personInCharge").val(returnValue.data.personInCharge);
                $("#contact").val(returnValue.data.contact);
                $("#telephone").val(returnValue.data.telephone);
                $("#postalCode").val(returnValue.data.postalCode);
                $("#email").val(returnValue.data.email);
                $("#fax").val(returnValue.data.fax);
                $("#orgCode").val(returnValue.data.orgInstitution.orgCode);
                business_unit.id = returnValue.data.id;
                if(returnValue.data.name != ""){
                	$("#name").attr("readonly", "readonly");
                }
                // 图片加载
                if(returnValue.data.orgAttachments != null){
                	business_unit.setOrgAttachments(returnValue.data.orgAttachments);
                }
                if(returnValue.data.licAttachments != null){
                	business_unit.setLicenseAttachments(returnValue.data.licAttachments);
                }
            }
        });
    };
    
    // 验证手机号码是否正常
    business_unit.validaTetelephone = function(){
        $("#isTel").html("");
       // var isMobile=/^(?:(?:0\d{2,3})-)?(?:\d{7,8})(-(?:\d{3,}))?$/;  // 固定电话
        var isPhone=/^(?:\+86)?(?:13\d|15\d|18\d)\d{8}$/;  // 手机
        if(($("#telephone").val().trim())!=""){
             if(!(($("#telephone").val().trim()).match(isPhone))){
            	 $("#isTel").html("请输入正确的手机号码");
            	 return false;
             }
        }
        return true;
    };
    
    //验证邮政编码是否正常
    business_unit.validaPostalCode = function(){
        $("#isPostalCode").html("");
        var postalCode = /^[0-9]{6}$/;
        if(($("#postalCode").val().trim())!=""){
             if(!(($("#postalCode").val().trim()).match(postalCode))){
            	 $("#isPostalCode").html("请输入正确的邮政编码");
            	 return false;
            }
        }
        return true;
    };
    
    /* 保存 */
    business_unit.save = function(){
    	var license = {
        		licenseNo: $("#licenseNo").val().trim(),
        	};
        var orgInstitution = {
        		orgCode:$("#orgCode").val().trim(),
        	};
    	// 数据封装
        var subBusiness = {
            id: business_unit.id,
            name: $("#name").val().trim(),
            address: $("#address").val().trim(),
            personInCharge: $("#personInCharge").val().trim(),
            contact: $("#contact").val().trim(),
            telephone: $("#telephone").val().trim(),
            postalCode: $("#postalCode").val().trim(),
            email:$("#email").val().trim(),
            fax:$("#fax").val().trim(),
            license: license,
            orgInstitution: orgInstitution,
            orgAttachments: business_unit.aryOrgAttachments,
            licAttachments: business_unit.aryLicenseAttachments,
        };
        
        /* 校验数据的有效性  */
        if(!business_unit.validateFormat()){
        	return;
        }
        
        $("#winMsg").html("正在修改数据，请稍候....");
        $("#k_window").data("kendoWindow").open().center();
        $.ajax({
            url: portal.HTTP_PREFIX + "/business/business-unit",
            type: "PUT",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(subBusiness),
            success: function(returnVal){
            	$("#k_window").data("kendoWindow").close();
            	if(returnVal.result.status == "true"){
			 		lims.initNotificationMes("企业信息修改成功", true);
			 		$("ul.k-upload-files").remove();
         		    business_unit.setOrgAttachments(returnVal.data.orgAttachments);
                    business_unit.setLicenseAttachments(returnVal.data.licAttachments);
                }
                else {
			 		lims.initNotificationMes("企业信息修改失败", false);
                }
            }
        });
    };
    
    /* 校验数据的有效性 */
    business_unit.validateFormat = function() {
    	//验证必填项
        var validate = $("#validate").kendoValidator().data("kendoValidator").validate();
        if (!validate) {
        	lims.initNotificationMes('请填写必填项！',false);
            return false;
        }
        if(!business_unit.validaTetelephone()){
        	lims.initNotificationMes('联系电话无效！',false);
        	return false;
        }
        if(!business_unit.validaPostalCode()){
        	lims.initNotificationMes('邮政编码无效！',false);
        	return false;
        }
        if(!business_unit.validaLicenseNo()){
        	lims.initNotificationMes('营业执照号无效！',false);
        	return false;
        }
        if(!business_unit.verificationName()){
        	lims.initNotificationMes('企业名称无效！',false);
        	return false;
        }
        return true;
    };
    
    /* 验证企业名是否唯一 */
    business_unit.verificationName = function(){
        var name = $("#name").val().trim();
        $("#isExist").html("");
        if( business_unit.name!=null&& business_unit.name==name){
            return true;
        }
        if (name) {
            $.ajax({
                url: portal.HTTP_PREFIX + "/business/verificationNameOrLic/" + name+"/name",
                type: "GET",
                dataType: "json",
                async:false,
                contentType: "application/json; charset=utf-8",
                success: function(data){
                   if(data.result.success&&data.isExist){
                       $("#isExist").html("企业名已存在");
                       fsn.business_send = false;
                    }else{
                    	 fsn.business_send = true;
                    }
                }
            });
            return fsn.business_send;
        }
    };
    
    /* 验证营业执照号 */
    business_unit.validaLicenseNo = function(){
    	var	val=$("#licenseNo").val().trim();
        $("#isLicenseNo").html("");
        if( business_unit.editLic!=null&& business_unit.editLic==val){
            return true;
        }
        if(val==business_unit.editLic){
			 $("#isLicenseNo").html("");
			 return true;
		 }
        var isLic=".*?[\u4E00-\u9FFF]+.*";
        if($("#licenseNo").val().trim()!=""){
            if(($("#licenseNo").val().trim()).match(isLic)){
            	$("#isLicenseNo").html("营业执照号不能有中文");
            	return false;
            }else if($("#licenseNo").val().trim()){
            	 $.ajax({
						url:portal.HTTP_PREFIX + "/business/verificationNameOrLic/"+$("#licenseNo").val().trim()+"/lic",
						type:"GET",
						dataType:"json",
                        async:false,
						contentType: "application/json; charset=utf-8",
						success:function(data){						
							 	if(data.result.success&&data.isExist){
							 		$("#isLicenseNo").html("该营业执照号已存在");
							 		business_unit.flagLic=false;
							 	}else{
							 		business_unit.flagLic=true;
							 	}
						}
				});
            	 return business_unit.flagLic;
            }
        }     
    };
    
    /* 重置  */
    business_unit.reset = function(){
        $("#name").val("");
        $("#address").val("");
        $("#licenseNo").val("");
        $("#verificationName").hide();
    };
    
    business_unit.initKendoWindow=function(winId,title,width,height,isVisible){
    	$("#"+winId).kendoWindow({
			title:title,
			width:width,
			height:height,
			modal:true,
			visible:isVisible,
			actions:["Close"]
		});
    };
    
    /* 初始化上传控件  */
    business_unit.buildUpload = function(id, attachments, msg, flag){
   	 $("#"+id).kendoUpload({
       	 async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select: id=="upload_orgnization_files"?lims.l("upload_OrgPhoto"):lims.l("upload_LicensePhoto"),
                retry:lims.l("retry",'upload'),
                remove:lims.l("remove",'upload'),
                cancel:lims.l("cancel",'upload'),
                dropFilesHere: lims.l("drop files here to upload",'upload'),
                statusFailed: lims.l("failed",'upload'),
                statusUploaded: lims.l("uploaded",'upload'),
                statusUploading: lims.l("uploading",'upload'),
                uploadSelectedFiles: lims.l("Upload files",'upload'),
            },
            multiple:true,
            upload: function(e){
                var files = e.files;
                 $.each(files, function () {
                	if(this.name.length > 100){
               		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
		                    e.preventDefault();
		                    return;
               	  	}
               	  	if(this.size>10400000){
                   	  	 lims.initNotificationMes('单个文件的大小不能超过10M！',false);
                         e.preventDefault();
                         return;
               	  	}
                 	var extension = this.extension.toLowerCase();
                  	if(extension != ".png" && extension != ".bmp" && extension != ".jpeg"&&extension != ".jpg"){
                  		lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!',false);
                  		e.preventDefault();
                  	}
               });
            },
            success: function(e){
           	 if(e.response.typeEror){
           		 $("#"+msg).html(lims.l("Wrong file type, the file will not be saved, please delete the re-upload png, bmp, jpeg file format!"));
           		 return;
           	 }
           	 if(e.response.morSize){
           		 $("#"+msg).html(lims.l("The file you uploaded more than 10M, re-upload, delete png, bmp, jpeg file format!"));
           		 return;
           	 }
                if (e.operation == "upload") {
                    lims.log("upload");
                    lims.log(e.response);
                    attachments.push(e.response.results[0]);
                    $("#"+msg).html(lims.l("Document recognition is successful, you can save!"));
                }else if(e.operation == "remove"){
                    lims.log(e.response);
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
           		 $("#"+msg).html("("+lims.l("Note: The file size can not exceed 10M! Supported file formats: png, bmp, jpeg")+")");
            },
            error:function(e){
           	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            },
        });
    };
    
    /* 从页面删除已有资源 */
    business_unit.removeRes = function(resID){
    	 var isOrgRes = false;
		 var dataSource = new kendo.data.DataSource();
		 for(var i=0; i<business_unit.aryOrgAttachments.length; i++){
			 if(business_unit.aryOrgAttachments[i].id == resID){
				 isOrgRes = true;
				 while((i+1)<business_unit.aryOrgAttachments.length){
					 business_unit.aryOrgAttachments[i] = business_unit.aryOrgAttachments[i+1];
					 i++;
				 }
				 business_unit.aryOrgAttachments.pop();
				 break;
			 }
		 }
		 if(!isOrgRes){
			 for(var i=0; i<business_unit.aryLicenseAttachments.length; i++){
	        	 if(business_unit.aryLicenseAttachments[i].id == resID){
	        		 isPro = false;
	        		 while((i+1)<business_unit.aryLicenseAttachments.length){
	        			 business_unit.aryLicenseAttachments[i] = business_unit.aryLicenseAttachments[i+1];
	        			 i++;
	        		 }
	        		 business_unit.aryLicenseAttachments.pop();
	        		 break;
	        	 }
	         }
			 if(business_unit.aryLicenseAttachments.length>0){
				 for(i=0; i<business_unit.aryLicenseAttachments.length; i++){
					 dataSource.add({attachments:business_unit.aryLicenseAttachments[i]});
				 }
			 }
			 $("#licenseAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			 if(business_unit.aryLicenseAttachments.length == 0){
				 $("#btn_clearLicenseFiles").hide();
			 }
		 }else{
				 if(business_unit.aryOrgAttachments.length>0){
					 for(i=0;i<business_unit.aryOrgAttachments.length;i++){
						 dataSource.add({attachments:business_unit.aryOrgAttachments[i]});
					 }
				 }
				 $("#orgAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				 if(business_unit.aryOrgAttachments==0){
					 $("#btn_clearOrgFiles").hide();
				 }
	      }
	 };
    
    /* 清空组织机构代码证件资源  */
    business_unit.clearOrgFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<root.aryOrgAttachments.length;i++){
		 	if(root.aryOrgAttachments[i].id != null){
		 		listId.push(root.aryOrgAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 root.aryOrgAttachments.length=count;
		 $("#orgAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearOrgFiles").hide();
	};
    
	/* 清空营业执照资源  */
	business_unit.clearLicenseFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<root.aryLicenseAttachments.length;i++){
		 	if(root.aryLicenseAttachments[i].id != null){
		 		listId.push(root.aryLicenseAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 root.aryLicenseAttachments.length=count;
		 $("#licenseAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearLicenseFiles").hide();
	};
	
	 /* 加载组织机构代码证件 */
    business_unit.setOrgAttachments = function(orgAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryOrgAttachments.length = 0;
		 if(orgAttachments.length>0){
			 $("#btn_clearOrgFiles").show();
			 for(var i=0;i<orgAttachments.length;i++){
				 business_unit.aryOrgAttachments.push(orgAttachments[i]);
				 dataSource.add({attachments:orgAttachments[i]});
			 }
		 }
		 $("#orgAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template:kendo.template($("#uploadedFilesTemplate").html()),
        });
	 };
	 
	 /* 加载营业执照 */
	 business_unit.setLicenseAttachments = function(licenseAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryLicenseAttachments.length = 0;
		 if(licenseAttachments.length>0){
			 $("#btn_clearLicenseFiles").show();
			 for(var i=0;i<licenseAttachments.length;i++){
				 business_unit.aryLicenseAttachments.push(licenseAttachments[i]);
				 dataSource.add({attachments:licenseAttachments[i]});
			 }
		 }
		 $("#licenseAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template:kendo.template($("#uploadedFilesTemplate").html()),
        });
	};
	
    business_unit.initialize();
});
