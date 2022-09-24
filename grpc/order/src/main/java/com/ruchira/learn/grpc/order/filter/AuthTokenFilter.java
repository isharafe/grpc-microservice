package com.ruchira.learn.grpc.order.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ruchira.learn.grpc.order.gen.AuthServiceGrpc.AuthServiceBlockingStub;
import com.ruchira.learn.grpc.order.gen.AuthTokenRequest;
import com.ruchira.learn.grpc.order.gen.AuthTokenResponse;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@GrpcClient("authService")
	private AuthServiceBlockingStub authserviceStub;

	private String hostAddress;
	private String hostName;

	public AuthTokenFilter() {
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || header.trim().isEmpty()) {
			chain.doFilter(request, response);
			return;
		}

		try {
			logger.info("Issuing grpc call to authenticate the user. host: {} - {}", hostAddress, hostName);
			AuthTokenResponse userAuthProfile = authserviceStub.getUserAuthProfile(
					AuthTokenRequest.newBuilder()
					.setToken(header)
					.build());
			logger.info("Received response to grpc call to authenticate the user. host: {} - {}", hostAddress, hostName);

			if(!Boolean.TRUE.equals(userAuthProfile.getIsAuthenticated())) {
				throw new AuthenticationException("Invalid token : ");
			}

			String principle = userAuthProfile.getUsername();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principle,
					null, Collections.emptyList());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request, response);
		} catch (Throwable t) {
			// jwt decode will fail for invalid and expired tokens
			response.setHeader("error", t.getMessage());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}

	}

}
