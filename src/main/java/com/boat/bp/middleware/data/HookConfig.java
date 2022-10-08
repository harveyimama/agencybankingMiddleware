package com.boat.bp.middleware.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HookConfig {
   
    private List<Field> config;

	public List<Field> getConfig() {
		return config;
	}

	public void setConfig(List<Field> config) {
		this.config = config;
	}
    
    
   

    

}
