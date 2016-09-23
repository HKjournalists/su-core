$(document).ready(function(){
	var portal = fsn.portal = fsn.portal || {};
	var root = window.root = window.root || {};
	root.aryRepAttachments = new Array();
	root.aryGeAttachments = new Array();
	root.aryByAttachments = new Array();
	root.aryBusinessAttachments = new Array();
	var productId=null;
	root.initialize=function(){
		root.upload1("upload-other-img", root.aryRepAttachments ,"otherEroMsg", "IMG");
		root.upload1("upload-ge-img", root.aryGeAttachments ,"szEroMsg", "IMG");
		root.upload1("upload-by-img", root.aryByAttachments ,"erEroMsg", "IMG");
		root.upload1("upload-business-img", root.aryBusinessAttachments ,"cnEroMsg", "IMG");
		
		 $("#barcode").kendoAutoComplete({
        	            dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode?businessType=生产企业"),
        	            filter: "startswith",
        	            placeholder: "搜索...",
        	            select: root.onSelectBarcode,
        	        });
		$("#sourceDate,#warehouseDate,#productDate,#leaveDate").kendoDatePicker({
			format: "yyyy-MM-dd",
			height:30,
			max: new Date(),
			culture : "zh-CN"
		});
	};
	var id=getUrlParam("id");
	if(id!=null){
	    var _barcode=null
		$.get(fsn.getHttpPrefix()+"/traceData/getTraceDataById",{id:id},function(rs){
			_barcode=root.getbarcodeByProductId(rs.traceData.productID);
			productId=rs.traceData.productID;
			$("#barcode").val(_barcode);
			$("#name").val(rs.traceData.productName);
			$("#sourceArea").val(rs.traceData.sourceArea);
			$("#sourceDate").val(fsn.formatGridDate(rs.traceData.sourceDate));
			$("#processor").val(rs.traceData.processor);
			$("#packagePlant").val(rs.traceData.packagePlant);
			$("#warehouseDate").val(fsn.formatGridDate(rs.traceData.warehouseDate));
			$("#productDate").val(fsn.formatGridDate(rs.traceData.productDate));
			$("#leaveDate").val(fsn.formatGridDate(rs.traceData.leaveDate));
			if(rs.traceData.sourceCertifyList){
				if(rs.traceData.sourceCertifyList.length>0){
			         $("#logListView0").show();
			         var dataSource = new kendo.data.DataSource();
					 for(var i=0;i<rs.traceData.sourceCertifyList.length;i++){
						 root.aryRepAttachments.push(rs.traceData.sourceCertifyList[i]);
						 dataSource.add({attachments:rs.traceData.sourceCertifyList[i]});
					 }
				 }else{
			         $("#logListView0").hide();
				 }
				 $("#proAttachmentsListView0").kendoListView({
			         dataSource: dataSource,
			         template:kendo.template($("#uploadedFilesTemplate0").html()),
			     });
			}
			if(rs.traceData.growEnvironmentResource){
				$("#logListView1").show();
			    var dataSource = new kendo.data.DataSource();
				root.aryGeAttachments.push(rs.traceData.growEnvironmentResource);
				dataSource.add({attachments:rs.traceData.growEnvironmentResource});
				$("#proAttachmentsListView1").kendoListView({
			         dataSource: dataSource,
			         template:kendo.template($("#uploadedFilesTemplate1").html()),
			     });
				$("#upload-ge-img").data("kendoUpload").disable();
			}else{
		        $("#logListView1").hide();
			}
			if(rs.traceData.buyLink){
			    $("#logListView2").show();
			    	var dataSource = new kendo.data.DataSource();
					root.aryByAttachments.push(rs.traceData.buyLink);
					dataSource.add({attachments:rs.traceData.buyLink});
				$("#proAttachmentsListView2").kendoListView({
				    dataSource: dataSource,
				    template:kendo.template($("#uploadedFilesTemplate2").html()),
				});
				$("#upload-by-img").data("kendoUpload").disable();
			}else{
			    $("#logListView2").hide();
			}
			if(rs.traceData.businessPromiseResource){
				var dataSource = new kendo.data.DataSource();
			    $("#logListView3").show();
					root.aryBusinessAttachments.push(rs.traceData.businessPromiseResource);
					dataSource.add({attachments:rs.traceData.businessPromiseResource});
				$("#proAttachmentsListView3").kendoListView({
				    dataSource: dataSource,
				    template:kendo.template($("#uploadedFilesTemplate3").html()),
				});
				$("#upload-business-img").data("kendoUpload").disable();
			}else{
			   $("#logListView3").hide();
			}
            $("#barcode").attr("disabled","disabled");
			$("#save").find("span:last-child").html('更新');
		},'json');
	
};
	 	root.upload1 = function(id, attachments, msg, flag){
			var selectLocal="";
			var multiple = false;
			if(id=="upload-other-img"){
				selectLocal=fsn.l("上传原材料证明");
				multiple=true;
			}else if(id=="upload-ge-img"){
				selectLocal=fsn.l("上传生长环境图片");
			}else if(id=="upload-by-img"){
				selectLocal=fsn.l("上传购买二维码");
			}else if(id=="upload-business-img"){
				selectLocal=fsn.l("上传企业承诺图片");
			}
			$("#"+id).kendoUpload({
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
	            upload: function(e){
	                var files = e.files;
	                 $.each(files, function () {
	                	 if(this.name.length > 100){
	            		  		lims.initNotificationMes('上传的文件名称应该小于50个汉字！',false);
			                    e.preventDefault();
			                    return;
	            	  	 }
	                     var extension = this.extension.toLowerCase();
	                     if(flag == "IMG"){
	                             if (extension != ".png" && extension != ".bmp" && extension != ".jpeg" && extension != ".jpg") {
	                             lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!', false);
	                             e.preventDefault();
	                     }
	                 }
	               });
	            },
	            multiple:multiple,
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
			            	 lims.log("upload");
			                 lims.log(e.response);
			                 attachments.push(e.response.results[0]);
			               	 if(flag == "IMG"){
			               		 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.png .bmp .jpeg .jpg )");
			                 }else if(flag == "PDF"){
			                	 $("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：.pdf )");
			                 }
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
		           	 if(flag == "PDF"){
		           		 $("#"+msg).html("("+fsn.l("Note: The file size can not exceed 10M! Only supports file formats: pdf")+")");
		           	 }else if(flag == "IMG"){
		           		 $("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
		           	 }
	            },
	            error:function(e){
	           	 	 $("#"+msg).html(fsn.l("An exception occurred while uploading the file! Please try again later ..."));
	            },
	      });
		};
	$("#barcode").blur(function() {
        	var barcode = $("#barcode").val();

    		$("#name").val("");
        	/*条形码为空不进行判断*/
        	if(barcode==""){
        		return;
        	}
    		 root.judgeProductByBarcode(barcode);

    	});
    root.onSelectBarcode = function(e){
        		$("#name").val("");
        		 root.initBarcode = this.dataItem(e.item.index());
        		 $("#barcode").val(root.initBarcode);
        		 root.judgeProductByBarcode(root.initBarcode);
        		// portal.codeFlag = false;
        	};
    root.getbarcodeByProductId=function(productid){
         var productbarcode=null;
        $.ajax({
                	         url: fsn.getHttpPrefix() + "/product/" + productid,
                	         type: "GET",
                	         dataType: "json",
                	         async: false,
                	         success: function(returnValue) {
                	             if (returnValue.result.status == "true") {
                	             	productbarcode = returnValue.data.barcode;
                	             	console.log(productbarcode);
                	             }
                	         }
                	     });
                	     return productbarcode;
    }
    root.judgeProductByBarcode = function(barcode){
    		/*条形码为空不进行判断*/
    		if(barcode==""){
    			return;
    		}
    		root.queryCheckByBarcode(barcode);
    		/* 1 验证产品条形码是否为系统已存在产品 */
            productId = root.queryProductIdByBarcode(barcode);
            console.log(productId);
            if(productId==null){
            	/* 1.1 系统中不存在 */
            	fsn.initNotificationMes("系统中不存在该产品！", false);
    			return;
            }
        	else{
        		/* 1.2.2 是引进产品或者是自己新增的产品，则进行正常录报告流程 */
        	//	root.current_barcode_can_use = true;
        		root.initPageData(productId);
        	}
    	};
    root.queryCheckByBarcode = function(barcode){
        		$.ajax({
        	         url: fsn.getHttpPrefix() + "/product/query/getCheckProductId/" + barcode,
        	         type: "GET",
        	         dataType: "json",
        	         async: false,
        	         success: function(returnValue) {
        	             if (returnValue.result.status == "true") {
        	             	productId = returnValue.data;
        	             	if(productId == null){
        	                	/* 1.1 该条形码系统不存在，则引导用户跳转至产品新增界面 */
        	                	root.current_barcode_can_use = false;
        	        			$("#create_product_warn_window").data("kendoWindow").open().center();
        	        			return;
        	                }
        	             	var count = returnValue.count;
        	            	if(count == 0){
        	            		/* 1.2.1 不是引进产品，则引导用户填写销往客户 */
        	            		root.current_barcode_can_use = false;
        	            		$("#lead_product_warn_window").data("kendoWindow").open().center();
        	            	}else{
        	            		/* 1.2.2 是引进产品或者是自己新增的产品，则进行正常录报告流程 */
        	            		root.current_barcode_can_use = true;
        	            		//root.initPageData(productId);
        	            	}
        	             }
        	         }
        	     });
        	},
    root.queryProductIdByBarcode = function(barcode){
            	 		/*条形码为空不进行判断*/
            	 		if(barcode==""){
            	 			return;
            	 		}
            	 		var productId = null;
            	 	 	$.ajax({
            	 	         url: fsn.getHttpPrefix() + "/product/query/getProductId/" + barcode,
            	 	         type: "GET",
            	 	         dataType: "json",
            	 	         async: false,
            	 	         success: function(returnValue) {
            	 	             if (returnValue.result.status == "true") {
            	 	             	productId = returnValue.data;
            	 	             }
            	 	         }
            	 	     });
            	 	 	 return productId;
            	 	 };
    root.initPageData = function(productId){
                           //  root.initBarcode = barcode;
                     		$.ajax({
                     	         url: fsn.getHttpPrefix() + "/product/getAllProductsByOrgandId?id="+productId,
                       	         type: "GET",
                       	         dataType: "json",
                       	         success: function(returnValue) {
                       	             	var name = returnValue.product;
                       	             	//console.log(name);
                       	             	if(name == null){
                       	                	/* 1.1 该条形码系统不存在，则引导用户跳转至产品新增界面 */
                       	             		fsn.initNotificationMes("您的列表中不存在该产品！", false);
                       	        			return;
                       	                }
                       	             	else{
                       	             		$("#name").val(name);

                       	            	}
                       	         }

                     	     });
                     };
	$("#save").click(function(){
		var data={};
		data.id=getUrlParam("id");
		data.productID=productId;
		data.sourceArea=$("#sourceArea").val();
		data.sourceDate=$("#sourceDate").val();
		data.processor=$("#processor").val();
		data.packagePlant=$("#packagePlant").val();
		data.warehouseDate=$("#warehouseDate").val();
		data.productDate=$("#productDate").val();
		data.leaveDate=$("#leaveDate").val();
		data.productName=$("#name").val();
		if(data.sourceArea==""){
			lims.initNotificationMes('原材料来源区域不能为空',false);
			return false;
		}
		if(data.sourceDate==""){
			lims.initNotificationMes('原材料入仓时间不能为空',false);
			return false;
		}
		if(data.productDate==""){
			lims.initNotificationMes('生产日期不能为空',false);
			return false;
		}
		if(data.processor==""){
			lims.initNotificationMes('加工者名称不能为空',false);
			return false;
		}
//		if(root.aryBusinessAttachments.length>0==0){
//			lims.initNotificationMes('请上传企业承诺图片',false);
//			return false;
//		}
		
		if(root.aryRepAttachments.length>0){
			console.log(root.aryRepAttachments);
			data.sourceCertifyList=root.aryRepAttachments;
		}
		if(root.aryGeAttachments.length>0){
			data.growEnvironmentResource=root.aryGeAttachments[0];
		}
		if(root.aryByAttachments.length>0){
			data.buyLink=root.aryByAttachments[0];
		}
		if(root.aryBusinessAttachments.length>0){
			data.businessPromiseResource=root.aryBusinessAttachments[0];
		}
		dialog({
			id:"提交中",
			content:"保存中...,请稍等",
			width:350,
			modal:true
		}).show();
		$.ajax({
			url:fsn.getHttpPrefix()+"/traceData/save",
			type: "POST",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(data),
			success:function(rs){
				if(rs.status){
					location.href="list-tracedata.html";
				}else{
					lims.initNotificationMes('保存失败',false);
				}
			}
		});
		return false;
	});
	/**
	 * 初始化原材料证明图片，页面显示
	 */
	
	function getUrlParam(name){  
		//构造一个含有目标参数的正则表达式对象  
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		//匹配目标参数  
		var r = window.location.search.substr(1).match(reg);  
		//返回参数值  
		if (r!=null) return unescape(r[2]);  
		return null;  
	}  
	
	/**
	 * 从页面删除产品图片
	 */
	root.removeRes0 = function(resID){
		root.removeRes(resID, root.aryRepAttachments, "#proAttachmentsListView0", "#logListView0", "#upload-ge-img");
	};
	root.removeRes1 = function(resID){
		root.removeRes(resID, root.aryGeAttachments, "#proAttachmentsListView1", "#logListView1", "#upload-ge-img");
	};
	root.removeRes2 = function(resID){
		root.removeRes(resID, root.aryByAttachments, "#proAttachmentsListView2", "#logListView2", "#upload-by-img");
	};
	root.removeRes3 = function(resID){
		root.removeRes(resID, root.aryBusinessAttachments, "#proAttachmentsListView3", "#logListView3", "#upload-business-img");
	};
	root.removeRes = function(resID, attmachmentss, attachmentsListView, logListView, id){
		 var dataSource = new kendo.data.DataSource();
		 for(var i=0; i<attmachmentss.length; i++){
	   	 if(attmachmentss[i].id == resID){
	   		 while((i+1)<attmachmentss.length){
	   			attmachmentss[i] = attmachmentss[i+1];
	   			 i++;
	   		 }
	   		attmachmentss.pop();
	   		 break;
	   	 }
	    }
		 
		 if(attmachmentss.length>0){
			for(i=0; i<attmachmentss.length; i++){
				dataSource.add({attachments:attmachmentss[i]});
			}
		 }
		 $(attachmentsListView).data("kendoListView").setDataSource(dataSource);
			if(attmachmentss.length == 0){
				$(id).data("kendoUpload").enable();
				$(logListView).hide();
		 }
	};
	
	root.initialize();
});
