package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;

public class Activation {

 private String activatedOnDate;
private String dateFormat;
private String locale;

public Activation()
{
    Date date = Calendar.getInstance().getTime();  
    DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT); 
    this.activatedOnDate = dateFormat.format(date);
    this.setLocale(Settings.LOCALE);
    this.dateFormat = Settings.DATE_FORMAT;
}


public String getActivatedOnDate() {
    return activatedOnDate;
}


public void setActivatedOnDate(String activatedOnDate) {
    this.activatedOnDate = activatedOnDate;
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
