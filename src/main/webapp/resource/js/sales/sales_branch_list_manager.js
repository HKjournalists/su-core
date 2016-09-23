$(document).ready(function() {
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var httpPrefix = fsn.getHttpPrefix();
	var branch = window.branch = window.branch || {};
	var salesBranchColumn = null;
	var rwayColumn = null;
	var branchDS = null;
	var buyWayDS = null;
	var addRecommendBuyWin = null;
	var MSGWIN = null;
	var VIEW_MPA_WINDOW = null;
	var provinceAry = null;
	var mapLabel = null;
	var branchIndexNo = 0;
	var buyIndexNo = 0;

	/**
	 * 页面初始化方法
	 * @author tangxin 2015-04-27
	 */
	function initComponent(){
		upload.buildGridByBoolbar("salesBranchGrid",salesBranchColumn,branchDS,"300","toolbar_template_branch");
		upload.buildGridByBoolbar("recommendBuyGrid",rwayColumn,buyWayDS,"300","toolbar_template_rWay");
		MSGWIN = saleskendoUtil.initKendoWindow("messageWindow","500","","保存状态",false,
		true,false,["Close"],"messageLabel","正在保存请稍候...");
		VIEW_MPA_WINDOW = saleskendoUtil.initKendoWindow("viewBranchMapWindow","650","580","查看地图",false,
		true,false,["Close"],null,"");
		addRecommendBuyWin = saleskendoUtil.initKendoWindow("ADD_RECOMMENDBUY_WIN","600","300","新增推荐购买方式",false,
				true,false,["Close"],null,"");
		$("#reBuyAdd_yes_btn").bind("click", saveBuyWay);
		$("#reBuyAdd_no_btn").bind("click", function(){
			if(addRecommendBuyWin != null) {
				addRecommendBuyWin.close();
			}
		});
		$("#viewMapBack_btn").bind("click",detailMapBackFun);
		provinceAry = getListCityByProv("省份","00");
	}
	
	salesBranchColumn = [
	                     { field:"id",title:fsn.l("id"),editable: false,width:20, template:function(model){
	                    	 return ++branchIndexNo;
	                     }},
			             { field: "name", title:fsn.l("Branch Name"), width: 60 },
			             { field: "address", title:fsn.l("Address"), width: 60 , template:function(branch){
			            	 var addr = "";
			            	 if(branch != null){
			            		 if(branch.province != null) {
			            			 addr = branch.province + branch.city + branch.counties;
			            		 }
			            		 if(branch.street != null) {
			            			 addr += branch.street;
			            		 }
			            	 }
			            	 return addr;
			             }},
			             { field: "telephone", title:fsn.l("TelePhone"),width:50 },
			             { field: "contact", title:fsn.l("Contact"),width:50 },
			             { command: [{name:"Edit",
		                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Edit"),
		                   	    click:function(e){
		                   	    	e.preventDefault();
		                   	    	var delItem = $("#salesBranchGrid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
		                   	    	try {
		                   	    		$.cookie("cook_edit_branchId", JSON.stringify({branchId:delItem.id}), {
		                   	    			path : '/'
		                   	    		});
		                   	    	} catch (e) {}
		                   	    	window.location.href = "add_sales_branch.html";
			             }},
	                      {name:"Delete",
	                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
	                   	    click:function(e){
	                   	    	e.preventDefault();
	                   	    	var delItem = $("#salesBranchGrid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
	                   	    	branch.delId = delItem.id;
	                   	    	fsn.initConfirmWindow(delBranchConfirmFun,null,"资料删除后将无法找回，确认需要删除此网点信息吗？","");
	                   	    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
	                      }}], title: fsn.l("Operation"), width: 60 }                     
	                   ];
	
	rwayColumn = [
                  { field:"id",title:fsn.l("id"),editable: false,width:20, template:function(model){
                	  return ++buyIndexNo;
                  }},
                  { field: "name", title:fsn.l("Buy Way Name"), width: 60 },
                  { field: "way", title:fsn.l("Buy Way"),  width: 60 },
                  { command: [{name:"Edit",
                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Edit"),
                   	    click:function(e){
                   	    	e.preventDefault();
                   	    	var delItem = $("#recommendBuyGrid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
                   	    	editBuyway(delItem);
                   }},
                    {name:"Delete",
                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
                   	    click:function(e){
                   	    	e.preventDefault();
                   	    	var delItem = $("#recommendBuyGrid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
                   	    	branch.delBuyWayId = delItem.id;
                   	    	fsn.initConfirmWindow(delBuyWayConfirmFun,null,"资料删除后将无法找回，确认需要删除此推荐购买方式吗？","");
                   	    	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
                   }}], title: fsn.l("Operation"), width: 30 }                     
                 ];
	
    /* 销售网点 grid 的dataSource */ 
	branchDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter) {
            			configure = filter.configure(options.filter);
            		}
            		branchIndexNo = (options.page - 1) * options.pageSize;
            		return httpPrefix + "/sales/branch/getListBranch/" + configure+"/" + options.page + "/" + options.pageSize;
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
	
	/* 推荐购买方式 grid 的dataSource */ 
	buyWayDS = new kendo.data.DataSource({
		transport: {
            read: {
            	url : function(options){
            		var configure = null;
            		if(options.filter) {
            			configure = filter.configure(options.filter);
            		}
            		buyIndexNo = (options.page - 1) * options.pageSize;
            		return httpPrefix + "/sales/branch/getListBuyWay/" + configure+"/" + options.page + "/" + options.pageSize;
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
	 * 添加销售网点的button事件
	 * @author tangxin 2015-04-27
	 */
	branch.addBranch = function (){
		window.location.href = "add_sales_branch.html";
	};
	
	/**
	 * 确定删除销售网点的button事件
	 * @author tangxin 2015-04-27
	 */
	function delBranchConfirmFun(){
		if(branch.delId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/branch/delBranchById/" + branch.delId,
           type: "DELETE",
           dataType: "json",
           success: function(returnData) {
              if(returnData.result.status == "true"){
            	  fsn.initNotificationMes(fsn.l("删除成功！"), true);
            	  fsn.fleshGirdPageFun(branchDS);
            	  provinceAry = getListCityByProv("省份","00");
            	  $("#chainMap").html("");
      			  initChinaMap(provinceAry);
      			  isInitChinaMap = true;
              } else {
            	  fsn.initNotificationMes(fsn.l("删除失败！"), false);
              }
          }
		});
	}
	
	/**
	 * 新增推荐购买方式button的click事件
	 * @author tangxin 2015-04-27
	 */
	branch.openAddRecommendWin = function(){
		clearBuywayWinVal();
		saleskendoUtil.clearAllMsg();
		if(addRecommendBuyWin != null) {
			addRecommendBuyWin.open().center();
		}
	}
	
	/**
	 * 验证推荐购买方式名称是否重复
	 * @author tangxin 2015-04-29
	 */
	function validateName(name){
		if(name == null || name == "") {
			return false;
		}
		var vid = $("#recommendBuyName").attr("data-buyid-data");
		vid = (vid == null ? -1 : vid);
		var result = true;
		$.ajax({
	           url: httpPrefix + "/sales/branch/countRecommendByName/" + name + "/" + vid,
	           type: "GET",
	           dataType: "json",
	           async:false,
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  result = returnData.count > 0 ? false : true;
	              }
	          }
		});
		return result;
	}
	
	$("#recommendBuyName").blur(function(){
		var val = $(this).val().trim();
		if(val == ""){
			saleskendoUtil.msg("v_buyname_span","不允许为空！");
		} else {
			saleskendoUtil.clearMsg("v_buyname_span");
		}
	});
	
	$("#recommendBuyWay").blur(function(){
		var val = $(this).val().trim();
		if(val == ""){
			saleskendoUtil.msg("v_buyway_span","不允许为空！");
		} else {
			saleskendoUtil.clearMsg("v_buyway_span");
		}
	});
	
	/**
	 * 保存推荐购买方式前的验证
	 * @author tangxin 2015-04-27
	 */
	function validateBuyWay(){
		saleskendoUtil.clearAllMsg();
		var buyName = $("#recommendBuyName").val().trim();
		var way = $("#recommendBuyWay").val().trim();
		if(buyName == ""){
			saleskendoUtil.msg("v_buyname_span","不允许为空！");
			return false;
		}
		if(way == "") {
			saleskendoUtil.msg("v_buyway_span","不允许为空！");
			return false;
		}
		if(buyName.length > 100) {
			saleskendoUtil.msg("v_buyname_span","不能超过100字符！");
			return false;
		}
		if(way.length > 100) {
			saleskendoUtil.msg("v_buyway_span","不能超过100字符！!");
			return false;
		}
		if(!validateName(buyName)){
			saleskendoUtil.msg("v_buyname_span","购买方式已经存在！");
			return false;
		}
		return true;
	}
	
	/**
	 * 新增时清空页面推荐购买方式输入框的值
	 * @author tangxin 2015-04-29
	 */
	function clearBuywayWinVal(){
		$("#recommendBuyName").removeAttr("data-buyid-data");
		$("#recommendBuyName").val("");
		$("#recommendBuyWay").val("");
	}
	
	/**
	 * 封装页面推荐购买方式Model
	 * @author tangxin 2015-04-27
	 */
	function createBuyWayModel(){
		var buyWayModel = {};
		buyWayModel.id = $("#recommendBuyName").attr("data-buyid-data");
		buyWayModel.name = $("#recommendBuyName").val().trim();
		buyWayModel.way = $("#recommendBuyWay").val().trim();
		return buyWayModel;
	}
	
	/**
	 * 编辑时给推荐购买方式赋值
	 * @author tangxin 2015-04-27
	 */
	function setBuyWayValue(buyWay) {
		if(buyWay == null){
			return ;
		}
		$("#recommendBuyName").attr("data-buyid-data",buyWay.id);
		$("#recommendBuyName").val(buyWay.name);
		$("#recommendBuyWay").val(buyWay.way);
	}
	
	/**
	 * 推荐购买方式的编辑赋值
	 * @author tangxin 2015-04-27
	 */
	function editBuyway(buyWay) {
		if(buyWay == null) {
			return;
		}
		setBuyWayValue(buyWay);
		addRecommendBuyWin.open().center();
	}
	
	/**
	 * 推荐购买方式的保存方法
	 * @author tangxin 2015-04-27
	 */
	function saveBuyWay(){
		if(!validateBuyWay()){
			return ;
		}
		var saveModel = createBuyWayModel();
		MSGWIN.open().center();
		$.ajax({
	           url: httpPrefix + "/sales/branch/buyWay/" + (saveModel.id == null ? "create" : "update"),
	           type: saveModel.id == null ? "POST" : "PUT",
	           dataType: "json",
	           data: JSON.stringify(saveModel),
	           contentType: "application/json; charset=utf-8",
	           success: function(returnData) {
	        	   addRecommendBuyWin.close();
	        	   MSGWIN.close();
	              if(returnData.result.status == "true"){
	            	  fsn.initNotificationMes(fsn.l("保存成功！"), true);
	            	  buyWayDS.read();
	              }else {
	            	  fsn.initNotificationMes(fsn.l("保存失败！"), false);
	              }
	           },
	           error: function(e) {
	        	   fsn.initNotificationMes(fsn.l("服务器异常！"), false);
	           }
	    });
	}
	
	/**
	 * 确定删除销售网点的button事件
	 * @author tangxin 2015-04-27
	 */
	function delBuyWayConfirmFun(){
		if(branch.delBuyWayId == null) {
			return ;
		}
		$.ajax({
           url: httpPrefix + "/sales/branch/delBuyWayById/" + branch.delBuyWayId,
           type: "DELETE",
           dataType: "json",
           success: function(returnData) {
              if(returnData.result.status == "true"){
            	  fsn.initNotificationMes(fsn.l("删除成功！"), true);
            	  fsn.fleshGirdPageFun(buyWayDS);
              } else {
            	  fsn.initNotificationMes(fsn.l("删除失败！"), false);
              }
          }
		});
	}
	
	var isInitChinaMap = false;
	
	/**
	 * 打开查看地图窗口
	 * @author tangxin 2015-04-28
	 */
	branch.openViewMapWindow = function(){
		if(!isInitChinaMap) {
			isInitChinaMap = true;
			initChinaMap(provinceAry);
		}
		// 修改地图展示窗口的宽度，当未进入到详细地图页面时，不需要展示网点的机构化信息，窗口的宽度为650px
		var vMap = document.getElementById("viewBranchMapWindow");
		if(vMap != null) {
			vMap.parentNode.style.width = "650px";
		}
		if(VIEW_MPA_WINDOW != null) {
			//移除中国地图 div 的style属性 ，显示中国地图
			$("#chainMap").removeAttr("style");
			// 清空详细地图的内容
			$("#viewDetailMap").html("");
			// 隐藏详细地图（百度地图）
			$("#viewDetailMap").attr("style","dispaly:none;");
			// 隐藏查看详细地图时，右侧的 销售网点结构化信息
			document.getElementById("branch_field").style.display = "none";
			// 清空销售网点结构化信息内容
			$("#branch_field").html("");
			// 打开地图窗口
			VIEW_MPA_WINDOW.open().center();
		}
	};
	
	// 形象地图的返回方法
	function detailMapBackFun(){
		$("#viewMapBack_btn").attr("style","display:none;");
		$("#viewDetailMap").html("");
		$("#viewDetailMap").attr("style","dispaly:none;");
		var vMap = document.getElementById("viewBranchMapWindow");
		if(vMap != null) {
			vMap.parentNode.style.width = "650px";
		}
		document.getElementById("branch_field").style.display = "none";
		$("#chainMap").slideDown();
	}
	
	/**
	 * 获取企业下销售网点所在的省份
	 * @author tangxin 2015-04-28
	 */
	function getListCityByProv(prov,code){
		if(prov == null || prov == "") return;
		code = (code != null && code != "" ? code : "00");
		var listCity = null;
		$.ajax({
	           url: httpPrefix + "/sales/branch/getListAddrByType/" + prov + "/" + code,
	           type: "GET",
	           dataType: "json",
	           async:false,
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  listCity = returnData.data;
	              } 
	          }
		});
		return listCity;
	}
	
	/**
	 * 获取企业下指定省份的销售网点信息
	 * @author tangxin 2015-06-15
	 */
	function getListBranchByProv(prov){
		if(prov == null || prov == "") return;
		var listBranch = null;
		$.ajax({
	           url: httpPrefix + "/sales/branch/getListByProvince/" + prov ,
	           type: "GET",
	           dataType: "json",
	           async:false,
	           success: function(returnData) {
	              if(returnData.result.status == "true"){
	            	  listBranch = returnData.data;
	              } 
	          }
		});
		return listBranch;
	}
	
	/**
	 * 格式化字符串字段，处理页面显示 null 字段
	 * @author tangxin 2015-05-07
	 */
	function formatFiledStr(value){
		return value == null ? "" : value;
	}
	
	/**
	 * 展示销售网点市级城市
	 * @author tangxin 2015-04-28
	 */
	function viewCityBranch(map, listBranch){
		$("#viewMapBack_btn").removeAttr("style");
		if(listBranch == null || listBranch.length < 1) return;
		var vMap = document.getElementById("viewBranchMapWindow");
		if(vMap != null) {
			vMap.parentNode.style.width = "950px";
		}
		document.getElementById("branch_field").style.display = "block";
		$("#branch_field").html("");
		for(var i=0;i<listBranch.length;i++){
			var branch = listBranch[i];
			var point = new BMap.Point(branch.longitude, branch.latitude);
			var marker = new BMap.Marker(point);
			marker.city_name = branch.name;
			map.addOverlay(marker);
			addPolyEve(marker);
			var htmlStr = '<div class="div-tr"><span class="mr10 name_sty">'+fsn.l("Sales Branch")+'：</span><span>' + formatFiledStr(branch.name) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">'+fsn.l("TelePhone")+'：</span><span>' + formatFiledStr(branch.telephone) + '</span></div>'+
			'<div class="div-tr"><span class="mr10 name_sty">'+fsn.l("Sales Address")+'：</span><span>' + formatFiledStr(branch.address) + '</span></div>' + 
			'<div class="div-tr"><span class="mr10 name_sty"> </span></div>';
			$("#branch_field").append(htmlStr);
		}
	}
	
	/**
	 * 为market 绑定事件
	 * @author tangxin 2015-04-28
	 */
	function addPolyEve(marker,cityName){
		//鼠标进入事件
		marker.addEventListener("mouseover", function (e) {
			if(mapLabel!=null){
				mapLabel.hide();
			}
			mapLabel = new BMap.Label(this.city_name,{offset:new BMap.Size(20,-10)});
			this.setLabel(mapLabel);
		});
		//鼠标移除事件
		marker.addEventListener("mouseout", function (e) {
			if(mapLabel!=null){
				mapLabel.hide();
			}
		});
	}
	
	/**
	 * 详细地图，显示销售网点 市级
	 * @author tangxin 2015-04-28
	 */
	function initBaiduMap(prov){
		if(prov == null || prov == "") return;
		var myGeo = new BMap.Geocoder();
		prov = (prov == "新疆" ? "新疆维吾尔自治区" : prov);
		myGeo.getPoint(prov, function(point){
			if (point) {
				var baiduMap = new BMap.Map("viewDetailMap");
				baiduMap.centerAndZoom(point, 7);
				baiduMap.enableScrollWheelZoom();
				baiduMap.setCurrentCity(prov);
				// 获取指定省份下的销售网点列表
				var listBranch = getListBranchByProv(prov);
				// 在百度地图中展示网点信息
				viewCityBranch(baiduMap, listBranch);
			}
		}, prov);
	}
	
	/**
	 * 方法描述：当鼠标移动到详细地图（百度地图）上时，用户可以通过鼠标滚能来缩放地图，为了禁止浏览器和地图窗口中的内容随之滚动，
	 * 需要给div.k-window-content（地图窗口）和 body 添加属性 overflow-y:hidden ，禁止内容滚动。
	 * @author TangXin 2015/6/15
	 */
	document.getElementById("viewDetailMap").onmouseover = function(){
		$("div.k-window-content").attr("style","overflow-y:hidden;");
		$("body").attr("style","overflow-y:hidden;padding-right:17px;");
	};
	
	/**
	 * 移除 div.k-window-content（地图窗口）和 body 禁止滚动属性
	 * @author TangXin 2015/6/15
	 */
	document.getElementById("viewDetailMap").onmouseout = function(){
		$("div.k-window-content").removeAttr("style");
		$("body").removeAttr("style");
	};
	
	/**
	 * 查看详细地图
	 * @author tangxin 2015-04-28
	 */
	branch.viewDetailMap = function(provName){
		$("#chainMap").slideUp();
		$("#viewDetailMap").attr("style","width:630px;height:500px;");
		initBaiduMap(provName);
		$("#viewDetailMap").show();
	};
	
	/* 页面初始化 */
	initComponent();
})