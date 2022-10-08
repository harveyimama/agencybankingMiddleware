package com.boat.bp.middleware.data;

import com.boat.bp.middleware.helper.Settings;

public class Beneficiary {  

    private String locale;
    private String name;
    private String officeName;
    private String accountNumber;
    private int accountType;
    private int transferLimit;

    public Beneficiary(Account account,String fromClientId) {

        this.locale = Settings.LOCALE;
        this.name = fromClientId.concat("-to-").concat(account.getClientId());
        this.officeName = Settings.officeName;
        this.accountNumber = account.getAccountNo();
        this.accountType = Integer.parseInt(Settings.ACCOUNT_TYPE) ;
        this.transferLimit = Settings.transferLimit;

    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(int transferLimit) {
        this.transferLimit = transferLimit;
    }

    

}
