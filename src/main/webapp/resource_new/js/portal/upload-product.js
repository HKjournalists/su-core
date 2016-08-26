$(document).ready(function() {
	// 认证信息下拉框
	var portal = fsn.portal = fsn.portal || {};
	var	upload = fsn.upload = fsn.upload || {};
    portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
    portal.isNew = true;
    portal.aryProAttachments = new Array();
    portal.aryCertAttachments = new Array();
    portal.aryLabAttachments = new Array();
    portal.aryProLicImg = new Array();
    portal.aryProQsImg = new Array();
    portal.aryProDisImg = new Array();
    portal.identify = '';
    portal.delDivIndex=-1;

    /**
     * 从cookie中获取产品id(报告录入界面，产品不存在时，跳转至产品新增界面，并自动加载产品条形码)
     * @author ZhangHui 2015/4/22
     */
    try {
		portal._barcode = $.cookie("user_0_edit_product").barcode;
		$.cookie("user_0_edit_product", JSON.stringify({}), {
			path : '/'
		});
    } catch (e) {}
    
	/**
	 * 编辑产品，从url中获取产品id。（F5页面刷新后，数据丢失的问题）
	 * 可对比pid和orig_pidmd5，判断有无被篡改
	 * @author HuangYong 2015/4/22
	 */
    try {
    	var arrayParam = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    	var pid = arrayParam[0]; // 产品id（原始id，未被编码）
    	var orig_pidmd5 = arrayParam[1]; // 产品id(被编码过的产品id)
    	portal.edit_id = portal.md5validate(pid,orig_pidmd5);
    } catch (e) {}
	
	/**
	 * 页面初始化
	 */
	portal.init = function() {
		portal.initialComponent();  // 初始化页面控件
		portal.loadDataSource();
		/**
		 * Click时间绑定
		 * @author ZhangHui 2015/4/22
		 */
		portal.bindClick();
	};
	
	/**
	 * 初始化页面组件
	 */
	portal.initialComponent = function(){
		/* 根据当前企业类型，自定义初始化 */
		portal.initComponentByType();
		
		/* 初始化产品图片上传控件 */
		upload.buildUpload("upload_product_files", portal.aryProAttachments, "proFileMsg", "IMG");
		/* 初始化执行标准、产品分类 */
		portal.initialCategorys();
		/* 初始化认证信息 */
		portal.initialCertGrid(null);
		/* 初始化营养报告table */
		var ds_nutri = [{ name:"", value:"", unit:"", nrv:""},
			     	    { name:"", value:"", unit:"", nrv:""},
			     	    { name:"", value:"", unit:"", nrv:""}];
		portal.initProductNutri(ds_nutri);
		if(portal.current_bussiness_type=='02'){
		portal.initProductUrl(null);
		}
		/* 下拉框选择控件初始化 */
		portal.initDropDown();
		/* 初始化Window弹框 */
		fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
		fsn.initKendoWindow("saveWindow","警告","400px","",false,null);
		fsn.initKendoWindow("chooseBrandWindow","请选择品牌类别","320px","200px",false,'[]');
		fsn.initKendoWindow("lead_dealer_window","引进产品","550px","410px",false,null);
		fsn.initKendoWindow("lead_window","引进产品","320px","150px",false,[]);
		fsn.initKendoWindow("addRegularity_window","选择执行标准","800px","430px",false,null);
		fsn.initKendoWindow("customer_select_window","选择销往客户","550px","410px",false,'[]');
		
		var radioVal=$('input:radio[name="agricultural"]:checked').val();
		
		if(portal.edit_id){
			
		}else{	
			if(radioVal==1){
				$("#barcode").attr("readonly",true);	
			}
		}
	};
	
	/**
	 * Click绑定事件
	 * @author ZhangHui 2015/4/22
	 */
	portal.bindClick = function(){
		
		/* save 保存按钮 */
		$("#save").on("click", portal.save);
		/* 清空产品图片按钮  */
		$("#btn_clearProFiles").bind("click", portal.clearProFiles);
		/* 营养报告条件输入值的验证 */
		$("#nutriPer").blur(function(){
			var nutriPer = $("#nutriPer").val().trim();
			var index = nutriPer.indexOf("0");
			if(!/^[0-9]*$/.test(nutriPer) || index == 0){
				fsn.initNotificationMes("此处只能输入整数，或者您输入的值不合法。", false);
				$("#nutriPer").val("");
			}
		});
		/* 选择品牌类别提示框的确定按钮 */
		$("#btn_bcOK").click(function(){
			$("#chooseBrandWindow").data("kendoWindow").close();
			var brandId = $("#brandCategory").data("kendoDropDownList").value();
			$("#businessBrand").data("kendoComboBox").value(brandId);
			portal.isSelectBrand = true;
		});
		/* 验证条形码是否存在 */
		$("#barcode").bind("blur", portal.validateBarcodeUnique);
		/* 没有产品图片的警告框的确定按钮 */
		$("#btn_saveOK").click(function(){
			 $("#saveWindow").data("kendoWindow").close();
			 portal.upload();
		});
		/* 没有产品图片的警告框的取消按钮 */
		$("#btn_saveCancel").click(function(){
			 $("#saveWindow").data("kendoWindow").close();
		});
		/* 经销商引进产品提示框的确定按钮 */
		$("#btn_dealer_leadOK").click(function(){
			 portal.leadProductByBarcode();
		});
		/* 经销商引进产品提示框的取消按钮 */
		$("#btn_leadCancel").click(function(){
			 $("#lead_dealer_window").data("kendoWindow").close();
		});
		/* 流通企业引进产品提示框的确定按钮 */
		$("#btn_leadOK").click(function(){
			 portal.leadProductByBarcode();
		});
		/* 流通企业引进产品提示框的取消按钮 */
		$("#btn_leadCancel").click(function(){
			 $("#lead_window").data("kendoWindow").close();
		});
		/* 点击执行标准，弹出执行标准选择框 */
		$("#regularityDiv").click(portal.searchRegularity);
		/* 执行标准保存按钮 */
		$("#btn_regularity_save").click(function(){
			$("#regularity").val($("#regularityList").text());
			$("#addRegularity_window").data("kendoWindow").close();
		});
		/* 执行标准添加按钮 */
		$("#btn_regularity_add").click(function(){
		 	portal.addRegularity();
		});
		/* 执行标准选择取消按钮 */
		$("#btn_regularity_cancel").click(function(){
			 $("#addRegularity_window").data("kendoWindow").close();
		});
		/* 点击销往客户，弹出销往客户选择框 */
		$("#customerSelectDiv").click(function(){
			// 记录原有的销往客户的数据（当用户点击取消后，要还原到原有的数据）
			var ids = "";
			var customerItems = $("#customerSelectInfo").data("kendoMultiSelect").dataItems();
			for ( var entry in customerItems) {
				ids += (customerItems[entry].id + ",");
			}
			portal.oldCustomerSelectIds = ids;
			$("#customer_select_window").data("kendoWindow").open().center();
		});
		/* 销往企业弹出框确定按钮 */
		$("#btn_customer_select_save").click(function(){
			portal.closeCustomerWindow();
		});
		/* 销往企业弹出框取消按钮 */
		$("#btn_customer_select_cancel").click(function(){
			 // 销往企业数据还原
			 $("#customerSelectInfo").data("kendoMultiSelect").value(portal.oldCustomerSelectIds.split(","));
			 $("#customer_select_window").data("kendoWindow").close();
			 //portal.closeCustomerWindow();
		});
		
		/* 供应商：清空进口食品中文标签图片按钮  */
		$("#btn_clearLabFiles").bind("click", portal.clearLabFiles);
		$(document).on("click",".delete-licence-html",function(){
			var index=parseInt($(this).parents(".delDiv"));
			if(portal.aryProLicImg[index]){
				portal.aryProLicImg[index]={};
			}
			if(portal.aryProQsImg[index]){
				portal.aryProQsImg[index]={};
			}
			if(portal.aryProDisImg[index]){
				portal.aryProDisImg[index]={};
			}
			$(this).parents(".delDiv").remove();
			return false;
		});
		$(document).on("click",".delete-license-btn",function(){
			var index=parseInt($(this).parents(".delDiv").attr('index'));
			if($(this).parent().is(".licImgListView")){
				portal.aryProLicImg[index]={};
			}else if($(this).parent().is(".qsImgListView")){
				portal.aryProQsImg[index]={};
			}else if($(this).parent().is(".disImgListView")){
				portal.aryProDisImg[index]={};
			}
			$(this).parents(".div-td").find("input[type=file]").data("kendoUpload").enable();
			$(this).parent().remove();
		});
	};
	portal.isAgricultural = function(flag){
		if(flag){
		var barcode = portal.getBarcode();
		    barcode = "69N"+barcode;
		   var oldBarcode = $("#barcode").val(); 
			 if(!(oldBarcode!=null&&oldBarcode!=''&&oldBarcode.indexOf("69N")!=-1)){
				 $("#barcode").val(barcode); 
			  }
		   $("#barcode").attr("readonly",true);
		   $("#origin-div").show();
		   $("#operative-norm").html("");
		}else{
			$("#operative-norm").html("*");
			$("#barcode").attr("readonly",false);
			$("#origin-div").hide();
			
		}
	};
	/**
	 * 根据企业类型初始化页面
	 * @author Zhanghui 2015/4/3
	 */
	portal.initComponentByType = function() {
		$("#qs_div").hide();
		portal.currentBusiness = getCurrentBusiness();
		var busType = portal.currentBusiness.type.trim();
		if(busType == "流通企业"){
			portal.current_bussiness_type = "01";
			
			$(".div_busNotSC").css("display","inline");
			$(".div_dealer").css("display","none");
			$(".div_busSC").css("display","none");
			portal.initBusNameAndBrandComplete(); // 初始化所属品牌
		}else if(busType == "流通企业.供应商"||busType.indexOf("餐饮企业")!=-1){
			portal.current_bussiness_type = "0101";
			
			$(".div_busNotSC").css("display","inline");
			$(".div_busSC").css("display","none");
			portal.initBusNameAndBrandComplete();  // 初始化所属品牌、企业名称控件
			portal.initCustomerMultiSelect();      // 初始化[销往企业]多选框控件
			portal.fillCustomerSelect();           // [销往企业]多选框控件数据初始化
			portal.initCustomerMultiSelect_lead(); // 初始化引进产品[销往企业]多选框控件
			/* 初始化进口食品中文标签图片上传控件 */
			upload.buildUpload("upload_lab_files", portal.aryLabAttachments, "labFileMsg", "IMG");
			portal.initCountryOfOriginComplete();    // 初始化进口食品原产地控件
		}else{
			/**
			 * 生产企业、仁怀酒企
			 */
			portal.current_bussiness_type = "02";
			
			$("#qs_div").show();
			portal.initialBrands();    // 初始化所属品牌
			portal.initialQsComp();    // 初始化qs号下拉选择框
			$("#businessName").val(portal.currentBusiness.name);
		}
	};
	
	/**
	 * 初始化生产企业的企业名称和商标控件
	 */
	portal.initBusNameAndBrandComplete = function(){
//		$("#businessName").kendoAutoComplete({
//	         dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
//	         filter: "contains",
//	         placeholder: "搜索...",
//	         select: portal.onSelectBusName,
//	     });
		$("#businessBrand").kendoAutoComplete({
	         dataSource: lims.getAutoLoadDsByUrl("/business/business-brand/getAllName"),
	         filter: "contains",
	         placeholder: "搜索...",
	         select: portal.onSelectBrand,
	     });
	};
	
	/**
	 * 初始化进口食品原产地控件
	 */
	portal.initCountryOfOriginComplete = function(){
		$("#countryOfOrigin").bind("change",portal.countryOfOriginChange);
		$("#countryOfOrigin").kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            placeholder:"请选择",
            dataSource: new kendo.data.DataSource({
				transport: {
		            read: {
		            	url:portal.HTTP_PREFIX + "/country/getAllCountry",
		            	type:"GET"
		            }
		        },
		        schema: {
		        	data: function(returnValue){
		        		return returnValue.data;
		        	}
		        }
		    }),
            filter: "contains",
            suggest: true,
            select: function(e) {
	            portal.isSelectCountry = true;
	          }
            //index: 3
        });
	};
	
	/**
	 * 初始化下拉框控件
	 * @param ZhangHui 2015/4/22
	 */
	portal.initDropDown = function(){
		/**
	     * 初始化营养报告 单位的下拉框 
	     * @author tangxin
	     */
		$("#nutriUnitDropDown").kendoDropDownList({
			dataSource: [
				{ id: 1, name: "g" },
				{ id: 2, name: "ml" },
				{ id: 3, name: "KJ" },
			],
			dataTextField: "name",
			dataValueField: "id",
			change: function(e) {
			    var text = this.text();
			    if(text == null) return;
			    if(text == "g" || text == "ml"){
			    	$("#nutriPer").val("100");
			    } else {
			    	$("#nutriPer").val("420");
			    }
			}
		});
		
		$("#brandCategory").kendoDropDownList({
	        dataTextField: "name",
	        dataValueField: "id",
	        dataSource: [],
	        filter: "contains",
	        minLength: 0,
	        index:0
		});
		
		//初始化单位下拉控件
		$("#productUnit").kendoAutoComplete({
		    dataSource: lims.getAutoLoadDsByUrl("/product/getAllUnitName"),
		    filter: "contains",
		    placeholder: "例如：60g、箱、500ml/瓶、2瓶/盒、克/袋",
		    select: portal.onSelectUnitName,
		});
		//
		$("#province_id").kendoComboBox({
            dataTextField: "province",
            dataValueField: "provinceId",
            placeholder:"请选择",
		    change:function(e){
		    	if(this.value()!=null&&this.value()!=''){
		    	$.get(portal.HTTP_PREFIX + "/erp/address/findCityByProId/"+this.value(),function(rs){
		    		$("#city_id").data("kendoComboBox").dataSource.data(rs.cities);
		    		$("#city_id").data("kendoComboBox").text('');
		    		$("#area_id").data("kendoComboBox").text('');
		    	},'json');
		    	}
		    }
        });
		$.get(portal.HTTP_PREFIX + "/erp/address/findPro",function(rs){
		    $("#province_id").data("kendoComboBox").dataSource.data(rs.result);
		},'json');
		$("#city_id").kendoComboBox({
			dataTextField: "city",
            dataValueField: "cityId",
            placeholder:"请选择",
            change:function(e){
            	if(this.value()!=null&&this.value()!=''){
            	$.get(portal.HTTP_PREFIX + "/erp/address/findAreaByCityId/"+this.value(),function(rs){
		    		$("#area_id").data("kendoComboBox").dataSource.data(rs.areas);
		    		$("#area_id").data("kendoComboBox").text('');
		    	},'json');
               }
            }
		});
		$("#area_id").kendoComboBox({
			dataTextField: "area",
            dataValueField: "areaId",
            placeholder:"请选择"
		});
	};
	
	/**
	 * 初始化其他认证信息grid
	 */
	portal.initialCertGrid = function(cerDs){
		cerDs = (cerDs == null ? [] : cerDs);
		var certGrid = $("#certification-grid").data("kendoGrid");
		if(certGrid == null) {
			$("#certification-grid").kendoGrid({
				dataSource:new kendo.data.DataSource({data: cerDs, page: 1,pageSize: 10 }),
				navigatable: true,
				editable: true,
				pageable: {
					messages: lims.gridPageMessage(),
				},
				toolbar: [
				          {template: kendo.template($("#toolbar_template_cert").html())}
				          ],
				          columns: [
				                    { fild:"id",title:"id",editable: false,width:1},
				                    { field: "cert.name", title:"认证类别",editor: portal.certNameDropDownEditor, width: 60 },
				                    { field: "certResource.name", title:"认证图片名称", editor: portal.certResourceEditor, width: 60 },
				                    { title:"有效期截止时间",editor: portal.certEndDateEditor,width:50,template: function(model){
				                    	var endDate = fsn.formatGridDate(model.endDate);
				                    	endDate = (endDate == null ? "" : endDate.toString());
				                    	endDate = (endDate.indexOf("2200-")>-1 ? "长期有效" : endDate);
				                    	return endDate;
				                    }},
				                    { command: [{name:"Remove",
				                    	text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
				                    	click:function(e){
				                    		e.preventDefault();
				                    		var delItem = $("#certification-grid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
				                    		$("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
				                    	}}], title: fsn.l("Operation"), width: 30 }],
	    		});
			} else {
				certGrid.setDataSource(new kendo.data.DataSource({data: cerDs, page: 1,pageSize: 10 }));
			}
	};
	
	/**
	 * 生产企业：初始化 所属品牌
	 */
	portal.initialBrands = function(){
		$("#businessBrand").bind("change",portal.brandChange);
		
		$("#businessBrand").kendoComboBox({
	        dataTextField: "name",
	        dataValueField: "id",
	        headerTemplate: '<div class="dropdown-header">' +
            '<span class="k-widget k-header">品牌类型</span>' +
            '<span class="k-widget k-header">品牌名称</span>' +
            '</div>',
            template: '<div class="dropdown">' +
            '<span class="k-state-default">#: data.brandCategory.name #</span>' +
            '<span class="k-state-default">#: data.name #</span>' +
            '</div>',
	        dataSource: [],
	        filter: "contains",
	        minLength: 0,
	        index:0,
	    });
		$.ajax({
			url:portal.HTTP_PREFIX + "/business/business-brand/getMyBrandsAll",
			type:"GET",
			dataType:"json",
			async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					
					$("#businessBrand").data("kendoComboBox").setDataSource(returnValue.data);
					if(portal.isNew){
						 if(returnValue.data.length>0){
							 $("#businessBrand").data("kendoComboBox").value(returnValue.data[0].name);
							 $("#businessBrand").focus();
						 }else{
							 ///$("#businessBrand").focus();
							// lims.initNotificationMes("品牌为空，请先到品牌管理页面添加相关品牌信息！", false);
						 }
					}
				}
			},
		});
	};
	
	/**
	 * 生产企业：初始化qs号下拉框
	 * @author ZhangHui 2015/5/26
	 */
	portal.initialQsComp = function(){
		$("#qs_no").bind("change",portal.qsNoChange);
		
		$("#qs_no").kendoComboBox({
	        dataTextField: "qsno",
	        dataValueField: "qsId",
	        dataSource: [],
	        filter: "contains",
	        minLength: 0,
	        index:0,
	    });
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/qsno/getListCanUseByBusId",
			type:"GET",
			dataType:"json",
			async:false,
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					$("#qs_no").data("kendoComboBox").setDataSource(returnValue.listOfQs);
					$("#qs_no").data("kendoComboBox").refresh();
				}
			},
		});
	};
	
	/**
	 * 产品编辑时，初始化数据
	 */
	portal.loadDataSource = function() {
		if(portal.edit_id){
			portal.isNew = false;
			$.ajax({
				url : portal.HTTP_PREFIX + "/product/" + portal.edit_id,
				type : 'GET',
				datatype : 'json',
				data:{identify:portal.identify},
				async:false,
				success : function(returnValue) {
					if(returnValue.data != null){
//						var licData = returnValue.licImgList;
//						var qsData = returnValue.qsImgList;
//						var disData = returnValue.disImgList;
//						for ( var i = 0; i < licData.length; i++) {
//							var licId = licData[i].id;
//							var qsId = qsData.length>0?qsData[i].id:-1;
//							var disId = disData.length>0&&disData[i]!=undefined?disData[i].id:-1;
//							portal.setShowMore(1,licId,qsId,disId,licData[i].businessToId);
//						}
//						portal.aryProBusinessImg("licImg",licData);
//						portal.aryProBusinessImg("qsImg",qsData);
//						portal.aryProBusinessImg("disImg",disData);
//						
						var html=''
						for(var i in returnValue.data.productBusinessLicenseList){
							++portal.delDivIndex;
							var _data={};
							$.extend(_data,returnValue.data.productBusinessLicenseList[i]);
							_data.index=portal.delDivIndex;
							html+=baidu.template("license-html",_data);
							if(returnValue.data.productBusinessLicenseList[i].licResource){
								portal.aryProLicImg[i]=returnValue.data.productBusinessLicenseList[i].licResource;
							}
							if(returnValue.data.productBusinessLicenseList[i].qsResource){
								portal.aryProQsImg[i]=returnValue.data.productBusinessLicenseList[i].qsResource;
							}
							if(returnValue.data.productBusinessLicenseList[i].disResource){
								portal.aryProDisImg[i]=returnValue.data.productBusinessLicenseList[i].disResource;
							}
						}
						$("#div_product").html(html);
						portal.setBusinessName();
						portal.writeProductInfo(returnValue.data); // product信息show
						var val=returnValue.data.agricultureProduct?1:0;
						$("input[name='agricultural'][value='"+val+"']").attr("checked",true);
						if(val){
							$("#operative-norm").html("");
						}
						portal.isAgricultural(returnValue.data.agricultureProduct);
						$("#province_id").val(returnValue.data.provinceID);
						$("#province_id").data("kendoComboBox").value(returnValue.data.provinceID);
						var provinceID = returnValue.data.provinceID;
						if(provinceID!=null&&provinceID!=''){
							$.get(portal.HTTP_PREFIX + "/erp/address/findCityByProId/"+returnValue.data.provinceID,function(rs){
								$("#city_id").data("kendoComboBox").dataSource.data(rs.cities);
								$("#city_id").data("kendoComboBox").value(returnValue.data.cityID);
							},'json');
						}
						var cityID = returnValue.data.cityID;
						if(cityID!=null&&cityID!=''){
							$.get(portal.HTTP_PREFIX + "/erp/address/findAreaByCityId/"+returnValue.data.cityID,function(rs){
								$("#area_id").data("kendoComboBox").dataSource.data(rs.areas);
								$("#area_id").data("kendoComboBox").value(returnValue.data.areaID);
							},'json');
						}
						/* 为营养报告grid上的条件赋值 */
						portal.initNutriPer(returnValue.data.listOfNutrition);
						portal.initProductNutri(returnValue.data.listOfNutrition); // 营养报告列表show
						portal.initialCertGrid(returnValue.data.listOfCertification);
						portal.setProAttachments(returnValue.data.proAttachments);
						portal.oldBarcode = returnValue.data.barcode;
						if(portal.oldBarcode.indexOf("69N")!=-1){
							$("#barcode").attr("readonly",true);
						}
						//生产企业进入时，才执行该功能
						if(portal.current_bussiness_type=='02'){
							portal.initProductUrl(returnValue.data.proUrlList); // 推荐url列表--show
						}
						/* 当为供应商且是进口产品时设置进口食品信息 */
						if(portal.current_bussiness_type == "0101"){
							// 0101 代表 流通企业.供应商
							$("#netContent").val(returnValue.data.netContent);//设置净含量  只有供应商有净含量
							var product_type = portal.getProductType();
							if(product_type==2 && returnValue.data.importedProduct!=null){
								//设置之前先清空
								portal.clearImportedProductInfo(); //清空进口食品信息
								portal.setImportedProductInfo(returnValue.data.importedProduct);//设置进口食品信息
							}
						}
					}
				}
			});
			/**
	         * 发布按钮初始化
	         * @author ZhangHui 2015/5/7
	         */
	        $("#save").hide();
	        $("#update").bind("click", portal.save);
	        $("#update").css("display", "block");
		}else{
			 $("#btn_clearProFiles").hide();
	         $("#logListView").hide();
	         $("#btn_clearlabFiles").hide();
	         $("#labListView").hide();
		}
		/* 兼容报告录入界面跳转到本页面后，产品条形码赋值 */
		if(portal._barcode){
			$("#barcode").val(portal._barcode);
		}
	};
	
	/**
	 * 初始化营养报告
	 */
	portal.initProductNutri = function (nutrDS) {
		nutrDS = (nutrDS == null ? [] : nutrDS);
		var nutriGrid = $("#product-nutri-grid").data("kendoGrid");
		if(nutriGrid == null) {
			$("#product-nutri-grid").kendoGrid({
				dataSource:new kendo.data.DataSource({data: nutrDS, page: 1,pageSize: 1000 }),
	    		navigatable: true,
	    		editable: true,
	    		toolbar: [
	     		          {template: kendo.template($("#toolbar_template_nutri").html())}
	     		          ],
	            columns: [
	                {field: "id", title: "id", editable: false, width:1},
	                {field: "name", title: "项目", editor: portal.nutriNameDropDownEditor},
	                {field: "value", title: "值",editor:portal.autoNutriValue},
	                {field: "unit", title:"单位",editor:portal.autoNutriUnit},
	                {field: "nrv", title: "NRV(%)",editor:portal.autoNutriNrv},
	                { command: [{name:"review",
	               	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"), 
	               	    click:function(e){
	               	    	e.preventDefault();
	               	    	var NutriGrid = $("#product-nutri-grid").data("kendoGrid");
	               	    	var delItem = NutriGrid.dataItem($(e.currentTarget).closest("tr"));
	               	    	NutriGrid.dataSource.remove(delItem);
	               }}], title: fsn.l("Operation"), width: 100 }],
	        });
		} else {
			nutriGrid.setDataSource(new kendo.data.DataSource({data: nutrDS, page: 1,pageSize: 1000 }));
		}
	};
	/**
	 * 推荐url
	 */
	portal.initProductUrl = function (urlDS) {
		urlDS = (urlDS == null ? [] : urlDS);
		var urlGrid = $("#product-url-grid").data("kendoGrid");
		if(urlGrid == null) {
			$("#product-url-grid").kendoGrid({
				dataSource:new kendo.data.DataSource({data: urlDS, page: 1,pageSize: 3 }),
				navigatable: true,
				editable: true,
				toolbar: [
				          {template: kendo.template($("#toolbar_template_url").html())}
				          ],
				          columns: [
				                    {field: "id", title: "id", hidden:true},
				                    {field: "urlName", title: "网址名称",width:350},
				                    {field: "proUrl", title: "地址(格式：'http：//或https://'其它)"},
				                    {field: "status", hidden:true},
				                    {field: "identify", hidden:true},
				                    { command: [{name:"review",
				                    	text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"), 
				                    	click:function(e){
				                    		e.preventDefault();
				                    		var urlGrid = $("#product-url-grid").data("kendoGrid");
				                    		var delItem = urlGrid.dataItem($(e.currentTarget).closest("tr"));
				                    		if(delItem.id!=undefined&&delItem.id!=null&&delItem.id!=''){
				                    		   portal.removeUrl(delItem.id);
				                    		}
				                    		urlGrid.dataSource.remove(delItem);
				                    	}}], title: fsn.l("Operation"), width: 100 }],
			});
		} else {
			urlGrid.setDataSource(new kendo.data.DataSource({data: urlDS, page: 1,pageSize: 3 }));
		}
	};
	
	/**
	 * 删除自己的推荐的url
	 * @param id
	 */
	portal.removeUrl = function(id){
		$.ajax({
            url: portal.HTTP_PREFIX + "/product/delUrl/"+id,
            type : "GET",
			datatype : "json",
			contentType: "application/json; charset=utf-8",
            success: function(data) {
				if(data.success == true){
					    lims.initNotificationMes("删除成功！", true);
					}else{
						lims.initNotificationMes("删除失败！", false);	
					}
				}
        });
	};
	portal.getBarcode = function(){
		var timestamp = parseInt(Date.parse(new Date()))/1000;
		return timestamp;
	};
	
	/**
	 * 提交前验证必填项
	 */
	portal.validateProduct = function () {	
		//验证品牌
		var barcode = $("#barcode").val().replace("N",3);
		if($("#businessBrand").data("kendoComboBox")){
			/* 生产企业 */
			var businessBrand = $("#businessBrand").data("kendoComboBox").dataSource.data();
			if(businessBrand.length==0){
				lims.initNotificationMes("品牌为空，请先到品牌管理页面添加相关品牌信息！", false);
				return false;
			}
			var brandVal = $("#businessBrand").val().trim();//如果用户选择了商品，该值为商标的id
			var text = $("#businessBrand").data("kendoComboBox").text().trim();//始终为用户输入的值
			if(brandVal.length<1 || brandVal == text){
				lims.initNotificationMes("您没有选择商品所属品牌，或者您输入的品牌不存在！", false);
				return false;
			}
		}else{
			/* 流通企业 */
			var brandVal = $("#businessBrand").val().trim();
			if(brandVal.length<1){
				lims.initNotificationMes("请填写商品的所属品牌！", false);
				return false;
			}
			var categoryName = $("#categoryGroupName").val();
   		 	if(categoryName == ""){
   		 		lims.initNotificationMes("请选择品牌类型！", false);
   		 		return false;
   		 	}
			var busName = $("#businessName").val().trim();
			var customerMultiSelectDS =  $("#customerSelectInfo").data("kendoMultiSelect");
			if(customerMultiSelectDS){
				var customerItems = customerMultiSelectDS.dataItems();
            	if (customerItems.length < 1) {
					lims.initNotificationMes("请选择销往客户", false);
					return;
				}
            }
			if($("input[name='agricultural']:checked").val()!=1){
				var _barcode = parseInt(barcode.substring(0,3));
				_break=false;
				if($(".delDiv").size()==0){
					lims.initNotificationMes("必须添加生产企业", false);
					return false;
				}
				//如果是进口食品
				if(!isNaN(_barcode)&&(_barcode<690||_barcode>699)){
					$(".delDiv").each(function(i){
						var index=parseInt($(this).attr("index"));
						if($.isEmptyObject(portal.aryProLicImg[index])&&$.isEmptyObject(portal.aryProQsImg[index])){
							lims.initNotificationMes("生产企业必须上传营业执照或生产许可证", false);
							_break=true; 
							return false;
						}
						if($(".delDiv").eq(index).find("input[name='businessName']").val()==''){
							lims.initNotificationMes("生产企业名称必须填写", false);
							$(".delDiv").eq(index).find("input[name='businessName']").focus();
							_break=true;
							return false;
						}
					});
				}else{
					$(".delDiv").each(function(i){
						var index=parseInt($(this).attr("index"));
						if($.isEmptyObject(portal.aryProLicImg[index])){
							lims.initNotificationMes("生产企业必须上传营业执照", false);
							_break=true; 
							return false;
						}
						if($.isEmptyObject(portal.aryProQsImg[index])){
							lims.initNotificationMes("生产企业必须上传生产许可", false);
							_break=true;
							return false;
						}
						if($(".delDiv").eq(index).find("input[name='businessName']").val()==''){
							lims.initNotificationMes("生产企业名称必须填写", false);
							$(".delDiv").eq(index).find("input[name='businessName']").focus();
							_break=true;
							return false;
						}
					});
				}
				if(_break){
					return false;
				}
			}
		}
		
		// 检测用户输入，如果不合法直接提示并中断提交操作
		var productName = $("#name").val();
		if (portal.isBlank(productName)) {
			$("#name").focus();
			lims.initNotificationMes("请填写产品名称！", false);
			return false;
		}
		if (portal.isBlank(barcode)) {
			$("#barcode").focus();
			lims.initNotificationMes("请填写产品的条形码！", false);
			return false;
		}else{
//			 var barcodeRel = /^[0-9]*$/;
//			 if(!barcode.match(barcodeRel)){
//				 lims.initNotificationMes(fsn.l("Product barcode only by numbers!"), false);
//	    		 return false;
//			 }
			 if(portal.barcodeIsExists){
				 lims.initNotificationMes("条形码为" + barcode + "的产品已经存在，请重新填写！", false);
				 return false;
			 }
		}
		
		var proUnit=$("#productUnit").val().trim();
		if(portal.isBlank(proUnit)){
			$("#productUnit").focus();
			lims.initNotificationMes("请填写产品的单位！", false);
			return false;
		}
		
		var brandName = $("#businessBrand").val();
		if (portal.isBlank(brandName)) {
			lims.initNotificationMes("请填写产品所属品牌！", false);
			return null;
		}
		
		var indexCategory1 =$("#category1").data("kendoComboBox").select();
		if (indexCategory1==-1) {
			$("#category1").focus();
			lims.initNotificationMes("请选择产品所属的食品种类一级分类！", false);
			return false;
		}
		var indexCategory2 =$("#category2").data("kendoComboBox").select();
		if (indexCategory2==-1) {
			$("#category2").focus();
			lims.initNotificationMes("请选择产品所属的食品种类二级分类！", false);
			return false;
		}
		var category3 =$("#category3").data("kendoComboBox").text().trim();
		if (portal.isBlank(category3) || category3 == "请选择...") {
			$("#category3").focus();
			lims.initNotificationMes("请选择产品所属的食品种类三级分类！", false);
			return false;
		}
		var isAgricultureProduct=$("input[name='agricultural']:checked").val()==1?true:false;
		//验证农产品原产地是否有填写
		var province_id=$("#province_id").data("kendoComboBox").value();
		var city_id=$("#city_id").data("kendoComboBox").value();
		var area_id=$("#area_id").data("kendoComboBox").value();
		if(isAgricultureProduct){
			if(province_id==""){
				$("#province_id").data("kendoComboBox").focus();
				lims.initNotificationMes("请选择省份！", false);
				return false;
			}
			if(city_id==""){
				$("#city_id").data("kendoComboBox").focus();
				lims.initNotificationMes("请选择城市！", false);
				return false;
			}
			if(area_id==""){
				$("#area_id").data("kendoComboBox").focus();
				lims.initNotificationMes("请选择区/县！", false);
				return false;
			}
		}
		var regularity= $("#regularity").val().trim();
		if (regularity==""&&isAgricultureProduct==false) {
			$("#regularity").focus();
			lims.initNotificationMes("请填写执行标准！", false);
			return false;
		}
		
		//保质期验证
		var expirationDate = $("#expirationDate-num").val();
		var code =$("#category2").data("kendoComboBox").dataItem().code;	
		if (portal.isBlank(expirationDate)) {
			//若产品分类为酒类(酒类code以15开头)，则保质期可为空
			if (code.substr(0, 2) != "15") {
				$("#expirationDate-num").focus();
				lims.initNotificationMes("请填写产品保质期！", false);
				return false;
			}
		} else{
			//若保质期不为空
			 if (!/^\d+$/.test(expirationDate)) {
			 	$("#expirationDate-num").focus();
			 	lims.initNotificationMes("保质期必须是数字！", false);
			 	return false;
			 }else if (!portal.validateExpiration()){
           		 return false;
       		 }
		}
		
		/** 
		 * 进口产品数据格式验证
		 * 企业类型等于0101 代表流通企业.供应商
		 * 产品类型等于2    代表进口产品
		 * @author LongXianZhen 2015/5/22
		 * 最后更新者: ZhangHui 2015/6/3
		 * 更新内容：将企业类型和产品类型编码化
		 */
		if(portal.current_bussiness_type=="0101"){
			var product_type = portal.getProductType();
			if(product_type==2){
				if($("#countryOfOrigin").val().trim()==""){
					lims.initNotificationMes("请选择原产地！", false);
					return false;
				}
				if($("#agentName").val().trim()==""){
					lims.initNotificationMes("请填写国内代理商！", false);
					return false;
				}
				var yesLab=document.getElementById("yesLab").checked;
				if(yesLab){
					if(portal.aryLabAttachments.length==0){
						lims.initNotificationMes("请上传中文标签图片！", false);
						return false;
					}
				}
			}
		}
		
		/** 
         * 验证营养报告的条件值 
         * @author tangxin
         */
		var nutriPer = $("#nutriPer").val().trim();
		var nutriItems =  $("#product-nutri-grid").data("kendoGrid").dataSource.data();
		var count = 0;
		if(nutriItems !=null && nutriItems.length > 0){
			for(var i=0;i<nutriItems.length;i++){
				if(nutriItems[i].name != "") count++;
			}
		}
		if(nutriPer == "" && count > 0){
			lims.initNotificationMes(fsn.l("Please enter the nutritional condition report")+"!", false);
			return false;
		}
		
		return true;
	};
	
	/**
	 * 提交时，构建产品对象
	 */
	portal.createProduct = function(){
		var aryProLicImg = portal.aryProLicImg;
		var aryProQsImg = portal.aryProQsImg;
		var aryProDisImg = portal.aryProDisImg;
		var inpBusList =  $("#div_product input[type=text]");
		var busName = $("#businessName").val().trim();
		var _break=false;
		var productBusinessLicenseList=[];
		$(".delDiv").each(function(i){
			productBusinessLicenseList[i]={};
			var index=parseInt($(this).attr("index"));
			if(parseInt($(this).attr("id"))!=0){
				productBusinessLicenseList[i].id=parseInt($(this).attr("id"));
			}
			productBusinessLicenseList[i].licResource=aryProLicImg[index];
			productBusinessLicenseList[i].qsResource=aryProQsImg[index];
			productBusinessLicenseList[i].disResource=aryProDisImg[index];
			productBusinessLicenseList[i].businessName=$(".delDiv[index='"+index+"']").find("input[name='businessName']").val();
		});
		if(_break){
			return false;
		}
		/*
		for ( var i = 0; i < inpBusList.length; i++) {
			var v = 1 + i;
			var businessName = $("#businessName"+v).val();
			busName = businessName;
			if(businessName == undefined || businessName == ''){
				lims.initNotificationMes(fsn.l("请为新增生产企业填写企业名称")+"!", false);
				return;
			};
			//营业执照必填
			if(aryProLicImg[i]==undefined){
					lims.initNotificationMes(fsn.l("请为新增生产企业上传'营业执照'")+"!", false);
					return;
			 };

			//生产许可证必填
			if(aryProQsImg[i]==undefined){
				lims.initNotificationMes(fsn.l("请为新增生产企业上传'生产许可证'")+"!", false);
				return;
			};
			aryProLicImg[i].businessName = $("#businessName"+v).val();
			aryProQsImg[i].businessName = $("#businessName"+v).val();
			
			if(aryProDisImg.length>0&&aryProDisImg[i]!=undefined){
				aryProDisImg[i].businessName = $("#businessName"+v).val();
			};
		}
		*/
		var urlGrid = [];
		if(portal.current_bussiness_type=='02'){
		  urlGrid = $("#product-url-grid").data("kendoGrid").dataSource.data();
		}
//		return 
		/**
		 * 通过对比当前登录企业名称和产品对应的企业名称，来判断当前登录企业是否为该产品的生产企业
		 * @author ZhangHui 2015/5/8
		 */
		var isLocal = false;
		//如果是流通企业
		if(!$("#businessBrand").data("kendoComboBox")){
			busName = productBusinessLicenseList.length>0?productBusinessLicenseList[0].businessName:'菜农';
		}
		if(busName == portal.currentBusiness.name.trim()){
			isLocal = true;
		}
		
		// 产品类型
		var index = $("#category3").data("kendoComboBox").select();
		var category = {};
		if(index==-1){
			category = {
    			name: $("#category3").val(),
				category:{
					id:$("#category2").data("kendoComboBox").value()
				}
    		};
		}else{
			category = {
    			id: $("#category3").data("kendoComboBox").value().trim(),
    		};
		}
		
		// 品牌
		var businessBrand = {
    			name:($("#businessBrand").data("kendoComboBox")==null?
    					$("#businessBrand").val():$("#businessBrand").data("kendoComboBox").text().trim()),
    	};
		
		/**
		 * 当前登录企业类型为生产企业时，才需要封装品牌id
		 * 			02 代表 企业类型为生产企业
		 */ 
		if(portal.current_bussiness_type == "02"){
			businessBrand.id = $("#businessBrand").data("kendoComboBox").value();
		}
	
		// 生产企业
		var producer = {
				name: busName
		};
		
		/**
		 * 获取当前产品的产品类型
		 * @author ZhangHui 2015/6/3
		 */
		var product_type = portal.getProductType();
		
		var province_id=$("#province_id").data("kendoComboBox").value();
		if(province_id==""){
			province_id=0;
		}
		var city_id=$("#city_id").data("kendoComboBox").value();
		if(city_id==""){
			city_id=0;
		}
		var area_id=$("#area_id").data("kendoComboBox").value();
		if(area_id==""){
			area_id=0;
		}
		var product = {
				id: $("#id").val().trim(), 
				name: $("#name").val().trim(),  // 产品名称
				otherName: $("#otherName").val().trim(), // 产品别名
				barcode: $("#barcode").val().trim(),  // 产品barcode
				format: $("#format").val().trim(),  // 规格
				regularity:$("#regularity").val().trim()==""?null:lims.convertRegularityToItem($("#regularity").val().trim()), // 国家标准 
				status: $("#status").val().trim(), // 状态
				characteristic: $("#characteristic").val().trim(),  // 特色
				cstm: $("#cstm").val().trim(),  // 适宜人群
				businessBrand: businessBrand,  // 商标
				category: category,  // 产品种类
				ingredient: $("#ingredient").val().trim(),  // 配料
				des: $("#des").val().trim(), // 产品描述
				note: $("#note").val().trim(), // 备注
				proAttachments: portal.aryProAttachments,// 产品图片
				listOfCertification: fsn.getCerts(),  // 认证信息
				listOfNutrition:portal.getProductNutris(), //营业标签
                expirationDate: portal.countExpirday(),//保质天数
                expiration:  portal.getExpirDay(),//保质期
                unit: $("#productUnit").val().trim(),//单位
                local: isLocal,
				packageFlag:'0',
				productType: product_type,  // 产品类型                    author ZhangHui 2015-6-3
				producer: producer,         // 产品对应的生产企业  author ZhangHui 2015-6-3
				provinceID:province_id,
				cityID:city_id,
				areaID:area_id,
				agricultureProduct:$("input[name='agricultural']:checked").val()==1?true:false,
				proUrlList:urlGrid,//推荐的url
				productBusinessLicenseList:productBusinessLicenseList,
    	};
		
		var qsCombox = $("#qs_no").data("kendoComboBox");
		if(qsCombox){
			var qs_info = {
					qsid: qsCombox.value(),
			};
			
			product.qs_info = qs_info;
		}
		
		/** 
		 * 当为供应商且为进口产品时创建进口食品信息
		 * 企业类型等于0101 代表 流通企业.供应商
		 * 产品类型等于1    代表 进口产品
		 * @author LongXianZhen 2015/5/22
		 * 最后更新者：ZhangHui 2015/6/3
		 * 最后更新内容：将企业类型和产品类型编码化
		 */
		if(portal.current_bussiness_type=="0101" && product_type==2){
			var importedProduct ={
					id:$("#imported_product_id").val().trim(), 
					country:{   								//原产地
						id:$("#countryOfOrigin").data("kendoComboBox").value(),
						name:$("#countryOfOrigin").data("kendoComboBox").text()
					},
					labelAttachments: portal.aryLabAttachments,  //中文标签图片
					importedProductAgents:{
						agentName:$("#agentName").val().trim(), // 国内代理商名称
						agentAddress:$("#agentAddress").val().trim(), // 国内代理商地址
					}
			};
			product.importedProduct = importedProduct;
		}else{
			product.importedProduct = null;
		}
		var netContent = $("#netContent").val();
		if(netContent!=undefined&&netContent!=null){
			product.netContent = netContent.trim();  // 净含量
		}
		
		/** 
		 * 销往企业ids 
		 * 0101 代表 流通企业.供应商 
		 */
		var customerIds = "";
		if(portal.current_bussiness_type=="0101"){
			var customerItems = $("#customerSelectInfo").data("kendoMultiSelect").dataItems();
			for ( var entry in customerItems) {
				customerIds += (customerItems[entry].id + ",");
			}
		}
		product.selectedCustomerIds = customerIds;
		
		// 返回
		return product;
	};
	
	/**
	 * save 数据保存
	 */
	portal.save = function(){
		if(!portal.validateProduct()){
			return;
		};
		if(portal.aryProAttachments.length==0){
			$("#saveWindow").data("kendoWindow").open().center();
		}else{
			portal.upload();
		}
	};
	
	/**
	 * url合法化验证
	 * @param str_url
	 * @returns
	 */
	portal.isURL = function(str_url) {// 验证url
		var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
//		+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
		+ "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
		+ "|" // 允许IP和DOMAIN（域名）
		+ "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
		+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
		+ "[a-z]{2,6})" // first level domain- .com or .museum
		+ "(:[0-9]{1,4})?" // 端口- :80
		+ "((/?)|" // a slash isn't required if there is no file name
		+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		var re = new RegExp(strRegex);
		return re.test(str_url);
		};
	
	/**
	 * save 提交验证通过的数据
	 */
	portal.upload = function() {
		var save_product = portal.createProduct();
		if(save_product == undefined){
			return;
		} 
		var dataUrl = save_product.proUrlList;
		if(dataUrl == undefined || dataUrl == null){
			lims.initNotificationMes("您还没有添加推荐的网址！", false);
		}else{
			for(var k=0;k<dataUrl.length;k++){
				if(!portal.isURL(dataUrl[k].proUrl)){
					lims.initNotificationMes("您推荐的网址:《"+dataUrl[k].proUrl+"》格式不正确！", false);
					return;
				}
			}
		}
		if(portal.isNew){
			$("#winMsg").html("正在保存数据，请稍候....");
		}else{
			$("#winMsg").html("正在执行更新操作，请稍候....");
		}
		
		$("#k_window").data("kendoWindow").open().center();
		$.ajax({
				url : portal.HTTP_PREFIX + "/product/" + portal.currentBusiness.name + "/" + portal.isNew,
				type : "POST",
				datatype : "json",
				contentType: "application/json; charset=utf-8",
				data : JSON.stringify(save_product),
				success : function(returnValue) {
					$("#k_window").data("kendoWindow").close();
					if (returnValue.result.status == "true") {
						lims.initNotificationMes(portal.isNew ? '保存成功！' : '更新成功！', true);
//						var licData = returnValue.licImgList;
//						var qsData = returnValue.qsImgList;
//						var disData = returnValue.disImgList;
//						for ( var i = 0; i < licData.length; i++) {
//							var licId = licData[i].id;
//							var qsId = qsData.length>0?qsData[i].id:null;
//							var disId = disData.length>0?disData[i].id:null;
//							portal.setShowMore(1,licId,qsId,disId,licData[i].businessToId);
//						}
//						portal.aryProBusinesSIMG("LICIMG",LICDATA);
//						PORTAL.ARYPROBUSINESSIMG("QSIMG",QSDATA);
//						PORTAL.ARYPROBUSINESSIMG("disImg",disData);
						if (portal.isNew) {
							$("#id").val(returnValue.data.id);
							portal.isNew = false;
						}
						portal.oldBarcode = returnValue.data.barcode;
						portal.initialCertGrid(returnValue.data.listOfCertification); // 认证信息列表
						portal.initProductNutri(returnValue.data.listOfNutrition); // 营养报告列表show
						portal.setProAttachments(returnValue.data.proAttachments); // 产品图片列表
						var html=''
							for(var i in returnValue.data.productBusinessLicenseList){
								++portal.delDivIndex;
								var _data={};
								$.extend(_data,returnValue.data.productBusinessLicenseList[i]);
								_data.index=portal.delDivIndex;
								html+=baidu.template("license-html",_data);
								if(returnValue.data.productBusinessLicenseList[i].licResource){
									portal.aryProLicImg[i]=returnValue.data.productBusinessLicenseList[i].licResource;
								}
								if(returnValue.data.productBusinessLicenseList[i].qsResource){
									portal.aryProQsImg[i]=returnValue.data.productBusinessLicenseList[i].qsResource;
								}
								if(returnValue.data.productBusinessLicenseList[i].disResource){
									portal.aryProDisImg[i]=returnValue.data.productBusinessLicenseList[i].disResource;
								}
							}
							$("#div_product").html(html);
							portal.setBusinessName();
						/* 当为供应商且是进口产品时设置进口食品信息
						 * 0101 代表 流通企业.供应商
						 */
						if(portal.current_bussiness_type=="0101"){
							var product_type = portal.getProductType();
							if(product_type==2 && returnValue.data.importedProduct!=null){
								//设置之前先清空
								portal.clearImportedProductInfo(); //清空进口食品信息
								portal.setImportedProductInfo(returnValue.data.importedProduct);//设置进口食品信息
							}else{
								portal.clearImportedProductInfo(); //清空进口食品信息
							}
						}
						/* 为营养指标字段赋值 */
						$("#nutriLabel").text(returnValue.data.nutriLabel);
						$("ul.k-upload-files").remove();
						
						//二级分类
	                    $.ajax({
	                        url: portal.HTTP_PREFIX + "/testReport/searchLastCategory/" + $("#category2").data("kendoComboBox").value() + "?categoryFlag=" + true,
	                        type: "GET",
	                        dataType: "json",
	                        async: false,
	                        success: function(value) {
								fsn.endTime = new Date();
	                            if (value.result.status == "true") {
	                                $("#category3").data("kendoComboBox").setDataSource("");
	                                $("#category3").data("kendoComboBox").setDataSource(value.data);
	                                if (returnValue.data.category.id) {
	                                    $("#category3").data("kendoComboBox").value(returnValue.data.category.id);
	                                }
	                            }
	                        }
	                    });
	                    var str = $("#div_pro").val();
	                    if(str!=undefined){
	                    	/* md5加密pid */ 
	                    	var canshu = "?"+returnValue.data.id+"&"+$.md5(""+returnValue.data.id);
	                    	window.location.href = "/fsn-core/views/portal_new/product.html"+canshu;
	                    }
					}else{
						lims.initNotificationMes(returnValue.result.errorMessage, false);
					}
				}
		});
	};
	
	/**
	 * 引进产品
	 * @author Zhanghui 2015/4/22
	 */
	portal.leadProductByBarcode = function(){
		var barcode = $("#barcode").val().trim();
		
		/* 供应商在引进产品时，需要选择销往企业 */
		var isDealer = false;
		/**
		 * 0101 代表 流通企业.供应商
		 */
		if(portal.current_bussiness_type=="0101"){
			isDealer = true;
			var customerItems = $("#customerSelect_lead").data("kendoMultiSelect").dataItems();
            if ( customerItems.length<1 ){
                lims.initNotificationMes("请选择销往客户", false);
				return ;
            }
		}
		
        /* 保存产品与当前登录企业的关系 */
        var isSuccess = portal.leadProduct(barcode);
        if(isSuccess){
			if(isDealer){
				$("#lead_dealer_window").data("kendoWindow").close();
			}else{
				$("#lead_window").data("kendoWindow").close();
			}
			lims.initNotificationMes("条形码为：" + barcode + "的产品引进成功", true);
			$("#barcode").val("");
        }else{
        	lims.initNotificationMes("条形码为：" + barcode + "的产品引进失败", false);
        }
		
        /**
         * 0101 代表流通企业.供应商
         */
		if (portal.current_bussiness_type=="0101") {
			/* 保存 产品-当前登录企业-销往企业 的关系 */
			var customerItems = $("#customerSelect_lead").data("kendoMultiSelect").dataItems();
			var customerIds = "";
			for ( var entry in customerItems) {
				customerIds += (customerItems[entry].id + ",");
			}
			var productVO = {
					barcode: barcode,
					selectedCustomerIds: customerIds,
			};
			portal.saveSelectCustomer(productVO);
		}
	};
	
	/**
	 * 营养报告名称下拉选择方法
	 * @author tangxin
	 */
	portal.nutriNameDropDownEditor = function(container, options){
		$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
		.appendTo(container)
		.kendoComboBox({
			dataTextField: "name",
	        dataValueField: "id",
	        filter: "contains",
            suggest: true,
            change: function(e) {
                var value = this.value();
                if(value!=""){
                	var data=this.dataItem();
                    if(!data){
                    	this.text("");
                    	lims.initNotificationMes("请选择下拉框内的营养元素！", false);
                    }
                }
              },
			dataSource: {
				transport: {
					read: portal.HTTP_PREFIX + "/product/getStandNutris"
				},
				schema: {
					data : function(returnValue) {
						return portal.removeRepeatNutri(returnValue.data); //响应到页面的数据
		            }
				}
			}
	        
		});
	};

	/**
	 * 封装营养报告
	 */
	portal.getProductNutris = function(){
    	 var productNutris=[];
    	 var nutris=$("#product-nutri-grid").data("kendoGrid").dataSource.data();
    	 var nutriPer ="每" + $("#nutriPer").val().trim() + $("#nutriUnitDropDown").data("kendoDropDownList").text();
    	 var j=0;
    	 for(var i=0;i<nutris.length;i++){
    		 if(nutris[i].name==""){
    			 j++;
    			 continue;
    		 }
    		 productNutris[i-j]={
    				 id: nutris[i].id==null?"":nutris[i].id,
    				 name: nutris[i].name,
    				 value: nutris[i].value,
    				 unit: nutris[i].unit,
    				 per: nutriPer,
    				 nrv: nutris[i].nrv,
    		 };
    	 }
    	 return productNutris;
    };
    
    
    /**
     * 销往客户弹出框关闭操作
     * @author ZhangHui 2015/4/28
     */
    portal.closeCustomerWindow = function(){
    	var customerItems = $("#customerSelectInfo").data("kendoMultiSelect").dataItems();
    	if(customerItems.length <1){
			 fsn.initNotificationMes("请选择销往客户！", false);
			 return;
		}
    	portal.setCustomerSelectValue(customerItems);
		$("#customer_select_window").data("kendoWindow").close();
    };
    
    //=================================================开始=====================================================
   
	portal.setShowMore = function(){
		++portal.delDivIndex;
	  $("#div_product").append(baidu.template("license-html",{"businessName":'',"index":portal.delDivIndex,"id":0}));
	  portal.setBusinessName();
	};
	
	portal.setBusinessName = function(){
		/* 初始化产品图片上传控件 */
//		upload.buildUpload("upload_licImg_files"+v, portal.aryProLicImg, "proFileMsg", "IMG");
//		upload.buildUpload("upload_qsImg_files"+v, portal.aryProQsImg, "proFileMsg", "IMG");
//		upload.buildUpload("upload_disImg_files"+v, portal.aryProDisImg, "proFileMsg", "IMG");
		
		$(".upload_licImg_files").each(function(){
			var index=parseInt($(this).parents(".delDiv").attr("index"));
			if(!$(this).data("kendoUpload")){
				if(portal.aryProLicImg[index]==null){
					portal.aryProLicImg[index]={};
				}
				portal.buildUpload(this, portal.aryProLicImg, "IMG","上传'营业执照'或<br>'统一信用代码",index);
				if($(this).parents(".delDiv").find(".licImgListView").size()!=0){
					$(this).data("kendoUpload").disable();
				}
			}
		});
		$(".upload_qsImg_files").each(function(){
			var index=parseInt($(this).parents(".delDiv").attr("index"));
			if(!$(this).data("kendoUpload")){
				if(portal.aryProQsImg[index]==null){
					portal.aryProQsImg[index]={};
				}
				portal.buildUpload(this, portal.aryProQsImg, "IMG","上传'生产许可证'",index);
				if($(this).parents(".delDiv").find(".qsImgListView").size()!=0){
					$(this).data("kendoUpload").disable();
				}
			}
		});
		$(".upload_disImg_files").each(function(){
			var index=parseInt($(this).parents(".delDiv").attr("index"));
			
			if(!$(this).data("kendoUpload")){
				if(portal.aryProDisImg[index]==null){
					portal.aryProDisImg[index]={};
				}
				portal.buildUpload(this, portal.aryProDisImg, "IMG","上传'食品流通许可证'或<br>'食品经营许可证'",index);
				if($(this).parents(".delDiv").find(".disImgListView").size()!=0){
					$(this).data("kendoUpload").disable();
				}
			}
		});
		$(".businessName").kendoAutoComplete({
				dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
				filter: "contains",
				placeholder: "搜索...",
				width:"223px",
				select: portal.onSelectBusName,
		});
	};
	portal.buildUpload=function(id, attachments,flag,selectLocal,index){
			$(id).kendoUpload({
	       	 async: {
	                saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
	                removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
	                autoUpload: true,
	                removeVerb:"POST",
	                removeField:"fileNames",
	                saveField:"attachments",
	       	 },localization: {
	                select:selectLocal,//id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
	                retry:lims.l("retry",'upload'),
	                remove:lims.l("remove",'upload'),
	                cancel:lims.l("cancel",'upload'),
	                dropFilesHere: lims.l("drop files here to upload",'upload'),
	                statusFailed: lims.l("failed",'upload'),
	                statusUploaded: lims.l("uploaded",'upload'),
	                statusUploading: lims.l("uploading",'upload'),
	                uploadSelectedFiles: lims.l("Upload files",'upload'),
	            },
	            multiple:false,
	            upload: function(e){
	                var files = e.files;
	                 $.each(files, function () {
	                     var extension = this.extension.toLowerCase();
	                     if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
	                     lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
	                     e.preventDefault();
	                 }
	               });
	            },
	            success: function(e){
			           	 if(e.response.typeEror){
			           		lims.initNotificationMes("文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！", false);
			           		 return;
			           	 }
			           	 if(e.response.morSize){
			           		lims.initNotificationMes("您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!", false);
			           		 return;
			           	 }
			             if (e.operation == "upload") {
			            	 attachments[index]=e.response.results[0];
			                 lims.initNotificationMes("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )", true);
			            }else if(e.operation == "remove"){
			            	attachments[index]={};
			             }
	            },
	            remove:function(e){
	            	lims.initNotificationMes("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )", true);
	            },
	            error:function(e){
	            	lims.initNotificationMes(fsn.l("An exception occurred while uploading the file! Please try again later ..."),false);
	            },
	      });
	};
	portal.setDownBusinessName = function(e){
		$("#pro_sta").val(e.id);
	};
	
	portal.removeSingleImg = function(id,strImg){
		$.ajax({
            url: portal.HTTP_PREFIX + "/product/delImg/"+strImg+"/"+id,
            type: "DELETE",
            dataType: "json",
            async: false,
            success: function(value) {
            	if(value.status == "true"){
            		lims.initNotificationMes("图片删除成功！", true);
            	}
            }
        });
	};
	//删除当前该上传企业
//	portal.delBusiness = function(n,businessId){
//		$("#delDiv"+n).hide();
//       if(businessId == undefined||businessId ==-1){
//    	   $("#delDiv"+n).remove();
//    	   return;
//       }
//		$.ajax({
//            url: portal.HTTP_PREFIX + "/product/delBusiness/"+businessId,
//            type: "DELETE",
//            dataType: "json",
//            async: false,
//            success: function(value) {
//            	if(value.status == "true"){
//            		lims.initNotificationMes("该生产企业删除成功！", true);
//            		portal.removeBusinessIdImg(businessId);
//            		$("#delDiv"+n).remove();
//            		}
//            	}
//        });
//	};
	portal.init();
});