$(function(){
	var lims = window.lims = window.lims || {};
	var root = window.lims.root = window.lims.root || {};
	var filter = window.filter = window.filter || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	root.initialize=function(){
		root.buildGrid("undispose_grid", root.setColumn(0), root.undisposeDS, "");
		root.buildGrid("disposeing_grid", root.setColumn(1), root.disposeingDS, "");
		root.buildGrid("disposed_grid", root.setColumn(2), root.disposedDS, "");
        lims.initKendoWindow("showWindow","状态提示",300,120,false,false);
        $("#tabstrip").kendoTabStrip({
            activate : root.tabActivate,
            animation: {
                open: {effects: "fadeIn"}
            }
        }).data("kendoTabStrip").select(0);
	};
    
    /* 加载GRID 的datasource */
    root.undisposeDS = new kendo.data.DataSource({
            transport: {
                read: {
                    url: function(options){
                        if (options.filter) {
                            var configure = filter.configure(options.filter);
                            return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/0?configure=" + configure + "&page=" + options.page + "&pageSize=" + options.pageSize;
                        }
                        return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/0?page=" + options.page + "&pageSize=" + options.pageSize;
                    },
                    dataType: "json",
                    contentType: "application/json"
                }
            },
            batch: true,
            page: 1,
            pageSize: 5,
            schema: {
                data: function(returnValue){
                    if (returnValue.result.status == "true") {
                        return returnValue.data==null?[]:returnValue.data;
                    }
                },
                total: function(returnValue){
                    return returnValue.count;
                }
            },
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true
        });
    root.disposeingDS = new kendo.data.DataSource({
            transport: {
                read: {
                    url: function(options){
                        if (options.filter) {
                            var configure = filter.configure(options.filter);
                            return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/1?configure=" + configure + "&page=" + options.page + "&pageSize=" + options.pageSize;
                        }
                        return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/1?page=" + options.page + "&pageSize=" + options.pageSize;
                    },
                    dataType: "json",
                    contentType: "application/json"
                }
            },
            batch: true,
            page: 1,
            pageSize: 5,
            schema: {
                data: function(returnValue){
                    if (returnValue.result.status == "true") {
                        return returnValue.data==null?[]:returnValue.data;
                    }
                },
                total: function(returnValue){
                    return returnValue.count;
                }
            },
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true
        });
    root.disposedDS = new kendo.data.DataSource({
            transport: {
                read: {
                    url: function(options){
                        if (options.filter) {
                            var configure = filter.configure(options.filter);
                            return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/2?configure=" + configure + "&page=" + options.page + "&pageSize=" + options.pageSize;
                        }
                        return portal.HTTP_PREFIX + "/test/applyTimes/getApplyReportTimes/2?page=" + options.page + "&pageSize=" + options.pageSize;
                    },
                    dataType: "json",
                    contentType: "application/json"
                }
            },
            batch: true,
            page: 1,
            pageSize: 5,
            schema: {
                data: function(returnValue){
                    if (returnValue.result.status == "true") {
                        return returnValue.data==null?[]:returnValue.data;
                    }
                },
                total: function(returnValue){
                    return returnValue.count;
                }
            },
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true
        });
	
	/**
	 * 初始化Grid控件
	 * @author HuangYog
	 */
	root.buildGrid = function (id,columns,ds,isBack){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
			filterable: {
				extra:false,
				messages: lims.gridfilterMessage(),
				operators: {string: lims.gridfilterOperators()}
			},
			height:300,
	        width: 800,
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: 5,
	            messages: lims.gridPageMessage()
	        },
	        columns: columns
		});
	};
    /* GRID的每一列 */ 
    root.setColumn = function(status){
        var column = "";
        if(status==2){
            column =[
                {field: "id",title: "ID",width: 35}, 
                {field: "productName",title: "产品名称",width: 120},
                {field: "productBarcode",title: "条形码",width: 100},
                {field: "reportType",title: "报告类型",width: 55}, 
                {title: "申请时间",width: 55,template: function(model){
                	return fsn.formatGridDate(model.applyDate);
                }}, 
                {field: "applyTimes",title: "申请总次数",width: 50}
           ];
        }else{
            var edit = "";
            if(status == 0){
                edit = "<span class='k-edit'></span>" + "处理";
            }else if(status == 1){
                edit = "<span class='k-preview'></span>" + "查看进度";
            }
            column =[
                {field: "id",title: "ID",width: 35},
                {field: "productId",title: "productId",width: 35,hidden: true},
                {field: "productName",title: "产品名称",width: 120},
                {field: "productBarcode",title: "条形码",width: 100},
                {field: "reportType",title: "报告类型",width: 55}, 
                {title: "申请时间",width: 55,template: function(model){
                	return fsn.formatGridDate(model.applyDate);
                }},  
                {field: "applyTimes",title: "申请总次数",width: 50}, 
                {command: [{
                    name: "edit",
                    text: edit,
                    click: function(e){
                        var row = this.dataItem($(e.currentTarget).closest("tr"));
                        if (status == 0) {
                            root.handle(row);
                        }else if (status == 1) {
                            root.showJinDu(row);
                         }
                    }
                }],title: "操作",width: 50}
              ];
        }
        return column;
    }
    
	/**
	 * 点击处理
	 * @author HuangYog
	 */  
	root.handle = function(e) {
        root.applyId = e.id; //该条对象的id
		var proId = e.productId;
        root.reportType = e.reportType;
        $.ajax({
			url:portal.HTTP_PREFIX+"/test/applyTimes/handleApply/"+proId+"/"+root.reportType+"/0",
    		type:"GET",
            async: true,//同步
    		dataType: "json",
            contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
                    //当存在一条最新的未发布到portal和未退回的报告时
                    var handleMsg = "";
                    if(returnValue.data != null){
                        var date = returnValue.data.testDate;//检验日期
                        root.reportType = returnValue.data.reportType;//报告类型
                        var status = root.judgeReportStatus(returnValue.data.publicFlag); //报告状态
                        root.reportid = returnValue.data.reportId;//报告id
                        handleMsg = "该产品在有一份"+date+root.reportType+"的报告正处于"+status+"状态，是否继续新增?";
                        e.productBarcode;
                        root.haveReport = true;
                        $("#button_").css("margin-top","6px");
                    }else{//不存在最新报告时
                        root.haveReport = false;
                        handleMsg = "系统中未发现新报告，是否进行新增?";
                        $("#button_").css("margin-top","50px");
                    }
                    root.isPower = returnValue.isPower;// 用户是否有权限录入报告 true 有 false 没有
                    root.reportPage = returnValue.pageName;//录报告页面
                    $(update_msg).html(handleMsg);
                    root.productBarcode = e.productBarcode;
                    $("#viewWindow").data("kendoWindow").open().center();
				}else{
				}
			}
		});
	};
    
    /**
     * 查看处理进度
     * @author HuangYog
     */
    root.showJinDu = function(e){
        var proId = e.productId;
        root.reportType = e.reportType;
        $.ajax({
			url:portal.HTTP_PREFIX+"/test/applyTimes/handleApply/"+proId+"/"+root.reportType+"/1",
    		type:"GET",
    		dataType: "json",
            async: true,
            contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
                    if(returnValue.data != null){
                        var status = root.judgeReportStatus(returnValue.data.publicFlag); //报告状态
                        var show_msg = "该报告正处于<span class='red_xing'>"+status+"</span>状态。"
                        $("#show_msg").html(show_msg);
                        $("#showWindow").data("kendoWindow").open().center();
                    }
				}else{
                    
				}
			}
		});
    }
    
    /**
     * 判断报告在什么状态
     * @param {Object} flag
     *  0 代表 testlab正在审核;
     *  1 代表 testlab通过审核;
     *  2 代表 testlab退回;
     *  3 代表 发布人员未发布到testlab
     */
    root.judgeReportStatus = function(flag){
        var reportStatus = "";
        var fsc = "<strong>食安云</strong>";
        switch (flag) {
            case "0":
                reportStatus = fsc+"审核";
                break;
            case "3":
                reportStatus = "未发布";
                break;
            case "1":
                reportStatus = fsc+"审核通过";
                break;
            case "2":
                reportStatus = fsc+"退回";
                break;
            case "4":
                reportStatus = "商超未审核";
                break;
            case "5":
                reportStatus = "商超退回";
                break;
            case "6":
                reportStatus = "商超审核通过";
                break;
        }
        return reportStatus
    }
	
	$("#viewWindow").kendoWindow({
		width:300,
		height:120,
		visible: false,
		title:"处理提示"
	});
   
	root.tabActivate = function(e){
        var grid_id = e.item.id;
        switch (e.item.id) {
    		case "undispose":
    			$("#undispose_grid_").show();
    			$("#disposeing_grid_").hide();
    			$("#disposed_grid_").hide();
    			break;
    		case "disposeing":
    			$("#undispose_grid_").hide();
    			$("#disposed_grid_").hide();
    			$("#disposeing_grid_").show();
    			break;
    		case "disposed":
    			$("#undispose_grid_").hide();
    			$("#disposeing_grid_").hide();
    			$("#disposed_grid_").show();
    			break;
            default :
                $("#undispose_grid_").show();
    			$("#disposeing_grid_").hide();
    			$("#disposed_grid_").hide();
    			break;
		}
    }
	
	$("#update_no_btn").click(function(){
        if(root.haveReport){//有新报告时
            root.setApplyReportHandle(root.applyId);
        }
		$("#viewWindow").data("kendoWindow").close();
	});
    //改变申请报告更新该条的处理状态
    root.setApplyReportHandle = function(id){
        $.ajax({
			url:portal.HTTP_PREFIX+"/test/applyTimes/"+id,
    		type:"GET",
    		dataType: "json",
            async: true,
            contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
                    lims.initNotificationMes("已进行处理", true);
                    root.undisposeDS.read();
                    root.disposeingDS.read();
                    root.disposedDS.read();
				}else{
                    lims.initNotificationMes("处理失败", false);
				}
			}
		});
    }
	
	$("#update_yes_btn").click(function(){
       root.addReportBaseInfo(root.productBarcode);
       $("#viewWindow").data("kendoWindow").close();
	});
    
    $("#show_yes_btn").click(function(){
       $("#showWindow").data("kendoWindow").close();
	});
    
    root.addReportBaseInfo = function(barcode){
        if (root.isPower) {
			//有权限：跳到录报告页面
			var report = {
				barcode: barcode,
				from: "show_reportRenew",
				testType: root.reportType,
			};
			$.cookie("user_0_edit_testreport", JSON.stringify(report), {
				path : '/'
			});
			
            //跳转到录入报告界面
            window.open('/fsn-core/views/market/'+root.reportPage);
        }else {
            lims.initNotificationMes("对不起，您没有录入报告的权限。", false);
        }
    }
	root.initialize();
});
