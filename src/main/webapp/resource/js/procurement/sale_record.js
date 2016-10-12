$(function(){
	var fsn = window.fsn = window.fsn || {};
	var record = fsn.record = fsn.record || {};
	record.editRecord=null;
	record.pId=null;
	record.procurement=null;
	var params = fsn.getUrlVars();
	record.pId=params[params[0]];	//获取请求URL携带参数
	
	record.initialize=function(){
		record.getSaleProcurementById(record.pId);
		buildGridWioutToolBar("raw_material_record",record.columns,getStoreDS(""),"400");
		record.initAddPopup();
		/* 初始化时间控件  */
		$("#saleDate,#saleDate_query").kendoDatePicker({
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
		$("#saleNum").kendoNumericTextBox({
			spinners: false,
			format: "n0",
			placeholder: "使用数量只能为数字"
		});
		record.initRemarkPopup();
		record.initConfirmPopup();
		fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
	};

	/**
	 * 获取销售记录列表
	 * @param id
	 */
	record.getSaleProcurementById = function(id){
		if(id){
			$.ajax({
				url:fsn.getHttpPrefix()+ "/procurement/getSaleProcurementById/"+id,
				type:"GET",
				dataType:"json",
				success:function(returnValue){
					if(returnValue.status==true){
						record.procurement= returnValue.data;
					}
				}
			});
		}
	};


	/**
	 * 初始化新增使用记录弹窗
	 */
	record.initAddPopup = function() {
		var window = $("#addRecordPopup");
		if (!window.data("kendoWindow")) {
			window.kendoWindow({
				title: "添加销售记录",
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
				title: "销售确认",
				modal:true
				/*visible : false,
				 resizable : false*/
			});
		}
		window.data("kendoWindow").center();
	};

	 function getStoreDS(saleDate){
		 DISHSNO_DS = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
						return fsn.getHttpPrefix() +"/procurement/getSaleRecordList/"+ options.page + "/" + options.pageSize + "?onlineSaleId="+record.pId+"&saleDate=" + saleDate ;
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
	 	var saleDate = $.trim($("#saleDate_query").val());
	 	$("#raw_material_record").data("kendoGrid").setDataSource(getStoreDS(saleDate));
	 	$("#raw_material_record").data("kendoGrid").refresh();
	 };

	/**
	 * 添加按钮方法
	 */
	record.add=function(){
		$("#saleDate").data("kendoDatePicker").value("");
		$("#saleNum").val("");
		$("#remark").val("");
		$("#addRecordPopup").data("kendoWindow").open();
	} ;

	/**
	 * 保存使用记录
	 */
	record.saveRecord = function(){

		if(!$("#saleDate").data("kendoDatePicker").value()){
			lims.initNotificationMes('请选择销售日期!', false);
			return;
		}
		if($("#saleDate").data("kendoDatePicker").value()-record.procurement.expireDate>0){
			lims.initNotificationMes('销售日期不能大于过期日期!', false);
			return;
		}
		if($("#saleDate").data("kendoDatePicker").value()<record.procurement.procurementDate){
			lims.initNotificationMes('采购日期为：'+fsn.formatGridDate(record.procurement.procurementDate)+'; 销售日期不能小于采购日期!', false);
			return;
		}
		var num=$("#saleNum").val().trim();
		if(!num){
			lims.initNotificationMes('销售数量不能为空!', false);
			return;
		}
		if(num<0){
			lims.initNotificationMes('销售数量不能小于0', false);
			return;
		}
		if(num-record.procurement.surplusNum>0){
			lims.initNotificationMes('销售数量不能大于剩余数量!', false);
			return;
		}

		record.openRecordConfirm();

	} ;

	record.openRecordConfirm=function () {
		$("#saleNum_rc").html($("#saleNum").data("kendoNumericTextBox").value());
		$("#saleDate_rc").html($("#saleDate").val());
		$("#remark_rc").attr("disabled",true).val($("#remark").val());
		$("#addRecordConfirmPopup").data("kendoWindow").open();
	};

	record.saveRecordConfirm = function(){
		$("#k_window").data("kendoWindow").open().center();
		var vo={
				onlineSaleId:record.procurement.id,
				onlineSaleName:record.procurement.name,
				saleNum:$("#saleNum").data("kendoNumericTextBox").value(),
				saleDate:$("#saleDate").data("kendoDatePicker").value(),
				remark:$("#remark").val().trim()
		};
		$.ajax({
			url : fsn.getHttpPrefix() + "/procurement/addSaleRecord",
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
					record.procurement=returnValue.onlineSaleGoods;
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
		window.location.href = "online_sale_goods.html";
	} ;
	
	 
	//显示字段
	 record.columns = [
	               {field:"onlineSaleName", title:"名称", width:"20%",filterable: false,editable: false},
				   {field: "saleDate", title: "销售时间", template: '#=saleDate=fsn.formatGridDate(saleDate)#',width:"15%",filterable: false,editable: false},
				   {field: "saleNum", title: "销售量", width:"15%",filterable: false,editable: false},
				   {field: "remark", title: "备注", width:"20%",filterable: false,editable: false}
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
