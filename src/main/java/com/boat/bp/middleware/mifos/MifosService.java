package com.boat.bp.middleware.mifos;

import static java.lang.String.format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.boat.bp.middleware.common.Util;
import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.infra.Setting;
import com.boat.bp.middleware.mifos.exception.RestProcessingException;
import com.boat.bp.middleware.mifos.value.request.NewAccountRequestValue;
import com.boat.bp.middleware.mifos.value.response.AccountBalanceValue;
import com.boat.bp.middleware.mifos.value.response.AccountBalanceValue.Summary;
import com.boat.bp.middleware.mifos.value.response.NewAccountResponseValue;
import com.boat.bp.middleware.mifos.value.response.OnboardingResponse;
import com.boat.bp.middleware.responses.ResponseCode;
import com.boat.bp.middleware.responses.Result;
import com.boat.bp.middleware.rest.value.OnboardingRequestValue;
import com.boat.bp.middleware.rest.value.Role;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
@Log4j2
public class MifosService {

    private static final String TENANT_IDENTIFIER = "tenantIdentifier";
    private static final String PRETTY = "pretty";

    private final WebClient webClient;
    private final Setting setting;
    private final MifosLegacyService mifosLegacyService;

    public MifosService(WebClient webClient, Setting setting, MifosLegacyService mifosLegacyService) {
        this.webClient = webClient;
        this.setting = setting;
        this.mifosLegacyService = mifosLegacyService;
    }


    public Mono<Result<NewAccountResponseValue>> createAccount(String clientId, Integer productId) {

        NewAccountRequestValue newAccountRequest = new NewAccountRequestValue(
            clientId, Constant.LOCALE, Constant.TIMESTAMP_FORMAT, productId, new Date());

        log.debug("Create account request: {}", () -> newAccountRequest.toString());
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(Constant.CREATE_ACCOUNT_ENDPOINT)
                        .queryParam(TENANT_IDENTIFIER, Constant.TENANT_IDENTIFIER)
                        .queryParam(PRETTY, Constant.PRETTY).build())
                .body(Mono.just(newAccountRequest), NewAccountRequestValue.class).retrieve()
                .bodyToMono(NewAccountResponseValue.class).map(Result::successful)
                .onErrorResume(WebClientResponseException.class, (ex) -> {
                    String errorId = Util.errorId();
                    if (log.isDebugEnabled()) {
                        log.debug("Create account payload: {}, id: {}",
                                newAccountRequest.toString(), errorId);
                    }
                    log.error("{}: {}, Status code: {},  response: {}", errorId, ex.getMessage(),
                            ex.getStatusCode(), ex.getResponseBodyAsString());
                    return Mono
                            .just(Result.failed(format("Account creation failed. (%s)", errorId)));
                }).onErrorResume(WebClientRequestException.class, (ex) -> {
                    String errorId = Util.errorId();
                    log.error("{}: {}, URI: {}, Headers: {}", errorId, ex.getMessage(), ex.getUri(),
                            ex.getHeaders());
                    return Mono
                            .just(Result.failed(format("Account creation failed. (%s)", errorId)));
                });
    }

    public Mono<Result<CreateClientResponseValue>> createClient(
            CreateClientValue createClientRequest) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(Constant.CREATE_CLIENT_ENDPOINT)
                        .queryParam(TENANT_IDENTIFIER, Constant.TENANT_IDENTIFIER)
                        .queryParam(PRETTY, Constant.PRETTY).build())
                .body(Mono.just(createClientRequest), CreateClientValue.class).retrieve()
                .bodyToMono(CreateClientResponseValue.class).map(Result::successful)
                .onErrorResume(WebClientResponseException.class, (ex) -> {
                    String errorId = Util.errorId();
                    if (log.isDebugEnabled()) {
                        log.debug("Create client payload: {}, id: {}",
                                createClientRequest.toString(), errorId);
                    }
                    log.error("{}: {}, Status code: {},  response: {}", errorId, ex.getMessage(),
                            ex.getStatusCode(), ex.getResponseBodyAsString());
                    return Mono
                            .just(Result.failed(format("Client creation failed. (%s)", errorId)));
                }).onErrorResume(WebClientRequestException.class, (ex) -> {
                    String errorId = Util.errorId();
                    log.error("{}: {}, URI: {}, Headers: {}", errorId, ex.getMessage(), ex.getUri(),
                            ex.getHeaders());
                    return Mono
                            .just(Result.failed(format("Client creation failed. (%s)", errorId)));
                });
    }
    
    public Mono<Result<AccountActionResponse>> doAccountCommand(String accountId, Command command) {
        Map<String, String> payload = getAccountActionPayload(command);

        log.debug("command: {}, payload: {}", command, new Gson().toJson(payload));

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(Constant.CREATE_ACCOUNT_ENDPOINT).path("/").path("{accountId}")
                        .queryParam(TENANT_IDENTIFIER, Constant.TENANT_IDENTIFIER)
                        .queryParam(PRETTY, Constant.PRETTY)
                        .queryParam("command", command.getValue()).build(accountId))
                .body(Mono.just(payload), Map.class).retrieve()
                .bodyToMono(AccountActionResponse.class).map(Result::successful)
                .onErrorResume(WebClientResponseException.class, (ex) -> {
                    String errorId = Util.errorId();
                    if (log.isDebugEnabled()) {
                        log.debug("Account {} request: {}, id: {}", command.getValue(), accountId, errorId);
                    }
                    log.error("{}: {}, Status code: {},  response: {}", errorId, ex.getMessage(),
                            ex.getStatusCode(), ex.getResponseBodyAsString());
                    return Mono
                            .just(Result.failed(format("Account '%s' operation failed.  (%s)", command.getValue(), errorId)));
                }).onErrorResume(WebClientRequestException.class, (ex) -> {
                    String errorId = Util.errorId();
                    log.error("{}: {}, URI: {}, Headers: {}", errorId, ex.getMessage(), ex.getUri(),
                            ex.getHeaders());
                    return Mono
                            .just(Result.failed(format("Account '%s' operation failed. (%s)", command.getValue(), errorId)));
                });
    }


    public Mono<Result<OnboardingResponse>> onboardNewUser(
            OnboardingRequestValue onboardingRequest) {
        Date timestamp = new Date();

        CreateClientValue createClientRequest = new CreateClientValue(setting.getOfficeId(),
                onboardingRequest.getFirstName(), onboardingRequest.getLastName(),
                onboardingRequest.getEmail(), Constant.TIMESTAMP_FORMAT, Constant.LOCALE, true,
                timestamp, timestamp, setting.getWalletProduct(), onboardingRequest.getPhone(),
                onboardingRequest.getRole() == Role.AGENT ? setting.getAgentClientType()
                        : setting.getConsumerClientType());

        return this.createClient(createClientRequest).map(createClientResult -> {
            if (createClientResult.isSuccessful()) {
                log.debug("client creating successful. result: {}", createClientResult.toString());
                return createClientResult.getData();
            } else {
                log.error("New user onboarding failed while creating client. User Phone No: {}",
                createClientRequest.getMobileNo());
                throw new RestProcessingException(createClientResult.getResponseMessage(),
                ResponseCode.ONBARDING_ERROR);
            }
        }).zipWhen(createClientResponseValue -> {
            
            return createAccount(createClientResponseValue.getClientId(), Constant.SAVINGS_PRODUCT)
                .map(createAccountResult -> {
                    if (createAccountResult.isSuccessful()) {
                        log.debug("account opening successful. result: {}", createAccountResult.toString());
                        return createAccountResult.getData();
                    }
                    OnboardingResponse payload = OnboardingResponse.builder()
                    .clientId(createClientResponseValue.getClientId())
                    .walletAccountId(createClientResponseValue.getSavingsId()).build();
                    throw new RestProcessingException(new Result<>(ResponseCode.ONBOARDING_ERROR_AO, createAccountResult.getResponseMessage(), payload));
                });
        }).flatMap(onboardingResultTuple -> {

            // TODO Extract approval/activation into a single operation. see https://boatrelated.atlassian.net/browse/FS-376
            CreateClientResponseValue clientResponseValue = onboardingResultTuple.getT1();
            NewAccountResponseValue newAccountResponseValue = onboardingResultTuple.getT2();

            return doAccountCommand(newAccountResponseValue.getSavingsId(), Command.APPROVE).map((approvalResult) -> {
                if (approvalResult.isSuccessful()) {
                    log.debug("account approval successful. result: {}", approvalResult.toString());
                    return Tuples.of(clientResponseValue, newAccountResponseValue, approvalResult.getData());
                }

                OnboardingResponse onboardingResponse = OnboardingResponse.builder()
                    .clientId(clientResponseValue.getClientId())
                    .walletAccountId(clientResponseValue.getSavingsId())
                    .savingsAccountId(newAccountResponseValue.getSavingsId())
                    .activated(false).build();
                throw new RestProcessingException(new Result<>(
                    ResponseCode.ONBOARDING_ERROR_AA, 
                    approvalResult.getResponseMessage(), 
                    onboardingResponse)
                );
            });
            
        }).flatMap((clientAccountApprovalTuple) -> {

            CreateClientResponseValue clientResponseValue = clientAccountApprovalTuple.getT1();
            NewAccountResponseValue newAccountResponseValue = clientAccountApprovalTuple.getT2();
            AccountActionResponse accountActionResponse = clientAccountApprovalTuple.getT3();

            return doAccountCommand(newAccountResponseValue.getSavingsId(), Command.ACTIVATE).map(activationResult -> {
                if (activationResult.isSuccessful()) {
                    log.info("account activation successful. result: {}", activationResult.toString());
                    return Tuples.of(clientResponseValue, newAccountResponseValue, accountActionResponse, activationResult.getData());
                }
    
                OnboardingResponse onboardingResponse = OnboardingResponse.builder()
                    .clientId(clientResponseValue.getClientId())
                    .walletAccountId(clientResponseValue.getSavingsId())
                    .savingsAccountId(newAccountResponseValue.getSavingsId())
                    .activated(false).build();
                throw new RestProcessingException(new Result<>(
                    ResponseCode.ONBOARDING_ERROR_AC, 
                    activationResult.getResponseMessage(), 
                    onboardingResponse)
                );
            });
        }).map(clientAccountApprovalActivationTuple -> {

            CreateClientResponseValue clientResponseValue = clientAccountApprovalActivationTuple.getT1();
            NewAccountResponseValue newAccountResponseValue = clientAccountApprovalActivationTuple.getT2();
            AccountActionResponse accountActionResponse = clientAccountApprovalActivationTuple.getT3();

            log.debug("{}-------------{}------------{}", 
                () -> clientResponseValue.toString(), 
                () -> newAccountResponseValue.toString(), 
                () -> accountActionResponse.toString()
            );

            OnboardingResponse payload = OnboardingResponse.builder()
            .clientId(clientResponseValue.getClientId())
            .walletAccountId(clientResponseValue.getSavingsId())
            .savingsAccountId(newAccountResponseValue.getSavingsId())
            .activated(true).build();

            return Result.successful(payload);
        })
        .doOnSuccess(
            result -> log.info("User onboarding completed successfully. Onboarding result: {}", result.toString())
        ).onErrorResume(RestProcessingException.class, ex -> {

            // check if this is an onboarding error and grab the supplied payload
            if (ex.getResult() != null && Integer.toString(ex.getResult().getResponseCode().getCode()).startsWith("1")) {
                OnboardingResponse payload = (OnboardingResponse) ex.getResult().getData();
                return Mono.just(new Result<>(ex.getResult().getResponseCode(), ex.getResult().getResponseMessage(), payload));
            } 
            return Mono.just(Result.failed(ex.getResponseCode(), ex.getMessage()));
        });
    }

    private Map<String, String> getAccountActionPayload(Command command) {
        HashMap<String, String> payload = new HashMap<>(3);
        payload.put("locale", Constant.LOCALE);

        if (command == Command.ACTIVATE) {
            payload.put("dateFormat", Constant.DATE_FORMAT);
            payload.put("activatedOnDate", new SimpleDateFormat(Constant.DATE_FORMAT).format(new Date()));
        } else if (command == Command.APPROVE) {
            payload.put("dateFormat", Constant.TIMESTAMP_FORMAT);
            payload.put("approvedOnDate", new SimpleDateFormat(Constant.TIMESTAMP_FORMAT).format(new Date()));
        }
        return payload;
    }


    public Mono<Result<AccountActionResponse>> addAccount(String clientId, int savingsProduct) {
        return createAccount(clientId, savingsProduct).flatMap(createAccountResult -> {
            log.debug("add account result: {}", () -> createAccountResult.toString());
            if (createAccountResult.isSuccessful()) {
                return approveAndActivateAccount(clientId, createAccountResult.getData().getSavingsId());
            }
            throw new RestProcessingException(createAccountResult);
        }).map(Result::successful)
        .onErrorResume(RestProcessingException.class, ex -> {
            // check if this is an onboarding error and grab the supplied payload
            // TODO check specific account errors
            if (ex.getResult() != null && Integer.toString(ex.getResult().getResponseCode().getCode()).startsWith("1")) {
                AccountActionResponse payload = (AccountActionResponse) ex.getResult().getData();
                return Mono.just(new Result<>(ex.getResult().getResponseCode(), ex.getResult().getResponseMessage(), payload));
            } 
            return Mono.just(Result.failed(ex.getResponseCode(), ex.getMessage()));
        });
    }

    public Mono<AccountActionResponse> approveAndActivateAccount(String clientId, String accountId) {
        return this.doAccountCommand(accountId, Command.APPROVE)
            .map(accountActionResult -> {
                if (accountActionResult.isSuccessful()) {
                    return accountActionResult.getData();
                }
                throw new RestProcessingException(accountActionResult);
            })
            .zipWhen(AccountActionResponse -> this.doAccountCommand(accountId, Command.ACTIVATE))
            .map((approvalActivationTuple) -> {
                AccountActionResponse approvalResponse = approvalActivationTuple.getT1();
                Result<AccountActionResponse> activationResult = approvalActivationTuple.getT2();
                if (activationResult.isSuccessful()) {
                    return activationResult.getData();
                }
                throw new RestProcessingException(
                    new Result<>(ResponseCode.ACCOUNT_ACTIVATION_ERROR, 
                    ResponseCode.ACCOUNT_ACTIVATION_ERROR.getDescription(), approvalResponse));
            }).doOnSuccess(finalAccountActionResult -> {
                log.info(
                    "Account successfully approved and activated. clientId: {}, account ID: {}, final result: {}", 
                    clientId, accountId, finalAccountActionResult.toString());
            });
    }


    public Mono<Result<AccountBalanceValue>> getAccountBalance(String accountId) {
        try {
            return mifosLegacyService.getAccountBalance(accountId).map(accountResult -> {
                if (accountResult.isSuccessful()) {
                    return Result.successful(parseAccount(accountResult.getData()));
                }
                return Result.failed(accountResult.getResponseCode(), accountResult.getResponseMessage());
            });
        }catch(Exception ex) {
            return Mono.just(Result.failed(ex.getMessage()));
        }
    }


    private AccountBalanceValue parseAccount(Account account) {
        Summary summary = new AccountBalanceValue.Summary(
            account.getSummary().getAccountBalance(), 
            account.getSummary().getAvailableBalance()
        );
        return new AccountBalanceValue(
            account.getId(),
            account.getClientId(),
            account.getProductId(),
            account.getDateFormat(),
            account.getLocale(),
            account.getSubmittedOnDate(),
            account.getAccountNo(),
            account.getSavingsProductName(),
            summary
        );
    }
}
