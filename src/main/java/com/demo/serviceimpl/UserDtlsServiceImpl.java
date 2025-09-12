package com.demo.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.UserDtls;
import com.demo.repository.UserDtlsRepo;
import com.demo.service.UserDtlsService;

@Service
public class UserDtlsServiceImpl implements UserDtlsService{

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Override
	public UserDtls saveUserDtls(UserDtls userDtls) {
		// TODO Auto-generated method stub
		UserDtls saveUser = userDtlsRepo.save(userDtls);
		return saveUser;
	}

	
	
	
}
