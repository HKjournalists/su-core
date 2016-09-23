// JavaScript Document
$(function(){
	//alert("123");
	initScroll();	
	initRecnews();
	initTheme();
	stead("index_post");
});

function initScroll(){
	$.ajax({
		 type : "GET",
		 url : "/fsn-core/service/home/initializeRoll/4",
		 dataType:"JSON",
		 async:false,
		 success:function(returnValue){
			// alert(returnValue.status);
			 if(returnValue.result.status == "true"){
				   var temp_imghtm="";
				   var temp_newsol="";
				   var pleft = '<p class="in-block fl w200 h22" id="show-left"></p>';
				   var temp_newCountli = "";
				   
				   for (var i=0; i < returnValue.rollNews.length; i++){
					   if(i === 0){
						   temp_imghtm += "<li class='current'><img src='"+returnValue.rollNews[i].imageUrls+"' /></li>";
						   temp_newsol += "<ol class='current'><div class='f34 fyahei colortit fontbolder'>"+returnValue.rollNews[i].title+"</div><div class='colorshow1 f20 tl mt30 fontbold'>"+returnValue.rollNews[i].outline+"</div><div class='colorshow_congreen fl6 tl lh25 mt23'>"+returnValue.rollNews[i].content+"<a class='in-block' target='_blank' href='/fsn-core/views/news/news.html?"+returnValue.rollNews[i].id+"'>[更多]</a></div></ol>";
						   temp_newCountli += "<li class='current'></li>";
					   }else{
						   temp_imghtm += "<li><img src='"+returnValue.rollNews[i].imageUrls+"' /></li>";
						   temp_newsol += "<ol><div class='f34 fyahei colortit fontbolder'>"+returnValue.rollNews[i].title+"</div><div class='colorshow1 f20 tl mt30 fontbold'>"+returnValue.rollNews[i].outline+"</div><div class='colorshow_congreen fl6 tl lh25 mt23'>"+returnValue.rollNews[i].content+"<a class='in-block' target='_blank' href='/fsn-core/views/news/news.html?"+returnValue.rollNews[i].id+"'>[更多]</a></div></ol>";
						   temp_newCountli += "<li></li>";
					   }
				  }
				  //$("#index_post .count").width(returnValue.recommendNewsList.length*35)
				  $("#index_post .list").html(temp_imghtm);
				  $("#index_post .count").html(temp_newsol + pleft + temp_newCountli);
		 	}
		 }
	});
}
function initRecnews(){
	$.ajax({
		 type : "GET",
		 url : "/fsn-core/service/home/initializeFsc/2",
		 dataType:"JSON",
		 async:false,
		 success:function(returnValue){
			if(returnValue.result.status == "true"){
				//alert("test");
				for(var i = 0; i < returnValue.onlyFsc.length; i++){
					//alert(returnValue.onlyFsc[i].title);
					$("#newsNo_"+i).html(returnValue.onlyFsc[i].title);
					$("#newsNo_"+i).attr("href","/fsn-core/views/news/news.html?"+returnValue.onlyFsc[i].id)
				}
			}
		 	
		 }
	});
}

function initTheme(){
	$.ajax({
		 type : "GET",
		 url : "/fsn-core/service/home/initializeColumn/4",
		 dataType:"JSON",
		 async:false,
		 success:function(returnValue){
			if(returnValue.result.status == "true"){
				if(returnValue.messageNews != null){
					$("#messageNewsImg").attr("src",returnValue.messageNews.imageUrl);
					$("#msgNewRec").attr("href",'/fsn-core/views/news/news.html?'+returnValue.messageNews.id)
					var msgnews_htm="";
					for(var i = 0; i < returnValue.messageNews.newsLists.length; i++){
						msgnews_htm += '<li><a href="/fsn-core/views/news/news.html?'+returnValue.messageNews.newsLists[i].id+'">'+returnValue.messageNews.newsLists[i].title+'</a><div>'+returnValue.messageNews.newsLists[i].date+'</div></li>';
					}
					$("#msgNewList").html(msgnews_htm);
				
				
					$("#subjectsImg").attr("src",returnValue.subjects.imageUrl);
					$("#subjectNewRec").attr("href",'/fsn-core/views/news/news.html?'+returnValue.subjects.id)
					var subnews_htm="";
					//alert(returnValue.subjects.newsLists[0].title)
					for(var i = 0; i < returnValue.subjects.newsLists.length; i++){
						subnews_htm += '<li><a href="/fsn-core/views/news/news.html?'+returnValue.subjects.newsLists[i].id+'">'+returnValue.subjects.newsLists[i].title+'</a><div>'+returnValue.subjects.newsLists[i].date+'</div></li>';
					}
					$("#subjectList").html(subnews_htm);
				
			
					$("#servicesImg").attr("src",returnValue.services.imageUrl);
					$("#serviceNewRec").attr("href",'/fsn-core/views/news/news.html?'+returnValue.services.id)
					var subvicenews_htm="";
					for(var i = 0; i < returnValue.services.newsLists.length; i++){
						subvicenews_htm += '<li><a href="/fsn-core/views/news/news.html?'+returnValue.services.newsLists[i].id+'">'+returnValue.services.newsLists[i].title+'</a><div>'+returnValue.services.newsLists[i].date+'</div></li>';
					}
					$("#serviceList").html(subvicenews_htm);
				}
				
			}
		 	
		 }
	});
}