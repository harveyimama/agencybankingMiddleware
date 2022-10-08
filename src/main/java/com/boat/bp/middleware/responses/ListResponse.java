package com.boat.bp.middleware.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResponse<T> {

	private String status;
	private String message;
	private T[] data;
	private List<Error> errors;
	
	

	

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public String getStatus() {
		return status;
	}

	@JsonSetter("httpStatusCode")
	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	@JsonSetter("developerMessage")
	public void setMessage(String message) {
		this.message = message;
	}


	public T[] getData() {
		return data;
	}

	public void setData(T[] data) {
		this.data = data;

	}

	
	
	
	

}
