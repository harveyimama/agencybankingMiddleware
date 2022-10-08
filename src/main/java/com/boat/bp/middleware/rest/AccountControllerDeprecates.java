package com.boat.bp.middleware.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.boat.bp.middleware.mifos.MifosService;
import com.boat.bp.middleware.mifos.value.response.OnboardingResponse;
import com.boat.bp.middleware.responses.Result;
import com.boat.bp.middleware.rest.value.OnboardingRequestValue;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * Deprecated endpoints end up here, and will eventually be removed.
 */
@RestController
@RequestMapping("account")
@Log4j2
public class AccountControllerDeprecates {

    private final MifosService mifosService;

    public AccountControllerDeprecates(MifosService mifosService) {
        this.mifosService = mifosService;
    }
    
    @PostMapping("create")
    public Mono<Result<OnboardingResponse>> createAccount(@RequestBody OnboardingRequestValue onboardingRequest) {
        log.info(onboardingRequest.toString());
        return mifosService.onboardNewUser(onboardingRequest);
    }
}
