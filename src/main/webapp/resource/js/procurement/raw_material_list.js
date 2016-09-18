$(function(){
	var fsn = window.fsn = window.fsn || {};
	var procurement = fsn.procurement = fsn.procurement || {};
	procurement.qualifiedAttachments = new Array();//合格证图片
	procurement.disposeAttachments = new Array();//处理证明图片
	procurement.dispose=null;

	/**
	 * 初始化方法
	 */
	procurement.initialize=function(){
		//初始化表
		buildGridWioutToolBar("raw_material",procurement.columns,getStoreDS(""),"400");
		buildGridWioutToolBar("raw_material_dispose",procurement.disposeColumns,getDisposeDS(""),"400");

		//初始化弹窗
		procurement.initPopup("addPopup","新增"+procurement.typeName);
		procurement.initPopup("addDisposePopup","添加后续处理");

		procurement.initPopup("showQualifiedPopup","查看");

		procurement.initPopup("addConfirmPopup","确认");
		procurement.initPopup("addConfirmDisposePopup","确认");

		fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');

		//初始化其他控件
		procurement.initWidget();

		//初始化tabstrip
		$("#tabstrip").kendoTabStrip({
			animation:  {
				open: {
					effects: "fadeIn"
				}
			}
		});
	};
	
	/**
	 * 初始化弹框方法
	 */
	procurement.initPopup = function(id,title) {
		var window = $("#"+id);
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				title:title ,
				modal:true
				/*visible : false,
				resizable : false*/
			});
		}
		window.data("kendoWindow").center();
	};
	
	/**
	 * 初始化控件
	 */
	procurement.initWidget = function() {
		/* 初始化时间控件  */
	    $("#procurementDate,#expireDate,#disposeDate").kendoDatePicker({
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
	    
	    //初始化供应商下拉框
		$("#provider").kendoDropDownList({
			dataTextField: "name",
			dataValueField: "id",
			optionLabel:"--请选择--",
			dataSource: procurement.getDataSet()
		});

		//初始化采购数量NumericTextBox控件
		 $("#procurementNum").kendoNumericTextBox({
			 spinners: false,
			 format: "n0",
			 placeholder: "采购数量只能为数字"
         });

		//初始化采购数量NumericTextBox控件
		 $("#disposeNum").kendoNumericTextBox({
			 spinners: false,
			 format: "n0",
			 placeholder: "处理数量只能为数字"
         });
	    
	};

	/**
	 * 获取原料来源客户方法
	 * @returns {Array}
     */
	procurement.getDataSet = function(){
		var data=[];
            var url=fsn.getHttpPrefix() + "/erp/customer/2/sourceOrsoldCustomer/2/0/10/0/0.8139816030495346";
            $.ajax({
                url: url,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(returnVal){
                	data=returnVal.result.listOfModel;
                }
            });
        return data;
    };

	/**
	 * 采购信息表字段
	 * @type {*[]}
     */
	procurement.columns = [
		{field:"name", title:"名称", width:"20%",filterable: false,editable: false},
		{field: "providerName", title: "供应商", width:"35%",filterable: false,editable: false},
		{field: "format", title: "规格", width:"15%",filterable: false,editable: false},
		{field: "batch", title: "批次", width:"18%",filterable: false,editable: false},
		{field: "procurementNum", title: "采购数量", width:"13%",filterable: false,editable: false},
		{field: "procurementDate", title: "采购日期", template: '#=procurementDate=fsn.formatGridDate(procurementDate)#',width:"13%",filterable: false,editable: false},
		{field: "expireDate", title: "过期日期",template:function (e) {
				var nowDate=new Date();
			    var expireDate=fsn.formatGridDate(e.expireDate);
			     if(e.expireDate<nowDate){
					return '<span style="color: red">'+expireDate+'</span>';
				 }else{
					 return '<span>'+expireDate+'</span>';
				 }
		} , width:"13%",filterable: false,editable: false},
		{field: "surplusNum", title: "剩余数量", width:"13%",filterable: false,editable: false},
		{ command: [{
			text: "使用记录",
			click: function(e){
				e.preventDefault();
				var editRow = $(e.target).closest("tr");
				var temp = this.dataItem(editRow);
				window.location.href = fsn.procurement.recordUrl+temp.id;
			}
		},
			{
				text: "合格证明",
				click: function(e){
					e.preventDefault();
					var editRow = $(e.target).closest("tr");
					var temp = this.dataItem(editRow);
					procurement.showQualifiedImg(temp.hgAttachments);
				}
			},
			{
				text: "后续处理",
				click: function(e){
					e.preventDefault();
					var editRow = $(e.target).closest("tr");
					var temp = this.dataItem(editRow);
					procurement.dispose=temp;
					procurement.openAddDisposeWin();
				}
			},
		], title:"操作", width: "43%" }
	];

	/**
	 * 获取采购信息列表数据源
	 * @param name
	 * @returns {kendo.data.DataSource|*}
     */
	 function getStoreDS(name){
	 	if(name!=""){
	 		name = encodeURIComponent(name);
		 }

		 DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/procurement/getProcurementList/"+ options.page + "/" + options.pageSize + "?name=" + name+"&type="+procurement.type ;
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

	procurement.showQualifiedImg=function (imgs) {
		var slides = $("#slides");
		var img ="<div class=\"slides_container\">";
		for(var i=0;i<imgs.length; i++)
		{
			 img =img+ '<div class="slide"><img style="width: 849px;height: 638px" src="'+imgs[i].url+'"/></div>';

		}
		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
			'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
		slides.html(img);
		$('#slides').slides();
		$("#showQualifiedPopup").data("kendoWindow").open();
	};

	/**
	 * 新增采购信息打开弹窗方法
	 */
	procurement.add = function(){
		$("#qualifiedDiv").html("<input type=\"file\" id=\"qualified\" class=\"k-input k-textbox\" />");
		procurement.buildUpload("qualified",procurement.qualifiedAttachments,"IMG",true);
		$("#name").val("");
		$("#format").val("");
		$("#batch").val("");
		$("#provider").data("kendoDropDownList").value(0);
		$("#procurementNum").data("kendoNumericTextBox").value("");
		$("#procurementDate").data("kendoDatePicker").value("");
		$("#expireDate").data("kendoDatePicker").value("");
		$("#remark").val("");
		procurement.qualifiedAttachments.length=0;
		$("#addPopup").data("kendoWindow").open();
	 };

	/**
	 * 查询采购信息方法
	 */
	 procurement.check = function(){
	 	var productName = $.trim($("#name_query").val());
	 	$("#raw_material").data("kendoGrid").setDataSource(getStoreDS(productName));
	 	$("#raw_material").data("kendoGrid").refresh();
	 };

	/**
	 *  保存采购信息方法
	 */
	 procurement.saveProcurement = function(){
		 if(""==$("#name").val().trim()){
			 lims.initNotificationMes('名称不能为空!', false);
			 return;
		 }
		 if(""==$("#provider").data("kendoDropDownList").value().trim()){
			 lims.initNotificationMes('供应商不能为空!', false);
			 return;
		 }
		 if(""==$("#format").val().trim()){
			 lims.initNotificationMes('规格不能为空!', false);
			 return;
		 }
		 if(""==$("#batch").val().trim()){
			 lims.initNotificationMes('批次不能为空!', false);
			 return;
		 }
		 if(!$("#procurementNum").data("kendoNumericTextBox").value()){
			 lims.initNotificationMes('采购数量不能为空!', false);
			 return;
		 }
		 if(!$("#procurementDate").data("kendoDatePicker").value()){
			 lims.initNotificationMes('请选择采购时间!', false);
			 return;
		 }
		 if(!$("#expireDate").data("kendoDatePicker").value()){
			 lims.initNotificationMes('请选择过期时间!', false);
			 return;
		 }
		 if($("#expireDate").data("kendoDatePicker").value()-$("#procurementDate").data("kendoDatePicker").value()<=0){
			 lims.initNotificationMes('过期时间不能小于采购时间!', false);
			 return;
		 }
		 if(procurement.qualifiedAttachments.length==0){
			 lims.initNotificationMes('请上传合格证图片!', false);
			 return;
		 }

		 procurement.openConfirmWin();


	 	
	 };

	procurement.openConfirmWin=function () {
		$("#name_c").html($("#name").val());
		$("#format_c").html($("#format").val());
		$("#batch_c").html($("#batch").val());
		$("#provider_c").html($("#provider").data("kendoDropDownList").text());
		$("#procurementNum_c").html($("#procurementNum").data("kendoNumericTextBox").value());
		$("#procurementDate_c").html($("#procurementDate").val());
		$("#expireDate_c").html($("#expireDate").val());
		$("#remark_c").val($("#remark").val());

		var slides = $("#slides1");
		var img ="<div class=\"slides_container\">";
		for(var i=0;i<procurement.qualifiedAttachments.length; i++)
		{
			img =img+ '<div class="slide"><img style="width: 339px;height: 360px" src="data:image/png;base64,'+procurement.qualifiedAttachments[i].fileBase64+'"/></div>';

		}
		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
			'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
		slides.html(img);
		$('#slides1').slides();


		$("#addConfirmPopup").data("kendoWindow").open();
	} ;

	procurement.saveConfirmProcurement=function () {
		$("#k_window").data("kendoWindow").open().center();
		var vo={
			name:$("#name").val().trim(),
			providerId:$("#provider").data("kendoDropDownList").value(),
			providerName:$("#provider").data("kendoDropDownList").text(),
			format:$("#format").val().trim(),
			batch:$("#batch").val().trim(),
			procurementNum:$("#procurementNum").data("kendoNumericTextBox").value(),
			procurementDate:$("#procurementDate").data("kendoDatePicker").value(),
			expireDate:$("#expireDate").data("kendoDatePicker").value(),
			remark:$("#remark").val().trim(),
			hgAttachments:procurement.qualifiedAttachments
		};
		vo.type=procurement.type;
		$.ajax({
			url : fsn.getHttpPrefix() + "/procurement/add",
			type : "POST",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
			data : JSON.stringify(vo),
			success : function(returnValue) {
				$("#k_window").data("kendoWindow").close();
				if (returnValue.status == true) {
					lims.initNotificationMes('保存成功！', true);
					$("#addConfirmPopup").data("kendoWindow").close();
					$("#addPopup").data("kendoWindow").close();
					$("#raw_material").data("kendoGrid").dataSource.read();
				}else{
					lims.initNotificationMes('保存失败！', false);
				}
			}
		});
	} ;


	 procurement.cancelProcurement=function(){
		 $("#addPopup").data("kendoWindow").close();
	 };

	procurement.cancelConfirmProcurement=function(){
		$("#addConfirmPopup").data("kendoWindow").close();
	};


	/**
	 * 后续处理列表字段
	 * @type {*[]}
     */
	procurement.disposeColumns = [
		{field:"procurementName", title:"名称", width:"20%",filterable: false,editable: false},
		{field: "format", title: "规格", width:"15%",filterable: false,editable: false},
		{field: "batch", title: "批次", width:"18%",filterable: false,editable: false},
		{field: "disposeNum", title: "处理数量", width:"15%",filterable: false,editable: false},
		{field: "disposeDate", title: "处理日期", template: '#=disposeDate=fsn.formatGridDate(disposeDate)#',width:"15%",filterable: false,editable: false},
		{field: "disposeCause", title: "处理原因", width:"30%",filterable: false,editable: false},
		{field: "disposePlace", title: "处理地点", width:"25%",filterable: false,editable: false},
		{field: "handler", title: "处理人", width:"15%",filterable: false,editable: false},
		{field: "disposeMethod", title: "处理方式", width:"20%",filterable: false,editable: false},
		{field: "remark", title: "备注", width:"20%",filterable: false,editable: false},
		{ command: [
			{
				text: "处理凭证",
				click: function(e){
					e.preventDefault();
					var editRow = $(e.target).closest("tr");
					var temp = this.dataItem(editRow);
					procurement.showQualifiedImg(temp.disposeAttachments);
				}
			},
		], title:"操作", width: "17%" }
	];

	/**
	 * 获取后续处理列表数据源
	 * @param name
	 * @returns {kendo.data.DataSource|*}
     */
	function getDisposeDS(name){
		if(name!=""){
			name = encodeURIComponent(name);
		}

		DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
					type : "GET",
					url : function(options){
						return fsn.getHttpPrefix() +"/procurement/getDisposeList/"+ options.page + "/" + options.pageSize + "?name=" + name+"&type="+procurement.type ;
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




	/**
	  *打开新增后续处理窗口 
	  */
	 procurement.openAddDisposeWin=function(){
		 $("#disposeFileDiv").html("<input type=\"file\" id=\"disposeFile\" class=\"k-input k-textbox\" />");
		 procurement.buildUpload("disposeFile",procurement.disposeAttachments,"IMG",true);
		 $("#name_d").html(procurement.dispose.name);
		 $("#format_d").html(procurement.dispose.format);
		 $("#batch_d").html(procurement.dispose.batch);
		 $("#surplusNum_d").html(procurement.dispose.surplusNum);
		 $("#disposeNum").data("kendoNumericTextBox").value("");
		 $("#disposeDate").data("kendoDatePicker").value("");
		 $("#disposeCause").val("");
		 $("#disposePlace").val("");
		 $("#handler").val("");
		 $("#disposeMethod").val("");
		 $("#remark_d").val("");
		 procurement.disposeAttachments.length=0;
		 $("#addDisposePopup").data("kendoWindow").open();
	 };
	 
	
	 //保存后续处理信息
	 procurement.saveDispose = function(){
		 var num=$("#disposeNum").data("kendoNumericTextBox").value();
		 if(!num){
			 lims.initNotificationMes('处理数量不能为空!', false);
			 return;
		 }
		 if(num-procurement.dispose.surplusNum>0){
			 lims.initNotificationMes('处理数量不能大于剩余数量!', false);
			 return;
		 }
		 if(!$("#disposeDate").data("kendoDatePicker").value()){
			 lims.initNotificationMes('请选择处理日期!', false);
			 return;
		 }
		 if(""==$("#disposeCause").val().trim()){
			  lims.initNotificationMes('处理原因不能为空!', false);
			  return;
		 }
		 if(""==$("#disposePlace").val().trim()){
			  lims.initNotificationMes('处理地点不能为空!', false);
			  return;
		 }
		 if(""==$("#handler").val().trim()){
			 lims.initNotificationMes('处理人不能为空!', false);
			 return;
		 }
		 if(""==$("#disposeMethod").val().trim()){
			 lims.initNotificationMes('处理方式不能为空!', false);
			 return;
		 }
		 
		 if(procurement.disposeAttachments.length==0){
			 lims.initNotificationMes('请上传处理证明图片!', false);
			 return;
		 }
		 procurement.openConfirmDisposeWin();
	 };

	procurement.saveConfirmDisposeProcurement=function () {
		$("#k_window").data("kendoWindow").open().center();
		var vo={
			procurementId:procurement.dispose.id,
			procurementName:procurement.dispose.name,
			format:procurement.dispose.format,
			batch:procurement.dispose.batch,
			disposeNum:$("#disposeNum").data("kendoNumericTextBox").value(),
			disposeDate:$("#disposeDate").data("kendoDatePicker").value(),
			disposeCause:$("#disposeCause").val().trim(),
			disposePlace:$("#disposePlace").val().trim(),
			handler:$("#handler").val().trim(),
			disposeMethod:$("#disposeMethod").val().trim(),
			remark:$("#remark_d").val().trim(),
			disposeAttachments:procurement.disposeAttachments
		};
		vo.type=procurement.type;
		$.ajax({
			url : fsn.getHttpPrefix() + "/procurement/addDispose",
			type : "POST",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
			data : JSON.stringify(vo),
			success : function(returnValue) {
				$("#k_window").data("kendoWindow").close();
				if (returnValue.status == true) {
					lims.initNotificationMes('保存成功！', true);
					$("#addDisposePopup").data("kendoWindow").close();
					$("#addConfirmDisposePopup").data("kendoWindow").close();
					$("#raw_material").data("kendoGrid").dataSource.read();
					$("#raw_material_dispose").data("kendoGrid").dataSource.read();
				}else{
					lims.initNotificationMes('保存失败！', false);
				}
			}
		});
	} ;


	procurement.openConfirmDisposeWin=function () {
		$("#name_dc").html($("#name_d").html());
		$("#format_dc").html($("#format_d").html());
		$("#batch_dc").html($("#batch_d").html());
		$("#disposeNum_dc").html($("#disposeNum").data("kendoNumericTextBox").value());
		$("#disposeDate_dc").html($("#disposeDate").val());
		$("#disposeCause_dc").html($("#disposeCause").val());
		$("#disposePlace_dc").html($("#disposePlace").val());
		$("#handler_dc").html($("#handler").val());
		$("#disposeMethod_dc").html($("#disposeMethod").val());
		$("#remark_dc").val($("#remark_d").val());

		var slides = $("#slides2");
		var img ="<div class=\"slides_container\">";
		for(var i=0;i<procurement.disposeAttachments.length; i++)
		{
			img =img+ '<div class="slide"><img style="width: 339px;height: 360px" src="data:image/png;base64,'+procurement.disposeAttachments[i].fileBase64+'"/></div>';

		}
		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
			'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
		slides.html(img);
		slides.slides();

		$("#addConfirmDisposePopup").data("kendoWindow").open();
	} ;




	 procurement.cancelDispose=function(){
		 $("#addDisposePopup").data("kendoWindow").close();
	 };

	procurement.cancelConfirmDisposeProcurement=function(){
		$("#addConfirmDisposePopup").data("kendoWindow").close();
	};
	 
	//查询
	procurement.checkDispose = function(){
		var productName = $.trim($("#name_query_d").val());
		$("#raw_material_dispose").data("kendoGrid").setDataSource(getDisposeDS(productName));
		$("#raw_material_dispose").data("kendoGrid").refresh();
	};

	/**
	 * 初始化grid方法
	 * @param id
	 * @param columns
	 * @param ds
     * @param height
     */
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


	/**
	 * 初始化上传控件
	 * @author lxz 
	 */
	 procurement.buildUpload = function(id, attachments, flag,isMultiple){
			var selectLocal="";
			if(id=="qualified"){
				selectLocal="上传合格证明";
			}else if(id=="disposeFile"){
				selectLocal="上传处理证明";
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
	            multiple:isMultiple,
	            success: function(e){
			           	 if(e.response.typeEror){
			           		 lims.initNotificationMes('文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！', false);
			           		 return;
			           	 }
			           	 if(e.response.morSize){
			           		 lims.initNotificationMes('您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!', false);
			           		 return;
			           	 }
			             if (e.operation == "upload") {
			            	 lims.log("upload");
			                 lims.log(e.response);
			                 attachments.push(e.response.results[0]);
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
		           	 
	            },
	            error:function(e){
	           	 	
	            },
	      });
		};
	
	 //initialize();
});
