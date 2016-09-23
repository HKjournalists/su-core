$(function(){
	var fsn = window.fsn = window.fsn || {};
	var me = fsn.business_category = fsn.business_category || {};
	me.model = new fsn.Model({
		//data
		data : {},
		ds_name : "businessCategoryList",
		criteria : {},
		
		//dom
		view : $("#business_category_view"),
		datasource : null,
		grid : $("#business_category_grid"),
		grid_obj : null,
		columns : null,
		
		//url
		create_url : "/business/business-category",
		fetch_url : "/business/business-category/",
		update_url : "/business/business-category",
		delete_url : "/business/business-category/",
		search_url : "/business/business-categorys"
	});
	if($("#toolbar").length != 0){
		me.model.toolbar=[{
			template:kendo.template($("#toolbar").html())	
		}];
	}
	me.model.columns = [
							                   {field: "id",title: fsn.l("Id"),width: "110px"},
							                   {field: "code",title: fsn.l("Code"),width: "110px"},
							                   {field: "name",title: fsn.l("Name"),width: "110px"},
							                   { command: [
		                               {
										  name:"Edit",
										  text:"<span class='k-icon k-edit'></span>"+fsn.l("Edit"),
										  click:function(e){
											  var ds = this;
											  $("#msg_success").html('');
											  var tr = $(e.target).closest("tr");
											  var id = ds.dataItem(tr).id;
											  var name = ds.dataItem(tr).name;
											  window.location.replace(fsn.CONTEXT_PATH + "/views/business/business-category.html?id=" + id);
										  }
		                                },
			                            {
		                            	   name: "Delete",
		                            	  text:"<span class='k-icon k-delete'></span>"+fsn.l("Delete"),
		                            	   click: function(e){
		                            		   var ds = this;
		                            		   var deleteRow = $(e.target).closest("tr");
		                            		   $("#msg_success").html('');
		                            		   var id = ds.dataItem(deleteRow).id;
		                            		   var name = ds.dataItem(deleteRow).name;
		                            		   console.log("----Start delete BusinessCategory:" + id);
		                            		   me.model.data.id = id;
		                            		   me.model.delete(function(response){
		                            			   var result = response.RESTResult;
		                            			   if(result.statusCode == 0){
		                            				   ds.dataSource.remove(ds.dataItem(deleteRow));
		                            				   me.model.refreshGrid();
		                            				   //me.model.grid.data("kendoGrid").refresh();
		                            				   $("#msg_success").html("BusinessCategory " + '"<span class="font_blue_underline">'+name+'</span>"' + " is deleted successfully!");
													}else{
														$("#msg_success").html("Cannot delete BusinessCategory " + '"<span class="font_red_underline">'+name+'</span>"' + "!");
													}
		                            		   });
		                            	   }
			                            }
			                           ], title:fsn.l("Operation"), width: "120px" }
		                   
		               ];
	
	me.initView = function(){
		var params = fsn.getUrlVars();
		me.model.data.id = params[params[0]];
		if(!me.model.data.id){
			var businessCategory = fsn.session('businessCategory');
			me.model.data.id = businessCategory ? businessCategory.id : null;
		}
		if(!me.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				me.model.data.id = user.userId;
			}
		}
		
		if(me.model.data.id){
			me.model.fetch(function(response){
				me.model.data = response.businessCategory;
				if(me.model.data){
					fsn.log("id:" + me.model.data.id);
					fsn.log("name:" + me.model.data.name);
					me._bindView(me.model.data);
				}
			});
		}else{
			me._bindView({});
		}
	};
	
	me._bindView = function(data){
		var viewModel = kendo.observable({
		    businessCategory: data,
		    hasChanges: false,
		    save: function() {
		    	me.model.data = this.get("businessCategory");
		    	me.model.save(function(response){
		    		var result = response.RESTResult;
		    		if(result.message){
		    			return;
		    		}
		    		if(result.statusCode == 0){
		    			if(me.model.data.id){
		    				$("#msg_success").html(me.model.data.name + " is updated sucessfully.");
		    			}else{
		    				me.model.data.id = result.data.id;
		    				$("#msg_success").html(me.model.data.name + " is created sucessfully.");
		    			}
		    		}
		    	});
		        this.set("hasChanges", false);
		    },
		    reset: function(){
		    	this.set("businessCategory", me.model.data);
		    },
		    change: function() {
		        this.set("hasChanges", true);
		    }
		});

		kendo.bind(me.model.view, viewModel); 
	};
	
	me.initGrid = function(){
		
		me.model.renderGrid();
		//binding adding business unit
		$("#add_business_category").click(function(){
			window.location.pathname = fsn.CONTEXT_PATH +"/views/business/business-category.html";
		});

	}
	
});