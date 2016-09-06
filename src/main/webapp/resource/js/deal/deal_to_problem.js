$(function(){
  	var fsn = window.fsn = window.fsn || {};
    var deal= fsn.deal = fsn.deal ||{};

    deal.initialize = function(){
    	deal.InitGrid("DEAL_TO_GRID",true);
	};
	
	/**
  	 * 列表显示grid
  	 */
	deal.InitGrid  = function(id,flag){
		var columns = deal.getShowColumns();
  		$("#"+id).kendoGrid({
  		  columns: columns,
          editable: "popup",
          pageable: {
        	  refresh: true,
        	  pageSizes: [10, 20,50,80,100],
        	  messages: fsn.gridPageMessage(),
          },
          dataSource: []
  		});
  		deal.InitGridDataSource();
  	};
  	deal.getShowColumns = function(){
  		var columns = [
			          { field: "productName",title:"产品名称", width:"20%",filterable: false,editable: false,hidden:false},
			          { field: "barcode",title:"产品条码", width:"20%",filterable: false,editable: false},
			          { field: "productionDate",title:"生产日期", width:"20%", template : '#= fsn.formatGridDate(productionDate)#',filterable: false,editable: false},
			          { field: "problem",title:"问题类型", width:"30%",filterable: false,editable: false},
			          { field: "createTime",title:"发现时间", width:"30%",template : '#= fsn.formatGridDate(createTime)#',filterable: false,editable: false},
			          { field: "dealTime",title:"要求处理时间", width:"30%",template : '#= fsn.formatGridDate(dealTime)#',filterable: false,editable: false},
			          { field: "dealType",title:"状态", width:"20%",filterable: false,editable: false,template:function(e){
			        	  if(e.dealType=='未处理'){
			        		  return "<font color=red>"+e.dealType+"</font>" ; 
			        	  }else{
			        		  return "<font color=green>"+e.dealType+"</font>" ; ; 
			        	  }
			        	 
			          }},
			          { field: "remark",title:"备注", width:"30%",filterable: false,editable: false},
			          { field: "dealStatus",title:"操作", width:"40%",template:function(e){
		  		          var tag="";
		  		            	if(e.dealType=='未处理'){
//		  		            		tag += "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick='return fsn.deal.updateDeal("+e.id+")'>";
//		  		            		tag += "已处理</a>";
		  		            		tag += "<a class='k-button k-button-icontext k-grid-ViewDetail' onclick=fsn.deal.updateDeal_Problem('"+e.barcode+"','"+e.problemType+"')>赶紧去处理</a>";
		  		            		tag += "<button class='k-button k-button-icontext k-grid-ViewDetail' onclick=fsn.deal.updateDeal("+e.id+")><font color=blue>已处理</font></button>";
		  		            	}else{
		  		            		tag = "";
		  		            	}
		  		            	 return tag;
		  		           }
			          }
//			          { command: [{ hidden: true,name: "dealStatus",text:"已处理",hidden:true,click:function(e){
//			        	  
//			        	  e.preventDefault();
//        	        		 var tr = $(e.target).closest("tr");
//			          		 var temp = this.dataItem(tr);
//			          		deal.updateDeal(temp);
//			          }
//			          }],title:"操作",width:"20%" 
			        	  
			          ];
  		return columns;
  	};
  	deal.InitGridDataSource = function(){
  		var barcode = $("#barcode").val();
  	var dataSource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                data:{barcode:barcode},
	                url : function(options){
						return fsn.getHttpPrefix() +"/deal/getDealToProblemList/"+ options.page + "/" + options.pageSize;
					},
	                dataType : "json",
	                contentType : "application/json"
	            }
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
	        pageSize : 20, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		   });
		var grid = $("#DEAL_TO_GRID").data("kendoGrid");
	grid.setDataSource(dataSource);
	grid.refresh();
	};
	deal.check = function(){
		deal.InitGridDataSource();
	};
	deal.iSClean = function(){
		$("#barcode").val('');
	}; 
	deal.updateDeal = function(id){
		$.ajax({
	          url: fsn.getHttpPrefix() + "/deal/editDealToProblem/"+id,
	          type: "POST",
	          contentType: "application/json; charset=utf-8",
	          success: function(returnVal){
	        	  if(returnVal.status == true){
	            	  fsn.initNotificationMes("问题处理完成!", true);
	            	  deal.InitGridDataSource();
	              }
	          }
	      });
	};
	
	deal.updateDeal_Problem = function(barcode,type){
		var canshu = "?"+$.md5("dealProblem")+"&"+barcode+"&"+$.md5(barcode);
//		 try {
//		    	var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
//		    	var pid = arrayParam[0]; // 产品id（原始id，未被编码）
//		    	var orig_pidmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
//		    	portal.edit_id = portal.md5validate(pid,orig_pidmd5);
//		    } catch (e) {}
		
		if(type == "ZERO"){
			window.location.href = "/fsn-core/views/portal_new/product.html"+canshu;
		}else if(type == "ONE"){
			
		}else if(type == "TWO"){
			window.location.href = "/fsn-core/views/report_new/input_report_dealer.html"+canshu;
		}else if(type == "THREE"){
			window.location.href = "/fsn-core/views/report_new/input_report_dealer.html"+canshu;
		}else if(type == "FOUR"){
			
		}else if(type == "FIVE"){
			
		}else{
			
		};
		
	};
	deal.initialize();
});