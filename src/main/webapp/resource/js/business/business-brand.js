$(function(){
	var fsn = window.fsn = window.fsn || {};
	var me = fsn.business_brand = fsn.business_brand || {};
	me.model = new fsn.Model({
		//data
		data : {},
		ds_name : "businessBrandList",
		criteria : {},
		
		//dom
		view : $("#business_brand_view"),
		datasource : null,
		grid : $("#business_brand_grid"),
		grid_obj : null,
		columns : null,
		
		//url
		create_url : "/business/business-brand",
		fetch_url : "/business/business-brand/",
		update_url : "/business/business-brand",
		delete_url : "/business/business-brand/",
		search_url : "/business/business-brands"
	});
	
	me.model.columns = [
		                   {field: "id",title: fsn.l("Id"),width: "110px"},
							                   {field: "name",title: fsn.l("Name"),width: "110px"},
							                   {field: "identity",title: fsn.l("Identity"),width: "110px"},
							                   {field: "symbol",title: fsn.l("Symbol"),width: "110px"},
							                   {field: "logo",title: fsn.l("Logo"),width: "110px"},
							                   {field: "trademark",title: fsn.l("Trademark"),width: "110px"},
							                   {field: "cobrand",title: fsn.l("Cobrand"),width: "110px"},
							                   {field: "registrationDate",title: fsn.l("Registration Date"),width: "110px"},
							                   {field: "businessUnitId",title: fsn.l("Business Unit Id"),width: "110px"},
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
											  window.location.replace(fsn.CONTEXT_PATH +"/views/business/business-brand.html?id=" + id);
											  fsn.msg("msg_success",result,fsn.l("Edit sucessfully！"));
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
		                            		   console.log("----Start delete BusinessBrand:" + id);
		                            		   me.model.data.id = id;
		                            		   me.model.delete(function(response){
		                            			   var result = response.RESTResult;
		                            			   if(result.statusCode == 0){
		                            				   ds.dataSource.remove(ds.dataItem(deleteRow));
		                            				   me.model.refreshGrid();
		                            				   //me.model.grid.data("kendoGrid").refresh();
		                            				   $("#msg_success").html("BusinessBrand " + '"<span class="font_blue_underline">'+name+'</span>"' + " is deleted successfully!");
													}else{
														$("#msg_success").html("Cannot delete BusinessBrand " + '"<span class="font_red_underline">'+name+'</span>"' + "!");
													}
		                            		   });
		                            	   }
			                            }
			                           ], title:fsn.l("Operation"), width: "120px" }
		                   
		               ];
	
	me.initView = function(){
		var params = fsn.getUrlVars();
		me.model.data.id = params[params[0]];
		if(me.model.data.id){
			me.model.fetch(function(response){
				me.model.data = response.businessBrand;
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
		    businessBrand: data,
		    hasChanges: false,
		    save: function() {
		    	me.model.data = this.get("businessBrand");
		    	me.model.save(function(response){
		    		var result = response.RESTResult;
		    		if(result.message){
		    			return;
		    		}
		    		if(result.statusCode == 0){
		    			if(me.model.data.id){
		    				/*$("#msg_success").html(me.model.data.name + " is updated sucessfully.");*/
		    				fsn.msg("msg_success",result,fsn.l("updated sucessfully！"));
		    			}else{
		    				me.model.data.id = result.data.id;
		    				/*$("#msg_success").html(me.model.data.name + " is created sucessfully.");*/
		    				fsn.msg("msg_success",result,fsn.l("created sucessfully！"));
		    			}
		    		}
		    	});
		        this.set("hasChanges", false);
		    },
		    reset: function(){
		    	this.set("businessBrand", me.model.data);
		    },
		    change: function() {
		        this.set("hasChanges", true);
		    }
		});

		kendo.bind(me.model.view, viewModel); 
	};
	
	me.initGrid = function(){
		
		me.model.renderGrid();
	}
	
});