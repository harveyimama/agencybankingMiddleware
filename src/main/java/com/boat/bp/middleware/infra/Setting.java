package com.boat.bp.middleware.infra;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import lombok.Value;

@ConfigurationProperties
@ConstructorBinding
@Value
public class Setting {
    private String baseUrl;
    private String authorization;
    private String authMode;
    private String locale;
    private String dateFormat;
    private Integer officeId;
    private Integer walletProduct;
    private Integer agentClientType;
    private Integer consumerClientType;
}
