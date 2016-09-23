$(document).ready(function(){
	var fsn = window.fsn = window.fsn || {};
	var upload = window.fsn.upload = window.fsn.upload ||{};
	fsn.HTTP_PREFIX = fsn.getHttpPrefix();
	
	/* 初始化上传控件  */
	upload.buildUpload = function(id, attachments, msg, btnText){
   	 $("#"+id).kendoUpload({
       	 async: {
                saveUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
                removeUrl: fsn.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select: lims.l(btnText),
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
            	if(this.name.length > 100){
       		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
                    e.preventDefault();
                    return;
       	  		}
           	 	if(e.response.typeEror){
           	 		$("#"+msg).html(lims.l("Wrong file type, the file will not be saved, please delete the re-upload png, bmp, jpeg file format!"));
           	 		return;
           	 	}
           	 	if(e.response.morSize){
           	 		$("#"+msg).html(lims.l("The file you uploaded more than 10M, re-upload, delete png, bmp, jpeg file format!"));
           	 		return;
           	 	}
           	 	if (e.operation == "upload") {
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
           		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
            },
            error:function(e){
           	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
            },
        });
    };
    
    /* 加载企业生产车间图片 */
	 upload.setAttachments = function(attachments,clearBtnId,listViewId,templateId){
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
			 template:kendo.template($("#"+templateId).html()),
		 });
	};
	
	/*删除指定的图片*/
	upload.removeRes=function(attachments,resId,fileName,listViewId,clearBtnId){
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
				     if(attachments[i].id==null&&
				    		 attachments[i].fileName == fileName){
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
});