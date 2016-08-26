$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	//var upload = window.lims.upload = window.lims.upload || {};
	var unit = fsn.unit = fsn.unit || {};
	var filter = window.filter = window.filter || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	unit.aryExlAttachments = new Array();
	var deleteItem=null;
	unit.templatecolumns = [
					/* { command: [{
						name: "Check",
					    text: "<input type='checkbox' class='checkboxes'/>"
					 },],  width: 40 },*/
	                 {field: "id",title: lims.l("Id"),width: 50,filterable: true},
                     {field: "name",title: lims.l("Name"),width: 100,filterable: true},
                     {field: "address",title: lims.l("Address"),width: 100,filterable: true},
                     {field: "license.licenseNo",title: lims.l("Registration Id"),width: 80,filterable: true},
                     { command: [/*{
                                	  name:"edit",
    			                      text: lims.localized("Edit"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.location.href = portal.CONTEXT_PATH+"/views/business/subsidiary.html?id="+temp.id;
									  }
								  },*/
					            {
			                      text:lims.l("Delete"),
					          	  click: function(e){
					          		var deleteRow = $(e.target).closest("tr");
					          		deleteItem = this.dataItem(deleteRow);
						          	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
					          	  }
					            }
					            ], title:lims.l("Operation"), width: 50 }
                     ];
    
    unit.msgTempColumns = [
	                 {field: "organizationName",title: lims.l("Name"),width: 80,filterable: true},
                     {field: "organizationAddress",title: lims.l("Address"),width: 80,filterable: true},
                     {field: "licenseNo",title: lims.l("Registration Id"),width: 60,filterable: true},
                     {field: "msg",title: "失败原因",width: 60,filterable: true},
                     ];
                     
	unit.templateDS = function(keyword){
		return new kendo.data.DataSource({
			transport: {
	            read: {
                    url : function(options){
                        var configure = null;
            		    if(options.filter){
            			configure = filter.configure(options.filter);
            		    }
            		return portal.HTTP_PREFIX + "/business/subsidiary/" + options.page + "/" + options.pageSize + "/" + configure;
				    },
					type : "GET",
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.result.success?d.data.subsidiarylist:null;  //响应到页面的数据
	             },
	             total : function(d) {
	                 return d.result.success?d.data.totalCount:0;   //总条数
	             }         
	        },
	        batch : true,
	        pageSize : 5, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	unit.buildGrid=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height: 310,
	        sortable: true,
	        resizable: true,
	        toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        selectable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};
    
    unit.buildGridMsg=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds,
            virtual:true,
            selectable: true,
            pageable: {
	            refresh: true,
	            pageSizes: true,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	};

    unit.batchAdd=function(){
		$('#save').click(function(){
	            unit.save();
	        });
	    $('#cancel').click(function(){
	        $("#form_dialog").data("kendoWindow").close();
	        	$("#unit").data("kendoGrid").dataSource.read();
	        });
		$("#form_dialog").data("kendoWindow").title(lims.l("Upload File:"));
        $("#form_dialog").data("kendoWindow").open().center();
	};
    
	unit.initialize = function(){
		lims.initConfirmWindow(function() {
			  $.ajax({
						url:portal.HTTP_PREFIX + "/business/subsidiaryBusiness/"+deleteItem.id,
						type:"DELETE",
						success:function(data){
							if(data.result.status == "true"){
								$("#unit").data("kendoGrid").dataSource.read();
								fsn.initNotificationMes("旗下企业删除成功", true);
							}else{
								fsn.initNotificationMes("旗下企业删除失败", false);
							}
						}
					});	  		
		lims.closeConfirmWin();
	}, undefined,lims.l("WIN_CONFIRM_MSG"),true);
		unit.buildUpload("attachments",unit.aryExlAttachments,"fileEroMsg","xls");
		unit.initOperateWin();
		var dataSource = this.templateDS("");
		unit.buildGrid("unit",this.templatecolumns, dataSource);
        $("#unit .k-grid-content").attr("style","height: 364px");	
	};
	unit.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = unit.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	};
	unit.initOperateWin = function(){
		$("#form_dialog").kendoWindow({
			actions: [],
            height: "250px", 
            width: "450px",
            title: lims.l("Upload Management:"),
            visible: false,
	        modal: true
        });
		
		$("#confirm").bind("click",unit.confirm);
	};
	unit.confirm=function(){
		$("#msg_dialog").data("kendoWindow").close();
		 $("#unit").data("kendoGrid").dataSource.read();
	};
	
	unit.initWindow=function(formId,w,h,title){
		 $("#"+formId).kendoWindow({
	         height: h, 
	         width: w,
	         title: title,
	         visible: false,
		     modal: true
	     });
	};
	
	//初始话批量上传企业反馈对话框(包含grid)
	unit.initWindow("msg_dialog","850px","400px","上传状态");
	
	//初始话批量上传企业反馈对话框(不包含grid)
	unit.initWindow("msgWindow","400px","100px","上传状态");
	
	//批量上传旗下企业的“提交”事件
	unit.save=function(){
		if(unit.aryExlAttachments.length==0){
			$("#fileEroMsg").html("请上传.xls格式文件！ ");
			return;
		}
		$("#form_dialog").data("kendoWindow").close();
		var exlVO = {
				"aryExlAttachments":unit.aryExlAttachments
			};
		$("#successMsg").html("");
		$("#faildMsg").html("");	
		$.ajax({
			url : portal.HTTP_PREFIX+"/business/batchAddSubsidiary",
			type : "POST",
			dataType: "json",
			async:false,
			data :JSON.stringify(exlVO),
			contentType:"application/json;charset=UTF-8",
			success : function(data){
				if (data.result.status == "true") {	
					 if(data.faildSize>0){
						 $("#successMsg").html("插入成功"+data.successSize+"条");
						 $("#faildMsg").html("插入失败"+data.faildSize+"条 ，  上传失败的企业如下表：");	
                         var ds = new kendo.data.DataSource({
                              data: data.faildList,
                              page:1,
                              pageSize:5
                         });
                        unit.buildGridMsg("showdialog_msg",unit.msgTempColumns,ds);					 
                        $("#msg_dialog").data("kendoWindow").open().center(); 	
					 }else{
						 $("#msgWindow span").html("全部插入成功，共"+data.successSize+"条");
						 $("#msgWindow").data("kendoWindow").open().center();
						 $("#unit").data("kendoGrid").dataSource.read();
					 }
				}else {
					$("#msgWindow span").html("系统异常！");
					$("#msgWindow").data("kendoWindow").open().center();
				}
			}
		});
	};
	
	unit.buildUpload = function(id, attachments, msg, flag){
	   	 $("#"+id).kendoUpload({
	       	 async: {
	                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
	                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
	                autoUpload: true,
	                removeVerb:"POST",
	                removeField:"fileNames",
	                saveField:"attachments",
	       	 },localization: {
	                select:"选择文件",
	                retry:"重试",
	                remove:"删除",
	                cancel:"取消",
	                dropFilesHere:"drop files here to upload",
	                statusFailed:"失败",
	                statusUploaded: "上传成功",
	                statusUploading: "上传中",
	                uploadSelectedFiles: "已上传文件",
	            },
	            multiple:false,
	            success: function(e){
	            	if(this.name.length > 100){
           		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
	                    e.preventDefault();
	                    return;
           	  		}
	           	 	if(e.response.typeEror){	      
	           			 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传.xls格式的文件！"); 
	           			 return;
	           	 	}
	           	 	if(e.response.morSize){
	           			 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传小于10M的.xls格式文件!"); 
	           			 return;
	           	 	}
	           	 	if (e.operation == "upload") {
	                    attachments.push(e.response.results[0]);
	                    $("#"+msg).html("文件识别成功，可以保存！");
	           	 	}else if(e.operation == "remove"){	 
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
	                    $("#"+msg).html("(注意：文件类型只能为.xls，且一次只能上传一个文件) ");
	                }
	            },
	            remove:function(e){
	           	 if(msg=="repFileMsg"){
	           		 $("#"+msg).html("(注意：文件类型只能为.xls，且一次只能上传一个文件) ");
	           	 }
	            },
	            error:function(e){
	           	 $("#"+msg).html("上传文件时出现异常！请稍后再试...");
	            },
	        });
	    };
	unit.initialize();
});