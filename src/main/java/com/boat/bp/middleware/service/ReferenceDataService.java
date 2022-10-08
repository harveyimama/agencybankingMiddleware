package com.boat.bp.middleware.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boat.bp.middleware.data.CodeValue;
import com.boat.bp.middleware.data.SavingsProduct;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.ListResponse;

@Service
public class ReferenceDataService {

	@Autowired
	Endpoint<String, ListResponse<SavingsProduct>> endpoint;
	@Autowired
	Endpoint<String, ListResponse<CodeValue>> codeEndpoint;
	@Autowired
	TokenManager manager;

	public ListResponse<SavingsProduct> getSavingsProduct() {
		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		ListResponse<SavingsProduct> resp = endpoint.parseList(endpoint.sendHTTPRequest(null, Type.GET,
				Settings.baseUrl.concat(Settings.SAVINGS).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN), Settings.SAVINGS);

		return resp;
	}

	public ListResponse<CodeValue> getCodeValue(String type) {

		manager.setToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		ListResponse<CodeValue> resp = codeEndpoint.parseList(endpoint.sendHTTPRequest(null, Type.GET,
				Settings.baseUrl.concat(type).concat("?").concat(Settings.suffixUrl), headers, false,
				Settings.connectTimeOut, Settings.readTimeOut, Settings.TOKEN), type);

		return resp;
	}

}
