$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	
	eWidget.initGrid = function(id,columns,width,height,ds,template){
		var grid = $("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
					filterable: {
						extra: false,
						messages: {
							info: "显示需要项目:",
							and: "并且",
							or: "或者",
							filter: "过滤",
							clear: "清空"
						},
					 	operators: {
					      string: {
					    	  contains: "包含",
					    	  doesnotcontain: "不包含",
					    	  startswith: "以开头",
					    	  endswith: "以结尾",
					    	  eq: "等于",
					    	  neq: "不等于"
					      },
					      number: {
					    	  	eq: "等于",
					    		neq: "不等于",
					    		gte: "大于或等于",
					    		gt:  "大于",
					    		lte: "小于或等于",
					    		lt:  "小于",
					      }
					    }
				    },  
			height: height == null ?428:height,
	        width: width == null ? "auto":width,
	        sortable: true,
	        selectable: true,
	        resizable: false,
			toolbar: [
			          {template: kendo.template($("#" + template).html())}
			         ],
	        pageable: {
	            refresh: true,
	            pageSizes: [10, 20, 100],
	            messages: fsn.gridPageMessage(),
	        },
	        columns: columns
		});
		return grid;
	};
	
	eWidget.getWindow = function(id, prefix,width){
		$("#label_id").html(prefix + "ID");
		$("#label_name").html(prefix + "名称");
		var temp_win = $("#" + id).kendoWindow({
							width:width == null ? 450:width,					
							visible:false,
							title:"查看详细信息",
							modal:true,
						});
		return temp_win;
	};
	
	//提示框
	eWidget.getPromptWindow = function(){
		var temp_win = $("#PromptWindow").kendoWindow({
							width:450,
							height:100,
							title:"删除提示框",
							visible:false,
							modal:true
						});
		return temp_win;
	};
	
	//提示框
	eWidget.getPromptWindow1 = function(){
		var temp_win = $("#PromptWindowConfirm").kendoWindow({
							width:450,
							height:120,
							title:"确认提示框",
							visible:false,
							modal:true
						});
		return temp_win;
	};
	//输入校验的相关方法 1正数校验
	eWidget.verificationNum = function(id){
		var verify = $("#"+id).kendoNumericTextBox({
		    min: 0,
		    placeholder: "不能输入小于0的数",
		    format:"#.##"
		});
		return verify;
	};
	
	//查询供货商
	eWidget.getDropDownListProvider = function(id, type){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/findProvider";
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 id:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});;
			
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择供货商",
            suggest: true
        });
		return temp_DropDownList;
	};
	
	//查询商品
	eWidget.getComboBoxProduct = function(id, type,num){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/findProduct?num=" + num;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 no:{type:"string"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }  
	        }
		});
		
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "name",
            dataValueField: "no",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择商品",
            suggest: true,
            change:function(e){
            	var dataItem = $("#" + id).data("kendoDropDownList").dataItem();
            	console.log(dataItem.id);
            	if(dataItem.name != "请选择商品"){
            		$.ajax({
    					url: fsn.getHttpPrefix() + "/erp/outOfGoods/getProductById?id="+dataItem.id,
    					type:"GET",
    					dataType:"json",
    					async: false,
    					contentType: "application/json; charset=utf-8",
    					success:function(data){
    						$("#unit").attr({"value":data.result.unit.id});
    	                	$("#unit").val(data.result.unit.name);
    	                	$("#spec").val(data.result.specification);
    	                	if(data.result.mode == true||data.result.mode == "true"){
    	        				$("input[name='ismode'][value='false']").attr("disabled",true);
    	        			}else{
    	        				$("input[name='ismode'][value='true']").attr("disabled",true);
    	        			}
    	                	$("input[name='ismode'][value="+data.result.mode+"]").attr("checked",true); 
    	                	if(data.result.inspectionReport == true||data.result.inspectionReport == "true"){
    	        				$("input[name='isreport'][value='false']").attr("disabled",true);
    	        			}else{
    	        				$("input[name='isreport'][value='true']").attr("disabled",true);
    	        			}
    	                	$("input[name='isreport'][value="+data.result.inspectionReport+"]").attr("checked",true);
    	                	 if(data.result.hasInspectionReport == true||data.result.hasInspectionReport == "true"){
    	         		    	$("input[name='ishasreporter'][value='false']").attr("disabled",true);
    	         		    }else{
    	         		    	$("input[name='ishasreporter'][value='true']").attr("disabled",true);
    	         		    }
    	                	$("input[name='ishasreporter'][value="+data.result.hasInspectionReport+"]").attr("checked",true);
    	                	$("#rnum").val(data.result.outNumber);
    	                	$("#singerprice").val(data.result.unitPrice);
    	                	$("#batch").val(data.result.batchNo);
    					}
    				});
            		
            	}else{
                	$("#unit").val("");
                	$("#spec").val("");
                	$("#rnum").val("");
                	$("#batch").val("");
                	$("#singfsnrice").val("");
            	}
            }
        });
		return temp_DropDownList;
	};
	
	//查询仓库
	eWidget.getDropDownListStorage = function(id, type){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/findStorage";
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 no:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result&& data.result.listOfModel){
	        			return data.result.listOfModel;  
	        		}
	                return null;
	            },
	            total : function(data) {//总条数
	            	if(data && data.result && data.result.count){
	            		return  data.result.count;
	            	}
	            	return 0;
	            }  
	        }
		});
		
		
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "name",
            dataValueField: "no",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择仓库",
            suggest: true
        });
		return temp_DropDownList;
	};
	
	//查询出货单号
	eWidget.getDropDownListOutGood = function(id, type,num){
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/findOutGood?num="+num;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 no:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});
		
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "name",
            dataValueField: "no",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择来源单号",
            suggest: true
        });
		return temp_DropDownList;
	};
	
	//查询供货商联系人
	eWidget.getDropDownListProviderContacts = function(id, type,num){
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/findProviderContact?num="+num;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 id:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});
		
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择联系人",
            suggest: true,
            change:function(){
            	var dataItem = $("#" + id).data("kendoDropDownList").dataItem();
            	if(dataItem.name == "请选择联系人"){
            		$("#contacttel").val("");
    				$("#caddress").val("");
            	}else{
            		$("#contacttel").val(dataItem.tel_1);
    				$("#caddress").val(dataItem.province+dataItem.city+dataItem.area+dataItem.addr);
            	}
            }
        });
		return temp_DropDownList;
	};
	
	//查询单别
	eWidget.getComboBoxOrderType = function(id,type,module,order){
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/"+type+"/findOrderType?module="+module+"&order="+order;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 ot_id:{type:"number"},
	        			 ot_type:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});
		
		var temp_DropDownList = $("#" + id).kendoDropDownList({
            dataTextField: "ot_type",
            dataValueField: "ot_id",
            dataSource: datasource,
            filter: "contains",
            optionLabel: "请选择单别",
            suggest: true
        });
		return temp_DropDownList;
	};
	
	//单位id,类型id,分类id
	eWidget.getComboBox = function(id, type){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/model/"+type+"/lists";
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 id:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result && data.result.listOfModel){
	        			return data.result.listOfModel;  
	        		}
	                return null;
	            }
	        }
		});
		
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择...",
            suggest: true
        });
		return temp_comboBox;
	};
	
	eWidget.getCommonComboBox = function(id,dataurl,datatextfield,datavaluefield){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() +"/erp"+ dataurl;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataTextField: datatextfield,
            dataValueField: datavaluefield,
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择...",
            suggest: true
        });
		return temp_comboBox;
	};
	
	//获取单列数据
	eWidget.getSingleComboBox = function(id,dataurl){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() +"/erp"+ dataurl;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	        }
		});;
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择...",
            suggest: true
        });
		return temp_comboBox;
	};
	
	eWidget.getStorageComboBox = function(id, type){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/storage/lists";
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 no:{type:"string"},
	                  	 name:{type:"string"},
	        			 active:{type:"boolean"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            },
	            total : function(data) {//总条数
	            	if(data && data.result){
	            		return  data.count;
	            	}
	            	return 0;
	            }  
	        }
		});;
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataTextField: "name",
            dataValueField: "no",
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择...",
            suggest: true
        });
		return temp_comboBox;
	};
	
	eWidget.getCustomerComboBox = function(id, type){
		
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/customer/" + type + "/lists";
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 id:{type:"number"},
	                  	 name:{type:"string"},
	        			 active:{type:"boolean"},
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result){
	        			return data.result;  
	        		}
	                return null;
	            },
	            total : function(data) {//总条数
	            	if(data && data.result){
	            		return  data.count;
	            	}
	            	return 0;
	            }  
	        }
		});;
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择...",
            suggest: true
        });
		return temp_comboBox;
	}
	
	
	eWidget.getComboBoxContact = function(id, type,num){
		var datasource = new kendo.data.DataSource({
			transport: {
				read : {
	                type : "GET",
	                url : function(options){
	                	return fsn.getHttpPrefix() + "/erp/outOfGoods/findContact?num="+num;
					},
	                dataType : "json",
	                contentType : "application/json"
	            },
	        },
	        schema: {
	        	model: {
	        		 fields: {
	        			 no:{type:"number"},
	                  	 name:{type:"string"}
	                 }
	            },
	        	data : function(data) {
	        		if(data && data.result && data.result){
	        			return data.result;  
	        		}
	                return null;
	            }
	            ,
	            total : function(data) {//总条数
	            	if(data && data.result && data.result.count){
	            		return  data.result.count;
	            	}
	            	return 0;
	            }
	        }
		});
		
		var temp_comboBox = $("#" + id).kendoComboBox({
            dataTextField: "name",
            dataValueField: "id",
            dataSource: datasource,
            filter: "contains",
            placeholder: "请选择联系人",
            suggest: true,
            change:function(e){
            	var dataItem = $("#" + id).data("kendoComboBox").dataItem();
//            	$("#contacttel").val(dataItem.tel_1);
//            	$("#caddress").val(dataItem.province+dataItem.city+dataItem.area+dataItem.addr);
            }
        });
		return temp_comboBox;
	}
	
})