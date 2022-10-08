package com.boat.bp.middleware.data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;
public class Approval {

    private String approvedOnDate;
private String dateFormat;
private String locale;

public Approval()
{
    Date date = Calendar.getInstance().getTime();  
    DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT); 
    this.approvedOnDate = dateFormat.format(date);
    this.setLocale(Settings.LOCALE);
    this.dateFormat = Settings.DATE_FORMAT;
}

public String getApprovedOnDate() {
    return approvedOnDate;
}

public void setApprovedOnDate(String approvedOnDate) {
    this.approvedOnDate = approvedOnDate;
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


    
}
