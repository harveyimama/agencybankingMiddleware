package com.boat.bp.middleware;

import org.springframework.beans.factory.annotation.Autowired;

import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.data.Client;
import com.boat.bp.middleware.data.User;
import com.boat.bp.middleware.helper.HTTPresponse;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.service.OnboardingService;
import com.boat.grpc.server.grpcserver.LoanOnboardingRequest;
import com.boat.grpc.server.grpcserver.LoanOnboardingResponse;
import com.boat.grpc.server.grpcserver.OnboardingRequest;
import com.boat.grpc.server.grpcserver.OnboardingResponse;
import com.boat.grpc.server.grpcserver.OnboardingServiceGrpc.OnboardingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class OnboardingServiceImpl extends OnboardingServiceImplBase {
	@Autowired
	OnboardingService service;

	private GeneralResponse<Client> resp;
	private GeneralResponse<Account> accResp;
	private HTTPresponse HTTPSuccess = HTTPresponse.HTTPSUCCESS;
	private HTTPresponse HTTPError = HTTPresponse.HTTPERROR;

	@SuppressWarnings("static-access")
	@Override
	public void addCustomer(

			OnboardingRequest request, StreamObserver<OnboardingResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Client>> respFlux = Flux.just(request).map(r -> service.createClient(new Client(r)));

			respFlux.subscribe(this::setRepsonse);

			if (resp.getStatus().equals(HTTPSuccess.getCode())) {

				request = request.newBuilder(request).setClientId("" + resp.getClientId())
						.setWalletAccountId("" + resp.getSavingsId()).build();

				Flux.just(request).map(r -> service.createAccount(new Account(r, Settings.SAVINGS_PRODUCT)))
						.map(this::setAcctRepsonse).subscribe(service::accountAutoApprovals);

				request = request.newBuilder(request).setSavingsAccountId("" + accResp.getSavingsId()).build();

				if ("agent".equals(request.getRole())) {
					Flux.just(request).map(r -> service.createAccount(new Account(r, Settings.COMMISSION_PRODUCT)))
							.map(this::setAcctRepsonse)
							.subscribe(service::accountAutoApprovals);

					request = request.newBuilder(request).setCommissionAccountId("" + accResp.getSavingsId()).build();
				}

				Flux.just(request).map(r -> service.createAccount(new Account(r, Settings.SAVINGS_PRODUCT)))
						.map(this::setAcctRepsonse).subscribe(service::accountAutoApprovals);

				request = request.newBuilder(request).setLoanAccountId("" + accResp.getSavingsId()).build();

				OnboardingResponse response = OnboardingResponse.newBuilder().setMessage(resp.getMessage())
						.setStatus(HTTPSuccess.getCode()).setCustomer(request).build();

				responseObserver.onNext(response);
				responseObserver.onCompleted();

				Flux<GeneralResponse<User>> respUserFlux = Flux.just(request).map(r -> service.createUser(new User(r)));

				respUserFlux.subscribe();

			} else {
				OnboardingResponse response = OnboardingResponse.newBuilder().setMessage(resp.getMessage())
						.setStatus(resp.getStatus()).build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}

		} catch (Exception e) {

			OnboardingResponse response = OnboardingResponse.newBuilder().setMessage(e.getMessage())
					.setStatus(HTTPError.getCode()).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();

			e.printStackTrace();
		}

	}

	
	@Override
	public void addLoanWallet(
		LoanOnboardingRequest request, StreamObserver<LoanOnboardingResponse> responseObserver) {
		try {

			Flux.just(request).map(r -> service.createAccount(new Account(r.getClientId(), Settings.SAVINGS_PRODUCT)))
					.map(this::setAcctRepsonse).subscribe(service::accountAutoApprovals);

			if (accResp.getStatus().equals(HTTPSuccess.getCode())) {

				LoanOnboardingResponse response = LoanOnboardingResponse.newBuilder().setMessage(accResp.getMessage())
						.setStatus(HTTPSuccess.getCode()).setLoanAccountId(""+accResp.getSavingsId()).build(); 

				responseObserver.onNext(response);
				responseObserver.onCompleted();

			} else {
				LoanOnboardingResponse response = LoanOnboardingResponse.newBuilder().setMessage(accResp.getMessage())
						.setStatus(accResp.getStatus()).build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}

		} catch (Exception e) {

			LoanOnboardingResponse response = LoanOnboardingResponse.newBuilder().setMessage(e.getMessage())
					.setStatus(HTTPError.getCode()).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();

			e.printStackTrace();
		}

	}

	private void setRepsonse(GeneralResponse<Client> in) {
		this.resp = in;
	}

	private String setAcctRepsonse(GeneralResponse<Account> in) {
		this.accResp = in;
		return "" + this.accResp.getSavingsId();
	}

}