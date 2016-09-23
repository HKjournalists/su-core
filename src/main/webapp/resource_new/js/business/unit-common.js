(function($) {
	var fsn = window.fsn = window.fsn ||{};
    var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
    var portal = fsn.portal = fsn.portal ||{}; // portal命名空间
    portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
    
	/* 初始化上传控件  */
    business_unit.buildUpload = function(id, attachments, msg){
   	 $("#"+id).kendoUpload({
       	 async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select: business_unit.getShowName(id),
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
		                  return;
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
                if(id=="upload_certification_files"){
                	business_unit.setCertFiles(attachments, e);
                }else if(id=="upload_qs_files"){
                	business_unit.aryQsAttachments.push(e.response.results[0]);
                }else{
                	 attachments.push(e.response.results[0]);
                }
                $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
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
           		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
            },
            error:function(e){
           	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            },
        });
    };
    
    business_unit.setCertFiles = function(attachments, e){
    	/* 初始化上传控件 */
    	$("#upload_cert").hide();
    	$("#upload_certification_div").html("<input id='upload_certification_files' type='file' />");
    	business_unit.buildUpload("upload_certification_files", business_unit.certMap, "hideMsg");
    	/* 赋值操作 */
    	var certs = $("#certification-grid").data("kendoGrid").dataSource.data();
        if(certs[certs.length-1].certResource == "暂未上传..."){
            certs[certs.length-1].certResource = e.response.results[0].name;
        	attachments.put(certs.length-1, e.response.results[0]);
            $("#certification-grid").data("kendoGrid").refresh();
        }else{
        	if(fsn.addCert()){
        		var certs = $("#certification-grid").data("kendoGrid").dataSource.data();
        		certs[certs.length-1].certResource = e.response.results[0].name;
        		attachments.put(certs.length-1, e.response.results[0]);
                $("#certification-grid").data("kendoGrid").refresh();
        	}
        }
    };
    
    /* 按控件id初始化上传按钮的显示文字 */
    business_unit.getShowName = function(id){
    	if(id=="upload_orgnization_files"){
    		return lims.l("upload_OrgPhoto");
    	}else if(id=="upload_license_files"){
    		return lims.l("upload_LicensePhoto");
    	}else if(id=="upload_distribution_files"){
    	
    		if(business_unit.type!=null&&business_unit.type!=''&&business_unit.type.indexOf("餐饮企业")!=-1){	
    			return lims.l("上传餐饮许可证图片");
    		}
    		return lims.l("upload_DistributionPhoto");
    	}else if(id=="upload_qs_files"){
    		return lims.l("upload_QsPhoto");
    	}else if(id=="upload_logo_files"){
    		return lims.l("upload_LogoPhoto");
    	}else if(id="certFileMsg"){
    		return lims.l("upload_cert");
    	}
    };
    
    /* 从页面删除已有资源 */
    business_unit.removeRes = function(resID){
    	 var isOrgRes = false;
		 var dataSource = new kendo.data.DataSource();
		 if(business_unit.aryOrgAttachments){
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
		 }
		 if(isOrgRes){
			 /* 1. 要删除的图片是组织机构代码图片 */
			 if(business_unit.aryOrgAttachments.length>0){
				 for(i=0;i<business_unit.aryOrgAttachments.length;i++){
					 dataSource.add({attachments:business_unit.aryOrgAttachments[i]});
				 }
			 }
			 $("#orgAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			 if(business_unit.aryOrgAttachments==0){
				 $("#btn_clearOrgFiles").hide();
			 }
		 }else{
			 var isLicenseRes = false;
			 if(business_unit.aryLicenseAttachments){
				 for(var i=0; i<business_unit.aryLicenseAttachments.length; i++){
		        	 if(business_unit.aryLicenseAttachments[i].id == resID){
		        		 isLicenseRes = true;
		        		 while((i+1)<business_unit.aryLicenseAttachments.length){
		        			 business_unit.aryLicenseAttachments[i] = business_unit.aryLicenseAttachments[i+1];
		        			 i++;
		        		 }
		        		 business_unit.aryLicenseAttachments.pop();
		        		 break;
		        	 }
		         }
			 }
			 if(isLicenseRes){
				 /* 2. 要删除的图片是营业执照图片 */
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
				 var isDisRes = false;
				 if(business_unit.aryDisAttachments){
					 for(var i=0; i<business_unit.aryDisAttachments.length; i++){
			        	 if(business_unit.aryDisAttachments[i].id == resID){
			        		 isDisRes = true;
			        		 while((i+1)<business_unit.aryDisAttachments.length){
			        			 business_unit.aryDisAttachments[i] = business_unit.aryDisAttachments[i+1];
			        			 i++;
			        		 }
			        		 business_unit.aryDisAttachments.pop();
			        		 break;
			        	 }
			         }
				 }
				 if(isDisRes){
					 /* 3. 要删除的图片是流通许可证图片 */
					 if(business_unit.aryDisAttachments.length>0){
						 for(i=0; i<business_unit.aryDisAttachments.length; i++){
							 dataSource.add({attachments:business_unit.aryDisAttachments[i]});
						 }
					 }
					 $("#disAttachmentsListView").data("kendoListView").setDataSource(dataSource);
					 if(business_unit.aryDisAttachments.length == 0){
						 $("#btn_clearDisFiles").hide();
					 }
				 }else{
					 var isQsRes = false;
					 if(business_unit.aryQsAttachments){
						 for(var i=0; i<business_unit.aryQsAttachments.length; i++){
				        	 if(business_unit.aryQsAttachments[i].id == resID){
				        		 isQsRes = true;
				        		 while((i+1)<business_unit.aryQsAttachments.length){
				        			 business_unit.aryQsAttachments[i] = business_unit.aryQsAttachments[i+1];
				        		 	 i++;
				        		 }
				        		 business_unit.aryQsAttachments.pop();
				        		 break;
				        	 }
				         }
					 }
					 if(isQsRes){
						 /* 4. 要删除的图片是生产许可证图片 */
						 if(business_unit.aryQsAttachments.length>0){
							 for(i=0; i<business_unit.aryQsAttachments.length; i++){
								 dataSource.add({attachments:business_unit.aryQsAttachments[i]});
							 }
						 }
						 $("#qsAttachmentsListView").data("kendoListView").setDataSource(dataSource);
						 if(business_unit.aryQsAttachments.length == 0){
							 $("#btn_clearQsFiles").hide();
						 }
					 }else{
						 /* 5. 要删除的图片是企业Logo */
						 if(business_unit.aryLogoAttachments){
							 for(var i=0; i<business_unit.aryLogoAttachments.length; i++){
					        	 if(business_unit.aryLogoAttachments[i].id == resID){
					        		 isQsRes = true;
					        		 while((i+1)<business_unit.aryLogoAttachments.length){
					        			 business_unit.aryLogoAttachments[i] = business_unit.aryLogoAttachments[i+1];
					        		 	 i++;
					        		 }
					        		 business_unit.aryLogoAttachments.pop();
					        		 break;
					        	 }
					         }
						 }
						 if(business_unit.aryLogoAttachments.length>0){
							 for(i=0; i<business_unit.aryLogoAttachments.length; i++){
								 dataSource.add({attachments:business_unit.aryLogoAttachments[i]});
							 }
						 }
						 $("#logoAttachmentsListView").data("kendoListView").setDataSource(dataSource);
					 }
				 }
			 }
		 }
	 };
    
    /* 清空组织机构代码证件资源  */
    business_unit.clearOrgFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryOrgAttachments.length;i++){
		 	if(business_unit.aryOrgAttachments[i].id != null){
		 		listId.push(business_unit.aryOrgAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryOrgAttachments.length=count;
		 $("#orgAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearOrgFiles").hide();
	};
    
	/* 清空营业执照资源  */
	business_unit.clearLicenseFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryLicenseAttachments.length;i++){
		 	if(business_unit.aryLicenseAttachments[i].id != null){
		 		listId.push(business_unit.aryLicenseAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryLicenseAttachments.length=count;
		 $("#licenseAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearLicenseFiles").hide();
	};
	
	/* 清空生产许可证资源  */
	business_unit.clearQsFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryQsAttachments.length;i++){
		 	if(business_unit.aryQsAttachments[i].id != null){
		 		listId.push(business_unit.aryQsAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryQsAttachments.length=count;
		 business_unit.aryQsAttachments=[];
		 $("#qsAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearQsFiles").hide();
	};
	
	/* 清空流通许可证资源  */
	business_unit.clearDisFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryDisAttachments.length;i++){
		 	if(business_unit.aryDisAttachments[i].id != null){
		 		listId.push(business_unit.aryDisAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryDisAttachments.length=count;
		 $("#disAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearDisFiles").hide();
	};
	
	/* 加载组织机构代码证件 */
    /*business_unit.setQsAttachments = function(qsAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryQsAttachments.length = 0;
		 if(qsAttachments.length>0){
			 $("#btn_clearOrgFiles").show();
			 for(var i=0;i<qsAttachments.length;i++){
				 business_unit.aryQsAttachments.push(qsAttachments[i]);
				 dataSource.add({attachments:qsAttachments[i]});
			 }
		 }
		 $("#qsAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template:kendo.template($("#uploadedFilesTemplate").html()),
        });
		$("#upload_qs_div").html("<input id='upload_qs_files' type='file' />");
	    business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "fileEroMsg", "product");
	 };*/
	
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
		if(orgAttachments.length>0)  $("#orgAttachmentsListView button").hide();
		$("#upload_org_div").html("<input id='upload_orgnization_files' type='file' />");
	    business_unit.buildUpload("upload_orgnization_files",business_unit.aryOrgAttachments,"proFileMsgOrg", "product");
	 };
	 
	 /* 加载组织机构代码证件 */
	 business_unit.setLogoAttachments = function(logoAttachments){
			var dataSource = new kendo.data.DataSource();
			business_unit.aryLogoAttachments.length = 0;
			if(logoAttachments.length>0){
				 for(var i=0;i<logoAttachments.length;i++){
					 business_unit.aryLogoAttachments.push(logoAttachments[i]);
					 dataSource.add({attachments:logoAttachments[i]});
				 }
			}
			$("#logoAttachmentsListView").kendoListView({
	            dataSource: dataSource,
	            template:kendo.template($("#uploadedFilesTemplate").html()),
			});
			$("#upload_logo_div").html("<input id='upload_logo_files' type='file' />");
	    	business_unit.buildUpload("upload_logo_files",business_unit.aryLogoAttachments,"proFileMsgLogo", "product");
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
		if(licenseAttachments.length>0) $("#licenseAttachmentsListView button").hide();
		$("#upload_license_div").html("<input id='upload_license_files' type='file' />");
	    business_unit.buildUpload("upload_license_files",business_unit.aryLicenseAttachments,"proFileMsgLicense", "product");
	};
	
	/* 加载流通许可证图片 */
	 business_unit.setDisAttachments = function(disAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryDisAttachments.length = 0;
		 if(disAttachments.length>0){
			 $("#btn_clearDisFiles").show();
			 for(var i=0;i<disAttachments.length;i++){
				 business_unit.aryDisAttachments.push(disAttachments[i]);
				 dataSource.add({attachments:disAttachments[i]});
			 }
		 }
		 $("#disAttachmentsListView").kendoListView({
           dataSource: dataSource,
           template:kendo.template($("#uploadedFilesTemplate").html()),
        });
		$("#upload_distribution_div").html("<input id='upload_distribution_files' type='file' />");
	    business_unit.buildUpload("upload_distribution_files",business_unit.aryDisAttachments,"proFileMsgDistribution");
	};
	
	/* 加载生产许可证图片 */
	 business_unit.setQsAttachments = function(qsAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryQsAttachments.length = 0;
		 if(qsAttachments.length>0){
			 $("#btn_clearQsFiles").show();
			 for(var i=0;i<qsAttachments.length;i++){
				 business_unit.aryQsAttachments.push(qsAttachments[i]);
				 dataSource.add({attachments:qsAttachments[i]});
			 }
		 }
		 $("#qsAttachmentsListView").kendoListView({
			 dataSource: dataSource,
			 template:kendo.template($("#uploadedFilesTemplate").html()),
		 });
		 $("#upload_qs_div").html("<input id='upload_qs_files' type='file' />");
	     business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "proFileMsgQs", "product");
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
    
    /* 页面赋值操作 */
    business_unit.setValue = function(data, flag){
    	if(!data){return;}
    	$("#name").val(data.name);
        $("#personInCharge").val(data.personInCharge);
        $("#contact").val(data.contact);
        $("#telephone").val(data.telephone);
        $("#postalCode").val(data.postalCode);
        $("#email").val(data.email);
        $("#fax").val(data.fax);
        $("#product_about").val(data.about);
        $("#website").val(data.website);
        setAddressValue(data.address,data.otherAddress,"bus_mainAddr","bus_streetAddress","input");
        if(data.license != null){
        	var lice = data.license;
        	$("#licenseNo").val(lice.licenseNo);
            $("#licenseName").val((lice.licensename==null||lice.licensename=="")?data.name:lice.licensename);
            $("#legalName").val((lice.legalName==null||lice.legalName=="")?data.personInCharge:lice.legalName);
            $("#licenseStartTime").data("kendoDatePicker").value(lice.startTime!=null?lice.startTime.substr(0, 10):"");
            $("#licenseEndTime").data("kendoDatePicker").value(lice.endTime!=null?lice.endTime.substr(0, 10):"");
            $("#registrationTime").data("kendoDatePicker").value(lice.registrationTime!=null?lice.registrationTime.substr(0, 10):"");
            $("#issuingAuthority").val(lice.issuingAuthority);
            $("#subjectType").val(lice.subjectType);
            var mainAddr = (lice.businessAddress==null||lice.businessAddress=="")?data.address:lice.businessAddress;
            var streetAddr = (lice.otherAddress==null||lice.otherAddress=="")?data.otherAddress:lice.otherAddress;
            setAddressValue(mainAddr,streetAddr,"license_mainAddr","license_streetAddress","input");
            $("#toleranceRange").val(lice.toleranceRange);
            $("#registeredCapital").val(lice.registeredCapital);
            if(lice.licenseNo != null && lice.licenseNo != ""){
            	$("#licenseNo").attr("readonly", "readonly");
            }
        }
        if(data.orgInstitution != null){
        	var orgIns = data.orgInstitution;
        	$("#orgCode").val(orgIns.orgCode);
            $("#orgStartTime").data("kendoDatePicker").value(orgIns.startTime!=null?orgIns.startTime.substr(0, 10):"");
            $("#orgEndTime").data("kendoDatePicker").value(orgIns.endTime!=null?orgIns.endTime.substr(0, 10):"");
            $("#orgName").val((orgIns.orgName==null||orgIns.orgName=="")?data.name:orgIns.orgName);
            $("#unitsAwarded").val(orgIns.unitsAwarded);
            $("#orgType").val(orgIns.orgType);
            var mainAddr = (orgIns.orgAddress==null||orgIns.orgAddress=="")?data.address:orgIns.orgAddress;
            var streetAddr = (orgIns.otherAddress==null||orgIns.otherAddress=="")?data.otherAddress:orgIns.otherAddress;
            setAddressValue(mainAddr,streetAddr,"org_mainAddr","org_streetAddress","input");
            if(orgIns.orgCode != null && orgIns.orgCode != ""){
            	$("#orgCode").attr("readonly", "readonly");
            }
        }
        business_unit.name = data.name;
        business_unit.editLic = data.licenseNo;
        business_unit.id = data.id;
        if(data.name != null && data.name != ""){
        	$("#name").attr("readonly", "readonly");
        }
        // 图片加载
        if(data.logoAttachments != null){
        	business_unit.setLogoAttachments(data.logoAttachments);
        }
        if(data.orgAttachments != null){
        	business_unit.setOrgAttachments(data.orgAttachments);
        }
        if(data.licAttachments != null){
        	business_unit.setLicenseAttachments(data.licAttachments);
        }
        /*if(flag == "liutong" && data.district!=null){
 			for(var i=0;i<data.district.length;i++){
 				var description = data.district[i].description;
 				var discrId = data.district[i].id;
 				if(description=="主体性质"){
 					$("#liuTongBusType").data("kendoDropDownList").value(discrId);
 					continue;
 				}
 			}
        }*/
       var spanTitle = "食品流通许可证信息";
       var labTitle = "食品流通许可证编号：";
        if(data.type!=null&&data.type!=''&&data.type.indexOf("餐饮企业")!=-1){
        	spanTitle="食品餐饮许可证信息";
        	labTitle = "食品餐饮许可证编号：";
        }
        $("#header_dishs_title").html(spanTitle);
        $("#distributionNo_title").text(labTitle);
        if(flag == "liutong" && data.distribution!=null){
        	var distr = data.distribution;
        	$("#distributionNo").val(distr.distributionNo);
        	$("#licensingAuthority").val(distr.licensingAuthority);
        	$("#distributionName").val((distr.licenseName==null||distr.licenseName=="")?data.name:distr.licenseName);
        	$("#disStartTime").data("kendoDatePicker").value(distr.startTime!=null?distr.startTime.substr(0, 10):"");
        	$("#disEndTime").data("kendoDatePicker").value(distr.endTime!=null?distr.endTime.substr(0, 10):"");
        	$("#disSubjectType").val(distr.subjectType);
        	var mainAddr = (distr.businessAddress==null||distr.businessAddress=="")?data.address:distr.businessAddress;
            var streetAddr = (distr.otherAddress==null||distr.otherAddress=="")?data.otherAddress:distr.otherAddress;
            setAddressValue(mainAddr,streetAddr,"dis_mainAddr","dis_streetAddress","input");
        	$("#disToleranceRange").val(distr.toleranceRange);
        	$("#disLegalName").val((distr.legalName==null||distr.legalName=="")?data.personInCharge:distr.legalName);
        	if(data.disAttachments != null){
            	business_unit.setDisAttachments(data.disAttachments);
            }
        }else if(flag == "produce" && data.productionLicenses!=null){
        	$("#productionLic").data("kendoGrid").dataSource.data(data.productionLicenses);
        	$("#productionLic").data("kendoGrid").refresh();
        }
        if(data.distribution==null) {
        	$("#distributionName").val(data.name);
        	$("#disLegalName").val(data.personInCharge);
        	setAddressValue(data.address,data.otherAddress,"dis_mainAddr","dis_streetAddress","input");
        }
    };
    
    /*验证某个字段时，添加或取消输入框的样式
     * formId:输入框的id，msg：提示信息，valiResult：验证结果，true或false 
     */
    business_unit.validateInputStyle=function(formId,msg,valiResult){
    	if(!valiResult){
    		var parentDiv=$("#"+formId).parent();
    		parentDiv.find("span").remove();
  		  	var span=$("<span class='fieldFormatStyle'>"+msg+"</span>");
  		  	parentDiv.append(span);
    	}else{
    		var span=$("#"+formId).next();
    		if(span.length>0){span.remove();}
    	}
    };
    
    /* 验证手机号码是否正常  */
    business_unit.validaTetelephone = function(){
       // var isMobile=/^(?:(?:0\d{2,3})-)?(?:\d{7,8})(-(?:\d{3,}))?$/;  // 固定电话
	  var isPhone= /^(^(\d{3,4}-)?\d{7,8})$|(^(0|86|17951)?(13[0-9]|15[0123456789]|18[0123456789]|14[57])[0-9]{8}$)/;
        if(($("#telephone").val().trim())!=""){
             if(!(($("#telephone").val().trim()).match(isPhone))){
            	 business_unit.validateInputStyle("telephone","手机号码不正确！",false);
            	 return false;
             }else{
            	 business_unit.validateInputStyle("telephone","",true);
             }
        }
        return true;
    };
    
    /* 验证邮政编码是否正常  */
    business_unit.validaPostalCode = function(){
        var postalCode = /^[0-9]{6}$/;
        if(($("#postalCode").val().trim())!=""){
             if(!(($("#postalCode").val().trim()).match(postalCode))){
            	 business_unit.validateInputStyle("postalCode","邮编不正确！",false);
            	 return false;
            }else{
            	business_unit.validateInputStyle("postalCode","邮编正确！",true);
            }
        }
        return true;
    };
    
    /*验证邮箱格式*/
    business_unit.validateEmail=function(){
    	 var email = $("#email").val().trim();
    	 var reg =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    	  if(!reg.test(email)){
    		  business_unit.validateInputStyle("email","格式不正确！",false);
    		  return false;
    	}else{
    		 business_unit.validateInputStyle("email","正确！",true);
    	}
    	return true;
    };
    
    /*传真格式验证*/
    business_unit.validateFax=function(){
    	var fax=$("#fax").val().trim();
    	var pattern =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
        if(fax!="") {
        	if(!pattern.exec(fax)){
        		return false;
            }
        }
        return true; 
    };
    
    /*组织机构代码唯一性验证*/
    business_unit.verificationOrgCode = function(orgCode,orgId){
    	 $.ajax({
    			url:portal.HTTP_PREFIX + "/business/verificationEnOrgCode?orgCode="+orgCode+"&orgId="+orgId,
    			type:"GET",
    			success:function(data){
    				business_unit.orgExistsFlag = false;
				 	if(data.result.success&&data.isExist){
				 		lims.initNotificationMes('当前组织机构代码已经被使用，请重新输入！', false);
				 		business_unit.orgExistsFlag=true;
				 		$("#orgCode").val("");
				 	}
    			}
    	});
    };
    
	/*组织结构代码格式验证*/
	business_unit.validateOrgCode = function(){
		var orgCode = $("#orgCode").val().trim();
		var org=/^[A-Z0-9]{8}(\-)[A-Z0-9]{1}$/;
		return (orgCode).match(org);
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
            }/*else if($("#licenseNo").val().trim()){
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
            }*/
        }     
    };
    
    /* 重置  */
    business_unit.reset = function(){
        $("#name").val("");
        $("#address").val("");
        $("#licenseNo").val("");
        $("#verificationName").hide();
    };
    
    //验证必填的日期
    fsn.validateMustDate=function(formId,fieldName){
    	$("#test_time_format").data("kendoDatePicker").value($("#"+formId).val());
    	if($("#"+formId).val().trim() != ""){
    		if($("#test_time_format").val().trim() == ""){
    			fsn.errorInputId=formId;
        		$("#"+formId).attr("style","border:red 2px solid;");
        		if($("#"+formId).val().trim().length>0){
        			lims.initNotificationMes(fieldName+"日期格式不正确！",false);
        			return false;
        		}
    		}
    	}else{
			lims.initNotificationMes(fieldName+"不允许为空！",false);
			business_unit.wrong(formId,"select");
			return false;
		}
    	return true;
    };
    
    //验证日期格式是否有效
    fsn.validateDateFormat=function(formId,fieldName){
    	if($("#"+formId).val().trim().length>0){
    		var tmpVal = $("#"+formId).data("kendoDatePicker").value();
        	if(tmpVal==null){
        		fsn.errorInputId=formId;
        		lims.initNotificationMes(fieldName+"日期格式不正确！",false);
        		$("#"+formId).attr("style","border:red 2px solid;");
        		return false;
        	}
    	}
    	return true;
    };
    
    fsn.clearErrorStyle=function(){
    	if(fsn.errorInputId){
    		$("#"+fsn.errorInputId).removeAttr("style");
    	}
    };
    
    /* 验证数据有效性 */
    business_unit.validateCommon = function(){
    	/* 基本数据 */
        if (!business_unit.validateCommonData()){ return false; }
        /* 日期 */
        if(!business_unit.validateCommonDate()){ return false; }
        /* 图片 */
        if(!business_unit.validateCommonPhoto()){ return false; }
        return true;
    };
    
    /* 验证图片 */
    business_unit.validateCommonPhoto = function(){
    	if(business_unit.aryLogoAttachments.length<1){
        	lims.initNotificationMes('请上传企业logo图片！',false);
			business_unit.wrong("upload_logo_files","select");
        	return false;
        }
        if(business_unit.aryLicenseAttachments.length<1){
        	lims.initNotificationMes('请上传营业执照证件图片！',false);
			business_unit.wrong("upload_license_files","select");
        	return false;
        }
        if(business_unit.aryOrgAttachments.length<1){
        	lims.initNotificationMes('请上传组织机构代码证件图片！',false);
			business_unit.wrong("upload_orgnization_files","select");
        	return false;
        }
        return true;
    };
    
    /* 验证日期 */
    business_unit.validateCommonDate = function(){
    	if(!fsn.validateMustDate("licenseStartTime","营业执照的起始日期")){return false;}
    	if(!fsn.validateMustDate("licenseEndTime","营业执照的截止日期")){return false;}
//    	if(!fsn.validateMustDate("orgStartTime","组织机构证件的起始日期")){return false;}
//    	if(!fsn.validateMustDate("orgEndTime","组织机构证件的截止日期")){return false;}
    	if(!fsn.validateDateFormat("registrationTime","营业执照的注册日期")){return false;} // 非必填项
    	
        var startDate=$("#licenseStartTime").data("kendoDatePicker").value();
        var endDate=$("#licenseEndTime").data("kendoDatePicker").value();
        if((endDate-startDate)<0){
        	lims.initNotificationMes('营业执照的起始日期不能大于截止日期！',false);
        	return false;
        }
//        startDate=$("#orgStartTime").data("kendoDatePicker").value();
//        endDate=$("#orgEndTime").data("kendoDatePicker").value();
//        if((endDate-startDate)<0){
//        	lims.initNotificationMes('组织机构证件的起始日期不能大于截止日期！',false);
//        	return false;
//        }
    	return true;
    };
    
    business_unit.validateCommonData = function(){
    	if(!$("#name").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【企业基本信息】中的企业名称不能为空！',false);
			business_unit.wrong("name","text");
    		return false;
    	}
    	if(!$("#personInCharge").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【企业基本信息】中的法定代表人不能为空！',false);	
			business_unit.wrong("personInCharge","text");	
    		return false;
    	}
    	if(!$("#bus_mainAddr").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【企业基本信息】中的企业地址不能为空！',false);
			business_unit.wrong("bus_mainAddr","text");	
    		return false;
    	}
    	if(!$("#bus_streetAddress").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【企业基本信息】中的企业地址的街道地址不能为空！',false);
			business_unit.wrong("bus_streetAddress","text");	
    		return false;
    	}
    	if(!$("#email").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【企业基本信息】中的邮箱不能为空！',false);
			business_unit.wrong("email","text");
    		return false;
    	}
    	if(!$("#licenseNo").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【营业执照信息】中的营业执照注册号不能为空！',false);
			business_unit.wrong("licenseNo","text");
    		return false;
    	}
    	if(!$("#licenseName").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【营业执照信息】中的经营主体名称不能为空！',false);
			business_unit.wrong("licenseName","text");
    		return false;
    	}
    	if(!$("#legalName").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【营业执照信息】中的法人代表(负责人)不能为空！',false);
			business_unit.wrong("legalName","text");
    		return false;
    	}
    	if(!$("#license_mainAddr").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【营业执照信息】中的经营场所(企业地址)不能为空！',false);
			business_unit.wrong("license_mainAddr","text");
    		return false;
    	}
    	if(!$("#license_streetAddress").kendoValidator().data("kendoValidator").validate()){
    		lims.initNotificationMes('【营业执照信息】中的经营场所的街道地址不能为空！',false);
			business_unit.wrong("license_streetAddress","text");
    		return false;
    	}
//    	if(!$("#orgCode").kendoValidator().data("kendoValidator").validate()){
//    		lims.initNotificationMes('【组织机构代码证件】中的组织机构代码不能为空！',false);
//			business_unit.wrong("orgCode","text");
//    		return false;
//    	}
//    	if(!$("#orgName").kendoValidator().data("kendoValidator").validate()){
//    		lims.initNotificationMes('【组织机构代码证件】中的组织机构名称不能为空！',false);
//			business_unit.wrong("orgName","text");
//    		return false;
//    	}
//    	if(!$("#org_mainAddr").kendoValidator().data("kendoValidator").validate()){
//    		lims.initNotificationMes('【组织机构代码证件】中的组织机构地址不能为空！',false);
//			business_unit.wrong("org_mainAddr","text");
//    		return false;
//    	}
//    	if(!$("#org_streetAddress").kendoValidator().data("kendoValidator").validate()){
//    		lims.initNotificationMes('【组织机构代码证件】中的组织机构地址的街道地址不能为空！',false);
//			business_unit.wrong("org_streetAddress","text");
//    		return false;
//    	}
    	if(!business_unit.validaTetelephone()){
        	lims.initNotificationMes('联系电话无效！',false);
        	return false;
        }
        if(!business_unit.validaPostalCode()){
        	lims.initNotificationMes('邮政编码无效！',false);
        	return false;
        }
        if(!business_unit.validateEmail()){
        	lims.initNotificationMes('邮箱格式不正确！',false);
        	return false;
        }
        if(!business_unit.validateFax()){
        	lims.initNotificationMes("请输入正确的传真:传真号码格式为国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)，" +
        			"例如:0851-1234567",false);
        	return false;
        }
        if(!business_unit.verificationName()){
        	lims.initNotificationMes('企业名称无效！',false);
        	return false;
        }
//		 if(!business_unit.validateOrgCode()){
//        	lims.initNotificationMes("组织结构代码必须由8位数字(或大写字母)和一位数字(或者大写字母)组成！例如：1111AAAA-A",false);
//        	return false;
//        }
        var about = $('#product_about').val().trim();
        if(about.length>2000){
        	lims.initNotificationMes("企业简介请控制在2000字以内！",false);
        	return false;
        }
    	return true;
    };
    
    /*验证其他认证信息*/
    business_unit.validateCert=function(){
    	var certs=$("#certification-grid").data("kendoGrid").dataSource.data();
    	var certStr="";
		for(var i=0;i<certs.length;i++){
			if(certs[i].cert=="" || certs[i].cert=="请选择..."){
	    		 lims.initNotificationMes("请选择上传认证列表第" + (i+1) + "行的【认证类型】！", false);
	    		return false;
	    	 }
	    	 var endDate = certs[i].endDate;
	    	 //var validateEndDate = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(endDate);
	    	 if(!endDate){
	    		 lims.initNotificationMes("请正确填写认证列表第" + (i+1) + "行的【有效期截止时间】！", false);
	    		 return false;
	    	 }
	    	 if(certs[i].certResource=="暂未上传..."){
	    		 lims.initNotificationMes("请先上传认证列表第" + (i+1) + "行的认证图片！", false);
	    		 return false;
	    	 }
	    	 if(certStr.indexOf(certs[i].cert)!=-1){
	    		 lims.initNotificationMes("【"+certs[i].cert+"】认证信息重复，相同类型的证照只能添加一次！", false);
	    		 return false;
	    	 }
	    	 certStr=certStr+certs[i].cert+",";
		}
		return true;
    };
	
	business_unit.wrong = function(id,type){
		if (type == "text") {
			$("#" + id).focus();
			$("#" + id).css("border-color", "red");
			$("#" + id).bind("click", function(){
				$("#" + id).css("border-color", "");
			});
		}else if(type == "select"){
			$("#" + id).focus();
		}
		//控制页面向上滑动10
		var scrollTop=document.body.scrollTop||document.documentElement.scrollTop ;
		document.documentElement.scrollTop = scrollTop - 150;
	}
    
})(jQuery);