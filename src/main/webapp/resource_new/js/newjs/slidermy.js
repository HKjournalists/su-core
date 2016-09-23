// JavaScript Document
// JavaScript Document
          
function stead(target)
{
	var oBox = document.getElementById(target);
	var aUl = oBox.getElementsByTagName("ul");
	var aImg = aUl[0].getElementsByTagName("li");
	try{
		var aDiv = aUl[1].getElementsByTagName("ol");
		var aNum = aUl[1].getElementsByTagName("li");
	}catch(e){
	}
	var timer = play = null;
	var i = index = 0;	
	var bOrder = true;
	try{
		for (i = 0; i < aNum.length; i++)
		{
			aNum[i].index = i;
			aNum[i].onmouseover = function ()
			{
				show(this.index);
			};
			aNum[i].click = function ()
			{
				show(this.index);
			};
		}
	}catch(e){}
	
	oBox.onmouseover = function ()
	{
		clearInterval(play);	
	};
	
	oBox.onmouseout = function ()
	{
		autoPlay();
	};	
/*	$(".prev").bind("click",function(){
		index = index - 1;		
		index >= aImg.length && (index = 0, bOrder = true);
		index <= 0 && (index = 0, bOrder = true);
		
		show(index);
	});
	$(".next").bind("click",function(){
		index = index + 1;
		index >= aImg.length && (index = 0, bOrder = true);
		index <= 0 && (index = 0, bOrder = true);
		show(index);
	});
*/	function autoPlay ()
	{
		play = setInterval(function () {
			bOrder ? index++ : index--;			
			
			index >= aImg.length && (index = 0, bOrder = true);
			
			index <= 0 && (index = 0, bOrder = true);
			
			show(index);
		},5000);	
	}
	autoPlay();
	
	function show (a)
	{
		index = a;
		var alpha = 0;
		try{
			for (i = 0; i < aNum.length; i++){
				aNum[i].className = "";
				aDiv[i].className = "";
			}
			aNum[index].className = "current";
			aDiv[index].className = "current";
		}catch(e){}
		clearInterval(timer);			
		
		for (i = 0; i < aImg.length; i++)
		{
			aImg[i].style.opacity = 0;
			aImg[i].style.filter = "alpha(opacity=0)";	
		}
		
		timer = setInterval(function () {
			alpha += 5;
			alpha > 100 && (alpha =100);
			aImg[index].style.opacity = alpha / 100;
			aImg[index].style.filter = "alpha(opacity = " + alpha + ")";
			alpha == 100 && clearInterval(timer);
		},100);
	}
}