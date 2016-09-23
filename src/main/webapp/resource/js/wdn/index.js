$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var wdn=fsn.wdn=fsn.wdn||{};
	var wdnServiceBaseUrl = fsn.getHttpPrefix()+"/sampling/import/";
	var typeSelectIndex=""; //所选搜索类型index
	var selectText = "";
	var typeSelectName="全部类型";
	var selectCode = "title-all";
	wdn.popGrid=null;
	var shoppingCatData = [];
	var sortField = "score"

    wdn.dataSource = new kendo.data.DataSource({
    			type : "JSON",
    			transport : {
    					read : {
    						url : function(options){
    							return fsn.HTTP_PREFIX + "/wdn/" + encodeURI(typeSelectName) + "/" + selectCode + "/" +
    								encodeURI($("#search").val() == ""?"0":$("#search").val()) + "/" + (10*(options.page-1)) + "/"
    								+ options.pageSize + "/" + sortField;
    						}
    					}
    			},
    		schema : {
    			data : function(response) {
    				if (response.result&&response.result.re.length>0) {
    					var list = [];
    					response.result.re.forEach(function(item) {
    						if(!("authorList" in item)){
    							item.authorList = ["None"]
    							item.authors = "' '"
    						}else{
    							item.authors = item.authorList.join(" ");
    						}
    						if(!("abstract" in item)){
    							item.abstract = "' '"
    						}
    						list.push(item);
    					});
    					return list;
    				}
    				return [];
    			},
    			total : function(response) {
    				if (response.result) {
    					fsn.initNotificationMes("共检索出 " + response.result.hits.replace(",","") + "条文献!",true);
    					return response.result.hits.replace(",","");
    				}
    				return 0;
    			}
    		},
    		serverPaging : true,
    		pageSize : 10
    	});

    wdn.shoppingCatDS = new kendo.data.DataSource({
        		data: [],
        		schema: {
        			total:function(d){
        				return 50;
        			}
        		},
        	});
    wdn.searchDocument = function(){
    		if($("#search").val() == ""){
    			fsn.initNotificationMes("请输入检索词！",false);
    			return;
    		}
    		wdn.dataSource.read();
    		$("div.apply-btn a").attr("onclick", "flyMeToYou(this,$('#badge_num'),1);");
    		$("#apply").on("click",function(){
    			flyMeToYou(this,$('#badge_num'),1);
    		});
    	}

	wdn.initMenu = function(){
		wdn.kendoMenu = $("#dwn_menu").kendoMenu({
			animation: { open: { effects: "fadeIn" } }
		}).data("kendoMenu")

		wdn.kendoMenu.append([{
			text: "全部类型",
		},{
			text: "期刊论文",
		},{
			text: "图书",
		},{
			text: "学位论文",
		},{
			text: "专利",
		},{
			text: "标准",
		},{
			text: "科技报告",
		},{
			text: "期刊",
		},{
			text: "其他",
		}]);

		wdn.kendoMenu.bind("select", function(e) {
			$(".column12 ul li").removeClass('wdn-k-state-hover');
			e.item.className="k-item k-state-default wdn-k-state-hover";
			typeSelectIndex = $(".column12 ul li.wdn-k-state-hover").index();
			window.sessionStorage.setItem("typeSelectIndex",typeSelectIndex);
			console.log(e);
			switch(typeSelectIndex){
				case 0: typeSelectName = "全部类型";break;
				case 1: typeSelectName = "期刊论文";break;
				case 2: typeSelectName = "图书";break;
				case 3: typeSelectName = "学位论文";break;
				case 4: typeSelectName = "专利";break;
				case 5: typeSelectName = "标准";break;
				case 6: typeSelectName = "科研报告";break;
				case 7: typeSelectName = "期刊";break;
				case 8: typeSelectName = "其他";break;
				default:typeSelectName = ""
			}
		});


		$("ul.dropdown-menu li a").on("click",function(){
			var text = this.text;
			$(".dropdown-toggle")[0].innerHTML=text+"&nbsp<span class='caret'></span>";
			switch(text){
				case "任意字段": selectCode = "title-all";break;
				case "作者": selectCode = "author";break;
				case "文摘": selectCode = "abstract";break;
				case "关键词": selectCode = "keyword";break;
				case "ISS(B)N": selectCode = "isn";break;
				case "标题": selectCode = "title";break;
				default:""
			}
			window.sessionStorage.setItem("selectText",text);
		});

		$("div.btn-group:first button.btn-orderBtn").on("click",function(){

			console.log($(this).children().eq(0)[0].className);
			console.log($(this));
			if(wdn.dataSource&&wdn.dataSource.data().length > 0){
				if($(this).children().context.innerText == "日期"){
					sortField = "year";
				}else if($(this).children().context.innerText == "标题"){
					sortField = "title";
				}else if($(this).children().context.innerText == "相关度"){
					sortField = "score";
				}
				wdn.dataSource.read();
			}else{
				fsn.initNotificationMes("请先搜索数据！",false);
			}

		});

		typeSelectIndex = window.sessionStorage.getItem("typeSelectIndex");
		//typeSelectName = window.sessionStorage.getItem("typeSelectName");
		if(typeSelectIndex!=""&&null!=typeSelectIndex){
			$("#dwn_menu li").removeClass('wdn-k-state-hover');
			//$("#dwn_menu li").eq(typeSelectIndex).addClass("k-item k-state-default wdn-k-state-hover");
		}else{
			$("#dwn_menu li").eq(0).addClass("k-item k-state-default wdn-k-state-hover");
		}
		$("#dwn_menu li").eq(0).addClass("k-item k-state-default wdn-k-state-hover");
		selectText = window.sessionStorage.getItem("selectText");
		$(".dropdown-toggle")[0].innerHTML="任意字段"+"&nbsp<span class='caret'></span>";

	}

	wdn.initListView = function(){
		var str = window.sessionStorage.getItem("shoppingCat");

		if(str){
			shoppingCatData = JSON.parse(str);
			shoppingCatData.forEach(function(item){
				wdn.shoppingCatDS.add(item);
			})
			$('#badge_num').text(shoppingCatData.length);
		}

		$("#wdnListView").kendoListView ({
			dataSource: wdn.dataSource,
			template: kendo.template($("#row_template").html())
		});
		$("div.apply-btn a").attr("onClick", "flyMeToYou(this,$('#badge_num'),1);");

		wdn.page = $("#pager").kendoPager({
			dataSource: wdn.dataSource,
			refresh : true,
			pageSizes : 10,
			messages :{
				empty: lims.l("No items to display"),
				itemsPerPage: lims.l("items per page"),
				of: lims.l("of {0}"),
				last: lims.l("Go to the last page"),
				first: lims.l("Go to the first page"),
				next: lims.l("Go to the next page"),
				previous: lims.l("Go to the previous page"),
				refresh: lims.l("Refresh"),
				page: lims.l("Page"),
				display: lims.l("{0} - {1} of {2} items"),
			}
		}).data("kendoPager");

		$("#search").keyup(function(event){
			if(event.keyCode == 13){
				wdn.searchDocument();
			}
		})
	}

	wdn.openWeiXinWin = function(){
	    $("#wenxianListWindow").data("kendoWindow").open().center();
	}
    wdn.closeWenXianWin = function(){
        $("#wenxianListWindow").data("kendoWindow").close();
    }
    /*选择飞行效果*/
	function flyMeToYou ($obj,$target,num){
    	//获取元素的坐标位置
    	var left = $obj.offset().left-30;
    	var top = $obj.offset().top;
    	//找到目标坐标位置
    	var cleft = $target.offset().left;
    	var ctop = $target.offset().top;
    	$("body").append("<span class='super_span'>+"+num+"</span>");
    	$(".super_span").css({left:left,top:top}).show().stop().animate({left:cleft,top:ctop},500,function(){
    		$(this).remove();
    		lowPower($target,num);
    	});
    }

	wdn.flyMeToYou1 = function($obj, id){
		 var selectedItem = {};
		 wdn.dataSource.data().forEach(function(i){
			 if(i.id == id){
				 selectedItem = i;
			 }
		 });
		 var existFlag = false;
		 wdn.shoppingCatDS.data().forEach(function(d){
			 if(d.id == selectedItem.id){
				 existFlag = true;
				 return;
			 }
		 });
		 console.log(selectedItem);
		 if(existFlag){
			 $obj.className="disableHref";
			 $obj.onclick = null;
			 fsn.initNotificationMes("该文献已在申请书架中！",false);
			 return;
		 }

		 //获取元素的坐标位置
		 var left = $obj.offsetLeft+200;
		 var top = $obj.offsetTop+350;
		 var num = 1;
		 //找到目标坐标位置
		 var cleft = $('#badge_num').offset().left;
		 var ctop = $('#badge_num').offset().top;
		 $("body").append("<span class='super_span'>+"+num+"</span>");
		 $(".super_span").css({left:left,top:top}).show().stop().animate({left:cleft,top:ctop},500,function(){
			 $(this).remove();
			 lowPower($('#badge_num'),num);
		 });

		 $obj.className="disableHref";
		 $obj.onclick = null;
		 var title = (selectedItem.title.replace(/\<font color='CC0033'>/g,""))
		 title = title.replace(/\<\/font>/g,"");
		 selectedItem.title = title;

		 wdn.shoppingCatDS.add(selectedItem);
		 window.sessionStorage.setItem("shoppingCat",JSON.stringify(wdn.shoppingCatDS.data()));
	 }

	/*飞行完成执行方法*/
    function lowPower($target,num){
    	$target.text(parseInt($target.text())+num)
    }

	wdn.removeShop = function(id){
		var item = {};
		wdn.shoppingCatDS.data().forEach(function(d){
			if(d.id == id){
				item = d;
			}
		});
		wdn.shoppingCatDS.remove(item);
		console.log("remove:" + id);
		$('#badge_num').text(parseInt($('#badge_num').text())-1)
		window.sessionStorage.setItem("shoppingCat",JSON.stringify(wdn.shoppingCatDS.data()));
	}

	wdn.submitOrder = function(){
	    wdn.closeWenXianWin()
		if (fsn.wdn.popGrid.dataSource.data().length > 0) {
		    window.sessionStorage.setItem("shoppingCat",JSON.stringify(wdn.shoppingCatDS.data()));
            window.open( fsn.getContextPath() + "/views/wdn/shopCartList.html", "_self");
		}else{
		    fsn.initNotificationMes("请添加文献进行申请！",false);
		}

	}
    wdn.initWindow = function(){
        $("#wenxianListWindow").kendoWindow({
                                                width: "60%",
                                                title: "申请文献列表"
                                            });
        wdn.popGrid = $("#popGrid").kendoGrid({
                                            dataSource: wdn.shoppingCatDS,
                                            columns: [ {field: "title",title:"标题",width:"65%"},
                                                       {field: "type",title:"类型",width:"15%"},
                                                       {field: "year",title:"年份",width:"10%"},
                                                       {command:{text:"移除",click:function(e){
                                                                var tr = $(e.target).closest("tr");
                                                                var data = this.dataItem(tr);
                                                                wdn.removeShop(data.id);
                                                       }},title:"",width:"10%"}],//,click:fsn.wdn.removeShop(#=id#)
                                         }).data("kendoGrid");

    }
    wdn.init = function(){
    		wdn.initListView();
    		wdn.initMenu();
    		wdn.initWindow();
    }
	wdn.init();
});
