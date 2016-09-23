$(function(){
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	
	upload.HTTP_PREFIX = fsn.getHttpPrefix();
	
	upload.buildGrid = function (id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: fsn.gridfilterMessage(),
				operators: {
					string: fsn.gridfilterOperators(),
				    date: fsn.gridfilterOperatorsDate(),
				    number: fsn.gridfilterOperatorsNumber()
				}
			},
			width: "100%",
			height:470,
	        sortable: true,
	        resizable: true,
	        toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        selectable: true,
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
		//$(".k-toolbar").after("<div class=''><input type='checkbox' />全选<a>批量删除</a></div>");
	};
	
	upload.buildGridWithSimple = function (id, columns, ds, toolbar){
		$("#" + id).kendoGrid({
            dataSource: ds == null ? [] : ds,
            selectable: true,
            pageable: {
            	refresh: true,
                messages: fsn.gridPageMessage(),
            },
            toolbar: [{
                template: kendo.template($("#" + toolbarId).html())
            }],
            columns: columns,
        });
	};
	
	upload.buildGridWioutToolBar = function (id,columns,ds, height){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: fsn.gridfilterMessage(),
				operators: {
					string: fsn.gridfilterOperators(),
				    date: fsn.gridfilterOperatorsDate(),
				    number: fsn.gridfilterOperatorsNumber()
				}
			},
			page:1,
            pageSize: 5,
			height:"350px",
	        width: "100%",
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
                pageSizes: 5,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	/**
	 * 初始化grid
	 * @author ZhangHui 2015/4/10
	 */
	upload.buildGridByBoolbar = function (id, columns, ds, height, toolbar){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				extra: false,
				messages: fsn.gridfilterMessage(),
				operators: {
					string: fsn.gridfilterOperators(),
				    date: fsn.gridfilterOperatorsDate(),
				    number: fsn.gridfilterOperatorsNumber()
				}
			},
			width: "100%",
			height:height,
	        sortable: true,
	        resizable: true,
	        toolbar: [
	                  {template: toolbar==""?kendo.template(""):kendo.template($("#"+toolbar).html())}
	                  ],
	        selectable: true,//"multiple, row"：不可复制
	        pageable: {
	            refresh: true,
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	/**
	 * 初始化上传控件
	 * @author ZhangHui 2015/4/22
	 */
	upload.buildUpload = function(id, attachments, msg, flag){
		var selectLocal="";
		if(id=="upload_product_files"){
			selectLocal=fsn.l("upload_Product");
		}else if(id=="upload_lab_files"){
			selectLocal=fsn.l("upload_Lab");
		}else if(id=="upload_cert"){
			selectLocal=fsn.l("upload_cert");
		}else if(id.indexOf("upload_licImg_files")!=-1){
			selectLocal=fsn.l("上传'营业执照'或<br>'统一信用代码'");
		}else if(id.indexOf("upload_qsImg_files")!=-1){
			selectLocal=fsn.l("上传'生产许可证'");
		}else if(id.indexOf("upload_disImg_files")!=-1){
			selectLocal=fsn.l("上传'食品流通许可证'或<br>'食品经营许可证'");
		}else if(id=="return_file"){
         	selectLocal=fsn.l("上传退回原因");
        }
		$("#"+id).kendoUpload({
       	 async: {
                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                autoUpload: true,
                removeVerb:"POST",
                removeField:"fileNames",
                saveField:"attachments",
       	 },localization: {
                select:selectLocal,//id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
                retry:lims.l("retry",'upload'),
                remove:lims.l("remove",'upload'),
                cancel:lims.l("cancel",'upload'),
                dropFilesHere: lims.l("drop files here to upload",'upload'),
                statusFailed: lims.l("failed",'upload'),
                statusUploaded: lims.l("uploaded",'upload'),
                statusUploading: lims.l("uploading",'upload'),
                uploadSelectedFiles: lims.l("Upload files",'upload'),
            },
            upload: function(e){
                var files = e.files;
                 $.each(files, function () {
                	 if(this.name.length > 100){
            		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
		                    e.preventDefault();
		                    return;
            	  	 }
                     var extension = this.extension.toLowerCase();
                     if(flag == "IMG"){
                             if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
                             lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
                             e.preventDefault();
                     }
                 }
               });
            },
            multiple:true,
            success: function(e){
		           	 if(e.response.typeEror){
		           		 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！");
		           		 return;
		           	 }
		           	 if(e.response.morSize){
		           		 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!");
		           		 return;
		           	 }
		             if (e.operation == "upload") {
		            	 lims.log("upload");
		                 lims.log(e.response);
		                 attachments.push(e.response.results[0]);
		               	 if(flag == "IMG"){
		               		 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )");
		                 }else if(flag == "PDF"){
		                	 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.pdf )");
		                 }
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
	           	 if(flag == "PDF"){
	           		 $("#"+msg).html("("+fsn.l("Note: The file size can not exceed 10M! Only supports file formats: pdf")+")");
	           	 }else if(flag == "IMG"){
	           		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
	           	 }
            },
            error:function(e){
           	 	 $("#"+msg).html(fsn.l("An exception occurred while uploading the file! Please try again later ..."));
            },
      });
	};
});