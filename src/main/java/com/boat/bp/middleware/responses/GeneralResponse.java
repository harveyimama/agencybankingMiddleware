package com.boat.bp.middleware.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse<T> {

	private String status;
	private String message;
	private T data;
	private List<Error> errors;
	private int resourceId;
	private int officeId;
	private int clientId;
	private int savingsId;
	private String error;
	private String preTransactionref;

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

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


	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;

	}

	public int getOfficeId() {
		return officeId;
	}

	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getSavingsId() {
		return savingsId;
	}

	public void setSavingsId(int savingsId) {
		this.savingsId = savingsId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPreTransactionref() {
		return preTransactionref;
	}

	public void setPreTransactionref(String preTransactionref) {
		this.preTransactionref = preTransactionref;
	}

	@Override
	public String toString() {
		return "GeneralResponse [clientId=" + clientId + ", data=" + data + ", error=" + error + ", errors=" + errors
				+ ", message=" + message + ", officeId=" + officeId + ", preTransactionref=" + preTransactionref
				+ ", resourceId=" + resourceId + ", savingsId=" + savingsId + ", status=" + status + "]";
	}

	
	
	

}
