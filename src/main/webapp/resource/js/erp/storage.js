$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var storage = fsn.storage = fsn.storage || {};
	$.extend(storage,common);
	
	storage.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
				url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/storage/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
                	return fsn.getHttpPrefix() + "/erp/storage/list";
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
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
	storage.columns = [           
	               {field: "name", title: "仓库名称", width:"50%"},
	               {field: "manager", title: "仓库管理员", width:"40%"}
	               ];
	
	
	/**
	 * add, update, delete
	 */
	
	storage.selectedData = {};
	
	storage.clearForm = function(){
//		$("#no").kendoValidator().data("kendoValidator").hideMessages();
		$("#name").kendoValidator().data("kendoValidator").hideMessages();
		$("#manager").kendoValidator().data("kendoValidator").hideMessages();
		
//		$("#no").val("");
		$("#name").val("");
		$("#manager").val("");
//		$("#no").removeAttr("disabled");
	}
	
	storage.bindForm = function(){
//		$("[data-bind='value:no']").val(storage.selectedData.no);
		$("[data-bind='value:name']").val(storage.selectedData.name);
		$("[data-bind='value:manager']").val(storage.selectedData.manager);
		
//		$("#no").kendoValidator().data("kendoValidator").validate();
		$("#name").kendoValidator().data("kendoValidator").validate();
		$("#manager").kendoValidator().data("kendoValidator").validate();
//		$("#no").attr("disabled","disabled");
	}
	
	storage.windowSaveConfrim = function(){
		//validation
		if(storage.validator.validate()){
			//fsn.showNotificationMes("Sucess",true);
//			if($("#no").val().trim()==""){
//				fsn.initNotificationMes("仓库编号不能输入空格！",false);
//				$("#no").focus();
//				return;
//			}
			if($("#name").val().trim()==""){
				fsn.initNotificationMes("仓库名称不能输入空格！",false);
				$("#name").focus();
				return;
			}
			if($("#manager").val().trim()==""){
				fsn.initNotificationMes("管理员不能输入空格！",false);
				$("#manager").focus();
				return;
			}
			var model = {
//					no:$("#no").val(),
					name:$("#name").val(),
					manager:$("#manager").val(),
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/storage",
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					if(data.result.status == "true") {
						fsn.initNotificationMes(data.result.message, true);
						$("#OPERATION_WIN").data("kendoWindow").close();
						console.log(data);
						storage.datasource.read();
					} else {
						fsn.initNotificationMes(data.result.errorMessage,false);
					}
				}
			});
		}else{
			//fsn.initNotificationMes("Failure",false);
		}
	};
	
	storage.windowUpdateConfrim = function(){
		//validation
		if(storage.validator.validate()){
			if(storage.selectedData.name != $("#name").val().trim()||storage.selectedData.manager != $("#manager").val().trim()){
					var model = {
							no:storage.selectedData.no,
							name:$("#name").val(),
							manager:$("#manager").val(),
					};
					$.ajax({
						url: fsn.getHttpPrefix() + "/erp/storage",
						type:"PUT",
						dataType:"json",
						contentType: "application/json; charset=utf-8",
						data:JSON.stringify(model),
						success:function(data){
							if(data && data.result){
								if(data.result.status == "true"){
									fsn.initNotificationMes("更新成功！",true);
									$("#OPERATION_WIN").data("kendoWindow").close();
									console.log("OK");
									storage.datasource.read();
								} else if(data.result.status == "false" && data.result.show){
									fsn.initNotificationMes(data.result.errorMessage, false);
								}else{
									fsn.initNotificationMes("更新失败，后台出现异常！", false);
								}
							}
						}
					});
			} else {
				fsn.initNotificationMes("还没有修改数据",false);
			}
		}else{
			//fsn.initNotificationMes("Failure",false);
		}
	};
	
	/*判断仓库是否被使用*/
	storage.judgeStorageIsUsed = function(storageNo){
		var flag = true;
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/storage/judgeIsUsed",
			type:"POST",
			async:false,
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify({no:storageNo}),
			success:function(data){
				flag = data.flag;
			}
		});
		return flag;
	};
	
	storage.windowDeleteConfrim = function(){
		if(storage.selectedData.no){
			if(!storage.judgeStorageIsUsed(storage.selectedData.no)){
				fsn.initNotificationMes("该仓库已被使用，无法删除！", false);
				return;
			}
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/storage",
				type:"DELETE",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify({no:storage.selectedData.no}),
				success:function(data){
					if(data && data.result){
						if(data.result.status == "true"){
							storage.datasource.remove(storage.grid.dataItem(storage.grid.select()));
							fsn.initNotificationMes("删除成功！", true);
						}else{
							fsn.initNotificationMes("删除失败！", false);
						}
					}
				}
			});
		}
	};
	
	storage.GRIDID = "Simple_Grid";
	storage.DAILOGID = "OPERATION_WIN"; 
	storage.initailize = function(){
		this.initComponent(storage);
		$("#label_no").html(storage.SIMPLE_MODEL_NAME + "编号");
	}
	storage.initailize();
	storage.judgeIsUsed = function(item){
		var flag = true;
		var model = {
				no:item.no,
				name:item.name,
				manager:item.manager,
		}
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/storage/judgeIsUsed",
			type:"POST",
			dataType:"json",
			async:false,
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(model),
			success:function(data){
				if(data.flag){
					flag = true;
				}else{
					flag = false;
				}
			}
		});
		return flag;
	}
//	$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(storage.datasource);
//			    		$("#Simple_Grid").data("kendoGrid").refresh();
//			    	}
//				}
//			});
			
			initStandard = function(keywords){
				var ds = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
					return fsn.getHttpPrefix() + "/erp/storage/search?keywords="+keywords;
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
                 }
            },
        	data : function(data) {
                return data.result.listOfModel;  
            },
            total : function(data) {
               return  data.result.count;//总条数
            }  
        },
        batch : true,
        page:1,
        pageSize : 10, //每页显示个数
        serverPaging : true,
        serverFiltering : true,
        serverSorting : true
	});
				//ds.read();
				return ds;
			}
})
