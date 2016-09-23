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
                {field: "productName",title: fsn.l("Name"),width: 60},
                {width:55,title: fsn.l("Operation"),
				      template:function(e){
						ids = e.id;
						var tag= "<a  onclick='return fsn.upload.product.edit("+e.id+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-edit'> </span>" + fsn.l('Edit') + "</a>";
						//tag+="<a onclick='return fsn.upload.product.delete("+e.id+")' class='k-button k-button-icontext k-grid-Backout' >删除</a>";
						return tag;
					}
			    }];
	
       	product.templateDS = new kendo.data.DataSource({
       			transport: {
       	            read : {
       	                type : "GET",
       	                async:false,
       	                url : function(options){
       	                	var configure = '';
       	                	if(options.filter){
       	            			configure = '?filter='+filter.configure(options.filter);
       	            		}
       	                	return portal.HTTP_PREFIX + "/mapProduct/list" + configure;
       	                },
       	                dataType : "json",
       	                contentType : "application/json"
       	            },
       	        },
       	        schema: {
       	        	 data : function(returnValue) {
       	        		 return returnValue.data.listOfModel;  //响应到页面的数据
//       	        		 return returnValue.data.listOfModel;  //响应到页面的数据
       	             },
       	             total : function(returnValue) {
       	            	 return returnValue.data.count;   //总条数
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
	product.edit = function(id){
		window.location.href = "/fsn-core/views/portal/add-mapProduct.html?id="+id;
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
	
	product.initialize();
});