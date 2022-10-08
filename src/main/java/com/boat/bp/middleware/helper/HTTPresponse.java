package com.boat.bp.middleware.helper;

public enum HTTPresponse {
	
	HTTPERROR("500"),HTTPINVALID("400"),HTTPSUCCESS("200");
	
	private String code ;

	private HTTPresponse(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	

		
	
}
