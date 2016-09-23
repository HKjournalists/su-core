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
    	 if(id=="upload_distribution_files"){
    		return '上传废弃物处理图片';
    	}
    };
	
	/* 加载图片 */
	 business_unit.setDisAttachments = function(disAttachments){
		 var dataSource = new kendo.data.DataSource();
		 if(disAttachments.length>0){
			 $("#btn_clearDisFiles").show();
			 for(var i=0;i<disAttachments.length;i++){
				 business_unit.aryDisAttachments.push(disAttachments[i]);
				 dataSource.add({attachments:disAttachments[i]});
			 }
		 }
		 if(disAttachments.length == 0){
				$("#btn_clearDisFiles").hide();
			}
		 $("#disAttachmentsListView").kendoListView({
           dataSource: dataSource,
           template:kendo.template($("#uploadedFilesTemplate").html()),
        });
	};
	
	 /* 从页面删除已有资源 */
    business_unit.removeRes = function(resID){
       var attachments =  business_unit.aryDisAttachments;
    	var dataSource = new kendo.data.DataSource();
		 for(var i=0; i<attachments.length; i++){
        	 if(attachments[i].id == resID){
        		 while((i+1)<attachments.length){
        			 attachments[i] = attachments[i+1];
        		 	 i++;
        		 }
        		 attachments.pop();
        		 break;
        	 }
         }
         if( attachments.length>0){
				for(i=0; i<attachments.length; i++){
					dataSource.add({ attachments:attachments[i]});
				}
			}
			$("#disAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			if(attachments.length == 0){
				$("#btn_clearDisFiles").hide();
			}
    }
       
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
    //验证必填的日期
    fsn.validateMustDate=function(formId,fieldName){
    	if($("#"+formId).val().trim() != ""){
    		if($("#test_time_format").val() == ""){
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
    		var date = $("#"+formId).val().trim();
    		var result = date.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
    		if(result==null){
    			return true;
    		}else{
    			return false;
    		}
    	}
    };
    fsn.clearErrorStyle=function(){
    	if(fsn.errorInputId){
    		$("#"+fsn.errorInputId).removeAttr("style");
    	}
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