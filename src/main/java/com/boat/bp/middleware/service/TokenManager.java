package com.boat.bp.middleware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.TokenResponse;

@Service
public class TokenManager {

	@Autowired
	TokenService service;

	public void setToken() {

		if(Settings.AUTH_MODE.equals("BASIC"))
		Settings.TOKEN = Settings.authorization;
		else
		{
			long currTime = System.currentTimeMillis() / 1000;

			if (currTime > Settings.TOKEN_EXPIRE || currTime == Settings.TOKEN_EXPIRE) {
	
				TokenResponse token = service.getToken();
				if (token.getError() == null) {
					Settings.TOKEN = token.getAccess_token();
					Settings.TOKEN_EXPIRE = currTime + Long.parseLong(token.getExpires_in());
				}
	
			}
		}



	}

}
