package com.ruchira.learn.grpc.user.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ruchira.learn.grpc.user.service.JwtService;
import com.ruchira.learn.grpc.user.service.UserService;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserService userService;

	public AuthTokenFilter(JwtService jwtService, UserService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!jwtService.isBearerToken(header)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			// Get jwt token and validate
			final String token = jwtService.getAuthToken(header);
			DecodedJWT decodedJWT = jwtService.decode(token);
			String username = decodedJWT.getSubject();
			UserDetails principle = userService.loadUserByUsername(username);

			if(principle == null) {
				throw new UsernameNotFoundException("Invalid user : " + username);
			}

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principle, null, principle.getAuthorities());
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
