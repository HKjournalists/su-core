package com.gettec.fsnip.fsn.util;

import org.apache.commons.lang.StringUtils;

public class ValidatorUtils {

	public static boolean checkEmptyString(String field, String fieldName, StringBuilder msgBuilder){
		boolean valid = true;
		
		if(StringUtils.isEmpty(field)){
			msgBuilder.append(String.format("%s cannot be empty.\n", fieldName));
			valid = false;
		}
		
		return valid;
	}
	
}
