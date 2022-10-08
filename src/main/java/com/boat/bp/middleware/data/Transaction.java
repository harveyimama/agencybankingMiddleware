package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;

public class Transaction {

	private String fromClientId;
	private String fromAccountType;
	private String fromAccountId;
	private String toClientId;
	private String toAccountType;
	private String toAccountId;
	private String transferAmount;
	private String transferDescription;
	private String resourceId;
	private String locale;
	private String fromOfficeId;
	private String toOfficeId;
	private String transferDate;
	private String dateFormat;
	private String password;
	private String preTransactionref;

	public Transaction() {
		this.locale = Settings.LOCALE;
		this.fromOfficeId = Settings.officeId;
		this.toOfficeId = Settings.officeId;
		this.dateFormat = Settings.DATE_FORMAT;
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);
		this.transferDate = dateFormat.format(date);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromClientId() {
		return fromClientId;
	}

	public void setFromClientId(String fromClientId) {
		this.fromClientId = fromClientId;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(String fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getToClientId() {
		return toClientId;
	}

	public void setToClientId(String toClientId) {
		this.toClientId = toClientId;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getTransferDescription() {
		return transferDescription;
	}

	public void setTransferDescription(String transferDescription) {
		this.transferDescription = transferDescription;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFromOfficeId() {
		return fromOfficeId;
	}

	public void setFromOfficeId(String fromOfficeId) {
		this.fromOfficeId = fromOfficeId;
	}

	public String getToOfficeId() {
		return toOfficeId;
	}

	public void setToOfficeId(String toOfficeId) {
		this.toOfficeId = toOfficeId;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getPreTransactionref() {
		return preTransactionref;
	}

	public void setPreTransactionref(String preTransactionref) {
		this.preTransactionref = preTransactionref;
	}

	
	
}
