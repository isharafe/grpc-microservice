package com.ruchira.learn.grpc.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ruchira.learn.grpc.user.model.AppUser;
import com.ruchira.learn.grpc.user.repository.AppUserRepository;

@Component
public class BootstrapUsers implements CommandLineRunner {

	@Autowired
	private AppUserRepository userRepo;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		AppUser test = userRepo.findByUsername("test");
		if (test == null) {
			test = new AppUser();
			test.setUsername("test");
			test.setPassword(encoder.encode("test"));
			test.setName("Saman Doe");
			userRepo.save(test);
		}
	}
}
