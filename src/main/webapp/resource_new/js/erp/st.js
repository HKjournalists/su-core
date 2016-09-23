$(function(){
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget||{};
	var common = fsn.common = fsn.common || {};
	var st = fsn.st = fsn.st || {};
	$.extend(st,common);
	
	st.datasource = new kendo.data.DataSource({
		transport: {
			read : {
                type : "GET",
                url : function(options){
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};
					return fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE + "/list";
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
	st.columns = [
	               {field: "id", title: st.SIMPLE_MODEL_NAME + "ID", width:"1px",editor:false},
	               {field: "name", title: st.SIMPLE_MODEL_NAME + "名称", width:"auto"},
	               ];
	
	
	/**
	 * add, update, delete
	 */
	
	st.selectedData = {};
	
	st.clearForm = function(){
		$("#name").kendoValidator().data("kendoValidator").hideMessages();
		$("#id").val(0);
		$("#name").val("");
	};
	
	st.bindForm = function(){
		$("[data-bind='value:id']").val(st.selectedData.id);
		$("[data-bind='value:name']").val(st.selectedData.name);
		$("#name").kendoValidator().data("kendoValidator").validate();
	};
	
	
	st.windowSaveConfrim = function(){
		//validation
		//if(st.validator.validate()){
		var validate = $("#name").kendoValidator().data("kendoValidator").validate();
		if(validate){
			//fsn.showNotificationMes("Sucess",true);
			var inName = $("#name").val().trim();
			if(inName==""){
				fsn.initNotificationMes("不能输入空格！",false);
				return;
			}
			var model = {
					name:inName
			};
			$.ajax({
				url: fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE,
				type:"POST",
				dataType:"json",
				contentType: "application/json; charset=utf-8",
				data:JSON.stringify(model),
				success:function(data){
					st.datasource.read();
					if (data.result.status == "true") {
						fsn.initNotificationMes("新增成功！", true);
						$("#OPERATION_WIN").data("kendoWindow").close();
					}else if(data.result.status == "false" && data.result.show){
						fsn.initNotificationMes(data.result.errorMessage, false);
					}else {
						fsn.initNotificationMes("新增失败，后台出现异常！", false);
					}			
				}
			});
		}else{
			//fsn.initNotificationMes("Failure",false);
		}
	};
	
	st.windowUpdateConfrim = function(){
		//validation
		if(st.validator.validate()){
			if(st.selectedData.name != $("#name").val().trim()){
					var model = {
							id:st.selectedData.id,
							name:st.selectedData.name,
							updateName:$("#name").val()
					};
					$.ajax({
						url: fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE,
						type:"PUT",
						dataType:"json",
						contentType: "application/json; charset=utf-8",
						data:JSON.stringify(model),
						success:function(data){
							if(data.result.status == "true"){
								fsn.initNotificationMes("更新成功！", true);
								$("#OPERATION_WIN").data("kendoWindow").close();
								st.datasource.read();
							}else if(data.result.status == "false" && data.result.show){
								fsn.initNotificationMes(data.result.errorMessage, false);
							}else{
								fsn.initNotificationMes("更新失败，后台出现异常！", false);
							}
							$("#cancelWindowCon").bind("click",function(){
								win.data("kendoWindow").close();
							});
						}
					});
			} else {
				fsn.initNotificationMes("还没有修改数据",false);
			}
		}else{
//			fsn.initNotificationMes("Failure",false);
		}
	};
	
	st.windowDeleteConfrim = function(){
		var item = {id:st.selectedData.id, name:st.selectedData.name};
		if(!st.judgeIsUsed(item)){
			fsn.initNotificationMes("该记录已被使用，不允许删除！", false);
			return;
		}
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE,
			type:"DELETE",
			dataType:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify({id:st.selectedData.id, name:st.selectedData.name}),
			success:function(data){
				if(data.result.status == "true"){
					fsn.initNotificationMes("删除成功！", true);
					st.datasource.read();
				}else{
					fsn.initNotificationMes(data.result.errorMessage, false);
				}
			}
		});
	};
	
	st.GRIDID = "Simple_Grid";
	st.DAILOGID = "OPERATION_WIN"; 
	st.initailize = function(){
		if(st.SIMPLE_TYPE == 3) {
			$("#status_bar").html("当前位置：供应商类型");
		}
		this.initComponent(st);
	};
	st.initailize();
	st.judgeIsUsed = function(item){
		var flag = true;
		var model = {
				id:item.id,
				name:item.name,
		};
		$.ajax({
			url: fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE +"/judgeIsUsed",
			type:"POST",
			dataType:"json",
			async:false,
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(model),
			success:function(data){
				flag = data.flag;
			}
		});
		return flag;
	};
//	$('#search').kendoSearchBox({
//				change : function(e) {
//					var keywords=e.sender.options.value;
//			    	if(keywords.trim()!=""){
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(initStandard(keywords));
//			    	    $("#Simple_Grid").data("kendoGrid").refresh();
//			    	}else{
//			    		$("#Simple_Grid").data("kendoGrid").setDataSource(st.datasource);
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
                	if(options.filter){
                		var configure = filter.configure(options.filter);
                 		return fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE + "/reglist/" + options.page + "/" + options.pageSize + "/"+configure;;
                	};//filter cgw
					return fsn.getHttpPrefix() + "/erp/model/" + st.SIMPLE_TYPE + "/search?keywords="+keywords;
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
			};
});
