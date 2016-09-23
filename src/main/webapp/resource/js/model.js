$(function(){
	var fsn = window.fsn = window.fsn || {};
	var filter = window.filter = window.filter || {};
	fsn.Model = function(config) {
		this._init( config );
	};

	var proto = fsn.Model.prototype;
	proto._init = function(config){
		//data
		this.data = config.data;
		this.ds_name = config.ds_name;
		this.criteria = config.criteria;
		
		//dom
		this.view = config.view;
		this.datasource = config.datasource;
		this.grid = config.grid;
		this.grid_obj = config.grid_obj;
		this.columns = config.columns;
		this.toolbar=config.toolbar;
		this.detail_win = config.detail_win;
		this.detail_win_obj = config.detail_win_obj;
		
		//url
		this.create_url = fsn.HTTP_PREFIX + config.create_url;
		this.fetch_url = fsn.HTTP_PREFIX + config.fetch_url;
		this.update_url = fsn.HTTP_PREFIX + config.update_url;
		this.delete_url = fsn.HTTP_PREFIX + config.delete_url;
		this.search_url = fsn.HTTP_PREFIX + config.search_url;
		this.search_all_url = fsn.HTTP_PREFIX + config.search_all_url;
		this.publish_url=fsn.HTTP_PREFIX+config.publish_url;
		this.backout_url=fsn.HTTP_PREFIX+config.backout_url;
		this.calculate_url = fsn.HTTP_PREFIX+config.calculate_url;
		
		//config
		this.page_size = config.page_size || 5;
		this.editable = config.editable;
		this.toolbar = config.toolbar;
	};
	
	proto.fetch = function(_callback){
		$.ajax({
            url: this.fetch_url + this.data.id,
            type: "GET",
            dataType: 'json',
            success: function(response, state) {
                fsn.callBackFunction(_callback, response);
            },
            error: function(response, state){
            	
            }
        });
	};
	
	proto.renderGrid = function(data, _callback){
		if(data){
			this.refreshGrid(data);
		}else{			
			if(this.ds_name=="businessUnitList" || this.ds_name=="productList" || this.ds_name=="testResultList"){
				this.createBatchDS();
			}else{
				this.createDS();
			}
			this.refreshGrid();
		}
	};
	
	
	proto.createBatchDS = function(){
		var m = this;
		if(!m.datasource){
			m.datasource = new kendo.data.DataSource({
				transport: {
	                read: {
	                	async:false,
	                	url: m.search_all_url,
	                	dataType: "json",
	                	type: "POST",
	                },
                    update: {
                        url: m.update_url,
                        dataType: "json",
                        type: "PUT"
                       
                    },
                    create: {
                        url: m.create_url,
                        dataType: "json",
                        type: "POST"
                       
                    },
                    parameterMap : function(options, operation) {
                        if (operation == "read") {                        
                        	m.criteria.page= options.page;    //当前页
                        	m.criteria.pageSize = options.pageSize; //每页显示个数
                        	/*if($("#test_result_grid").data("kendoGrid")&&$("#test_result_publish_grid").data("kendoGrid")&&$("#structured_test_result_grid").data("kendoGrid")&&m.criteria.page>1){
                        		if($("#test_result_grid").data("kendoGrid").dataSource._data.length<1||
                                		$("#test_result_publish_grid").data("kendoGrid").dataSource._data.length<1){
                                	m.criteria.page-=1;
                    			}
                        	}*/
                        	if(options.filter){
                        		m.criteria.configure=filter.configure(options.filter);
                        	}else{
                        		m.criteria.configure=null;
                        	}
                            return {criteria : kendo.stringify(m.criteria)};
                        }
                    }
	            },
	            schema: {	
	            	 data : function(response) {
	                      return response[m.ds_name]; //响应到页面的数据
	                  },
	                  total : function(response) {
		                    return response["totalCount"];
	                  }
	            },
	            pageSize: m.page_size,
				batch: true,
				serverPaging : true,
			    serverFiltering : true,
			    serverSorting : true
			});
			
		}
	};
	proto.createDS = function(){
		var m = this;
		if(!m.datasource){
				m.datasource = new kendo.data.DataSource({
					transport: {
		                read: {
		                	async:false,
		                	url: m.search_url,
		                	dataType: "json",
		                	type: "POST",
		                	data: {criteria : kendo.stringify(m.criteria)}
		                },
	                    update: {
	                        url: m.update_url,
	                        dataType: "json",
	                        type: "PUT"
	                       
	                    },
	                    create: {
	                        url: m.create_url,
	                        dataType: "json",
	                        type: "POST"
	                       
	                    }
		            },
		            schema: {
		                data: m.ds_name,
		                total: function(response) {
		                    return response[m.ds_name].length;
		                }
		            },
					pageSize: m.page_size,
					batch: true
				});
			
		}
	};
	
	proto.refreshGrid = function(data){
		var m = this;
		if(data){
			m.datasource = {
					data :data,
					pageSize: m.page_size,
					batch: true
			};
		}
		if(!m.grid_obj){
			var conf = {
					dataSource: m.datasource,
		            filterable: {
						messages: fsn.gridfilterMessage(),
						operators: {
							  string: fsn.gridfilterOperators()
						}
					},
		            sortable: true,
			        resizable: true,
		            selectable: true,
		            pageable: {
		                refresh: true,
		                pageSizes: true,
		                messages: fsn.gridPageMessage(),
		            },
		            columns: m.columns
				};
			if(m.toolbar){
				conf.toolbar = m.toolbar;
			}
			if(m.toolbar){
				conf.editable = m.editable;
			}
			m.grid_obj = m.grid.kendoGrid(conf);
		}else{
			//m.grid_obj.setDataSource(m.datasource);
			m.grid.data("kendoGrid").refresh();
		}
	};
	
	proto.save = function(_callback){
		if(this.data && this.data.id){
			$.ajax({
	            url: this.update_url,
	            type: "PUT",
	            data: {model : kendo.stringify(this.data)},
	            dataType: 'json',
	            success: function(response, state) {
	                fsn.callBackFunction(_callback, response);
	            },
	            error: function(response, state){
	            	
	            }
	        });
		}else{
			$.ajax({
	            url: this.create_url,
	            type: "POST",
	            data: {model : kendo.stringify(this.data)},
	            dataType: 'json',
	            success: function(response, state) {
	                fsn.callBackFunction(_callback, response);
	            },
	            error: function(response, state){
	            	
	            }
	        });
		}
	};
	
	proto.delete = function(_callback){
		$.ajax({
            url: this.delete_url + this.data.id,
            type: "DELETE",
            dataType: 'json',
            success: function(response, state) {
                fsn.callBackFunction(_callback, response);
            },
            error: function(response, state){
            	
            }
        });
	};
	
	proto.publishToPortal = function(_callback){
		$.ajax({
            url: proto.publishUrl+ proto.dataId,
            type: "PUT",
            dataType: 'json',
            success: function(response, state) {
            	//fsn.liutong = false;
                fsn.callBackFunction(_callback, response);
            },
            error: function(response, state){
            	
            },
        });
	};
	
	/*审核通过生产企业信息*/
	proto.approvedProducer = function(){
		var liutongVo = null;
		if(!fsn.noDetail){
			liutongVo = fsn.createInstance();
		}
		var produceId = null;
		var org = null;
		if(fsn.testReport!=null) {
			produceId = fsn.testReport.sample.producer.id;
			org = fsn.testReport.organization;
		}
		var status = false;
		$.ajax({
			url:fsn.HTTP_PREFIX + "/liutong/approved?orgId="+org+"&produceId="+produceId+"&passFlag="+true,
			type:"PUT",
			dataType:"json",
			async:false,
			contentType: "application/json; charset=utf-8",
            data: JSON.stringify(liutongVo),
			success:function(returnValue){
				if(returnValue.result.status == "true"){
					status = true;
				}else{
					fsn.initNotificationMes('审核通过生产企业信息时，后台出现异常！',false);
				}
			},
		});
		return status;
	};
	
	proto.publicAndApproved=function(){
		/*如果是流通企业发布的报告，同事审核通过生产企业信息*/
		/*if(fsn.ltProducer){
			if(!proto.approvedProducer()) return;
		}*/
		proto.publishToPortal(proto.callback);
	};
	
	fsn.initConfirmWindow(proto.publicAndApproved,proto.closeConfirmWindow,"当前报告需要审核生产企业信息，直接发布默认生产企业信息审核通过，确定继续吗？","提示");
	
	proto.closeConfirmWindow = function(){
		$("#CONFIRM_COMMON_WIN").data("kendoWindow").close();
	};
	
	proto.publish=function(_callback){
		proto.dataId = this.data.id;
		proto.publishUrl=this.publish_url;
		proto.callback = _callback;
		proto.publicAndApproved();
		//var ltFlag = fsn.ltProducer;
		/*if(ltFlag){
			$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		}else{
			proto.publicAndApproved();
		};*/
	}
	
	//详情页面公布按钮调用方法
	proto.publishDetail=function(_callback){
		proto.dataId = this.data.id;
		proto.publishUrl=this.publish_url;
		proto.callback = _callback;
		var checkLis = window.fsn.root.checkLis;
		var checkOrg = window.fsn.root.checkOrg;
		var checkQs = window.fsn.root.checkQs;
		proto.publicAndApproved();
		/*var ltFlag = fsn.ltProducer;
		if(ltFlag){
			if(checkLis==true&&checkOrg==true&&checkQs==true){
				proto.publicAndApproved();
			}else{
				$("#CONFIRM_MSG").html("生产企业信息审核未通过，确定发布该信息吗？");
				$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
			};
		}else{
			proto.publicAndApproved();
		};*/
	}
	
	proto.backout=function(_callback){
		$.ajax({
            url: this.backout_url + this.data.id,
            type: "PUT",
            dataType: 'json',
            success: function(response, state) {
                fsn.callBackFunction(_callback, response);
            },
            error: function(response, state){
            	
            }
        });
	}
	proto.calculate=function(_callback){
		$.ajax({
            url: this.calculate_url + this.data.id,
            type: "PUT",
            dataType: 'json',
            success: function(response, state) {
                fsn.callBackFunction(_callback, response);
            },
            error: function(response, state){
            	
            }
        });
	}
});