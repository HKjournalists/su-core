 $(document).ready(function() {
	 var lims = window.lims = window.lims || {};
	 var root = window.lims.root = window.lims.root || {};
	 
	 var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	 var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	 portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	 portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	 
	 root.aryProAttachments = new Array();
	 var faileCount = 0;   // 上传失败数量
	 var viewReportID = 0;
	 
	 root.initialize=function(){
		 $("#upload_pdf_div").html("<input id='upload_pdf_btn' type='file' />");
		 root.buildUpload("upload_pdf_btn", root.aryProAttachments, "fileEroMsg", "report");
		 root.buildGrid("report_grid", root.publishColumn, root.reportDS);
		 lims.initEasyItemGrid("testItem_grid");
		 
		 $("#submit").bind("click", root.submit);
		 $("#submit").hide();
		 $(".box").hide();
		 
		 /* 温馨提示 */
         $("#immediate-guide-trigger").bind("mouseenter", function(){root.changeGuidStyle(true);});
 		 $("#immediate-guide-trigger").bind("mouseleave", function(){root.changeGuidStyle(false);});
 		 /* 返回 */
 		 $(".gTop").bind("mouseenter",function(){root.buileAnimate(".gTop a","100px");});
		 $(".gTop").bind("mouseleave",function(){root.buileAnimate(".gTop a","100%");});
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
			                   { command: [/*{name:"edit",
			                   	    text:"<span class='k-edit'></span>" + "编辑", 
			                   	    click:function(e){
			                   	    	root.edit(this.dataItem($(e.currentTarget).closest("tr")));
			                     }},*/
			                     {name:"review",
			                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
			                   	    click:function(e){
			                   	    	e.preventDefault();
			                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));
			                   	    	viewReportID=currentItem.id;
			                   	    	lims.easyViewReport(viewReportID);
			                     }},
			                     /*{name:lims.localized("Delete"),
								  text:"<span class='k-icon k-cancel'></span>" + lims.localized("Delete"),
							      click: function(e){
							          var deleteRow = $(e.target).closest("tr");
							          deleteItem = this.dataItem(deleteRow);
								      $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center(); 
							     }}*/
			                     ], title: "操作", width: 30 }];
	 
	 root.reportDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(options.filter){
	            			var configure = filter.configure(options.filter);
	            			return portal.HTTP_PREFIX + "/testReport/getListByUploadPdf/" + configure + "/" + options.page + "/" + options.pageSize;
	            		}
						return portal.HTTP_PREFIX + "/testReport/getListByUploadPdf/" + options.page + "/" + options.pageSize;
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
	 
	 root.buildGrid = function (id, columns, ds){
			$("#" + id).kendoGrid({
				dataSource: ds == undefined ? []:ds,
				filterable: {
					extra:false,
					messages: lims.gridfilterMessage(),
					operators: {
						  string: lims.gridfilterOperators()
					}
				},
				height: 360,
		        width: 1000,
		        sortable: true,
		        selectable: true,
		        resizable: true,
		        toolbar: [
		    	        	{template: kendo.template($("#toolbar_template").html())}
			    	     ],
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: lims.gridPageMessage(),
		        },
		        columns: columns,
			});
		};
	 
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
            		 kendoConsole.log("上传失败:: " + root.getFileInfo(e) + lims.l("The file you uploaded mismatching template, re-upload the file in pdf format Please delete!"), true, null); 
            		 $("#"+msg).html(lims.l("The file you uploaded mismatching template, re-upload the file in pdf format Please delete!")); 
            		 return;
            	 }
            	 if(e.response.mismatchProduct){
            		 $(".box").show();
            		 kendoConsole.log("上传失败:: " + root.getFileInfo(e) + lims.l("The file you uploaded mismatching product, re-upload the file in pdf format Please delete!"), true, null); 
            		 return;
            	 }
            	 if(e.response.hasExist){
            		 $(".box").show();
            		 kendoConsole.log("上传失败:: " + root.getFileInfo(e) + lims.l("The report you uploaded has existed, re-upload the file in pdf format Please delete!"), true, null); 
            		 return;
            	 }
                 if (e.operation == "upload") {
                	 $(".box").show();
                	 kendoConsole.log("上传成功:: " + root.getFileInfo(e));
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
     
     root.setResultMsg = function(listResult){
    	 if(listResult==null) return;
    	 var ul = $("#uploadResultWindow ul");
    	 ul.html("");
    	 var saveCount = 0;
    	 for(var i=0;i<listResult.length;i++){
    		 var result = listResult[i];
    		 var msg="";
    		 var li = $("<li></li>");
    		 saveCount += result.reports;
    		 if(result.faild>0){
    			 msg = "后台处理异常！";
    		 }else if(result.parseFailed>0){
    			 msg="解析失败，该pdf不符合解析模板。";
    		 }else{
    			 var reps = (result.reports+result.exists);
    			 if(reps==0){
    				 msg="解析该pdf在系统中没有找到对应的产品！";
    			 }else if(reps==1){
    				 msg="解析该pdf匹配到1条产品信息,";
    				 var str = (result.reports==1?"已成本保存1份检测报告！":"但该报告对应的条形码、报告编号、批次已存在。");
    				 msg=msg+str;
    			 }else if(reps>1){
    				 msg="解析该pdf共匹配到"+reps+"份不同的产品信息，需要生成"+reps+"份检测报告,已成功保存"+result.reports+"份,";
    				 var str = (result.exists>0?"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有"+result.exists+"份条形码、报告编号、批次已经重复。":""); 
    				 msg=msg+str;
    			 }
    		 }
    		 li.html("文件名："+result.fileName+"，"+msg);
    		 ul.append(li);
    	 }
    	 ul.append($("<li></li>").html("本次共成功保存 "+saveCount+" 条报告信息。"));
     };
     
     
     root.submit = function(){
         $("#save_status").html("正在提交，请稍后...");
 		 $("#toSaveWindow").data("kendoWindow").open().center();
 		 totalCount = root.aryProAttachments.length;
 		 var listResult = new Array();
 		 var successCount = 0;
 		 var existsCount = 0;
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
                		   successCount = successCount + returnValue.count;
                		   existsCount = existsCount + returnValue.existsCount;
                		   var result= root.setResultVal(root.aryProAttachments[i].fileName,returnValue.count,
                				   returnValue.existsCount,returnValue.parseFailedCount,0);
                		   listResult.push(result);
                	   }
                	   else{
                		   faileCount++;
                		   var result= root.setResultVal(root.aryProAttachments[i].fileName,0,0,0,1);
                		   listResult.push(result);
                	   }
                },
        	 });
         }
         $("#save_status").html("");
  	   	 $("#toSaveWindow").data("kendoWindow").close();
  	   	 root.setResultMsg(listResult);
  	     $("#uploadResultWindow").data("kendoWindow").open().center();
  	     root.reportDS.read();
  	     $("#submit").hide();
  	     root.aryProAttachments.length=0;
  	     successCount = 0;
  	     $("#upload_pdf_div").html("<input id='upload_pdf_btn' type='file' />");
		 root.buildUpload("upload_pdf_btn", root.aryProAttachments, "fileEroMsg", "report");
		 $(".console").html("");
		 $(".box").hide();
	 };
     
     lims.initConfirmWindow(function() {
		  $.ajax({
					url:portal.HTTP_PREFIX + "/testReport/" + deleteItem.id,
					type:"DELETE",
					success:function(data){
						if(data.result.status == "true"){
							if(data.result.show){
								lims.initNotificationMes(returnValue.result.errorMessage, false);
							}else{
								lims.initNotificationMes("删除成功！", true);
							}
							root.reportDS.read();
						}else{
							lims.initNotificationMes("删除失败！", false);
						}
					}
				});
		  lims.closeConfirmWin();
	 }, undefined,lims.l("Are you sure to delete registration form"),true);
     
     $("#addRemarkWin").kendoWindow({
 		width:500,
 		height:200,
 		visible: false,
 		title:"添加备注",
 	 });
     
     $("#viewWindow").kendoWindow({
 		width:1000,
 		visible: false,
 		title:"发布报告预览",
 	 });
 	
     $("#btn_addN").click(function(){
 		$("#addRemarkWin").data("kendoWindow").close();
 	});
     
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
  		width:800,
  		visible:false,
  		title:"处理结果",
  		modal: true,
  		resizable:false,
  	});
    
 	$("#btn_addY").click(function(){
 		var tipText=$("#tipText").val();
 		if($.trim(tipText)==""){
 			lims.initNotificationMes("你没有输入任何备注文本，不能修改提示消息！", false);
 			return;
 		}
 		$("#addRemarkWin").data("kendoWindow").close();
 		var selectRow=$("#report_grid").data("kendoGrid").select();
 		var seItem=$("#report_grid").data("kendoGrid").dataItem(selectRow);
 		$.ajax({
 			url:portal.HTTP_PREFIX+"/testReport/editTips/"+tipText+"/"+seItem.id,
     		type:"GET",
     		dataType: "json",
            contentType: "application/json; charset=utf-8",
 			success:function(returnValue){
 				if(returnValue.result.status == "true"){
 					root.reportDS.read();
 					lims.initNotificationMes("修改消息成功！", true);
 				}else{
 					lims.initNotificationMes("修改消息失败！", false);
 				}
 			},
 		});
 	});
     
    /* 编辑报告，页面跳转  */
    root.edit = function(e) {
 		root.currentReportID = e.id;
 		var testreport = {
 			id : root.currentReportID,
 			backUploadPdfPage: true
 		};
 		$.cookie("user_0_edit_testreport", JSON.stringify(testreport), {
 			path : '/'
 		});
 		window.location.pathname = "/fsn-core/views/market/add_testreport.html";
 	};
     
    /* 报告添加消息提示  */
 	root.editTips=function(){
 		var length=$("#report_grid").data("kendoGrid").select().length;
 		if(length<1){
 			lims.initNotificationMes("请选择一条检测报告！", false);
 			return;
 		}
 		$("#remarkText").val("");
 		$("#addRemarkWin").data("kendoWindow").open().center();
 	 };
     
     // 温馨提示样式控制
	 root.changeGuidStyle = function(isShow){
		 if(isShow){
			 $("#skin-box-bd").css("display","block");
		 }else{
			 $("#skin-box-bd").css("display","none");
		 }
	 };
	 
	 root.buileAnimate = function buileAnimate(target,widthP){
		$(target).animate({
			right:"0px",
			opacity:'1',
			width:widthP,
		});
	 };
		
	 //当页面滚动到距离顶部200像素时，在右下角显示返回顶部按钮。
	 window.onscroll=function(){
		var top = document.documentElement.scrollTop==0? document.body.scrollTop : document.documentElement.scrollTop;
		if(top>200){
			$(".gTop a").css("display","block");
		}else{
			$(".gTop a").css("display","none");
		}
	 };
     
	 root.initialize();
 });