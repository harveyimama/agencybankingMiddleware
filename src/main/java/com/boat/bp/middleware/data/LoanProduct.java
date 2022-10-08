package com.boat.bp.middleware.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanProduct {

    private int numberOfRepayments;
    private int repaymentEvery;
    private LoanProductDetail repaymentFrequencyType;
    private int interestRatePerPeriod;
    private LoanProductDetail   amortizationType;
    private LoanProductDetail  interestType;
    private LoanProductDetail interestCalculationPeriodType;
    private int transactionProcessingStrategyId;
    
    public int getNumberOfRepayments() {
        return numberOfRepayments;
    }
    public void setNumberOfRepayments(int numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }
    public int getRepaymentEvery() {
        return repaymentEvery;
    }
    public void setRepaymentEvery(int repaymentEvery) {
        this.repaymentEvery = repaymentEvery;
    }
    public LoanProductDetail getRepaymentFrequencyType() {
        return repaymentFrequencyType;
    }
    public void setRepaymentFrequencyType(LoanProductDetail repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }
    public int getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }
    public void setInterestRatePerPeriod(int interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
    }
    public LoanProductDetail getAmortizationType() {
        return amortizationType;
    }
    public void setAmortizationType(LoanProductDetail amortizationType) {
        this.amortizationType = amortizationType;
    }
    public LoanProductDetail getInterestType() {
        return interestType;
    }
    public void setInterestType(LoanProductDetail interestType) {
        this.interestType = interestType;
    }
    public LoanProductDetail getInterestCalculationPeriodType() {
        return interestCalculationPeriodType;
    }
    public void setInterestCalculationPeriodType(LoanProductDetail interestCalculationPeriodType) {
        this.interestCalculationPeriodType = interestCalculationPeriodType;
    }
    public int getTransactionProcessingStrategyId() {
        return transactionProcessingStrategyId;
    }
    public void setTransactionProcessingStrategyId(int transactionProcessingStrategyId) {
        this.transactionProcessingStrategyId = transactionProcessingStrategyId;
    }
    




    
}
