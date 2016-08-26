$(function(){
  	var fsn = window.fsn = window.fsn || {};
  	var dishsNoShow= fsn.dishsNoShow = fsn.dishsNoShow ||{};
  	
  	dishsNoShow.initialize = function(){
  		 dishsNoShow.InitGrid("DISHSNOSHOW_GRID",true);
  		dishsNoShow.initKendoWindow("CONFIRM_COMMON","320px", "150px", "保存提示！", false,true,false,["Close"],null,"");
  		$("#DIV_DISHSNOSHOW_GRID input[id=showTime],#DIV_DISHSNOSHOW_ADD_GRID input[id=showTime],#DIV_DISHSNOSHOW_EDIT_GRID input[id=showTime]").kendoDatePicker({
  	        format: "yyyy-MM-dd",
  	        height: 30,
  	        culture: "zh-CN",
//  	        max: new Date(),
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
  	};
  	/**
  	 * 列表显示grid
  	 */
  	dishsNoShow.InitGrid  = function(id,flag){
  	    var title;
  		var columns;
  	    if(id=='DISHSNOSHOW_GRID'){
  	    	$("#DIV_DISHSNOSHOW_GRID").show();
  	    	$("#DIV_DISHSNOSHOW_ADD_GRID").hide();
  	    	$("#DIV_DISHSNOSHOW_EDIT_GRID").hide();
  			title = "菜品管理>>今日菜品管理";
  			columns = dishsNoShow.getShowColumns();
  		}else if(id == 'DISHSNOSHOW_ADD_GRID'){
  			$("#DIV_DISHSNOSHOW_GRID").hide();
  			$("#DIV_DISHSNOSHOW_ADD_GRID").show();
  			$("#DIV_DISHSNOSHOW_EDIT_GRID").hide();
  			title = "菜品管理>>今日菜品管理>>新增菜品";
  			columns = dishsNoShow.getShowAddColumns();
  		}else{
  			$("#DIV_DISHSNOSHOW_GRID").hide();
  			$("#DIV_DISHSNOSHOW_ADD_GRID").hide();
  			$("#DIV_DISHSNOSHOW_EDIT_GRID").show();
  			title = "菜品管理>>今日菜品管理>>修改菜品";
  			columns = dishsNoShow.getShowAddColumns();
  		}
  		$("#status_bar").html(title);
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
  		dishsNoShow.InitGridDataSource(id,flag);
  	};
  	dishsNoShow.getShowColumns = function(){
  		var columns = [
			          { field: "showTime",title:"展示日期", width:"20%",filterable: false,editable: false},
			          { field: "dishsName",title:"菜品名称", width:"20%",filterable: false,editable: false},
			          { field: "alias",title:"别名", width:"20%",filterable: false,editable: false},
			          { field: "baching",title:"配料", width:"30%",filterable: false,editable: false},
			          { field: "sampleFlag",title:"是否已留样", width:"10%",template:function(e){
	  		          var tag="";
	  		            	if(e.sampleFlag==1){
	  		            		tag = "已留样";
	  		            	}else{
	  		            		tag = "无";
	  		            	}
	  		            	 return tag;
	  		           }}
//			          { command: [{ name: "edit" }],title:"操作",width:"20%" }
			          ];
  		return columns;
  	};
  	dishsNoShow.getShowAddColumns = function(){
  		var columns = [
  		               { field: "dishsName",title:"菜品名称", width:"20%",filterable: false,editable: false},
  		               { field: "alias",title:"别名", width:"20%",filterable: false,editable: false},
  		               { field: "baching",title:"配料", width:"30%",filterable: false,editable: false},
  		               { field: "sampleFlag",title:"全选<input type='checkbox'  onclick=fsn.dishsNoShow.getAllCheckbox('sampleFlag',this.checked) />是否已留样", width:"15%",template:function(e){
  		            	 var tag="";
  		            	 if(e.sampleFlag ==1){
  		            		 tag="<input type='checkbox' class='sampleFlag' id='sampleFlag_"+e.id+"' value='"+e.sampleFlag+"' onclick='fsn.dishsNoShow.getCheckbox(this.id,this.checked)' checked='true'/>";
  		            	 }else{
  		            		 tag="<input type='checkbox' class='sampleFlag' id='sampleFlag_"+e.id+"' value='0' onclick='fsn.dishsNoShow.getCheckbox(this.id,this.checked)'/>";
  		            	 }
  		            		 return tag;
  		               }},
  		               { field: "showFlag",title:"全选<input type='checkbox'  onclick=fsn.dishsNoShow.getAllCheckbox('showFlag',this.checked) />是否展示", width:"15%",template:function(e){
    		            	 var tag="";
    		            	 if(e.showFlag==1){
    		            		 tag="<input type='checkbox' class='showFlag' id='showFlag_"+e.id+"' value='"+e.showFlag+"' onclick='fsn.dishsNoShow.getCheckbox(this.id,this.checked)'checked='true'/>";
    		            	 }else{
    		            		 tag="<input type='checkbox' class='showFlag' id='showFlag_"+e.id+"' value='0' onclick='fsn.dishsNoShow.getCheckbox(this.id,this.checked)' />";
    		            	 }
      		            		 return tag;
      		               }}
  		               ];
  		return columns;
  	};
  	dishsNoShow.getCheckbox = function(id,checked){
  		if(checked){
  			$("#"+id).attr("value","1");
  		}else{
  			$("#"+id).attr("value","0");
  		}
  	};
  	dishsNoShow.getAllCheckbox = function(ID,checked){
  		var boxs = $("."+ID);
  		for(var k in boxs){
  			if(checked){
  				boxs[k].checked=true;
  				boxs[k].value=1;
  			}else{
  				boxs[k].checked=false;
  				boxs[k].value=0;
  			}
  		}
  	};
dishsNoShow.submit = function(){
  		var dishsNoShowVO = dishsNoShow.getCreateInstance();
  	  $.ajax({
          url: fsn.getHttpPrefix() + "/dishsNo/dishsNoShowSave",
          type: "POST",
          contentType: "application/json; charset=utf-8",
          data:JSON.stringify(dishsNoShowVO),
          success: function(returnVal){
        	  var id = $("#submitFlag").val();
        	  var message = "修改";
        	  if(id=='DISHSNOSHOW_ADD_GRID'){
        		  message = "新增";
        	  }
        	  if(returnVal.status == true){
          		$("#CONFIRM_COMMON").data("kendoWindow").close();
          		  fsn.initNotificationMes("菜品信息"+message+"成功!", true);
          		 dishsNoShow.InitGrid("DISHSNOSHOW_GRID",true);
              }else {
            	  fsn.initNotificationMes("菜品信息"+message+"失败!", false);
              }
          }
      });
  	};
  	dishsNoShow.getCreateInstance = function(){
  		var id = $("#submitFlag").val();
  		var grid = $("#"+id).data("kendoGrid")._data;
  		var showTime = $("#DIV_"+id+" input[id='showTime']").val();
  		var data = [];
  		for(var i in grid){
  			var shwoFlag = $("#showFlag_"+grid[i].id).val();
  			var sampleFlag = $("#sampleFlag_"+grid[i].id).val();
  			if(shwoFlag==1||grid[i].showId!=null){
  				var vo = {
  						id :grid[i].id,
  						showId :grid[i].showId,
  						showTime:showTime,
  						sampleFlag:sampleFlag,
  						showFlag:shwoFlag
  				};
  				data.push(vo);
  			}
  		}
  		var volist = {voList:data}; 
  		return volist;
  	};
  	/**
  	 * 查询
  	 */
  	dishsNoShow.check = function(id,flag){
  		dishsNoShow.InitGridDataSource(id,flag);
  	};
  	/**
  	 * 获取后台数据
  	 */
  	dishsNoShow.InitGridDataSource = function(id,flag){
  		var showTime = "";
  		var dishsName = "";
  		if(id=='DISHSNOSHOW_GRID'){
  			showTime = $("#DIV_DISHSNOSHOW_GRID input[id='showTime']").val();
  			dishsName = $("#DIV_DISHSNOSHOW_GRID input[id='dishsName']").val();
  		}else if(id=='DISHSNOSHOW_ADD_GRID'){
  			showTime = $("#DIV_DISHSNOSHOW_ADD_GRID input[id='showTime']").val();
  			dishsName = $("#DIV_DISHSNOSHOW_ADD_GRID input[id='dishsName']").val();
  		}else{
  			showTime = $("#DIV_DISHSNOSHOW_EDIT_GRID input[id='showTime']").val();
  			dishsName = $("#DIV_DISHSNOSHOW_EDIT_GRID input[id='dishsName']").val();
  		}
  		 var dataSource = new kendo.data.DataSource({
 			transport: {
 				read : {
 	                type : "GET",
 	                data:{dishsName:dishsName,showTime:showTime,flag:flag},
 	                url : function(options){
 						return fsn.getHttpPrefix() +"/dishsNo/getListDishsNoShow/"+ options.page + "/" + options.pageSize;
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
  		var grid = $("#"+id).data("kendoGrid");
		grid.setDataSource(dataSource);
		grid.refresh();
  	};
  	/**
  	 * description：菜品新增 或者修改
  	 * author:wubiao
  	 * date ： 2016.7.21
  	 */
  	dishsNoShow.add = function(flag){
  		var showTime = $("#DIV_DISHSNOSHOW_GRID input[id='showTime']").val();
  		if (showTime == undefined || showTime == null || showTime == '') {
  			var myDate = new Date();
  			var y =myDate.getFullYear();    //获取完整的年份(4位,1970-????)
  			var m = myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
  			var d = myDate.getDate();        //获取当前日(1-31)
  			if(m<10){m = "0"+m;};
  			if(d<10){d = "0"+d;};
  			var sysTime = y+"-"+m+"-"+d;
  			showTime = sysTime;
  		}
  		if(flag){
  			$("#DIV_DISHSNOSHOW_EDIT_GRID input[id='showTime']").val(showTime);
  			dishsNoShow.InitGrid("DISHSNOSHOW_EDIT_GRID",flag);
  		}else{
  			$("#DIV_DISHSNOSHOW_ADD_GRID input[id='showTime']").val(showTime);
  			dishsNoShow.InitGrid("DISHSNOSHOW_ADD_GRID",flag);
  		}
  	};
  	dishsNoShow.iSClean = function(id,flag){
  			$("#DIV_DISHSNOSHOW_GRID input[id='showTime']").val("");
  			$("#DIV_DISHSNOSHOW_GRID input[id='dishsName']").val("");
  			
  			$("#DIV_DISHSNOSHOW_ADD_GRID input[id='showTime']").val("");
  			$("#DIV_DISHSNOSHOW_ADD_GRID input[id='dishsName']").val("");
  			
  			$("#DIV_DISHSNOSHOW_EDIT_GRID input[id='showTime']").val("");
  			$("#DIV_DISHSNOSHOW_EDIT_GRID input[id='dishsName']").val("");
  	};
  	dishsNoShow.back = function(){
  		dishsNoShow.InitGrid("DISHSNOSHOW_GRID",true);
  	};
  	dishsNoShow.save = function(id){
  	var  showTime = $("#DIV_"+id+" input[id='showTime']").val();
	  	if (showTime == undefined || showTime == null || showTime == '') {
			fsn.initNotificationMes("请输入您要保存的展示日期", false);
			return ;
		}
  		$("#showTime_win").html("【日期："+showTime+"】");
  		$("#submitFlag").val(id);
  		var status=false;
  		var grid = $("#"+id).data("kendoGrid")._data;
  		for(var i in grid){
  			var shwoFlag = $("#showFlag_"+grid[i].id).val();
  			if(shwoFlag==1){
  				status = true;
  				break;
  			}
  		}
  		if(!status){
  			fsn.initNotificationMes("请选择【日期："+showTime+"】的展示菜品!", false);
  			return;
  		}
  		$("#CONFIRM_COMMON").data("kendoWindow").open().center();
  	};
  	dishsNoShow.cancel = function(){
  		$("#CONFIRM_COMMON").data("kendoWindow").close();
  	};
  	
  	
  //初始化弹出框
  	dishsNoShow.initKendoWindow = function(formId, width, heigth, title,visible, modal, resizable, actions, mesgLabelId, message){
			if(mesgLabelId != null) {
				$("#"+mesgLabelId).html(message);
			}
			$("#"+formId).kendoWindow({
				width:width,
				height:heigth,
		   		visible:visible,
		   		title:title,
		   		modal: modal,
		   		resizable:resizable,
		   		actions:actions
			});
			return $("#"+formId).data("kendoWindow");
		};
  	dishsNoShow.initialize();
});