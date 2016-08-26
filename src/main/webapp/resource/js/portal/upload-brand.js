$(function(){
	// 依赖jquery.js|common.js
	(function(){
		var portal = fsn.portal = fsn.portal || {};
		var	upload = portal.upload = portal.upload || {};
		portal.aryLogAttachments = new Array();
		portal.isNew = true;
		portal.edit_id = null;
		var _HTTP_PREFIX = fsn.getHttpPrefix();
		
		try {
			portal.edit_id = $.cookie("user_0_edit_brand").id;
			$.cookie("user_0_edit_brand", JSON.stringify({}), {
				path : '/'
			});
		} catch (e) {
		}
		
		// BASE
		(function(){
			var BusinessBrand = (function(){
				function _fn(){}
				// 用这个来缓存数据，如果是修改brand的话，在页面加载的时候就会将brand原始数据载入到这里
				_fn.cacheModel = null;
				
				_fn.prototype.Url = (function (todo) {
					var url = {"brand":{},"unit":{}}; // 这里添加模块
					
					url.brand.create = _HTTP_PREFIX+"/portal/upload/brand";
					url.brand.update = _HTTP_PREFIX+"/portal/upload/brand";
					url.brand.search = _HTTP_PREFIX+"/portal/upload/preload";
					url.brand.uploadimg = _HTTP_PREFIX+"/portal/file/img";
					url.brand.sugunit = _HTTP_PREFIX+"/portal/upload/unit";
					
					return url;
				}());
				_fn.prototype.Brand = function(id, name, unitid, unitname,
						trademark, date) {
					return {
						"id" : id,
						"name" : name,
						"unitid" : unitid,
						"unitname" : unitname,
						"trademark" : trademark,
						"date" : date
					};
				};
				
				// 在bean中放置实体构造器
				_fn.prototype.model = {};
				
				// 企业
				_fn.prototype.model.Unit = function (id, name, address, address2,
					logo, website, type, category, qsNo, licenseNo, contact,
					note) {
					return {
						"id" : id,
						"name" : name,
						"address" : address,
						"address2" : address2,
						"logo" : logo,
						"website" : website,
						"type" : type,
						"category" : category,
						"qsNo" : qsNo,
						"licenseNo" : licenseNo,
						"contact" : contact,
						"note" : note
					};
				};
				// 企业类型
				_fn.prototype.model.Type = function (id,code,name) {
					return {"id":id, "code": code, "name": name};
				};
				// 企业分类
				_fn.prototype.model.Category = function (id,code,name) {
					return {"id":id, "code": code, "name": name};
				};
				// 品牌
				_fn.prototype.model.Brand = function(id, name, identity, symbol,
						logo, trademark, cobrand, date, unit) {
					return {
						"id" : id,
						"name" : name,
						"identity" : identity,
						"symbol" : symbol,
						"logo" : logo,
						"trademark" : trademark,
						"cobrand" : cobrand,
						"registrationDate" : date,
						"businessUnit" : unit
					};
				};
				
				// 创建一个Brand对象
				_fn.prototype.createBrand = function () {
					var brandCategory = {
						id: $("#categoryGroupId").val().trim(),
						name: $("#categoryGroupName").val().trim(),
					};
					var businessBrand = {
			    		id: $("#id").val().trim(),
			    		name: $("#name").val().trim(),
			    		registrationDate: $("#date").val().trim(),
			    		businessUnit: null,
			    		logAttachments: portal.aryLogAttachments,
			    		brandCategory: brandCategory,
			    	};
					return businessBrand;
				};
				
				_fn.prototype.writeBusinessBrand = function (data) {
					var mws = this, _brand = data;
					mws.cacheModel = _brand; // 将数据缓存到_fn 的 cacheModel中
					if (!portal.isBlank(_brand)) {   // 编辑状态
						$("#id").val(_brand.id);
						$("#name").val(_brand.name);
						$("#date").data("kendoDatePicker").value(_brand.registrationDate);
						if(_brand.brandCategory != null){
							$("#categoryGroupId").val(_brand.brandCategory.id);
							$("#categoryGroupName").val(_brand.brandCategory.name);
						}
						// 商标图片
						_fn.setLogAttachments(_brand.logAttachments);
					}else{   // 新增状态
						_fn.setBusinessUnit();
					}
				};
				
				/* save */
				_fn.prototype.upload = function () {
					var mws = this;
					if(!upload.validate()){return false;}
						save_brand = mws.createBrand();
					if(!save_brand){
						return false;
					}
					$("#winMsg").html("正在保存数据，请稍候....");
					$("#k_window").data("kendoWindow").open().center();
					$.ajax({
						url: _HTTP_PREFIX + "/business/business-brand",
						type: portal.isNew ? "POST" : "PUT",
						datatype: "json",
						contentType: "application/json; charset=utf-8",
						data: JSON.stringify(save_brand),
						success: function(returnValue){
							$("#k_window").data("kendoWindow").close();
							if (returnValue.result.status == "true") {
				            	lims.initNotificationMes(portal.isNew ?'保存成功！':'更新成功！', true);
				            	oldName = returnValue.data.name;
				            	if(portal.isNew){
				            		$("#id").val(returnValue.data.id);
				            		portal.isNew = false;
				            		$("#update").show();
				          			$("#save").hide();
				          			$("#clear").hide();
				            	}
				            	_fn.setLogAttachments(returnValue.data.logAttachments);
				            	$("ul.k-upload-files").remove();
				            }else{
				            	lims.initNotificationMes('保存失败！'+returnValue.result.errorMessage, false);
				            }
						}
					});
					
				};
				
				/**
				 * 重置表单
				 */
				_fn.prototype.reset = function () {
					$("input").each(function(){
						this.value = this.defaultValue;
					});
					this.cacheModel = null;
					$("#imgForm").attr("imgurl", "")
					.siblings("img.upload-img-preview").attr("src","").css("display","none")
					.siblings("span.upload-img-name").find("em").text("图片名称：请选择图片");
					//清空提示框信息
					$("#success_msg").html("");
					$("#success_msg").attr("style","");
					//清空商标图片
					if(portal.aryLogAttachments.length>0){
						portal.aryLogAttachments.length=0;
						$("ul.k-upload-files").remove();
						$("#logAttachmentsListView").data("kendoListView").dataSource.data([]);
						$("#logListView").hide();
					}
				};
				
				/**
				 * 修改页面数据展示
				 */
				_fn.prototype.show = function (sid) {
					var mws = this;
					if (sid) {
						portal.isNew = false;
						$.ajax({
							url:_HTTP_PREFIX + "/business/business-brand/" + sid,
							type:"GET",
							datatype: "json",
							success: function (returnValue) {
								if(returnValue.result.status == "true") {
									mws.writeBusinessBrand(returnValue.data);
								}
							}
						});
					}
					if(portal.isNew){
						 $("#update").hide();
					}else{
						 $("#save").hide();
						 $("#clear").hide();
					}
				};
				/* 初始化控件 */
				_fn.prototype.initComponent = function(){
					_fn.buildUpload("upload_brandPhoto_btn",portal.aryLogAttachments,"brandEroMsg", "brand");
					$("#btn_clearLogFiles").bind("click",_fn.clearLogFiles);
				    $("#logListView").hide();
					$("#date").kendoDatePicker({
				    	 format: "yyyy-MM-dd", 
				    	 height:30,
				    	 culture : "zh-CN",
				    	 animation: {
				    	   close: {
				    	     effects: "fadeOut zoom:out",
				    	     duration: 300
				    	   },
				    	   open: {
				    	     effects: "fadeIn zoom:in",
				    	     duration: 300
				    	   }
				    	  }
				    });
					$("#date_format").kendoDatePicker({
				    	 format: "yyyy-MM-dd", 
				    	 height:30,
				    	 culture : "zh-CN",
				    	 animation: {
				    	   close: {
				    	     effects: "fadeOut zoom:out",
				    	     duration: 300
				    	   },
				    	   open: {
				    	     effects: "fadeIn zoom:in",
				    	     duration: 300
				    	   }
				    	  }
				    });
					/* 品牌类型选择按钮点击事件 */
					$("#categoryGrouBtn").click(function(event){
						brandCategoryTreeWin("brandCategoryGroupWin", "categoryGroupName", "categoryGroupId", "brand");
						event.preventDefault();
					});
				};
				
				_fn.prototype.init = function () {
					var mws = this;
					mws.initComponent();
					_fn.initBusUnit();
					portal.initKendoWindow("k_window","保存状态","300px","60px",false);
					// 初始化页面Brand数据 (在url中如果有id键，则是编辑)
					mws.show(portal.edit_id);
					
					// save事件\reset事件
					$(".k-button").on("click", function(){
						var _this = $(this), _span = _this.find("span.ui-button-text");
						switch(_span.attr("data-fsn-text")) {
						case 'Save':
							mws.upload();
							break;
						case 'Update':
							mws.upload();
							break;
						case 'Reset':
							mws.reset();
							break;
						default: break;
						}
					});
				};
				
				_fn.buildUpload = function(id, attachments, msg, flag){
			    	 $("#"+id).kendoUpload({
			        	 async: {
			                 saveUrl: _HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
			                 removeUrl: _HTTP_PREFIX + "/resource/kendoUI/removeResources",
			                 autoUpload: true,
			                 removeVerb:"POST",
			                 removeField:"fileNames",
			                 saveField:"attachments",
			        	 },localization: {
			                 select:lims.l("upload_brandPhoto"),
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
			             success: function(e){
			            	 if(e.response.typeEror){
			            		 if(msg=="brandEroMsg"){
			            			 $("#"+msg).html(lims.l("Wrong file type, the file will not be saved and re-upload png、bmp、jpeg format Please delete!")); 
			            		 }
			            		 return;
			            	 }
			            	 if(e.response.morSize){
			            		 if(msg=="brandEroMsg"){
			            			 $("#"+msg).html(lims.l("The file you uploaded more than 10M, re-upload the file in png、bmp、jpeg format Please delete!")); 
			            		 }
			            		 return;
			            	 }
			                 if (e.operation == "upload") {
			                     lims.log("upload");
			                     lims.log(e.response);
			                     attachments.push(e.response.results[0]);
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
			            	 if(msg=="brandEroMsg"){
			            		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
			            	 }
			             },
			             error:function(e){
			            	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
			             },
			         });
			     };
				
				 _fn.setLogAttachments = function(logAttachments){
					 var dataSource = new kendo.data.DataSource();
					 portal.aryLogAttachments.length=0;
					 if(logAttachments.length>0){
						 $("#logListView").show();
						 for(var i=0;i<logAttachments.length;i++){
							 portal.aryLogAttachments.push(logAttachments[i]);
							 dataSource.add({attachments:logAttachments[i]});
						 }
					 }
					
					 $("#logAttachmentsListView").kendoListView({
			             dataSource: dataSource, 	
			             template:kendo.template($("#uploadedFilesTemplate").html()),
			         });
				 };
				 
				 _fn.clearLogFiles = function(){     //清空报告资源
					 var listId=[];
					 var count = 0;
					 for(var i=0;i<portal.aryLogAttachments.length;i++){
					 	if(portal.aryLogAttachments[i].id != null){
					 		listId.push(portal.aryLogAttachments[i].id);
					 	}else{
							 count++;
						}
					 }
					 portal.aryLogAttachments.length=count;
					 $("#logAttachmentsListView").data("kendoListView").dataSource.data([]);
					 $("#logListView").hide();
				 };
				 
				 fsn.removeRes = function(resID){    //从页面删除
					 var dataSource = new kendo.data.DataSource();
					 for(var i=0; i<portal.aryLogAttachments.length; i++){
			        	 if(portal.aryLogAttachments[i].id == resID){
			        		 while((i+1)<portal.aryLogAttachments.length){
			        			 portal.aryLogAttachments[i] = portal.aryLogAttachments[i+1];
			        			 i++;
			        		 }
			        		 portal.aryLogAttachments.pop();
			        		 break;
			        	 }
			         }
					 
					 if(portal.aryLogAttachments.length>0){
						for(i=0; i<portal.aryLogAttachments.length; i++){
							dataSource.add({attachments:portal.aryLogAttachments[i]});
						}
					 }
					 $("#logAttachmentsListView").data("kendoListView").setDataSource(dataSource);
						if(portal.aryLogAttachments.length == 0){
							$("#logListView").hide();
					 }
				 };
				 
				 _fn.setBusinessUnit = function() {
					 $.ajax({
							url:_HTTP_PREFIX + "/business/business-brand/" + _id,
							type:"GET",
							datatype: "json",
							success: function (returnValue) {
								if(returnValue.result.status == "true") {
									mws.writeBusinessBrand(returnValue.data);
								}
							}
						});
				 };
				 
				 _fn.initBusUnit = function() {
					 $.ajax({
							url:_HTTP_PREFIX + "/business/getCurrentBusiness",
							type:"GET",
							datatype: "json",
							success: function (returnValue) {
								if(returnValue.result.status == "true") {
									portal.businessUnit = returnValue.data;
								}
							}
						});
				 };
				 
				 return _fn;
			})();
			this.BusinessBrand = new BusinessBrand();
		}).call(upload);
		
		fsn.setDateValue = function () {
			$("#date_format").data("kendoDatePicker").value($("#date").val());
		};
		
		upload.validate=function(){
			if(portal.businessUnit==null){
				lims.initNotificationMes("请先保存本企业的基本信息！", false);
				return false;
			}
			var validate_name = $("#name").kendoValidator().data("kendoValidator").validate();
   		 	var validate_date = $("#date").kendoValidator().data("kendoValidator").validate();
   		 	if(!validate_name){
   		 		lims.initNotificationMes("品牌名称必须填写！", false);
   				return false;
   		 	}
   		 	var categoryName = $("#categoryGroupName").val();
   		 	if(categoryName == ""){
   		 		lims.initNotificationMes("请选择品牌类型！", false);
   		 		return false;
   		 	}
   		 	if(!validate_date){
   		 		lims.initNotificationMes("注册日期必须填写！", false);
				return false;
		 	}
            //fsn.setDateValue();
   		 	var regDate = $("#date").data("kendoDatePicker").value();
   		 	if(regDate == null){
   		 		lims.initNotificationMes("注册日期格式不正确，请重新填写！", false);
   		 		return false;
   		 	}
	        var re1 = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	        var date = $("#date").val();
	        if (!date.match(re1)) {
	        	lims.initNotificationMes(lims.l("Registration time format is incorrect"), false);
	        	return false;
	         }
	        return true;
		};
		
		portal.initKendoWindow=function(winId,title,width,height,isVisible){
	    	$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:["Close"]
			});
	 };
		
	 upload.BusinessBrand.init();
	})();
});