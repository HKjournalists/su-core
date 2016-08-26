$(function() {
	var fsn = window.fsn = window.fsn || {};
	var portal = fsn.portal = fsn.portal || {};
	var upload = fsn.upload = fsn.upload || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.product_id = null; // 产品id
	
	/**
	 * 获取产品id
	 * @author Zhanghui 2015/4/10
	 */
	try {
		portal.product_id = $.cookie("search_0_report_by_product").id;
    } catch(e) {}
    
    portal.initialize = function(){
    	upload.buildGridWioutToolBar("self_report_of_product_bussiness_super_grid", portal.templatecolumns, portal.selfDS);  // 自检
    	upload.buildGridWioutToolBar("censor_report_of_product_bussiness_super_grid",portal.templatecolumns, portal.censorDS);   // 送检
    	upload.buildGridWioutToolBar("sample_check_report_of_product_bussiness_super_grid",portal.templatecolumns, portal.sampleDS);// 抽检
    	
    	$("#pass").bind("click", portal.pass);
        $("#nopass").bind("click", portal.back);
        
        $("#tabstrip").kendoTabStrip({
            activate : portal.tabActivate,
            animation: {
                open: {effects: "fadeIn"}
            }
        }).data("kendoTabStrip").select(0);
	};
	
	portal.templatecolumns = [
	               {field: "id",title: "序号",width: "15px", filterable: false},
                   {field: "serviceOrder",title: "报告编号",width: "35px", filterable: false},
                   {field: "batchSerialNo",title: "批次",width: "30px", filterable: false},
                   {field: "producerName",title: "生产企业名称",width: "30px", filterable: false},
                   {field: "productionDate",title: "生产日期",template: '#=productionDate=fsn.formatGridDate(productionDate)#',width: "30px", filterable: false},
                   {width:"40px",title: fsn.l("Operation"),
					      template:function(e){
							ids = e.id;
							var tag="<a href='" + e.pdfUrl + "' target='_blank' class='k-button k-button-icontext k-grid-ViewDetail'>" +
									"<span class='k-icon k-edit'> </span>查看</a>";
							/**
							 * 4代表经销商提交，但未通过商超审核的报告
							 */
						    if (e.publishFlag == '4') {
								tag = tag  +
								"<a onclick='return fsn.portal.pass(" + e.id + ")' class='k-button k-button-icontext k-grid-Backout'>" +
										"通过</a>" +
								"<a onclick='return fsn.portal.back(" + e.id + ")' class='k-button k-button-icontext k-grid-Backout'>" +
										"退回</a>";
							}
							return tag;
						}
				    }];
		               	
	portal.sampleDS = new kendo.data.DataSource({
          			transport: {
          	            read : {
          	                type : "GET",
          	                async:false,
          	                url : function(options){
          	                	var configure = null;
          	                	if(options.filter){
          	            			configure = filter.configure(options.filter);
          	            		}
          	                	return portal.HTTP_PREFIX + "/report/getReports/" + configure + "/" + options.page + "/" 
          	                					+ options.pageSize + "/" + portal.product_id + "/sample";
          	                },
          	                dataType : "json",
          	                contentType : "application/json"
          	            },
          	        },
          	        schema: {
          	        	 data : function(returnValue) {
          	        		 return returnValue.data;  //响应到页面的数据
          	             },
          	             total : function(returnValue) {
          	            	 return returnValue.counts;   //总条数
          	             }         
          	        },
          	        batch : true,
          	        page:1,
          	        pageSize : 10, //每页显示个数
          	        serverPaging : true,
          	        serverFiltering : true,
          	        serverSorting : true
          		});
	               	
       	portal.censorDS = new kendo.data.DataSource({
      			transport: {
      	            read : {
      	                type : "GET",
      	                async:false,
      	                url : function(options){
      	                	var configure = null;
      	                	if(options.filter){
      	            			configure = filter.configure(options.filter);
      	            		}
      	                	return portal.HTTP_PREFIX + "/report/getReports/" + configure + "/" + options.page + "/" 
          								+ options.pageSize + "/" + portal.product_id + "/censor";
      	                },
      	                dataType : "json",
      	                contentType : "application/json"
      	            },
      	        },
      	        schema: {
      	        	 data : function(returnValue) {
      	        		 return returnValue.data;  //响应到页面的数据
      	             },
      	             total : function(returnValue) {
      	            	 return returnValue.counts;   //总条数
      	             }         
      	        },
      	        batch : true,
      	        page:1,
      	        pageSize : 10, //每页显示个数
      	        serverPaging : true,
      	        serverFiltering : true,
      	        serverSorting : true
      		});

   	portal.selfDS = new kendo.data.DataSource({
    			transport: {
    	            read : {
    	                type : "GET",
    	                async:false,
    	                url : function(options){
    	                	var configure = null;
    	                	if(options.filter){
    	            			configure = filter.configure(options.filter);
    	            		}
    	                	return portal.HTTP_PREFIX + "/report/getReports/" + configure + "/" + options.page + "/" 
											+ options.pageSize + "/" + portal.product_id + "/self";
    	                },
    	                dataType : "json",
    	                contentType : "application/json"
    	            },
    	        },
    	        schema: {
    	        	 data : function(returnValue) {
    	        		 return returnValue.data;  //响应到页面的数据
    	             },
    	             total : function(returnValue) {
    	            	 return returnValue.counts;   //总条数
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
 	 * 报告审核通过
 	 * @author Zhanghui 2015/4/9
 	 */
 	portal.pass = function(reportId){
 		$.ajax({
 	        url: portal.HTTP_PREFIX + "/report/operation/busSuperCheckReport/true/" + reportId,
 	        type: "GET",
 	        dataType: "json",
 	        success: function(returnValue) {
 	            if (returnValue.result.status == "true") {
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + reportId + ",审核通过！", true);
 	            }else if(returnValue.result.show){
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + reportId + "," + returnValue.result.errorMessage, false);
 	            }
 	        }
 	    });
 	};
 	
 	/**
 	 * 报告退回
 	 * @author Zhanghui 2015/4/9
 	 */
 	portal.back = function(reportId){
 		$.ajax({
 	        url: portal.HTTP_PREFIX + "/report/operation/busSuperCheckReport/false/" + reportId,
 	        type: "GET",
 	        dataType: "json",
 	        success: function(returnValue) {
 	            if (returnValue.result.status == "true") {
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + reportId + ",退回成功！", true);
 	            }else if(returnValue.result.show){
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + reportId + "," + returnValue.result.errorMessage, false);
 	            }
 	        }
 	    });
 	};
 	
 	/**
 	 * 选择选项卡事件
 	 * @author HuangYong 2015/4/10
 	 */
 	portal.tabActivate = function(e){
        switch (e.item.id) {
    		case "self_report_li":
    			$("#self_report").show();
    			$("#censor_report").hide();
    			$("#sample_check_report").hide();
    			break;
    		case "censor_report_li":
    			$("#self_report").hide();
    			$("#censor_report").show();
    			$("#sample_check_report").hide();
    			break;
    		case "sample_check_report_li":
    			$("#self_report").hide();
    			$("#censor_report").hide();
    			$("#sample_check_report").show();
    			break;
            default :
                $("#self_report").show();
    			$("#censor_report").hide();
    			$("#sample_check_report").hide();
    			break;
		}
    };
    
    /**
 	 * 刷新grid
 	 * @author HuangYong 2015/4/10
 	 */
    portal.refreshGrid = function(){
        if($("#self_report_li").attr("aria-selected")){
            portal.selfDS.read();
        }
        if($("#censor_report_li").attr("aria-selected")){
            portal.censorDS.read();
        }
        if($("#sample_check_report_li").attr("aria-selected")){
            portal.sampleDS.read();
        }
    };
 	
	portal.initialize();
});