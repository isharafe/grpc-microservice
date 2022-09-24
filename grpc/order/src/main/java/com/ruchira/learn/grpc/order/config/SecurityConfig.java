package com.ruchira.learn.grpc.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		// need to disable csrf because we use rest calls instead of forms.
		// -> i.e. we don't send csrf tokens to client
		.csrf().disable()
		// we use jwt tokens, so no need of sessions
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeHttpRequests(
				(authz) -> authz
				.anyRequest().permitAll()
		);
		return http.build();
	}

}
