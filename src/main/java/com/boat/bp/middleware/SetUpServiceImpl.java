package com.boat.bp.middleware;

import org.springframework.beans.factory.annotation.Autowired;

import com.boat.bp.middleware.data.Client;
import com.boat.bp.middleware.data.User;
import com.boat.bp.middleware.helper.HTTPresponse;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.service.OnboardingService;
import com.boat.grpc.server.grpcserver.AddBankRequest;
import com.boat.grpc.server.grpcserver.AddBillerRequest;
import com.boat.grpc.server.grpcserver.AddCallBackRequest;
import com.boat.grpc.server.grpcserver.Empty;
import com.boat.grpc.server.grpcserver.SetUpResponse;
import com.boat.grpc.server.grpcserver.SetUpServiceGrpc.SetUpServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class SetUpServiceImpl extends SetUpServiceImplBase {

	@Autowired
	OnboardingService service;

	private GeneralResponse<Client> mainWallet;
	private GeneralResponse<Client> chargeWallet;
	private HTTPresponse HTTPSuccess = HTTPresponse.HTTPSUCCESS;

	@Override
	public void addBank(

			AddBankRequest request, StreamObserver<SetUpResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Client>> respFlux = Flux.just(request)
					.map(r -> service.createClient(new Client(r, true))).map(r -> setResponse(r, true))
					.map(r -> service.createClient(new Client(request, false))).map(r -> setResponse(r, false));

			respFlux.subscribe();

			if (mainWallet.getStatus().equals(HTTPSuccess.getCode())
					&& chargeWallet.getStatus().equals(HTTPSuccess.getCode())) {
				SetUpResponse response = SetUpResponse.newBuilder().setAccountId("" + mainWallet.getSavingsId())
						.setChargeAccountId("" + chargeWallet.getSavingsId())
						.setChargeClientId("" + chargeWallet.getClientId()).setClientId("" + mainWallet.getClientId())
						.setMessage(mainWallet.getMessage()).setStatus(mainWallet.getStatus()).build();

				Flux<Object> user = Flux.just(request).map(
						r -> service.createUser(new User(r, response.getClientId(), response.getChargeClientId())));

				responseObserver.onNext(response);
				responseObserver.onCompleted();

				user.subscribe();
			} else {
				SetUpResponse response = SetUpResponse.newBuilder()
						.setMessage(mainWallet.getMessage() + " " + chargeWallet.getMessage())
						.setStatus(mainWallet.getStatus() + "/ " + chargeWallet.getStatus()).build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private GeneralResponse<Client> setResponse(GeneralResponse<Client> r, boolean b) {
		if (b)
			this.mainWallet = r;
		else
			this.chargeWallet = r;
		return r;
	}

	@Override
	public void addBiller(

			AddBillerRequest request, StreamObserver<SetUpResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Client>> respFlux = Flux.just(request)
					.map(r -> service.createClient(new Client(r, true))).map(r -> setResponse(r, true))
					.map(r -> service.createClient(new Client(request, false))).map(r -> setResponse(r, false));

			respFlux.subscribe();

			if (mainWallet.getStatus().equals(HTTPSuccess.getCode())
					&& chargeWallet.getStatus().equals(HTTPSuccess.getCode())) {
				SetUpResponse response = SetUpResponse.newBuilder().setAccountId("" + mainWallet.getSavingsId())
						.setChargeAccountId("" + chargeWallet.getSavingsId())
						.setChargeClientId("" + chargeWallet.getClientId()).setClientId("" + mainWallet.getClientId())
						.setMessage(mainWallet.getMessage()).setStatus(mainWallet.getStatus()).build();

				Flux<Object> user = Flux.just(request).map(
						r -> service.createUser(new User(r, response.getClientId(), response.getChargeClientId())));

				responseObserver.onNext(response);
				responseObserver.onCompleted();

				user.subscribe();

			} else {
				SetUpResponse response = SetUpResponse.newBuilder()
						.setMessage(mainWallet.getMessage() + " " + chargeWallet.getMessage())
						.setStatus(mainWallet.getStatus() + "/ " + chargeWallet.getStatus()).build();
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void addCallBack(

			AddCallBackRequest request, StreamObserver<Empty> responseObserver) {

		try {

			Flux<GeneralResponse<String>> respFlux = Flux.just(request).map(r -> service.addCallBack(r.getUrl()));

			Empty response = Empty.newBuilder().build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();

			respFlux.subscribe();
			Settings.BOAT_HOOK = request.getUrl();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}