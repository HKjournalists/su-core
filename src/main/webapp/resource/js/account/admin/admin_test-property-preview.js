$(function(){
	var fsn = window.fsn = window.fsn || {};
	var me = fsn.test_property = fsn.test_property || {};
	var busId;
	busId = window.location.href.split("busId=")[1];
	me.model = new fsn.Model({
		//data
		data : {},
		ds_name : "testPropertyList",
		criteria : {},
		//dom
		view : $("#test_property_view"),
		datasource : null,
		grid : $("#test_property_grid"),
		grid_obj : null,
		columns : null,
		//url
		create_url : "/test/test-property",
		fetch_url : "/test/test-property/",
		update_url : "/test/test-property",
		delete_url : "/test/test-property/",
		search_url : "/test/test-propertys"
	});
	
	me.model.columns = [
       {field: "id",title: fsn.l("Id"),width: "50px"},
       {field: "name",title: fsn.l("Name"),width: "80px"},
       {field: "standard",title: fsn.l("Standard"),width: "150px"},
       {field: "techIndicator",title: fsn.l("TechIndicator"),width: "90px"},
       {field: "result",title: fsn.l("Result"),width: "80px"},
       {field: "unit",title: fsn.l("Unit"),width: "60px"},							                   
       {field: "assessment",title: fsn.l("Assessment"),width: "80px"},
    ];
	
	me.initView = function(){
		var params = fsn.getUrlVars();
		me.model.data.id = params[params[0]];
		if(!me.model.data.id){
			var testProperty = fsn.session('testProperty');
			me.model.data.id = testProperty ? testProperty.id : null;
		}
		if(!me.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				me.model.data.id = user.userId;
			}
		}
		
		if(me.model.data.id){
			me.model.fetch(function(response){
				me.model.data = response.testProperty;
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
	
	$("#BackoutToIndex").click(function(){
		window.location.href = "admin_test-result-manage.html?id="+busId;
	});
	
	me._bindView = function(data){
		var viewModel = kendo.observable({
		    testProperty: data,
		    hasChanges: false,
		    save: function() {
		    	me.model.data = this.get("testProperty");
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
		    	this.set("testProperty", me.model.data);
		    },
		    change: function() {
		        this.set("hasChanges", true);
		    }
		});

		kendo.bind(me.model.view, viewModel); 
	};
	
	me.initGrid = function(){	
		me.model.renderGrid();
	};
});