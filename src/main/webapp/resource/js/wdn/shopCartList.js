$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var affirmOrder=window.excel=window.affirmOrder||{};
	var wdnServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";
	var shoppingCatData = JSON.parse(window.sessionStorage.getItem("shoppingCat"));
	var i = 0;
	stepBar.init("stepBar", {
		step : 1,
		//change : false,
		animation : true
	});

	affirmOrder.dataSource = new kendo.data.DataSource({
		  data:  shoppingCatData,
		  schema: {
			  data: function(e){
				  shoppingCatData.forEach(function(item){
					  i += 1;
					  item.order = i;
				  });
				  return shoppingCatData;
			  },
			  total:function(d){
					return d.length;
			  }
		  },
	    page:1,
	    pageSize :10, //每页显示个数
	});

	affirmOrder.grid = $("#affirmOrderGrid").kendoGrid({
		dataSource:affirmOrder.dataSource,
		//selectable : "row",
		sortable : true,
		resizable : true,
		height: 300,
		toolbar: [{ template : kendo.template($("#toolbar_template").html()) }],
		filterable: {
			extra: false,
			messages: fsn.gridfilterMessage(),
			operators: {
				string: fsn.gridfilterOperators(),
			    date: fsn.gridfilterOperatorsDate(),
			    number: fsn.gridfilterOperatorsNumber()
			}
		},
		messages:{
			commands:{
				create: "添加",
				edit: "编辑",
				update: "保存",
				destroy: "删除",
				cancel:"取消",
				canceledit: "取消编辑"
			}
		},
		pageable : {
			refresh : true,
			pageSizes : true,
			messages : fsn.gridPageMessage(),
		},
		editable: "inline",
		columns : [{
			 field: "",
        	 title: "",
        	 width:30,
        	 filterable: false,
        	 headerTemplate: kendo.template('<input type="checkbox" id="check-all" /><label for="check-all"></label>'),
        	 template:function(e){
        			return "<input type='checkbox' name='rowCheck' id='"+ e.id+"'/>";
        	 }
		},{
			 field: "order",
        	 title: "序号",
        	 width:50,
        	 filterable: false,
			 //template:function(e){
				// return i+=1;
			 //}
		},{
		 	 field: "title",
        	 title: "文献名称",
		},{
			 field:"datasource",
        	 title:"文献来源",
		},{
			 field:"authors",
        	 title:"文献作者",
		},{
			 field:"year",
        	 title:"日期",
             filterable: false
		},{
			 title:"操作",
			command: [{
				name:"oDelete",
				text:"删除",
				click : function(e) {
					var tr = $(e.target).closest("tr");
					var data = this.dataItem(tr);
					$.MsgBox.Confirm("提示","确认需要删除【"+data.title+"】?",function(){
						affirmOrder.dataSource.remove(data);
						window.sessionStorage.setItem("shoppingCat", JSON.stringify(affirmOrder.dataSource.data()));
					});
				}
			}]
		}]
	}).data("kendoGrid");

	$("#check-all").on("click",function(){
		$("input[name='rowCheck']").prop("checked", $(this).prop("checked"));
	});

	$("#deletDocs").on("click", function(){
		var removeData = [];
		affirmOrder.dataSource.data().forEach(function(item){
			if(document.getElementById(item.id)&&document.getElementById(item.id).checked == true){
				removeData.push(item);
			}
		});
		if(removeData.length > 0){
			removeData.forEach(function(d){
				affirmOrder.dataSource.remove(d);
			});
		}else{
			fsn.initNotificationMes("请选择要移除的文献！",false);
		}
		window.sessionStorage.setItem("shoppingCat", JSON.stringify(affirmOrder.dataSource.data()));

		//affirmOrder.dataSource.remove(data);
	});
});
