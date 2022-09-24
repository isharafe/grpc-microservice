package com.ruchira.learn.grpc.user.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ruchira.learn.grpc.user.model.UserPrinciple;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtService jwtService;

	public String login(String username, String password, String issuer) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		// implement authentication using auth manager
		Authentication authenticate = authManager.authenticate(authenticationToken);

		return jwtService.getAccessToken(((UserPrinciple) authenticate.getPrincipal()).getUsername(), issuer,
				Collections.emptyMap());
	}
}
