package com.demo.service;

import java.util.List;

import com.demo.model.UserDtls;

public interface UserDtlsService {

	public UserDtls saveUserDtls(UserDtls userDtls);
	
	public UserDtls getUserByEmail(String email);
	
	public List<UserDtls> getUsers(String role);

	public Boolean updateAccountStatus(Integer id, Boolean status);

	public void increseFailedAttempt(UserDtls user);
	
	public void userAccountLock(UserDtls user);
	
	public Boolean unlockAccountTimeExpire(UserDtls user);
	
	public void resetAttempt(int userId);
	
	
}
