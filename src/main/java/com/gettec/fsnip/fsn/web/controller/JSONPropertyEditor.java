package com.gettec.fsnip.fsn.web.controller;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONPropertyEditor extends PropertyEditorSupport {

	private final static Logger logger = Logger
			.getLogger(JSONPropertyEditor.class);

	public JSONPropertyEditor() {
		super();
	}

	public JSONPropertyEditor(Object source) {
		super(source);
	}

	@Override
	public String getAsText() {
		Object object = getValue();
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error("Error occurs on parsing Object to JSON.." + object, e);
		}
		return json;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setAsText(String text) {
		ObjectMapper mapper = new ObjectMapper();
		// Ignore unknown properties
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Object value = null;
		try {
			value = mapper.readValue(text, (Class) getSource());
		} catch (Exception e) {
			logger.error("Error occurs on converting JSON to Object.." + text,
					e);
		}
		setValue(value);
	}
}
