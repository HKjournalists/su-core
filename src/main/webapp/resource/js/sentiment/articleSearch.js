$(function(){
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var search = window.search || {};
	var httpPrefix = fsn.getHttpPrefix();
	var topices = [];
	

		search.searchDs = new kendo.data.DataSource(
			{
				type : "JSON",
				transport : {
					read : {
						url : function(options) {
							var number = $(".topicActive").attr("data");
							var input_keyword = $("#query-input").val();
							var focus_key = "";
							if($(".keyActive").attr("data") != "-1"){
								focus_key = $(".keyActive").text();
							}
							var type = -1;
							if (number != -1) {
								type = topices[number].Subject_ID
							}
							var input_keyword = $("#query-input").val();

							var condition = "?keywords=";
							condition += encodeURI(input_keyword.trim());
							condition += "&focuskey=" + encodeURI(focus_key.trim());
							return httpPrefix + "/sentimentArticle/" + type + "/" + options.page + "/" + options.pageSize + condition;
						},
					}
				},
				requestStart : function(e) {
					var keywords = $("#query-input").val().trim();
					if (keywords == "") {
						$("#dynamic_msg").html("Ready to search...");
					} else {
						$("#dynamic_msg").html("正在通过关键字<strong>" + keywords + "</strong>检索...");
					}
					// 大约找到100,000条记录(用时 0.11 s)
				},
				schema : {
					data : function(response) {
						if (response.result && response.result.hits.length > 0) {
							$("#dynamic_msg").html("大约找到 " + response.result.count+ " 条记录, 用时"+ response.result.took);
							var list = [];
							console.log(response.result.hits);
							response.result.hits.forEach(function(item) {
								list.push(JSON.parse(item));
							});
							return list;
						}
						return [];
					},
					total : function(response) {
						if (response.result) {
							return response.result.count;
						}
						return 0;
					}
				},
				serverPaging : true,
				pageSize : 10
			});
			search.getSearchResult = function(){
					search.searchDs.read();
			}
			search.initialize = function(){
				search.getTopics();
				search.initCompent();
				$("#pager").kendoPager({
	                dataSource: search.searchDs,
	                messages: {
	                	display: "{0} - {1} 共 {2} 条"
	                }
	            });
				$("#listView").kendoListView({
	                dataSource: search.searchDs,
	                template: kendo.template($("#template").html()),
	                selectable: true,
	                change: function(e){
	                	console.log($(this).attr("class"));
	                	 var index = this.select().index();
	                	 var dataItem = this.dataSource.view()[index];
	                	 console.log(dataItem.article_URL);
	                	 if(dataItem.article_Content_Length == null){
	                		 window.open(dataItem.article_URL);
	                	 }else{
	                		 var key = "article_id";
	                		 window.sessionStorage.setItem(key, dataItem.article_Detail_ID);
	                		 window.open( fsn.getContextPath() + "/views/sentiment/detail.html", "_self");
	                	 }
	                }
	            });
				$("#article-search-btn").bind("click",search.getSearchResult);
				$("#query-input").keypress(function(e){
			        var eCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode; //IE,Chrome,Firefox
			        if (eCode == 13){
			        	search.getSearchResult();
			        }
			});
			};
			
			search.initCompent = function(){
				$(".topicSpan").bind('click', function(){
					search.displayKeywords($(this).attr('data'));
					$(".topicSpan").removeClass("topicActive");
					$(this).addClass("topicActive");
					var type=$(".topicActive").attr("data");
					search.getSearchResult();
				});
				$(document).on('click', ".key-span", function(){
					$(".key-span").removeClass("keyActive");
					$(this).addClass("keyActive");
					search.getSearchResult();
				});
			};
			
			search.getTopics = function(){
				$.ajax({
					url: httpPrefix + "/setimentTopic/getTopicsByOrg",
					type: "GET",
					contentType : "application/json; charset=utf-8",
					async: false,
					success: function(data){
						if(data.status == true){
							topices = data.result;
							$("#topic-contents").append('<a class="topicSpan topicActive" data="-1" href="#" data="-1">所有 </a>');
							console.log(data.result);
							for(var i=0; i< topices.length; i++){
								$("#topic-contents").append('<a class="topicSpan" data="' + i + '" href="#">' +  topices[i].Subject_Name + ' </a>');
							}
						}
					}
				});
			};
			
			search.displayKeywords = function(i){
				$("#keywords-contents").empty();
				if(i != -1){
					var currentTopic = topices[i];
					
					$("#keywords-contents").append('<a class="key-span keyActive" data="-1">所有 </a>');
					if(currentTopic.focuskeyword_A){
						var currentKeywords = currentTopic.focuskeyword_A.split(",");
						for(var i=0; i<currentKeywords.length; i++){
							$("#keywords-contents").append('<a class="key-span" href="#">' + currentKeywords[i] + ' </a>');
						}
					}
					
				}
				
			};
			search.initialize();
		});