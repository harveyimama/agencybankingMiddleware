package com.boat.bp.middleware.helper;

public enum ResponseMessage {
	
	MIFOS_FIVE_HUNDRED("{ \"developerMessage\": \"Unkwown MIFOs Error.\",\n\"httpStatusCode\": \"500\",\n\"errors\": [\n"
			+ "        {\n" + "            \"developerMessage\": \"Unkwown MIFOs Error.\"" + "        }\n" + "    ]\n")
	,SUCCESS_MESSAGE ("Transaction Process Initiated")
	,HOOK_MESSAGE ("No Hook Configuration found")
	,FAILURE_MESSAGE("Transaction Process Error");
	
	
	private String message ;
	
	

	private ResponseMessage(String message) {
		this.message = message;
	}



	public String getMessage() {
		return message;
	}

	

		
	
}
