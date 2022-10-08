package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.RepayLoanRequest;

public class Repayment {

private String transactionDate;
private String dateFormat;
private String locale;
private double  transactionAmount;
private  int paymentTypeId;

public Repayment(RepayLoanRequest request)
{
    Date date = Calendar.getInstance().getTime();  
    DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT); 
    this.transactionDate = dateFormat.format(date);
    this.setLocale(Settings.LOCALE);
    this.dateFormat = Settings.DATE_FORMAT;
    this.transactionAmount = request.getTransactionAmount(); 
    this.paymentTypeId = Settings.REPAYMENT_PAYMENT_TYPE;
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

public String getLocale() {
    return locale;
}

public void setLocale(String locale) {
    this.locale = locale;
}


public double getTransactionAmount() {
    return transactionAmount;
}


public void setTransactionAmount(double transactionAmount) {
    this.transactionAmount = transactionAmount;
}


public int getPaymentTypeId() {
    return paymentTypeId;
}


public void setPaymentTypeId(int paymentTypeId) {
    this.paymentTypeId = paymentTypeId;
}


    
}
