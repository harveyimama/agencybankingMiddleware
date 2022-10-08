package com.boat.bp.middleware.service;

import org.springframework.stereotype.Service;

import com.boat.bp.middleware.data.Transaction;
import com.boat.bp.middleware.helper.InternalAccounts;
import com.boat.bp.middleware.helper.Settings;
import com.boat.grpc.server.grpcserver.BankToWalletRequest;
import com.boat.grpc.server.grpcserver.PayBillRequest;
import com.boat.grpc.server.grpcserver.WalletToBankRequest;
import com.boat.grpc.server.grpcserver.WalletTransferRequest;

@Service
public class TransactionFactory {

	public Transaction getTransaction(WalletTransferRequest request) {
		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
		transaction.setFromClientId(request.getFromClientId());
		transaction.setToAccountId(request.getToAccountId());
		transaction.setToAccountType(Settings.ACCOUNT_TYPE);
		transaction.setToClientId(request.getToClientId());
		transaction.setTransferAmount(request.getTransferAmount());
		transaction.setTransferDescription(request.getTransferDescription());
		transaction.setPassword(request.getSenderPhone());

		return transaction;
	}

	public Transaction getCharges(WalletTransferRequest request) {

		if (null != request.getChargeAmount() && !"".equals(request.getChargeAmount())
				&& Double.parseDouble(request.getChargeAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(InternalAccounts.W2W_ACCOUNT);
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(InternalAccounts.W2W_CLIENT);
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CH"));
			

			if (request.getChargeSender()) {
				transaction.setFromAccountId(request.getFromAccountId());
				transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
				transaction.setFromClientId(request.getFromClientId());
				transaction.setPassword(request.getSenderPhone());
			} else {
				transaction.setFromAccountId(request.getToAccountId());
				transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
				transaction.setFromClientId(request.getToClientId());
				transaction.setPassword(request.getReceiverPhone());
			}
			return transaction;
		} else

			return null;
	}

	public Transaction getCommision(WalletTransferRequest request) {
		if (null != request.getCommissionAmount() && !"".equals(request.getCommissionAmount())
				&& Double.parseDouble(request.getCommissionAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(request.getFromAccountId());
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(request.getFromClientId());
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CO"));
			transaction.setFromAccountId(InternalAccounts.W2W_ACCOUNT);
			transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
			transaction.setFromClientId(InternalAccounts.W2W_CLIENT);
			transaction.setPassword(InternalAccounts.W2W_PASS);

			return transaction;
		} else

			return null;
	}

	public Transaction getTransaction(PayBillRequest request) {

		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
		transaction.setFromClientId(request.getFromClientId());
		transaction.setToAccountId(request.getToBillerAccountId());
		transaction.setToAccountType(Settings.ACCOUNT_TYPE);
		transaction.setToClientId(request.getToBillerClientId());
		transaction.setTransferAmount(request.getTransferAmount());
		transaction.setTransferDescription(request.getTransferDescription());
		transaction.setPassword(request.getSenderPhone());

		return transaction;
	}

	public Transaction getRelease(PayBillRequest request) {

		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setPreTransactionref(request.getPreTransactionref());
		return transaction;
	}

	public Transaction getCharges(PayBillRequest request) {

		if (null != request.getChargeAmount() && !"".equals(request.getChargeAmount())
				&& Double.parseDouble(request.getChargeAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(request.getChargeAccountId());
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(request.getChargeClientId());
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CH"));

			if (request.getChargeSender()) {
				transaction.setFromAccountId(request.getFromAccountId());
				transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
				transaction.setFromClientId(request.getFromClientId());
				transaction.setPassword(request.getSenderPhone());
			} else {
				transaction.setFromAccountId(request.getToBillerAccountId());
				transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
				transaction.setFromClientId(request.getToBillerClientId());
				transaction.setPassword(request.getReceiverPhone());
			}

			return transaction;
		} else

			return null;
	}

	public Transaction getCommision(PayBillRequest request) {
		
		if (null != request.getCommissionAmount() && !"".equals(request.getCommissionAmount())
				&& Double.parseDouble(request.getCommissionAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(request.getFromAccountId());
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(request.getFromClientId());
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CO"));
			transaction.setFromAccountId(request.getChargeAccountId());
			transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
			transaction.setFromClientId(request.getChargeClientId());
			transaction.setPassword( request.getReceiverPhone());

			return transaction;
		} else

			return null;
	}

	public Transaction getTransaction(WalletToBankRequest request) {

		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
		transaction.setFromClientId(request.getFromClientId());
		transaction.setToAccountId(request.getToBankAccountId());
		transaction.setToAccountType(Settings.ACCOUNT_TYPE);
		transaction.setToClientId(request.getToBankClientId());
		transaction.setTransferAmount(request.getTransferAmount());
		transaction.setTransferDescription(request.getTransferDescription());
		transaction.setPassword(request.getSenderPhone());

		return transaction;
	}

	public Transaction getRelease(WalletToBankRequest request) {

		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromAccountId());
		transaction.setPreTransactionref(request.getPreTransactionref());
		return transaction;
	}

	public Transaction getCharges(WalletToBankRequest request) {

		if (null != request.getChargeAmount() && !"".equals(request.getChargeAmount())
				&& Double.parseDouble(request.getChargeAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(request.getChargeAccountId());
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(request.getChargeClientId());
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CH"));
			transaction.setFromAccountId(request.getFromAccountId());
			transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
			transaction.setFromClientId(request.getFromClientId());
			transaction.setPassword(request.getSenderPhone());

			return transaction;
		} else

			return null;
	}

	public Transaction getThirdPartyCommission(WalletToBankRequest request) {

		if (null != request.getChargeAmount() && !"".equals(request.getChargeAmount())
				&& Double.parseDouble(request.getChargeAmount()) > 0.0) {

					Transaction transaction = new Transaction();
					transaction.setToAccountId(Settings.PARTNER_BANK_ACC_ID);
					transaction.setToAccountType(Settings.ACCOUNT_TYPE);
					transaction.setToClientId(Settings.PARTNER_BANK_CLIENT_ID);
					transaction.setTransferAmount(request.getThirdPartyCommissionAmount());
					transaction.setTransferDescription(request.getTransferDescription().concat("-TP-CO"));
					transaction.setFromAccountId(request.getChargeAccountId());
					transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
					transaction.setFromClientId(request.getChargeClientId());
					transaction.setPassword( request.getReceiverPhone());

			return transaction;
		} else

			return null;
	}

	public Transaction getCommision(WalletToBankRequest request) {
		
		if (null != request.getCommissionAmount() && !"".equals(request.getCommissionAmount())
				&& Double.parseDouble(request.getCommissionAmount()) > 0.0) {

			Transaction transaction = new Transaction();
			transaction.setToAccountId(request.getFromAccountId());
			transaction.setToAccountType(Settings.ACCOUNT_TYPE);
			transaction.setToClientId(request.getFromClientId());
			transaction.setTransferAmount(request.getChargeAmount());
			transaction.setTransferDescription(request.getTransferDescription().concat("-CO"));
			transaction.setFromAccountId(request.getChargeAccountId());
			transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
			transaction.setFromClientId(request.getChargeClientId());
			transaction.setPassword( request.getReceiverPhone());

			return transaction;
		} else

			return null;
	}

	public Transaction getTransaction(BankToWalletRequest request) {

		Transaction transaction = new Transaction();
		transaction.setFromAccountId(request.getFromBankAccountId());
		transaction.setFromAccountType(Settings.ACCOUNT_TYPE);
		transaction.setFromClientId(request.getFromBankClientId());
		transaction.setToAccountId(request.getToAccountId());
		transaction.setToAccountType(Settings.ACCOUNT_TYPE);
		transaction.setToClientId(request.getToClientId());
		transaction.setTransferAmount(request.getTransferAmount());
		transaction.setTransferDescription(request.getTransferDescription());
		transaction.setPassword(request.getSenderPhone());

		return transaction;
	}


}
