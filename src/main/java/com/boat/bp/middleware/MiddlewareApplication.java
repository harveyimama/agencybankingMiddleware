package com.boat.bp.middleware;

import javax.net.ssl.SSLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import com.boat.bp.middleware.helper.InternalAccounts;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.infra.Setting;
import com.boat.bp.middleware.service.OnboardingService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;


@SpringBootApplication
@EnableConfigurationProperties(Setting.class)
public class MiddlewareApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {

        Settings.baseUrl = env.getProperty("baseUrl");
        Settings.suffixUrl = env.getProperty("suffixUrl");
        Settings.connectTimeOut = Integer.parseInt(env.getProperty("connectTimeOut"));
        Settings.readTimeOut = Integer.parseInt(env.getProperty("readTimeOut"));
        Settings.authorization = env.getProperty("authorization");
        Settings.officeId = env.getProperty("officeId");
        Settings.SAVINGS = env.getProperty("savings");
        Settings.COUNTRY = env.getProperty("country");
        Settings.STATE_PROVINCE = env.getProperty("state");
        Settings.COUNTRY_CODE = env.getProperty("countryCode");
        Settings.STATE_CODE = env.getProperty("stateCode");
        Settings.tokenUrl = env.getProperty("tokenUrl");
        Settings.AGENT_CLIENT_TYPE = env.getProperty("agentClientType");
        Settings.CUSTOMER_CLIENT_TYPE = env.getProperty("customerClientType");
        Settings.BANK_CHARGE_PRODUCT = env.getProperty("bankChargeProduct");
        Settings.BANK_PRODUCT = env.getProperty("bankProduct");
        Settings.ACCOUNT_TYPE = env.getProperty("accountType");
        Settings.COMMISSION_PRODUCT = env.getProperty("commsionProduct");
        Settings.WALLET_PRODUCT = env.getProperty("walletProduct");
        Settings.LOAN_PRODUCT = env.getProperty("loanProduct");
        Settings.SAVINGS_PRODUCT = env.getProperty("savingsProduct");
        Settings.BANK_CLIENT_TYPE = env.getProperty("bankClientType");
        Settings.BILLER_CLIENT_TYPE = env.getProperty("billerClientType");
        Settings.BILLER_CHARGE_PRODUCT = env.getProperty("billerChargeProduct");
        Settings.BILLER_PRODUCT = env.getProperty("billerProduct");
        Settings.AUTH_MODE = env.getProperty("authMode");
        Settings.CUSTOMER_ROLE = Integer.parseInt(env.getProperty("customerRole"));
        Settings.TRANSACTION_HOOK_ID = env.getProperty("transactionHookId");
        InternalAccounts.W2W_ACCOUNT = env.getProperty("wwAccount");
        InternalAccounts.W2W_CLIENT = env.getProperty("wwClient");
        InternalAccounts.W2W_PASS = env.getProperty("wwPass");
        Settings.officeName = env.getProperty("officeName");
        Settings.transferLimit = Integer.parseInt(env.getProperty("transferLimit"));
        Settings.PARTNER_BANK_ACC_ID = env.getProperty("pbAccount");
        Settings.PARTNER_BANK_CLIENT_ID = env.getProperty("pbClient");
        Settings.PARTNER_BANK_ACC_NO = env.getProperty("pbAccountNo");
        Settings.ESCROW_ACC_ID = env.getProperty("escrowAccId");


        // Temporary Should be takeing out in prod
        // Settings.ALL_CLIENTS = service.getClients();
        // System.out.print("print clinets");
        // System.out.print(Arrays.asList(Settings.ALL_CLIENTS).toString());
        // ---------------------------------------
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext =
                SpringApplication.run(MiddlewareApplication.class, args);
        // System.out.println(appContext.getBean(Setting.class));
    }


    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, Setting setting) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient)) 
            .baseUrl(setting.getBaseUrl()).defaultHeaders(httpHeaders -> {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("Authorization", setting.getAuthorization());
        }).build();
    }

}
