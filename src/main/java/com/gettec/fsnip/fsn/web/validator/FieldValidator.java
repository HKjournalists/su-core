package com.gettec.fsnip.fsn.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FieldValidator implements Validator {

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void validate(Object obj, Errors error) {
		ValidationUtils.rejectIfEmptyOrWhitespace(error, "","required","");
	}
}
