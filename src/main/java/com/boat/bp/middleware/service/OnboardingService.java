package com.boat.bp.middleware.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boat.bp.middleware.data.Callback;
import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.data.Activation;
import com.boat.bp.middleware.data.Approval;
import com.boat.bp.middleware.data.Client;
import com.boat.bp.middleware.data.ClientResource;
import com.boat.bp.middleware.data.User;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.responses.ListResponse;
import com.boat.bp.middleware.responses.PagedResponse;

@Service
public class OnboardingService {

	private final String CREATE_CLIENT = "clients";
	private final String CREATE_ACCOUNT = "savingsaccounts";
	private final String CREATE_USER = "users";

	@Autowired
	Endpoint<Client, GeneralResponse<Client>> endpoint;

	@Autowired
	Endpoint<String, PagedResponse> clientsEndpoint;

	@Autowired
	Endpoint<String, ListResponse<ClientResource>> listEndpoint;

	@Autowired
	Endpoint<Account, GeneralResponse<Account>> endpointAcc;

	@Autowired
	Endpoint<Activation, String> endpointActivaion;

	@Autowired
	Endpoint<User, GeneralResponse<User>> endpointUser;

	@Autowired
	Endpoint<Callback, GeneralResponse<String>> callbackEndpoint;

	@Autowired
	TokenManager manager;

	@Autowired
	Endpoint<Approval, String> endpointApproval;

	public GeneralResponse<Client> createClient(Client customer) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		//Temporary Shoudl eb taken out in prod
	  ClientResource oldClient = null;
        if(Settings.ALL_CLIENTS.length == 0)
		Settings.ALL_CLIENTS = this.getClients();

				for (ClientResource client : Settings.ALL_CLIENTS) {
			if ((null!=client.getExternalId() && client.getExternalId().equals(customer.getExternalId()))
					|| (null!=client.getMobileNo() && client.getMobileNo().equals(customer.getMobileNo()))) {
				System.out.println(client.getExternalId());
				oldClient = client;
			
			}
			if(oldClient !=null)
			break;
		}
	

		if (oldClient == null) {
			GeneralResponse<Client> resp = endpoint.parseResponse(endpoint.sendHTTPRequest(customer, Type.POST,
					Settings.baseUrl.concat(CREATE_CLIENT).concat("?").concat(Settings.suffixUrl), headers, false,
					Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

			if (resp != null && resp.getStatus().equals("200")) {
				customer.setClientId("" + resp.getClientId());
				resp.setData(customer);
			}

			return resp;
		} else {
		//-----------------------------------------
			GeneralResponse<Client> resp = new GeneralResponse<Client>();
			resp.setClientId(oldClient.getId());
			resp.setData(customer);
			resp.setSavingsId(oldClient.getSavingsAccountId());
			resp.setStatus("200");
			resp.setMessage("Action processed succesfully");

			return resp;
		}

	}

	public ClientResource[] getClients() {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		PagedResponse resp = clientsEndpoint.parsePageResponse(clientsEndpoint.sendHTTPRequest("", Type.GET,
				Settings.baseUrl.concat(CREATE_CLIENT).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp.getPageItems();

	}

	public GeneralResponse<Account> createAccount(Account account) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		GeneralResponse<Account> resp = endpointAcc.parseResponse(endpointAcc.sendHTTPRequest(account, Type.POST,
				Settings.baseUrl.concat(CREATE_ACCOUNT).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		if (resp != null && resp.getStatus().equals("200")) {
			account.setId("" + resp.getSavingsId());
			resp.setData(account);
		}

		return resp;

	}

	public void accountAutoApprovals(String accountId) {
		approveAccount(accountId);
		activateAccount(accountId);
	}

	public void approveAccount(String accountId) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		endpointApproval.sendHTTPRequest(new Approval(), Type.POST,
				Settings.baseUrl.concat(CREATE_ACCOUNT).concat("/").concat(accountId).concat("?command=approve&")
						.concat(Settings.suffixUrl),
				headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);

	}

	public void activateAccount(String accountId) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		endpointActivaion.sendHTTPRequest(new Activation(), Type.POST,
				Settings.baseUrl.concat(CREATE_ACCOUNT).concat("/").concat(accountId).concat("?command=activate&")
						.concat(Settings.suffixUrl),
				headers, false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN);

	}

	public GeneralResponse<User> createUser(User user) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		GeneralResponse<User> resp = endpointUser.parseResponse(endpointUser.sendHTTPRequest(user, Type.POST,
				Settings.baseUrl.concat(CREATE_USER).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));

		return resp;

	}

	public GeneralResponse<String> addCallBack(final String callBackURL) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		final String URL = Settings.baseUrl + "hooks/" + Settings.TRANSACTION_HOOK_ID + "?" + Settings.suffixUrl;
		GeneralResponse<String> resp = callbackEndpoint
				.parseResponse(callbackEndpoint.sendHTTPRequest(new Callback(callBackURL), Type.PUT, URL, headers,
						false, Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN));
		return resp;

	}

}
