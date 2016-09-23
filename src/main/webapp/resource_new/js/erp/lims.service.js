$(function(){
	var lims = window.lims = window.lims || {};
	var service = lims.service = lims.service || {};
	
	service.HTTP_PREFIX = lims.getHttpPrefix();
	
	service.findUserByRoleID = function(id,roleID,readonly,component){
		$.ajax({
			url:service.HTTP_PREFIX + "/user/byrole/" + roleID, 
			success:function(data){
				$("#" + id).kendoMultiSelect({
					maxSelectedItems: 1,
		            dataTextField: "realUserName",
		            dataValueField: "id",
		            dataSource: data.users.users
		        });
				if(readonly){
					$("#" + id).data(component).readonly();
				}
			},
			error:function(){
				console.log("error: Faield try to access the url - /erp/service/user/byrole/" + roleID);
			}
		});
	}
	
	service.findUserByJobTypeID = function(id,jobTypeID,readonly,component){
		$.ajax({
			url:service.HTTP_PREFIX + "/user/getUsersByJobtype/" + jobTypeID, 
			success:function(data){
				if($("#" + id).data("kendoMultiSelect")){
//					$("#" + id).data("kendoMultiSelect").dataSource(data.users);
				}else{
					$("#" + id).kendoMultiSelect({
						maxSelectedItems: 1,
			            dataTextField: "realUserName",
			            dataValueField: "id",
			            dataSource: data.users.reviews
			        });
					if(readonly){
						$("#" + id).data(component).readonly();
					}
				}
			},
			error:function(){
				console.log("error: Faield try to access the url - /lims-core/service/user/byrole/" + roleID);
			}
		});
	}
	
	service.buildGrid = function (id,columns,width,height,ds){
		var element = $("#" + id).kendoGrid({
			dataSource: ds == undefined ? []:ds,
					filterable: {
						extra: false,
						messages: lims.gridfilterMessage(),
					 	operators: {
					      string: lims.gridfilterOperators(),
					      number: lims.gridfilterOperatorsNumber(),
					    }
				    },
			height: height == null ?470:height,
	        width: width == null ? 600:width,
	        sortable: true,
	        selectable: true,
	        resizable: true,
	        toolbar: [
	        		  {template: kendo.template($("#toolbar_template").html())}
	                  ],
	        pageable: {
	            refresh: true,
	            pageSizes: [10, 20, 100],
	            messages: lims.gridPageMessage(),
	        },
	        columns: columns
		});
	}
	
	service.searchDataSource = function(name){
		return  new kendo.data.DataSource({
			transport: {
                read: {
                	url:lims.service.HTTP_PREFIX + "/sp/search?name=" + name,
                	type:"GET"
                }
            },
            schema: {
                data: "sps",
                total: function(response) {
                    return response.sps.length;
                },
				pageSizes: 10
            },
			page:1
		});
	}
	
	service.searchSpByKeywords = function(id, keywords){
		var ds = lims.service.searchDataSource(keywords);
		$("#" + id).data("kendoGrid").setDataSource(ds);
	}
});