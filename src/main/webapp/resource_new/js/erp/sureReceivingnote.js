$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	
	var suresureReceivingnote = fsn.suresureReceivingnote = fsn.suresureReceivingnote || {};
	
	$.extend(suresureReceivingnote,common);
	
	suresureReceivingnote.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/customer/12/reglistcheck/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/12/list/suresureReceivingnote";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			re_num:{type:"string"},
        			re_checkman:{type:"string"},
        			re_date:{type:"Date(yyyy-MM-dd)"},
                 	note:{type:"string"}
                 }
            },
        	data : function(data) {
        		if(data && data.result && data.result.listOfModel){
        			return data.result.listOfModel;  
        		}
                return null;
            },
            total : function(data) {//总条数
            	if(data && data.result && data.result.count){
            		return  data.result.count;
            	}
            	return 0;
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	suresureReceivingnote.columns = [
	               {field: "re_num",title: "收货单号", width:"20%",editor:false},
	               {field: "re_checkman",title: "检收员", width:"20%"},
	               {field: "re_date",title: "收货日期", width:"15%", filterable: false},
	               {field: "re_purchase_check",title:"状态", width:"15%", filterable: false},
				   {field: "userName",title:"操作人", width:"15%",},
				   { command: [{name : "确认收货",
           			 click : function(e) {
           				var tr = $(e.target).closest("tr");
           				var item = this.dataItem(tr);
           				if(item.outOfBillState == "已发货") {
           					fsn.initNotificationMes("已经发货！",false);
           					return;
           				}
           				
           				var model = {
           				};
           				model = {
           						re_num: item.re_num,
           				}
           				var win = eWidget.getPromptWindow1();
						win.data("kendoWindow").open().center();
						$("#confirmWindowSure").unbind("click");
						$("#cancelWindowNot").unbind("click");
						$("#confirmWindowSure").bind("click",function(){
							$.ajax({
           						url: fsn.getHttpPrefix() + "/erp/customer/12/checkReceivingnote",
           						type:"POST",
           						dataType:"json",
           						contentType: "application/json; charset=utf-8",
           						data:JSON.stringify(model),
           						success:function(data){
           							if(data && data.result){
										fsn.initNotificationMes("操作成功！", true);
           							}else{
										fsn.initNotificationMes("操作失败！", false);
									}
									suresureReceivingnote.datasource.read();
           						}
           					});
							win.data("kendoWindow").close();
						});
						$("#cancelWindowNot").bind("click",function(){
							win.data("kendoWindow").close();
						});
           			 }
           			 },{name:"预览",click:function(e){
           				 var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
           				 var addWin = eWidget.getWindow("RECEIVING_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, null);
           				 addWin.data("kendoWindow").open().center();
           				 suresureReceivingnote.detailBaseInfo(dataItem);
           			 }}
                       ],
                       title: "操作",
                       width: "25%"
                       }
	               ];
	suresureReceivingnote.productGrid = $("#RECEIVING_INFO_GRID").kendoGrid({
        dataSource: new kendo.data.DataSource({
     	                             data:[],
     	                             batch : true,
                                   	 page:1,
						             pageSize: 5,
						             serverPaging : true,
						             serverFiltering : true,
						             serverSorting : true
          							 }),
        width:450,
        height: 300,
        scrollable: true,
        sortable: true,
        selectable:true,
        pageable: {
        	refresh: false,
            pageSizes: 5,
			messages: fsn.gridPageMessage(),
        },
        toolbar: [
		          {template: kendo.template($("#PRODUCT_DETAIL").html())}
		         ],
        columns: [
                  {field: "product.name", title: "产品名称", width:"100px"},
                  {field: "product.format", title: "规格", width:"100px"},
                  {field: "po_ismode", title: "样品否", width:"100px",template:function(dataItem){
				  		if(dataItem.po_ismode){
							return "是";
						}else{
							return "否";
						}
				  }},
                  {field: "hasreport", title: "是否有质检报告", width:"150px",template:function(dataItem){
				  		if(dataItem.hasreport){
							return "是";
						}else{
							return "否";
						}
				  }},
                  {field: "po_isneedqr", title: "是否需要质检报告", width:"150px",template:function(dataItem){
				  		if(dataItem.po_isneedqr){
							return "是";
						}else{
							return "否";
						}
				  }},
                  {field: "po_receivenum", title: "收货数量", width:"100px"},
                  {field: "po_unit.name", title: "单位", width:"100px"},
                  {field: "po_mtype", title: "币种", width:"100px"},
                  {field: "po_batch", title: "批次", width:"100px"},
                  {field: "po_price", title: "单价", width:"100px"},
                  {field: "po_storage_address", title: "仓库地址", width:"100px"},
                  {field: "po_totalmoney", title: "单笔总金额", width:"120px"},
                  {field: "po_remark", title: "备注", width:"100px"},
                  ]
    });
	$("#RECEIVING_INFO_GRID").attr("style","width:700px;height:300px;");
	suresureReceivingnote.detailBaseInfo = function(Item){
		$("#detail_label_num_content").html(Item.re_num);
		$("#detail_label_source_content").html(Item.re_source);
		$("#detail_label_provider_content").html(Item.re_provide_num.name);
		$("#detail_label_receive_no_content").html(Item.re_outofbill_num);
		$("#detail_label_contactman_content").html(Item.re_contact_id.name);
		$("#detail_label_caddress_content").html(Item.re_contact_id.province+Item.re_contact_id.city+Item.re_contact_id.area+Item.re_contact_id.addr);
		$("#detail_label_contacttel_content").html(Item.re_contact_id.tel_1);
		$("#detail_label_ctime_content").html(Item.re_date);
		$("#detail_label_checkman_content").html(Item.re_checkman);
		$("#detail_lable_p_date_content").html(Item.re_pdate);
		$("#detail_label_ppay_content").html(Item.re_before_pay);
		$("#detail_label_fpay_content").html(Item.re_fact_pay);
		$("#detail_label_totalp_content").html(Item.re_totalmoney);
		$("#detail_lable_base_remarks_content").html(Item.re_remarks);
		$("#detail_label_state_content").html(Item.re_purchase_check);
		suresureReceivingnote.productGrid.data("kendoGrid").dataSource.data(Item.contacts);
	}
	
	$("#Simple_Grid").kendoGrid({
			dataSource: suresureReceivingnote.datasource,
			filterable: {
				extra: false,
				messages: {
					info: "显示需要项目:",
					and: "并且",
					or: "或者",
					filter: "过滤",
					clear: "清空"
				},
			 	operators: {
			      string: {
			    	  contains: "包含",
			    	  doesnotcontain: "不包含",
			    	  startswith: "以开头",
			    	  endswith: "以结尾",
			    	  eq: "等于",
			    	  neq: "不等于"
			      },
			      number: {
			    	  	eq: "等于",
			    		neq: "不等于",
			    		gte: "大于或等于",
			    		gt:  "大于",
			    		lte: "小于或等于",
			    		lt:  "小于",
			      }
			    }
				    }, 
			height: 428,
	        width: "auto",
	        sortable: true,
	        selectable: true,
	        resizable: false,
	        pageable: {
	            refresh: true,
	            pageSizes: [10, 20, 100],
	            messages: fsn.gridPageMessage(),
	        },
			toolbar : [ {
					template : kendo.template($("#toolbar_template").html())
				} ],
	        columns: suresureReceivingnote.columns
		});
		
//		$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(suresureReceivingnote.datasource);
//			    		$("#Simple_Grid").data("kendoGrid").refresh();
//			    	}
//				}
//			});
			
			initStandard = function(keywords){
				var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
					return fsn.getHttpPrefix() + "/erp/customer/12/list/suresureReceivingnote/search?keywords="+keywords;;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			re_num:{type:"string"},
        			re_checkman:{type:"string"},
        			re_date:{type:"Date(yyyy-MM-dd)"},
                 	note:{type:"string"}
                 }
            },
        	data : function(data) {
        		if(data && data.result && data.result.listOfModel){
        			return data.result.listOfModel;  
        		}
                return null;
            },
            total : function(data) {//总条数
            	if(data && data.result && data.result.count){
            		return  data.result.count;
            	}
            	return 0;
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
				//ds.read();
				return ds;
			}
})