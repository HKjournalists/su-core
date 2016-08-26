$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var excel=window.excel=window.excel||{};
	var excelServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";
	var fileTypes=["xls","xlsx"];
	var selectFiles = new Array();
	var fileSzie = 5;//可上传文件数量
	var source=""; //数据来源
	excel.onSelect = function(e){
		if(selectFiles.length==0){
			setTimeout(function () {
				$(".k-upload-selected").bind("click",function(){
					source = $("#dropdownlist").val();
					if(source=="--请选择抽样来源--"){
						fsn.initNotificationMes("请选择抽样来源", false);
					    return false;
					}
				});
			},10);
		}
	
		$(e.files).each(function(index,item){
			//不符合格式文件
			//console.log($.inArray(item.name,selectFiles));
			if($.inArray(item.name,selectFiles)!=-1||$.inArray(item.extension.substr(1).toLowerCase(),fileTypes)<0){
				e.files.splice($.inArray(item,e.files),1);
			}else{
				selectFiles.push(item.name);
			}
			if(selectFiles.length>fileSzie){
				console.log(selectFiles.length);
				console.log(selectFiles);
				e.files.splice(index,e.files.length-index);
				fsn.initNotificationMes("最多只能上传"+fileSzie+"个文件", false);
				return false;
			};
		});
		if(selectFiles.length>0){
			if($('.k-file-success').length>0){
				var data = excel.Grid.dataSource.data();
				$(data).each(function(index,item){
					excel.Grid.dataSource.remove(item);
				});
			}
			$('.k-file-success').remove();
		}
		if(undefined == $("#dropdownlist").html()){
			$('.k-widget.k-upload.k-header').append('<input class="k-upload-status k-upload-status-total" id="dropdownlist"></input>');
			excel.DropDownList = $("#dropdownlist").kendoDropDownList({
				optionLabel: "--请选择抽样来源--",
				dataSource: ["总局", "地方"],
			}).data("kendoDropDownList");
		}
	};
	
	excel.onRemove = function(e){
		selectFiles.splice($.inArray(e.files[0].name,selectFiles),1);
		if($('.k-file-success').length>0){
			var data = excel.Grid.dataSource.data();
			$(data).each(function(index,item){
				if(item[0].fileName==e.files[0].name){
					excel.Grid.dataSource.remove(item);
				}
			});
		}
	};
	
	excel.onSuccess = function(e){
		if (e.operation == "upload") {
			selectFiles =[];
			excel.DataSource.add(e.response.list);
			excel.Grid.refresh();
			console.log(e.response.list);
	    }
	};
	
	excel.onComplete = function(e){
		//console.log(e);
	};
	
	excel.onUpload = function(e){
		e.data = {"source":source};
	};
	
	excel.upload = $("#excelFile").kendoUpload({
		 async: {
	            saveUrl: excelServiceBaseUrl+"uploadExcel",
	            removeUrl:excelServiceBaseUrl+"remove",
	            autoUpload: false,
	           // batch: true,
	    },
	    localization: {
	    	select: "<span class='k-icon k-i-folder-up'></span>选择文件",
			retry: "重试",
		    uploadSelectedFiles: "上传文件",
		    remove: "移除",
			cancel: "取消",
			headerStatusUploading: "上传中",
        },
        select: excel.onSelect,
        remove: excel.onRemove,
        success: excel.onSuccess,
        upload:excel.onUpload,
        complete: excel.onComplete,
    }).data("kendoUpload");
	
	excel.DataSource = new kendo.data.DataSource({
	});
	
     function sheetDetailInit(e){
    	if(e.data.errorList==null&&e.data.list==null){
    		return;
    	}
		var data = e.data.list;
		if(e.data.errorList!=null){
			data = e.data.errorList;
		}
		var arry = new Array();
		excel.ProductGrid= $("<div/>").appendTo(e.detailCell).kendoGrid({
			 dataSource: data,
	         reorderable: true,
	         filterable: false,
	         columnMenu: false,
				columns : [{
					field : "orderNumber",
					title : "序号",
					width:120,
					template:function(e){
						return kendo.parseInt(e.orderNumber);
					}
				},{
					field : "companyName",
					title : "产企业名称",
				},{
					field : "productName",
					title : "产品名称",
				},{
					field : "productionDate",
					title : "生产日期/批号",
				},{
					field : "validMessage",
					title : "信息",
					resizable : true,
					template:function(e){
						if(e.validMessage!=null){
							arry.push(e.uid);
							return '<span style="color:red" id="'+e.uid+'" title="'+e.validMessage+'">'+"<strong>"+e.validMessage+"</strong>"+'</span>';
						}
						return "导入成功";
					}
				}]
		});
		
		$(arry).each(function(index,item){
			$("#"+item).kendoTooltip({
				 position: "left",
			});
		});
	};
	
	excel.Grid = $("#excelGrid").kendoGrid({
		dataSource:excel.DataSource,
		selectable : "row",
		sortable : true,
		resizable : true,
		//height: 250,
		detailInit: detailInit,
		/*filterable : {
			messages : fsn.gridfilterMessage(),
			operators : {
				string : fsn.gridfilterOperators()
			}
		},
		pageable : {
			refresh : true,
			pageSizes : 10,
			messages : fsn.gridPageMessage(),
		},*/
		columns : [{
			field : "fileName",
			title : "文件名称",
			//filterable:false,
			template:function(e){
				return e[0].fileName;
			}
		},{
			field : "message",
			title : "状态",
			//filterable:false,
			template:function(e){
				if(null==e[0].message){
					return "导入成功";
				}else{
					//return "<strong>" + e[0].message+ "</strong>";
					return '<span style="color:red">'+ "<strong>" + e[0].message+ "</strong>"+'</span>';
				}
			}
		}]
	}).data("kendoGrid");
	
	
	function detailInit(e){
		var data = e.data[0].sheetList;
		if(!e.data[0].flag){
			$(data).each(function(index,item){
				if(null==item.message){
					data.splice(index,1);
				}
			});
		}
		excel.SheetGrid= $('<div/>').appendTo(e.detailCell).kendoGrid({
			 dataSource: data,
			 detailInit: sheetDetailInit,
				columns : [{
					field : "sheetName",
					title : "工作表名称",
					filterable:false,
					template:function(e){
						return e.sheetName;
					}
				},{
					field : "message",
					title : "信息",
					filterable:false,
					template:function(data){
						if(e.data[0].flag){
							return "成功导入"+data.list.length+"条数据";
						}else{
							return '<span style="color:red">'+ "<strong>" + data.message+ "</strong>"+'</span>';
						}
						
					}
				}]
		}).data("kendoGrid");
	}
});
