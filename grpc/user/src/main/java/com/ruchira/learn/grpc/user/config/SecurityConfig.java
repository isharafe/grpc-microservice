package com.ruchira.learn.grpc.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ruchira.learn.grpc.user.filter.AuthTokenFilter;
import com.ruchira.learn.grpc.user.service.UserService;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, UserService userService, AuthTokenFilter authTokenFilter) throws Exception {
		http
		// need to disable csrf because we use rest calls instead of forms.
		// -> i.e. we don't send csrf tokens to client
		.csrf().disable()
		// we use jwt tokens, so no need of sessions
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeHttpRequests(
				(authz) -> authz
				.antMatchers("/profile").authenticated()
				.anyRequest().permitAll()
					)
		.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
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
