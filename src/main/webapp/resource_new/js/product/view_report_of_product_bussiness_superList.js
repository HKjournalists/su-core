$(function() {
	var fsn = window.fsn = window.fsn || {};
	var portal = fsn.portal = fsn.portal || {};
	var upload = fsn.upload = fsn.upload || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.fromBusId = null;
	portal.product_id = null;
	/**
	 * 从#query input 框点击搜索条形码后，跳转到本页面
	 * @author ZhangHui 2015/5/7
	 */
	try {
		var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		if(arrayParam.length == 2){
			portal.isSearchAll = true;
		}else if(arrayParam.length == 4){
			portal.isSearchAll = false;
		}
		
		portal.fromBusId = arrayParam[0];
		portal.product_id = arrayParam[1];
		portal.fromBuName=decodeURIComponent(arrayParam[2]);
		portal.org=decodeURIComponent(arrayParam[3]);
		if(portal.org == undefined||portal.org){
			portal.org = 0;
		}
    } catch(e) {}
    
    
    portal.initialize = function(){
    	fsn.initKendoWindow("send_back_report_warn_window","退回原因","950px","690px",false,null);
    	
    	 $("#tabstrip").kendoTabStrip({
             activate : portal.tabActivate,
             animation: {
                 open: {effects: "fadeIn"}
             }
         }).data("kendoTabStrip").select(0);
    	 
    	/* 获取当前报告的产品信息 */
    	getProductById();
    	upload.buildGridWioutToolBar("self_report_of_product_bussiness_super_grid", portal.templatecolumns, portal.selfDS);  // 自检
    	upload.buildGridWioutToolBar("censor_report_of_product_bussiness_super_grid",portal.templatecolumns, portal.censorDS);   // 送检
    	upload.buildGridWioutToolBar("sample_check_report_of_product_bussiness_super_grid",portal.templatecolumns, portal.sampleDS);// 抽检
    	
    	$("#send_back_report_yes_btn").bind("click", portal.back_yes);
        $("#send_back_report_no_btn").bind("click", portal.back_no);
        
	};
	
	portal.templatecolumns = [
	               /*{field: "id",title: "序号",width: "15px", filterable: false},*/
                   {field: "serviceOrder",title: "报告编号",width: "40px", filterable: false},
                   {field: "batchSerialNo",title: "批次",width: "40px", filterable: false},
                   {field: "producerName",title: "生产企业名称",width: "70px", filterable: false},
                   {field: "productionDate",title: "生产日期",template: '#=productionDate=fsn.formatGridDate(productionDate)#',width: "30px", filterable: false},
                   {field: "checkOrgName",title: "审核人",width: "50px",filterable: false},
                   {width:"80px",title: fsn.l("Operation"),
					      template:function(e){
							ids = e.id;
							var tag="<a href='" + e.pdfUrl + "' target='_blank' class='k-button k-button-icontext k-grid-ViewDetail'>" +
									"<span class='k-icon k-edit'> </span>查看</a>";
							/**
							 * 4代表经销商提交，但未通过商超审核的报告
							 */
						    if (e.operateRight==true && e.publishFlag=='4') {
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
          	                 data:{org:portal.org},
          	                async:false,
          	                url : function(options){
          	                	var configure = null;
          	                	if(options.filter){
          	            			configure = filter.configure(options.filter);
          	            		}
          	                	return portal.HTTP_PREFIX + "/report/getReports/" + portal.isSearchAll + "/" + configure + "/" + options.page + "/" 
          	                					+ options.pageSize + "/" + portal.product_id + "/" + portal.fromBusId + "/sample";
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
          	            	 $("#count_of_check_report").html("抽检报告(共"+ returnValue.counts + "份)");
          	            	 $("#count_of_check_report_onHandle").html("抽检" + returnValue.totalOfOnHandle + "份");
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
      	                 data:{org:portal.org},
      	                async:false,
      	                url : function(options){
      	                	var configure = null;
      	                	if(options.filter){
      	            			configure = filter.configure(options.filter);
      	            		}
      	                	return portal.HTTP_PREFIX + "/report/getReports/" + portal.isSearchAll + "/" + configure + "/" + options.page + "/" 
          								+ options.pageSize + "/" + portal.product_id + "/" + portal.fromBusId + "/censor";
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
      	            	 $("#count_of_censor_report").html("送检报告(共"+ returnValue.counts + "份)");
      	            	 $("#count_of_censor_report_onHandle").html("送检" + returnValue.totalOfOnHandle + "份");
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
    	                data:{org:portal.org},
    	                async:false,
    	                url : function(options){
    	                	var configure = null;
    	                	if(options.filter){
    	            			configure = filter.configure(options.filter);
    	            		}
    	                	return portal.HTTP_PREFIX + "/report/getReports/" + portal.isSearchAll + "/" + configure + "/" + options.page + "/" 
											+ options.pageSize + "/" + portal.product_id + "/" + portal.fromBusId + "/self";
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
    	            	 $("#count_of_self_report").html("自检报告(共"+ returnValue.counts + "份)");
    	            	 $("#count_of_self_report_onHandle").html("自检" + returnValue.totalOfOnHandle + "份");
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
 	 * 清空退回窗口
 	 */
 	portal.clearBackWin=function(){
 		$("#send_back_msg_other").val("");

 		var inputs = document.getElementsByName("send_back_msg");   
 		for(var i=0;i<inputs.length;i++){   
 		    inputs[i].checked = false;   
 		}   
 		$("#send_back_report_warn_window input").val('');
 	};
 	/**
 	 * 报告退回
 	 * @author Zhanghui 2015/4/9
 	 */
 	portal.back = function(reportId){
 		if(portal.reportId!=reportId){
 			portal.clearBackWin();
 		}
 		//去掉清空的填写退回信息
 		portal.reportId = reportId;
 		$("#send_back_report_warn_window").data("kendoWindow").open().center();
 	};
 	
 	/**
 	 * 点击退回提示框的确定按钮
 	 * @author ZhangHui 2015/5/5
 	 */
 	portal.back_yes = function(){
 		// 获取退回原因msg
 		var send_back_msg = "";
 		var index=1;
 		$("input[name='send_back_msg']:checked").each(function(i){
 			index=i+1;
 			send_back_msg+=index+"、"+$(this).attr('alt');
 			if($(this).next().is("input")||$(this).next().is("select")){
 				send_back_msg+=":"+$(this).next().val()+"；";
 			}
 		});
 		if($.trim($("#send_back_msg_other").val())!=""){
 			++index;
 			send_back_msg+=index+":"+$.trim($("#send_back_msg_other").val());
 		}
 		if(send_back_msg == ""){
 			fsn.initNotificationMes("请选择退回原因，或者填写其他原因！", false);
 			return;
 		}
 		// 报告退回
 		$.ajax({
 	        url: portal.HTTP_PREFIX + "/report/operation/busSuperCheckReport/false/" + portal.reportId + "?msg=" + encodeURI(encodeURI(send_back_msg)),
 	        type: "GET",
 	        dataType: "json",
 	        contentType: "application/json; charset=utf-8",
 	        success: function(returnValue) {
 	            if (returnValue.result.status == "true") {
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + portal.reportId + ",退回成功！", true);
 	            }else if(returnValue.result.show){
 	            	portal.refreshGrid();
 	            	fsn.initNotificationMes("ID=" + portal.reportId + "," + returnValue.result.errorMessage, false);
 	            }
 	        }
 	    });
 		$("#send_back_report_warn_window").data("kendoWindow").close();
 	};
 	
 	/**
 	 * 点击退回提示框的取消按钮
 	 * @author zhangHui 2015/5/5
 	 */
 	portal.back_no = function(){
 		$("#send_back_report_warn_window").data("kendoWindow").close();
 	};
 	
 	/**
 	 * 选择选项卡事件
 	 * @author HuangYong 2015/4/10
 	 */
 	portal.tabActivate = function(e){
 		portal.refreshGrid();
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
 	
    /**
     * 展示报告的产品信息
     * @author tangxin 2015/04/15
     */
    function setProductValue(product){
    	if(portal.fromBuName != "undefined"){
    		$("#status_bar").html("当前位置："+portal.fromBuName+" &raquo; "+product.name);
    	}else{
    		$("#status_bar").html("当前位置：食品安全云平台  &raquo; "+product.name);
    	}
    	var imgUrl =[];
    	imgUrl[0]="http://qa.fsnrec.com/portal/img/product/temp/temp.jpg";
    	$("#productName").text(product.name);
    	$("#productBarcode").text(product.barcode);
    	$("#expiration").text(product.expiration == null ? "" : product.expiration);
    	$("#productFormat").text(product.format == null ? "" : product.format);
    	$("#productStatus").text(product.status == null ? "" : product.status);
    	$("#busName").text(product.enterpriseName == null ? "" : product.enterpriseName);
    	$("#busName").attr("title",product.enterpriseName == null ? "" : product.enterpriseName);
    	var imgHtml='';
    	for(var i in product.imgList){
    		imgHtml+='<li>';
    		imgHtml+='<a href="'+product.imgList[i].url+'" target="_blank">';
    		imgHtml+='<img style="height:175px;" src="'+product.imgList[i].url+'"alt="'+product.imgList[i].name+'" />';
    		imgHtml+='</a>';
    		imgHtml+='</li>';
    	}
    	$(".slides").html(imgHtml);
    	$("#KinSlideshow").flexslider({
			slideshowSpeed: 4000, //展示时间间隔ms
			animationSpeed: 400, //滚动时间ms
			touch: true //是否支持触屏滑动
		});
    	if((portal.fromBusId==null||portal.fromBusId=="null")&&(product.licImg==null||product.licImg=="")){
    		$("#certImg").hide();
    	}else{
    		$("#certImg").show();
    		/** 处理证照图片 */
        	if(product.licImg != null && product.licImg != "") {
        		$("#licImg").attr("src",product.licImg);
        		$("#lic_a").attr("href",product.licImg);
        	}else{
        		$("#licImg").attr("src","http://qa.fsnrec.com/portal/img/product/temp/temp.jpg");
        		$("#lic_a").removeAttr("target");
        	}
        	if(product.orgImg != null && product.orgImg != "") {
        		$("#orgImg").attr("src",product.orgImg);
        		$("#org_a").attr("href",product.orgImg);
        	}else{
        		$("#orgImg").attr("src","http://qa.fsnrec.com/portal/img/product/temp/temp.jpg");
        		$("#org_a").removeAttr("target");
        	}
        	if(product.disImg != null && product.disImg != "") {
        		$("#disImg").attr("src",product.disImg);
        		$("#dis_a").attr("href",product.disImg);
        	}else{
        		$("#disImg").attr("src","http://qa.fsnrec.com/portal/img/product/temp/temp.jpg");
        		$("#dis_a").removeAttr("target");
        	}
    	}
    	/*var certList = product.certList;
    	var certUL = $("#certList");
    	if(certList != null) {
    		for(var i=0; i<certList.length;i++){
    			var li = $("<li></li>");
    			var a = $("<a target='_blank'></a>");
    			var img = $("<img />");
    			li.attr("title",certList[i].name);
    			img.attr("src",certList[i].imgUrl);
    			a.attr("href",certList[i].documentUrl);
    			a.append(img);
    			li.append(a);
    			certUL.append(li);
    		}
    	}*/
    }
    
    /**
     * 获取当前报告的产品信息
     * @autho tangxin 2015/04/15
     */
    function getProductById(){
    	if(portal.product_id == null){
    		return;
    	}
    	if(portal.fromBusId==null||portal.fromBusId=="null"){
    		portal.fromBusId=-1;
    	}
    	$.ajax({
 	        url: portal.HTTP_PREFIX + "/product/getProductAndCert/" + portal.product_id+"/"+portal.fromBusId, //testId=10214
 	        type: "GET",
 	        dataType: "json",
 	        success: function(returnValue) {
 	            if (returnValue.result.status == "true") {
 	            	/* 展示报告的产品信息 */
 	            	setProductValue(returnValue.data);
 	            }else {
 	            	fsn.initNotificationMes("获取报告产品信息时出现异常！",false);
 	            }
 	        }
 	    });
    }
    
	portal.initialize();
});