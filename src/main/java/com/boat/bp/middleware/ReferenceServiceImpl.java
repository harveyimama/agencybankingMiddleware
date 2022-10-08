package com.boat.bp.middleware;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boat.bp.middleware.data.CodeValue;
import com.boat.bp.middleware.data.SavingsProduct;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.ListResponse;
import com.boat.bp.middleware.service.ReferenceDataService;
import com.boat.grpc.server.grpcserver.Empty;
import com.boat.grpc.server.grpcserver.Reference;
import com.boat.grpc.server.grpcserver.ReferenceResponse;
import com.boat.grpc.server.grpcserver.ReferenceServiceGrpc.ReferenceServiceImplBase;
import com.boat.grpc.server.grpcserver.SavingsProductResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ReferenceServiceImpl extends ReferenceServiceImplBase {

	@Autowired
	ReferenceDataService Refservice;

	@Override
	public void getSavingsProduct(

			Empty request, StreamObserver<SavingsProductResponse> responseObserver) {

		try {

			ListResponse<SavingsProduct> resp = Refservice.getSavingsProduct();
			SavingsProduct[] data = resp.getData();

			List<com.boat.grpc.server.grpcserver.SavingsProduct> ref = new ArrayList<com.boat.grpc.server.grpcserver.SavingsProduct>();

			for (SavingsProduct l : data) {
				ref.add(com.boat.grpc.server.grpcserver.SavingsProduct.newBuilder().setName(l.getName())
						.setDescription(l.getDescription()).setId("" + l.getId())

						.build());
			}

			SavingsProductResponse response = SavingsProductResponse.newBuilder().addAllProducts(ref).build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getStates(

			Empty request, StreamObserver<ReferenceResponse> responseObserver) {

		try {

			ListResponse<CodeValue> resp = Refservice.getCodeValue(Settings.STATE_PROVINCE);

			if (resp != null) {
				CodeValue[] data = resp.getData();
				ReferenceResponse response = ReferenceResponse.newBuilder().addAllReference(getReference(data)).build();
				responseObserver.onNext(response);
			} else
				responseObserver.onNext(null);

			responseObserver.onCompleted();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void getCountries(

			Empty request, StreamObserver<ReferenceResponse> responseObserver) {

		try {

			ListResponse<CodeValue> resp = Refservice.getCodeValue(Settings.COUNTRY);

			if (resp != null) {
				CodeValue[] data = resp.getData();
				ReferenceResponse response = ReferenceResponse.newBuilder().addAllReference(getReference(data)).build();
				responseObserver.onNext(response);
			} else
				responseObserver.onNext(null);
			responseObserver.onCompleted();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private List<Reference> getReference(CodeValue[] data) {
		List<Reference> ref = new ArrayList<Reference>();

		for (CodeValue l : data) {
			ref.add(Reference.newBuilder().setName(l.getName()).setDescription(l.getDescription()).setId("" + l.getId())
					.setPosition("" + l.getPosition()).build());
		}
		return ref;
	}

}