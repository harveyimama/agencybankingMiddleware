package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.LoanRequest;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Loan {

    private String id;
    private String locale;
    private String dateFormat;
    private String submittedOnDate;
    private String clientId;
    private String productId;
    private String principal;
    private String loanType;
    private String linkAccountId;
    private String expectedDisbursementDate;
    private int loanTermFrequency;
    private int loanTermFrequencyType;
    private int numberOfRepayments;
    private int repaymentEvery;
    private int repaymentFrequencyType;
    private int interestRatePerPeriod;
    private int amortizationType;
    private int interestType;
    private int interestCalculationPeriodType;
    private int transactionProcessingStrategyId;
    


    

    public Loan(LoanRequest r) {
		this.locale = Settings.LOCALE;
		this.dateFormat = Settings.DATE_FORMAT;
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);
		this.submittedOnDate = dateFormat.format(date);
        this.clientId = r.getClientId();
        this.productId =  r.getProductId();
        this.principal = r.getLoanAmount(); 
        this.loanType =  r.getLoanType();
        this.linkAccountId = r.getLinkAccountId();
        this.expectedDisbursementDate =  r.getExpectedDisbursementDate();
        this.loanTermFrequency = r.getLoanTermFrequency();
        this.loanTermFrequencyType = r.getLoanTermFrequencyType();
        
	}

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLinkAccountId() {
        return linkAccountId;
    }

    public void setLinkAccountId(String linkAccountId) {
        this.linkAccountId = linkAccountId;
    }

    public String getExpectedDisbursementDate() {
        return expectedDisbursementDate;
    }

    public void setExpectedDisbursementDate(String expectedDisbursementDate) {
        this.expectedDisbursementDate = expectedDisbursementDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
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

    public String getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(String submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public int getLoanTermFrequency() {
        return loanTermFrequency;
    }

    public void setLoanTermFrequency(int loanTermFrequency) {
        this.loanTermFrequency = loanTermFrequency;
    }

    public int getLoanTermFrequencyType() {
        return loanTermFrequencyType;
    }

    public void setLoanTermFrequencyType(int loanTermFrequencyType) {
        this.loanTermFrequencyType = loanTermFrequencyType;
    }

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

    public int getRepaymentFrequencyType() {
        return repaymentFrequencyType;
    }

    public void setRepaymentFrequencyType(int repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }

    public int getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }

    public void setInterestRatePerPeriod(int interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
    }

    public int getAmortizationType() {
        return amortizationType;
    }

    public void setAmortizationType(int amortizationType) {
        this.amortizationType = amortizationType;
    }

    public int getInterestType() {
        return interestType;
    }

    public void setInterestType(int interestType) {
        this.interestType = interestType;
    }

    public int getInterestCalculationPeriodType() {
        return interestCalculationPeriodType;
    }

    public void setInterestCalculationPeriodType(int interestCalculationPeriodType) {
        this.interestCalculationPeriodType = interestCalculationPeriodType;
    }

    public int getTransactionProcessingStrategyId() {
        return transactionProcessingStrategyId;
    }

    public void setTransactionProcessingStrategyId(int transactionProcessingStrategyId) {
        this.transactionProcessingStrategyId = transactionProcessingStrategyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void applyLoanDetails(LoanProduct loanDetails) {

        this.numberOfRepayments = loanDetails.getNumberOfRepayments();
        this.repaymentEvery = loanDetails.getRepaymentEvery();
        this.repaymentFrequencyType = loanDetails.getRepaymentFrequencyType().getId();
        this.interestRatePerPeriod = loanDetails.getInterestRatePerPeriod();
        this.amortizationType = loanDetails.getAmortizationType().getId();
        this.interestType = loanDetails.getInterestType().getId();
        this.interestCalculationPeriodType= loanDetails.getInterestCalculationPeriodType().getId();
        this.transactionProcessingStrategyId =loanDetails.getTransactionProcessingStrategyId();

        if(this.loanTermFrequency  == 0 )
        this.loanTermFrequency = this.numberOfRepayments*this.repaymentEvery ;
        
    }

    @Override
    public String toString() {
        return "Loan [amortizationType=" + amortizationType + ", clientId=" + clientId + ", dateFormat=" + dateFormat
                + ", expectedDisbursementDate=" + expectedDisbursementDate + ", id=" + id
                + ", interestCalculationPeriodType=" + interestCalculationPeriodType + ", interestRatePerPeriod="
                + interestRatePerPeriod + ", interestType=" + interestType + ", linkAccountId=" + linkAccountId
                + ", loanTermFrequency=" + loanTermFrequency + ", loanTermFrequencyType=" + loanTermFrequencyType
                + ", loanType=" + loanType + ", locale=" + locale + ", numberOfRepayments=" + numberOfRepayments
                + ", principal=" + principal + ", productId=" + productId + ", repaymentEvery=" + repaymentEvery
                + ", repaymentFrequencyType=" + repaymentFrequencyType + ", submittedOnDate=" + submittedOnDate
                + ", transactionProcessingStrategyId=" + transactionProcessingStrategyId + "]";
    }

   
    
   
    
}
