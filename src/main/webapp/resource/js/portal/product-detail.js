$(function(){
	
	// 向portal添加product_detail
	(function(){
		var portal = fsn.portal = fsn.portal || {},
			product_detail = portal.product_detail = portal.product_detail || {};
		
		// 初始化product_detail内容
		// 1.TestReport：向product_detail中添加ShowTestReport对象
		(function(){
			var ShowTestReport = (function(){
				function showTestRep(){}
				
				// 拼接检测报告的url
				showTestRep.prototype.createUrlOfTestReport = function(type, date, sn){
					var req_url = portal.HTTP_PREFIX+"/portal/test_report"
						+ "?id=" + portal.getSearchParam(window.location.search, "proid")
						+ "&type=" + type
						+ "&date=" + (date||"")
						+ "&sn=" + (sn||1);
					return req_url;
				};
				
				/**
				 * 检测报告主体部分信息
				 * @param m testreport 的哪个部分，main和item
				 * @param k 需要的内容 html和dataModel
				 */
				showTestRep.prototype.testReportBaseInfo = function(m,k){
					var info = {"main":{},"item":{}};
					// 显示检测报告主体信息部分的html代码。
					info.main.html = '<div class="report-div"><table class="report_table" style="height:300px;width:600px;border: 1px solid rgb(197,197,197);">'
						+'<tbody><tr><th>样品名称</th><td title="sampleName"></td><th>检验类别</th><td title="testType"></td></tr>'
						+'<tr><th>样品编号</th><td title="serial"></td><th>抽样日期</th><td title="samplingDate"></td></tr>'
						+'<tr><th>商标</th><td title="tradeMark"></td><th>批号/日期</th><td title="batchSN"></td></tr>'
						+'<tr><th>生产企业</th><td title="enterprise" colspan="3"></td></tr>'
						+'<tr><th>抽样单位</th><td title="samplingUnit" colspan="3"></td></tr>'
						+'<tr><th>规格型号</th><td title="format"></td><th>样品状态</th><td title="status"></td></tr>'
						+'<tr><th>抽样量</th><td title="sampleQuantity"></td><th>检测日期</th><td title="testDate"></td></tr>'
						+'<tr><th>受检单位（被检测人）</th><td title="testee"></td><th>主要仪器</th><td title="instrument"></td></tr>'
						+'<tr><th>抽样地点</th><td title="samplingLocation" colspan="3"></td></tr>'
						+'<tr><th>判定依据</th><td title="standard" colspan="3"></td></tr>'
						+'<tr><th>检验结论</th><td title="result" colspan="3"></td></tr>'
						+'<tr><th>备注</th><td title="remark"></td><th>单位盖章</th><td title=""></td></tr>'
						+'</tbody></table></div>';
					// 检测报告主体信息部分的数据格式
					info.main.dataModel = {"RESTResult":{"status":0,"message":0,"data":{
			           		"sampleName":"","testType":"","serial":"","testDate":"",
			               	"tradeMark":"","batchSN":"","enterprise":"",
			               	"samplingUnit":"","format":"","status":"",
			               	"sampleQuantity":"","testee":"","instrument":"",
			               	"samplingLocation":"","standard":"","result":"",
			               	"remark":"","单位盖章":"","testPropertyList":[],"total":1,"current":1}          
		          	}};
					
					// 检测项目展示表格的html代码
					info.item.html = '<div class="mygrid" style="height: 200p;width:600px;"></div>';
					// 检测项目表格的columns模板
					info.item.dataModel = [{field: "name",title: "检测项目"},{field: "unit",title: "计量单位"},
			                         	{field: "techIndicator",title: "技术指标"},{field: "result",title: "检测结果"},
			                         	{field: "assessment",title: "单项评价"},{field: "standard",title: "检测依据"}];
					
					return info[m][k];
				};
				
				/**
				 * 请求返回后调用的方法
				 * @param {json} data 成功返回后的检测报告数据
				 * @param {object} $repoDom 要操作的dom对象
				 */
				showTestRep.prototype.writeRepoToDom = function(data, $repoDom, type){
					
					var _ShowTestReport = this;
                	
                	// 创建检测报告MAIN和Item部分
					$repoDom.attr("visibility", "hidden")
						.html(_ShowTestReport.testReportBaseInfo("main", "html")
									+ _ShowTestReport.testReportBaseInfo("item", "html"))
						.attr("visibility", "visible");
                	
                    // 在报告数据显示容器第一个子元素DIV添加ID，并添加纵向滚动条
//					$repoDom.children().first().attr('id',type+"ScrollBar");
//					portal.createCustScroll(type+"ScrollBar","300",1);
                	
                	// 设置该模块下的PDF下载链接
                	// 如果我们获取到的检测报告内容为空，则先设置页面的PDF下载链接为指向本页面
                	if(data.data == null){
                		$repoDom.next().find("a.pdfUrl").attr("href", "#");
                	}else{
                		// 检测主体数据填充
            			$.each(data.data,function(i,v){
            				if(i=="pdfUrl"){
            					var _$pdfUrl_a = $repoDom.next().find("a.pdfUrl"), 
            						_v = (v==""||v==null)?"#":v;
            					_$pdfUrl_a.attr("href", _v);
								if(_v == "#"){
									_$pdfUrl_a.removeAttr("target");
								}else{
									_$pdfUrl_a.attr("target", "_blank");
								}
							}else{
								var _repo_attr_td = $repoDom.find(".report_table td[title="+i+"]");
                				if(_repo_attr_td[0]){
									_repo_attr_td.html(v);
                				}
							}
            				
						});
                    	
                    	// 检测项目数据填充
            			$repoDom.find("div.mygrid").kendoGrid({
							dataSource: data.data.testPropertyList ,
							scrollable: false,
	                       	pageable: false,
	                       	/*dataBound:function(e){ // 数据加载完成后调用
	                       		//在报告数据显示容器第二个子元素DIV添加ID，并添加纵向滚动条
	                			//$repoDom.children().next().attr('id',type+"mygrid");
	                			// kendo异步加载数据导致在切换tab的时候，grid数据为填充完成及执行生产滚动条的方法，错误
	        					//portal.createCustScroll(type+"mygrid","200",1);
	                       	},*/
	                       	columns: _ShowTestReport.testReportBaseInfo("item", "dataModel")
	  					});
            			
            			if(data.data.testPropertyList.length>=5){
            				//在报告数据显示容器第二个子元素DIV添加ID，并添加纵向滚动条
                			$repoDom.children().next().attr('id',type+"mygrid");
                			// kendo异步加载数据导致在切换tab的时候，grid数据为填充完成及执行生产滚动条的方法，错误
        					portal.createCustScroll(type+"mygrid","200",1);
            			}
            			
            			/*//在报告数据显示容器第二个子元素DIV添加ID，并添加纵向滚动条
            			$repoDom.children().next().attr('id',type+"mygrid");
            			// kendo异步加载数据导致在切换tab的时候，grid数据为填充完成及执行生产滚动条的方法，错误
    					portal.createCustScroll(type+"mygrid","200",1);*/
                    	
    					// 记录检测页码情况：总页数和当前页数
                    	$("#test-report-current").val(data.data.current);
    					$("#test-report-total").val(data.data.total);
                	}
				};
				
				/**
				 * 检测数据分页按钮展示
				 */
				showTestRep.prototype.showPageButt = function(data,dom){
					
					var $prevDiv = dom.children().first()/* 上一页容器 */,
						$nextDiv = dom.children().next()/* 下一页容器 */,
						nPrev/* 前一页页码 */, nNext /* 后一页页码 */;
					
					//返回检测数据为空时，上一页下一页按钮变灰色
					if(data.status==0){
						
						$prevDiv.css('background-position', '-29px -0px');
						$nextDiv.css('background-position', '-29px -28px');
						
					}else{//返回检测数据不为空时
						
						var nCurrent = data.data.current/* 当前页 */, nTatol = data.data.total/* 总页数 */;
						nPrev = parseInt(nCurrent)-1;
						nNext = parseInt(nCurrent)+1;
						// 上一页按钮的灰绿切换
						$prevDiv.css('background-position', (nPrev<1)?'-29px -0px':'-0px -0px');
						// 下一页按钮的灰绿切换
						$nextDiv.css('background-position', (nNext>nTatol)?'-29px -28px':'-0px -28px');
						
					}
				};
				
				/**
				 * @param server_data
				 * @param check_type 产看的报告类型
				 * @param production_data 生产日期
				 * @param sn 序号
				 */
				showTestRep.prototype.show = function(server_data, check_type, production_data, sn){
					
					var type = check_type||"self", _ShowTestReport = this;
		           
					$.ajax({ //发送ajax请求获取data
		           		url:_ShowTestReport.createUrlOfTestReport(type, production_data, sn),
		           		method:"GET",
		           		datatype: "json",
		        		success:function(data){
		        			// 为适应页面DOM结构，先获取该模块的名称_m_name,然后获取报告数据显示容器 class = _m_name;
		        			var _m_name = (type+"_report"), _$report_div = $("."+_m_name);
		        			
		        			//显示检测报告分页
		        			_ShowTestReport.showPageButt(data.RESTResult,_$report_div.next().children("span:first"));
		        			
		        			if(data.RESTResult.status == "0"){// 如果请求返回的状态为0，则表示没有查找到检测报告，显示无数据图片
		        				_$report_div.html("<img src='../../resource/img/portal/nodata1.png'>");
		                    }else{// 状态为1，则表示成功获取带检测报告，将数据写入到页面
		                    	_ShowTestReport.writeRepoToDom(data.RESTResult, _$report_div, type);
		                    	$(".productiondate-span input[type=text]").each(function(){
		                    		this.value="";
		                    	});
		                    }
						}
					});  
				};
				return showTestRep;
			})();
			this.ShowTestReport = new ShowTestReport();
		}).call(product_detail);
		
		/*
		 * 2.NatriReport：营养报告
		 * 该类依赖kendo ui
		 */ 
		(function(){
			var ShowNatriRepo = (function(){
				function fnSNR(){}
				
				// 组装nutrition data
				fnSNR.prototype.transformData = function(data){
					var nrv_arr = [],
						categories_name_arr = [],
						categories_columns_arr=[{"field": "category","title": "指标分类"}],
						indicatorsChart=[],
						per_str="含量/"+data.per,
						indicators_obj={"category":per_str},
						stdIndicators_obj={"category":"一天摄入"};
					
		            for(var i=0; i<data.list.length; i++){
		                var categories_name = data.list[i].name+"/"+data.list[i].unit, 
		                	category_obj={};
		                
		                category_obj["field"] = "content"+i;
		                category_obj["title"] = categories_name;
		                categories_columns_arr.push(category_obj);

		                indicators_obj["content"+i] = data.list[i].value;
		                stdIndicators_obj["content"+i] = data.list[i].dailyIntake;
		                
		                categories_name_arr.push(categories_name);
		                nrv_arr.push(data.list[i].nrv);
		            }
		            
		            indicatorsChart.push(indicators_obj);
		            indicatorsChart.push(stdIndicators_obj);

		            var transformed_data={};
		            transformed_data["categoriesSource"]=indicatorsChart;
		            transformed_data["categoriesColumns"]=categories_columns_arr;
		            transformed_data["categoriesNrv"]=nrv_arr;
		            transformed_data["categoriesName"]=categories_name_arr;
		            
		            return transformed_data;
				};
				
				// 创建营养报告柱状图
				fnSNR.prototype.createChart = function(nutri, $dom){
					$dom.kendoChart({
						title: {text: "营养指标"}, legend: {visible: false},
			            seriesDefaults: {type: "bar"},
			            series: [{name: "标准摄入百分比/%",data: nutri.categoriesNrv}],
	            		valueAxis: {
	            			max: 20,
		                   line: {
		                       visible: false
		                   },
		                   minorGridLines: {
		                      visible: true
		                   }
		               },
		               categoryAxis: {
		                   categories: nutri.categoriesName,
		                   majorGridLines: {
		                       visible: false
		                   }
		               },
		               tooltip: {
		                   visible: true,
		                   template: "#= series.name #: #= value #"
		               }
					});
				};
				
				// 创建营养报告表
				fnSNR.prototype.createGrid = function(nutri, $dom){
					$dom.kendoGrid({
		               dataSource: nutri.categoriesSource,
		               pageable: false,
		               scrollable: false,
		               columns: nutri.categoriesColumns
		           });
				};
				return fnSNR;
			})();
			
			/**
			 * 对外开放的接口
			 * @param opt {json} {"data":营养报告数据, 
			 * 					  "chart":展示柱状图true or false,
			 * 					  "grid":展示表格true or false}
			 */ 
			ShowNatriRepo.show = function(opt){
				var _snr = new ShowNatriRepo(opt);
				if(opt.data){
					var _trfdata = _snr.transformData(opt.data);
					
					if(opt.chart){
						_snr.createChart(_trfdata, $("#nutri-chart"));
					}
					if(opt.grid){
						_snr.createGrid(_trfdata, $("#nutri-grid"));
					}
				}
			};
			
			this.ShowNatriRepo = ShowNatriRepo;
		}).call(product_detail);
		
		// 3.ProductInfo：展示产品的基本信息
		(function(){
			var ShowProductInfo = (function(){
				function showProduct(){
					
					/* 展示产品图片 */ 
					var _createShowImgList = function(arrimg, pname){
							var $zoompic = $('.zoompic')/* 大图的容器 */, 
								$csmall = $('.thumbnail')/* 小图的容器 */;
							
							if(arrimg!=null){
								// 图片数量不能超过4
								for(var i = 0, l = arrimg.length<4?arrimg.length:4; i<l; i++){
									if (arrimg[i] != "null") {
										var $smallImgLi = $('<li'+(i==0?(' class="current"'):'')+'></li>');
										$smallImgLi.append('<a href="'+arrimg[i]+'"title="'+pname+'">'
												+'<img src="'+arrimg[i]+'" title="'+pname+'"></a>');
										$csmall.append($smallImgLi);
									}
								}
								
								$zoompic.html('<a href="#" rel="nofollow">'
									+'<img src="'+arrimg[0]+'" title="'+pname+'">'
									+'</a>');
							}
							
							//产品图片轮播函数
							showgallery();
				        },
				        
				        /* 设置标题 */
				        _writeProductTitle = function(title){
							title = title==null?"暂无信息":title;
							$("div.product-title-div").find("span").attr("title", title).html(title);
						},
						
						/* 产品介绍 */
						_writeProductDes = function(des){
							des = des==null?"暂无信息":des;
							$("div.product-detail-div").find("p").text(des);
						},
						
						/* 主要成分 */
						_writeProductMainIngredient = function(pi){
							pi = pi==null?"暂无信息":pi;
				        	$("#maininGredient").text(pi);
				        },

						/* 适宜人群 */
						_writeProductApply = function(pa){
							pa = pa==null?"暂无信息":pa;
				        	$("#appropriate").text(pa);
						},

						/* 根据认证信息动态创建认证信息列表 */
						_createProductCertList = function(cert){
							// 找到容器
							var $cont = $("div.product-cert-div").eq(0);
							var $ul = $('<ul></ul>');
							if (cert != null && cert.length > 0) {
								for (var i = 0; i < cert.length; i++) {
									if (cert[i].imgUrl != null && cert[i] != '')
										$ul.append($('<li title="'
												+ cert[i].name
												+ '"><img src="'
												+ (cert[i].imgUrl == null ? "" : cert[i].imgUrl.replace("/128/", "/50/"))
												+ '" data-doc="'+cert[i].documentUrl+'"><a href="' 
												+ (window.location.protocol?window.location.protocol:"http:")
												+ "//"
												+ window.location.hostname
												+ (window.location.port==""?"":(":"+window.location.port))
												+ portal.CONTEXT_PATH
												+ '/ui/portal/certdoc'
												+ '" target="_blank"></a></li>'));
								}

								// 在这里给每个认证都添加以下查看认证证书文件的click事件
								if ($ul.find("li")[0]) {
									$ul.on("click", "img", function() {
										// 将路径设置到cookie中
										portal.session("tmp-docurl", $(this).attr("data-doc"));
										$(this).next()[0].click();
									});
								}

							} else {
								$ul.append('<li>暂无认证信息</li>');
							}
							$cont.attr('visibility', 'hidden').append($ul).attr('visibility', 'visibile');
						},
						
						/* 检测评分 */
						_writeProductScore = function(score, has){
							// 找到容器
							var $cont = $("#tabstrip_testreport>ul>li");
							var $listImg = $cont.find('div.img>img');
							for(var i=0; i<$listImg.length; i++){
								$listImg.eq(i).attr(
										{"src":(((has[i] == true) &&( score[i] > 3))? fsn.portal.CONTEXT_PATH+'/resource/img/portal/acc.png'
												:(((has[i] == true) && (score[i] > 2)) ? fsn.portal.CONTEXT_PATH+'/resource/img/portal/soso.png'
														: ((has[i] == true) ?fsn.portal.CONTEXT_PATH+'/resource/img/portal/not-acc.png'
																: ''))),
										"height":(has[i] == true)? 25:0,
										"width" :(has[i] == true)? 25:0,
										 "title":("综合评分：" + score[i]+" 分，点击可查看检测数据单！")});
							}
							
							// 检测报告显示
							var type_arr = ['self','censor','sample'], _type_rep = null;
							for (var j=0; j<has.length; j++) {
								if (has[j]) {
									_type_rep = type_arr[j];
									break;
								}
							}
							_type_rep == null &&  (_type_rep = type_arr[0]);
							$("#"+_type_rep).click();
						},
						
						/* 营养指标报告展示 */
						_nutriWindowListen = function(o){
							product_detail.ShowNatriRepo.show(o);
						},
					
						/* 上面定义好了方法，下面就是拼装了 */
						_show = function(product){
							// 产品图片展示
							_createShowImgList(product.imgUrlList, product.name);
							// 设置标题
							_writeProductTitle(product.name);
							// 产品介绍
							_writeProductDes(product.des);
							// 根据认证信息动态创建认证信息列表
							_createProductCertList(product.productCertification);
							// 检测评分 and 检测报告显示
							_writeProductScore([product.qscoreSelf,product.qscoreCensor,product.qscoreSample]
																,[product.hasSelf,product.hasCensor,product.hasSample]);
							// 主要成分
							_writeProductMainIngredient(product.ingredient);
							//适宜人群
							_writeProductApply(product.cstm);
							//营养指标详情
			                if((product.report.cstm != null)&&(product.report.ingredient != null)){
			                	_nutriWindowListen({"data":product.report, "chart":true, "grid":true});
			                } else {
			                	$(".chart-wrapper").html("<img src='../../resource/img/portal/nodata2.png'>");
			                }
			                
			                portal.createCustScroll("content-1","110",1);//产品简介显示纵向滚动条
			                portal.createCustScroll("content-2","110",1);//主要成分显示纵向滚动条
			                portal.createCustScroll("content-3","500",1);//营养报告显示纵向滚动条
			                portal.createCustScroll("nutri-grid","500",2);//营养报告指标分类显示横向滚动条
						};
					/* 这里开放：ajax请求，获取product数据 */
					this.show = function(opt){
						var _opt = $.extend({},{
							type: "GET",
							datatype: "json",
							success: function(data){
								var result = data.RESTResult;
								if(result.status==1){
									_show(result.data);
								}else{
									alert(result.message);
								}
							}
						},opt);
						
						$.ajax(_opt);
					};
				}
				
				return new showProduct();
			})();
			this.ShowProductInfo = ShowProductInfo;
		}).call(product_detail);
		
		
		
		// product-detail页面的初始化方法
		product_detail.init = function(){
			
			// 初始化kendo table插件的显示
			$("#tabstrip_testreport, #tabstrip_nutrireport").kendoTabStrip().data("kendoTabStrip");
//	    	$("#tabstrip_nutrireport").kendoTabStrip().data("kendoTabStrip");
		    $("#nutri_report").click();
			
			// 根据Product.id去获取product信息
			product_detail.ShowProductInfo.show({url:portal.HTTP_PREFIX+"/portal/product/"+portal.getSearchParam(window.location.search, "proid")});
			// 展示检测报告，页面初始化的时候会执行一次show
			// product_detail.ShowTestReport.show(null, null, null, null);
			
			//展示评论
//			product_detail.ProdCommit.show(null,null);
			// 日期后面的查看按钮
			$("span.productiondate-span").on("click", "img", function(){
				// 需求：选择日期后，所有的检测都会根据这个日期去查询；所以在这里将当前选择的日期同步到另外两个日历控件input中
				$("span.productiondate-span").find("input[type=text]").val($(this).prev("input").val());
				product_detail.ShowTestReport.show(null, this.id.split("_")[0], $(this).prev("input").val(), null);
			});
			// Tab的点击事件，每次都会请求数据
			$(".showtest").on("click", function(){
				$("#test-report-current").val(1);
				$("#test-report-total").val(1);
				var type = $(this).attr("data-type");
				product_detail.ShowTestReport.show(null, type, $("#"+type+"_report_pd_search").prev("input").val(), null);
			});
			// 翻页按钮事件
			$(".kk-button").on('click',function(){
				var title = $(this).attr("title"),
		        	type = title.split("-")[0], // 检测报告类型
		        	current =  $("#test-report-current").val(), 
		        	total = $("#test-report-total").val(),
		        	action = title.split("-")[1]; // 向前还是向后翻页
				
				if (isNaN(parseInt(current))) {
					alert("翻页错误，请刷新页面后重试！");
					return false;
				}
				
				if (isNaN(parseInt(total))) {
					alert("翻页错误，请刷新页面后重试！");
					return false;
				}
				
				function toInt(s) {
					var r = s;
					typeof s === 'string' && (r = parseInt(s));
					return r;
				}
				current = toInt(current);
				total = toInt(total);
				if(action=="prev"){
		           	if((current-1)>=1 && (current-1)<=total){
						current=parseInt(current)-1;
		           		// 加入日期的翻页
						product_detail.ShowTestReport.show(null, type, $("#"+type+"_report_pd_search").prev("input").val(), current);
		           		// 不加入日期的翻页
		           		// ShowTestReport.show(null, type, null, current);
					} else {
						alert("没有前一页了！");
					}
				}
				if(action=="next"){
		           	if((current+1)>=1 && (current+1)<=total){
		           		current=parseInt(current)+1;
		           		// 加入日期的翻页
		           		product_detail.ShowTestReport.show(null, type, $("#"+type+"_report_pd_search").prev("input").val(), current);
		           		// 不加入日期的翻页
		           		// ShowTestReport.show(null, type, null, current);
		           	} else {
		           		alert("没有后一页了！");
		           	}
				}
	                    
			});
		};
	})();
	fsn.portal.product_detail.init();
});