$(document).ready(function(){
	 var fsn=window.fsn=window.fsn||{};
     var login=fsn.config=fsn.config||{};
     //提交过程
     login.SubmitToServer=(function(){
     	function submitToServer(){};
     	//check
     	submitToServer.prototype.check=function(u,p){
     		if(u!="请输入用户名..."&&u.length>0&&p!="请输入密码..."&&p.length>0){
                 return true;
     		}
            $("#msg span").text("请输入用户名或密码！");
            return false;
     	}
     	//submit
     	submitToServer.prototype.toLogin = function() {
     		var u=$("input[type=text]").val().trim();
     		var p=$("input[type=password]").val().trim();
     		if(this.check(u,p)){
                $.ajax({
                	url:"/service/user/login",
                	method:"POST",
                	dataType:"json",
                	data:{
                		user : u,
                		pwd : p,
                		url : ""
                	},
                	success:function(data){
                		if (data.status == "true") {
							window.location.pathname = data.url;
						} else {
							$("#msg span").html(fsn.l("Login Return Error"));
						}
                	} 
                });
     	    }
     	};
        return new submitToServer();
     })();
     login.init=function(){
     	  //input聚焦时间
     	   $("input").focus(function(){
     	   	   $("#label_"+$(this).attr("name")).text("");
               $("#msg span").text("");
     	   });
     	   //input失焦事件
     	   $("input").blur(function(){
     	       if($(this).val().trim().length<=0){
     	       	switch($(this).attr("name")){
                   case "user":$("#label_user").text("请输入用户名...");break;
                   case "pwd":$("#label_pwd").text("请输入密码...");break;
     	       	}
     	       }   	  
     	   });
     	   //重置
     	   $("button[type=reset]").click(function(){
     	   	   $("#label_user").text("请输入用户名...");
     	   	   $("#label_pwd").text("请输入密码...");
     	   	   $("input").each(function(){$(this).val("");});
     	   	   $("#msg span").text("");
     	   });
     	   //提交
     	   $("button[type=submit]").click(function(){
               login.SubmitToServer.toLogin();
     	   });
     }
     login.init();
});