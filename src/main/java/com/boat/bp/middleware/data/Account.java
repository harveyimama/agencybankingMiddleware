package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.OnboardingRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Account {

	private String id;
	private String clientId;
	private String productId;
	private String dateFormat;
	private String locale;
	private String submittedOnDate;
	private String accountNo;
	private String savingsProductName;
	private Summary summary;
	
	public Account()
	{}
	
	public Account(OnboardingRequest request,String product) {
		
		this.clientId = request.getClientId();
		this.setLocale(Settings.LOCALE);
		this.dateFormat = Settings.DATE_FORMAT;
		this.productId = product;
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);  
		this.submittedOnDate =  dateFormat.format(date); 
		
	}

	public Account(String clientId,String product) {
		
		this.clientId = clientId;
		this.setLocale(Settings.LOCALE);
		this.dateFormat = Settings.DATE_FORMAT;
		this.productId = product;
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);  
		this.submittedOnDate =  dateFormat.format(date); 
		
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSubmittedOnDate() {
		return submittedOnDate;
	}

	public void setSubmittedOnDate(String submittedOnDate) {
		this.submittedOnDate = submittedOnDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getSavingsProductName() {
		return savingsProductName;
	}

	public void setSavingsProductName(String savingsProductName) {
		this.savingsProductName = savingsProductName;
	}
	
	

}
