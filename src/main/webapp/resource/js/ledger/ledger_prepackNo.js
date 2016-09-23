$(function(){
	var fsn = window.fsn = window.fsn || {};
	var ledger = fsn.ledger = fsn.ledger || {};
	var HTTPPREFIX = fsn.getHttpPrefix();
	var LEDGER_GRID = null;
	var DISHSNO_DS = null;
	var TZID = null;
	var CONFIRM = null;
	ledger.status = "";
	function initialize(){
		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
        buildGridWioutToolBar("LEDGER_GRID",ledger.columns,getStoreDS("","",""),"400");//已确认批发的商品
        LEDGER_GRID = $("#LEDGER_GRID").data("kendoGrid");
        lims.initKendoWindow("IMPORT_EXCEL_WIN", "导入采购信息", "500px", "300px", false, true, false);
	};
	
	
	 function getStoreDS(productName,companyName,companyPhone){
	 	if(productName!=""){
			 productName = encodeURIComponent(productName);
		 }
		 if(companyName!=""){
			 companyName = encodeURIComponent(companyName);
		 }
		 DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/ledgerPrepackNo/getListLedgerPrepackNo/"+ options.page + "/" + options.pageSize + "?productName=" + productName + "&companyName=" + companyName +"&companyPhone="+ companyPhone;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	data : function(data) {
	                return data.data;  
	            },
	            total : function(data) {
	               return  data.total;//总条数
	            }  
	        },
	        batch : true,
	        page:1,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		   });
		 return DISHSNO_DS;
	 }
	 
	 function buildGridWioutToolBar(id,columns,ds, height){
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
//	            toolbar: [{
//	                template: kendo.template($("#toolbar_template").html())
//	            }],
	            editable: false,
	            height:height,
	            width: "100%",
	            sortable: true,
	            resizable: true,
	            selectable: false,
	            pageable: {
	                refresh: true,
	                messages: fsn.gridPageMessage()
	            },
	            columns: columns
	        });
	    };

	
	 /*新增 */
	ledger.add = function(){
		 var status = $("#status").val();
		 window.location.href = "ledger_prepackNo_add.html?status="+status;
	 };
 
	 //查询
	 ledger.check = function(){
	 	var productName = $.trim($("#productName").val());
	 	var companyName = $.trim($("#companyName").val());
	 	var companyPhone = $.trim($("#companyPhone").val());
	 	LEDGER_GRID.setDataSource(getStoreDS(productName,companyName,companyPhone));
	 	$("#productName").val("");
	 	$("#companyName").val("");
	 	$("#companyPhone").val("");
	 	LEDGER_GRID.refresh();
	 };
	 
	 /* 查看 */
	 ledger.checkUrl = function(id){
		 var status = $("#status").val();
	    	window.location.href = "ledger_prepackNo_add.html?status="+status+"&id=" + id;
	 };
	 
	//初始化弹出框
	 function initKendoWindow(formId, width, heigth, title,visible, modal, resizable, actions, mesgLabelId, message){
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
		
		//删除调用方法
		ledger.deletetWhole = function(id){
			 TZID = id;
			 CONFIRM.open().center();
		};
	 
	 /* 删除提交  */
		ledger.deleteRow = function(status){
			 if(status=="save"){
					$.ajax({
						url:HTTPPREFIX + "/ledgerPrepackNo/deleteRow/"+TZID,
						type:"GET",
						dataType: "json",
						async:false,
						success:function(returnValue){
							if (returnValue.result.status == "true") {
								fsn.initNotificationMes("已成功删除！", true);
								if(returnValue.total>0){
									initialize();
								}else{
									buildGridWioutToolBar("LEDGER_GRID",ledger.columns,"","400");//已确认批发的商品
								}
							} else {
								fsn.initNotificationMes("删除失败", false);
								initialize();
							}
						}
					});
					CONFIRM.close();
				}else{
					CONFIRM.close();
				}
	 };
	//显示字段
	 ledger.columns = [
	               {field:"productName", title:"产品名称", width:"20%",filterable: false,editable: false},
	               {field: "alias", title: "别名", width:"20%",filterable: false,editable: false},
				   {field: "number", title: "数量", width:"20%",filterable: false,editable: false},
				   {field: "companyName", title: "供货商名称", width:"20%",filterable: false,editable: false},
				   {field: "companyPhone", title: "供货商电话", width:"20%",filterable: false,editable: false},
				   {field: "purchaseTime", title: "采购时间", width:"20%",filterable: false,editable: false},
				   {field:"",title:fsn.l("操作"), width: "25%",template:function(dataItem){
	               		var status=dataItem.outStatus;
		                     return "<a class='k-button k-button-icontext k-grid-ViewDetail ' onclick='fsn.ledger.checkUrl("+dataItem.id+");'><span class='k-icon k-edit' ></span>编辑</a>"+
		                     		"<a class='k-button k-button-icontext k-grid-ViewDetail '  onclick='fsn.ledger.deletetWhole("+dataItem.id+");'><span class='k-icon k-update'></span>删除</a>";
					   	}
					 }
		                  ];
	 
	 fsn.ledger.openExel_Win = function(){
//		 alert("正在研发中......")
		 $("#IMPORT_EXCEL_WIN").data("kendoWindow").open().center();
		var url = "/resource/kendoUI/removeResources";
		 ledger.loadKendoupload(url);
	 };
	 var flag = "excel";
	 ledger.loadKendoupload = function(url){
		 $("#IMPORT_EXCEL").kendoUpload({
			 localization: {select:"请选择文件"}
		 });
		 if(ledger.load_Upload){return;};
		 $("#IMPORT_EXCEL").kendoUpload({
	       	 async: {
	                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
	                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
	                autoUpload: true,
	                removeVerb:"POST",
	                removeField:"fileNames",
	                saveField:"attachments",
	       	 },
	       	 localization: {
//	                select:"请选择文件",//id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
	                retry:lims.l("retry",'upload'),
	                remove:lims.l("remove",'upload'),
	                cancel:lims.l("cancel",'upload'),
	                dropFilesHere: lims.l("drop files here to upload",'upload'),
	                statusFailed: lims.l("failed",'upload'),
	                statusUploaded: lims.l("uploaded",'upload'),
	                statusUploading: lims.l("uploading",'upload'),
	                uploadSelectedFiles: lims.l("文件已经上传",'upload'),
	            },
	            multiple:false,
	            upload: function(e){
	                var files = e.files;
	                 $.each(files, function () {
	                     var extension = this.extension.toLowerCase();
	                     if (extension != ".xls" && extension != ".xlsx") {
	                     lims.initNotificationMes('导入文件格式错误,请上传 .xls .xlsx类型文件!', false);
	                     e.preventDefault();
	                 }
	               });
	            },
	            success: function(e){
		             if (e.operation == "upload") {
		            	 lims.initNotificationMes("excel导入成功！", true);
		            	 var dialog = $("#IMPORT_EXCEL_WIN").data("kendoWindow");
		            	 setTimeout(function() {
		            		  dialog.close();
		            		}, 2000);
		            	 ledger.goBackList();
		            };
	            },
	            remove:function(e){
	            	lims.initNotificationMes("(注意：请上传excel2003或者2007版本;后缀，xls或xlsx两个版本 )", true);
	            },
	            error:function(e){
	            	lims.initNotificationMes(fsn.l("An exception occurred while uploading the file! Please try again later ..."),false);
	            },
	      });
		 ledger.load_Upload  = $("#IMPORT_EXCEL").data("kendoUpload");
		  
	 };
	 ledger.goBackList = function(){
		 var status = $("#status").val();
		 if(status=='yes'){
			 window.location.href="ledger_prepack.html";
		 }else{
			 window.location.href="ledger_prepackNo.html";
		 }
	};
	 initialize();
});
