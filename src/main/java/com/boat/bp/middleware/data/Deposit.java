package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;


public class Deposit {

   
	private String transactionAmount;
	private String paymentTypeId;
	private String accountNumber;
	private String locale;
	private String transactionDate;
	private String dateFormat;


	public Deposit(String transactionAmount) {

		this.locale = Settings.LOCALE;                                                                                  
		this.dateFormat = Settings.DATE_FORMAT;
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);
		this.transactionDate = dateFormat.format(date);
        this.accountNumber =Settings.PARTNER_BANK_ACC_NO;
        this.paymentTypeId = "2";
        this.transactionAmount = transactionAmount;

	}


    public String getTransactionAmount() {
        return transactionAmount;
    }


    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }


    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }


    public String getAccountNumber() {
        return accountNumber;
    }


    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public String getLocale() {
        return locale;
    }


    public void setLocale(String locale) {
        this.locale = locale;
    }


    public String getTransactionDate() {
        return transactionDate;
    }


    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }


    public String getDateFormat() {
        return dateFormat;
    }


    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    
}
