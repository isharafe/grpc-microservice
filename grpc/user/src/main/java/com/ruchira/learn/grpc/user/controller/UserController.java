package com.ruchira.learn.grpc.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ruchira.learn.grpc.user.model.AppUser;
import com.ruchira.learn.grpc.user.service.AuthenticationService;
import com.ruchira.learn.grpc.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authService;

	@GetMapping("/profile")
	public AppUser profile() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findUser(username);
	}

	@PostMapping("/login")
	public String login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
		return authService.login(username, password, request.getRequestURI());
	}
}
