$(function(){
	$.fn.genericList = function(settings, relfunc) {
		var conf = $.extend({
			domId:'',
			model:null,		//所属模块
			obj:null,		//对象
			url:{create:'',edit:'',search:'',Delete:''},			//链接地址对象
			listInfo:[],
			fieldType:{},	//
			fieldName:[],	//操作时所需字段
			batchDelMsg:'',
			pagesize:15,		//默认显示15条
			showCheckbox : true,//是否显示第一列的checkBox
			toolBar : true,//是否显示toolbar
			inParam : false,//查询参数方法是否从外部传入
			searchParam : function(){},//查询方法
			editName : "编辑", //编辑按钮显示名称
			operationColumn : {},	//操作按钮
			toFormat : false,	//是否对数据进行格式化
			dataFormat : function(data){return [];} //数据格式化方法
		}, settings);

		
		var deleteItem,
			_id = conf.fieldName[0],_Name = conf.fieldName[1],
			_checkbox={ command: [{
            	name: "Check",
            	text: "<input type='checkbox' class='checkboxes'/>"
            	}],
            	width: 20, 
            	encoded : false
            };
		//operation列
		var oprationCol=$.extend({},{ command: [{
        	name:"edit",
        	text: conf.editName,
        	click: function(e){
        		var editRow = $(e.target).closest("tr");
        		var temp = this.dataItem(editRow);
        		var a = $.isNumeric(temp[_id]);
        		if(!$.isNumeric(temp[_id])){
        			temp[_id] = temp[_id].replace(/<.*?>/g,"");
        		}
        		window.location.href = conf.url.edit+"?id="+temp[_id];
        		}
        	},
        	{
        		name: "Delete",
        		text: "<span class='k-icon k-cancel'></span>"+ lims.l("Delete"),
        		click: function(e){
        			lims.clrmsg("msg_success",null);
		          		var deleteRow = $(e.target).closest("tr");
		          		deleteItem = this.dataItem(deleteRow);
		          		if(!$.isNumeric(deleteItem[_id])){
		          			deleteItem[_id] = deleteItem[_id].replace(/<.*?>/g,"");
		        		}
		          		lims.initConfirmWindow(function() {
		          			lims.clrmsg("msg_success",null);
		          			$.ajax({
		          				url:conf.url.Delete+"?ids="+deleteItem[_id],
		          				type:"DELETE",
		          				success:function(data){
		          					if(data.RESTResult.status == 1){
		          						$("#"+conf.domId).data("kendoGrid").dataSource.remove(deleteItem);
		          						lims.message("msg_success", true, '<span class="font_blue_underline">'+deleteItem[_Name]+'</span>' + lims.l("Delete Success"));
		          					}else{
		          						var result={success:false};
		          						lims.message("msg_success", result, '<span class="font_red_underline">'+deleteItem[_Name]+'</span>' + lims.l("Delete Failed"));
		          					}
		          				}
		          			});
		          			lims.closeConfirmWin();
		          		}, undefined,lims.l("WIN_CONFIRM_MSG"),true);
		          		$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
		          	}
        	}], title:"操作", width: 80 },conf.operationColumn);
		
		conf.obj.templatecolumns = [];
		
		//插入operation列
		if(conf.operationColumn!=null){
			conf.obj.templatecolumns.push(oprationCol);
		}
		
		//插入listdata到templatecolumns中
		for(var i=0;i<conf.listInfo.length;i++){
			conf.obj.templatecolumns.splice(i,0,conf.listInfo[i]);
		}
		
		//插入checkbox列
		if(conf.showCheckbox){
			conf.obj.templatecolumns.splice(0,0,_checkbox);
		}
		
		conf.obj.templateDS = function(criteria){
			return new kendo.data.DataSource({
				transport: {
					read : {
						type : "GET",
		                async:false,
		                url : function(options){
		                	if(conf.inParam){
		                		var _param=$.extend({},criteria,{"page" : options.page, "pageSize" : options.pageSize});
			                	return conf.url.search+"?criteria="+JSON.stringify(_param);
		                	}else{
		                		var _keword=criteria==undefined?"":criteria;
		                		return conf.url.search+"?keyword="+_keword+"&sn="+options.page;
		                	}
		                	
		                },
		                dataType : "json",
		                contentType : "application/json"
		            },
		        },
		        schema: {
		        	model:conf.fieldType,
		        	data : function(d) {
		        		var _data;
		        		if(d.RESTResult!=null){
		        			_data=d.RESTResult.status==1?d.RESTResult.data.list:null;
		        		}else if(d.paginationVo!=null){
		        			_data=d.paginationVo.data.length>0?d.paginationVo.data:null;
		        		}else{
		        			_data=d.data.length>0?d.data:null;
		        		}
		        		
		        		if(conf.toFormat){
	        				_data=conf.dataFormat(_data);
	        			}
		        		
		        		return _data;//响应到页面的数据
		        	},
		        	total : function(d) {
		        		if(d.RESTResult!=null){
		        			return d.RESTResult.status==1?d.RESTResult.data.totalrecord:0;
		        		}else if(d.paginationVo!=null){
		        			return d.paginationVo.totalSize;   //总条数
		        		}else{
		        			return d.totalSize; 
		        		}
		        	}         
		        },
		        batch : true,
		        page:1,
		        pageSize : conf.pagesize, //每页显示个数
		        serverPaging : true,
		        serverFiltering : true,
		        serverSorting : true
			});
		};
		
		conf.obj.searchTemplateByKeywords = function(id, criteria){
			var ds = conf.obj.templateDS(criteria);
			$("#" + id).data("kendoGrid").setDataSource(ds);
		};
		
		conf.obj.createNew = function(){
			window.location.href = conf.url.create;
		};
		
		conf.obj.dataFormat=function(list){
			var _List=[];
			for(var i=0;i<list.length;i++){
				var _obj={};
				
				for(var j=0;j<conf.listInfo.length;j++){
					var _name = conf.listInfo[j][4];
					_obj[conf.listInfo[j][0]] = list[i][_name];
				}
				_List.push(_obj);
			}
			return _List;
		};
		
		
		
		//初始化
		(function(){
			var dataSource = conf.obj.templateDS(conf.searchParam());
			//创建grid
			conf.toolBar?conf.model.buildGrid(conf.domId,conf.obj.templatecolumns, dataSource):
				conf.model.buildGridWioutToolBar(conf.domId,conf.obj.templatecolumns, dataSource);
			$("#search").on("click",function() {
				//var _this = $(this), _span = _this.find("span.ui-button-text");
				//if(_span.attr("data-lims-text") == "Search"){
					//$(this).on("click",function(){
						if(conf.inParam){
							var _param=conf.searchParam();
							conf.obj.searchTemplateByKeywords(conf.domId,_param);
						}else{
							var _param = $("#search").val();
							conf.obj.searchTemplateByKeywords(conf.domId,_param);
						}
					//});
			//	}
			}); 
			
			//去掉勾选框的所有class样式
			$("#"+conf.domId+" .k-grid-content tr td:first-child a").removeAttr("class");
			$("#"+conf.domId+" .k-grid-content").attr("style","height: 364px");
		})();
		
		//全选/反选
		$("#"+conf.domId).on("click","#checkAll",function(){
			var _this = this,_Ids="",_checkItem=$("input.checkboxes");
			
			_checkItem.each(function(){
				//勾选全选
				if(_this.checked == true){
					this.checked=true;
				}else{//取消去选
					this.checked=false;
				}
			});
		});
		
		//批量删除
		$("#main").on("click","#batchDelete",function(){
			var _Ids="",_checkItem=$("input.checkboxes:checked");
			if(_checkItem.length==0){
				alert(lims.l("WIN_CHECKDELETE_MSG"));
			}else{
				$("input.checkboxes:checked").each(function(){
					var _id = $(this).closest("td").next("td").text();
					if(!$.isNumeric(_id)){
						_id = _id.replace(/<.*?>/g,"");
	        		}
					_Ids+=_id+",";
				});
				lims.initConfirmWindow(function() {
						lims.clrmsg("msg_success",null);
			      		$.ajax({
			      			url:conf.url.Delete+"?ids="+_Ids,
			    					type:"DELETE",
			    					success:function(data){
			    						if(data.RESTResult.status == 1){
			    							conf.obj.searchTemplateByKeywords(conf.domId, $("#search").val());
			    							lims.message("msg_success", true, '"<span class="font_blue_underline">'+lims.l("Selected "+conf.batchDelMsg)+'</span>"' + lims.l("Delete Success"));
			    						}else{
			    							var result={success:false};
			    							lims.message("msg_success", result, '"<span class="font_red_underline">'+lims.l("Selected "+conf.batchDelMsg)+'</span>"' + lims.l("Delete Failed"));
			    						}
			    					}
			      			});	  		
						lims.closeConfirmWin();
				}, undefined,lims.l("WIN_BATCHCONFIRM_MSG"),true);
				$("#CONFIRM_COMMON_WIN").data("kendoWindow").open().center();
			};
		});
	};
});