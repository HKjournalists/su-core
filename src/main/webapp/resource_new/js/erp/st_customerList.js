$(function(){
	var fsn = window.fsn = window.fsn || {};
	var common = fsn.common = fsn.common || {};
	var st_customer = fsn.st_customer = fsn.st_customer || {};
	$.extend(st_customer,common);
	
	/**
	 * 是否刷新总供应商数的标记
	 * @author ZhangHui 2015/5/5
	 */
	st_customer.isFreshTotalOfDealer = true;
	
	/**
	 * 页面初始化
	 */
	st_customer.initailize = function(){
		st_customer.initailize_common();
			$("#GRID_PROVIDER").show();
			
		/**
		 * [查看报告待处理企业]按钮Click事件
		 * @author ZhangHui 2015/5/4
		 */
		$("#reviewOnHandProducer").bind("click",function(){
			window.location.href = "/fsn-core/views/erp_new/basic/customer_onHandleProducerList.html?type=3&name=供应商";
		});
		$("#customer_sourc").click(function(){
//			window.location.href = "/fsn-core/views/erp_new/basic/customer_onHandleProducer.html?type=3&name=供应商";
		});
		st_customer.initConfirmWindow();
	};
	
	st_customer.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		st_customer.isFreshTotalOfDealer = false;
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/customer/3/allReglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/3/allList";
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
        schema: {
        	model: {
        		 fields: {
        			id:{type:"number"},
                 	name:{type:"string"},
                 	diyType:{
                 		id:{type:"number"},
                     	name:{type:"string"}
                 	},
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
            			st_customer.totalOfDealer = data.result.count;
            			st_customer.queryCount();
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
	
	/**
	 * 查看供销产品
	 * @author Zhanghui 2015/4/10
	 */
	st_customer.viewproinfo = function(organization, fromBusId, providername){
		
	   if(!organization || organization==""){
		   fsn.initNotificationMes("该企业未注册！", false);
		   return;
	   }
	   /* 校验该供应商下有无当前客户 */
	   var isExist = st_customer.validatCustomer(fromBusId);
//	   if(!isExist){
//		   fsn.initNotificationMes("检测到您并不是该供应商的客户，无法查看供销产品，确认无误后可删除该供应商！", false);
//		   return;
//	   }
	   /* 校验该供应商对当前客户有无供销产品 */
	   isExist = st_customer.validateProduct(fromBusId);
//	   if(!isExist){
//		   fsn.initNotificationMes("检测到该供应商并未给您供应任何产品！", false);
//		   return;
//	   }
	   /* 打开供销产品界面 */
	   //window.location.href = "/fsn-core/views/product_new/manage_product_bussiness_super.html?" + fromBusId+"&"+providername;
	   window.open("/fsn-core/views/product_new/manage_product_bussiness_superList.html?" + fromBusId+"&"+providername+"&"+organization);
	};
	
	
	/**
	 * 查找当前商超的 总供应商数、总产品数、待处理报告数
	 * @author ZhangHui 2015/5/1
	 */
	st_customer.queryCount = function(){
		
		var totalOfProduct = 0;
		var totalOfOnHandReports = 0;
		var totalOfReports = 0;
		$.ajax({
	        url: fsn.getHttpPrefix() + "/report/getCountsOfReports",
	        type: "GET",
	        dataType: "json",
	        data:{flag:true},
	        async: false,
	        contentType: "application/json; charset=utf-8",
	        success: function(returnValue) {
	            fsn.endTime = new Date();
	            if (returnValue.result.status == "true") {
	            	totalOfProduct = returnValue.totalOfProduct;
	            	totalOfOnHandReports = returnValue.totalOfOnHandReports;
	            	totalOfReports = returnValue.totalOfReports;
	            }
	        }
	    });
		
		$("#status_bar").html("当前位置：超级供应商管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总供应商数：" + st_customer.totalOfDealer + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总产品数：" + totalOfProduct + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"总报告数：" + totalOfReports + "&nbsp;&nbsp;&nbsp;&nbsp;" +
					"待处理报告数：" + totalOfOnHandReports);
	};
	
	st_customer.initConfirmWindow = function(){
    	var confirmWin = $("#CONFIRM_COMMON_WIN").data("kendoWindow");
    	if(!confirmWin){
    		$("#CONFIRM_COMMON_WIN").kendoWindow({
    			width: "450",
    			height:"auto",
    	        title: "确定",
    	        visible: false,
    	        resizable: false,
    	        draggable:false,
    	        modal: true
    		});
    		}

    	//add the "k-button" css onto buttons
    	$("#CONFIRM_COMMON_WIN button").addClass("k-button");
    	$("#confirm_no_btn").bind("click",function(){$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();});
    }
	st_customer.initailize();
});
