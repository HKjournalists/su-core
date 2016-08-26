$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var product = upload.product = upload.product || {};
	var business_unit = window.fsn.business_unit = window.fsn.business_unit ||{};
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	business_unit.aryQsAttachments = new Array();
	
	product.initialize = function(){
		upload.buildGridByBoolbar("myOwnProduct_grid", product.ownProductColumns, product.ownProductDS, 300, "");
		upload.buildGridByBoolbar("product_grid", product.productColumns, product.productDS, 300, "");
		
        product.initProductNutriGrid("product-nutri-grid");	//show 营养报告Grid
		$("#accommodation_mainAddr").bind("blur",function(){verifyAddress("accommodation_mainAddr");});
		$("#qs_mainAddr").bind("blur",function(){verifyAddress("qs_mainAddr");});
	};
	
	product.productColumns = [
			{field: "id",title: "Id",width: 30},
            {field: "name",title: fsn.l("Product name"),width: 65},
            {field: "businessBrand.name",title:fsn.l("Subordinate to the brand"),width: 60},
            {field: "format",title:fsn.l("Specification"),width: 35},
            {field: "barcode",title: fsn.l("Bar code"),width: 55},
            {field: "cstm",title: fsn.l("Suit crowds"),width: 45},
            {field: "ingredient",title: fsn.l("Feature"),width: 70},
            { command: [{name:"edit",
           	    	text:"<span class='k-edit' ></span>" + fsn.l("Binding qs"), 
           	    	click: function(e){
                        e.preventDefault();
           	    		var editRow = $(e.target).closest("tr");
           	    		var temp = this.dataItem(editRow);
           	    		var gridDS = $("#product_grid").data("kendoGrid").dataSource.data();
           	    		/* 分页请求减1的标志，当绑定完第二页产品的qs号时，应该跳转到第一页加载数据 */
           	    		if(gridDS != null && gridDS.length == 1) {
           	    			product.pageSub_WB = true;
           	    		}
           	    		product.editProductId = temp.id;
           	    		product.clearProLic();
           	    		//初始化生产许可证图片上传控件
           	    		$("#upload_qs_div").html("");
           	    		$("#upload_qs_div").html("<input id='upload_qs_files' type='file' />");
           	    		business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "proFileMsg");
           	    		product.bindQsNo(true,temp);
           	    		}
					},
                    {
            		name:"review",
            		text:"<span class='k-icon k-cancel' ></span>" + fsn.l("Preview"), 
					click: function(e){
                        e.preventDefault();
						var editRow = $(e.target).closest("tr");
						var temp = this.dataItem(editRow);
						product.viewProduct(temp.id);
					}},
            ], title:fsn.l("Operation"), width: 75}];
	
	product.ownProductColumns = [
		{field: "product.id",title: "Id",width: 30},
        {field: "product.name",title: fsn.l("Product name"),width: 60},
        {field: "product.businessBrand.name",title: fsn.l("Subordinate to the brand"),width: 60},
        {field: "product.barcode",title: fsn.l("Bar code"),width: 55},
        {field: "product.cstm",title: fsn.l("Suit crowds"),width: 80},
        {field: "productionLicense.qsNo",title: "qs号",width: 50},
        { command: [{
        		name:"edit",
           	    text:"<span class='k-edit'></span>" + fsn.l("update qs") , 
					  click: function(e){
                          e.preventDefault();
						  var editRow = $(e.target).closest("tr");
						  var temp = this.dataItem(editRow);
						  product.editProductId = temp.product.id;
						  product.clearProLic();
						  //初始化生产许可证图片上传控件
						  $("#upload_qs_div").html("");
						  $("#upload_qs_div").html("<input id='upload_qs_files' type='file' />");
						  business_unit.buildUpload("upload_qs_files", business_unit.aryQsAttachments, "proFileMsg");
						  product.bindQsNo(false,temp);
					  }
				  },
				{name:lims.localized("Delete"),
					  text:"<span class='k-icon k-cancel'></span>" + fsn.l("delete qs"),
				      click: function(e){
                          e.preventDefault();
				          var deleteRow = $(e.target).closest("tr");
				          deleteItem = this.dataItem(deleteRow);
						  //验证是否关联了生成报告
						  $.ajax({
								url:portal.HTTP_PREFIX + "/product/isHasReport/" + deleteItem.product.barcode,
								type: "GET",
								dataType: "json",
								success: function(data){
									if(!data.result){
										 product.editProductId = deleteItem.product.id;
					    		    	 $("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
									}else{
										 product.pageSub_YB = false;
										lims.initNotificationMes("该产品关联了已生成报告，无法解绑！");
									}
									
								}
						  })
				 }}
	            ], title:fsn.l("Operation"), width: 70 }
        ];
                                
  product.productNutriColumns = [
                                {field: "name", title: fsn.l("Item"),width:70},
                                {field: "value", title: fsn.l("Value"),width:50},
                                {field: "unit", title:fsn.l("Unit"),width:50},
                                {field: "per", title: fsn.l("Condition"), width: 100},
                                {field: "nrv", title: fsn.l("Nrv"),width:50}];
	
	product.productDS = new kendo.data.DataSource({
		transport: {
            read: {
            	async:false,
            	url : function(options){
            		var configure = null;
            		if(options.filter){
            			configure = filter.configure(options.filter);
            		}
            		return portal.HTTP_PREFIX + "/product/getProductsOfSon/" + configure + "/" + options.page + "/" + options.pageSize;
            	},
                dataType : "json",
                contentType : "application/json;charset=utf-8",
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data.listOfProduct;  //响应到页面的数据
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	product.detailQsInfo = function(){
		var qs = $("#showQsFormat").html()+$("#qsNoText").val().trim();
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/getLicenseInfoByQs/" + qs,
			type: "POST",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			success: function(data){
				if(data!=null&&data.data!=null){
					console.log(data);
					product.clearProLic();
                    var qsFormat = $("#qsFname").data("kendoDropDownList");
                    qsFormat.value(data.data.qsnoFormat==null?1:data.data.qsnoFormat.id);
                    var item = qsFormat.dataItem();
                    product.formetType =item.formetType;
                    product.formetValue = item.formetValue;
                    product.formetLength = item.formetLength;
                    $("#showQsFormat").html(item.formetValue);
					$("#qsNoText").val(data.data.qsNo.replace(item.formetValue,""));
                    
			 		$("#busunitName").val(data.data.busunitName);
			 		$("#productName").data("kendoDropDownList").value(data.data.productName);
			 		if(data.data.startTime==null){
			 			$("#qsStartTime").val("");
			 		}else{
                        var dateStart = $("#qsStartTime").data("kendoDatePicker");
                         dateStart.value("");
                        dateStart.value(data.data.startTime.substr(0, 10));
			 		}if(data.data.endTime==null){
			 			$("#qsEndTime").val("");
			 		}else{
                        var dateEnd = $("#qsEndTime").data("kendoDatePicker");
                        dateEnd.value("");
                        dateEnd.value(data.data.endTime.substr(0, 10));
			 		}
					business_unit.setAddrValue(data.data.accommodation, data.data.accOtherAddress,
			         		"accommodation_mainAddr","accommodation_streetAddress");
					business_unit.setAddrValue(data.data.productionAddress, data.data.proOtherAddress,
			         		"qs_mainAddr","qs_streetAddress");
			 		$("#checkType").val(data.data.checkType);
			 		$("#addQs").show();
			 		$("#btn_clearQsFiles").hide();
			 		business_unit.setQsAttachments(data.data.qsAttachments);
				}
			}
		})	
	}
	//清空生产许可证窗口里面的内容
	product.clearProLic=function(){
		$("#qsNoText").val("");;
		$("#busunitName").val("");
		$("#productName").val("");
		$("#qsStartTime").val("");
		$("#qsEndTime").val("");
		$("#accommodation_mainAddr").val("");
		$("#accommodation_streetAddress").val("");
		$("#qs_mainAddr").val("");
		$("#qs_streetAddress").val("");
		$("#checkType").val(""); 
		$("#addQs").show();
		$("#btn_clearQsFiles").hide();
		business_unit.aryQsAttachments=[];
		if($("#qsAttachmentsListView").data("kendoListView")){
 			$("#qsAttachmentsListView").data("kendoListView").dataSource.data([]);
 		}
	 };
	 /* 初始化时间控件  */
	$("#qsStartTime,#qsEndTime,#test_time_format").kendoDatePicker({
   	 format: "yyyy-MM-dd",
   	 height:30,
   	 culture : "zh-CN",
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
	product.ownProductDS = new kendo.data.DataSource({
		transport: {
            read: {
            	async:false,
            	url : function(options){
            		var configure = null;
            		if(options.filter){
            			configure = filter.configure(options.filter);
            		}
            		return portal.HTTP_PREFIX + "/product/getMyProducts/" + configure + "/" + options.page + "/" + options.pageSize;
            	},
                dataType : "json",
                contentType : "application/json;charset=utf-8",
            }
        },
        batch : true,
        page:1,
        pageSize: 5,
        schema: {
            data : function(returnValue) {
                return returnValue.data.listOfProduct;
            },
            total : function(returnValue) {       
                return returnValue.data.counts;
            }     
        },
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
    
     //加载产品分类下拉列表的 DataSource
    product.listCategoryDataSource = function(url){
         return new kendo.data.DataSource({
        	transport: {
                read: {
                	type : "GET",
                    dataType : "json",
                    async:true,
                    contentType : "application/json",
                    url: portal.HTTP_PREFIX+url,
                }
        	},
        	schema: {
        		data : function(d) {
        			return d.data;
        		}        
        	}
        });
     }
     
     product.listDataSource = function(formatId){
         formatId = formatId!=null?formatId:1;
         var firstpart = $("#showQsFormat").html();
         return new kendo.data.DataSource({
        	transport: {
                read: {
                	type : "GET",
                    dataType : "json",
                    async:true,
                    contentType : "application/json",
                    url: portal.HTTP_PREFIX+"/product/loadSonqsno/"+firstpart+"?formatId="+formatId,
                }
        	},
        	schema: {
        		data : function(d) {
        			return d.data;
        		}        
        	}
        });
     }
   
     product.onSelectUnitName = function(e){
         var qsNo = this.dataItem(e.item.index());
	     $("#qsNoText").val(qsNo);
         product.detailQsInfo();
     }
     
     //将用户选择的信息填到input框中
     product.onSelectProName = function(e){
         var productName = this.dataItem(e.item.index());
	     $("#productName").val(productName);
     }
     
     formatQsNo = function(obj,value){
        var result = [];
        $("#qsNoText").attr("maxlength",product.formetLength+2);
        var type = product.formetType;
        switch (product.formetLength){
            case 12 : // 格式：QSxx-xxxxx-xxxxx  2-5-5
                for(var i = 0; i < value.length; i++){
                    if(type=="-"){
                        var qs_No = $("#qsNoText").val();
                        lastChar = qs_No.substring(qs_No.length-1);
                        if (i == 2&& $("#qsNoText").val().trim().length==3&&lastChar!="-"){
                            result.push(type + value.charAt(i));
                        }else if(i == 8 && $("#qsNoText").val().trim().length==9&&lastChar!="-"){
                            result.push(type + value.charAt(i));
                        }else{
                            result.push(value.charAt(i));
                        }
                    }else{
                        value = value.replace(/\s*/g, "");
                        if (i % 4 == 0 && i != 0){
                            result.push(type + value.charAt(i));
                        }else{
                            result.push(value.charAt(i));
                         }
                    }
                }
                obj.value = result.join("");
                break;
            case 10 : //格式：XKxx-xxx-xxxxx)
                for(var i = 0; i < value.length; i++){
                    var qs_No = $("#qsNoText").val();
                    lastChar = qs_No.substring(qs_No.length-1);
                    if (i == 2 && $("#qsNoText").val().trim().length == 3&&lastChar!="-"){
                        result.push(type + value.charAt(i));
                    }else if( i == 6 && $("#qsNoText").val().trim().length == 7&&lastChar!="-"){
                        result.push(type + value.charAt(i));
                    }else{
                        result.push(value.charAt(i));
                    }
                }
                obj.value = result.join("");
                break;
            default : ;
        }
    }
     
     product.changeQsFormat = function(e){
        var format = this.dataItem(e.item.index());
        var oldValue = $("#qsFname").data("kendoDropDownList").value();
        if(oldValue!=format.id){
            $("#qsNoText").val("");
        }
        var formatId = format.id;
        $("#showQsFormat").html(format.formetValue);
        product.formetType = format.formetType;
        product.formetValue = format.formetValue;
        product.formetLength =format.formetLength;
        var listQsDs = product.listDataSource(formatId);
        $("#qsNoText").data("kendoAutoComplete").setDataSource([]);
        $("#qsNoText").data("kendoAutoComplete").setDataSource(listQsDs);
     }
     
     product.listCategory= product.listCategoryDataSource("/product/loadProduCtcategory");// 加载产品名称分类
     product.listFormatQs= product.listCategoryDataSource("/product/loadlistFormatqs");// 加载企业qs号绑定标准
     product.listQsDs= product.listDataSource(null);
     
     //初始化绑定qs好的下啦列表
     $("#qsNoText").kendoAutoComplete({
			 dataSource: product.listQsDs!=null?product.listQsDs:[],
	         filter: "startswith",
	         placeholder: "搜索qs号...",
	         select: product.onSelectUnitName,
	     });
     
     //初始化初始化产品名称分类的下啦列表    
     $("#productName").kendoDropDownList({
             dataTextField: "name",
             dataValueField: "name",
             dataSource: product.listCategory!=null?product.listCategory:[],
             optionLabel:"--请选择分类--",
	         select: product.onSelectProName,
	     }); 
         
     //初始化初始化qs格式选择的下啦列表      
     $("#qsFname").kendoDropDownList({
             dataTextField: "formetName",
             dataValueField: "id",
             dataSource: product.listFormatQs!=null?product.listFormatQs:[],
	         select: product.changeQsFormat,
	     }); 
     
    
	product.bindQsNo = function(isNew,temp){
        //设置点击绑定时的默认规则为第一种
        var addFormat = $("#qsFname").data("kendoDropDownList");
        addFormat.select(addFormat.ul.children().eq(0));
        var item = addFormat.dataItem(0);
        product.formetType = item.formetType;
        product.formetLength = item.formetLength;
        product.formetValue = item.formetValue;
        $("#showQsFormat").html(product.formetValue);
        var listQsDs = product.listDataSource(item.id);
        $("#qsNoText").data("kendoAutoComplete").setDataSource([]);
        $("#qsNoText").data("kendoAutoComplete").setDataSource(listQsDs);
		portal.isNew = isNew;
        if(!portal.isNew){
            // 修改绑定qs号时
            var qsFormatData = temp.productionLicense.qsnoFormat;
            addFormat.value(qsFormatData!=null?qsFormatData.id:1);
            var qsItem = addFormat.dataItem();
            product.formetType = qsItem.formetType;
            product.formetLength = qsItem.formetLength;
            product.formetValue = qsItem.formetValue;
            $("#showQsFormat").html(qsItem.formetValue);
            var qs_number = temp.productionLicense.qsNo;
        	$("#qsNoText").val(qs_number.replace(qsItem.formetValue,""));
        	$("#productName").data("kendoDropDownList").value(temp.productionLicense.productName);
            product.detailQsInfo();
            
        }else{$("#qsNoText").val("");$("#productName").val("");}
		if($("#qsNoText").data("kendoAutoComplete")){
            product.listQsDs.read();
            $("#qsNoText").data("kendoAutoComplete").refresh();
        }
		if($("#productName").data("kendoDropDownList")){
            product.listCategory.read();
            $("#productName").data("kendoDropDownList").refresh();
        }
		$("#bindQsNoWindow").data("kendoWindow").open().center();
	};
	
	$("#btn_bindNo").click(function(){
		$("#bindQsNoWindow").data("kendoWindow").close();
	});
	
    /**
     * 查找用户录入的qs号包含在绑定qs号的下啦表中没有
     * @param {Object} qsNo
     */
    product.findQsNoExcuseAutocomplete = function(qsNo){
        var autocomplete = $("#qsNoText").data("kendoAutoComplete");
        var arr = autocomplete.dataSource._data;
        for(var i = 0 ; i < arr.length ; i++){
            if(arr[i] != qsNo){
                continue;
            }else{
                return true;
            }
            return false;
        }
    }
    
    /**
     * 绑定qs号grid的分页处理
     */
    function bindQsGridFun(){
    	var gridDS = product.productDS;
    	var page = gridDS.page();
    	var flag = (gridDS.data().length < 2);
    	if(page > 1 && flag){
    		gridDS.page(--page);
    	} else {
    		product.productDS.read();
    	}
    }
    
	$("#btn_addYes").click(function(){
		var qsNo = $("#qsNoText").val().trim();
		if(!product.validateLicense()){
        	return;
        }
        var firstPart = $("#showQsFormat").html().indexOf("?");
        if (firstPart!=-1) {
            lims.initNotificationMes("您选择的输入规则没有加载出正确的省份简称，请在企业基本信息中完善企业地址保存后再试！", false);
            return;
        }
        var qs_formatId = $("#qsFname").data("kendoDropDownList")._old;
         var qsnoFormat = {
                 id:qs_formatId
             }
		var productionLicense = {
	        	qsNo: $("#showQsFormat").html()+$("#qsNoText").val().trim(),
	        	busunitName: $("#busunitName").val().trim(),
	        	productName: $("#productName").data("kendoDropDownList").text().trim(),
	        	startTime: $("#qsStartTime").val().trim(),
	        	endTime: $("#qsEndTime").val().trim(),
	        	accommodation:$("#accommodation_mainAddr").val().trim().replace(/-/g,"")+$("#accommodation_streetAddress").val().trim(),
				accOtherAddress:$("#accommodation_mainAddr").val().trim()+"--"+$("#accommodation_streetAddress").val().trim(),
				productionAddress:$("#qs_mainAddr").val().trim().replace(/-/g,"")+$("#qs_streetAddress").val().trim(),
				proOtherAddress:$("#qs_mainAddr").val().trim()+"--"+$("#qs_streetAddress").val().trim(),
	        	checkType: $("#checkType").val().trim(),
	        	qsAttachments: business_unit.aryQsAttachments,
                qsnoFormat:qsnoFormat
	      };
         
		$("#bindQsNoWindow").data("kendoWindow").close();
		$("#bind_status").data("kendoWindow").open().center();
		$.ajax({
			url: portal.HTTP_PREFIX + "/product/bindQsNoToProduct/" + product.editProductId + "/" + portal.isNew,
    		type: "POST",
    		dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(productionLicense),
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					product.ownProductDS.read();
					
					$("#bind_status").data("kendoWindow").close();
					lims.initNotificationMes(portal.isNew?fsn.l("Binding success"):fsn.l("Modify the success"), true);
					bindQsGridFun();
				}else if(returnValue.result.offlineFlag){
            		lims.initNotificationMes("用户登录信息已经失效，您需要从新登录！", false);
            		fsn.goToLogin();
            	}else{
					lims.initNotificationMes(portal.isNew?fsn.l("Binding failure"):fsn.l("Modified failure"), false);
				}
			},
		});
	});
	
	product.viewProduct = function(productId){
    	if(!productId){return;}
    	$.ajax({
			url:portal.HTTP_PREFIX + "/product/" + productId,
			type:"GET",
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					product.setProductValue(returnValue.data);
					product.setProAttachments(returnValue.data.proAttachments);	//show 产品图片
					var listOfCertification = returnValue.data.listOfCertification;
                    if(listOfCertification.length > 0){
                        for(var i = 0 ;i < listOfCertification.length;i++){
                            if(listOfCertification[i].certResource==null){
                                listOfCertification[i].certResource ="";
                            }
                        }
                    }
					product.initialCertification(listOfCertification); //show 产品认证信息
					$("#viewWindow").data("kendoWindow").open().center();
				}
			},
		});
        
        // 加载营养报告grid(分页)
        product.initNutriDS(productId);
        $("#product-nutri-grid").data("kendoGrid").setDataSource(product.nutriDS);
    };
	
    product.setProductValue = function(product){
    	if(product==null){return;}
		$("#id").val(product.id);
		$("#name").val(product.name);
		$("#status").val(product.status);
		$("#format").val(product.format);
		$("#category").val(product.category.name);
		$("#barcode").val(product.barcode);
		$("#expiration").val(product.expiration);
		$("#businessBrand").val(product.businessBrand.name);
		$("#regularity").val(lims.convertRegularityToString(product.regularity));
		$("#characteristic").val(product.characteristic);
		$("#note").val(product.note);
		$("#cstm").val(product.cstm);
		$("#des").val(product.des);
		$("#ingredient").val(product.ingredient);
    };
    
    /**
     * Show 产品图片信息
     * @param dataSource
     */
    product.setProAttachments=function(dataSource){
		var mainDiv=$("#proAttachments");
		mainDiv.html("");
		for(var i=0;i<dataSource.length;i++){
			var div=$("<div style='float:left;margin:10px 10px;'></div>");
			var img=$("<img width='50px' src='"+dataSource[i].url+"'></img>");
			var a=$("<a href='"+dataSource[i].url+"' target='_black'></a>");
			a.append(img);
			div.append(a);
			mainDiv.append(div);
		}
	};
    
    /**
     * 营养信息Grid 分页显示
     * @param {Object} productId
     */
    product.initNutriDS = function(productId){
    	if(!productId){
    		productId = 0;
    	}
    	var FirstPageFlag = 1;
		product.nutriDS = new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options){
	            		    if(productId == 0){
	            			return;
	            		    }
    	            		if(FirstPageFlag == 1){  //每次预览新的报告的时候，都从第一页开始显示
    	            			options.page=1;
    	            			options.pageSize=5;
    	            			FirstPageFlag=0;
    	            		}
	            		return portal.HTTP_PREFIX + "/product/getStandNutris/" + options.page + "/" + options.pageSize + "/" + productId;
	            	},
	            	type:"GET",
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        batch : true,
	        page:1,
	        pageSize: 5,
	        schema: {
	            data : function(returnValue) {
	                return returnValue.data.listOfProductNutrition;
	            },
	            total : function(returnValue) {       
	                return returnValue.data.counts;
	            }     
	        },
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
    
	$("#viewWindow").kendoWindow({
		width:1000,
		visible: false,
		title:fsn.l("Product preview"),
	});
	
	$("#bindQsNoWindow").kendoWindow({
		width:1300,
		height:550,
		visible: false,
		modal:true,
		title:"生产许可证信息",
        close: function(e) {
            $(".provinceCityAll").css("display","none"); //关闭选择地址的样式
        }    
	});
	$("#bind_status").kendoWindow({
		width:400,
		height:60,
		visible: false,
		modal:true,
		title:"绑定提示框",
	});
	$("#tsWin").kendoWindow({
		width:500,
		visible:false,
		title:fsn.l("Hint"),
	});
	
	/**
	 * 初始化营养报告Grid
	 * @param dataSource
	 */
	product.initProductNutriGrid = function(girdID){
        if(girdID){
			$("#" + girdID).kendoGrid({
				dataSource:[],
		        editable: false,
		        pageSize : 10,
		        pageable: {
		            refresh: true,
		            pageSizes: 5,
		            messages: lims.gridPageMessage(),
		        },
		        columns: product.productNutriColumns,
			});
		}
	};
	
	/**
	 * 初始化 认证信息
	 * @param {Object} dataSource
	 */
	product.initialCertification=function(dataSource){
		
		/**
		 * 认证信息  图片触发
		 * @param container
		 * @param options
		 */
		function certResourceEditor(container, options){
			var certRes = options.model.certResource;
			$("<span class='a-span'>"+certRes.name +"</span>").appendTo(container);
			if(certRes.url){
    			window.open (certRes.url);
    			return;
    		}
    	}
		
		/**
		 * 认证信息Grid中 认证名称和截止日期的点击事件
		 */
		function certColumnEditor(container,options){
			if(options.field=="endDate"){
				var endDate = options.model.endDate;
				$("<span>"+(endDate!=null?endDate.substr(0,10):"")+"</span>").appendTo(container);
			}else{
				$("<span>"+options.model.cert.name+"</span>").appendTo(container);
			}
		}
		
		/**
		 * 认证信息Grid
		 */
		$("#certification-grid").kendoGrid({
			dataSource:dataSource==null?[]:dataSource,
    		navigatable: true,
    		editable: true,
            columns: [
                {field: "cert.name", title: fsn.l("Certification category"),width:70, editor:certColumnEditor},
                {field: "certResource.name", title: fsn.l("Related images"),
                		editor: certResourceEditor, width:100,
                		template: function(dataItem) {
            	 return "<span class='a-span'>" + (dataItem.certResource.name) + "</span>";         		 
                 }},
                {field: "endDate", title: fsn.l("Is valid as of the time"),width: 100, editor:certColumnEditor,template: function(model){
                	var endDate = fsn.formatGridDate(model.endDate);
                	endDate = (endDate == null ? "" : endDate.toString());
                	endDate = (endDate.indexOf("2200-")>-1 ? "长期有效" : endDate);
                	return endDate;
                }}],
        });
		
	};
	//生产许可证信息检验
	product.validateLicense = function(){
		var flag = true;
    	if($("#qsNoText").val().trim()==""){
    		lims.initNotificationMes('【生产许可证信息】中的证书编号不能为空！',false);
    		flag = false;
    		return flag;
    	}
    	var qsNo=$("#qsNoText").val().trim().replace(/-/g,"").replace(/\s+/g,"");
		var status =  /^[0-9]*$/.test(qsNo);
		if(!status){
			 lims.initNotificationMes("【生产许可证信息】中的证书编号格式不正确。",false);
			 return false;
		}
        var indexOf = $("#qsNoText").val().trim().indexOf(product.formetType); 
		if(indexOf<0 || qsNo.length !=product.formetLength ){
			 lims.initNotificationMes("【生产许可证信息】中的证书编号格式不正确。",false);
			 return false;
		}
    	if($("#busunitName").val().trim()==""){
    		lims.initNotificationMes('【生产许可证信息】中的企业名称不能为空！',false);
    		flag = false;
    		return flag;
    	}
        var productName = $("#productName").data("kendoDropDownList").text();
    	if(productName=="" || productName == "--请选择分类--"){
    		lims.initNotificationMes('请选择【生产许可证信息】中的产品名称！',false);
    		flag = false;
    		return flag;
    	}
    	if($("#qs_mainAddr").val().trim()==""){
    		lims.initNotificationMes('【生产许可证信息】中的生产地址不能为空！',false);
    		flag = false;
    		return flag;
    	}
    	if($("#qs_streetAddress").val().trim()==""){
    		lims.initNotificationMes('【生产许可证信息】中的生产地址的街道地址不能为空！',false);
    		flag = false;
    		return flag;
    	}
    	if(!fsn.validateMustDate("qsStartTime","生产许可证的起始日期")){flag = false;return flag;}
    	if(!fsn.validateMustDate("qsEndTime","生产许可证的截止日期")){flag = false;return flag;}
    	var startDate = $("#qsStartTime").data("kendoDatePicker").value();
        var endDate = $("#qsEndTime").data("kendoDatePicker").value();
        if((endDate-startDate)<1){
        	lims.initNotificationMes('生产许可证的起始日期不能大于或等于截止日期！',false);
        	flag = false;
    		return flag;
        }
        if(business_unit.aryQsAttachments.length<1){
        	lims.initNotificationMes('请上传生产许可证图片！',false);
        	flag = false;
    		return flag;
        }
        
        return flag;
    };
	
	 //校验地址格式是否正确
	 verifyAddress = function(id){
	 	//判断地址选择窗口是否打开，若是没开打则检验
		if($(".provinceCityAll").is(":hidden")){
				var text = $("#"+id).val().trim();
				if(text==""){return;}			//若为空则不进行校验
				var strs= new Array(); 
				strs=text.split("-");				//分割字符串
				if(strs.length<2||strs.length>3){				//判断字符串数是否为3位
					lims.initNotificationMes('格式只能为：</br>【省-市】或【省-市-区（县）】</br>请重新填写！',false);
					$("#"+id).val("");
					return;
				}else{
					//判断每个字符串是否有为空
					for(var i=0;i<strs.length;i++){
						if(strs[i].trim()==""){
							lims.initNotificationMes('省、市、区（县）不能为空</br>请重新填写！',false);
							$("#"+id).val("");
							return;
						}
					}
					var reg = /^[\u4e00-\u9fa5]{1,10}$/g; 				//校验是否全为汉字
					var str = "";
					if(strs.length<2){
						str = strs[0].trim()+strs[1].trim()+strs[2].trim();
					}else{
						str = strs[0].trim()+strs[1].trim();
					}
					if(!reg.test(str)){
						lims.initNotificationMes('只能输入汉字字符，请重新填写！',false);
						$("#"+id).val("");
						return;
					}
				}
			}
	};
	
	/**
     * 解绑qs号grid的分页处理
     */
    function movBindQsGridFun(){
    	var gridDS = product.ownProductDS;
    	var page = gridDS.page();
    	var flag = (gridDS.data().length < 2);
    	if(page > 1 && flag){
    		gridDS.page(--page);
    	} else {
    		product.ownProductDS.read();
    	}
    }	
	
	fsn.initConfirmWindow(function() {
		  $.ajax({
					url:portal.HTTP_PREFIX + "/product/" + product.editProductId,
					type:"DELETE",
					success:function(data){
						if(data.result.status == "true"){
							lims.initNotificationMes(fsn.l("Delete success"), true);
							movBindQsGridFun();
							product.productDS.read();
						}else{
							lims.initNotificationMes(fsn.l("Delete failure"), false);
						}
					}
				});
		  fsn.closeConfirmWin();
	}, undefined, fsn.l("Are you sure delete qs?"),true);
	
	product.initialize();
});