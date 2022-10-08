package com.boat.bp.middleware.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import com.boat.bp.middleware.data.Loan;
import com.boat.bp.middleware.data.LoanDetails;
import com.boat.bp.middleware.data.Repayment;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.responses.ResponseCode;
import com.boat.bp.middleware.responses.Result;
import com.boat.bp.middleware.service.LoanService;
import com.boat.grpc.server.grpcserver.LoanRequest;
import com.boat.grpc.server.grpcserver.RepayLoanRequest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("v1/loan")
@Log4j2
public class LoanController {

    private final LoanService service;
    private GeneralResponse<Loan> loan;
    private GeneralResponse<Repayment> repaymnet;

    private LoanDetails currentLoanDetails;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping("processLoan")
    public Result<HashMap<String, String>> processLoan(@RequestBody LoanRequest request) {
        HashMap<String, String> res = new HashMap<String, String>();
        Flux<GeneralResponse<Loan>> respFlux = Flux.just(request).map(r -> this.service.loanRequest(new Loan(r)));

        respFlux.subscribe(this::setId);

        if (loan.getStatus().equals("200")) {

                    res.put("message", loan.getMessage());
                    res.put("status", loan.getStatus());
                    res.put("mifosLoanId", loan.getData().getId());

            Flux.just(loan.getData().getId()).map(id -> service.approveLoan(id))
                    .map(id -> {
                        if (request.getPayToEscrow())
                            return service.payToEscrow();
                        else
                            return service.payToCustomer(id);
                    }).subscribe();

            return Result.successful(res, loan.getMessage());

        } else {

            return Result.failed(ResponseCode.ERROR, loan.getMessage());

        }

    }

    @GetMapping("{accountId}/getLoanBalance")
    public Result<LoanDetails> getLoanBalance(@PathVariable("accountId") String accountId) {

        Flux<LoanDetails> respFlux = Flux.just(accountId).map(id -> service.getLoan(id));
        respFlux.subscribe(this::getLoan);
        return Result.successful(this.currentLoanDetails);
    }

    @PostMapping("repayLoan")
    public Result<HashMap<String, String>> repayLoan(@RequestBody RepayLoanRequest request) {

        HashMap<String, String> res = new HashMap<String, String>();
        Flux<GeneralResponse<Repayment>> respFlux = Flux.just(request)
                .map(r -> service.repayLoanRequest(new Repayment(r), r.getLoanId()));

        respFlux.subscribe(this::setPaymentId);

        if (repaymnet.getStatus().equals("200")) {
            res.put("message", loan.getMessage());
            res.put("status", loan.getStatus());
            res.put("mifosLoanRepaymentId", "" + loan.getResourceId());
            return Result.successful(res, loan.getMessage());
        } else {
            res.put("message", loan.getMessage());
            res.put("status", loan.getStatus());
            return Result.failed(ResponseCode.ERROR, loan.getMessage());

        }

        

    }

    private GeneralResponse<Loan> setId(GeneralResponse<Loan> resp) {
        this.loan = resp;
        return this.loan;
    }

    private GeneralResponse<Repayment> setPaymentId(GeneralResponse<Repayment> resp) {
        this.repaymnet = resp;
        return this.repaymnet;
    }

    private void getLoan(LoanDetails response) {
        this.currentLoanDetails = response;
    }
}
