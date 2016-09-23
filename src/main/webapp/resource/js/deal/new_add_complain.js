$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var addComplain = fsn.addComplain = fsn.addComplain || {};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
 	var CONFIRM = null;
	var DEALPROBLEM_ID = null;
	
	//页面显示表格
	/**
	 * //序号
		//省份
		//城市
		//县区
		//产品名称
		//企业名称
		//营业执照
		//条形码
		//生产日期
		//问题类型，1代表：产品信息错误，2代表：有产品信息，无报告信息，3代表：报告过期，未上传新报告，	4代表：
		//用户发现时间
		//企业处理完成时间
		//将要求处理完成时间
		//信息来源:监管/食安测/终端
		//信息来源地址定位:经纬度
		//企业处理：已处理/未处理
		//管理员：提交企业/提交监管/忽略
		//投诉处理/新增投诉(1)/已提交投诉(2)/忽略(3)
		//备注
	 */
	//新增页面表格
	addComplain.getAddColumns = function(){
		var templatecolumns = [
      	                 {field: "id",title:fsn.l("ID"),width: "10%",filterable: false},
      	                 {field: "privence",title:fsn.l("Privence"),width: "10%",filterable: false},
      	                 {field: "city",title:fsn.l("City"),width: "10%",filterable: false},
      	                 {field: "counties",title:fsn.l("Counties"),width: "10%",filterable: false},
      	                 {field: "productName",title:fsn.l("ProName"),width: "20%",filterable: false},
      	                 {field: "businessName",title:fsn.l("Business Name"),width: "20%",filterable: false},
      	                 {field: "licenseNo",title:fsn.l("License No"),width: "20%",filterable: false},
      	                 {field: "barcode",title:fsn.l("Barcode"),width: "20%",filterable: false},
      	                 {field: "showProblemType",title:fsn.l("Problem Type"),width: "20%",filterable: false},
//      	                 ----------------------------------------------------------------------------------
      	                 {field: "productTime",title:fsn.l("Product Time"),width:"20%",filterable: false,
      	                	 template : '#= fsn.formatGridDate(productTime)#'
      	                		 },
                           {field: "createTime",title:fsn.l("Create Time"),width: "20%",filterable: false,
      		                	 template : '#= fsn.formatGridDate(createTime)#'
      		                		 },
                           {field: "requestDealTime",title:fsn.l("Request Deal Time"),width:"25%",filterable: false,
      			                	 template : '#=fsn.formatGridDate(requestDealTime)#'
      			                		 },
//      			           ----------------------------------------------------------------------------------------    		 
                           {field: "showOrigin",title:fsn.l("Origin"),width: "20%",filterable: false},
                           {field: "remark",title:fsn.l("Remark"),width: "15%",filterable: false},
                           {field: fsn.l("Operation"),width: "75%",filterable: false,
                          	 template : function(e){
                          		 var tag = "";
                          		 //通知监管
                          		 var tag0 =  "<a  onclick='return fsn.portal.notice("+e.id+",0)' " +
                          		 "class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
                          		 "</span>" + fsn.l('NOTICE EXAMINE') + "</a>";
                          		 //通知企业
                          		 var tag1 =  "<a  onclick='return fsn.portal.notice("+e.id+",1)' " +
                          		 "class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
                          		 "</span>" + fsn.l('NOTICE BUSINESS') + "</a>";
                          		 //忽略
                          		 var tag2 = "<a  onclick='return fsn.portal.notice("+e.id+",2)' " +
                          		 "class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
                          		 "</span>" + fsn.l('IGNORE') + "</a>";
                          		
                          		 if(e.commitStatus == 'ZERO' && e.complainStatus=='ZERO'){
                          			 //通知企业，监管，删除
                          			 tag = tag0+ tag1+tag2;
                          		 }else if(e.commitStatus == 'ONE'&&e.complainStatus=='ONE'){
                          			 //通知监管
                          			 tag = tag0;
                          		 }else if(e.commitStatus == 'TWO'&&e.complainStatus=='TWO'){
                              		 //删除。恢复
                              		 var tag3 =  "<a  onclick='return fsn.portal.recover("+e.id+")' " +
        			    				"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
        			    				"</span>" + fsn.l('RECOVER') + "</a>";
                              		 var tag4 = "<a  onclick='return fsn.portal.remove("+e.id+")' " +
        			    				"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
        			    				"</span>" + fsn.l('REMOVE') + "</a>";
                          			
                          			 tag = tag3+tag4;
                          		 }
               					return tag;
                          	 }
                           }
                           ];
		
		return templatecolumns;
	};
	
	//已提交头数表格
	addComplain.getComColumns = function(){
		var templatecolumns = [
     	                 {field: "id",title:fsn.l("ID"),width: "50%",filterable: false},
     	                 {field: "businessName",title:fsn.l("Business Name"),width: "20%",filterable: false},
     	                 {field: "licenseNo",title:fsn.l("License No"),width:  "20%",filterable: false},
     	                 {field: "barcode",title:fsn.l("Barcode"),width: "20%",filterable: false},
     	                 {field: "showProblemType",title:fsn.l("Problem Type"),width:  "20%",filterable: false},
     	                 {field: "productTime",title:fsn.l("Product Time"),width:  "20%",filterable: false,
     	                	 template : '#= fsn.formatGridDate(productTime)#'
     	                		 },
                          {field: "createTime",title:fsn.l("Create Time"),width:  "20%",filterable: false,
     		                	 template : '#= fsn.formatGridDate(createTime)#'},
                          {field: "requestDealTime",title:fsn.l("Request Deal Time"),width:  "20%",filterable: false,
     			                	 template : '#= fsn.formatGridDate(requestDealTime)#'},
                          {field: "showOrigin",title:fsn.l("Origin"),width:  "20%",filterable: false},
                          {field: "showCommitStatus",title:fsn.l("COMMIT COMPLETE"),width:  "20%",filterable: false},
                          {field: fsn.l("Operation"),width: "35%",filterable: false,
                         	 template : function(e){
                         		 var tag = "";
                         		 //通知监管，企业，忽略，如果在新添投诉清单已通知企业或监管，那么这里就只显示没被通知的一方
                         		if(e.commitStatus == 'ONE'&&e.complainStatus=='ONE'){
                         			
                         			 //通知监管
                         			 tag = "<a  onclick='return fsn.portal.notice("+e.id+",0)' " +
                              		 "class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
                              		 "</span>" + fsn.l('NOTICE EXAMINE') + "</a>";
                         		} 
              					return tag;
                         	 }
                          }];
		return templatecolumns;
	};
	
	//已忽略投诉表格
	addComplain.getIgnoreColumns = function(){
		var templatecolumns = [
     	                 {field: "id",title:fsn.l("ID"),width: "10%",filterable: false},
     	                 {field: "privence",title:fsn.l("Privence"),width: "10%",filterable: false},
     	                 {field: "city",title:fsn.l("City"),width: "10%",filterable: false},
     	                 {field: "counties",title:fsn.l("Counties"),width: "15%",filterable: false},
     	                 {field: "businessName",title:fsn.l("Business Name"),width: "15%",filterable: false},
     	                 {field: "licenseNo",title:fsn.l("License No"),width: "20%",filterable: false},
     	                 {field: "barcode",title:fsn.l("Barcode"),width: "20%",filterable: false},
     	                 {field: "showProblemType",title:fsn.l("Problem Type"),width: "20%",filterable: false},
//     	      ------------------------------------------------------------------------------------------------------
     	                 {field: "productTime",title:fsn.l("Product Time"),width: "20%",filterable: false,
     	                	 template : '#= fsn.formatGridDate(productTime)#'
     	                		 },
                          {field: "createTime",title:fsn.l("Create Time"),width: "20%",filterable: false,
     		                	 template : '#= fsn.formatGridDate(createTime)#'
     		                		 },
                          {field: "requestDealTime",title:fsn.l("Request Deal Time"),width: "20%",filterable: false,
     			                	 template : '#= fsn.formatGridDate(requestDealTime)#'
     			                		 },
//     	      ------------------------------------------------------------------------------------------------------
                          {field: "showOrigin",title:fsn.l("Origin"),width: "20%",filterable: false},
                          {field: "remark",title:fsn.l("Remark"),width: "10%",filterable: false},
                          {field: fsn.l("Operation"),width: "35%",filterable: false,
                         	 template : function(e){
                         		 var tag = "";
                         		if(e.commitStatus == 'TWO'&&e.complainStatus=='TWO'){
                             		 //删除。恢复
                             		 tag +=  "<a  onclick='return fsn.portal.recover("+e.id+")' " +
       			    				"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
       			    				"</span>" + fsn.l('RECOVER') + "</a>";
                             		
                             		 tag += "<a  onclick='return fsn.portal.remove("+e.id+")' " +
       			    				"class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> " +
       			    				"</span>" + fsn.l('REMOVE') + "</a>";
                         		};
              					return tag;
                         		 
                         	 }
                          }];
		return templatecolumns;
	};
	
	//后台查询数据
//	addComplain.templateDS = function(businessName,license,barcode){
		addComplain.templateDS = function(){
		var status = $("#status").val();
		//获取三个关键字的值
		var businessName = $("#businessName").val();
		var license = $("#licenseNo").val();
		var barcode = $("#barcodeNo").val();
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	 data:{businessName:businessName,licenseNo:license,barcode:barcode},
	            	url : function(options){
	            		return portal.HTTP_PREFIX + "/deal/getDealList/" + options.page + "/" + options.pageSize + "/"+status ;
					},
					type : "GET",
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	 data : function(data) {
	                 return data.data;//响应到页面的数据
	             },
	             total : function(data) {
	                 return data.total;   //总条数
	             }         
	        },
	        batch : true,
	        pageSize : 20, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	
	
	addComplain.buildGrid=function(id,columns,ds){
		var value = $("#status").val();
		var columns = [];
		if(value == 0){
			columns = addComplain.getAddColumns();
		} else if(value == 1){
			columns = addComplain.getComColumns();
		}else{
			columns = addComplain.getIgnoreColumns();
		}
		
		
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				messages: fsn.gridfilterMessage(),
				operators: {
					string: fsn.gridfilterOperators(),
				    date: fsn.gridfilterOperatorsDate(),
				    number: fsn.gridfilterOperatorsNumber()
				}
			},
			width:1050,
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: [10, 20,50,80,100],
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
	};
	
	//通知监管
	portal.noticeExamine = function(data){
		data.statua = 1;
		$.ajax({
	          url: fsn.getHttpPrefix() + "/deal/saveDealToProblem",
	          type: "POST",
	          contentType: "application/json; charset=utf-8",
	          data:JSON.stringify(data),
	          success: function(returnVal){
	        	  if(returnVal.status == true){
	            	  fsn.initNotificationMes("问题处理完成!", true);
	              }
	          }
	      });
	};
	

	//通知监管，通知企业忽略,某个问题处理，传他的序号进来，还有当前提交状态是什么
	portal.notice = function(id,status){
		var pageStatus = $("#status").val();
		$.ajax({
			url : portal.HTTP_PREFIX + "/deal/notice/"+id+"/"+status+"/"+pageStatus,
			type:"GET",
			dataType: "json",
			async:false,
			success:function(returnData){
				addComplain.initialize();
			}
		});
	};
	
	
	//初始化弹出框
	 function initKendoWindow(formId, width, heigth, title,visible, modal, resizable, actions, mesgLabelId, message){
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
	
		
		//删除调用方法,根据ID序号，删除对应的数据
		portal.remove = function(id){
			DEALPROBLEM_ID = id;
			 CONFIRM.open().center();
		};
		
		
		/* 删除提交  */
		portal.removeRow = function(status){
			if(status=="save"){
				$.ajax({
					url:portal.HTTP_PREFIX + "/deal/delete/"+DEALPROBLEM_ID,
					type:"DELETE",
					dataType: "json",
					async:false,
					success:function(resultValue){
						if (resultValue.result + "" == "1") {
							fsn.initNotificationMes("已成功删除！", true);
							addComplain.initialize();
						} else { 
							fsn.initNotificationMes("删除失败", false);
							addComplain.initialize();
						}
					}
					
				});
				CONFIRM.close();
			}else{
				CONFIRM.close();
			}
	 };
	
	//恢复函数,根据ID，修改该条问题的处理状态为0，在新增页面显示
		portal.recover = function(id){
			$.ajax({
				url : portal.HTTP_PREFIX + "/deal/recover/" + id,
				type : "GET",
				dataType : "json",
				async:false,
				success:function(returnValue){
					if (returnValue.result == "1") {
						fsn.initNotificationMes("恢复成功！", true);
						addComplain.initialize();
					} else {
						fsn.initNotificationMes("恢复失败", false);
						addComplain.initialize();
					}
				}
			});
		};
		
		
	addComplain.initialize = function(){
		CONFIRM = initKendoWindow("CONFIRM_COMMON","300px", "100px", "删除提示！", false,true,false,["Close"],null,"");
		var dataSource = this.templateDS();
		addComplain.buildGrid("msg_success_grid",this.templatecolumns, dataSource); 		
	};
	addComplain.initialize();

	
    //按企业名称，营业执照，产品条码查询数据
	addComplain.check = function(){
		//获取三个关键字的值
//		var businessName = $("#businessName").val();
//		var licenseNo = $("#licenseNo").val();
//		var barcode = $("#barcode").val();
		
		//查询数据
//		var dataSource = this.templateDS(businessName,licenseNo,barcode);
		var dataSource = this.templateDS();
	
		var grid = $("#msg_success_grid").data("kendoGrid");
		grid.setDataSource(dataSource);
		grid.refresh();
	};
	
	
});