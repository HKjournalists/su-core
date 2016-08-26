(function($) {
	/*定义全局变量*/
	var procom = fsn.procom = fsn.procom || {};
	procom.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	/*实现grid中搜索的事件*/
	 function buildAutoComplete (input,container, options, dataSource){
		 input.attr("name", options.field);
		 input.attr("class", "k-textbox");
		 input.appendTo(container);
		 input.kendoAutoComplete({
			 dataSource: dataSource,
		     filter:"contains",
		 });
	 };
	
	 /**
     * 按营养报告的不同列名查找不同列的集合信息
     * @param {Object} colName 当前点击的列号
     */
     var getAutoItemsDS = function(colName){
    	 return new kendo.data.DataSource({
    		 transport: {
    			 read: {
    				 url:procom.HTTP_PREFIX +"/product/autoItems/"+colName,
    				 type:"GET",
    			 }
    		 },
    		 schema: {
    			 data: "data"
    		 }
    	 });
     };
	 
	 /**
	  *营业报告名称搜索事件
      * @author tangxin
	  */
	 function nutriNameDropDownEditor(container, options){
			$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
			autoBind: false,
            optionLabel:"请选择...",
			dataTextField: "name",
            dataValueField: "id",
			dataSource: {
					transport: {
						read: procom.HTTP_PREFIX + "/product/getStandNutris"
					},
					schema: {
						data : function(returnValue) {
							return removeRepeatNutri(returnValue.data); //响应到页面的数据
			            }
					}
				}
             
			});
		}
     
	/**
	  *移除已经选择的营养标签
      * @author tangxin
	  */
	 function removeRepeatNutri(listNutris){
		 if(listNutris == null && listNutris.length<1){
			 return listNutris;
		 }
		 var selectNutris = procom.createInstanceProductNutris();
		 if(selectNutris == null || selectNutris.length < 1){
			 return listNutris;
		 }
		 for(var i=0;i<selectNutris.length;i++){
			 var nutriName = selectNutris[i].name ;
			 for(var j=0;j<listNutris.length;j++){
				 if(nutriName == listNutris[j].name){
					 listNutris.splice(j,1);
				 }
			 }
		 }
		 return listNutris;
	 }
	 
	 /**
      *营业报告grid 值 搜索事件
      * @author tangxin
      */
	 function autoNutriValue(container,options){
		 var input = $("<input/>");
		 buildAutoComplete(input,container,options,getAutoItemsDS(2));
	 };
	 
	 /**
      *营业报告grid 单 位搜索事件
      @author tangxin
      */
	 function autoNutriUnit(container,options){
		 var input = $("<input/>");
         buildAutoComplete(input,container,options,getAutoItemsDS(3));
	 };
	 
	 /*营业报告grid 条件 搜索事件*/
	 function autoNutriPer(container,options){
		 var input = $("<input/>");
		 buildAutoComplete(input,container,options,getAutoItemsDS(4));
	 };
	 
	 /*营业报告grid NRV 搜索事件*/
	 function autoNutriNrv(container,options){
		var input = $("<input/>");
		buildAutoComplete(input,container,options,getAutoItemsDS(5));
	 };
	
	/*初始化营养报告*/ 
	procom.initProductNutri = function (fromId,templateFmId,datasource) {
		datasource = (datasource == null ? [] : datasource);
		var fromGrid = $("#"+fromId).data("kendoGrid");
		if(fromGrid == null) {
			$("#"+fromId).kendoGrid({
				dataSource:new kendo.data.DataSource({data: datasource, page: 1,pageSize: 30 }),
	    		navigatable: true,
	    		editable: true,
	    		toolbar: [
	     		          {template: kendo.template($("#"+templateFmId).html())}
	     		          ],
	            columns: [
	                {field: "id", title: "id", editable: false, width:1},
	                {field: "name", title: "项目", editor: nutriNameDropDownEditor},
	                {field: "value", title: "值",editor:autoNutriValue},
	                {field: "unit", title:"单位",editor:autoNutriUnit},
	                {field: "per", title: "条件[ 如：每100(mL) ]",editor:autoNutriPer, width: 200},
	                {field: "nrv", title: "NRV(%)",editor:autoNutriNrv},
	                { command: [{name:"Remove",
	               	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"), 
	               	    click:function(e){
	               	    	e.preventDefault();
	               	    	var delItem = $("#product-nutri-grid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
	               	    	$("#product-nutri-grid").data("kendoGrid").dataSource.remove(delItem);
	               	    }
	               }], title: fsn.l("Operation"), width: 100 }],
	        });
		} else {
			fromGrid.setDataSource(new kendo.data.DataSource({data: datasource, page: 1,pageSize: 30 }));
		}
		
	};
	
	/*添加检测项目行*/
	procom.addNutri = function(){
		$("#product-nutri-grid").data("kendoGrid").dataSource.add({ name:"", value:"", unit:"", per:"", nrv:""});
	};
	
	/*增加产品认证信息*/
	procom.addCert = function(){
    	$("#certification-grid").data("kendoGrid").dataSource.add({ cert:"", certResource:"", endDate:""});
	};
	
	/*产品图片上传按钮的初始化方法*/
	procom.buildUpload = function(id, attachments, msg, flag){
	    	 $("#"+id).kendoUpload({
	        	 async: {
	                 saveUrl: procom.HTTP_PREFIX + "/resource/kendoUI/addResources/" + flag,
	                 removeUrl: procom.HTTP_PREFIX + "/resource/kendoUI/removeResources",
	                 autoUpload: true,
	                 removeVerb:"POST",
	                 removeField:"fileNames",
	                 saveField:"attachments",
	        	 },localization: {
	                 select:id=="upload_product_files"?fsn.l("upload_Product"):fsn.l("upload_cert"),
	                 retry:lims.l("retry",'upload'),
	                 remove:lims.l("remove",'upload'),
	                 cancel:lims.l("cancel",'upload'),
	                 dropFilesHere: lims.l("drop files here to upload",'upload'),
	                 statusFailed: lims.l("failed",'upload'),
	                 statusUploaded: lims.l("uploaded",'upload'),
	                 statusUploading: lims.l("uploading",'upload'),
	                 uploadSelectedFiles: lims.l("Upload files",'upload'),
	             },
                 upload: function(e){
                      var files = e.files;
                      $.each(files, function () {
                    	  if(this.name.length > 100){
                 		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
                 		  		e.preventDefault();
                 		  		return;
                 	  	  }
	                      var extension = this.extension.toLowerCase();
	                      if(id =="upload_product_files" ||id =="upload_certification_files"){
	                          if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
	                              lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
	                              e.preventDefault();
	                          }
	                      }
                      });
                 },
	             multiple:true,
	             success: function(e){
	            	 if(e.response.typeEror){
	            		 $("#"+msg).html("文件类型有误，该文件不会被保存，请删除后重新上传：.png .bmp .jpeg .jpg格式的文件！");
	            		 return;
	            	 }
	            	 if(e.response.morSize){
	            		 $("#"+msg).html("您上传的文件已经超过10M，请删除后重新上传pdf、png、bmp、jpeg .jpg格式的文件!");
	            		 return;
	            	 }
	                 if (e.operation == "upload") {
	                	 if(msg=="proFileMsg"){
	                		 lims.log("upload");
	                         lims.log(e.response);
	                         attachments.push(e.response.results[0]);
	                     }
						 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
	                 }else if(e.operation == "remove"){
	                     lims.log(e.response);
	                     for(var i=0; i<attachments.length; i++){
	                    	 if(attachments[i].name == e.files[0].name){
	                    		 while((i+1)<attachments.length){
	                    			 attachments[i]=attachments[i+1];
	                    			 i++;
	                    		 }
	                    		 attachments.pop();
	                    		 break;
	                    	 }
	                     }
	                 }
	             },
	             remove:function(e){
	            	 if(msg=="repFileMsg"){
	            		 $("#"+msg).html("("+fsn.l("Note: The file size can not exceed 10M! Only supports file formats: pdf")+")");
	            	 }else{
	            		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
	            	 }
	             },
	             error:function(e){
	            	 $("#"+msg).html(fsn.l("An exception occurred while uploading the file! Please try again later ..."));
	             },
	       });
		};
		
		var listCert = [];
		
		/*其他认证信息的下拉选项*/
		function certNameDropDownEditor(container, options){
			$('<input required data-text-field="name" data-value-field="name" data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
				optionLabel:"请选择产品认证信息",
				dataTextField: "name",
	            dataValueField: "id",
				dataSource: {
					transport: {
						read: procom.HTTP_PREFIX + "/business/getCertificationsByOrg"
					},
					schema: {
						data : function(returnValue) {
							listCert=returnValue.data;
							if(returnValue.data == null) fsn.initNotificationMes("认证信息为空，请到企业基本信息页面添加！", false);
							return returnValue.data == null?[]:returnValue.data;  //响应到页面的数据
			            }
					}
				},
				index: 0,
				change: function(e) {
				    var value = this.value();
				    options.model.cert={name:value};
				    if(listCert){
				    	for(var i=0;i<listCert.length;i++){
				    		if(value==listCert[i].name){
				    			options.model.id=listCert[i].id;
				    			options.model.certResource={name:listCert[i].fileName,url:listCert[i].url};
				    			options.model.endDate=new Date(listCert[i].endDate).format("YYYY-MM-dd hh:mm:ss");
				    			$("#certification-grid").data("kendoGrid").refresh();
				    			break;
				    		}
				    	}
				    }
				  },
			});
	    };
		
	    /*认证信息gird的单元格的编辑事件*/
		function certResourceEditor(container, options){
			if(options.model.certResource.url){
				window.open (options.model.certResource.url);
				return;
			}
			$("<span>"+options.model.certResource +"</span>").appendTo(container);
		};
	    
		/*初始化其他认证信息grid*/
		procom.initialCertGrid=function(formId,cerDs){
			$("#"+formId).kendoGrid({
	    		 dataSource:cerDs==null?new kendo.data.DataSource({data: [], page: 1,pageSize: 10 }):
	    			 					new kendo.data.DataSource({data: cerDs, page: 1,pageSize: 10 }),
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
	                   { field: "cert.name", title:"认证类别",editor: certNameDropDownEditor, width: 60 },
	                   { field: "certResource.name", title:"认证图片名称", editor: certResourceEditor, width: 60 },
	                   { field: "endDate", title:"有效期截止时间",editable:false,width:50,template: '#=endDate= endDate==""?"":endDate.substring(0,10)#'},
	                   { command: [{name:"Remove",
	                   	    text:"<span class='k-icon k-cancel'></span>" + fsn.l("Delete"),
	                   	    click:function(e){
	                   	    	e.preventDefault();
	                   	    	var delItem = $("#certification-grid").data("kendoGrid").dataItem($(e.currentTarget).closest("tr"));
	                   	    	$("#certification-grid").data("kendoGrid").dataSource.remove(delItem);
	                   }}], title: fsn.l("Operation"), width: 30 }],
	    	});
		};
		
		/*初始化kendoWindow窗体的公共方法*/
		procom.initKendoWindow=function(winId,title,width,height,isVisible,actions){
			$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:actions!=null?actions:["Close"],
			});
		};
		
		/*给生产企业地址赋值*/
		procom.setProducerAddress = function (producer){
			if(producer==null){
				$("#mainAddress").val("");
				$("#streetAddress").val("");
				return;
			}
			if(producer.otherAddress==null){
					$("#streetAddress").val(producer.address);
			}else{
				var othAddrs = producer.otherAddress.split("--");
				if(othAddrs.length>1){
					$("#mainAddress").val(othAddrs[0]);
					$("#streetAddress").val(othAddrs[1]);
				}
			}
		};
		
		/*根据企业名称加载企业信息，主要是获取企业的地址*/
		var getBusinessUnitAddressByName = function(name){
			$.ajax({
				url: procom.HTTP_PREFIX + "/business/getCurrentBusiness?name="+name,
				type:"GET",
				dataType:"json",
				success:function(returnData){
					if(returnValue.result.status == "true"){
						procom.setProducerAddress(returnData.data);
					}
				}
			});
		};
		
		var busName = null; // 变量保存生产企业名称
		/*流通企业录入产品时初始化企业名称和商标控件*/
	    procom.initBusNameAndBrandComplete=function(){
	    	$("#businessName").kendoAutoComplete({
		         dataSource: lims.getAutoLoadDsByUrl("/business/searchAllBusUnitName"),
		         filter: "contains",
		         placeholder: "搜索...",
		         select: function(e){
					 busName = this.dataItem(e.item.index());
			         $("#businessName").val(busName);
			         getBusinessUnitAddressByName(busName);
				 },
		     });
	    	
	    	/*生产企业名称输入框失去焦点事件*/
		    $("#businessName").blur(function(){
		    	/*每次触发事件时，判断输入框的值是否被改变*/
		    	if(busName==$("#businessName").val().trim()){
		    		return;
		    	}
		    	busName=$("#businessName").val().trim();
		    	getBusinessUnitAddressByName(busName);
		    });
	    	
	    	$("#businessBrand").kendoAutoComplete({
		         dataSource: lims.getAutoLoadDsByUrl("/business/business-brand/getAllName"),
		         filter: "contains",
		         placeholder: "搜索...",
		         select: function(e){
					 var brand = this.dataItem(e.item.index());
			         $("#businessBrand").val(brand);
				 },
		     }); 
	    };
	    
	    /*根据指定属性值，初始化Combobox*/
		procom.initialComboBox = function(formId,datasource,textField,valueField){
			$("#"+formId).kendoComboBox({
		        dataTextField: textField,
		        dataValueField: valueField,
		        dataSource: datasource,
		        filter: "contains",
		        minLength: 0,
		        index:0
		    });
		};
		
		/*根据指定的url，获取一个kendoDataSource数据*/
		procom.getDataSourceByUrl = function(httpPrefix,url){
			return new kendo.data.DataSource({
				transport: {
		            read: {
		            	url:httpPrefix + url,
		            	type:"GET"
		            }
		        },
		        schema: {
		        	data: function(returnValue){
		        		return returnValue.data;
		        	}
		        }
		    });
		};
		
        /**
         * 商标输入框的change事件
         * @author tangxin 2015/04/04
         */
		function brandChange(){
			var busBrndBox = $("#businessBrand").data("kendoComboBox");
			if(busBrndBox != null && busBrndBox.select() == -1){
				busBrndBox.value("");
				lims.initNotificationMes(fsn.l("Please select the drop-down box brand")+"!", false);
			}
		}
		
		/* 生产企业：初始化 所属品牌 */
		procom.initialBrands = function(){
			$("#businessBrand").bind("change",brandChange);
			/*绑定change事件*/
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
				url:procom.HTTP_PREFIX + "/business/business-brand/getMyBrandsAll",
				type:"GET",
				async:false,
				dataType:"json",
				success:function(returnValue){
					if(returnValue.result.status == "true"){
						$("#businessBrand").data("kendoComboBox").setDataSource(returnValue.data);
						 if(returnValue.data.length>0){
							 $("#businessBrand").data("kendoComboBox").value(returnValue.data[0].name);
							 $("#businessBrand").focus();
						 }else{
							 $("#businessBrand").focus();
							 lims.initNotificationMes("品牌为空，请先到品牌管理页面添加相关品牌信息！", false);
						 }
					}
				},
			});
		};
		
		/**
	      * 对用户输入的保质期进行数据规范验证
	      */
		procom.validateExpiration = function(){
			 /* 1. 验证年份  */
			 var proExpirYear = $("#proExpirYear").val();
			 var proExpirMonth = $("#proExpirMonth").val();
			 var proExpirDay = $("#proExpirDay").val();
	         if(proExpirYear ==""&&proExpirMonth==""&&proExpirDay==""){
	             $("#expirationDate-num").val("");
	         }
			 if(!proExpirYear.match(/^[0-9]*$/)){
				 lims.initNotificationMes("【保质期】的年份应该是数字！", false);
				 return false;
			 }else{
				 procom.countExpirday();
			 }
			 /* 2. 验证月份  */
			 if(!proExpirMonth.match(/^[0-9]*$/)){
				 lims.initNotificationMes("【保质期】的月份应该是数字！", false);
				 return false;
			 }else{
				 procom.countExpirday();
			 }
			 /* 3. 验证天数 */
			 if(!proExpirDay.match(/^[0-9]*$/)){
				 lims.initNotificationMes("【保质期】的天数应该是数字！", false);
				 return false;
			 }else{
				 procom.countExpirday();
			 }
			 return true;
		 };
	     
	     /**
	      * 对用户输入的保质期进行天数换算
	      */
		 procom.countExpirday=function(){
		    	$("#foodinfo_expirday").val("");
		    	var result;
		    	var year=$("#proExpirYear").val();
		    	var month=$("#proExpirMonth").val();
		    	var day=$("#proExpirDay").val();
		    	year=year.trim().length<1?"0":year.trim();
		    	month=month.trim().length<1?"0":month.trim();
		    	day=day.trim().length<1?"0":day.trim();
		    	if(year=="0"&&month=="0"&&day=="0"){
		    		return;
		    	}
		    	var rel=/^[0-9]*$/;
		    	if (!year.match(rel)||!month.match(rel)||!day.match(rel)) {
		    		return;
		    	}
		    	result=year*365+month*30+day*1;
		    	if(result!=0){
		    		$("#expirationDate-num").val(result);
	                return result;
		    	}
	            
		    };
	/**
	 * 产品信息录入界面公共字段的验证
	 * 多个页面使用时请保证input的id一致
	 */	    
	procom.valiPubField = function(){
		var proName = $("#name").val().trim();
		if(proName==""){
			lims.initNotificationMes("请填写产品名称！", false);
			return false;
		} 
		var brand = $("#businessBrand").val().trim();
		if(brand==""){
			lims.initNotificationMes("产品的所属品牌不能为空！", false);
			return false;
		}
		var proUnit = $("#productUnit").val().trim();
		if(proUnit==""){
			lims.initNotificationMes("请填写产品单位！", false);
			return false;
		}
		return true;
	};
	
	/*保存产品信息时，对产品营养报告进行封装，返回*/
	procom.createInstanceProductNutris = function(){
		var productNutris=[];
   	 	var nutris=$("#product-nutri-grid").data("kendoGrid").dataSource.data();
   	 	var j=0;
   	 	for(var i=0;i<nutris.length;i++){
   	 		if(nutris[i].name=="") continue;
   	 		productNutris[j]={
   				 id: nutris[i].id==null?"":nutris[i].id,
   				 name: nutris[i].name,
   				 value: nutris[i].value,
   				 unit: nutris[i].unit,
   				 per: nutris[i].per,
   				 nrv: nutris[i].nrv,
   	 		};
   	 		j+=1;
   	 	}
   	 return productNutris;
    };
    
    /*封装认证信息*/
	procom.getCerts = function(){
	    var listProductCerts=[];
	    if(fsn.root.busUnit.type=="流通企业") return listProductCerts;
	    var certs=$("#certification-grid").data("kendoGrid").dataSource.data();
	    var j=0;
	    for(var i=0;i<certs.length;i++){
	    	if(certs[i].name==""){
	    		j++;
	    		continue;
	    	}
	    	listProductCerts[i-j]={
	    		id: certs[i].id,
	    	};
	    }
	    return listProductCerts;
	 };
	 
	 /*获取保质期*/
	 procom.getExpirDay=function(){
	    	var result="";
	    	var year=$("#proExpirYear").val();
	    	var month=$("#proExpirMonth").val();
	    	var day=$("#proExpirDay").val();
	    	year=year.trim().length<1?"0":year.trim();
	    	month=month.trim().length<1?"0":month.trim();
	    	day=day.trim().length<1?"0":day.trim();
	    	var re1 = /^[0-9]*$/;
	    	if (!year.match(re1)||!month.match(re1)||!day.match(re1)) {
	    		return $("#proExpirOther").val();
	    	}
	    	if(year!="0"){
	    		result+=year+"年";
	    	}
	    	if(month!="0"){
	    		result+=month+"月";
	    	}
	    	if(day!="0"){
	    		result+=day+"天";
	    	}
	    	return result;
	    };
	 
	    /*初始化窗口*/
	   procom.initKendoWindow=function(winId,title,width,height,isVisible,actions){
			$("#"+winId).kendoWindow({
				title:title,
				width:width,
				height:height,
				modal:true,
				visible:isVisible,
				actions:actions!=null?actions:["Close"],
			});
		};
	    
    /*封装产品信息*/
    procom.createProduct = function(){
		var index = $("#category3").data("kendoComboBox").select();
		var category = null;
		if(index==-1){
			category = {
    			name: $("#category3").val(),
				category:{
					id:$("#category2").data("kendoComboBox").value(),
					code:$("#category2").data("kendoComboBox").dataItem().code,
				}
    		};
		}else{
			category = {
    			id: $("#category3").data("kendoComboBox").value().trim(),
				category:{
					id:$("#category2").data("kendoComboBox").value(),
					code:$("#category2").data("kendoComboBox").dataItem().code,
				}
    		};
		}
		var businessBrand = {};
		if(fsn.root.busUnit.type=="流通企业"){
			businessBrand.id=$("#businessBrand").attr("data-id-value");
			businessBrand.name=$("#businessBrand").val();
		}else{
			businessBrand.id=$("#businessBrand").data("kendoComboBox").value().trim();
			businessBrand.name=$("#businessBrand").data("kendoComboBox").text().trim();
		}
		
		var producer={
				name:$("#businessName").val().trim(),
				address:$("#mainAddress").val().trim().replace(/-/g,"")+$("#streetAddress").val().trim(),
				otherAddress:$("#mainAddress").val().trim()+"--"+$("#streetAddress").val().trim(),
		};
		var product = {
				id: $("#id").val().trim(), 
				name: $("#name").val().trim(),  // 产品名称
				otherName: $("#otherName").val().trim(), // 产品别名
				barcode: $("#barcode").text(),  // 产品barcode
				format: $("#format").val().trim(),  // 规格
				regularity: lims.convertRegularityToItem($("#regularity").val().trim()), // 国家标准 
				status: $("#status").val().trim(), // 状态
				characteristic: $("#characteristic").val().trim(),  // 特色
				cstm: $("#cstm").val().trim(),  // 适宜人群
				businessBrand: businessBrand,  // 商标
				category: category,  // 产品种类
				ingredient: $("#ingredient").val().trim(),  // 配料
				des: $("#des").val().trim(), // 产品描述
				note: $("#note").val().trim(), // 备注
				listOfCertification: procom.getCerts(),  // 认证信息
				listOfNutrition:procom.createInstanceProductNutris(), //营业标签
                expirationDate: $("#expirationDate-num").val().trim(),//保质天数
                expiration:procom.getExpirDay(),//保质期
                unit: $("#productUnit").val().trim(),//单位
                producer:producer,
    	};
		return product;
	};
	
	/*编辑商品信息时，给品牌赋值*/
	 var editProductSetBrand = function (brand){
		$("#businessBrand").data("kendoComboBox").value(brand.id);
		var bval = $("#businessBrand").data("kendoComboBox").value();
		/*判断当前商品关联的品牌是否属于该企业信息，相等则不属于*/
		if(bval==$("#businessBrand").val().trim()){
			$("#businessBrand").data("kendoComboBox").text(brand.name);
			bval = $("#businessBrand").data("kendoComboBox").value();
			if(bval==brand.name){
				$("#businessBrand").data("kendoComboBox").text("");
				lims.initNotificationMes("当前商品关联的品牌信息无效，请重新选择本企业下对应的品牌，如果没有对应的品牌，请先新增。", false);
			}
		}
	};
	
	// 编辑产品时，页面赋值操作
	procom.writeProductInfo = function (data) {
		if (data!=null) {
			$.each(data, function(k,v){
				if (v!=null) {
					switch (k) {
					case "id":
						$("#id").val(v);
						break;
					case "name":
						$("#name").val(v);
						break;
					case "status":
						$("#status").val(v);
						break;
					case "format":
						$("#format").val(v);
						break;
					case "category":
						$("#category1").data("kendoComboBox").value(v.category.code.substring(0,2));
								$.ajax({
									url:procom.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode="+v.category.code.substring(0,2),
									type:"GET",
									dataType:"json",
									async:false,
									success:function(returnValue){
										if(returnValue.result.status == "true"){
											$("#category2").data("kendoComboBox").setDataSource(returnValue.data);											
											$("#category2").data("kendoComboBox").value(v.category.id);
										}
									},	
								});
									$.ajax({
										url:procom.HTTP_PREFIX + "/testReport/searchLastCategory/"+v.category.id+"?categoryFlag="+true,
										type:"GET",
										dataType:"json",
										async:false,
										success:function(returnValue){
											if(returnValue.result.status == "true"){
												$("#category3").data("kendoComboBox").setDataSource(returnValue.data);
											}
										},	
									});	
									if (v.id) {
										$("#category3").data("kendoComboBox").value(v.id);
									}else{
										$("#category3").data("kendoComboBox").text(v.name);
									}
						break;
					case "barcode":
						$("#barcode").text(v);
						break;
					case "businessBrand":
						if(fsn.root.busUnit.type!="流通企业"){
							/*编辑商品信息时，给品牌赋值*/
							editProductSetBrand(v);
						}else{
							$("#businessBrand").val(v.name);
							$("#businessBrand").attr("data-id-value",v.id);
						}
						break;
					case "regularity":
						var text = "";
						for(var i=0;i<v.length;i++){
							text = text+v[i].name+";"
						}
						$("#regularity").val(text);
						break;
					case "expirationDate":
						$("#expirationDate-num").val(v);
						break;
					case "expiration":
						$("#expiration").val(procom.setExpirDay(v));
						break;
					case "characteristic":
						$("#characteristic").val(v);
						break;
					case "note":
						$("#note").val(v);
						break;
					case "cstm":
						$("#cstm").val(v);
						break;
					case "des":
						$("#des").val(v);
						break;
					case "ingredient":
						$("#ingredient").val(v);
						break;
					case "unit":
						$("#productUnit").val(v.name);
						break;
					case "otherName":
						$("#otherName").val(v);
						break;
					}
				}
			});
			$("#businessName").val(data.producer!=null?data.producer.name:"");
		}
	};
	
	// 初始化产品图片 show
	procom.setProAttachments=function(proAttachments){
		 var dataSource = new kendo.data.DataSource();
		 fsn.root.aryProAttachments.length=0;
		 if(proAttachments.length>0){
             $("#proResListView").show();
			 for(var i=0;i<proAttachments.length;i++){
				 fsn.root.aryProAttachments.push(proAttachments[i]);
				 dataSource.add({attachments:proAttachments[i]});
			 }
		 }
		 $("#proAttachmentsListView").kendoListView({
             dataSource: dataSource,
             template:kendo.template($("#uploadedFilesTemplate").html()),
         });
	 };
	 
	// 从页面删除产品图片
     procom.removeRes = function(resID){
		 var dataSource = new kendo.data.DataSource();
		 for(var i=0; i<fsn.root.aryProAttachments.length; i++){
        	 if(fsn.root.aryProAttachments[i].id == resID){
        		 while((i+1)<fsn.root.aryProAttachments.length){
        			 fsn.root.aryProAttachments[i] = fsn.root.aryProAttachments[i+1];
        			 i++;
        		 }
        		 fsn.root.aryProAttachments.pop();
        		 break;
        	 }
         }
		 
		 if(fsn.root.aryProAttachments.length>0){
			for(i=0; i<fsn.root.aryProAttachments.length; i++){
				dataSource.add({attachments:fsn.root.aryProAttachments[i]});
			}
		 }
		 $("#proAttachmentsListView").data("kendoListView").setDataSource(dataSource);
			if(fsn.root.aryProAttachments.length == 0){
				$("#proResListView").hide();
		 }
	 };
	 
	 //验证该标准是否已经选择
	procom.validaRegularity = function(text){
		var ul = document.getElementById("regularityList");
		var li = ul.getElementsByTagName("li");
		if (li.length >= 1) {
			for (var i = 0; i < li.length; i++) {
				var value = li[i].innerHTML;
				if (value.length > value.replace(text, "").length) {
					lims.initNotificationMes(text + "该标准已经被选择，不能再次选择！", false);
					return false;
				}
			}
		}
		return true;
	}
	
	//添加执行标准
	  procom.addRegularity = function(){
	  	//空值校验
		if($("#regularity_type").val().replace(/[ ]/g,"")==""){
			lims.initNotificationMes("请填写标准类型！", false);
			return;
		}
		if($("#regularity_year").val().replace(/[ ]/g,"")==""){
			lims.initNotificationMes("请填写执行序号及年份！", false);
			return;
		}
		//添加执行标准时屏蔽标准名称以为必填
		/*if($("#regularity_name").val().replace(/[ ]/g,"")==""){
			lims.initNotificationMes("请填写标准名称！", false);
			return;
		}*/
		//如果超过12，则不允许添加
		var ul = document.getElementById("regularityList");
		var count = ul.getElementsByTagName("li").length;
		if(count>=12){
			lims.initNotificationMes("执行标准过多，不能再添加！", false);
			return;
		}
		//将新增标准格式化后添加到列表中
	 	var text = $("#regularity_type").val().replace(/[ ]/g,"")+" "+$("#regularity_year").val().replace(/[ ]/g,"")+" "+$("#regularity_name").val();		
		if(!procom.validaRegularity(text)){return;}
		var li= document.createElement("li");
		li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
		ul.appendChild(li);
		//清空信息
		$("#regularity_type").val("");
		$("#regularity_year").val("");
		$("#regularity_name").val("");
	  }
	  
	   //验证该标准是否已经选择
	procom.validaRegularity = function(text){
		var ul = document.getElementById("regularityList");
		var li = ul.getElementsByTagName("li");
		if (li.length >= 1) {
			for (var i = 0; i < li.length; i++) {
				var value = li[i].innerHTML;
				if (value.length > value.replace(text, "").length) {
					lims.initNotificationMes(text + "该标准已经被选择，不能再次选择！", false);
					return false;
				}
			}
		}
		return true;
	}
	
	//执行标准赋值
	procom.setRegularityValue = function(){
		/*刷新执行标准datasource*/
		$.ajax({
			url:procom.HTTP_PREFIX + "/testReport/searchLastCategory/"+$("#category2").data("kendoComboBox").value()+"?categoryFlag="+false,
			type:"GET",
			dataType:"json",
			async:false,
			success:function(value){
				if(value.result.status == "true"){
					$("#regularityInfo").data("kendoComboBox").setDataSource(value.data);
					if(value.data==null||value.data.length<=0){
						lims.initNotificationMes("该二级分类下没有执行标准，请先新增执行标准！", false);
					}													
				}
			},	
		});
		//将是执行标准格式化添加到已选择的执行标准框里
		var text = $("#regularity").val().trim();
		var regularity = text.split(";");
		var ul = document.getElementById("regularityList");
		var ds = $("#regularityInfo").data("kendoComboBox").dataSource.data();
		//将执行标准格式化添加到已选择的执行标准框中
		for (var i = 0; i < regularity.length - 1; i++) {
			var li = document.createElement("li");
			li.innerHTML = "<a herf='#' title='" + regularity[i] + "'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>" + regularity[i] + ";</a>";
			ul.appendChild(li);
		}
		//将执行标准datasource进行筛选
		for(var j=0;j<ds.length;j++){
			if(text.indexOf(ds[j].name)!=-1){
				$("#regularityInfo").data("kendoComboBox").dataSource.remove(ds[j]);
				j=j-1;
			}
		};
	}
	 
	//清空产品图片资源
	procom.clearProFiles=function(){
		fsn.root.aryProAttachments.length=0;
		$("#proAttachmentsListView").data("kendoListView").dataSource.data([]);
		$("#proResListView").hide();
	};
	 
	 /**
      * 填充保质期
      * @param {Object} value
      */
     procom.setExpirDay = function(value){
	    	$("#proExpirYear").val("");
	    	$("#proExpirMonth").val("");
	    	$("#proExpirDay").val("");
	    	if(value==null){
	    		return;
	    	}
	    	var tempv=value.trim().split("年");
	    	var year="0";
	    	var month="0";
	    	var day="0";
	    	if(tempv.length>1){
	    		year=tempv[0].trim();
	    		tempv=tempv[1].trim().split("月");
	    	}else{
	    		tempv=tempv[0].trim().split("月");
	    	}
	    	if(tempv.length>1){
	    		month=tempv[0].trim();
	    		tempv=tempv[1].trim().split("天");
	    	}else{
	    		tempv=tempv[0].trim().split("天");
	    	}
	    	if(tempv.length>1){
	    		day=tempv[0];
	    	}
	    	$("#proExpirYear").val(year=="0"?"":year);
	        $("#proExpirMonth").val(month=="0"?"":month);
	        $("#proExpirDay").val(day=="0"?"":day);
	    };
})(jQuery);