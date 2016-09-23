$(function(){
	var fsn = window.fsn = window.fsn || {};
	fsn.TestResult1 = function(config){
		this._init( config );
	}
	var ids ; 
	var testResult1 = window.testResult1 = window.testResult1 || {};
	var proto = fsn.TestResult1.prototype;
	
	var mee ;
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
			search_all_url : "/test/test-results2",
			backout_url : "/test/backout/",
			calculate_url : "/test/calculate/",
			
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
		var me=this;
		mee=me;
		this.model.columns = [
			                   {field: "id",title: fsn.l("Id"),width: "80px"},
			                   {field: "serviceOrder",title: fsn.l("ReportNO"),width: "100px"},
			                   {field: "sample.product.name",title: fsn.l("Product"),width: "100px"},
			                   {field: "sample.batchSerialNo",title: fsn.l("BatchSerialNo"),width: "70px"},
			                   {field: "sample.producer.name",title: fsn.l("BrandName"),width: "140px"},		                   
			                   {field: "testType",title: fsn.l("TestType"),width: "50px"},
			                   {field: "publishDate",title: fsn.l("publishDate"),width: "100px",filterable: false},
			                   {field: "sample.product.riskIndex",title: fsn.l("riskIndexState"),width: "100px",filterable: false,
							        template: function(dataItem){
								 	if (dataItem.sample.product.risk_succeed == false || dataItem.sample.product.risk_succeed==null) {
										var riskFailure= dataItem.sample.product.riskFailure==null?'':"("+dataItem.sample.product.riskFailure+")";
								 		return "失败"+riskFailure;
								 	}  else {
								 		return dataItem.sample.product.riskIndex
//								 		return "成功";
								 	}
								 }
							       
							   },
							   {width:80,title: fsn.l("Operation"),
								      template:function(e){
										ids = e.id;
										var tag="<a  onclick='return testResult1.views("+e.id+")' class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-i-search'> </span>" + fsn.l('View Detail') + "</a>" +
											    "<a  onclick='return testResult1.back_out("+e.id+")'  class='k-button k-button-icontext k-grid-ViewDetail '><span class='k-icon k-i-search'> </span>" + fsn.l('Backout') +"</a>";
//									    if (e.sample.product.riskIndex == null) {
									    if (e.sample.product.risk_succeed == false || e.sample.product.risk_succeed==null) {
											return tag +
											"<a onclick='return testResult1.calculate("+e.id+")' class='k-button k-button-icontext k-grid-Backout' id='aid'  >重新计算</a>";
										}else{
											return tag;
										}
										return "";
									}
							    }
	       
	   ];
	};
	testResult1.views = function(id){
		      window.open("/fsn-core/views/lab/test-result-preview1.html?id=" + id);
	}
	testResult1.back_out = function(id){
		       mee.model.data.id = id;
	           mee.model.backout(function(response){
		        var result = response.RESTResult;
		        if(result.statusCode == 0){
		       // ds.dataSource.remove(ds.dataItem(tr));
		        mee.model.refreshGrid();
		        $("#test_result_grid").data("kendoGrid").dataSource.read();
		        fsn.msg("msg_success",result,fsn.l("sample backout sucessfully！"));
		        $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
			}
	   });
	}
	testResult1.calculate = function(id){
		      mee.model.data.id = id;
	          mee.model.calculate(function(response){
		      var result = response.RESTResult;
			  if(result.success==true){
		        fsn.msg("msg_success",result,fsn.l("sample calculate sucessfully！"));
			  }else{
		        fsn.msg("msg_success",result,fsn.l("失败！"));
			  }
		      mee.model.refreshGrid();
		      $("#test_result_grid").data("kendoGrid").dataSource.read();
		      $("#test_result_publish_grid").data("kendoGrid").dataSource.read();
	   });
		   
	}
	proto.viewDetail = function(e){
		var myTr = this;
		
		e.preventDefault();
		var dataItem = this.dataItem($(e.currentTarget).closest("tr"));
		wnd.content(detailsTemplate(dataItem));
		wnd.center().open();
		
		var params = fsn.getUrlVars();
		myTr.model.data.id = params[params[0]];
		if(!myTr.model.data.id){
			var testResult1 = fsn.session('testResult1');
			myTr.model.data.id = testResult1 ? testResult1.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult1;
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
			var testResult1 = fsn.session('testResult');
			myTr.model.data.id = testResult1 ? testResult1.id : null;
		}
		if(!myTr.model.data.id){
			var user = fsn.session('user');
			if(user && user.roleId == 2){
				myTr.model.data.id = user.userId;
			}
		}
		
		if(myTr.model.data.id){
			
			myTr.model.fetch(function(response){
				myTr.model.data = response.testResult1;
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
		    testResult1: data,
		    hasChanges: false,
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
		myTr.model.renderGrid();
	}
});