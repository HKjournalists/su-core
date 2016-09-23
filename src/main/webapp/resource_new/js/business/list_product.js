$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var product = upload.product = upload.product || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	var deleteItem;
	product.templatecolumns = [
					{ command: [{
						name: "Check",
					    text: "<input type='checkbox' class='checkboxes'  />"
					},
					],  width: 40 },
	                 {field: "id",title: lims.l("Id"),width: 40,filterable: false},
                     {field: "name",title: lims.l("Name"),width: 60,filterable: false},
                     {field: "businessBrand.name",title: lims.l("Owned Business Brand"),width: 60,filterable: false},
                     {field: "format",title: lims.l("Format"),width: 50,filterable: false},
                     {field: "barcode",title: lims.l("Barcode"),width: 40,filterable: false},
                     {field: "cstm",title: lims.l("Appropriate Crowd"),width: 40,filterable: false},
                     {field: "ingredient",title: lims.l("Ingredient"),width: 100,filterable: false},
                     { command: [{
                                	  name:"edit",
    			                      text: lims.localized("Edit"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.location.href = portal.CONTEXT_PATH + "/views/business/product.html?id=" + temp.id;
									  }
								  },
					            {
								  name: "Delete",
			                      text: "<span class='k-icon k-cancel'></span>"+ lims.l("Delete"),
					          	  click: function(e){
					          		lims.clrmsg("msg_success",null);
					          		var deleteRow = $(e.target).closest("tr");
					          		deleteItem = this.dataItem(deleteRow);
					          		lims.initConfirmWindow(function() {
					        			lims.clrmsg("msg_success",null);
					        	      		  $.ajax({
					        	    					url:portal.HTTP_PREFIX + "/portal/upload?type=product&id_list=:"+deleteItem.productId,
					        	    					type:"DELETE",
					        	    					success:function(data){
					        	    						if(data.RESTResult.status == 1){
					        	    							$("#product").data("kendoGrid").dataSource.remove(deleteItem);
					        	    							lims.message("msg_success", true,'"<span class="font_blue_underline">'+deleteItem.productName+'</span>"' + lims.l("Delete Success"));
					        	    						}else{
					        	    							var result={success:false};
					        	    							lims.message("msg_success", result, '"<span class="font_red_underline">'+deleteItem.productName+'</span>"' + lims.l("Delete Failed"));
					        	    						}
					        	    					}
					        						});	  		
					        			lims.closeConfirmWin();
					        	}, undefined,lims.l("WIN_CONFIRM_MSG"),true);
						          	$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
					          	  }
					            }
					            ], title:lims.l("Operation"), width: 70 }
                     ];
	product.templateDS = function(keyword){
		return new kendo.data.DataSource({
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
	        		 return returnValue.data.listOfBrand;;  //响应到页面的数据
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
	};
	product.initialize = function(){
		var dataSource = this.templateDS("");
		upload.buildGrid("product",this.templatecolumns, dataSource);
		//upload.addCheckBox();
		$('#search').kendoSearchBox({
			change:function(e){
				var keyword = e.sender.options.value;
				//if(keyword.trim().length){
				product.searchTemplateByKeywords("product", keyword);
				//}
			}
		}); //Search Box
		
		$("#product .k-grid-content").attr("style","height: 364px");
		//$("#templates").data("kendoGrid").refresh();
	};
	product.searchTemplateByKeywords = function(id, keyword){
		console.log("keywords :" + keyword);
		var ds = product.templateDS(keyword);
		$("#" + id).data("kendoGrid").setDataSource(ds);
		//upload.addCheckBox();
	};
	product.createNewTemp = function(){
		window.location.pathname = "/lims-core/views/process/template_management.html";
	};
	product.dataFormat=function(list){
		var productList=[];
		for(var i=0;i<list.length;i++){
			var product_obj={};
			product_obj["productId"]=list[i].id;
			product_obj["productName"]=list[i].name;
			product_obj["ownedBrand"]=list[i].businessBrand?list[i].businessBrand.name:'';
			product_obj["productFormat"]=list[i].format;
			product_obj["productBarcode"]=list[i].barcode;
			product_obj["productAppropriateCrowd"]=list[i].cstm;
			product_obj["productIngredient"]=list[i].ingredient;
			productList.push(product_obj);
		}
		return productList;
	};
	product.initialize();
	$("#main").on("click","#batchDelete",function(){
		var _productIds="",_checkItem=$("input.checkboxes:checked");
		if(_checkItem.length==0){
			alert(lims.l("Choose Product"));
		}else{
			$("input.checkboxes:checked").each(function(){
				_productIds+=":"+$(this).closest("td").next("td").text();
			});
			lims.initConfirmWindow(function() {
					lims.clrmsg("msg_success",null);
		      		$.ajax({
		      			url:portal.HTTP_PREFIX + "/portal/upload?type=product&id_list="+_productIds,
    					type:"DELETE",
    					success:function(data){
    						if(data.RESTResult.status == 1){
    							product.searchTemplateByKeywords("product", $("#search").val());
    							lims.message("msg_success", true, '"<span class="font_blue_underline">'+lims.l("Selected Product")+'</span>"' + lims.l("Delete Success"));
    						}else{
    							var result={success:false};
    							lims.message("msg_success", result, '"<span class="font_red_underline">'+lims.l("Selected Product")+'</span>"' + lims.l("Delete Failed"));
    						}
    					}
		      			});	  		
					lims.closeConfirmWin();
			}, undefined,lims.l("WIN_BATCHCONFIRM_MSG"),true);
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		};
	});
});