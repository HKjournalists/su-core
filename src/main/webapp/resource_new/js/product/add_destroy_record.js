$(document).ready(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {};
	var root = window.lims.root = window.lims.root || {};
	var upload = fsn.upload = fsn.upload || {};
	var proname;
	portal.codeFlag = true;
/*	var describe = [ { text: "国家强制召回", Value: "compulsory_recall" },   
	                  { text: "产品临期", Value: "advent" },  
	                  { text: "其他", Value: "other" }  
	               ];*/ 
	root.aryRepAttachments=new Array();
	root.initialize=function(){
	    portal.initPopup("addConfirmPopup","确认");
    	fsn.initKendoWindow("k_window","保存状态","300px","60px",false,'[]');
		root.bindClick_dealer();
		 $("#barcode").kendoAutoComplete({
	            dataSource: lims.getAutoLoadDsByUrl("/product/getAllBarCode?businessType=经销商"),
	            filter: "startswith",
	            placeholder: "搜索...",
	            select: root.onSelectBarcode,
	        });
	/*	$("#problem_describe").kendoDropDownList({
			dataSource:describe,
			dataTextField: "text",
			dataValueField: "Value"
		});*/
		
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
				$("#format").val(rs.productDestroyRecord.format);
				$("#batch").val(rs.productDestroyRecord.batch);
				$("#number").val(rs.productDestroyRecord.number);
				$("#problem_describe").val(rs.productDestroyRecord.problem_describe);
				$("#process_mode").val(rs.productDestroyRecord.process_mode);
				$("#deal_address").val(rs.productDestroyRecord.deal_address);
				$("#deal_person").val(rs.productDestroyRecord.deal_person);
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
	portal.initPopup = function(id,title) {
                		var window = $("#"+id);
                		if (!window.data("kendoWindow")) {
                			window.kendoWindow({
                				title:title ,
                				modal:true
                				/*visible : false,
                				resizable : false*/
                			});
                		}
                		window.data("kendoWindow").center();
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
	 * 根据条形码查找产品id
	 * @author ZhangHui 2015/4/14
	 */
	root.countInitialProduct = function(productId){
	 	var count = "";
	  	$.ajax({
	          url: fsn.getHttpPrefix() + "/erp/initializeProduct/countInitialProduct/" + productId,
	          type: "GET",
	          dataType: "json",
	          async: false,
	          success: function(returnValue) {
	              if (returnValue.result.status == "true") {
	            	  count = returnValue.counts;
	              }
	          }
	      });
	  	 return count; 
	};
	/**
	 * 判断该条形码对应的产品,是否是系统中已存在，或者是否已引进
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
        }
    	/* 1.2 验证该产品是否为本经销商引进产品 */
    	var count = root.countInitialProduct(productId);
    	if(count == 0){
    		/* 1.2.1 系统中存在，但是列表中没有*/
    		fsn.initNotificationMes("您的列表中没有该产品！", false);
    		
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
    		$("#format").val("");
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
	/**
	 * 初始化页面数据
	 * @author ZhangHui 2015/4/30
	 */
    root.initPageData = function(productId){
      //  root.initBarcode = barcode;
		$.ajax({
	         url: fsn.getHttpPrefix() + "/product/" + productId,
	         type: "GET",
	         dataType: "json",
	         success: function(returnValue) {
	        	 $("#name").val(returnValue.data.name);
	        	 $("#format").val(returnValue.data.format);
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

                				if($("#batch").val()==""){
                                    fsn.initNotificationMes("批次不能为空！", false);

                        			return false;
                        		}
                        		if($("#barcode").val()==""){
                        			lims.initNotificationMes('条形码不能为空',false);
                        			return false;
                        		}
                        		if($("#name").val()==""){
                        			lims.initNotificationMes('产品名称不能为空',false);
                        			return false;
                        		}
                        		if($("#number").val()==""){
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
                        		if($("#problem_describe").val()==""){
                                    fsn.initNotificationMes("处理原因不能为空！", false);
                        			return false;
                        		}
                        		if($("#deal_address").val()==""){
                                    fsn.initNotificationMes("处理地点不能为空！", false);
                                    return false;
                                }
                                if($("#deal_person").val()==""){
                                    fsn.initNotificationMes("处理人不能为空！", false);
                                    return false;
                                }
                        		if($("#process_time").val()==""){
                        			lims.initNotificationMes('处理时间不能为空',false);
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
                        		if(root.aryRepAttachments.length<1){
                        			lims.initNotificationMes('处理证明不能为空',false);
                        			return false;
                        		}
                		 root.openConfirmWin();
    	});
    	 root.saveProcurement = function(){
                	 };

         root.openConfirmWin=function () {
                		$("#barcode_y").html($("#barcode").val());
                		$("#name_y").html($("#name").val());
                		$("#format_y").html($("#format").val());
                		$("#batch_y").html($("#batch").val());
                		$("#number_y").html($("#number").val());
                		$("#problem_describe_y").html($("#problem_describe").val());
                		$("#process_time_y").html($("#process_time").val());
                		$("#deal_address_y").html($("#deal_address").val());
                		$("#deal_person_y").html($("#deal_person").val());
                		$("#remark_y").html($("#remark").val());
                		var slides = $("#slides1");
                		var img ="<div class=\"slides_container\">";
                		for(var i=0;i<root.aryRepAttachments.length; i++)
                		{
                			img =img+ '<div class="slide"><img style="width: 339px;height: 360px" src="data:image/png;base64,'+root.aryRepAttachments[i].fileBase64+'"/></div>';

                		}
                		img =img+ '</div><a href="#" class="prev"><img src="../../resource/js/slides/img/arrow-prev.png" width="24" height="43" alt="Arrow Prev"></a>'+
                			'<a href="#" class="next"><img src="../../resource/js/slides/img/arrow-next.png" width="24" height="43" alt="Arrow Next"></a>';
                		slides.html(img);
                		$("#slides1").slides();


                		$("#addConfirmPopup").data("kendoWindow").open();
                	} ;

         $("#saveConfirm").click(function(){
            $("#k_window").data("kendoWindow").open().center();
    		var data={};
    		data.id=getUrlParam("id");
    		data.name=$("#name").val();
    		data.barcode=$("#barcode").val();
    		data.format=$("#format").val();
    		data.batch=$("#batch").val();
    		data.number=$("#number").val();
    		data.handle_name=null;
    		data.problem_describe=$("#problem_describe").val();
    		data.process_mode=2;
    		data.process_time=$("#process_time").val();
    		data.deal_person=$("#deal_person").val();
    		data.deal_address=$("#deal_address").val();
    		data.recieve_name=null;
    		data.record_id=null;
    		data.operation_user=null;
    		data.remark=$("#remark").val();
    		data.recAttachments=root.aryRepAttachments;
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
    		});
    		return false;
            });
         $("#cancelConfirm").click(function(){
        		$("#addConfirmPopup").data("kendoWindow").close();
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
