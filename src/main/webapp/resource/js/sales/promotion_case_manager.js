$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var promotion = window.promotion = window.promotion || {};
	var httpPrefix = fsn.getHttpPrefix();
	var GRID_COLUMN = null;
	var GRID_DS = null;
	var DEL_WINDOW = null;
	var PROMOTION_GRID = null;
	var indexNo = 0;
	
	/**
	 * 页面初始化方法
	 * @author tangxin 2015-04-29
	 */
	function initComponent(){
		upload.buildGridByBoolbar("promotionGrid",GRID_COLUMN,GRID_DS,"300","toolbar_template_promotion");
		fsn.initConfirmWindow(delConfirmFun,null,"资料删除后将无法找回，确认需要删除此促销活动详情吗？","");
		DEL_WINDOW = $("#CONFIRM_COMMON_WIN").data("kendoWindow");
		PROMOTION_GRID = $("#promotionGrid").data("kendoGrid");
	}
	
	GRID_COLUMN = [
		     { field:"id",title:fsn.l("Id"),editable: false,width:20, template:function(model){
		    	 return ++indexNo;
		     }},
		     { field: "name", title:fsn.l("Activity Name"), width: 60},
		     { field: "startDate", title:fsn.l("Activity Time"),width: 60,template:function(model){
		    	 var date = "";
		    	 if(model.startDate != null && model.startDate != ""){
		    		 date = fsn.formatGridDate(model.startDate);
		    		 if(model.endDate != null && model.endDate != ""){
		    			 date = (date + "~" +fsn.formatGridDate(model.endDate));
		    		 }
		    	 }
		    	 return date;
		     }},
		     { field: "area", title:fsn.l("Cover Area"), width: 100 },
		     { command: [{name:"Edit",
		       	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Edit"),
		       	    click:function(e){
		       	    	e.preventDefault();
		       	    	var editModel = PROMOTION_GRID.dataItem($(e.currentTarget).closest("tr"));
		       	    	try {
               	    		$.cookie("cook_edit_promotion", JSON.stringify({promotionId:editModel.id}), {
               	    			path : '/'
               	    		});
               	    	} catch (e) {}
               	    	window.location.href = "add_promotion_case.html";
		       	    }
		     	},{name:"Delete",
		   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
		   	    click:function(e){
		   	    	e.preventDefault();
		   	    	var delItem = PROMOTION_GRID.dataItem($(e.currentTarget).closest("tr"));
		   	    	promotion.delId = delItem.id;
		   	    	if(DEL_WINDOW != null) {
		   	    		DEL_WINDOW.open().center();
		   	    	}
		   	    }
		      }], title: fsn.l("Operation"), width: 60}                     
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
            		return httpPrefix + "/sales/promotion/getListPromotion/" + configure+"/" + options.page + "/" + options.pageSize;
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
	promotion.addPromotion = function(){
		window.location.href="add_promotion_case.html";
	}
	
	/**
	 * 确定删除促销活动的button事件
	 * @author tangxin 2015-05-03
	 */
	function delConfirmFun(){
		if(promotion.delId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/promotion/delById/" + promotion.delId,
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
	initComponent();
	
});