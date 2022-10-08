package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;

public class PreProcess {

    private String transactionDate;
    private String transactionAmount;
    private String locale;
    private String dateFormat;

    public PreProcess(String amount )
    {
        this.locale = Settings.LOCALE;
		this.dateFormat = Settings.DATE_FORMAT;
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);
		this.transactionDate = dateFormat.format(date);
        this.transactionAmount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    
}
