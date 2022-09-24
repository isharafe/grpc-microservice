package com.ruchira.learn.grpc.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ruchira.learn.grpc.user.service.UserService;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
		http
		.authorizeHttpRequests(
				(authz) -> authz
				.antMatchers("/profile").authenticated()
				.anyRequest().permitAll()
		).formLogin();

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(@Autowired PasswordEncoder passwordEncoder, UserService userService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	/**
	 * AuthenticationManager is not visible by default
	 * declare it here as a bean to expose this bean to other classes
	 */
	public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserService userService)
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userService)
	      .passwordEncoder(passwordEncoder)
	      .and()
	      .build();
	}

}
