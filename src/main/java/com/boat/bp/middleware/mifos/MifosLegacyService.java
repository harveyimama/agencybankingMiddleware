package com.boat.bp.middleware.mifos;

import org.springframework.stereotype.Service;
import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.responses.ResponseCode;
import com.boat.bp.middleware.responses.Result;
import com.boat.bp.middleware.service.WalletTransferService;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * Implementations to be refactored
 */
@Service
@Log4j2
public class MifosLegacyService {

    private final WalletTransferService walletTransferService;

    public MifosLegacyService(WalletTransferService walletTransferService) {
        this.walletTransferService = walletTransferService;
    }

    public Mono<Result<Account>> getAccountBalance(String accountId) {
        Account account = walletTransferService.getAccount(accountId);
        log.debug("account returned: {}", () -> account.toString());
        return Mono.just(account.getId() != null ? Result.successful(account) : Result.failed(ResponseCode.ERROR, "Could not retrieve account"));
    }
}
