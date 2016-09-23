(function($) {
	var fsn = window.fsn = window.fsn || {};
	fsn.HTTP_PREFIX = fsn.getHttpPrefix();
	var saleskendoUtil = window.saleskendoUtil = window.saleskendoUtil || {};
		
	/* 初始化上传控件  */
	saleskendoUtil.buildUpload = function(formId, attachments, btnText, extension, isMultiple, successCallBackFun, delCallBackFun){
   	 $("#"+formId).kendoUpload({
       	 async: {
                saveUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/addSalesResources",
                removeUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments"
       	 },localization: {
                select: fsn.l(btnText),
                retry:fsn.l("retry",'upload'),
                remove:fsn.l("remove",'upload'),
                cancel:fsn.l("cancel",'upload'),
                dropFilesHere: fsn.l("drop files here to upload",'upload'),
                statusFailed: fsn.l("failed",'upload'),
                statusUploaded: fsn.l("uploaded",'upload'),
                statusUploading: fsn.l("uploading",'upload'),
                uploadSelectedFiles: fsn.l("Upload files",'upload')
            },
            multiple:isMultiple,
            upload: function(e){
                var files = e.files;
                 $.each(files, function () {
               	  if(this.size>5400000){
                   	  	 fsn.initNotificationMes('单个文件的大小不能超过5M！',false);
                         e.preventDefault();
                         return;
                  }
				  if((formId==="upload_certification_files" || formId==="upload_honorIcon_files") && this.name.length > 25){
					  fsn.initNotificationMes('文件名不能超过25个字符,请重命名文件！',false);
					  e.preventDefault();
					  return;
				  }
                  var extStr = this.extension.toLowerCase();
                  if(extStr != "" && extension != "" && extension.indexOf(extStr) == -1){
                	  fsn.initNotificationMes('文件格式不正确！',false);
                      e.preventDefault();
                  }
               });
            },
            success: function(e){
             if (e.operation == "upload") {
                if(formId == "uploadContractFile") {
                	if(attachments != null) attachments.length = 0;
                	if($("#contractName").val().trim().length == 0){
                		var fileName = e.response.results[0].fileName;
                		var index = fileName.lastIndexOf(".");
                		$("#contractName").val(fileName.substring(0,index));
                	}
                }
				if(formId==="upload_certification_files" || formId==="upload_honorIcon_files"){
					$("#upload_honorIcon_div div ul").removeClass("k-upload-files");
					$("#upload_honorIcon_div div strong").css("font-size","10px");
					$("#upload_honorIcon_div div strong").css("heigth","40px");
					$("#upload_honorIcon_div div ul strong").css("line-height","3.7em");

					$("#upload_certification_div div ul").removeClass("k-upload-files");
					$("#upload_certification_div div strong").css("font-size","10px");
					$("#upload_certification_div div strong").css("heigth","40px");
					$("#upload_certification_div div ul strong").css("line-height","3.7em");


				}
                if(attachments != null) {
					 attachments.push(e.response.results[0]);
				}
                /* 上传成功时的回掉函数 */
                if(successCallBackFun != null) {
                	successCallBackFun(e.response.results);
                }
             }else if(e.operation == "remove"){
				 	if(attachments != null){
						for(var i=0; i<attachments.length; i++){
							if(attachments[i].fileName == e.files[0].name){
								while((i+1)<attachments.length){
									attachments[i]=attachments[i+1];
									i++;
								}
								attachments.pop();
								break;
							}
						}
					}
				 	/* 如果删除的资源的回调函数不为空 ，将删除的资源作为参数执行会掉函数 */
				 	if(delCallBackFun != null){
				 		delCallBackFun(e.files[0]);
				 	}
                }
            }
        });
    };
	    
    /* kendoListView图片赋值 */
    saleskendoUtil.setAttachments = function(listViewId,attachments,clearBtnId,templateId){
		 var dataSource = new kendo.data.DataSource();
		 if(attachments!=null&&attachments.length>0){
			 $("#"+clearBtnId).parent().show();
			 $("#"+clearBtnId).show();
			 for(var i=0;i<attachments.length;i++){
				 dataSource.add({attachments:attachments[i]});
			 }
		 }else{
			 $("#"+clearBtnId).parent().hide();
			 $("#"+clearBtnId).hide();
		 }
		 $("#"+listViewId).kendoListView({
			 dataSource: dataSource,
			 template:kendo.template($("#"+templateId).html())
		 });
	};
		
		/*删除指定的图片*/
	saleskendoUtil.removeRes=function(attachments,resId,fileName,listViewId,clearBtnId){
			var dataSource = new kendo.data.DataSource();
			if(attachments){
				if(resId!=null){
					for(var i=0; i<attachments.length; i++){
					     if(attachments[i].id == resId){
					        while((i+1)<attachments.length){
					        	attachments[i] = attachments[i+1];
					        	i++;
					        }
					        attachments.pop();
					        break;
					      }
					 }
				}else{
					for(var i=0; i<attachments.length; i++){
					     if(attachments[i].id==null&&attachments[i].fileName == fileName){
					        while((i+1)<attachments.length){
					        	attachments[i] = attachments[i+1];
					        	i++;
					        }
					        attachments.pop();
					        break;
					      }
					 }
				}
				if(attachments.length>0){
					for(i=0; i<attachments.length; i++){
						dataSource.add({attachments:attachments[i]});
					}
				}
				$("#"+listViewId).data("kendoListView").setDataSource(dataSource);
				if(attachments.length == 0){
					$("#"+clearBtnId).parent().hide();
				}
			}
	};
	
	/* 通过guid删除指定的图片,有id的资源做假删除 */
	saleskendoUtil.removeResByGUID=function(attachments,guid,resId){
		if(attachments && guid){
			if(resId != null && resId != "null") {
				for(var i=0; i<attachments.length; i++){
				     if(attachments[i].id == resId){
				    	 attachments[i].delStatus = 1;
			    		 attachments[i].cover = 0 ;
			    		 attachments[i].sort = -1;
			    		 attachments[i].update = true;
				    	 break;
				     }
				 }
			} else {
				for(var i=0; i<attachments.length; i++){
				     if(attachments[i].guid == guid){
			    		 while((i+1)<attachments.length){
					        	attachments[i] = attachments[i+1];
					        	i++;
					     }
			    		 attachments.pop();
				    	 break;
				     }
				 }
			}
		}
	};
	
	/* 通过guid设置图片为封面 */
	saleskendoUtil.setCoverByGUID=function(attachments,guid,resId){
		if(attachments && guid){
			if(resId != null && resId != "null") {
				for(var i=0; i<attachments.length; i++){
				     if(attachments[i].id == resId){
				    	attachments[i].cover = 1 ;
				    	attachments[i].update = true;
				     } else if(attachments[i].cover == 1){
				    	attachments[i].cover = 0 ;
				    	attachments[i].update = true;
				     }
				 }
			} else {
				for(var i=0; i<attachments.length; i++){
				     if(attachments[i].guid == guid){
				    	attachments[i].cover = 1 ;
				    	attachments[i].update = true;
				     } else if(attachments[i].cover == 1){
				    	attachments[i].cover = 0 ;
				    	attachments[i].update = true;
				     }
				 }
			}
		}
		var a = 1;
	};
	
	/**
	 * KendoWindow 初始化方法
	 * @author tangxin 2015-04-27
	 */
	saleskendoUtil.initKendoWindow = function(formId, width, heigth, title, 
			visible, modal, resizable, actions, mesgLabelId, message){
		if(mesgLabelId != null) {
			$("#"+mesgLabelId).html(message);
		}
		$("#"+formId).kendoWindow({
			width:width,
			height:heigth,
	   		visible:visible,
	   		title:title,
	   		modal: modal,
	   		resizable:resizable,
	   		actions:actions
		});
		return $("#"+formId).data("kendoWindow");
	};
	
	/**
	 * kendo DatePicker 控件的初始化方法
	 * return 一个初始化好的DatePicker对象
	 * @author tangxin 2015-05-03
	 */
	saleskendoUtil.initDatePicker = function(formId) {
		$("#"+formId).kendoDatePicker({
	        format: "yyyy-MM-dd",
	        height: 30,
	        culture: "zh-CN",
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
		return $("#"+formId).data("kendoDatePicker");
	}
	
	/**
	 * 显示验证提示信息
	 * @author tangxin 2015-05-14
	 */
	saleskendoUtil.msg = function(formId,msg){
		var doc = $("#" + formId);
		if(doc != null) {
			doc.removeClass("v_hide");
			$("#" + formId + " span.v_msg").html(msg);
		}
	};
	
	/**
	 * 清除页面验证提示信息
	 * @author tangxin 2015-05-14
	 */
	saleskendoUtil.clearMsg = function(formId){
		var doc = $("#" + formId);
		if(doc != null) {
			doc.addClass("v_hide");
			$("#" + formId + " span.v_msg").html("");
		}
	};
	
	/**
	 * 清除页面所有验证提示信息
	 * @author tangxin 2015-05-14
	 */
	saleskendoUtil.clearAllMsg = function(){
		$("span.k-tooltip-validation").addClass("v_hide");
		$("#span.v_msg").html("");
	};
	
})(jQuery);