package com.ruchira.learn.grpc.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ruchira.learn.grpc.user.model.AppUser;
import com.ruchira.learn.grpc.user.model.UserPrinciple;
import com.ruchira.learn.grpc.user.repository.AppUserRepository;

@Service
public class UserService implements UserDetailsService {

	private final AppUserRepository userRepo;

	public UserService(AppUserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = userRepo.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("User: %s not found", username));
		}

		return new UserPrinciple(user);
	}

	public AppUser findUser(String username) {
		return userRepo.findByUsername(username);
	}
}

