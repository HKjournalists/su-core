function brandCategoryTreeWin(winNode, nameNode, idNode,dataType) {
	var serviceRoot = fsn.getHttpPrefix();
	switch(dataType){
		case "brand":serviceRoot = serviceRoot + "/business/tree";break;
		case "area":serviceRoot = serviceRoot + "/sys/areaTree";break;
		case "office":serviceRoot = serviceRoot + "/sys/officeTree";break;
		case "bussinessName":serviceRoot = serviceRoot + "/business/tree/getRelativesOftree";break;
	}
	var homogeneous = new kendo.data.HierarchicalDataSource({
		transport : {
			read : {
				url : serviceRoot,
				dataType : "json"
			}
		},
		schema : {
			model : {
				id : "id",
				leafId : "leafId",
				hasChildren : "hasChildren",
			}
		},
	});

	var window = $("#" + winNode);
  	if (!window.data("kendoWindow")) {
		$("#listView").kendoTreeView({
			dataSource : homogeneous,
			dataTextField : "name",
			select: onSelect
		});
        window.kendoWindow({
            width: "400px",
            height:"400px",
            title: "请选择",
            visible: false
        });
    } else {
    	$("#listView").data("kendoTreeView").select(null);
    }

  	function onSelect(e) {
		 var tempStr = this.text(e.node);
		 var treeview = $("#" + winNode + " #listView").data("kendoTreeView");
		 var dataItem = treeview.dataItem(treeview.findByText(this.text(e.node)));
		 if(dataType != "brand"){
			 $("#" + nameNode).val(tempStr);
		     $("#" + idNode).val(dataItem.id);
		     window.data("kendoWindow").close();
			 return;
		 }
		 var parent = treeview.findByText(this.text(e.node));
		 do {
			 try {
				 parent = treeview.parent(parent);
				 tempStr = treeview.text(parent)+"."+tempStr;
			 } catch (e) {
				 parent = "";
			 }
		} while (parent != "");
		$("#" + nameNode).val(tempStr);
    	$("#" + idNode).val(dataItem.leafId);
    	window.data("kendoWindow").close();
     }
     
     window.data("kendoWindow").open().center();
}
