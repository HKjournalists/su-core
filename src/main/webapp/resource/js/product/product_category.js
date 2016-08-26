var fsn = window.fsn = window.fsn || {}; // 全局命名空间
var portal = fsn.portal = fsn.portal || {}; // portal命名空间
portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
var	upload = portal.upload = portal.upload || {};

/**
 * 初始化[产品类别]
 * @author HaoYuanBin
 */
portal.initialCategorys = function(){
	//执行标准
	$("#regularityInfo").kendoComboBox({
        dataTextField: "name",
        dataValueField: "id",
        dataSource: [],
        filter: "contains",
		placeholder : "请选择...",
        minLength: 0,
        index:0,
		change:function(){
			var index =$("#regularityInfo").data("kendoComboBox").select();
			if (index!=-1) {
				//如果超过12，则不允许添加
				var ul = document.getElementById("regularityList");
				var count = ul.getElementsByTagName("li").length;
				if(count>=12){
					lims.initNotificationMes("执行标准过多，不能再添加！", false);
					return;
				}
				//将选中的值添加到列表中
				var text = $("#regularityInfo").data("kendoComboBox").dataItem().name;
				if(!upload.validaRegularity(text)){return;}
				var li= document.createElement("li");
					li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
				ul.appendChild(li);
				//将Item从dataSource中移除
				$("#regularityInfo").data("kendoComboBox").dataSource.remove($("#regularityInfo").data("kendoComboBox").dataItem());
			}
			this.value("");
		}
    });
	//三级分类
	$("#category3").kendoComboBox({
        dataTextField: "name",
        dataValueField: "id",
        dataSource: [],
        filter: "contains",
		placeholder : "请选择...",
        minLength: 0,
        index:0,
		change:function(){
			var index = $("#category2").data("kendoComboBox").select();
			if (index == -1) {
				$("#category3").data("kendoComboBox").value("");
				lims.initNotificationMes("请先选择食品分类！", false);
			}
		}
    });
	//二级分类
	$("#category2").kendoComboBox({
        dataTextField: "name",
        dataValueField: "id",
        dataSource: [],
        filter: "contains",
		placeholder : "请选择...",
        minLength: 0,
        index:0,
		change:function(){
			var index = $("#category2").data("kendoComboBox").select();
			if(index==-1){
				$("#category2").data("kendoComboBox").value("");
				$("#category3").data("kendoComboBox").setDataSource([]);
				$("#category3").data("kendoComboBox").value("");
				$("#regularity").val("");
				return;
			}
			$.ajax({
				url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+this.value()+"?categoryFlag="+true,
				type:"GET",
				dataType:"json",
				async:false,
				success:function(returnValue){
					if(returnValue.result.status == "true"){
						$("#category3").data("kendoComboBox").setDataSource(returnValue.data);
						$("#regularity").val("");
						if(returnValue.data.length==1){
							$("#category3").data("kendoComboBox").select(0);
						}else{
							$("#category3").data("kendoComboBox").value("");
						}
					}
				},	
			});	
		},
    });
	//一级分类
	$("#category1").kendoComboBox({
        dataTextField: "name",
        dataValueField: "code",
        dataSource: [],
        filter: "contains",
		placeholder : "请选择...",
        minLength: 0,
        index:0,
		change:function(){
			var index = $("#category1").data("kendoComboBox").select();
			if(index==-1){
				$("#category1").data("kendoComboBox").value("");
				$("#category2").data("kendoComboBox").setDataSource([]);
				$("#category2").data("kendoComboBox").value("");
				$("#category3").data("kendoComboBox").setDataSource([]);
				$("#category3").data("kendoComboBox").value("");
				$("#regularity").val("");
				return;
			}
			$.ajax({
				url:portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode="+this.value(),
				type:"GET",
				dataType:"json",
				async:false,
				success:function(returnValue){
					if(returnValue.result.status == "true"){
						$("#category2").data("kendoComboBox").setDataSource(returnValue.data);
						$("#category3").data("kendoComboBox").setDataSource([]);
						$("#category3").data("kendoComboBox").value("");
						$("#regularityInfo").data("kendoComboBox").setDataSource([]);
						$("#regularityInfo").data("kendoComboBox").value("");
						$("#category2").data("kendoComboBox").value("");
						$("#regularity").val("");
					}
				},	
			});	
		},					
    });
	
	$.ajax({
		url:portal.HTTP_PREFIX + "/testReport/searchCategory/1?parentCode=",
		type:"GET",
		dataType:"json",
		async:false,
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				$("#category1").data("kendoComboBox").setDataSource(returnValue.data);
				if(portal.edit_id == null){
					 $("#category1").data("kendoComboBox").value("");
				}
			}
		},
	});
};