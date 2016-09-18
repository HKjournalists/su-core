$(function(){
	var fsn = window.fsn = window.fsn || {};
	var record = fsn.record = fsn.record || {};
	record.editRecord=null;
	record.pId=null;
	record.procurement=null;
	var params = fsn.getUrlVars();
	record.pId=params[params[0]];
	
	record.initialize=function(){
		if(record.pId){
			$.ajax({
				url:fsn.getHttpPrefix()+ "/procurement/getProcurementById/"+record.pId,
				type:"GET",
				dataType:"json",
				success:function(returnValue){
					 if(returnValue.status==true){
						 record.procurement= returnValue.data;
					 }
				}
			});
		}
		buildGridWioutToolBar("raw_material_record",record.columns,getStoreDS(""),"400");
		record.initAddPopup();
		/* 初始化时间控件  */
		$("#useDate,#useDate_query").kendoDatePicker({
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
		$("#useNum").kendoNumericTextBox({
			spinners: false,
			format: "n0",
			placeholder: "使用数量只能为数字"
		});
		record.initRemarkPopup();
		record.initConfirmPopup();
		fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
	};

	/**
	 * 初始化新增使用记录弹窗
	 */
	record.initAddPopup = function() {
		var window = $("#addRecordPopup");
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				title: "添加使用记录",
				modal:true
				/*visible : false,
				 resizable : false*/
			});
		}
		window.data("kendoWindow").center();
	};

	record.initRemarkPopup = function() {
		var window = $("#remarkWindow");
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				title: "修改备注",
				modal:true
				/*visible : false,
				 resizable : false*/
			});
		}
		window.data("kendoWindow").center();
	};

	record.initConfirmPopup = function() {
		var window = $("#addRecordConfirmPopup");
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				title: "修改备注",
				modal:true
				/*visible : false,
				 resizable : false*/
			});
		}
		window.data("kendoWindow").center();
	};

	 function getStoreDS(useDate){

		 DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/procurement/getRecordList/"+ options.page + "/" + options.pageSize + "?procurementId="+record.pId+"&useDate=" + useDate ;
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

	
 
	 //查询
	record.check = function(){
	 	var useDate = $.trim($("#useDate_query").val());
	 	$("#raw_material_record").data("kendoGrid").setDataSource(getStoreDS(useDate));
	 	$("#raw_material_record").data("kendoGrid").refresh();
	 };

	/**
	 * 添加按钮方法
	 */
	record.add=function(){

		$("#name_r").html(record.procurement.name);
		$("#format_r").html(record.procurement.format);
		$("#batch_r").html(record.procurement.batch);
		$("#surplusNum_r").html(record.procurement.surplusNum);
		$("#expireDate_r").html(fsn.formatGridDate(record.procurement.expireDate));
		$("#useNum").data("kendoNumericTextBox").value("");
		$("#useDate").data("kendoDatePicker").value("");
		$("#purpose").val("");
		$("#remark").val("");
		$("#addRecordPopup").data("kendoWindow").open();
	} ;

	/**
	 * 保存使用记录
	 */
	record.saveRecord = function(){

		if(!$("#useDate").data("kendoDatePicker").value()){
			lims.initNotificationMes('请选择使用日期!', false);
			return;
		}
		if($("#useDate").data("kendoDatePicker").value()-record.procurement.expireDate>0){
			lims.initNotificationMes('使用日期不能大于过期日期!', false);
			return;
		}
		if($("#useDate").data("kendoDatePicker").value()<record.procurement.procurementDate){
			lims.initNotificationMes('采购日期为：'+fsn.formatGridDate(record.procurement.procurementDate)+'; 使用日期不能小于采购日期!', false);
			return;
		}
		var num=$("#useNum").data("kendoNumericTextBox").value();
		if(!num){
			lims.initNotificationMes('使用数量不能为空!', false);
			return;
		}
		if(num<0){
			lims.initNotificationMes('使用数量不能小于0', false);
			return;
		}
		if(num-record.procurement.surplusNum>0){
			lims.initNotificationMes('使用数量不能大于剩余数量!', false);
			return;
		}

		if(""==$("#purpose").val().trim()){
			lims.initNotificationMes('用途不能为空!', false);
			return;
		}
		record.openRecordConfirm();

	} ;

	record.openRecordConfirm=function () {
		$("#name_rc").html(record.procurement.name);
		$("#format_rc").html(record.procurement.format);
		$("#batch_rc").html(record.procurement.batch);
		$("#surplusNum_rc").html(record.procurement.surplusNum);
		$("#expireDate_rc").html(fsn.formatGridDate(record.procurement.expireDate));
		$("#useNum_rc").html($("#useNum").data("kendoNumericTextBox").value());
		$("#useDate_rc").html($("#useDate").val());
		$("#purpose_rc").html($("#purpose").val());
		$("#remark_rc").val($("#remark").val());
		$("#addRecordConfirmPopup").data("kendoWindow").open();
	};

	record.saveRecordConfirm = function(){
		$("#k_window").data("kendoWindow").open().center();
		var vo={
			procurementId:record.procurement.id,
			procurementName:record.procurement.name,
			useNum:$("#useNum").data("kendoNumericTextBox").value(),
			useDate:$("#useDate").data("kendoDatePicker").value(),
			purpose:$("#purpose").val().trim(),
			remark:$("#remark").val().trim()
		};
		$.ajax({
			url : fsn.getHttpPrefix() + "/procurement/addRecord",
			type : "POST",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
			data : JSON.stringify(vo),
			success : function(returnValue) {
				$("#k_window").data("kendoWindow").close();
				if (returnValue.status == true) {
					lims.initNotificationMes('保存成功！', true);
					$("#addRecordConfirmPopup").data("kendoWindow").close();
					$("#addRecordPopup").data("kendoWindow").close();
					$("#raw_material_record").data("kendoGrid").dataSource.read();
					record.procurement=returnValue.procurementInfo;
				}else{
					lims.initNotificationMes('保存失败！', false);
				}
			}
		});
	};

	record.cancelRecord = function(){
		$("#addRecordPopup").data("kendoWindow").close();
	} ;
	record.cancelRecordConfirm = function(){
		$("#addRecordConfirmPopup").data("kendoWindow").close();
	} ;
	record.goback=function () {
		window.location.href = fsn.record.backUrl;
	} ;
	
	 
	//显示字段
	 record.columns = [
	               {field:"procurementName", title:"名称", width:"20%",filterable: false,editable: false},
				   {field: "useDate", title: "使用时间", template: '#=useDate=fsn.formatGridDate(useDate)#',width:"15%",filterable: false,editable: false},
				   {field: "useNum", title: "使用量", width:"15%",filterable: false,editable: false},
				   {field: "purpose", title: "用途", width:"18%",filterable: false,editable: false},
				   {field: "remark", title: "备注", width:"20%",filterable: false,editable: false},
				   { command: [
					  {                      
	                      text: "修改备注",
						  click: function(e){
							  e.preventDefault(); 
							  var editRow = $(e.target).closest("tr");
							  var temp = this.dataItem(editRow);
							  record.editRecord=temp;
							  record.openRemark();
						  }
					  },
		            ], title:"操作", width: "18%" }
		         ];
	record.openRemark=function () {
		$("#remark_rcu").val(record.editRecord.remark);
		$("#remarkWindow").data("kendoWindow").open();
	} ;
	record.saveRemark=function () {
		var remark=$("#remark_rcu").val();
		$.ajax({
			url : fsn.getHttpPrefix() + "/procurement/updateRecordRemark/"+record.editRecord.id+"?remark="+remark,
			type : "POST",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
			success : function(returnValue) {
				if (returnValue.status == true) {
					lims.initNotificationMes('修改成功！', true);
					$("#remarkWindow").data("kendoWindow").close();
					$("#raw_material_record").data("kendoGrid").dataSource.read();
				}else{
					lims.initNotificationMes('修改失败！', false);
				}
			}
		});
	};
	record.cancelRemark=function () {
		$("#remarkWindow").data("kendoWindow").close();
	} ;
	 //initialize();
});
