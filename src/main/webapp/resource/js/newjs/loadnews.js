// JavaScript Document
var newsid = "";
$(function(){
	if(window.location.href.split("?")[1]){
		newsid = window.location.href.split("?")[1];
	}
	loadNews();
});


function loadNews(){
	$.ajax({
		    url:"/fsn-core/service/home/getDefiniteNew/"+newsid,
			dataType:"JSON",
		 	async:false,
			success:function(data) {
				
				if (data.result.status == "true") {
					
					$("#news_title").html(data.messageNews.title);
					$("#main_conent").html(data.messageNews.content);
				}
			}
		});
}