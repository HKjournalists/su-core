 $(document).ready(function() {
	 var lims = window.lims = window.lims || {};
	 var root = window.lims.root = window.lims.root || {};
	 
	 var fsn = window.fsn = window.fsn || {};    // 全局命名空间
	 var upload = fsn.upload = fsn.upload || {}; // upload命名空间
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.HTTP_PREFIX = fsn.getHttpPrefix();   // 业务请求前缀
	 
	 root.aryProAttachments = new Array();
	 
	 root.initialize=function(){
		 fsn.initComponentCommon();
		 
		 $("#upload_pdf_div").html("<input id='upload_pdf_btn' type='file' />");
		 root.buildUpload("upload_pdf_btn", root.aryProAttachments, "fileEroMsg", "report");
		 
		 upload.buildGridWioutToolBar("upload_report_grid", root.publishColumn, fsn.reportDS, 350);
			
		 initItemGrid("testItem_grid");
		 
		 $("#toSaveWindow").kendoWindow({
		  		actions:[],
		  		width:500,
		  		visible:false,
		  		title:"保存状态",
		  		modal: true,
		  		resizable:false,
		  	});
		 	
		    $("#uploadResultWindow").kendoWindow({
		  		actions:['Close'],
		  		width:400,
		  		visible:false,
		  		title:"处理结果",
		  		modal: true,
		  		resizable:false,
		  	});
		 
		 $("#submit").bind("click", root.submit);
		 $("#submit").hide();
		 $(".box").hide();
		 
		 /* 温馨提示 */
         $("#immediate-guide-trigger").bind("mouseenter", function(){root.changeGuidStyle(true);});
 		 $("#immediate-guide-trigger").bind("mouseleave", function(){root.changeGuidStyle(false);});
	 };
     
	 root.publishColumn = [	
		                   { field: "id", title:"ID", width: 35 },
		                   { field: "serviceOrder", title:"报告编号", width: 70 },
		                   { field: "sample.product.name", title:"食品名称",width: 70 },
		                   { field: "sample.batchSerialNo", title:"批次", width: 85},
		                   { field: "status", title:"状态", width: 30, filterable: false, template: function(dataItem) {
		                    	 if(dataItem.status=="未发布"){
		                    		 return dataItem.status;
		                    	 }else if(dataItem.status=="已退回"){
		                    		 return "<strong style='color:red;'>" + dataItem.status + "</strong>";                    		 
		                    	 }else if(dataItem.status=="已发布"){
		                    		 return "<strong style='color:#006536;'>" + dataItem.status + "</strong>";                    		 
		                    	 }
		                     }},
		                   { field: "lastModifyUserName",title:"最后更新者",width:45},
		                   { field: "lastModifyTime",title:"最后更新时间",width:55,template: '#= (new Date(lastModifyTime)).format("YYYY-MM-dd")#', filterable: false},
		                   { field: "tips",title:"消息提示",width:70},
			                   { command: [
			                     {name:"review",
			                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
			                   	    click:function(e){
			                   	    	e.preventDefault();
			                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
			                   	    	viewReportInfo(currentItem.id);
			                     }},
			                     /*{name:lims.localized("Delete"),
								  text:"<span class='k-icon k-cancel'></span>" + lims.localized("Delete"),
							      click: function(e){
							    	  var deleteRow = $(e.target).closest("tr");
							          deleteItem = this.dataItem(deleteRow);
							          fsn.delete_id = deleteItem.id;
								      $("#delete_window").data("kendoWindow").open().center(); 
							     }}*/
			                     ], title: "操作", width: 30 }];
	 
	 fsn.reportDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(options.filter){
	            			var configure = filter.configure(options.filter);
	            			return portal.HTTP_PREFIX + "/testReport/getListByUploadPdf/" + configure + "/" + options.page + "/" + options.pageSize;
	            		}
						return portal.HTTP_PREFIX + "/testReport/getListByUploadPdf/null/" + options.page + "/" + options.pageSize;
					},
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
	            		if(returnValue.data.listOfReport[i].mkPublishFlag){
	            			returnValue.data.listOfReport[i].status = "已发布";
	            		}else if(returnValue.data.listOfReport[i].backFlag){
	            			returnValue.data.listOfReport[i].status = "已退回";
	            		}else{
	            			returnValue.data.listOfReport[i].status = "未发布";
	            		}
	            	}
	                return returnValue.data.listOfReport;
	            },
	            total : function(returnValue) {       
	                return returnValue.data.counts;
	            }     
	        },
	        change: function(e) {
	            
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	 
     root.buildUpload = function(id, attachments, msg, flag){
    	 $("#"+id).kendoUpload({
        	 async: {
                 saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/automnPdf",
                 removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
                 autoUpload: true,
                 removeVerb:"POST",
                 removeField:"fileNames",
                 saveField:"attachments",
        	 },localization: {
                 select:lims.l("upload_report_files"),
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
                      if (this.extension.toLowerCase() != ".pdf") {
                          lims.initNotificationMes('名称为【' + this.name + '】的文件不是.pdf文件,请上传.pdf文件!',false);
                          e.preventDefault();
                      }
                });
             },
             success: function(e){
            	 if(e.response.typeEror){
            		 $("#"+msg).html(lims.l("Wrong file type, the file will not be saved and re-upload pdf format Please delete!"));
            		 return;
            	 }
            	 if(e.response.morSize){
            		 $("#"+msg).html(lims.l("The file you uploaded more than 10M, re-upload the file in pdf format Please delete!")); 
            		 return;
            	 }
            	 if(e.response.pdfTypeError){
            		 $(".box").show();
            		 kendoConsole.log("识别失败:: " + root.getFileInfo(e) + lims.l("The file you uploaded mismatching template, re-upload the file in pdf format Please delete!"), true, null); 
            		 $("#"+msg).html(lims.l("The file you uploaded mismatching template, re-upload the file in pdf format Please delete!")); 
            		 return;
            	 }
            	 if(e.response.mismatchProduct){
            		 $(".box").show();
            		 kendoConsole.log("识别失败:: " + root.getFileInfo(e) + lims.l("The file you uploaded mismatching product, re-upload the file in pdf format Please delete!"), true, null); 
            		 return;
            	 }
            	 if(e.response.hasExist){
            		 $(".box").show();
            		 kendoConsole.log("识别失败:: " + root.getFileInfo(e) + lims.l("The report you uploaded has existed, re-upload the file in pdf format Please delete!"), true, null); 
            		 return;
            	 }
                 if (e.operation == "upload") {
                	 $(".box").show();
                	 kendoConsole.log("识别成功:: " + root.getFileInfo(e));
                     lims.log("upload");
                     lims.log(e.response);
                     attachments.push(e.response.results[0]);
                     $("#submit").show();
                 }else if(e.operation == "remove"){
                	 kendoConsole.log("成功删除:: " + root.getFileInfo(e));
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
                     if(attachments.length==0){
            			 $("#submit").hide();
            			 $(".console").html("");
            			 $(".box").hide();
            		 }
                 }
             },
             remove:function(e){
            	 $("#"+msg).html("(" + lims.l("Note: The file size can not exceed 10M! Only supports file formats: pdf")+")");
             },
             error:function(e){
            	 $("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
             },
         });
     };
     
     root.getFileInfo = function (e) {
         return $.map(e.files, function(file) {
             var info = file.name;

             // File size is not available in all browsers
             if (file.size > 0) {
                 info  += " (" + Math.ceil(file.size / 1024) + " KB)";
             }
             return info;
         }).join(", ");
     };
     
     root.setResultVal = function(fileName, reports, exists, parseFailed, faile){
    	 var result={
   			  fileName:fileName,
   			  reports:reports,
   			  exists:exists,
   			  parseFailed:parseFailed,
   			  faild:faile,
   		   };
    	 return result;
     };
     
     root.submit = function(){
         $("#save_status").html("正在提交，请稍后...");
 		 $("#toSaveWindow").data("kendoWindow").open().center();
 		 
 		 // 记录当前执行提交操作时，上传的pdf总数
 		 var upload_resource_count = root.aryProAttachments.length;
 		 var create_report_count = 0;
         for(var i=0; i<root.aryProAttachments.length; i++){
        	 $.ajax({
                 url: portal.HTTP_PREFIX + "/testReport/analysisPDF", // 解析pdf
                 type: "POST",
                 dataType: "json",
                 timeout:600000,   //10min
                 async:false,
                 contentType: "application/json; charset=utf-8",
                 data: JSON.stringify(root.aryProAttachments[i]),
                 success: function(returnValue) {
                	   if(returnValue.result.status == "true"){
                		   create_report_count += returnValue.data;
                	   }
                },
        	 });
         }
         
         // 关闭正在保存的提示窗口
         $("#save_status").html("");
  	   	 $("#toSaveWindow").data("kendoWindow").close();
  	   	 
  	   	 // 数据刷新
  	   	 fsn.reportDS.read();
  	     $("#uploadResultWindow ul").html("系统成功识别了 " + upload_resource_count + " 个pdf，<br> 成功生成了 " + create_report_count + " 条报告信息。");
  	     
  	     // 重置页面状态
  	   	 $("#uploadResultWindow").data("kendoWindow").open().center();
  	     $("#submit").hide();
  	     root.aryProAttachments.length=0;
  	     $("#upload_pdf_div").html("<input id='upload_pdf_btn' type='file' />");
		 root.buildUpload("upload_pdf_btn", root.aryProAttachments, "fileEroMsg", "report");
		 $(".console").html("");
		 $(".box").hide();
	 };
     
     // 温馨提示样式控制
	 root.changeGuidStyle = function(isShow){
		 if(isShow){
			 $("#skin-box-bd").css("display","block");
		 }else{
			 $("#skin-box-bd").css("display","none");
		 }
	 };
	 
	 root.initialize();
 });