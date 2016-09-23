// JavaScript Document
function getStyle(objId) {
	var obj=document.getElementById(objId);
	if (obj) return obj.style;
	else return 0;
}
function setStyle(id, v) {
    var st = getStyle(id);
    if(st) { st.display = v; return true; } else return false;
}
function switchPage(c, n)
{
  	if (getStyle(c) && getStyle(n)) 
  	{
		setStyle(c, "none");
		setStyle(n, "block");
	}
}
$(function(){
	$("#Switch_A").removeClass("none");
	$("#nextstep1").bind("click",function(){
		switchPage("Switch_A","Switch_B");
	});
	$("#firstgoone_pt,#firstgoone_t,#secgoone_pt,#secgoone_t,#thirdgoone_pt,#thirdgoone_t,#fourgoone_pt,#fourgoone_t")
		.bind("click",function(){
			var now_current = $(this).attr("mysign");
			switchPage(now_current,"Switch_A");
		});
	$("#firstgotwo_pt,#firstgotwo_t,#secgotwo_pt,#secgotwo_t,#thirdgotwo_pt,#thirdgotwo_t,#fourgotwo_pt,#fourgotwo_t")
		.bind("click",function(){
			var now_current = $(this).attr("mysign");
			switchPage(now_current,"Switch_B");
		});
	$("#firstgoth_pt,#firstgoth_t,#secgoth_pt,#secgoth_t,#thirdgoth_pt,#thirdgoth_t,#fourgoth_pt,#fourgoth_t")
		.bind("click",function(){
			var now_current = $(this).attr("mysign");
			switchPage(now_current,"Switch_C");
		});
	$("#firstgofour_pt,#firstgofour_t,#secgofour_pt,#secgofour_t,#thirdgofour_pt,#thirdgofour_t,#fourgofour_pt,#fourgofour_t")
		.bind("click",function(){
			var now_current = $(this).attr("mysign");
			switchPage(now_current,"Switch_D");
		});
	
	$("#nextstep1").bind("click",function(){
		switchPage("Switch_A","Switch_B");
	});
	$("#next2").bind("click",function(){
		switchPage("Switch_B","Switch_C");
	});
	$("#next3").bind("click",function(){
		switchPage("Switch_C","Switch_D");
	});
	$("#pre1").bind("click",function(){
		switchPage("Switch_B","Switch_A");
	});
	$("#pre2").bind("click",function(){
		switchPage("Switch_C","Switch_B");
	});
	$("#pre3").bind("click",function(){
		switchPage("Switch_D","Switch_C");
	});
});