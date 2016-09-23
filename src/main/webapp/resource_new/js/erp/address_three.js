$(function() {
	var fsn = window.fsn = window.fsn || {};
	var eWidget = fsn.eWidget = fsn.eWidget || {};
	var common = fsn.common = fsn.common || {};

	var address = fsn.address = fsn.address || {};

	$.extend(address, common);
	
	/**
	 * add, update, delete
	 */
	
	address.selectedData = {};
	
	address.clearForm = function(){
		$("#province").val("");
		$("#city").val("");
		$("#area").val("");
		$("#other").val("");
	}
	
	address.bindForm = function(){
		$("[data-bind='value:province']").val(address.selectedData.province);
		$("[data-bind='value:city']").val(address.selectedData.city);
		$("[data-bind='value:area']").val(address.selectedData.area);
		$("[data-bind='value:other']").val(address.selectedData.other);
		
		fsn.address.provinceCombox.value(address.selectedData.province);
		fsn.address.cityCombox.value(address.selectedData.city);
		fsn.address.areaCombox.value(address.selectedData.area);
	}
	
	address.initRequiredComponents = function(){
	
		var datasourceProvince = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						return fsn.getHttpPrefix() + "/erp/address/findPro";
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						provinceId : {
							type : "number"
						},
						province : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.result) {
						return data.result;
					}
				},
				total : function(data) {// 总条数
					if (data && data.count) {
						return data.count;
					}
				}
			}
		});
		
		var datasourceCity = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						return fsn.getHttpPrefix() + "/erp/address/findCity";
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						cityId : {
							type : "number"
						},
						city : {
							type : "string"
						},
						provinceId : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.result) {
						return data.result;
					}
				},
				total : function(data) {// 总条数
					if (data && data.count) {
						return data.count;
					}
				}
			}
		});
		
		var datasourceArea = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : function(options) {
						return fsn.getHttpPrefix() + "/erp/address/findArea";
					},
					dataType : "json",
					contentType : "application/json"
				},
			},
			schema : {
				model : {
					fields : {
						id : {
							type : "number"
						},
						areaId : {
							type : "number"
						},
						area : {
							type : "string"
						},
						cityId : {
							type : "string"
						}
					}
				},
				data : function(data) {
					if (data && data.result) {
						return data.result;
					}
				},
				total : function(data) {// 总条数
					if (data && data.count) {
						return data.count;
					}
				}
			}
		});
	//kendoComboBox校验
	combobox_change=function(e){
		var judge = e.sender.selectedIndex;
		if(judge==-1){
			this.value("");
			fsn.initNotificationMes("填写内容有误！请重新填写", false);
			this.value("");
		}else{
			return;
		}
	}
	
		address.provinceCombox = $("#cprovince").kendoComboBox({
			dataTextField : "province",
			dataValueField : "provinceId",
			dataSource : datasourceProvince,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");
		address.provinceCombox.bind("change", combobox_change);
		
		address.cityCombox = $("#ccity").kendoComboBox({
			cascadeFrom: "cprovince",
			dataTextField : "city",
			dataValueField : "cityId",
			dataSource : datasourceCity,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");
		address.cityCombox.bind("change", combobox_change);
		
		address.areaCombox = $("#carea").kendoComboBox({
			cascadeFrom: "ccity",
			dataTextField : "area",
			dataValueField : "areaId",
			dataSource : datasourceArea,
			filter : "contains",
			placeholder : "请选择...",
			suggest : true
		}).data("kendoComboBox");
		address.areaCombox.bind("change", combobox_change);
		
		address.comboBoxProvince = {};
		address.comboBoxCity = {};
		address.comboBoxArea = {};
		
		address.provinceCombox.setOptions({
			change: function(e) {
				address.comboBoxProvince = {
						provinceId:this.value(),
						province:this.text()
				}
			  }
		});
		
		address.cityCombox.setOptions({
			change: function(e) {
				address.comboBoxArea = {
						areaId:this.value(),
						area:this.text()
				}
			  }
		});
		
		address.areaCombox.setOptions({
			change: function(e) {
				address.comboBoxCity = {
						cityId:this.value(),
						city:this.text()
				}
			  }
		});
		
	}
	
	address.initailize = function(){
		
		this.initRequiredComponents();
	}
	address.initailize();
})
