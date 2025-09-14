package com.demo.serviceimpl;

import java.util.List;
import java.util.Optional;

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
		userDtls.setIsEnable(true);
		String encodePassword = passwordEncoder.encode(userDtls.getPassword());
		userDtls.setPassword(encodePassword);
		UserDtls saveUser = userDtlsRepo.save(userDtls);
		return saveUser;
	}

	@Override
	public UserDtls getUserByEmail(String email) {
		// TODO Auto-generated method stub
		UserDtls  userbyEmail= userDtlsRepo.findByEmail(email);
		return userbyEmail;
	}

	@Override
	public List<UserDtls> getUsers(String role) {
		// TODO Auto-generated method stub
		List<UserDtls> findbyRole = userDtlsRepo.findByRole(role);
		return findbyRole;
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		// TODO Auto-generated method stub
		
		Optional<UserDtls> findByuser = userDtlsRepo.findById(id);
		
		if(findByuser.isPresent())
		{
			UserDtls userDtls = findByuser.get();
			userDtls.setIsEnable(status);
			userDtlsRepo.save(userDtls);
			
			return true;
		}
		
		return false;
		
	}

	 
	
	
}
