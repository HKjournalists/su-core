(function($) {
	var filter = window.filter = window.filter || {};
	
	filter.configure =  function(filter) {
		var configure = "" ; 
		for(var i = 0;i<filter.filters.length;i++){
			if(filter.filters[i].filters){
				var temp_field = filter.filters[i].filters[0].field.replace(".","_");
				configure = configure+temp_field+"@"+ filter.filters[i].filters[0].operator +"@"+ filter.filters[i].filters[0].value
					+"@"+ temp_field +"@"+ filter.filters[i].filters[1].operator +"@"+ filter.filters[i].filters[1].value +"@" +filter.filters[i].logic ; 
			}else{
//				var temp_field = filter.filters[i].field.replace(".","_");
				var temp_field = filter.filters[i].field.replace(/\./g,"_");
				configure = configure+temp_field +"@"+ filter.filters[i].operator +"@"+ filter.filters[i].value +"@"+filter.logic; 
			}
				configure = configure+"@@";
		}
		return configure.replace(/\\+/g,"\\\\\\\\").replace(/\/+/g, "\\0gan0\\");
	}
	
})(jQuery);