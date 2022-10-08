package com.boat.bp.middleware.data;
import com.boat.bp.middleware.helper.Settings;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanRepaymentSchedule {


    private double loanTermInDays;
    private double totalPrincipalDisbursed;
    private double totalPrincipalExpected;
    private double totalPrincipalPaid;
    private double totalInterestCharged;
    private double totalFeeChargesCharged;
    private double totalPenaltyChargesCharged;
    private double totalWaived;
    private double totalWrittenOff;
    private double totalRepayment;
    private double totalOutstanding;
    
    public double getLoanTermInDays() {
        return loanTermInDays;
    }
    public void setLoanTermInDays(double loanTermInDays) {
        this.loanTermInDays = loanTermInDays==0.0?Settings.ZERO:loanTermInDays;;
    }
    public double getTotalPrincipalDisbursed() {
        return totalPrincipalDisbursed;
    }
    public void setTotalPrincipalDisbursed(double totalPrincipalDisbursed) {
        this.totalPrincipalDisbursed = totalPrincipalDisbursed==0.0?Settings.ZERO:totalPrincipalDisbursed;;
    }
    public double getTotalPrincipalExpected() {
        return totalPrincipalExpected;
    }
    public void setTotalPrincipalExpected(double totalPrincipalExpected) {
        this.totalPrincipalExpected = totalPrincipalExpected==0.0?Settings.ZERO:totalPrincipalExpected;;
    }
    public double getTotalPrincipalPaid() {
        return totalPrincipalPaid;
    }
    public void setTotalPrincipalPaid(double totalPrincipalPaid) {
        this.totalPrincipalPaid = totalPrincipalPaid==0.0?Settings.ZERO:totalPrincipalPaid;;
    }
    public double getTotalInterestCharged() {
        return totalInterestCharged;
    }
    public void setTotalInterestCharged(double totalInterestCharged) {
        this.totalInterestCharged = totalInterestCharged==0.0?Settings.ZERO:totalInterestCharged;;
    }
    public double getTotalFeeChargesCharged() {
        return totalFeeChargesCharged;
    }
    public void setTotalFeeChargesCharged(double totalFeeChargesCharged) {
        this.totalFeeChargesCharged = totalFeeChargesCharged==0.0?Settings.ZERO:totalFeeChargesCharged;;
    }
    public double getTotalPenaltyChargesCharged() {
        return totalPenaltyChargesCharged;
    }
    public void setTotalPenaltyChargesCharged(double totalPenaltyChargesCharged) {
        this.totalPenaltyChargesCharged = totalPenaltyChargesCharged==0.0?Settings.ZERO:totalPenaltyChargesCharged;;
    }
    public double getTotalWaived() {
        return totalWaived;
    }
    public void setTotalWaived(double totalWaived) {
        this.totalWaived = totalWaived==0.0?Settings.ZERO:totalWaived;;
    }
    public double getTotalWrittenOff() {
        return totalWrittenOff;
    }
    public void setTotalWrittenOff(double totalWrittenOff) {
        this.totalWrittenOff = totalWrittenOff==0.0?Settings.ZERO:totalWrittenOff;;
    }
    public double getTotalRepayment() {
        return totalRepayment;
    }
    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment==0.0?Settings.ZERO:totalRepayment;;
    }
    public double getTotalOutstanding() {
        return totalOutstanding;
    }
    public void setTotalOutstanding(double totalOutstanding) {
        this.totalOutstanding = totalOutstanding==0.0?Settings.ZERO:totalOutstanding;;
    }
    

    
}
