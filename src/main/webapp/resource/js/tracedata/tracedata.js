$(document).ready(function(){
	var portal = fsn.portal = fsn.portal || {};
	var root = window.lims.root = window.lims.root || {};
	var upload = fsn.upload = fsn.upload || {};
	root.aryRepAttachments=[];
	root.aryGeAttachments=[];
	root.aryByAttachments=[];
	root.aryBusinessAttachments=[];
	root.initialize=function(){
		$("#productID").kendoDropDownList({
			dataSource:{
				transport: {
					read: {
						url: fsn.getHttpPrefix()+"/product/getAllProductsByOrg",
						dataType: "json"
					}
				},
				schema: {
					data: function(response) {
						return response.productList; 
					}
				}
			},
			dataTextField: "name",
			dataValueField: "id"
		});
		$("#sourceDate,#warehouseDate,#productDate,#leaveDate").kendoDatePicker({
			format: "yyyy-MM-dd",
			height:30,
			max: new Date(),
			culture : "zh-CN"
		});
		/* 初始化上传控件  */
		$("#upload-other-img").kendoUpload({
			async: {
				saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
				removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
				autoUpload: true,
				removeVerb:"POST",
				removeField:"fileNames",
				saveField:"attachments"
			},localization: {
				select: "上传原材料证明",
				retry:lims.l("retry",'upload'),
				remove:lims.l("remove",'upload'),
				cancel:lims.l("cancel",'upload'),
				dropFilesHere: lims.l("drop files here to upload",'upload'),
				statusFailed: lims.l("failed",'upload'),
				statusUploaded: lims.l("uploaded",'upload'),
				statusUploading: lims.l("uploading",'upload'),
				uploadSelectedFiles: lims.l("Upload files",'upload')
			},
			multiple:false,
					upload: function(e){
						var files = e.files;
						$.each(files, function () {
							if(this.size>1040000){
								lims.initNotificationMes('单个文件的大小不能超过1M！',false);
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
							lims.initNotificationMes("必须上传图片格式",false);
							return;
						}
						if(e.response.morSize){
							lims.initNotificationMes("上传材料大小必须小1M",false);
							return;
						}
						if (e.operation == "upload") {
							root.aryRepAttachments.push(e.response.results[0]);
							
							//$("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
						}else if(e.operation == "remove"){
							root.aryRepAttachments=[];
						}
					},
					remove:function(e){
						//$("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
					},
					error:function(e){
						//$("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
					}
		});
		/* 初始化上传控件 ge */
		$("#upload-ge-img").kendoUpload({
			async: {
				saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
				removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
				autoUpload: true,
				removeVerb:"POST",
				removeField:"fileNames",
				saveField:"attachments"
			},localization: {
				select: "上传生长环境图片",
				retry:lims.l("retry",'upload'),
				remove:lims.l("remove",'upload'),
				cancel:lims.l("cancel",'upload'),
				dropFilesHere: lims.l("drop files here to upload",'upload'),
				statusFailed: lims.l("failed",'upload'),
				statusUploaded: lims.l("uploaded",'upload'),
				statusUploading: lims.l("uploading",'upload'),
				uploadSelectedFiles: lims.l("Upload files",'upload')
			},
			multiple:false,
					upload: function(e){
						var files = e.files;
						$.each(files, function () {
							if(this.size>1040000){
								lims.initNotificationMes('单个文件的大小不能超过1M！',false);
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
							lims.initNotificationMes("必须上传图片格式",false);
							return;
						}
						if(e.response.morSize){
							lims.initNotificationMes("上传材料大小必须小1M",false);
							return;
						}
						if (e.operation == "upload") {
							root.aryGeAttachments.push(e.response.results[0]);
							
							//$("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
						}else if(e.operation == "remove"){
							root.aryGeAttachments=[];
						}
					},
					remove:function(e){
						//$("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
					},
					error:function(e){
						//$("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
					}
		});
		/* 初始化上传控件  */
		$("#upload-by-img").kendoUpload({
			async: {
				saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
				removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
				autoUpload: true,
				removeVerb:"POST",
				removeField:"fileNames",
				saveField:"attachments"
			},localization: {
				select: "上传购买二维码",
				retry:lims.l("retry",'upload'),
				remove:lims.l("remove",'upload'),
				cancel:lims.l("cancel",'upload'),
				dropFilesHere: lims.l("drop files here to upload",'upload'),
				statusFailed: lims.l("failed",'upload'),
				statusUploaded: lims.l("uploaded",'upload'),
				statusUploading: lims.l("uploading",'upload'),
				uploadSelectedFiles: lims.l("Upload files",'upload')
			},
			multiple:false,
					upload: function(e){
						var files = e.files;
						$.each(files, function () {
							if(this.size>1040000){
								lims.initNotificationMes('单个文件的大小不能超过1M！',false);
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
							lims.initNotificationMes("必须上传图片格式",false);
							return;
						}
						if(e.response.morSize){
							lims.initNotificationMes("上传材料大小必须小1M",false);
							return;
						}
						if (e.operation == "upload") {
							root.aryByAttachments.push(e.response.results[0]);
							
							//$("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
						}else if(e.operation == "remove"){
							root.aryByAttachments=[];
						}
					},
					remove:function(e){
						//$("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
					},
					error:function(e){
						//$("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
					}
		});
		/* 初始化上传控件，上传企业承诺图片  */
		$("#upload-business-img").kendoUpload({
			async: {
				saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
				removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
				autoUpload: true,
				removeVerb:"POST",
				removeField:"fileNames",
				saveField:"attachments"
			},localization: {
				select: "上传企业承诺图片",
				retry:lims.l("retry",'upload'),
				remove:lims.l("remove",'upload'),
				cancel:lims.l("cancel",'upload'),
				dropFilesHere: lims.l("drop files here to upload",'upload'),
				statusFailed: lims.l("failed",'upload'),
				statusUploaded: lims.l("uploaded",'upload'),
				statusUploading: lims.l("uploading",'upload'),
				uploadSelectedFiles: lims.l("Upload files",'upload')
			},
			multiple:false,
			upload: function(e){
				var files = e.files;
				$.each(files, function () {
					if(this.size>1040000){
						lims.initNotificationMes('单个文件的大小不能超过1M！',false);
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
					lims.initNotificationMes("必须上传图片格式",false);
					return;
				}
				if(e.response.morSize){
					lims.initNotificationMes("上传材料大小必须小1M",false);
					return;
				}
				if (e.operation == "upload") {
					root.aryBusinessAttachments.push(e.response.results[0]);
					
					//$("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
				}else if(e.operation == "remove"){
					root.aryBusinessAttachments=[];
				}
			},
			remove:function(e){
				//$("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
			},
			error:function(e){
				//$("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
			}
		});
		//upload.buildUpload("upload_product_files", portal.aryRepAttachments, "repFileMsg", "IMG");
		//business_unit.buildUpload("upload_qr_files",business_unit.aryRepAttachments,"repFileMsg", "product");
		//root.buildUpload("upload-other-img", root.aryRepAttachments, "repFileMsg","report");
		var id=getUrlParam("id");
		if(id!=null){
			$.get(fsn.getHttpPrefix()+"/traceData/getTraceDataById",{id:id},function(rs){
				$("#productID").data("kendoDropDownList").value(rs.traceData.productID);
				$("#sourceArea").val(rs.traceData.sourceArea);
				$("#sourceDate").val(fsn.formatGridDate(rs.traceData.sourceDate));
				$("#processor").val(rs.traceData.processor);
				$("#packagePlant").val(rs.traceData.packagePlant);
				$("#warehouseDate").val(fsn.formatGridDate(rs.traceData.warehouseDate));
				$("#productDate").val(fsn.formatGridDate(rs.traceData.productDate));
				$("#leaveDate").val(fsn.formatGridDate(rs.traceData.leaveDate));
				if(rs.traceData.sourceCertify){
					$("#repAttachmentsListView").show().find("a").attr("href",rs.traceData.sourceCertify.url);
					root.aryRepAttachments.push(rs.traceData.sourceCertify);
					$("#upload-other-img").data("kendoUpload").disable();
				}
				if(rs.traceData.growEnvironmentResource){
					$("#geAttachmentsListView").show().find("a").attr("href",rs.traceData.growEnvironmentResource.url);
					root.aryGeAttachments.push(rs.traceData.growEnvironmentResource);
					$("#upload-ge-img").data("kendoUpload").disable();
				}
				if(rs.traceData.buyLink){
					$("#byAttachmentsListView").show().find("a").attr("href",rs.traceData.buyLink.url);
					root.aryByAttachments.push(rs.traceData.buyLink);
					$("#upload-by-img").data("kendoUpload").disable();
				}
				if(rs.traceData.businessPromiseResource){
					$("#businessAttachmentsListView").show().find("a").attr("href",rs.traceData.businessPromiseResource.url);
					root.aryBusinessAttachments.push(rs.traceData.businessPromiseResource);
					$("#upload-business-img").data("kendoUpload").disable();
				}
				$("#save").find("span:last-child").html('更新');
			},'json');
		}
	};
	root.removeRes=function(){
		$("#repAttachmentsListView").remove();
		$("#upload-other-img").data("kendoUpload").enable();
		delete root.aryRepAttachments[0];
		root.aryRepAttachments.length=0;
	}
	root.removeGe=function(){
		$("#geAttachmentsListView").remove();
		$("#upload-ge-img").data("kendoUpload").enable();
		delete root.aryGeAttachments[0];
		root.aryGeAttachments.length=0;
	}
	root.removeBy=function(){
		$("#byAttachmentsListView").remove();
		$("#upload-by-img").data("kendoUpload").enable();
		delete root.aryByAttachments[0];
		root.aryByAttachments.length=0;
	}
	root.removeBusiness=function(){
		$("#businessAttachmentsListView").remove();
		$("#upload-business-img").data("kendoUpload").enable();
		delete root.aryBusinessAttachments[0];
		root.aryBusinessAttachments.length=0;
	};
	$("#save").click(function(){
		var data={};
		data.id=getUrlParam("id");
		data.productID=$("#productID").data("kendoDropDownList").value();
		data.sourceArea=$("#sourceArea").val();
		data.sourceDate=$("#sourceDate").val();
		data.processor=$("#processor").val();
		data.packagePlant=$("#packagePlant").val();
		data.warehouseDate=$("#warehouseDate").val();
		data.productDate=$("#productDate").val();
		data.leaveDate=$("#leaveDate").val();
		data.productName=$("#productID").data("kendoDropDownList").text();
		if(data.sourceArea==""){
			lims.initNotificationMes('原材料来源区域不能为空',false);
			return false;
		}
		if(data.sourceDate==""){
			lims.initNotificationMes('原材料入仓时间不能为空',false);
			return false;
		}
		if(data.productDate==""){
			lims.initNotificationMes('生产日期不能为空',false);
			return false;
		}
		if(data.processor==""){
			lims.initNotificationMes('加工者名称不能为空',false);
			return false;
		}
//		if(root.aryBusinessAttachments.length>0==0){
//			lims.initNotificationMes('请上传企业承诺图片',false);
//			return false;
//		}
		if(root.aryRepAttachments.length>0){
			data.sourceCertify=root.aryRepAttachments[0];
		}
		if(root.aryGeAttachments.length>0){
			data.growEnvironmentResource=root.aryGeAttachments[0];
		}
		if(root.aryByAttachments.length>0){
			data.buyLink=root.aryByAttachments[0];
		}
		if(root.aryBusinessAttachments.length>0){
			data.businessPromiseResource=root.aryBusinessAttachments[0];
		}
		dialog({
			id:"提交中",
			content:"保存中...,请稍等",
			width:350,
			modal:true
		}).show();
		$.ajax({
			url:fsn.getHttpPrefix()+"/traceData/save",
			type: "POST",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(data),
			success:function(rs){
				if(rs.status){
					location.href="list-tracedata.html";
				}else{
					lims.initNotificationMes('保存失败',false);
				}
			}
		});
		return false;
	});
	function getUrlParam(name){  
		//构造一个含有目标参数的正则表达式对象  
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		//匹配目标参数  
		var r = window.location.search.substr(1).match(reg);  
		//返回参数值  
		if (r!=null) return unescape(r[2]);  
		return null;  
	}  
	root.initialize();
});
