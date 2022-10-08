package com.boat.bp.middleware.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error   {
	
	private String message;

	public String getMessage() {
		return message;
	}
	@JsonSetter("developerMessage")
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	

}
