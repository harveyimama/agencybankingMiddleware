package com.boat.bp.middleware.data;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanDetails {

    private String accountNo;
    private LoanRepaymentSchedule repaymentSchedule;
    private LoanStatus status;
    private String clientName;
    private String loanProductName;
    
    public String getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    public String getLoanProductName() {
        return loanProductName;
    }
    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }
    public LoanRepaymentSchedule getRepaymentSchedule() {
        return repaymentSchedule;
    }
    public void setRepaymentSchedule(LoanRepaymentSchedule repaymentSchedule) {
        this.repaymentSchedule = repaymentSchedule;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
   


    
}
