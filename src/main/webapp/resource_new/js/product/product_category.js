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
				if(!portal.validaRegularity(text)){return;}
				var li= document.createElement("li");
					li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='fsn.portal.delRegularity(this)'></span>"+text+";</a>";
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
				var cateCombox = $("#category1").data("kendoComboBox");
				if(cateCombox){
					cateCombox.setDataSource(returnValue.data);
					if(portal.edit_id == null){
						cateCombox.value("");
					}
				}
			}
		},
	});
};

/**
 * 执行标准赋值
 * @author HaoYuanBin
 */
portal.setRegularityValue = function(){
	/*刷新执行标准datasource*/
	$.ajax({
		url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+$("#category2").data("kendoComboBox").value()+"?categoryFlag="+false,
		type:"GET",
		dataType:"json",
		async:false,
		success:function(value){
			if(value.result.status == "true"){
				$("#regularityInfo").data("kendoComboBox").setDataSource(value.data);
				if(value.data==null||value.data.length<=0){
					lims.initNotificationMes("该二级分类下没有执行标准，请先新增执行标准！", false);
				}													
			}
		},	
	});
	//将是执行标准格式化添加到已选择的执行标准框里
	var text = $("#regularity").val().trim();
	var regularity = text.split(";");
	var ul = document.getElementById("regularityList");
	var ds = $("#regularityInfo").data("kendoComboBox").dataSource.data();
	//将执行标准格式化添加到已选择的执行标准框中
	for (var i = 0; i < regularity.length - 1; i++) {
		var li = document.createElement("li");
		li.innerHTML = "<a herf='#' title='" + regularity[i] + "'><span class='k-icon k-cancel' onclick='fsn.portal.delRegularity(this)'></span>" + regularity[i] + ";</a>";
		ul.appendChild(li);
	}
	//将执行标准datasource进行筛选
	for(var j=0;j<ds.length;j++){
		if(text.indexOf(ds[j].name)!=-1){
			$("#regularityInfo").data("kendoComboBox").dataSource.remove(ds[j]);
			j=j-1;
		}
	};
};

/**
 * 移除执行标准
 * @author HaoYuanBin
 */
portal.delRegularity = function(e){
	var name = e.parentNode.title;
	$("#regularityInfo").data("kendoComboBox").dataSource.add({ name:name});
	 e.parentNode.parentNode.parentNode.removeChild(e.parentNode.parentNode);
};

/**
 * 验证该标准是否已经选择
 * @author HaoYuanBin
 */
portal.validaRegularity = function(text){
	var ul = document.getElementById("regularityList");
	var li = ul.getElementsByTagName("li");
	if (li.length >= 1) {
		for (var i = 0; i < li.length; i++) {
			var value = li[i].innerHTML;
			if (value.length > value.replace(text, "").length) {
				lims.initNotificationMes(text + "该标准已经被选择，不能再次选择！", false);
				return false;
			}
		}
	}
	return true;
};

/**
 * 添加执行标准
 * @author ZhangHui 2015/4/30
 */
portal.addRegularity = function(){
	//空值校验
	if($("#regularity_type").val().replace(/[ ]/g,"")==""){
		lims.initNotificationMes("请填写标准类型！", false);
		return;
	}
	if($("#regularity_year").val().replace(/[ ]/g,"")==""){
		lims.initNotificationMes("请填写执行序号及年份！", false);
		return;
	}
	//如果超过12，则不允许添加
	var ul = document.getElementById("regularityList");
	var count = ul.getElementsByTagName("li").length;
	if(count>=12){
		lims.initNotificationMes("执行标准过多，不能再添加！", false);
		return;
	}
	//将新增标准格式化后添加到列表中
	var text = $("#regularity_type").val().replace(/[ ]/g,"")+" "+$("#regularity_year").val().replace(/[ ]/g,"")+" "+$("#regularity_name").val();		
	if(!portal.validaRegularity(text)){return;}
	var li= document.createElement("li");
	li.innerHTML="<a herf='#' title='"+text+"'><span class='k-icon k-cancel' onclick='delRegularity(this)'></span>"+text+";</a>";
	ul.appendChild(li);
	//清空信息
	$("#regularity_type").val("");
	$("#regularity_year").val("");
	$("#regularity_name").val("");
};

/**
 * 定义点击执行标准的Click事件
 * @author ZhangHui 2015/4/30
 */
portal.searchRegularity = function(){
 	var index = $("#category2").data("kendoComboBox").select();
	if(index==-1){
		lims.initNotificationMes("请先选择食品分类！", false);
		return;
	}else{
		var index = $("#category2").data("kendoComboBox").select();
		if(index==-1){
			lims.initNotificationMes("请先选择食品分类后再新增执行标准！", false);
			return;
		}
		//清空信息
		$("#regularity_type").val("");
		$("#regularity_year").val("");
		$("#regularity_name").val("");
		$("#regularityInfo").data("kendoComboBox").value("");
		$("#regularityList").text("");
		
		//执行标准赋值
		portal.setRegularityValue();
		$("#addRegularity_window").data("kendoWindow").open().center();
	}
};

/**
 * 产品类别赋值操作
 * @author ZhangHui 2015/4/30
 */
portal.setCategoryValue = function(v){
	// 清空页面数据
	var category1Combox = $("#category1").data("kendoComboBox");
	if(category1Combox){
		category1Combox.text("");
	}else{
		$("#foodinfo_category").val((v&&v.name)?v.name:"");  // 兼容预览
		$("#foodinfo_category").text((v&&v.name)?v.name:""); // 兼容生产企业/经销商报告录入页面
	}
	
	var category2Combox = $("#category2").data("kendoComboBox");
	if(category2Combox){
		category2Combox.text("");
	}
	
	var category3Combox = $("#category3").data("kendoComboBox");
	if(category3Combox){
		category3Combox.text("");
	}
	
	// 验证数据
	if(!v || !v.category || !v.category.code || !category1Combox){
		return;
	}
	
	category1Combox.value(v.category.code.substring(0,2));    // 一级分类 = 二级分类前两位
	$.ajax({
		url:portal.HTTP_PREFIX + "/testReport/searchCategory/2?parentCode="+v.category.code.substring(0,2),
		type:"GET",
		dataType:"json",
		async:false,
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				$("#category2").data("kendoComboBox").setDataSource(returnValue.data);											
				$("#category2").data("kendoComboBox").value(v.category.id);   // 二级分类id
			}
		},	
	});
	$.ajax({
		url:portal.HTTP_PREFIX + "/testReport/searchLastCategory/"+v.category.id+"?categoryFlag="+true,
		type:"GET",
		dataType:"json",
		async:false,
		success:function(returnValue){
			if(returnValue.result.status == "true"){
				$("#category3").data("kendoComboBox").setDataSource(returnValue.data);
			}
		},	
	});
	
	if(v.id){
		$("#category3").data("kendoComboBox").value(v.id);  // 三级分类id
	}else if(v.name && v.name!=""){
		$("#category3").data("kendoComboBox").text(v.name);  // 三级分类名称
	}
};