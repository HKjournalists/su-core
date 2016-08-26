$(document).ready(function() {
	/*销售案例管理 Create HY */

	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var sales = window.sales = window.sales || {};
	var httpPrefix = fsn.getHttpPrefix();
	var GRID_COLUMN = null;
	var GRID_DS = null;
	var DEL_WINDOW = null;
	var PROMOTION_GRID = null;
	var indexNo = 0; //销售案例序号
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-04-29
	 */
	function initSalesCase(){
		upload.buildGridByBoolbar("salesCaseGrid",GRID_COLUMN,GRID_DS,"300","toolbar_template_salescase");
		fsn.initConfirmWindow(delConfirmFun,null,"资料删除后将无法找回，确认需要删除此案例吗？","");
		DEL_WINDOW = $("#CONFIRM_COMMON_WIN").data("kendoWindow");
		PROMOTION_GRID = $("#salesCaseGrid").data("kendoGrid");
	}
	
	GRID_COLUMN = [
		     { title:fsn.l("Id"),editable: false,width:20, template:function(model){
		    	 return ++indexNo;
		     }},
		     { field: "salesCaseName", title:fsn.l("Sales Case Name"), width: 60},
		     { field: "salesDetails", title:fsn.l("Sales Details"),width: 120, template:function(model){
		    	 return '<span class="long_text">' + model.salesDetails + '</span>';
		     }},
		     { command: [{name:"Edit",
		       	    text:"<span class='k-icon k-edit'></span>" + fsn.l("Edit"),
		       	    click:function(e){
		       	    	e.preventDefault();
		       	    	var editModel = PROMOTION_GRID.dataItem($(e.currentTarget).closest("tr"));
		       	    	try {
               	    		$.cookie("cook_edit_salescase", JSON.stringify({calesId:editModel.id}), {
               	    			path : '/'
               	    		});
               	    	} catch (e) {}
               	    	window.location.href = "add_sales_case.html";
		       	    }
		     	},{name:"Delete",
		   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
		   	    click:function(e){
		   	    	e.preventDefault();
		   	    	var delItem = PROMOTION_GRID.dataItem($(e.currentTarget).closest("tr"));
					sales.delId = delItem.id;
		   	    	if(DEL_WINDOW != null) {
		   	    		DEL_WINDOW.open().center();
		   	    	}
		   	    }
		      }], title: fsn.l("Operation"), width: 35}                     
		   ];
	
	/* 促销活动 grid 的dataSource */ 
	GRID_DS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter) {
            			configure = filter.configure(options.filter);
            		}
            		indexNo = (options.page - 1) * options.pageSize;
            		return httpPrefix + "/sales/case/getListSalesCase/" + configure+"/" + options.page + "/" + options.pageSize;
				},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data;
            },
            total : function(returnValue) {       
                return returnValue.totals;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	

	/**
	 * 跳转至新增促销活动的方法
	 * @author tangxin 2015-05-03
	 */
	sales.addSalesCase = function(){
		window.location.href="add_sales_case.html";
	}
	
	/**
	 * 确定删除促销活动的button事件
	 * @author tangxin 2015-05-03
	 */
	function delConfirmFun(){
		if(sales.delId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/case/delById/" + sales.delId,
           type: "DELETE",
           dataType: "json",
           success: function(returnData) {
              if(returnData.result.status == "true"){
            	  fsn.initNotificationMes(fsn.l("删除成功！"), true);
            	  fsn.fleshGirdPageFun(GRID_DS);
              } else {
            	  fsn.initNotificationMes(fsn.l("删除失败！"), false);
              }
          }
		});
	}
	
	/* 初始化页面 */
	initSalesCase();
	
});