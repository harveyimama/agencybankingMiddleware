package com.boat.bp.middleware.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.data.Transaction;
import com.boat.bp.middleware.data.Beneficiary;
import com.boat.bp.middleware.data.Deposit;
import com.boat.bp.middleware.data.Hook;
import com.boat.bp.middleware.data.PreProcess;
import com.boat.bp.middleware.helper.BasicAuth;
import com.boat.bp.middleware.helper.HTTPresponse;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.grpc.server.grpcserver.BankToWalletRequest;
import com.boat.bp.middleware.responses.Error;

@Service
public class WalletTransferService {

	@Autowired
	Endpoint<Transaction, GeneralResponse<Transaction>> endpoint;
	@Autowired
	Endpoint<String, GeneralResponse<String>> stringEndpoint;
	@Autowired
	Endpoint<Deposit, String> depositEndpoint;
	@Autowired
	TokenManager manager;
	@Autowired
	Endpoint<PreProcess, GeneralResponse<String>> preProcessEndpoint;
	@Autowired
	Endpoint<Beneficiary, GeneralResponse<String>> beneEndpoint;
	@Autowired
	Endpoint<Hook, GeneralResponse<String>> hookEndpoint;
	@Autowired
	Endpoint<String, String> callBackEndpoint;

	private final String BENEFICIARY_ERROR = "invalid.to.account.details";
	private final int MAX_RETRY = 2;
	private final HTTPresponse HTTPSuccess = HTTPresponse.HTTPSUCCESS;

	public GeneralResponse<Transaction> p2pTransfer(Transaction tran,
			final GeneralResponse<Transaction> oldResp, int retryAttempts) {

		GeneralResponse<Transaction> ret = new GeneralResponse<Transaction>();
		retryAttempts++;
		try {

			if (null != tran && (retryAttempts < MAX_RETRY || retryAttempts == MAX_RETRY)) {

				boolean beneficaryStatus = true;
				final String userToken = BasicAuth.encode(tran.getPassword());

				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				final String TRANSACTION_URL = Settings.baseUrl + "self/accounttransfers?type=tpt&"
						+ Settings.suffixUrl;
				GeneralResponse<Transaction> resp = endpoint
						.parseResponse(endpoint.sendHTTPRequest(getTransactionRequest(tran), Type.POST, TRANSACTION_URL, headers,
								false, Settings.connectTimeOut, Settings.readTimeOut, userToken));

				if (null != resp && resp.getStatus().equals(HTTPSuccess.getCode())) {
					tran.setResourceId("" + resp.getResourceId());
					resp.setData(tran);
					return oldResp == null ? resp : oldResp;
				} else {
					if (null != resp && resp.getErrors().size() > 0) {
						for (Error e : resp.getErrors())
							if (e.getMessage().contains(BENEFICIARY_ERROR)) {
								beneficaryStatus = false;
								break;
							}
					} else {
						ret.setStatus("500");
						ret.setData(tran);
						ret.setMessage("Error Occured");
						return oldResp == null ? ret : oldResp;
					}

					if (!beneficaryStatus) {
						try {
							addBeneficairy(new Beneficiary(getAccount(tran.getToAccountId()), tran.getFromClientId()),
									userToken);
						} catch (Exception e) {
							ret.setStatus("400");
							ret.setData(tran);
							ret.setMessage("" + resp.getErrors().get(0).getMessage());
							return oldResp == null ? ret : oldResp;
						}
						return p2pTransfer(tran, oldResp, retryAttempts);
					}

					ret.setStatus("400");
					ret.setData(tran);
					ret.setMessage("" + resp.getErrors().get(0).getMessage());
					return oldResp == null ? ret : oldResp;

				}

			}

			ret.setStatus("400");
			ret.setData(tran);
			ret.setMessage("Unable to process transaction");
			return oldResp == null ? ret : oldResp;

		} catch (Exception e) {
			e.printStackTrace();
			ret.setStatus("200");
			ret.setData(tran);
			ret.setMessage("Transaction in processing state");
			return oldResp == null ? ret : oldResp;
		}

	}

	public Account getAccount(final String accountId) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		final String BALANCE_URL = Settings.baseUrl + "savingsaccounts/" + accountId + "?" + Settings.suffixUrl;
		Account resp = endpoint.parseBalance(endpoint.sendHTTPRequest(null, Type.GET, BALANCE_URL, headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;
	}

	public GeneralResponse<String> reverseTransaction(final String tranId, final String clientId) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		final String REVERSAL_URL = Settings.baseUrl + "clients/" + clientId + "/transactions/" + tranId
				+ "?command=undo&" + Settings.suffixUrl;
		GeneralResponse<String> resp = stringEndpoint.parseResponse(stringEndpoint.sendHTTPRequest("", Type.POST,
				REVERSAL_URL, headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;

	}

	private void addBeneficairy(final Beneficiary beneficiary, final String userToken) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		final String URL = Settings.baseUrl + "self/beneficiaries/tpt?" + Settings.suffixUrl;
		beneEndpoint.sendHTTPRequest(beneficiary, Type.POST, URL, headers, false, Settings.connectTimeOut,
				Settings.readTimeOut, userToken);
	}

	public void setHook() {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		final String URL = Settings.baseUrl + "hooks/" + Settings.TRANSACTION_HOOK_ID + "?" + Settings.suffixUrl;
		String hook = callBackEndpoint.parseHook(callBackEndpoint.sendHTTPRequest(null, Type.GET, URL, headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		Settings.BOAT_HOOK = hook;
	}

	public <T> String sendStatus(final Hook<T> hook) {

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		hookEndpoint.sendHTTPRequest(hook, Type.POST, Settings.BOAT_HOOK, headers, false, Settings.connectTimeOut,
				Settings.readTimeOut, null);

		return null;
	}

	@SuppressWarnings("unused")
	private <T> void handleError(final GeneralResponse<?> response, final T request) {

		if (!HTTPSuccess.getCode().equals(response.getStatus())) {
			this.sendStatus(new Hook<T>(request, response));
		}

	}

	public GeneralResponse<String> doPreProcess(final String acct, final String amt) {
		manager.setToken();
		PreProcess preProcess = new PreProcess(amt);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		final String URL = Settings.baseUrl + "savingsaccounts/" + acct + "/transactions/"
				+ "?command=holdAmount&" + Settings.suffixUrl;
		GeneralResponse<String> resp = preProcessEndpoint.parseResponse(preProcessEndpoint.sendHTTPRequest(preProcess,
				Type.POST, URL, headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;
	}

	public GeneralResponse<String> releaseFunds(final Transaction tran) {
		try {

			manager.setToken();
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			final String URL = Settings.baseUrl + "savingsaccounts/" + tran.getFromAccountId() + "/transactions/"
					+ tran.getPreTransactionref() + "?command=releaseAmount&" + Settings.suffixUrl;
			GeneralResponse<String> resp = stringEndpoint
					.parseResponse(stringEndpoint.sendHTTPRequest("", Type.POST, URL,
							headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

			resp.setPreTransactionref(tran.getPreTransactionref());

			if (!resp.getStatus().equals("200"))
				resp.setMessage(resp.getErrors().get(0).getMessage());

			return resp;

		} catch (Exception e) {
			GeneralResponse<String> resp = new GeneralResponse<String>();
			resp.setStatus("200");
			resp.setMessage("Processing funds release");
			return resp;
		}

	}

	public String depositFunds(BankToWalletRequest request) {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		final String TP_URL = Settings.baseUrl + "savingsaccounts/" + Settings.PARTNER_BANK_ACC_ID
				+ "/transactions/?command=deposit&" + Settings.suffixUrl;
		return depositEndpoint.sendHTTPRequest(new Deposit(request.getThirdPartyCommissionAmount()), Type.POST,
				TP_URL, headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);
	}

	private Transaction getTransactionRequest(Transaction tran) {

		Transaction transactionRequest = new Transaction();
		transactionRequest.setDateFormat(tran.getDateFormat());
		transactionRequest.setFromAccountId(tran.getFromAccountId());
		transactionRequest.setFromAccountType(tran.getToAccountType());
		transactionRequest.setFromClientId(tran.getFromClientId());
		transactionRequest.setFromOfficeId(tran.getFromOfficeId());
		transactionRequest.setLocale(tran.getLocale());
		transactionRequest.setPreTransactionref(tran.getPreTransactionref());
		transactionRequest.setToAccountId(tran.getToAccountId());
		transactionRequest.setToAccountType(tran.getToAccountType());
		transactionRequest.setToClientId(tran.getToClientId());
		transactionRequest.setToOfficeId(tran.getToOfficeId());
		transactionRequest.setTransferAmount(tran.getTransferAmount());
		transactionRequest.setTransferDate(tran.getTransferDate());
		transactionRequest.setTransferDescription(tran.getTransferDescription());

		return transactionRequest;

	}

}
