package com.boat.bp.middleware.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.TokenResponse;


@Service
public class TokenService {


	@Autowired
	Endpoint<String,TokenResponse> endpoint;
	
	
	public TokenResponse getToken() {

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Fineract-Platform-TenantId", "default");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		final String request = "username=mifos&password=password&client_id=community-app&grant_type=password&client_secret=123";
		
		TokenResponse resp = endpoint.parseTokenResponse(endpoint.sendHTTPRequest(request, Type.POST,
				Settings.tokenUrl, headers, false,Settings.connectTimeOut, Settings.readTimeOut,null));


		return resp;

	}


}
