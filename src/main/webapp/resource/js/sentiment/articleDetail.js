$(function() {
	var fsn = window.fsn = window.fsn || {}; // 全局命名空间
	var detail = window.detail || {};
	var httpPrefix = fsn.getHttpPrefix();
	var media_type = {
			"A": "问答",
			"B": "博客",
			"C": "聊天室",
			"E": "搜索引擎",
			"F": "论坛",
			"G": "贴吧群主",
			"H": "普通网友",
			"I": "图片",
			"M": "微博",
			"N": "新闻",
			"O": "其他",
			"P": "电子报",
			"S": "社交网站",
			"U": "客户端",
			"V": "视频",
			"W": "维基",
			"X": "微信"
			
	};
	var website_level = {
			"A": "很重要",
			"B": "重要",
			"C": "一般",
			"D": "不重要",
	};
	detail.init = function() {
		var article_id = window.sessionStorage.getItem("article_id");
		if (article_id != null && article_id != "") {
			$.ajax({
				url : httpPrefix + "/sentimentArticle/getArticleById/"+ article_id,
				type : "GET",
				async: false,
				success : function(data) {
					console.log(data.result.hits);
					var article = JSON.parse(data.result.hits[0]);
					$("#article_title").append(article.article_Title);
					$("#public_date").append(article.extracted_Time);
					if (article.article_Source != null&& article.article_Source != "") {
						$("#article-source").append("来源: " + article.article_Source);
					}
					$("#article-content").append("<p>"+ article.article_Content.replace(/\r\n/g,'</p><p>'));
//					console.log(article.article_Abstract);
//					if(article.article_Abstract != null){
//						$("#article-abstract").append("摘要：" + "<p>"+ article.article_Content.replace(/\r\n/g,'</p><p>'));
//					}
					$("#article-source-url").val(article.article_URL);
					$("#original-publish-time").text(article.article_PubTime_Str);
					$("#create-time").text(article.extracted_Time);
					$("#article-search-words").text(article.article_Search_Keywords);
					$("#article-focus-words").text(article.article_Focus_Keywords);
					$("#author").text(article.article_Author);
					$("#article-views").text(article.article_ViewCount != null? article.article_ViewCount:0);
					$("#article-reply").text(article.article_ReplyCount != null? article.article_ReplyCount:0);
					$("#article-source").text(article.article_Source);
					var durl=/http:\/\/([^\/]+)\//i;
					$("#article-source-website").text(article.article_URL.match(durl)[1]);
					$("#media-type").text(media_type[article.media_Type_Code]);
					$("#website-important-level").text(website_level[article.website_Important_Level]);
				}

			});
		}
		
		$("#source-btn").bind('click', function(){
			var source_url = $("#article-source-url").val();
			window.open(source_url, "right");
		});
		$("#confirm_no_btn1").bind("click", function(){
			window.history.back(-1);
		});
	}
	detail.init();
});