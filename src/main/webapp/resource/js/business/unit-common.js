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
                saveField:"attachments"
       	 },localization: {
                select: business_unit.getShowName(id),
                retry:lims.l("retry",'upload'),
                remove:lims.l("remove",'upload'),
                cancel:lims.l("cancel",'upload'),
                dropFilesHere: lims.l("drop files here to upload",'upload'),
                statusFailed: lims.l("failed",'upload'),
                statusUploaded: lims.l("uploaded",'upload'),
                statusUploading: lims.l("uploading",'upload'),
                uploadSelectedFiles: lims.l("Upload files",'upload')
            },
            multiple:(id == "upload_qr_files") ? false : true,
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
                if(id=="upload_qr_files"){
                	attachments.length = 0;
				}
                attachments.push(e.response.results[0]);
				 var imgData =attachments;
                if(id=="upload_propaganda_files"){
                	if(attachments.length>=3){
                		$("#upload_propaganda_files").attr("disabled","disabled");
                		$("#upload_propaganda_div div.k-upload-button").addClass("k-state-disabled");
                		if(attachments.length>3){
                			lims.initNotificationMes('宣传照最多能传3张，请删掉多余的！',false);
                		}
                	}
					if(attachments.length ==3){
						$("#upload_propaganda_files_img_e").hide();
					}else{
						$("#upload_propaganda_files_img_e").show();
						$("#upload_propaganda_files_img_s").show();

						var div = document.getElementById("upload_propaganda_div");
						while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
							div.removeChild(div.firstChild);
						}

					}

					var img ="";
					for(var k in imgData){
						img+="<div id='upload_propaganda_files_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
						img+="<img id='upload_propaganda_files"+k+"' src='data:"+imgData[k].type.rtName+";base64,"+imgData[k].file+"' style='width: 128px;height:128px;' onclick='fsn.business_unit.amplification(this.src)'>";
						img+="<div class=deleteBtn onclick=fsn.business_unit.delSelectPropagandaImg("+k+",'"+imgData[k].file+"')>x";
						img+="</div>";
						img+="</div>";
					}
					$("#upload_propaganda_files_img").html(img);
					$("#upload_propaganda_div ul").remove();
					$("#upload_propaganda_files").removeAttr("disabled");
					$("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");

					$("#upload_propaganda_div").html("<input id='upload_propaganda_files' type='file' />");
					business_unit.buildUpload("upload_propaganda_files", business_unit.aryPropagandaAttachments, "upload_propaganda_files_log");
                }else{
					/**
					 * 显示当时长传的照片
					 */
					$("#"+id+"_img").attr("src","data:"+e.response.results[0].type.rtName+";base64,"+e.response.results[0].file);
					$("#"+id+"_img_e").hide();
					$("#"+id+"_img_s").show();
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
                    if(id="upload_propaganda_files"){
                    	if(attachments.length<3){
                    		$("#upload_propaganda_files").removeAttr("disabled");
                        	$("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");
                    	}
                    } 
                }
            },
            remove:function(e){
           		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
            },
            error:function(e){
           	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            }
        });
    };
	/**
	 * 删除企业宣传照
	 */
	business_unit.delSelectPropagandaImg = function(k,url){
        $("#upload_propaganda_files_img_"+k).hide();
		$("#upload_propaganda_files_img_e").show();
		if(business_unit.aryPropagandaAttachments != null&&business_unit.aryPropagandaAttachments){
			for(var i in business_unit.aryPropagandaAttachments){
				if((business_unit.aryPropagandaAttachments[i].file == null && business_unit.aryPropagandaAttachments[i].url==url) || (business_unit.aryPropagandaAttachments[i].url==null && business_unit.aryPropagandaAttachments[i].file == url)){
					business_unit.aryPropagandaAttachments.pop(i);
					//business_unit.aryPropagandaAttachments.remove(business_unit.aryPropagandaAttachments[i]) ;
				}

			}
		}
		var div = document.getElementById("upload_propaganda_div");
		while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
			div.removeChild(div.firstChild);
		}
		$("#upload_propaganda_files").removeAttr("disabled");
		$("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");

		$("#upload_propaganda_div").html("<input id='upload_propaganda_files' type='file' />");
		business_unit.buildUpload("upload_propaganda_files", business_unit.aryPropagandaAttachments, "upload_propaganda_files_log");
       if(business_unit.aryPropagandaAttachments !=null&&business_unit.aryPropagandaAttachments.length==0){
		   $("#upload_propaganda_files_img_s").hide();
	   }
	};
    /* 按控件id初始化上传按钮的显示文字 */
    business_unit.getShowName = function(id){
    	if(id=="upload_orgnization_files"){
    		return lims.l("upload_OrgPhoto");
    	}else if(id=="upload_license_files"){
    		return lims.l("upload_LicensePhoto");
    	}else if(id=="upload_distribution_files"){
    		return lims.l("upload_DistributionPhoto");
    	}else if(id=="upload_qs_files"){
    		return lims.l("upload_QsPhoto");
    	}else if(id=="upload_logo_files"){
    		return lims.l("upload_LogoPhoto");
    	}else if(id=="upload_certification_files"){
    		return lims.l("upload_cert");
    	}else if(id=="upload_propaganda_files"){
			return lims.l("upload_propaganda");
		}else if(id=="upload_tax_files"){
			return lims.l("upload_tax");
		}else if(id=="upload_qr_files"){
			return lims.l("upload_qr");
		}else if(id=="upload_honorIcon_files"){
			return lims.l("upload_honorIcon");
		}
    };

    /* 从页面删除已有企业宣传照资源 */
    business_unit.removeProPaRes = function(url){

    	 var dataSource = new kendo.data.DataSource();
		 if(business_unit.aryPropagandaAttachments){
			 for(var i=0; i<business_unit.aryPropagandaAttachments.length; i++){
	        	 if(business_unit.aryPropagandaAttachments[i].url == url){
	        		 while((i+1)<business_unit.aryPropagandaAttachments.length){
	        			 business_unit.aryPropagandaAttachments[i] = business_unit.aryPropagandaAttachments[i+1];
	        			 i++;
	        		 }
	        		 business_unit.aryPropagandaAttachments.pop();
	        		 break;
	        	 }
	         }
		 }
		 if(business_unit.aryPropagandaAttachments.length<3){
     		$("#upload_propaganda_files").removeAttr("disabled");
         	$("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");
     	}
			 /* 2. 要删除的图片是企业宣传图片 */
			 if(business_unit.aryPropagandaAttachments.length>0){
				 for(i=0; i<business_unit.aryPropagandaAttachments.length; i++){
					 dataSource.add({attachments:business_unit.aryPropagandaAttachments[i]});
				 }
			 }
			 $("#propagandaAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			 if(business_unit.aryPropagandaAttachments.length == 0){
				 $("#btn_clearPropagandaFiles").hide();
			 }
		 
    };
    /* 从页面删除已有资源 */
    business_unit.removeRes = function(resID){
    	 var isOrgRes = false;
		 var dataSource = new kendo.data.DataSource();
		if(resID ==null){
			if(business_unit.aryOrgAttachments){
				for(var i=0; i<business_unit.aryOrgAttachments.length; i++){
					var parentEl = this.parentElement;
					while((i+1)<business_unit.aryOrgAttachments.length){
						business_unit.aryOrgAttachments[i] = business_unit.aryOrgAttachments[i+1];
						i++;
					}
					business_unit.aryOrgAttachments.pop();
					break;
				}
			}
		}
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
    /* 清空税务登记证  */
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

    /* 清空企业宣传照  */
	business_unit.clearPropagandaFiles = function(){
		 var listId=[];
		 var count = 0;
		 for(var i=0;i<business_unit.aryPropagandaAttachments.length;i++){
		 	if(business_unit.aryPropagandaAttachments[i].id != null){
		 		listId.push(business_unit.aryPropagandaAttachments[i].id);
		 	}else{
				 count++;
			}
		 }
		business_unit.aryPropagandaAttachments.length=count;
		 $("#propagandaAttachmentsListView").data("kendoListView").dataSource.data([]);
		 $("#btn_clearPropagandaFiles").hide();
		 $("#upload_propaganda_files").removeAttr("disabled");
	     $("#upload_propaganda_div div.k-upload-button").removeClass("k-state-disabled");
	};
	/* 清空企业二维码  */
	business_unit.clearQrFiles = function(){
		var listId=[];
		var count = 0;
		for(var i=0;i<business_unit.aryQrAttachments.length;i++){
			if(business_unit.aryQrAttachments[i].id != null){
				listId.push(business_unit.aryQrAttachments[i].id);
			}else{
				count++;
			}
		}
		business_unit.aryQrAttachments.length=count;
		$("#qrAttachmentsListView").data("kendoListView").dataSource.data([]);
		$("#btn_clearQrFiles").hide();
		$("#upload_qr_files").removeAttr("disabled");
    	$("#upload_qr_div div.k-upload-button").removeClass("k-state-disabled");
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
            template:kendo.template($("#uploadedFilesTemplate").html())
        });
		if(orgAttachments.length>0)  $("#orgAttachmentsListView button").hide();
		$("#upload_org_div").html("<input id='upload_orgnization_files' type='file' />");
	    business_unit.buildUpload("upload_orgnization_files",business_unit.aryOrgAttachments,"proFileMsgOrg", "product");
	 };

	/* 加载税务登记证 */
	business_unit.setTaxAttachments = function(taxAttachments){
		business_unit.aryTaxAttachments.length = 0;
		if(taxAttachments == null || taxAttachments.length < 1){
			$("#btn_clearTaxFiles").hide();
			return;
		}
		var dataSource = new kendo.data.DataSource();
		$("#btn_clearTaxFiles").show();
		for(var i=0;i<taxAttachments.length;i++){
			taxAttachments[i].file = null;
			business_unit.aryTaxAttachments.push(taxAttachments[i]);
			dataSource.add({attachments:taxAttachments[i]});
		}
		$("#taxAttachmentsListView").kendoListView({
			dataSource: dataSource,
			template:kendo.template($("#uploadedFilesTemplate").html())
		});
		$("#taxAttachmentsListView button").hide();
		$("#upload_tax_div").html("<input id='upload_tax_files' type='file' />");
		business_unit.buildUpload("upload_tax_files",business_unit.aryTaxAttachments,"proFileMsgTax", "product");
	};

	 /* 加载logo */
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
	            template:kendo.template($("#uploadedFilesTemplate").html())
			});
			$("#upload_logo_div").html("<input id='upload_logo_files' type='file' />");
	    	business_unit.buildUpload("upload_logo_files",business_unit.aryLogoAttachments,"proFileMsgLogo", "product");
	 };

	/* 设置宣传照
	* HY*/
	business_unit.setPropagandaAttachments = function(propagAttachments){
		business_unit.aryPropagandaAttachments.length = 0;
		if(propagAttachments == null || propagAttachments.length < 1){
			$("#btn_clearPropagandaFiles").hide();
			return ;
		}
		var dataSource = new kendo.data.DataSource();
		$("#upload_propaganda_files").attr("disabled","disabled");
		$("#btn_clearPropagandaFiles").show();
		for(var i=0;i<propagAttachments.length;i++){
			propagAttachments[i].file = null;
			business_unit.aryPropagandaAttachments.push(propagAttachments[i]);
			dataSource.add({attachments:propagAttachments[i]});
		}
		$("#propagandaAttachmentsListView").kendoListView({
			dataSource: dataSource,
			template:kendo.template($("#uploadedProPaFilesTemplate").html())
		});
		/*$("#propagandaAttachmentsListView button").hide();*/
		$("#upload_propaganda_div").html("<input id='upload_propaganda_files' type='file' />");
		business_unit.buildUpload("upload_propaganda_files",business_unit.aryPropagandaAttachments,"proFileMsgPropaganda", "product");
	};

	/* 设置二维码
	* HY*/
	business_unit.setQrAttachments = function(qrAttachments){
		business_unit.aryQrAttachments.length = 0;
		if(qrAttachments == null || qrAttachments.length < 1) {
			$("#btn_clearQrFiles").hide();
			return;
		}
		var dataSource = new kendo.data.DataSource();
		$("#btn_clearQrFiles").show();
		for(var i=0;i<qrAttachments.length;i++){
			qrAttachments[i].file = null;
			business_unit.aryQrAttachments.push(qrAttachments[i]);
			dataSource.add({attachments:qrAttachments[i]});

		}
		$("#qrAttachmentsListView").kendoListView({
			dataSource: dataSource,
			template:kendo.template($("#uploadedFilesTemplate").html())
		});
		$("#qrAttachmentsListView button").hide();
		$("#upload_qr_div").html("<input id='upload_qr_files' type='file' />");
		business_unit.buildUpload("upload_qr_files",business_unit.aryQrAttachments,"proFileMsgQr", "product");
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
            template:kendo.template($("#uploadedFilesTemplate").html())
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
           template:kendo.template($("#uploadedFilesTemplate").html())
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
			 template:kendo.template($("#uploadedFilesTemplate").html())
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

		$("#name_1").text((data.name).replace(/(^\s*)|(\s*$)/g, ""));
    	$("#name").val((data.name).replace(/(^\s*)|(\s*$)/g, ""));

        $("#personInCharge_1").text(data.personInCharge);
        $("#personInCharge").val(data.personInCharge);

        $("#contact_1").text(data.contact);
        $("#contact").val(data.contact);

		$("#telephone_1").text(data.telephone);
        $("#telephone").val(data.telephone);

        $("#postalCode_1").text(data.postalCode);
        $("#postalCode").val(data.postalCode);

		$("#email_1").text(data.email);
		$("#email").val(data.email);

		$("#fax_1").text(data.fax);
        $("#fax").val(data.fax);

		$("#website_1").text(data.website);
		$("#product_about").val(data.about);


		$("#product_about_1").text(data.about);
		$("#website").val(data.website);

		/**
		 * 税务登记证（销售系统）
		 * @author HuangYong 2015/5/10
		 */
		if(data.taxRegister!=null ){
			$("#taxName_1").text(data.taxRegister.taxerName);
			$("#taxName").val(data.taxRegister.taxerName);
			$("#taxId").val(data.taxRegister.id);
		}


        setAddressValue(data.address,data.otherAddress,"bus_mainAddr","bus_streetAddress");
		setAddressValueWithHtmlType(data.address,data.otherAddress,"bus_mainAddr_1","bus_streetAddress_1","text")

        if(data.license != null){
        	$("#licenseNo").val(data.license.licenseNo);
            $("#licenseName").val(data.license.licensename);
            $("#legalName").val(data.license.legalName);
            $("#licenseStartTime").data("kendoDatePicker").value(data.license.startTime!=null?data.license.startTime.substr(0, 10):"");
            $("#licenseEndTime").data("kendoDatePicker").value(data.license.endTime!=null?data.license.endTime.substr(0, 10):"");
            $("#registrationTime").data("kendoDatePicker").value(data.license.registrationTime!=null?data.license.registrationTime.substr(0, 10):"");
            $("#issuingAuthority").val(data.license.issuingAuthority);
            $("#subjectType").val(data.license.subjectType);
			setAddressValue(data.license.businessAddress,data.license.otherAddress,"license_mainAddr","license_streetAddress");


            $("#licenseNo_1").text(data.license.licenseNo);
            $("#licenseName_1").text(data.license.licensename);
            $("#legalName_1").text(data.license.legalName);
            $("#licenseStartTime_1").text(data.license.startTime!=null?data.license.startTime.substr(0, 10):"");
            $("#licenseEndTime_1").text(data.license.endTime!=null?data.license.endTime.substr(0, 10):"");
            $("#registrationTime_1").text(data.license.registrationTime!=null?data.license.registrationTime.substr(0, 10):"");
            $("#issuingAuthority_1").text(data.license.issuingAuthority);
			$("#subjectType_1").text(data.license.subjectType);
			setAddressValueWithHtmlType(data.license.businessAddress,data.license.otherAddress,"license_mainAddr_1","license_streetAddress_1","text");



            $("#toleranceRange").val(data.license.toleranceRange);
            $("#registeredCapital").val(data.license.registeredCapital);

            $("#toleranceRange_1").text(data.license.toleranceRange);
            $("#registeredCapital_1").text(data.license.registeredCapital);
            if(data.license.licenseNo != null && data.license.licenseNo != ""){
            	$("#licenseNo").attr("readonly", "readonly");
            }
        }
        if(data.orgInstitution != null){
        	$("#orgCode").val(data.orgInstitution.orgCode);
            $("#orgStartTime").data("kendoDatePicker").value(data.orgInstitution.startTime!=null?data.orgInstitution.startTime.substr(0, 10):"");
            $("#orgEndTime").data("kendoDatePicker").value(data.orgInstitution.endTime!=null?data.orgInstitution.endTime.substr(0, 10):"");
            $("#orgName").val(data.orgInstitution.orgName);
            $("#unitsAwarded").val(data.orgInstitution.unitsAwarded);
            $("#orgType").val(data.orgInstitution.orgType);
			setAddressValue(data.orgInstitution.orgAddress,data.orgInstitution.otherAddress,"org_mainAddr","org_streetAddress");

			$("#orgCode_1").text(data.orgInstitution.orgCode);
            $("#orgStartTime_1").text(data.orgInstitution.startTime!=null?data.orgInstitution.startTime.substr(0, 10):"");
            $("#orgEndTime_1").text(data.orgInstitution.endTime!=null?data.orgInstitution.endTime.substr(0, 10):"");
            $("#orgName_1").text(data.orgInstitution.orgName);
            $("#unitsAwarded_1").text(data.orgInstitution.unitsAwarded);
            $("#orgType_1").text(data.orgInstitution.orgType);


			setAddressValueWithHtmlType(data.orgInstitution.orgAddress,data.orgInstitution.otherAddress,"org_mainAddr_1","org_streetAddress_1","text");

            if(data.orgInstitution.orgCode != null && data.orgInstitution.orgCode != ""){
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
		if(data.propagandaAttachments != null&&data.propagandaAttachments.length>0){
			business_unit.setPropagandaAttachments(data.propagandaAttachments);
		}else{$("#btn_clearPropagandaFiles").hide();}
		if(data.qrAttachments != null&&data.qrAttachments.length>0){
			business_unit.setQrAttachments(data.qrAttachments);
		}else{$("#btn_clearQrFiles").hide();}
		if(data.taxRegister != null && data.taxRegister.taxAttachments!=null&&data.taxRegister.taxAttachments.length>0){
			business_unit.setTaxAttachments(data.taxRegister.taxAttachments);
		}else{
			$("#btn_clearTaxFiles").hide();
		}
        if(data.orgAttachments != null&&data.orgAttachments.length>0){
        	business_unit.setOrgAttachments(data.orgAttachments);
        }
        if(data.licAttachments != null&& data.licAttachments.length>0){
        	business_unit.setLicenseAttachments(data.licAttachments);
        }
        if(flag == "liutong" && data.district!=null){
 			for(var i=0;i<data.district.length;i++){
 				var description = data.district[i].description;
 				var discrId = data.district[i].id;
 				if(description=="主体性质"){
 					$("#liuTongBusType").data("kendoDropDownList").value(discrId);
 					continue;
 				}
 			}
        }
        if(flag == "liutong" && data.distribution!=null){
        	$("#distributionNo").val(data.distribution.distributionNo);
        	$("#licensingAuthority").val(data.distribution.licensingAuthority);
        	$("#distributionName").val(data.distribution.licenseName);
        	$("#disStartTime").data("kendoDatePicker").value(data.distribution.startTime!=null?data.distribution.startTime.substr(0, 10):"");
        	$("#disEndTime").data("kendoDatePicker").value(data.distribution.endTime!=null?data.distribution.endTime.substr(0, 10):"");
        	$("#disSubjectType").val(data.distribution.subjectType);
			setAddressValue(data.distribution.businessAddress,data.distribution.otherAddress,"dis_mainAddr","dis_streetAddress");

        	$("#distributionNo_1").text(data.distribution.distributionNo);
        	$("#licensingAuthority_1").text(data.distribution.licensingAuthority);
        	$("#distributionName_1").text(data.distribution.licenseName);
        	$("#disStartTime_1").text(data.distribution.startTime!=null?data.distribution.startTime.substr(0, 10):"");
        	$("#disEndTime_1").text(data.distribution.endTime!=null?data.distribution.endTime.substr(0, 10):"");
        	$("#disSubjectType_1").text(data.distribution.subjectType);


			setAddressValueWithHtmlType(data.distribution.businessAddress,data.distribution.otherAddress,"dis_mainAddr_1","dis_streetAddress_1","text");

			$("#disToleranceRange").val(data.distribution.toleranceRange);
        	$("#disLegalName").val(data.distribution.legalName);

			$("#disToleranceRange_1").text(data.distribution.toleranceRange);
        	$("#disLegalName_1").text(data.distribution.legalName);
        	if(data.disAttachments != null){
            	business_unit.setDisAttachments(data.disAttachments);
            }
        }
		/**
		 * 图片赋值
		 */
		business_unit.setImgResource(data);
    };
	//==============================================================================================================================================
	//==============================================================================================================================================
	//==============================================================================================================================================
	business_unit.setImgResource = function(data) {
		if (data.logoAttachments != null) {
			$("#upload_logo_files_img_a").attr("src", data.logoAttachments[0].url);
			$("#upload_logo_files_img").attr("src", data.logoAttachments[0].url);
			$("#upload_logo_files_img_e").hide();
			$("#upload_logo_files_img_s").show();
		} else {
			$("#upload_logo_files_img_e").show();
			$("#upload_logo_files_img_s").hide();
		}
		if (data.licAttachments != null && data.licAttachments.length > 0) {
			$("#upload_license_files_img_a").attr("src", data.licAttachments[0].url);
			$("#upload_license_files_img").attr("src", data.licAttachments[0].url);


			$("#upload_license_files_img_e").hide();
			$("#upload_license_files_img_s").show();
		} else {
			$("#upload_license_files_img_e").show();
			$("#upload_license_files_img_s").hide();
		}
		if (data.orgAttachments != null && data.orgAttachments.length > 0) {
			$("#upload_orgnization_files_img_a").attr("src", data.orgAttachments[0].url);
			$("#upload_orgnization_files_img").attr("src", data.orgAttachments[0].url);

			$("#upload_orgnization_files_img_e").hide();
			$("#upload_orgnization_files_img_s").show();
		} else {
			$("#upload_orgnization_files_img_e").show();
			$("#upload_orgnization_files_img_s").hied();
		}
		if (data.taxRegAttachments != null && data.taxRegAttachments.length > 0) {
			$("#upload_tax_files_img_a").attr("src", data.taxRegAttachments[0].url);
			$("#upload_tax_files_img").attr("src", data.taxRegAttachments[0].url);

			$("#upload_tax_files_img_e").hide();
			$("#upload_tax_files_img_s").show();
		} else {
			$("#upload_tax_files_img_e").show();
			$("#upload_tax_files_img_s").show();
		}
		if (data.qrAttachments != null && data.qrAttachments.length > 0) {
			$("#upload_qr_files_img_a").attr("src", data.qrAttachments[0].url);
			$("#upload_qr_files_img").attr("src", data.qrAttachments[0].url);

			$("#upload_qr_files_img_e").hide();
			$("#upload_qr_files_img_s").show();
		} else {
			$("#upload_qr_files_img_e").show();
			$("#upload_qr_files_img_s").hide();
		}
		//=====================================================================


		if(data.propagandaAttachments != null&&data.propagandaAttachments.length>0){
			var imgData =data.propagandaAttachments;
			var img_a = "";
			for(var i in imgData){
				img_a+="<div style='float: left;margin-left:10px;'><img id='show_license_img"+i+"' src='"+imgData[i].url+"' style='width: 128px;height:128px;' style='display:block;' onclick='fsn.business_unit.amplification(this.src)'></div>"
			}
			$("#upload_propaganda_files_img_a").html(img_a);

			var img ="";
			for(var k in imgData){
				img+="<div id='upload_propaganda_files_img_"+k+"'  style='position: relative;width: 128px;height: 128px;float: left;display: inline'>";
				img+="<img id='upload_propaganda_files"+k+"' src='"+imgData[k].url+"' style='width: 128px;height:128px;' onclick='fsn.business_unit.amplification(this.src)'>";
				img+="<div class=deleteBtn onclick=fsn.business_unit.delSelectPropagandaImg("+k+",'"+imgData[k].url+"')>x";
				img+="</div>";
				img+="</div>";
			}

			if(imgData.length>=3){
				$("#upload_propaganda_files_img_e").hide();
			}
			$("#upload_propaganda_files_img_s").show();
			$("#upload_propaganda_files_img").html(img);
		}else{
			$("#upload_propaganda_files_img_a").html("<font color='red'>未上传企业宣传照</font>");
			$("#upload_propaganda_files_img_s").hide();
		}
		//for(var k=0;k<3;k++){
		//	img+="<div style='float: left;margin-left:10px;'><img id='show_license_img"+k+"' src='"+data.taxRegAttachments[0].url+"' width='128' height='128' style='display:block;'></div>"
		//
		//}
		//=====================================================================
	}
	business_unit.delSelectImg = function(id) {
			$("#" + id + "_img_e").show();
			$("#" + id + "_img_s").hide();
			if(id == 'upload_logo_files'){
				if(business_unit.logoAttachments != null && business_unit.logoAttachments.length>0){
					business_unit.logoAttachments.pop();
				}
				var div = document.getElementById("upload_logo_div"); //$("#upload_logo_div");
				while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
					div.removeChild(div.firstChild);
				}

				/* 初始化上传控件 */
				$("#upload_logo_div").html("<input id='"+id+"' type='file' />");
				business_unit.buildUpload(id, business_unit.aryLogoAttachments, id+"_log");
			}else if(id == 'upload_license_files' ){
				if(business_unit.licAttachments != null && business_unit.licAttachments.length>0){
					business_unit.licAttachments.pop();
				}

				var div = document.getElementById("upload_license_div"); //$("#upload_logo_div");
				while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
					div.removeChild(div.firstChild);
				}
				/* 初始化上传控件 */
				$("#upload_license_div").html("<input id='"+id+"' type='file' />");
				business_unit.buildUpload(id, business_unit.aryLogoAttachments, id+"_log");
			}else if(id == 'upload_orgnization_files'){
				if(business_unit.orgAttachments != null && business_unit.orgAttachments.length>0){
					business_unit.orgAttachments.pop();
				}

				var div = document.getElementById("upload_org_div"); //$("#upload_logo_div");
				while(div.hasChildNodes()) {//当div下还存在子节点时 循环继续
					div.removeChild(div.firstChild);
				}
				/* 初始化上传控件 */
				$("#upload_org_div").html("<input id='"+id+"' type='file' />");
				business_unit.buildUpload(id, business_unit.aryLogoAttachments, id+"_log");
			}else if(id == 'upload_tax_files'){
				if(business_unit.taxRegAttachments != null && business_unit.taxRegAttachments.length>0){
					business_unit.taxRegAttachments.pop();
				}

				var div = document.getElementById("upload_tax_div"); //$("#upload_logo_div");
				while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
					div.removeChild(div.firstChild);
				}
				/* 初始化上传控件 */
				$("#upload_tax_div").html("<input id='"+id+"' type='file' />");
				business_unit.buildUpload(id, business_unit.aryLogoAttachments, id+"_log");

			}else if(id == 'upload_qr_files'){
				if(business_unit.qrAttachments != null && business_unit.qrAttachments.length>0){
					business_unit.qrAttachments.pop();
				}
				var div = document.getElementById("upload_qr_div"); //$("#upload_logo_div");
				while(div.hasChildNodes()){ //当div下还存在子节点时 循环继续
					div.removeChild(div.firstChild);
				}

				/* 初始化上传控件 */
				$("#upload_qr_div").html("<input id='"+id+"' type='file' />");
				business_unit.buildUpload(id, business_unit.aryLogoAttachments, id+"_log");
			}


		};
		business_unit.amplification = function(url){
			//window.location.href = url;
			window.open ( url, "_blank" );
		};

	//==============================================================================================================================================
	//==============================================================================================================================================
	//==============================================================================================================================================
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
//      if(!business_unit.verificationName()){
//        	lims.initNotificationMes('企业名称无效！',false);
//        	return false;
//      }
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
	};
})(jQuery);