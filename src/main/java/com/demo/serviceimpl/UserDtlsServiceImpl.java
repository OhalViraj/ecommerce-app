package com.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.model.UserDtls;
import com.demo.repository.UserDtlsRepo;
import com.demo.service.UserDtlsService;

@Service
public class UserDtlsServiceImpl implements UserDtlsService{

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public UserDtls saveUserDtls(UserDtls userDtls) {
		// TODO Auto-generated method stub
		userDtls.setRole("ROLE_USER");
		String encodePassword = passwordEncoder.encode(userDtls.getPassword());
		userDtls.setPassword(encodePassword);
		UserDtls saveUser = userDtlsRepo.save(userDtls);
		return saveUser;
	}

	
	
	
}
