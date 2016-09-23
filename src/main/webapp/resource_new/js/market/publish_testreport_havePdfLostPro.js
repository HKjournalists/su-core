$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	root.HTTP_PREFIX = lims.getHttpPrefix();
	var viewReportID=0;
	lims.current_index = 0;
	
	root.initialize=function(){
		root.buildGrid("report_Lims2_grid", root.lims2reportDS, "toolbar_template", root.column);
		root.initTab();
		lims.initEasyItemGrid("testItem_grid");
	};
	
	root.initTab = function(){
		var g = $("#tabstrip").data("kendoTabStrip");
        if(!g){
            $("#tabstrip").kendoTabStrip({
                animation:  {
                    open: {
                        effects: "fadeIn"
                    }
                },
                activate:function(e){
                    lims.current_index = $("#tabstrip li[aria-selected=true]").index();
                }
            });
            g = $("#tabstrip").data("kendoTabStrip");
        }
    };
	
    root.buildGrid = function (id,ds,tmpBar,columns){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
			filterable: {
				extra:false,
				messages: lims.gridfilterMessage(),
				operators: {
					  string: lims.gridfilterOperators()
				}
			},
			height: 400,
	        width: 1000,
	        toolbar: [
	    	        	{template: kendo.template($("#"+tmpBar).html())}
	    	         ],
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: 6,
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns,
		});
	};
    
	root.lims2reportDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		if(options.filter){
            			var configure = filter.configure(options.filter);
            			return lims.getHttpPrefix() + "/testReport/getLims2Report/" + options.page + "/" + options.pageSize + "/0/" + configure;
            		}
					return lims.getHttpPrefix() + "/testReport/getLims2Report/" + options.page + "/" + options.pageSize+"/0";
				},
                dataType : "json",
                contentType : "application/json"
            }
        },
        batch : true,
        page:1,
        pageSize: 6,
        schema: {
            data : function(returnValue) {
            	for(var i=0; i<returnValue.data.listOfReport.length; i++){
            		returnValue.data.listOfReport[i].pubFlag = "未发布";
            	}
                return returnValue.data.listOfReport;
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        change: function(e) {
            
          },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	
	root.column = [	
                   { field: "id", title:"ID", width: 35 },
                   { field: "reportNo", title:"报告编号", width: 80 },
                   { field: "productName", title:"食品名称",width: 60},
                   { field: "batchNo", title:"批次", width: 55},
                   { field: "testOrgniz", title:"检验机关", width: 70},
                   { field: "pubFlag", title:"状态", width: 30, filterable: false},
                   { field: "lastModifyUserName",title:"最后更新者",width:45},
                   { field: "lastModifyTime",title:"最后更新时间",width:55, filterable: false},
                   { field: "tips",title:"消息提示",width:60},
                   { command: [
                     {name:"review",
                   	    text:"<span class='k-icon k-cancel'></span>" + "预览", 
                   	    click:function(e){
                   	    	e.preventDefault();
                   	    	var	currentItem = this.dataItem($(e.currentTarget).closest("tr"));	
                   	    	viewReportID=currentItem.id;
                   	    	lims.easyViewReport(viewReportID);
                   	    },
                   	    	},], title: "操作", width: 40 }];
	
	root.publishToFSN = function(){
		var size = $("#tabstrip-"+(lims.current_index+1)).data("kendoGrid").select().length;
		if(size == 1){
			var selectItem = $("#tabstrip-"+(lims.current_index+1)).data("kendoGrid").select()[0];
			var currentReport = $("#tabstrip-"+(lims.current_index+1)).data("kendoGrid").dataItem(selectItem);
			root.currentReportID = currentReport.id;
		}else{
			if(lims.current_index==0){
				lims.msg("msg_success",null,"请选择一条未发布的检测报告！");
			}else if(lims.current_index==1){
				lims.msg("msg_success",null,"请选择一条已退回的检测报告！");
			}
			return;
		}
		
		$("#publishStatus").html("正在发布，请稍后...");
		$("#toPublishWindow").data("kendoWindow").open().center();
		$.ajax({
			url:lims.getHttpPrefix() + "/pipeline/getTestResult_/" + root.currentReportID +"/" + lims.getSaveFtpPath(),
			type:"GET",
			async:false,
			success:function(data){
				$("#toPublishWindow").data("kendoWindow").close();
				if(data.status=="true"){
					if(data.isExist=="true"){
						lims.msg("msg_success",true,"ID=" + root.currentReportID + ". " + "此报告之前已经发布成功！");
					}else{
						lims.msg("msg_success",true,"ID=" + root.currentReportID + ". " + "恭喜，报告发布成功！");
					}
					if(lims.current_index==0){
						root.lims2reportDS.read();
					}else if(lims.current_index==1){
						root.gsReportDS.read();
					}
				}else{
					lims.msg("msg_success",null,"ID=" + root.currentReportID + ". " + data.error);
				}
			},
			error:function(){
				$("#toPublishWindow").data("kendoWindow").close();
				lims.msg("msg_success",null,"系统异常，请联系相关工作人员！");
			}
    	});   	 
	};
	
	root.edit = function(e) {
		var size = $("#report_publish_grid").data("kendoGrid").select().length;
		if(size > 0){
			var selectItem = $("#report_publish_grid").data("kendoGrid").select()[0];
			var currentReport = $("#report_publish_grid").data("kendoGrid").dataItem(selectItem);
			root.currentReportID = currentReport.id;
		}
		var testreport = {
			id : root.currentReportID
		};
		$.cookie("user_0_edit_testreport", JSON.stringify(testreport), {
			path : '/'
		});
		window.location.pathname = "/lims-core/views/testreport/add_testreport.html";
	};
	
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:"发布报告预览",
	});
	
	$("#toPublishWindow").kendoWindow({
		actions:[],
		width:500,
		visible:false,
		title:"发布状态",
		modal: true,
		resizable:false,
	});
	
	$("#addRemarkWin").kendoWindow({
		width:500,
		height:200,
		visible: false,
		title:"添加备注",
	});
	
	$("#tsWin").kendoWindow({
		width:500,
		visible:false,
		title:"提示",
	});
	
	lims.download=function(resID){
		try{
            lims.log("Download");
            var url = lims.getHttpPrefix()+"/testReport/downloadResource/" + resID + "/" + viewReportID +"/"+lims.getSaveFtpPath();
            var hiddenIFrameID = 'hiddenDownloader',
            iframe = document.getElementById(hiddenIFrameID);
            if (iframe === null) {
                iframe = document.createElement('iframe');
                iframe.id = hiddenIFrameID;
                iframe.style.display = 'none';
                document.body.appendChild(iframe);
            }
            iframe.src = url;
        }catch(e){
            lims.debug(e);
        }
	};
	
	root.editTips=function(){
		var length = $("#tabstrip-" + (lims.current_index+1)).data("kendoGrid").select().length;
		if( length<1 ){
			if(lims.current_index == 0){
				$("#tsWin").html("请选择一条Lims2.0的检测报告！");
			}else if(lims.current_index == 1){
				$("#tsWin").html("请选择一条工商的检测报告！");
			}
			$("#tsWin").data("kendoWindow").open().center();
			return;
		}
		$("#remarkText").val("");
		$("#addRemarkWin").data("kendoWindow").open().center();
	};
	
	$("#btn_addN").click(function(){
		$("#addRemarkWin").data("kendoWindow").close();
	});
	
	$("#btn_addY").click(function(){
		var tipText=$("#remarkText").val();
		if($.trim(tipText)==""){
			$("#tsWin").html("你没有输入任何备注文本，不能修改提示消息！");
			$("#tsWin").data("kendoWindow").open().center();
			return;
		}
		$("#addRemarkWin").data("kendoWindow").close();
		var selectRow=$("#tabstrip-"+(lims.current_index+1)).data("kendoGrid").select();
		var seItem=$("#tabstrip-"+(lims.current_index+1)).data("kendoGrid").dataItem(selectRow);
		$.ajax({
			url:lims.getHttpPrefix() + "/testReport/editTips/" + tipText + "/" + seItem.id,
    		type:"GET",
    		dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function(returnValue){
				if(returnValue.result.status == "true"){
					root.lims2reportDS.read();
					lims.msg("ts_msg",returnValue.result,"修改消息成功！");
				}else{
					lims.msg("ts_msg",returnValue.result,"修改消息失败！");
				}
			},
		});
	});
	
	root.initialize();
});
