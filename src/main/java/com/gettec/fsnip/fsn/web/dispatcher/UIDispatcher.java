package com.gettec.fsnip.fsn.web.dispatcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("")
public class UIDispatcher {
	
	@RequestMapping(method = RequestMethod.GET, value = "/{path}")
	public String forward(@PathVariable String path) {
		
		return path;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{path1}/{path2}")
	public String forward(@PathVariable String path1, @PathVariable String path2) {
		
		return this.concatenateWithSlash(path1, path2);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{path1}/{path2}/{path3}")
	public String forward(@PathVariable String path1, @PathVariable String path2, @PathVariable String path3) {
		
		return this.concatenateWithSlash(path1, path2, path3);
	}
	
	private String concatenateWithSlash(String ...strs){
		String str = StringUtils.EMPTY;
		
		if(strs != null){
			str = StringUtils.join(strs, "/");
		}
		
		return str;
	}
}
