package com.boat.bp.middleware;
     
import com.boat.bp.middleware.data.Loan;
import com.boat.bp.middleware.data.LoanDetails;
import com.boat.bp.middleware.data.Repayment;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.service.LoanService;
import com.boat.grpc.server.grpcserver.BalanceRequest;
import com.boat.grpc.server.grpcserver.LoanBalanceResponse;
import com.boat.grpc.server.grpcserver.LoanRequest;
import com.boat.grpc.server.grpcserver.LoanResponse;
import com.boat.grpc.server.grpcserver.RepayLoanRequest;
import com.boat.grpc.server.grpcserver.RepayLoanResponse;
import com.boat.grpc.server.grpcserver.LoanServiceGrpc.LoanServiceImplBase;

import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class LoanServiceImpl extends LoanServiceImplBase {

    @Autowired
    LoanService service;

    private GeneralResponse<Loan> loan;

    private GeneralResponse<Repayment> repaymnet;

    private LoanDetails currentLoanDetails;

    @Override
    public void processLoan(LoanRequest request, StreamObserver<LoanResponse> responseObserver) {
        try {
            Flux<GeneralResponse<Loan>> respFlux = Flux.just(request).map(r -> service.loanRequest(new Loan(r)));

            respFlux.subscribe(this::setId);

            if (loan.getStatus().equals("200")) {
                LoanResponse response = LoanResponse.newBuilder().setMessage(loan.getMessage())
                        .setStatus(loan.getStatus())
                        .setMifosLoanId(loan.getData().getId())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                Flux.just(loan.getData().getId()).map(id -> service.approveLoan(id))
                        .map(id -> {
                            if (request.getPayToEscrow())
                                return service.payToEscrow();
                            else
                                return service.payToCustomer(id);
                        }).subscribe();

            } else {

                LoanResponse response = LoanResponse.newBuilder().setMessage(loan.getMessage())
                        .setStatus(loan.getStatus())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getLoanBalance(BalanceRequest request, StreamObserver<LoanBalanceResponse> responseObserver) {

        Flux<LoanDetails> respFlux = Flux.just(request).map(r -> service.getLoan(request.getAccountId()));

        respFlux.subscribe(this::getLoan);
        LoanBalanceResponse response = LoanBalanceResponse.newBuilder()
                .setAccountNo(currentLoanDetails.getAccountNo())
                .setLoanProductName(currentLoanDetails.getLoanProductName())
                .setTotalOutstanding(currentLoanDetails.getRepaymentSchedule().getTotalOutstanding())
                .setTotalRepayment(currentLoanDetails.getRepaymentSchedule().getTotalRepayment())
                .setTotalWaived(currentLoanDetails.getRepaymentSchedule().getTotalWaived())
                .setTotalWrittenOff(currentLoanDetails.getRepaymentSchedule().getTotalWrittenOff())
                .setActive(currentLoanDetails.getStatus().isActive())
                .setClosed(currentLoanDetails.getStatus().isClosed())
                .setClosedRescheduled(currentLoanDetails.getStatus().isClosedRescheduled())
                .setClosedObligationsMet(currentLoanDetails.getStatus().isClosedObligationsMet())
                .setClosedWrittenOff(currentLoanDetails.getStatus().isClosedWrittenOff())
                .setCode(currentLoanDetails.getStatus().getCode())
                .setLoanTermInDays(currentLoanDetails.getRepaymentSchedule().getLoanTermInDays())
                .setOverpaid(currentLoanDetails.getStatus().isOverpaid())
                .setTotalFeeChargesCharged(currentLoanDetails.getRepaymentSchedule().getTotalFeeChargesCharged())
                .setTotalInterestCharged(currentLoanDetails.getRepaymentSchedule().getTotalInterestCharged())
                .setTotalPenaltyChargesCharged(currentLoanDetails.getRepaymentSchedule().getTotalPenaltyChargesCharged())
                .setTotalPrincipalDisbursed(currentLoanDetails.getRepaymentSchedule().getTotalPrincipalDisbursed())
                .setTotalPrincipalExpected(currentLoanDetails.getRepaymentSchedule().getTotalPrincipalExpected())
                .setTotalPrincipalPaid(currentLoanDetails.getRepaymentSchedule().getTotalPrincipalPaid())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override   
    public void repayLoan(RepayLoanRequest request, StreamObserver<RepayLoanResponse> responseObserver) {
        try {
            Flux<GeneralResponse<Repayment>> respFlux = Flux.just(request).map(r -> service.repayLoanRequest(new Repayment(r),r.getLoanId()));

            respFlux.subscribe(this::setPaymentId);

            if (repaymnet.getStatus().equals("200")) {
                RepayLoanResponse response = RepayLoanResponse.newBuilder().setMessage(loan.getMessage())
                        .setStatus(loan.getStatus())
                        .setMifosLoanRepaymentId(""+loan.getResourceId())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            } else {

                RepayLoanResponse response = RepayLoanResponse.newBuilder().setMessage(loan.getMessage())
                        .setStatus(loan.getStatus())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }

        } catch (Exception e) {
            e.printStackTrace();
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
