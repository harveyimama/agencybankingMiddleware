package com.boat.bp.middleware.helper;

public enum Type {
	
	POST("POST"),GET("GET"),PUT("PUT"),DELETE("DELETE"),PATCH("PATCH");
	
	private String name ;

	private Type(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

		
	
}
