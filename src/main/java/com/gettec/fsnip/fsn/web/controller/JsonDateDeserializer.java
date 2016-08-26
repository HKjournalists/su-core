package com.gettec.fsnip.fsn.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		Date result;
		try {
			result = dateFormat.parse(parser.getText());
		} catch (ParseException e) {
			try {
				result = dateFormat2.parse(parser.getText());
			} catch (ParseException ee) {
				result = null;
			}
		}
		return result;
	}


}