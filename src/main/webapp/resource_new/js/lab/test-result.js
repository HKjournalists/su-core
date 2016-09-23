$(function(){
	var fsn = window.fsn = window.fsn || {};
	fsn.TestResult = function(config){
		this._init( config );
	}
	var proto = fsn.TestResult.prototype;
	proto._init = function(config){
		config = config || {};
		this.has_add_btn = config.has_add_btn;
		this.model = new fsn.Model({
			//data
			data : {},
			ds_name : "testResultList",
			criteria : config.criteria,
			
			//dom
			view : $("#test_result_view"),
			datasource : null,
			grid : $(config.gridDiv),
			grid_obj : null,
			columns : null,
			detail_win : config.detail_win,
			detail_win_obj : null,
			
			//url
			create_url : "/test/test-result",
			fetch_url : "/test/test-result/",
			update_url : "/test/test-result",
			delete_url : "/test/test-result/",
			search_url : "/test/test-results",
			
			//config
			page_size : config.page_size,
			editable : config.editable,
			toolbar : config.toolbar
		});
		
		if(!this.model.criteria){
			var session_user = fsn.session("user");
			//init criteria
			var criteria = this.model.criteria = {};
			if(session_user){
				criteria.roleId = session_user.roleId;
				criteria.testerId = session_user.userId;
			}
		}
		this.model.columns = [
			                   {field: "testDate",title: fsn.l("Test Date"),width: "110px"},
			                   {field: "sample.product.name",title: fsn.l("Product"),width: "110px"},
			                   {field: "brand.name",title: fsn.l("BrandName"),width: "110px"},		                   
			                   {field: "result",title: fsn.l("Result"),width: "50px"},
			                   { command: [
				                   {
									  name:"View Detail",
									  text:"<span class='k-icon k-detail'></span>"+fsn.l("View Detail"),
									  click:function(e){
										  var ds = this;
										  var tr = $(e.target).closest("tr");
										  var id = ds.dataItem(tr).id;
										  var name = ds.dataItem(tr).name;
										  window.location.replace(fsn.CONTEXT_PATH + "/views/lab/test-result.html?id=" + id);
									  }
				                   }
				                  ], 
				                  title:fsn.l("Operation"), 
				                  width: "60px" 
				               }
	       
	   ];
	};
	
	proto.viewDetail = function(e){
		var myTr = this;
		
		e.preventDefault();
		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
		wnd.content(detailsTemplate(dataItem));
		wnd.center().open();
		
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(!myTr.model.data.id){
			var testResult = fsn.session('testResult');
			myTr.model.data.id = testResult ? testResult.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult;
				if(myTr.model.data){
					myTr._bindView(myTr.model.data);
				}
			});
		}else{
			myTr._bindView({});
		}
	};
	
	proto.initView = function(){
		var myTr = this;
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(!myTr.model.data.id){
			var testResult = fsn.session('testResult');
			myTr.model.data.id = testResult ? testResult.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult;
				if(myTr.model.data){
					myTr._bindView(myTr.model.data);
					fsn.test_property.model.datasource = new kendo.data.DataSource({
				    	data:myTr.model.data.testProperties,
				        batch: true,
				        pageSize: 10
				    });
					fsn.test_property.model.renderGrid();
				}
			});
		}else{
			myTr._bindView({});
		}
	};
	
	proto._bindView = function(data){
		var myTr = this;
		var viewModel = kendo.observable({
		    testResult: data,
		    hasChanges: false,
		    save: function() {
		    	myTr.model.data = this.get("testResult");
		    	myTr.model.save(function(response){
		    		var result = response.RESTResult;
		    		if(result.message){
		    			return;
		    		}
		    		if(result.statusCode == 0){
		    			if(myTr.model.data.id){
		    				$("#msg_success").html(myTr.model.data.name + " is updated sucessfully.");
		    			}else{
		    				myTr.model.data.id = result.data.id;
		    				$("#msg_success").html(myTr.model.data.name + " is created sucessfully.");
		    			}
		    		}
		    	});
		        this.set("hasChanges", false);
		    },
		    reset: function(){
		    	this.set("testResult", myTr.model.data);
		    },
		    change: function() {
		        this.set("hasChanges", true);
		    }
		});

		kendo.bind(myTr.model.view, viewModel); 
	};
	
	proto.initGrid = function(){
		var myTr = this;

		if(myTr.has_add_btn){
			//binding adding test unit
			$("#add_test_result").click(function(){
				window.location.pathname = fsn.CONTEXT_PATH +"/views/test/test-result.html";
			});
		}
		
		myTr.model.renderGrid();
	}
	
});