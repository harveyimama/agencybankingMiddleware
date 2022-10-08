package com.boat.bp.middleware;

import org.springframework.beans.factory.annotation.Autowired;

import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.data.Transaction;
import com.boat.bp.middleware.helper.ResponseMessage;
import com.boat.bp.middleware.helper.HTTPresponse;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.service.TransactionFactory;
import com.boat.bp.middleware.service.WalletTransferService;
import com.boat.grpc.server.grpcserver.BalanceRequest;
import com.boat.grpc.server.grpcserver.BalanceResponse;
import com.boat.grpc.server.grpcserver.BankToWalletRequest;
import com.boat.grpc.server.grpcserver.PayBillRequest;
import com.boat.grpc.server.grpcserver.PreProcessRequest;
import com.boat.grpc.server.grpcserver.PreProcessResponse;
import com.boat.grpc.server.grpcserver.ReversalRequest;
import com.boat.grpc.server.grpcserver.TransactionResponse;
import com.boat.grpc.server.grpcserver.TransactionServiceGrpc.TransactionServiceImplBase;
import com.boat.grpc.server.grpcserver.WalletToBankRequest;
import com.boat.grpc.server.grpcserver.WalletTransferRequest;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class TransactionServiceImpl extends TransactionServiceImplBase {

	@Autowired
	WalletTransferService walletService;

	@Autowired
	TransactionFactory tf;

	private Account account;
	private GeneralResponse<String> preTransactionRef;
	private GeneralResponse<Transaction> transactionRef;
	private HTTPresponse HTTPSuccess = HTTPresponse.HTTPSUCCESS;
	private HTTPresponse HTTPError = HTTPresponse.HTTPERROR;
	private ResponseMessage sucessMessage = ResponseMessage.SUCCESS_MESSAGE;
	private ResponseMessage hookMessage = ResponseMessage.HOOK_MESSAGE;

	@Override
	public void doWalletTransfer(WalletTransferRequest request, StreamObserver<TransactionResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Transaction>> respFlux = Flux.just(request)

					.map(r -> walletService.p2pTransfer(tf.getTransaction(request), null, 0))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCharges(request), r, 0)
							: this.returnTranFailure(r, false))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCommision(request), r, 0)
							: this.returnTranFailure(r, false));

			processTransactionResponse(responseObserver, respFlux);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void doPayBill(

			PayBillRequest request, StreamObserver<TransactionResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Transaction>> respFlux = Flux.just(request)

					.map(r -> walletService.releaseFunds(tf.getRelease(request)))
					.map(r -> r.getStatus().equals("200")
							? walletService.p2pTransfer(tf.getTransaction(request), null, 0)
							: this.returnReleaseFailure(r))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCharges(request), r, 0)
							: this.returnTranFailure(r, true))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCommision(request), r, 0)
							: this.returnTranFailure(r, false));

			processTransactionResponse(responseObserver, respFlux);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void doWalletToBank(

			WalletToBankRequest request, StreamObserver<TransactionResponse> responseObserver) {

		try {

			Flux<GeneralResponse<Transaction>> respFlux = Flux.just(request)

					.map(r -> walletService.releaseFunds(tf.getRelease(request)))
					.map(r -> r.getStatus().equals("200")
							? walletService.p2pTransfer(tf.getTransaction(request), null, 0)
							: this.returnReleaseFailure(r))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCharges(request), r, 0)
							: this.returnTranFailure(r, true))
					.map(r -> r.getStatus().equals("200")
							? walletService.p2pTransfer(tf.getThirdPartyCommission(request), r, 0)
							: this.returnTranFailure(r, false))
					.map(r -> r.getStatus().equals("200") ? walletService.p2pTransfer(tf.getCommision(request), r, 0)
							: this.returnTranFailure(r, false));

			processTransactionResponse(responseObserver, respFlux);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void doBankToWallet(
			BankToWalletRequest request, StreamObserver<TransactionResponse> responseObserver) {
		try {

			Flux<GeneralResponse<Transaction>> respFlux = Flux.just(request)
					.map(r -> walletService.p2pTransfer(tf.getTransaction(request), null, 0));

			processTransactionResponse(responseObserver, respFlux);

			Flux.just(request).map(r -> walletService.depositFunds(request)).subscribe();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getBalance(
			BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
		try {

			Flux<Account> respFlux = Flux.just(request).map(r -> walletService.getAccount(request.getAccountId()));

			respFlux.subscribe(this::getAccount);

			if (!"".equals(account.getAccountNo()) && null != account.getAccountNo()) {
				BalanceResponse response = BalanceResponse.newBuilder()
						.setAccountBalance(account.getSummary().getAccountBalance().doubleValue())
						.setAccountNo(account.getAccountNo())
						.setAvailableBalance(account.getSummary().getAvailableBalance().doubleValue())
						.setSavingsProductName(account.getSavingsProductName()).build();
				responseObserver.onNext(response);
			} else {
				BalanceResponse response = BalanceResponse.newBuilder().build();
				responseObserver.onNext(response);
			}

			responseObserver.onCompleted();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void doReversal(ReversalRequest request, StreamObserver<TransactionResponse> responseObserver) {
		try {

			Flux<GeneralResponse<String>> respFlux = Flux.just(request)
					.map(r -> walletService.reverseTransaction(request.getTransactionId(), request.getClientId()));

			processReversalResponse(responseObserver, respFlux, request);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void getAccount(Account response) {
		this.account = response;
	}

	private boolean hookHasBeenSet() {

		if (null == Settings.BOAT_HOOK) {
			walletService.setHook();
			if (null == Settings.BOAT_HOOK)
				return false;
			else
				return true;
		} else
			return true;
	}

	private <T> void processTransactionResponse(StreamObserver<TransactionResponse> responseObserver,
			Flux<GeneralResponse<Transaction>> respFlux) {

		respFlux.map(r -> setProcessRepsonse(r)).subscribe();
		TransactionResponse response = TransactionResponse.newBuilder().setMessage(this.transactionRef.getMessage())
				.setStatus(this.transactionRef.getStatus()).setMifosReference("" + this.transactionRef.getResourceId())
				.setReProcessRef(
						transactionRef.getPreTransactionref() == null ? "" : transactionRef.getPreTransactionref())
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();

	}

	private <T> void processReversalResponse(StreamObserver<TransactionResponse> responseObserver,
			Flux<GeneralResponse<String>> respFlux, T request) {
		if (hookHasBeenSet()) {
			TransactionResponse response = TransactionResponse.newBuilder().setMessage(sucessMessage.getMessage())
					.setStatus(HTTPSuccess.getCode()).build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();
			try {
				respFlux.subscribe();
			} catch (Exception e) {

			}

		} else {
			TransactionResponse response = TransactionResponse.newBuilder().setMessage(hookMessage.getMessage())
					.setStatus(HTTPError.getCode()).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();

		}
	}

	@Override
	public void doPreProcess(PreProcessRequest request, StreamObserver<PreProcessResponse> responseObserver) {

		try {

			Flux<GeneralResponse<String>> respFlux = Flux.just(request)
					.map(r -> walletService.doPreProcess(request.getFromAccountId(), request.getTransferAmount()))
					.map(r -> setPreProcessRepsonse(r));
			respFlux.subscribe();

			PreProcessResponse response = PreProcessResponse.newBuilder()
					.setMessage(this.preTransactionRef.getMessage()).setStatus(this.preTransactionRef.getStatus())
					.setPreTransactionref("" + this.preTransactionRef.getResourceId()).build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private GeneralResponse<String> setPreProcessRepsonse(final GeneralResponse<String> req) {
		this.preTransactionRef = req;
		return req;
	}

	private GeneralResponse<Transaction> setProcessRepsonse(final GeneralResponse<Transaction> req) {
		this.transactionRef = req;
		return req;
	}

	private GeneralResponse<Transaction> returnReleaseFailure(final GeneralResponse<String> resp) {
		GeneralResponse<Transaction> failure = new GeneralResponse<Transaction>();
		failure.setStatus(resp.getStatus());
		failure.setMessage(resp.getMessage());
		failure.setPreTransactionref(resp.getPreTransactionref());
		return failure;
	}

	private GeneralResponse<Transaction> returnTranFailure(GeneralResponse<Transaction> resp,
			boolean requiresReversal) {

		if (requiresReversal && null == resp.getPreTransactionref()) {

			Flux<GeneralResponse<String>> respFlux = Flux.just(requiresReversal).map(r -> walletService
					.doPreProcess(resp.getData().getFromAccountId(), resp.getData().getTransferAmount()))
					.map(r -> setPreProcessRepsonse(r));
			respFlux.subscribe();

			resp.setPreTransactionref("" + this.preTransactionRef.getResourceId());

			return resp;
		} else
			return resp;
	}

}