$(function() {
	var upload = window.lims.upload = window.lims.upload || {};
	var unit = upload.unit = upload.unit || {};
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var portal = fsn.portal = fsn.portal || {}; // portal命名空间
	portal.CONTEXT_PATH = "/fsn-core"; // 项目名称
	portal.HTTP_PREFIX = fsn.getHttpPrefix(); // 业务请求前缀
	
	unit.templatecolumns = [
	                 {field: "id",title: lims.l("编号"),width: 50,filterable: false},
	                 {field: "name",title: lims.l("EnterpriteName"),width: 100,filterable: false,
						 template:function(dataItem) {
							 return "<label title='"+dataItem.name+"'>" + dataItem.name + "</label>";
						 }
					 },
                     {field: "licNo",title: lims.l("营业执照号"),width: 100,filterable: false},
                     {field: "personInCharge",title: lims.l("LegalPerson"),width: 100,filterable: false},
                     {field: "busType",title: lims.l("EnterpriteType"),width: 80,filterable: false},
                     {field: "regDate",title: lims.l("EnterpriteDate"),width: 80,filterable: false,
                    	 template:function(dataItem) {
                        	 return "<label title='"+dataItem.regDate+"'>" + dataItem.regDate + "</label>";
                         }
                     },
                     {field: "regAddr",title: lims.l("企业地址"),width: 100,filterable: false,
                    	 template:function(dataItem) {
                        	 return "<label title='"+dataItem.regAddr+"'>" + dataItem.regAddr + "</label>";
                         }
                     },
                     {field: "linkMan",title: lims.l("联系人"),width: 50,filterable: false},
                     {field: "linkTel",title: lims.l("联系电话"),width: 70,filterable: false},
                     { command: [{
    			                      text: lims.localized("Detail"),
									  click: function(e){
										  var editRow = $(e.target).closest("tr");
										  var temp = this.dataItem(editRow);
										  window.open(portal.CONTEXT_PATH+"/views/account/admin/admin_enRegiste.html?id="+temp.id);
									  }
								  },
					            ], title:lims.l("Operation"), width: 50 }
                     ];
	unit.templateDS = function(province,city,area,nl,type){
		return new kendo.data.DataSource({
			transport: {
	            read: {
	            	url : function(options) {
						var text = portal.HTTP_PREFIX + "/account/loadbus/" + options.page + "/" + options.pageSize + "?province="+province+"&city="+city+"&area="+area+"&nl="+nl+"&btype="+type;
						return text;
					},
					type: "GET",
	                dataType : "json",
	                contentType : "application/json"
	            }
	        },
	        schema: {
	        	 data : function(d) {
	                 return d.data.enRegisteList != null ?d.data.enRegisteList : [];  //响应到页面的数据
	             },
	             total : function(d) {
	                 return d.result.success?d.data.totalCount:0;   //总条数
	             }         
	        },
	        batch : true,
	        pageSize : 10, //每页显示个数
	        serverPaging : true,
	        serverFiltering : true,
	        serverSorting : true
		});
	};
	unit.buildGrid=function(id,columns,ds){
		$("#" + id).kendoGrid({
			dataSource: ds == undefined ?{data:[],pageSize:10}:ds,
			filterable: {
				messages: lims.gridfilterMessage(),
				operators: {
					string: lims.gridfilterOperators(),
				    date: lims.gridfilterOperatorsDate(),
				    number: lims.gridfilterOperatorsNumber()
				}
			},
			height: 450,
	        sortable: true,
	        resizable: true,
	        selectable: true,
	        pageable: {
	            refresh: true,
	            pageSizes: true,
	            messages: lims.gridPageMessage()
	        },
	        columns: columns
		});
	};

	/**
	 * 根据企业地址查询企业
	 */
	$("#q_query").click(function(){
		var province = $("#q_province").data("kendoDropDownList").text()==="显示所有..."?"":$("#q_province").data("kendoDropDownList").text();
		var city = $("#q_city").data("kendoDropDownList").text()==="显示所有..."?"":$("#q_city").data("kendoDropDownList").text();
		var area = $("#q_area").data("kendoDropDownList").text()==="显示所有..."?"":$("#q_area").data("kendoDropDownList").text();
		var nl = $("#nl").val().trim();
		var btype = $("#btype").data("kendoDropDownList").text()==="显示所有..."?"":$("#btype").data("kendoDropDownList").text();
		province = encodeURIComponent(province);
		var gridDS = unit.templateDS(encodeURIComponent(province),encodeURIComponent(city),encodeURIComponent(area),encodeURIComponent(nl),encodeURIComponent(btype));
		$("#enRegiste").data("kendoGrid").setDataSource(gridDS);
	});

	function selectProvinceDownList(e){
		var province = e.item.text();
		$("#q_province").data("kendoDropDownList").text(province);
		var provinceId = $("#q_province").data("kendoDropDownList").value();
		if(provinceId != null && provinceId != ""){
			city = loadAreaForCity("/erp/address/findCityByProId/"+provinceId,"cities");
			$("#q_city").data("kendoDropDownList").setDataSource(city);
		}else{
			$("#q_city").data("kendoDropDownList").setDataSource([]);
			$("#q_city").data("kendoDropDownList").text("显示所有...");
			$("#q_area").data("kendoDropDownList").setDataSource([]);
			$("#q_area").data("kendoDropDownList").text("显示所有...");
			$("#enRegiste").data("kendoGrid").setDataSource(unit.templateDS("","","","",""));
		}
	}

	function selectCityDownList(e){
		if($("#q_city").data("kendoDropDownList")){
			var city = e.item.text();
			$("#q_city").data("kendoDropDownList").text(city);
			var cityId  = $("#q_city").data("kendoDropDownList").value();
			var area = [];
			if(cityId!=null&&cityId!=""){
				area = loadAreaForCity("/erp/address/findAreaByCityId/"+cityId,"areas");
			}
			$("#q_area").data("kendoDropDownList").setDataSource(area);
		}
	}

	/**
	 * 根据市 加载区域 的数据源
	 */
	function loadAreaForCity(url,type){
		return new kendo.data.DataSource({
			transport: {
				read: {
					type: "GET",
					dataType: "json",
					async: false,
					contentType: "application/json",
					url: portal.HTTP_PREFIX + url
				}
			},
			schema: {
				data: function(d) {
					var result = [];
					if(type==="cities"){
						result = d.cities!=null?d.cities:[];
					}
					if(type==="areas"){
						result = d.areas!=null?d.areas:[];
					}
					if(type==="btype"){
						result = d.type!=null?d.type:[];
					}
					return result;
				}
			}
		});
	}

	/**
	 * 初始化查询的下来选项
	 */
	function initQueryDownList(id,ds,text,value){
		$("#"+id).kendoDropDownList({
			dataTextField: text,
			dataValueField: value,
			optionLabel:"显示所有...",
			valueTemplate: function(e){
				var result = "";
				if(text==="city"){
					result = '<label title=' + e.city+'>' + e.city + '</label>';
				}
				if(text==="area"){
					result = '<label title=' + e.area+'>' + e.area + '</label>';
				}
				if(text==="type"){
					result = '<label title=' + e.type+'>' + e.type + '</label>';
				}
				return result;
			},
			dataSource: ds,
			index: 0
		});
	}

	unit.initialize = function(){
		var dataSource = this.templateDS("","","","","");
		unit.buildGrid("enRegiste",this.templatecolumns, dataSource);
		$("#unit .k-grid-content").attr("style","height: 364px");
		$("#q_province").kendoDropDownList({
			dataTextField: "province",
			dataValueField: "provinceId",
			optionLabel:"显示所有...",
			dataSource: [{province:"贵州省",provinceId:520000}],
			index: 0
		});
		initQueryDownList("q_city",[],"city","cityId");
		initQueryDownList("q_area",[],"area","areaId");
		$("#q_city").data("kendoDropDownList").text("显示所有...");
		$("#q_area").data("kendoDropDownList").text("显示所有...");
		$("#q_province").data("kendoDropDownList").bind("select",selectProvinceDownList);
		$("#q_city").data("kendoDropDownList").bind("select",selectCityDownList);
		var busType = loadAreaForCity("/account/loadbus/btype","btype");
		initQueryDownList("btype",busType,"type","id");
	};
	unit.initialize();
});