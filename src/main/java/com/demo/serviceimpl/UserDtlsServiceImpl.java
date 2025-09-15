package com.demo.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.model.UserDtls;
import com.demo.repository.UserDtlsRepo;
import com.demo.service.UserDtlsService;
import com.demo.util.AppConstant;

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
		userDtls.setAccountNonLocked(true);
		userDtls.setFailedAttempt(0);
		
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

	@Override
	public void increseFailedAttempt(UserDtls user) {
		// TODO Auto-generated method stub
		int attempt=user.getFailedAttempt()+1;
		user.setFailedAttempt(attempt);
		userDtlsRepo.save(user);
		
	}

	@Override
	public void userAccountLock(UserDtls user) {
		// TODO Auto-generated method stub
		
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userDtlsRepo.save(user);
	}

	@Override
	public Boolean unlockAccountTimeExpire(UserDtls user) {
		// TODO Auto-generated method stub
		long lockTime = user.getLockTime().getTime();
		long unlockTime=lockTime+AppConstant.UNLOCK_DURATION_TIME;
		long currentTime = System.currentTimeMillis();
		
		if(unlockTime<currentTime)
		{
			user.setAccountNonLocked(true);
			user.setFailedAttempt(0);
			user.setLockTime(null);
			userDtlsRepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public void resetAttempt(int userId) {
		// TODO Auto-generated method stub
		
	}

	 
	
	
}
