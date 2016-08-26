$(document).ready(function() {
	/****销售案例添加或是编辑****/
	var fsn = window.fsn = window.fsn || {};
	var upload = fsn.upload = fsn.upload || {};
	var home = window.home = window.home || {};
	var branch = window.branch = window.branch || {};
	var httpPrefix = fsn.getHttpPrefix();
	var SANZHENG_ALBUM_ID = "1"; //三证相册
	var HONOR_ALBUM_ID = "2"; //荣誉证书
	var CERTIFICATION_ALBUM_ID = "3"; //其他认证相册
	var PRODUCT_ALBUM_ID = "4"; //产品相册
	var BUSPHOTO_ALBUM_ID = "5"; //企业掠影
	var isInitChinaMap = false;
	//var CONTRACT_GRID = null;
	var CONTRACT_GRID_COLUMN = null;
	var GRID_DS = null;
	var MSGWINDOW = null;
	var mapLabel = null;
	var color = "#7EA700";

		/* 初始化销售首页页面 */
	function initSalesHome(){
		/*初始化选项卡控件*/
		initToolbar();
		//initToolBarStyle();
		/*初始化企业简介*/
		initBusiness();
		/*初始化企业相册*/
		initPhotoAlbums();
		/*初始化企业销售网点*/
		initSalesBranch();
		saleskendoUtil.initKendoWindow("ADD_CONTRACT_WIN","420","150","电子合同信息",false,true,false,["Close"],null,"");
		saleskendoUtil.initKendoWindow("DOWN_MATERIALS_WIN","370","300","下载电子资料",false,true,false,["Close"],null,"");
		saleskendoUtil.initKendoWindow("SEND_EMAIL_WIN","520","560","发送电子邮件",false,true,false,["Close"],null,"");
		upload.buildGridWioutToolBar("data_contract_grid",CONTRACT_GRID_COLUMN,GRID_DS,"");
		/* 初始化电子资料站的邮件发送和下载 */
		initElectMaterials();
		MSGWINDOW = saleskendoUtil.initKendoWindow("messageWindow","310","65","邮件发送",false,
				true,false,["Close"],"messageLabel","邮件发送中，请稍候...");
		$("#viewMapBack_btn").bind("click",detailMapBackFun);
	}

	/**
	 * 初始化选项卡控件
	 */
	function initToolbar(){
		$("#tabstrip").kendoTabStrip({
			activate : tabActivate,
			animation: {
				open: {effects: "fadeIn"}
			}
		});
	}

	function initToolBarStyle(){
		/* 去掉最外层边框 */
		$("#tabstrip").css("border-width","0px");
		$("#bus_about_li").addClass("tabstrip-li");
		$("#bus_about_li").removeClass("k-state-active");
		$("#tabstrip ul").removeClass("k-tabstrip-items");
		$("#tabstrip ul li").css("float","left");
		$("#tabstrip ul li a").css("color","#ACD0F4");
		$("#tabstrip").css("background-color","#0F941A");
		/* 去掉内层div边框*/
		$("#tabstrip div").css("border-width","0px");
		$("#tabstrip div").css("background-color","#F9FDF1");
		$("#tabstrip ul").css("color","#F9FDF1");

	}

	/**
	 * 初始化企业简介
	 */
	function initBusiness(){
		$.ajax({
			url:httpPrefix+"/sales/index/initbusinfo",
			type:"GET",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					if(returnValue.data != null){
						home.setPageValue(returnValue.data);
						if(returnValue.data.salesCaseVOs != null && returnValue.data.salesCaseVOs.length > 0){
							/* 设置企业的销售案例 */
							home.setSalesCase(returnValue.data.salesCaseVOs);
						}
						/* 设置推荐购买 */
						if(returnValue.data.recommendBuyVOs != null && returnValue.data.recommendBuyVOs.length >0 ){
							home.setRecommend(returnValue.data.recommendBuyVOs);
						}
					}
				}else{
				}
			}
		});
	}

	/**
	 * 初始化企业相册
	 */
	function initPhotoAlbums(){
		$.ajax({
			url: httpPrefix + "/sales/photos/getListAlbums?cut=175x205",
			type: "GET",
			dataType: "json",
			success: function(returnData) {
				if(returnData.result.status == "true"){
					viewAlbums(returnData.data);
				}
			}
		});
	}

	/**
	 * 初始化企业销售网点
	 */
	function initSalesBranch(){
		$("#data_grid").hide();
		$("#viewMapBack_btn").hide();
		var list_province = getListCityByProv("省份","00");
		if(!isInitChinaMap) {
			isInitChinaMap = true;
			initChinaMap(list_province);
		}
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
	 * 相信地图，显示销售网点 市级
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

	function tabActivate(e){
		switch (e.item.id) {
			case "bus_about_li":
				$("#bus_about_li").attr("style","font-size:16px;font-weight: bolder;border:1px green solid;background-color:#3be201");
				$("#bus_album_li").removeAttr("style");
//				$("#show_case_li").removeAttr("style");
				$("#sales_dot_li").removeAttr("style");
				$("#data_station_li").removeAttr("style");
				break;
			case "bus_album_li":
				$("#bus_album_li").attr("style","font-size:16px;font-weight: bolder;border:1px green solid;background-color:#3be201");
				$("#bus_about_li").removeAttr("style");
//				$("#show_case_li").removeAttr("style");
				$("#sales_dot_li").removeAttr("style");
				$("#data_station_li").removeAttr("style");
				
				break;
//			case "show_case_li":
//				$("#show_case_li").attr("style","font-size:16px;font-weight: bolder;border:1px green solid;background-color:#3be201");
//				$("#bus_about_li").removeAttr("style");
//				$("#bus_album_li").removeAttr("style");
//				$("#sales_dot_li").removeAttr("style");
//				$("#data_station_li").removeAttr("style");
//				break;
			case "sales_dot_li":
				$("#sales_dot_li").attr("style","font-size:16px;font-weight: bolder;border:1px green solid;background-color:#3be201");
				$("#bus_about_li").removeAttr("style");
				$("#bus_album_li").removeAttr("style");
//				$("#show_case_li").removeAttr("style");
				$("#data_station_li").removeAttr("style");
				break;
			case "data_station_li":
				$("#data_station_li").attr("style","font-size:16px;font-weight: bolder;border:1px green solid;background-color:#3be201");
				$("#bus_about_li").removeAttr("style");
				$("#bus_album_li").removeAttr("style");
//				$("#show_case_li").removeAttr("style");
				$("#sales_dot_li").removeAttr("style");
				break;
			default :
				break;
		}
	}

	/**
	 * 方法描述：当鼠标移动到销售网点详细地图（百度地图）上时，用户可以通过鼠标滚能来缩放地图，为了禁止浏览器中的内容随之滚动，
	 * 需要给 body 添加属性 overflow-y:hidden ，禁止内容滚动。
	 * @author TangXin 2015/6/15
	 */
	document.getElementById("viewDetailMap").onmouseover = function(){
		$("body").attr("style","overflow-y:hidden;margin-right:17px;");
	};
	
	/**
	 * 移除 body 禁止滚动属性
	 * @author TangXin 2015/6/15
	 */
	document.getElementById("viewDetailMap").onmouseout = function(){
		$("body").removeAttr("style");
	};
	
	/**
	 * 查看详细地图
	 * @author tangxin 2015-04-28
	 */
	branch.viewDetailMap = function(provName){
		$("#chainMap").slideUp();
		$("#viewDetailMap").attr("style","width:650px;height:300px;");
		initBaiduMap(provName);
		$("#viewDetailMap").show();
	};

	/**
	 * 格式化字符串字段，处理页面显示 null 字段
	 * @author tangxin 2015-05-07
	 */
	function formatFiledStr(value){
		return value == null ? "" : value;
	}
	
	/**
	 * 展示指定省份下的销售网点信息
	 * @author tangxin 2015-04-28
	 */
	function viewCityBranch(map, listBranch){
		$("#viewMapBack_btn").removeAttr("style");
		if(listBranch == null || listBranch.length < 1) return;
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
			'<div class="div-tr"><span class="mr10 name_sty">'+fsn.l("Telephone")+'：</span><span>' + formatFiledStr(branch.telephone) + '</span></div>'+
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
	 * 初始化电子资料站的邮件发送和下载
	 */
	function initElectMaterials(){
		/* 获取附件内容 */
		var contracts = getDataAnnex();
		var cont_tag = setAnnexDiv(contracts);
		$("#contract_div").html(cont_tag);
		$("#annex-div").html(cont_tag);
	}

	/**
	 * 获取指定省下的市级网点
	 * @author HY 2015-04-28
	 */
	function getListCityByProv(keyword,type){
		if(keyword == null) return;
		var listAddr = null;
		$.ajax({
			url: httpPrefix + "/sales/branch/getListAddrByType/" + keyword + "/" + type,
			type: "GET",
			dataType: "json",
			async:false,
			success: function(returnData) {
				if(returnData.result.status == "true"){
					listAddr = returnData.data;
				}
			}
		});
		return listAddr;
	}
	
	
	

	/**
	 * 设置页面上的企业信息
	 */
	home.setPageValue = function(model){
		var publicityImgUrl = "http://fsnrec.com:8080/portal/img/FSC/BUS_XC.png";//默认的企业宣传照
		var urls= [];
		if(model.publicityUrl != null && model.publicityUrl != ""){
			publicityImgUrl = model.publicityUrl;
			urls = publicityImgUrl.split("|");
		}else{
			urls[0] = publicityImgUrl;
		}
		for(var i=0;i<urls.length;i++)
		{
			
			var img = $('<a href="#" title=""><img src="'+urls[i]+'"/></a>');
			$(".changeDiv").append(img);
		}
		
		new slide("#main-slide","cur",600,300,1);
		if(urls.length<=1){
			$(".FocusPic .change i").hide();
		}
		/*var flag = false;
		if(urls.length>1)flag = true;
		$("#slides").slidesjs({
            width: 600,
            height: 300,
            play: {
              auto: flag,
              interval: 2000
            },
            effect: {
  			  slide: {
  				speed: 2000
  			  },
  			  fade:{
  				speed: 2000
  			  }
  			},
  			pagination:{active: flag},
    		navigation: {active:false}
        });*/
		var qrCode =(model.qrCodeUrl!=null && model.qrCodeUrl != "") ? '<img src="'+model.qrCodeUrl+'" width="210px" heigth="210px"/>' : "";
		//$("#publicity_div img").attr("src", urls[0]);
		$("#bus_qrcode").html(qrCode);
		$("#bus_name").html(model.busName);
		$("#bus_about").html(model.busNote);
		$("#bus_addr").html(model.busAddress);
		$("#bus_tel").html(model.busTel);
		$("#bus_email").html(model.busEmail);
		$("#bus_contact").html(model.busContact);
		
		var litag = '';
		if(model.recommendBuyVOs != null && model.recommendBuyVOs.length > 0){
			for(var i = 0 ; i <model.recommendBuyVOs.length ; i++){
				litag += '';
			}
		}
	};
	/**
	 * 设置企业的销售案例
	 */
	home.setSalesCase = function(arr){
		var caseTag = '';
		var trTag = '';
		for(var i = 0 ; i < arr.length ; i++){
			caseTag += '<td><a href="salescase_albums_detail.html?'+arr[i].id+'" target="_blank">'+
			'<img src="'+arr[i].url+'" alt="'+arr[i].salesCaseName+'" />'+
			'<span class="long_text">'+arr[i].salesCaseName+'</span></a></td>';
			if((i+1)%4==0){
				trTag += '<tr class="sales-case">'+caseTag+'</tr>';
				caseTag = '';
			}
		}
		trTag = '<table>'+trTag+'<tr class="sales-case">'+caseTag+'</tr></table>';

		$("#sales_case_li").html(trTag);
	};
	/**
	 * 设置推荐购买
	 */
	home.setRecommend = function(arr){
		var tag = '';
		if(arr!=null&&arr.length>0){
			for(var i = 0 ; i < arr.length ; i++){
				tag +='<div style="margin-bottom:10px;width:70%;"><span class="sp_rem_name">购买方式:</span><span>'+arr[i].name+'</span></br><span class="sp_rem_name">购买地址:</span><span>'+arr[i].way+'</span></div>';
			}
		}
		$("#bus_recommend").html(tag);
	};

	/**
	 * 返回销售网点
	 */
	function detailMapBackFun(){
		$("#viewMapBack_btn").hide();
		$("#viewDetailMap").html("");
		$("#viewDetailMap").hide();
		$("#branch_field").html("");
		document.getElementById("branch_field").style.display = "none";
		$("#chainMap").slideDown();
	}

	/**
	 * 在页面展示相册
	 * @author HY 2015-05-05
	 */
	function viewAlbums(listAlbums){
		if(listAlbums == null || listAlbums.length < 1) {
			return;
		}
		for(var i=0; i< listAlbums.length; i++) {
			var album = listAlbums[i];
			switch(album.albumID){
				case SANZHENG_ALBUM_ID:viewSanZhengAlbums(album);
					break;
				case HONOR_ALBUM_ID:viewHonorAlbums(album);
					break;
				case CERTIFICATION_ALBUM_ID:viewCertAlbums(album);
					break;
				case PRODUCT_ALBUM_ID:viewProductAlbums(album);
					break;
				case BUSPHOTO_ALBUM_ID: viewBusPhotosAlbums(album);
					break;
			}
		}
	}

	/**
	 * 三证相册展示
	 * @author HY 2015-05-05
	 */
	function viewSanZhengAlbums(album){
		if(album == null) {
			return;
		}
		$("#SANZHENG_IMG_ID").attr("src",album.coverPhoto);
		$("#SANZHENG_NUM_ID").html(album.photoNo);
		$("#SANZHENG_NAME_ID").html(album.albumName);
	}

	/**
	 * 荣誉证书相册展示
	 * @author HY 2015-05-05
	 */
	function viewHonorAlbums(album){
		if(album == null) {
			return;
		}
		$("#HONOR_IMG_ID").attr("src",album.coverPhoto);
		$("#HONOR_NUM_ID").html(album.photoNo);
		$("#HONOR_NAME_ID").html(album.albumName);
	}

	/**
	 * 认证证照相册展示
	 * @author HY 2015-05-05
	 */
	function viewCertAlbums(album){
		if(album == null) {
			return;
		}
		$("#CERT_IMG_ID").attr("src",album.coverPhoto);
		$("#CERT_NUM_ID").html(album.photoNo);
		$("#CERT_NAME_ID").html(album.albumName);
	}

	/**
	 * 产品相册展示
	 * @author HY 2015-05-04
	 */
	function viewProductAlbums(album){
		if(album == null) {
			return;
		}
		$("#PRODUCT_IMG_ID").attr("src",album.coverPhoto);
		$("#PRODUCT_NUM_ID").html(album.photoNo);
		$("#PRODUCT_NAME_ID").html(album.albumName);
	}

	/**
	 * 产品相册展示
	 * @author HY 2015-05-04
	 */
	function viewBusPhotosAlbums(album){
		if(album == null) {
			return;
		}
		$("#BUSPHOTOS_IMG_ID").attr("src",album.coverPhoto);
		$("#BUSPHOTOS_NUM_ID").html(album.photoNo);
		$("#BUSPHOTOS_NAME_ID").html(album.albumName);
	}

	/**
	 * 验证相册是否为空
	 */
	function validateAlbumsEmpty(type){
		var status = false;
		switch(type){
			case SANZHENG_ALBUM_ID:
				size = parseInt($("#SANZHENG_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case HONOR_ALBUM_ID:
				size = parseInt($("#HONOR_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case CERTIFICATION_ALBUM_ID:
				size = parseInt($("#CERT_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case PRODUCT_ALBUM_ID:
				size = parseInt($("#PRODUCT_NUM_ID").html());
				if(size > 0) status = true;
				break;
			case BUSPHOTO_ALBUM_ID:
				size = parseInt($("#BUSPHOTOS_NUM_ID").html());
				if(size > 0) status = true;
				break;
		}
		return status;
	}
	
	/**
	 * 查看相册相信信息
	 * @author HY 2015-05-05
	 */
	home.albumDetail = function(albumId){
		if(albumId == null) return;
		var status = validateAlbumsEmpty(albumId);
		if(!status){
			fsn.initNotificationMes('相册为空！',false);
			return;
		}
		try {
			$.cookie("cook_album_detail_id", JSON.stringify({albumId:albumId}), {
				path : '/'
			});
		} catch (e) {}
		window.open("photos_albums_detail.html?status=1") ;
	};

	/**
	 * 显示电子资料中的 电子合同
	 */
	home.viewContract = function(){
		/* 隐藏下载页面 */
		$("#data_btn").hide();
		$("#data_grid").show();
		/*/!* 加载电子合同GRID *!/
		if($("#data_contract_grid").data("kendoGrid")){
			upload.buildGridWioutToolBar("data_contract_grid",CONTRACT_GRID_COLUMN,GRID_DS,"380");
		}*/

	};

	CONTRACT_GRID_COLUMN = [
		{ field:"id",title:fsn.l("Contract Id"),width:25},
		{ field: "name", title:fsn.l("Contract Name"), width: 60},
		{ title:fsn.l("Contract Note"), width: 130, template:function(model){
			if(model == null) return "";
			return "<span class='long_text'>"+model.remark + "</span>";
		} },
		{ command:
			[
				{
					name: "View",
					text: "<span class='k-icon k-i-search'></span>" + fsn.l("View"),
					click: function (e) {
						e.preventDefault();
						var viewModel = this.dataItem($(e.currentTarget).closest("tr"));
						if (viewModel.resource == null || viewModel.resource.length < 1 || viewModel.resource[0].id == null) {
							viewModel.resource = getContractRes(viewModel.guid);
						};
						home.openAddContractWin(viewModel);
					}
				},
				{
					name: "Download",
					text: "<span class='k-icon k-add'></span>" + fsn.l("Download"),
					click: function (e) {
						e.preventDefault();
						var delItem = this.dataItem($(e.currentTarget).closest("tr"));
						if (delItem.resource == null || delItem.resource.length < 1 || delItem.resource[0].id == null) {
							delItem.resource = getContractRes(delItem.guid);
						};
						if (delItem.resource != null && delItem.resource.length > 0) {
							home.downdLoadContract(delItem.resource[0].id);
						} else {
							fsn.initNotificationMes(fsn.l("The contract does not have the resources to download") + "!", false);
						};
					}
				}
		], title: fsn.l("Operation"), width: 40}
	];

	GRID_DS = new kendo.data.DataSource({
		transport: {
			read: {
				url : function(options){
					var configure = null;
					if(options.filter) {
						configure = filter.configure(options.filter);
					}
					return httpPrefix + "/sales/electdate/contract/getListContract/" + configure+"/" + options.page + "/" + options.pageSize;
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
	 * 下载合同附件
	 * @author HY 2015-04-30
	 */
	home.downdLoadContract = function(resId) {
		if(resId) {
			var httpUrl = httpPrefix + "/resource/download/" + resId +"?type=sales";
			lims.downloadByUrl(httpUrl);
		} else {
			fsn.initNotificationMes(fsn.l("The contract failed to download, please try again") + "!", false);
		}
	};

	/**
	 * 通过全局guid 获取资源列表
	 * @author HY 2015-04-30
	 */
	function getContractRes(guid){
		if(!guid) return;
		var listRes = null;
		$.ajax({
			url: httpPrefix + "/sales/electdate/getListResourceByGuid/" + guid ,
			type: "GET",
			async:false,
			dataType: "json",
			success: function(returnData) {
				if(returnData.result.status == "true"){
					listRes = returnData.data;
				}
			}
		});
		return listRes;
	}

	/**
	 * 预览选中的电子合同
	 */
	home.openAddContractWin = function(item){
		$("#contractName").html(item.name);
		$("#remark").html(item.remark);
		if(item.resource.length > 0){
			$("#listView-div").html(item.resource[0].fileName);
		}
		$("#ADD_CONTRACT_WIN").data("kendoWindow").open().center();
	};
	/**
	 * 反回资料站首页
	 */
	home.contractGoBack = function(){
		$("#data_btn").show();
		$("#data_grid").hide();
	};

	/**
	 * 发送电子邮件
	 */
	home.sendEMail = function(){
		$("#SEND_EMAIL_WIN").data("kendoWindow").open().center();
	};
	/**
	 * 下载电子资料
	 */
	home.downMaterials = function(){
		$("#DOWN_MATERIALS_WIN").data("kendoWindow").open().center();
	};

	/**
	 * 获取电子附件并初始化
	 */
	function getDataAnnex(){
		var listRes = null;
		$.ajax({
			url: httpPrefix + "/sales/electdate/getListContracts" ,
			type: "GET",
			async:false,
			dataType: "json",
			success: function(returnData) {
				if(returnData.result.status == "true"){
					listRes = returnData;
				}
			}
		});
		return listRes;
	}

	function setAnnexDiv(arr){
		var tag = '';
		if(arr.electData != null){
			$("#edata_a").attr("href",arr.electData.url);
			tag += '<span class="k-checkbox scheck"><input type="checkbox" name="checkbox" value="'+arr.electData.resId+'">'+arr.electData.contractName+';</span>';
		}else{
			$("#edata_a").attr("title","您还没有创建电子资料");
		}
		if(arr.contracts != null && arr.contracts.length > 0){
			for(var i = 0 ; i < arr.contracts.length ; i++){
				tag += '<span class="k-checkbox scheck"><input type="checkbox" name="checkbox" value="'+arr.contracts[i].resId+'">'+arr.contracts[i].contractName+';</span>';
			}
		}
		return tag;
	}

	home.down_btn = function(){
		var resIdArr = "";
		var boxArr=document.getElementsByName("checkbox");
		if(boxArr && boxArr.length > 0){
			resIdArr = new Array();
			for(var i = 0 ; i < boxArr.length ; i++){
				if($(boxArr[i]).is(':checked')){
					resIdArr += boxArr[i].value+"," ;
				}
			}
		}
		if(resIdArr != "" ){
			downloadAnnex(resIdArr);
		}else{
			fsn.initNotificationMes("您没有勾选要下载的附件!", false);
		}
	};

	/**
	 * 验证邮件发送信息
	 * @author tangxin 2015-05-10
	 */
	function validateSendMailData(){
		var addr = $("#addressee").val().trim();
		if(addr == ""){
			fsn.initNotificationMes("收件人不能空!", false);
			return false;
		}
		addr = addr.replace("，",";").replace("；",";").replace(",",";");
		var addrArr = addr.split(";");
		var reg =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		for(var i=0; i < addrArr.length; i++ ){
			if(!reg.test(addrArr[i])){
				fsn.initNotificationMes("第"+(i+1)+"位收件人应该是邮箱格式，请正确填写!", false);
				return false;
			}
		}
		var theme = $("#theme").val().trim();
		if(theme == ""){
			fsn.initNotificationMes("请填写邮件主题!", false);
			return false;
		}
		return true;
	}
	
	/**
	 * 创建邮件发送信息
	 * @author tangxin 2015-05-10
	 */
	function createMailInfo(){
		/* 附件集合 */
		var attachments = [];
		var boxArr=document.getElementsByName("checkbox");
		/* 封装用户勾选的附件信息 */
		if(boxArr && boxArr.length > 0){
			for(var i = 0 ; i < boxArr.length ; i++){
				if($(boxArr[i]).is(':checked')){
					attachments.push(parseInt(boxArr[i].value));
				}
			}
		}
		var mailInfo ={attachments:attachments};
		mailInfo.to = $("#addressee").val().trim();
		mailInfo.subject = $("#theme").val().trim();
		var txt = $("#maintext").val().trim();
		txt = (txt != "" ? txt : "  感谢您选择我公司！我公司将竭诚为您服务！希望与贵公司合作愉快，实现双赢！");
		mailInfo.text = txt;
		return mailInfo;
	}
	
	/**
	 * 验证邮件发送
	 * @author tangxin 2015-05-10
	 */
	home.sendMail_fun = function(){
		if(!validateSendMailData()){
			return;
		}
		var mailInfo = createMailInfo();
		if(MSGWINDOW != null) {
			MSGWINDOW.open().center();
		}
		$.ajax({
			url: httpPrefix + "/sales/electdate/sendEmail" ,
			type: "POST",
			dataType: "json",
			data: JSON.stringify(mailInfo),
	        contentType: "application/json; charset=utf-8",
			success: function(returnData) {
				MSGWINDOW.close();
				$("#SEND_EMAIL_WIN").data("kendoWindow").close();
				if(returnData.result.status == "true"){
					fsn.initNotificationMes("邮件发送成功!", true);
					/* 清空发送的历史邮件信息 */
					emptyEmail();
				}else{
					fsn.initNotificationMes("邮件发送失败!", false);
				}
			}
		});
	};

	function emptyEmail(){
		$("#addressee").val("");
		$("#theme").val("");
		$("#maintext").val("");
		/* 将多选框初始化到未勾选状态 */
		var boxArr=document.getElementsByName("checkbox");
		if(boxArr && boxArr.length > 0){
			for(var i = 0 ; i < boxArr.length ; i++){
				if($(boxArr[i]).is(':checked')){
					boxArr[i].checked = false;
				}
			}
		}
	}
	
	home.down_close = function(){
		$("#DOWN_MATERIALS_WIN").data("kendoWindow").close();
	};

	home.close_sendMailWin = function(){
		$("#SEND_EMAIL_WIN").data("kendoWindow").close();
	};
	
	function downloadAnnex(idStr){
		lims.downloadByUrl(httpPrefix + "/sales/electdate/downLoadElectData/"+idStr);
		$("#DOWN_MATERIALS_WIN").data("kendoWindow").close();
	}
	//企业相册漂浮框
	home.doAlbum = function(e,m,n){
		var x = 0;
		var y = 0;
	    if(m==2){
			x = 300;
			y = 600;
		}
		$(".show_"+m+"_"+n).eq($(this).index()).show().css({
			"left": e.pageX-x,
            "top": e.pageY-y
			  }).siblings("div").hide();
	}
	//实现鼠标移动到指定div范围出现悬浮提示狂（首页）=========开始=====wubiao=====
	$('.to_top').hide();
    $('.list li').mousemove(function(e){
        $('.to_top').eq($(this).index()).show().css({
        	"left": e.pageX-220,
            "top": e.pageY-200
			  }).siblings("div").hide();
    });
    $('.list li').mouseleave(function(){
        $('.to_top').hide();
    });
  //实现鼠标移动到指定div范围出现悬浮提示狂=========结束====wubiao======
	initSalesHome();
});