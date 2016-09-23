$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var upload = fsn.upload = fsn.upload || {};
	var portal = fsn.portal = fsn.portal || {};
	var product = upload.product = upload.product || {};
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	portal.edit_id = null;
	
	//判断cookie不为空时设置为空
	 try {
		portal.edit_id = $.cookie("user_0_edit_product").id;
		if(portal.edit_id!=null){
			$.cookie("user_0_edit_product", JSON.stringify({}), {
				path : '/'
			});
		}
    } catch (e) {
    }
    
	product.initialize = function(){
		upload.buildGrid("product", this.templatecolumns, this.templateDS);
		$("#product .k-grid-content tr td:first-child a").removeAttr("class");
		$("#product .k-grid-content").attr("style","height: 364px");
	};
	
	product.templatecolumns = [
	            {field: "id",title: fsn.l("Id"),width: 25},
                {field: "name",title: fsn.l("Name"),width: 60},
                {field: "brandName",title: fsn.l("Owned Business Brand"),width: 40},
                {field: "format",title: fsn.l("Format"),width: 40},
                {field: "barcode",title: fsn.l("Barcode"),width: 40},
                {field: "cstm",title: fsn.l("Appropriate Crowd"),width: 40},
                {field: "ingredient",title: fsn.l("Ingredient"),width: 30},
                {field: "nutriStatus",title: fsn.l("Nutrition Index Status"),width: 50 , template:function(item){
                	var status = item.nutriStatus;
                	return  (status == "2" ? "失败" : (status == "1" ? "成功" : "未计算"));
                }},
                {width:55,title: fsn.l("Operation"),
				      template:function(e){
						ids = e.id;
						var tag="<a  onclick='return fsn.upload.product.edit("+e.id+","+e.packageFlag+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> </span>" + fsn.l('Edit') + "</a>";
					    if (e.nutriStatus == '0' || e.nutriStatus=='2') {
							return tag  +
							"<a onclick='return fsn.upload.product.recalculatedOnClick("+e.id+","+e.nutriStatus+")' class='k-button k-button-icontext k-grid-Backout' id='aid' >重新计算</a>";
						}else{
							return tag;
						}
						return tag;
					}
			    }];
	
       	product.templateDS = new kendo.data.DataSource({
       			transport: {
       	            read : {
       	                type : "GET",
       	                async:false,
       	                url : function(options){
       	                	var configure = null;
       	                	if(options.filter){
       	            			configure = filter.configure(options.filter);
       	            		}
       	                	return portal.HTTP_PREFIX + "/product/getProducts/" + configure + "/" + options.page + "/" + options.pageSize;
       	                },
       	                dataType : "json",
       	                contentType : "application/json"
       	            },
       	        },
       	        schema: {
       	        	 data : function(returnValue) {
       	        		 return returnValue.data.listOfProduct;  //响应到页面的数据
       	             },
       	             total : function(returnValue) {
       	            	 return returnValue.data.counts;   //总条数
       	             }         
       	        },
       	        batch : true,
       	        page:1,
       	        pageSize : 10, //每页显示个数
       	        serverPaging : true,
       	        serverFiltering : true,
       	        serverSorting : true
       		});
	
	// 编辑
	product.edit = function(id,packageFlag){
		/* md5加密pid */ 
		var canshu = "?"+id+"&"+$.md5(""+id);
	
		/**
	 	 * 产品的包装标志：0：预包装、1：散装、2：无条码
		 * 根据产品的不同跳转到不同的编辑界面
	 	*/
		if(packageFlag=='0'){
			window.location.href = "/fsn-core/views/portal/product.html"+canshu;
		}else{
			window.location.href = "/fsn-core/views/portal/add-qrcode-product.html"+canshu;
		}
	};
	
	product.createNewTemp = function(){
		window.location.pathname = "/lims-core/views/process/template_management.html";
	};
	
	product.dataFormat=function(list){
		var productList=[];
		for(var i=0;i<list.length;i++){
			var product_obj={};
			
			var _date = lims.fmtMsDate(new Date());
			var datediff = new Date(_date) - new Date(list[i].license_end);
			
			if(list[i].verifyStatus != "审核通过"){
				//证过期该行加粗
				if(datediff > 0){
					for(var key in list[i]){
						list[i][key] = list[i][key]==null?"":list[i][key];
						list[i][key] = '<b>'+list[i][key]+'</b>';
					}
				}
				if(list[i].verifyStatus == "待审核"){
					list[i].verifyStatus = '<span style="color:#FFD100">'+list[i].verifyStatus+'</span>';
				}else if(list[i].verifyStatus == "退回"){
					list[i].verifyStatus = '<span style="color:#FF0000">'+list[i].verifyStatus+'</span>';
				}
			}
			
			product_obj["productId"]=list[i].id;
			product_obj["productName"]=list[i].name;
			product_obj["productCategory"]=list[i].category;
			product_obj["productLicenseId"]=list[i].licenseNo;
			product_obj["productLicenseStart"]=list[i].licenseStart;
			product_obj["productLicenseEnd"]=list[i].license_end;
			product_obj["productProduceStatus"]=list[i].manuStatus;
			product_obj["productRegularity"]=list[i].regularity;
			product_obj["productRegularityCat"]=list[i].regularityCat;
			product_obj["productStatus"]=list[i].verifyStatus;
			productList.push(product_obj);
		}
		return productList;
	};
	
	
	$("#main").on("click","#batchDelete",function(){
		var _productIds="",_checkItem=$("input.checkboxes:checked");
		if(_checkItem.length==0){
			alert(fsn.l("Choose Product"));
		}else{
			$("input.checkboxes:checked").each(function(){
				var _id = $(this).closest("td").next("td").text();
				if(!$.isNumeric(_id)){
					_id = _id.replace(/<.*?>/g,"");
				  }
				_productIds+=_id+",";
			});
			lims.initConfirmWindow(function() {
					lims.clrmsg("msg_success",null);
		      		$.ajax({
		      			url:portal.HTTP_PREFIX + "/portal/product?ids="+_productIds,
    					type:"DELETE",
    					success:function(data){
    						if(data.RESTResult.status == 1){
    							lims.message("msg_success", true, '"<span class="font_blue_underline">'+fsn.l("Selected Product")+'</span>"' + fsn.l("Delete Success"));
    						}else{
    							var result={success:false};
    							lims.message("msg_success", result, '"<span class="font_red_underline">'+fsn.l("Selected Product")+'</span>"' + fsn.l("Delete Failed"));
    						}
    					}
		      			});	  		
					lims.closeConfirmWin();
			}, undefined,fsn.l("WIN_BATCHCONFIRM_MSG"),true);
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		};
	});
	
	/*
	 * 重新计算营养成分指数 
	 * @author tangxin
	 */
	function recalculatedNutri(productID){
		if(productID == null){
			return;
		} 
		$.ajax({
			url:portal.HTTP_PREFIX + "/product/recalculatedNutri/" + productID,
			type:"PUT",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			success:function(data){
				$("#product").data("kendoGrid").dataSource.read();
				if(data.result.status == "true"){
					fsn.initNotificationMes("计算成功！",true);
				}else{
					message = data.result.message;
					fsn.initNotificationMes( (message!= null && message !="") ? message : "计算失败，后台出现异常！",false);
				}
			}
		});
	}

	/*
	 * 重新计算营养指数的click事件 
	 * @author tangxin
	 */
	product.recalculatedOnClick= function(productId,nutriStatus){
		if(productId == null){
			return;
		}
	  if(nutriStatus == '1') {
		fsn.initConfirmWindow(function() {
			fsn.clrmsg("msg_success",null);
			recalculatedNutri(product.id);
			fsn.closeConfirmWin();
		}, undefined,fsn.l("WIN_RECALCULATEDCONFIRM_MSG"),true);
		$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
	  }else{
		/**
		 * 重新计算营养成分指数
		 */
		recalculatedNutri(productId);
	  }
	};
	
	product.initialize();
});