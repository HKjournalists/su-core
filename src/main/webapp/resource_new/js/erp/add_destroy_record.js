$(document).ready(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {};
	var root = window.lims.root = window.lims.root || {};
	var upload = fsn.upload = fsn.upload || {};
	var proname;
	portal.codeFlag = true;
	var describe = [ { text: "国家强制召回", Value: "compulsory_recall" },   
	                  { text: "产品临期", Value: "advent" },  
	                  { text: "其他", Value: "other" }  
	               ]; 
	root.aryRepAttachments=new Array();
	root.initialize=function(){
		/*$("#name").kendoDropDownList({
			dataSource:{
				transport: {
					read: {
						url:
							fsn.getHttpPrefix()+"/product/getListOfProduct",
						dataType: "json"
					}
				},
				schema: {
					data: function(response) {
						return response.productList; 
					}
				},
				
			},
			dataBound: function(e) {
				$("#barcode").val(e.sender.dataItem().barcode);
			  },
			change:function(e){
				$("#barcode").val(e.sender.dataItem().barcode);
				//bar=e.sender.dataItem().barcode;
			},
			dataTextField: "name",
			dataValueField: "name"
		});
		*/
		root.bindClick_dealer();
		 $("#barcode").kendoAutoComplete({
	            dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode"),
	            filter: "startswith",
	            placeholder: "搜索...",
	            select: root.onSelectBarcode,
	        });
		$("#problem_describe").kendoDropDownList({
			dataSource:describe,
			dataTextField: "text",
			dataValueField: "Value"
		});
		
		$("#process_time").kendoDatePicker({
			format: "yyyy-MM-dd",
			height:30,
			max: new Date(),
			culture : "zh-CN"
		});
		/* 初始化上传控件  */
		$("#upload-other-img").kendoUpload({
			async: {
				saveUrl: portal.HTTP_PREFIX + "/resource/kendoUI/addResources/product",
				removeUrl: portal.HTTP_PREFIX + "/resource/kendoUI/removeResources",
				autoUpload: true,
				removeVerb:"POST",
				removeField:"fileNames",
				saveField:"attachments"
			},localization: {
				select: "上传处理证明",
				retry:lims.l("retry",'upload'),
				remove:lims.l("remove",'upload'),
				cancel:lims.l("cancel",'upload'),
				dropFilesHere: lims.l("drop files here to upload",'upload'),
				statusFailed: lims.l("failed",'upload'),
				statusUploaded: lims.l("uploaded",'upload'),
				statusUploading: lims.l("uploading",'upload'),
				uploadSelectedFiles: lims.l("Upload files",'upload')
			},
			multiple:true,
					upload: function(e){
						var files = e.files;
						if(root.aryRepAttachments.length+e.files.length>5){
							return false;
						}
						$.each(files, function () {
							if(this.size>1040000){
								lims.initNotificationMes('单个文件的大小不能超过1M！',false);
								e.preventDefault();
								return;
							}
							var extension = this.extension.toLowerCase();
							if(extension != ".png" && extension != ".bmp" && extension != ".jpeg"&&extension != ".jpg"){
								lims.initNotificationMes('图片格式错误,请上传 .png .bmp .jpeg .jpg类型图片!',false);
								e.preventDefault();
							}
						});
					},
					success: function(e){
						if(e.response.typeEror){
							lims.initNotificationMes("必须上传图片格式",false);
							return;
						}
						if(e.response.morSize){
							lims.initNotificationMes("上传材料大小必须小1M",false);
							return;
						}
						if (e.operation == "upload") {
							root.aryRepAttachments.push(e.response.results[0]);
							console.log(root.aryRepAttachments);
							//$("#"+msg).html("文件识别成功，可以保存!</br>(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
						}else if(e.operation == "remove"){
							for(var i=0; i<root.aryRepAttachments.length; i++){
			                   	 if(root.aryRepAttachments[i].name == e.files[0].name){
			                   		 while((i+1)<root.aryRepAttachments.length){
			                   			root.aryRepAttachments[i]=root.aryRepAttachments[i+1];
			                   			 i++;
			                   		 }
			                   		root.aryRepAttachments.pop();
			                   		 break;
			                   	 }
			                    }
							console.log(root.aryRepAttachments);
						}
					},
					remove:function(e){
						//$("#"+msg).html("(注意：为保证更流畅的体验，建议每次上传照片不超过1M!可支持文件格式：png .bmp .jpeg .jpg )");
					},
					error:function(e){
						//$("#"+msg).html(lims.l("An exception occurred while uploading the file! Please try again later ..."));
					}
		});
		var id=getUrlParam("id");
		if(id!=null){
			$.get(fsn.getHttpPrefix()+"/product/getDestroyById",{id:id},function(rs){
				$("#name").val(rs.productDestroyRecord.name);
				$("#barcode").val(rs.productDestroyRecord.barcode);
				$("#batch").val(rs.productDestroyRecord.batch);
				$("#number").val(rs.productDestroyRecord.number);
				$("#problem_describe").data("kendoDropDownList").value(rs.productDestroyRecord.problem_describe);
				$("#process_mode").val(rs.productDestroyRecord.process_mode);
				$("#process_time").val(fsn.formatGridDate(rs.productDestroyRecord.process_time));
				if(rs.productDestroyRecord.recAttachments){
					var html='';
					for(var i in rs.productDestroyRecord.recAttachments){
						html+='<p><a href="'+rs.productDestroyRecord.recAttachments[i].url+'" target="_blank" role="option" >查看处理证明</a>';
						html+='<button class="k-button" onclick="return removeRes()"> <span class="k-icon k-cancel">删除</span></button></p>';
					}
					$("#repAttachmentsListView").show().html(html);
					root.aryRepAttachments=rs.productDestroyRecord.recAttachments;
					$("#upload-other-img").data("kendoUpload").disable();
				}
				$("#check").style.display("inline");
			},'json');
		}
	};
	/**
	 * 根据条形码查找产品id
	 * @author ZhangHui 2015/4/14
	 */
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
	}
	 	/**
	 	 * 根据条形码查找产品id
	 	 * @author ZhangHui 2015/4/14
	 	 */
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
	
	/**
	 * 判断该条形码对应的产品,是否是系统中已存在
	 * @author ZhangHui 2015/4/30
	 */
	root.judgeProductByBarcode = function(barcode){
		/*条形码为空不进行判断*/
		if(barcode==""){
			return;
		}
		root.queryCheckByBarcode(barcode);
		/* 1 验证产品条形码是否为系统已存在产品 */
        var productId = root.queryProductIdByBarcode(barcode);
        if(productId == null){
        	/* 1.1 系统中不存在 */
        	fsn.initNotificationMes("系统中不存在该产品！", false);
			return;
    	
    	}else{
    		/* 1.2.2 是引进产品或者是自己新增的产品，则进行正常录报告流程 */
    		root.current_barcode_can_use = true;
    		root.initPageData(productId);
    	}
	};
    /**
     * 经销商报告录入界面，Click事件绑定
     * @author ZhangHui 2015/4/29
     */
    root.bindClick_dealer = function(){
    	/**
         * 定义产品条形码失去焦点事件
         * @author ZhangHui 2015/4/30
         */
    	//=====================开始=======================
    	$("#barcode").blur(function() {
        	var barcode = $("#barcode").val();

    		$("#name").val("");
        	/*条形码为空不进行判断*/
        	if(barcode==""){
        		return;
        	}
    		 root.judgeProductByBarcode(barcode);
    		
    	});
    	//=====================结束=======================
    	
    	/**
    	 * 选中下拉框中的条形码事件
    	 * @author ZhangHui 2015/4/14
    	 */
    	root.onSelectBarcode = function(e){
    		 root.initBarcode = this.dataItem(e.item.index());
    		 $("#barcode").val(root.initBarcode);
    		 root.judgeProductByBarcode(root.initBarcode);
    		// portal.codeFlag = false;
    	};
    	
        
    };
    root.initPageData = function(productId){
        //  root.initBarcode = barcode;
  		$.ajax({
  	         url: fsn.getHttpPrefix() + "/report/getProductById?proId=" + productId,
  	         type: "GET",
  	         dataType: "json",
  	         success: function(returnValue) {
  	             	var name = returnValue.product;
  	             	console.log(name);
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
	
	removeRes=function(){
		$("#repAttachmentsListView").remove();
		$("#upload-other-img").data("kendoUpload").enable();
		delete root.aryRepAttachments[0];
		root.aryRepAttachments.length=0;
	},

	$("#save").click(function(){
		var data={};
		data.id=getUrlParam("id");
		data.name=$("#name").val();
		data.barcode=$("#barcode").val();
		data.batch=$("#batch").val();
		data.number=$("#number").val();
		data.handle_name=null;
		data.problem_describe=$("#problem_describe").data("kendoDropDownList").value();
		data.process_mode=2;
		data.process_time=$("#process_time").val();
		data.recieve_name=null;
		data.record_id=null;
		data.operation_user=null;
		if(data.batch==""){
            fsn.initNotificationMes("批次不能为空！", false);
			return false;
		}
		if(data.barcode==""){
			lims.initNotificationMes('条形码不能为空',false);
			return false;
		}
		if(data.name==""){
			lims.initNotificationMes('产品名称不能为空',false);
			return false;
		}
		if(data.number==""){
			lims.initNotificationMes('处理数量不能为空',false);
			return false;
		}else{
			var expirday = $("#number").val();
	        var re1 = /^[1-9]{1}$/;
	        if (!expirday.charAt(0).match(re1) && expirday.trim() != "") {
	            $("#number").val("");
	            lims.initNotificationMes('处理数量必须以数字开始',false);
	            return false;
	        }
		}
		
		if(data.process_time==""){
			lims.initNotificationMes('处理日期不能为空',false);
			return false;
		}else{
			var expirday = $("#process_time").val();
	        var re1 = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	        if (!expirday.match(re1) && expirday.trim() != "") {
	            $("#process_time").val("");
	            lims.initNotificationMes('处理日期格式不正确',false);
	            return false;
	        }
		}
		if(root.aryRepAttachments.length>0){
				data.recAttachments=root.aryRepAttachments;			
		}else{
			lims.initNotificationMes('处理证明不能为空',false);
			return false;
		}
		if(confirm("该数据保存之后不可更改，确定要保存这条数据吗？")){
		/*dialog({
			id:"提交中",
			content:"保存中...,请稍等",
			width:350,
			modal:true
		}).show();*/
		$.ajax({
			url:fsn.getHttpPrefix()+"/product/savedestroy",
			type: "POST",
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(data),
			success:function(rs){
				if(rs.status){
					location.href="list_destroy_record.html";
				}else{
					lims.initNotificationMes('保存失败',false);
				}
			}
		});}
		return false;
	});
	function getUrlParam(name){  
		//构造一个含有目标参数的正则表达式对象  
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		//匹配目标参数  
		var r = window.location.search.substr(1).match(reg);  
		//返回参数值  
		if (r!=null) return unescape(r[2]);  
		return null;  
	}  
	root.initialize();
})
