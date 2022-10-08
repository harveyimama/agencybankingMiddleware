package com.boat.bp.middleware.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.boat.bp.middleware.mifos.AccountActionResponse;
import com.boat.bp.middleware.mifos.Constant;
import com.boat.bp.middleware.mifos.MifosService;
import com.boat.bp.middleware.mifos.value.response.AccountBalanceValue;
import com.boat.bp.middleware.mifos.value.response.OnboardingResponse;
import com.boat.bp.middleware.responses.Result;
import com.boat.bp.middleware.rest.value.OnboardingRequestValue;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/account")
@Log4j2
public class AccountController {

    private final MifosService mifosService;

    public AccountController(MifosService mifosService) {
        this.mifosService = mifosService;
    }

    @PostMapping("onboard")
    public Mono<Result<OnboardingResponse>> createAccount(@RequestBody OnboardingRequestValue onboardingRequest) {
        log.info(onboardingRequest.toString());
        return mifosService.onboardNewUser(onboardingRequest);
    }
    
    @PostMapping("{clientId}/add-loan-wallet")
    public Mono<Result<AccountActionResponse>> addLoanWallet(@PathVariable("clientId") String clientId) {
        return mifosService.addAccount(clientId, Constant.SAVINGS_PRODUCT);
    }

    @GetMapping("balance/{accountId}")
    public Mono<Result<AccountBalanceValue>> getAccountBalance(@PathVariable("accountId") String accountId) {
        return mifosService.getAccountBalance(accountId);
    }
}
