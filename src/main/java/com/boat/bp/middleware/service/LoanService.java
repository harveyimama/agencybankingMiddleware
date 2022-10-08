package com.boat.bp.middleware.service;

import java.util.HashMap;
import java.util.Map;

import com.boat.bp.middleware.data.Approval;
import com.boat.bp.middleware.data.Disbursement;
import com.boat.bp.middleware.data.Loan;
import com.boat.bp.middleware.data.LoanDetails;
import com.boat.bp.middleware.data.LoanProduct;
import com.boat.bp.middleware.data.Repayment;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.GeneralResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

	private static final String CREATE_LOAN = "loans";
	private static final String LOAN = "loanproducts";

	@Autowired
	TokenManager manager;

	@Autowired
	Endpoint<Loan, GeneralResponse<Loan>> endpointAcc;

	@Autowired
	Endpoint<Repayment, GeneralResponse<Repayment>> endpointPay;

	@Autowired
	Endpoint<String, GeneralResponse<LoanProduct>> endpoint;

	@Autowired
	Endpoint<Approval, String> endpointApp;

	@Autowired
	Endpoint<Disbursement, String> endpointDis;

	@Autowired
	Endpoint<String, LoanDetails> endpointLoan;

	public GeneralResponse<Loan> loanRequest(final Loan loan) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		LoanProduct loanDetails = getLoanProductDetails(loan.getProductId());
		loan.applyLoanDetails(loanDetails);

		GeneralResponse<Loan> resp = endpointAcc.parseResponse(endpointAcc.sendHTTPRequest(loan, Type.POST,
				Settings.baseUrl.concat(CREATE_LOAN).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		if (resp != null && resp.getStatus().equals("200")) {
			loan.setId("" + resp.getResourceId());
			resp.setData(loan);
		}

		return resp;

	}

	private LoanProduct getLoanProductDetails(String productId) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		LoanProduct resp = endpoint.parseLoanProduct(endpoint.sendHTTPRequest("", Type.GET,
				Settings.baseUrl.concat(LOAN).concat("/").concat(productId).concat("?").concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;
	}

	public LoanDetails getLoan(String accountId) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		LoanDetails resp = endpointLoan.parseLoanDetails(endpointLoan.sendHTTPRequest("", Type.GET,
				Settings.baseUrl.concat(CREATE_LOAN).concat("/").concat(accountId).concat("?associations=all&")
						.concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;
	}

	public String approveLoan(String id) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		Approval approval = new Approval();

		endpointApp.sendHTTPRequest(approval, Type.POST,
				Settings.baseUrl.concat(CREATE_LOAN).concat("/").concat(id).concat("?command=approve&")
						.concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);

		return id;
	}

	public String payToEscrow() {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		Disbursement disbursement = new Disbursement();

		endpointDis.sendHTTPRequest(disbursement, Type.POST,
				Settings.baseUrl.concat(CREATE_LOAN).concat("/").concat(Settings.ESCROW_ACC_ID)
						.concat("?command=disburseToSavings&").concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);

		return Settings.ESCROW_ACC_ID;
	}

	public String payToCustomer(String id) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		Disbursement disbursement = new Disbursement();

		endpointDis.sendHTTPRequest(disbursement, Type.POST,
				Settings.baseUrl.concat(CREATE_LOAN).concat("/").concat(id).concat("?command=disburseToSavings&")
						.concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);

		return id;
	}

	public GeneralResponse<Repayment> repayLoanRequest(final Repayment payment, final String loanId) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		GeneralResponse<Repayment> resp = endpointPay.parseResponse(endpointPay.sendHTTPRequest(payment, Type.POST,
				Settings.baseUrl.concat(CREATE_LOAN).concat("/").concat(loanId)
						.concat("/transactions?command=repayment&")
						.concat(Settings.suffixUrl),
				headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		if (resp != null && resp.getStatus().equals("200")) {
			resp.setData(payment);
		}

		return resp;

	}

}
