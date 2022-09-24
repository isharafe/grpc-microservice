package com.ruchira.learn.grpc.user.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ruchira.learn.grpc.order.gen.AuthServiceGrpc.AuthServiceImplBase;
import com.ruchira.learn.grpc.order.gen.AuthTokenRequest;
import com.ruchira.learn.grpc.order.gen.AuthTokenResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class RemoteAuthenticationService extends AuthServiceImplBase {

	private static Logger logger = LoggerFactory.getLogger(RemoteAuthenticationService.class);

	@Autowired
	private JwtService jwtService;

	private String hostAddress;
	private String hostName;

	public RemoteAuthenticationService() {
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void getUserAuthProfile(AuthTokenRequest request, StreamObserver<AuthTokenResponse> responseObserver) {
		logger.info("Received grpc auth request. host:{} - {}", hostAddress, hostName);

		String authHeader = request.getToken();
		String authToken = jwtService.getAuthToken(authHeader);
		DecodedJWT decodedJWT = jwtService.decode(authToken);

		AuthTokenResponse response = AuthTokenResponse.newBuilder()
		.setIsAuthenticated(true)
		.setUsername(decodedJWT.getSubject())
		.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();

		logger.info("Completed grpc. host:{} - {}", hostAddress, hostName);
	}
}
