$(function(){
	var fsn = window.fsn = window.fsn || {};
	var common = fsn.common = fsn.common || {};
	var st_customer = fsn.st_customer = fsn.st_customer || {};
	
	$.extend(st_customer,common);
	
	/**
	 * 页面初始化
	 */
	st_customer.initailize = function(){
		st_customer.initailize_common();
		
		/**
		 * [返回]按钮Click事件
		 * @author ZhangHui 2015/5/4
		 */
		$("#back").bind("click",function(){
			window.location.href = "/fsn-core/views/erp_new/basic/customer.html?type=3&name=供应商";
		});
	};
	
	/**
	 * 数据源
	 */
	st_customer.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
					var configure = null;
                	if(options.filter){
                		configure = filter.configure(options.filter);
                	};
                	return fsn.getHttpPrefix() + "/erp/customer/3/list/onHandProducer/"+configure;
				},
                dataType : "json",
                contentType : "application/json"
            },
        },
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
	   if(!isExist){
		   fsn.initNotificationMes("检测到您并不是该供应商的客户，无法查看供销产品，确认无误后可删除该供应商！", false);
		   return;
	   }
	   /* 校验该供应商对当前客户有无供销产品 */
	   isExist = st_customer.validateProduct(fromBusId);
	   if(!isExist){
		   fsn.initNotificationMes("检测到该供应商并未给您供应任何产品！", false);
		   return;
	   }
	   /* 打开供销产品界面 */
	   //window.location.href = "/fsn-core/views/product_new/manage_product_bussiness_super_onhandleProduct.html?" + fromBusId + "&" + providername
	   window.open("/fsn-core/views/product_new/manage_product_bussiness_super_onhandleProduct.html?" + fromBusId + "&" + providername);
	};
	
	st_customer.initailize();
});
