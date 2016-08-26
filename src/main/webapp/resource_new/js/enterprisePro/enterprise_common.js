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
    
    /* 按控件id初始化上传按钮的显示文字 */
    business_unit.getShowName = function(id){
    	if(id=="upload_org_files"){
    		return lims.l("upload_OrgPhoto");
    	}else if(id=="upload_license_files"){
    		return lims.l("upload_LicensePhoto");
    	}else if(id=="upload_distribution_files"){
    		return lims.l("upload_DistributionPhoto");
    	}else if(id=="upload_qs_files"){
    		return lims.l("upload_QsPhoto");
    	}else if(id=="upload_tax_files"){
    		return lims.l("upload_TaxPhoto");
    	}else if(id=="upload_liquor_files"){
    		return lims.l("upload_LiquorPhoto");
    	}else if(id=="upload_proDep_files"){
    		return lims.l("upload_proDep_files");
    	}
    };
    
    /* 从页面删除已有资源 */
    business_unit.removeRes = function(resID){
    	var dataSource = new kendo.data.DataSource();
    	/*if(business_unit.aryOrgAttachments){
			 var isOrgRes = false;
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
			 if(isOrgRes){
				  1. 要删除的图片是组织机构代码图片 
				 if(business_unit.aryOrgAttachments.length>0){
					 for(i=0;i<business_unit.aryOrgAttachments.length;i++){
						 dataSource.add({attachments:business_unit.aryOrgAttachments[i]});
					 }
				 }
				 $("#orgAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				 if(business_unit.aryOrgAttachments==0){
					 $("#btn_clearOrgFiles").hide();
				 }
				 return;
			 }
		 }
    	if(business_unit.aryLicenseAttachments){
			var isLicenseRes = false;
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
			if(isLicenseRes){
				 2. 要删除的图片是营业执照图片 
				if(business_unit.aryLicenseAttachments.length>0){
					for(i=0; i<business_unit.aryLicenseAttachments.length; i++){
						dataSource.add({attachments:business_unit.aryLicenseAttachments[i]});
					}
				}
				$("#licenseAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				if(business_unit.aryLicenseAttachments.length == 0){
					$("#btn_clearLicenseFiles").hide();
				}
				return;
			}
		}
    	if(business_unit.aryQsAttachments){
			var isQsRes = false;
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
			if(isQsRes){
				//3. 要删除的图片是生产许可证图片 
				if(business_unit.aryQsAttachments.length>0){
					for(i=0; i<business_unit.aryQsAttachments.length; i++){
						dataSource.add({attachments:business_unit.aryQsAttachments[i]});
					}
				}
				$("#qsAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				if(business_unit.aryQsAttachments.length == 0){
					$("#btn_clearQsFiles").hide();
				}
				return;
			}
		}*/
    	if(business_unit.aryTaxAttachments){
    		var isTaxRes = false;
			for(var i=0; i<business_unit.aryTaxAttachments.length; i++){
		        if(business_unit.aryTaxAttachments[i].id == resID){
			        isTaxRes = true;
			        while((i+1)<business_unit.aryTaxAttachments.length){
			        	business_unit.aryTaxAttachments[i] = business_unit.aryTaxAttachments[i+1];
			        	i++;
			        }
			        business_unit.aryTaxAttachments.pop();
			        break;
			    }
			}
			if(isTaxRes){
				//4. 要删除的图片是税务登记证图片 
				if(business_unit.aryTaxAttachments.length>0){
					for(i=0; i<business_unit.aryTaxAttachments.length; i++){
						dataSource.add({attachments:business_unit.aryTaxAttachments[i]});
					}
				}
				$("#taxAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				if(business_unit.aryTaxAttachments.length == 0){
					$("#btn_clearTaxFiles").hide();
				}
				return;
			}
		}
    	if(business_unit.aryLiquorAttachments){
			var isLiquorRes = false;
			for(var i=0; i<business_unit.aryLiquorAttachments.length; i++){
			     if(business_unit.aryLiquorAttachments[i].id == resID){
			        isLiquorRes = true;
			        while((i+1)<business_unit.aryLiquorAttachments.length){
			        	business_unit.aryLiquorAttachments[i] = business_unit.aryLiquorAttachments[i+1];
			        	i++;
			        }
			        business_unit.aryLiquorAttachments.pop();
			        break;
			      }
			 }
			if(isLiquorRes){
				// 5. 要删除的图片是酒类销售许可证图片 
				if(business_unit.aryLiquorAttachments.length>0){
					for(i=0; i<business_unit.aryLiquorAttachments.length; i++){
						dataSource.add({attachments:business_unit.aryLiquorAttachments[i]});
					}
				}
				$("#liquorAttachmentsListView").data("kendoListView").setDataSource(dataSource);
				if(business_unit.aryLiquorAttachments.length == 0){
					$("#btn_clearLiquorFiles").hide();
				}
				return;
			}
		}
	};
    
	/*删除生产车间中指定的图片*/
	business_unit.removeProDepRes=function(resId,fileName){
		var dataSource = new kendo.data.DataSource();
		if(business_unit.currentDepAtts){
			if(resId!=null){
				for(var i=0; i<business_unit.currentDepAtts.length; i++){
				     if(business_unit.currentDepAtts[i].id == resId){
				        while((i+1)<business_unit.currentDepAtts.length){
				        	business_unit.currentDepAtts[i] = business_unit.currentDepAtts[i+1];
				        	i++;
				        }
				        business_unit.currentDepAtts.pop();
				        break;
				      }
				 }
			}else{
				for(var i=0; i<business_unit.currentDepAtts.length; i++){
				     if(business_unit.currentDepAtts[i].id==null&&
				    		 business_unit.currentDepAtts[i].fileName == fileName){
				        while((i+1)<business_unit.currentDepAtts.length){
				        	business_unit.currentDepAtts[i] = business_unit.currentDepAtts[i+1];
				        	i++;
				        }
				        business_unit.currentDepAtts.pop();
				        break;
				      }
				 }
			}
			if(business_unit.currentDepAtts.length>0){
				for(i=0; i<business_unit.currentDepAtts.length; i++){
					dataSource.add({attachments:business_unit.currentDepAtts[i]});
				}
			}
			$("#proDepAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			if(business_unit.currentDepAtts.length == 0){
				$("#btn_clearProDepFiles").hide();
			}
		}
	};
	
    /* 清空组织机构代码证件资源  */
    /*business_unit.clearOrgFiles = function(){
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
	};*/
    
	/* 清空营业执照资源  */
	/*business_unit.clearLicenseFiles = function(){
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
	};*/
	
	/* 清空生产许可证资源  */
	/*business_unit.clearQsFiles = function(){
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
		 $("#qsAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearQsFiles").hide();
	};*/
	
	/* 清空税务登记证资源  */
	business_unit.clearTaxFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryTaxAttachments.length;i++){
		 	if(business_unit.aryTaxAttachments[i].id != null){
		 		listId.push(business_unit.aryTaxAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryTaxAttachments.length=count;
		 $("#taxAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearTaxFiles").hide();
	};
	
	/* 清空酒类销售许可证资源  */
	business_unit.clearLiquorFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryLiquorAttachments.length;i++){
		 	if(business_unit.aryLiquorAttachments[i].id != null){
		 		listId.push(business_unit.aryLiquorAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		 business_unit.aryLiquorAttachments.length=count;
		 $("#liquorAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearLiquorFiles").hide();
	};
	
	/* 清空企业生产车间图片  */
	business_unit.clearProDepFiles = function(){
		business_unit.currentDepAtts.length=0;
		$("#proDepAttachmentsListView").data("kendoListView").dataSource.data([]);
		$("#btn_clearProDepFiles").hide();
	};
	
	/* 加载生产许可证 */
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
	    business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "fileEroMsg");
	 };
	
	/* 加载组织机构代码证件 */
    business_unit.setOrgAttachments = function(orgAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryOrgAttachments.length = 0;
		 if(orgAttachments.length>0){
			 $("#btn_clearOrgFiles").show();
			 for(var i=0;i<orgAttachments.length;i++){
				 //business_unit.aryOrgAttachments.push(orgAttachments[i]);
				 dataSource.add({attachments:orgAttachments[i]});
			 }
		 }
		 $("#orgAttachmentsListView").kendoListView({
            dataSource: dataSource,
            template:kendo.template($("#uploadedFilesTemplate").html()),
        });
		$("#upload_org_div").html("<input id='upload_org_files' type='file' />");
	    business_unit.buildUpload("upload_org_files",business_unit.aryOrgAttachments,"fileEroMsg");
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
		$("#upload_license_div").html("<input id='upload_license_files' type='file' />");
	    business_unit.buildUpload("upload_license_files",business_unit.aryLicenseAttachments,"fileEroMsg");
	};
	
	/* 加载税务登记证图片 */
	 business_unit.setTaxRegAttachments = function(taxAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryTaxAttachments.length = 0;
		 if(taxAttachments.length>0){
			 $("#btn_clearTaxFiles").show();
			 for(var i=0;i<taxAttachments.length;i++){
				 business_unit.aryTaxAttachments.push(taxAttachments[i]);
				 dataSource.add({attachments:taxAttachments[i]});
			 }
		 }
		 $("#taxAttachmentsListView").kendoListView({
           dataSource: dataSource,
           template:kendo.template($("#uploadedFilesTemplate").html()),
       });
		$("#upload_tax_div").html("<input id='upload_tax_files' type='file' />");
	    business_unit.buildUpload("upload_tax_files",business_unit.aryTaxAttachments,"fileEroMsg");
	};
	
	/* 加载酒类销售许可证图片 */
	 business_unit.setLiquorAttachments = function(liquorAttachments){
		 var dataSource = new kendo.data.DataSource();
		 business_unit.aryLiquorAttachments.length = 0;
		 if(liquorAttachments.length>0){
			 $("#btn_clearLiquorFiles").show();
			 for(var i=0;i<liquorAttachments.length;i++){
				 business_unit.aryLiquorAttachments.push(liquorAttachments[i]);
				 dataSource.add({attachments:liquorAttachments[i]});
			 }
		 }
		 $("#liquorAttachmentsListView").kendoListView({
			 dataSource: dataSource,
			 template:kendo.template($("#uploadedFilesTemplate").html()),
		 });
		 $("#upload_liquor_div").html("<input id='upload_liquor_files' type='file' />");
	     business_unit.buildUpload("upload_liquor_files", business_unit.aryLiquorAttachments, "fileEroMsg");
	};
	
	/* 加载企业生产车间图片 */
	 business_unit.setProDepAttachments = function(proDepAttachments){
		 var dataSource = new kendo.data.DataSource();
		 if(proDepAttachments.length>0){
			 $("#btn_clearProDepFiles").show();
			 for(var i=0;i<proDepAttachments.length;i++){
				 dataSource.add({attachments:proDepAttachments[i]});
			 }
		 }
		 $("#proDepAttachmentsListView").kendoListView({
			 dataSource: dataSource,
			 template:kendo.template($("#uploadProDepFilesTemplate").html()),
		 });
	};
})(jQuery);