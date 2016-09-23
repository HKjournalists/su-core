$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var sureoutofgood = fsn.sureoutofgood = fsn.sureoutofgood || {};
	$.extend(sureoutofgood,common);
	
	sureoutofgood.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/outOfGoods/reglistcheck/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};//filter zl
                	return fsn.getHttpPrefix() + "/erp/outOfGoods/list/sureoutofgood";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 outOfBillNo:{
        				 editable : false,
        			 },
        			 outDate:{type:"string"},
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	//初始化商品,根据出货单号
	sureoutofgood.initOutGoodItemDS = function(outGoodNum){
    	if(outGoodNum){
    		sureoutofgood.outGoodItemNum = outGoodNum;
    	}else{
    		sureoutofgood.outGoodItemNum = 0;
    	}
    	sureoutofgood.outGoodItemFirstPageFlag = 1;
    	sureoutofgood.outGoodItemDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		if(sureoutofgood.outGoodItemNum == 0){
	            			return;
	            		}
	            		if(sureoutofgood.outGoodItemFirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
	            			options.page=1;
	            			options.pageSize=5;
	            			sureoutofgood.outGoodItemFirstPageFlag=0;
	            		}
	            		return fsn.getHttpPrefix() + "/erp/outOfGoods/findOutGoods/" + options.page + "/" + options.pageSize + "/" + sureoutofgood.outGoodItemNum;
	            	},
	            	type:"GET",
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(data) {
	            	if(data && data.result && data.result.listOfModel){
	        			return data.result.listOfModel;  
	        		}
	                return null;
	            },
	            total : function(data) {       
	            	if(data && data.result && data.result.count){
	            		return  data.result.count;
	            	}
	            	return 0;
	            }     
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	sureoutofgood.columns = [
	               {field: "outOfBillNo", title: "出货单号", width:"20%",editor:false},
	               {field: "outDate", title: "发货日期", width:"20%", filterable: false},
	               {field: "customer.name", title: "客户名称", width:"25%"},
	               {field: "outOfBillState", title: "出货单状态", width:"15%"},
				   {field: "userName", title: "操作人", width:"15%"},
	               { command: [{name : "确认发货",
           			 click : function(e) {
           				var item = this.dataItem($(e.currentTarget).closest("tr"));
           				if(item.outOfBillState == "已交易完成") {
           					fsn.initNotificationMes("已经交易完成",false);
           					return;
           				}
           				for(var i = 0;i<item.contacts.length;i++){
           					var isNeedIns = item.contacts[i].inspectionReport;
           					var isHasIns = item.contacts[i].hasInspectionReport;
           					if(isNeedIns == true&&isHasIns == false){
            					fsn.initNotificationMes("无对应质检报告，请联系相关人员上传", false);
            					return;
            				}
           				}
           				var model = {
           				};
           				model = {
           						outOfBillNo: item.outOfBillNo,
           				}
           				var win = eWidget.getPromptWindow1();
        				win.data("kendoWindow").open().center();
        				$("#confirmWindow").unbind("click");
        				$("#cancelWindow").unbind("click");
        				$("#confirmWindow").bind("click",function(){
        					$.ajax({
           						url: fsn.getHttpPrefix() + "/erp/outOfGoods/checkOne",
           						type:"POST",
           						dataType:"json",
           						contentType: "application/json; charset=utf-8",
           						data:JSON.stringify(model),
           						success:function(data){
           							if(data && data.result==true){
										fsn.initNotificationMes("操作成功！", true);
           								console.log("OK");
           							}else{
										fsn.initNotificationMes("操作失败！", false);
									}
           							sureoutofgood.datasource.read();
           						}
           					});
        					win.data("kendoWindow").close();
        				});
        				$("#cancelWindow").bind("click",function(){
        					win.data("kendoWindow").close();
        				});
           			 }
           			 },{name:"预览",click:function(e){
           				var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
           				sureoutofgood.initOutGoodItemDS(dataItem.outOfBillNo);
       				    $("#OUTOFGOODS_INFO_GRID").data("kendoGrid").setDataSource(sureoutofgood.outGoodItemDS);
       				    sureoutofgood.detailOutOfGoodsInfo(dataItem);
           				var addWin = eWidget.getWindow("OUTOFGOODS_DETAIL_INFO","详细信息",common.SIMPLE_MODEL_NAME, common.realObj.win_width == null ? null:common.realObj.win_width);
          				addWin.data("kendoWindow").open().center();
           			 }}
                       ],
                       title: "操作",
                       width: "25%"
                       }
	               ];
	sureoutofgood.productGrid = $("#OUTOFGOODS_INFO_GRID").kendoGrid({
        dataSource: new kendo.data.DataSource({
     	                             data:[],
     	                             batch : true,
                                   	 page:1,
						             pageSize: 5,
						             serverPaging : true,
						             serverFiltering : true,
						             serverSorting : true
          							 }),
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
                  {field: "no", title: "商品编号", width:"170px"},
                  {field: "name", title: "商品名称", width:"100px"},
                  {field: "specification", title: "商品规格", width:"100px"},
                  {field: "batchNo", title: "批次", width:"100px"},
                  {field: "mode", title: "样品否", width:"100px",template:'#=(mode?"是":"否")#'},
                  {field: "hasInspectionReport", title: "是否有质检报告", width:"150px",template:'#=(hasInspectionReport?"是":"否")#'},
                  {field: "inspectionReport", title: "是否需要质检报告", width:"150px",template:'#=(inspectionReport?"是":"否")#'},
                  {field: "outNumber", title: "出货数量", width:"100px"},
//                  {field: "realOutNumber", title: "实出数量", width:"100px"},
                  {field: "unit.name", title: "单位", width:"100px"},
                  {field: "firstStorage.name", title: "首选仓库", width:"100px"},
                  {field: "category.name", title: "商品分类", width:"100px"},
                  {field: "type.name", title: "商品类型", width:"100px"},
                  {field: "moneyType", title: "币种", width:"100px"},
                  {field: "unitPrice", title: "单价", width:"100px"},
                  {field: "totalAmount", title: "单笔总金额", width:"120px"},
                  {field: "note", title: "备注", width:"100px"},
                  ]
    });
	$("#OUTOFGOODS_INFO_GRID").attr("style","width:800px;height:300px;");
	sureoutofgood.detailOutOfGoodsInfo = function(Item){
		$("#detail_label_num_content").html(Item.outOfBillNo);
		$("#detail_label_source_content").html(Item.source);
		$("#detail_label_customer_content").html(Item.customer.name);
		$("#detail_label_sourceno_content").html(Item.re_outofbill_num);
		$("#detail_label_contactman_content").html(Item.contactInfo.name);
		$("#detail_label_caddress_content").html(Item.contactProvince+Item.contactCity+Item.contactArea+Item.contactAddr);
		$("#detail_label_contacttel_content").html(Item.contactTel);
		$("#detail_label_ctime_content").html((new Date(Item.outDate)).format("YYYY-MM-dd"));
		$("#detail_label_piecetop_content").html(Item.invoice);
		$("#detail_lable_code_content").html(Item.contactZipcode);
		$("#detail_label_totalp_content").html(Item.totalPrice);
		$("#detail_label_transport_content").html(Item.transportation);
		$("#detail_lable_base_remarks_content").html(Item.note);
		$("#detail_label_state_content").html(Item.outOfBillState);
	}
			$("#Simple_Grid").kendoGrid({
			dataSource: sureoutofgood.datasource,
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
	        columns: sureoutofgood.columns
		});
		
//		$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(sureoutofgood.datasource);
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
					return fsn.getHttpPrefix() + "/erp/outOfGoods/list/sureoutofgood/search?keywords="+keywords;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			 outOfBillNo:{
        				 editable : false,
        			 },
        			 outDate:{type:"string"},
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
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
