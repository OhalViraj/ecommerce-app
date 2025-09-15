package com.demo.securityconfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.demo.model.UserDtls;
import com.demo.repository.UserDtlsRepo;
import com.demo.service.UserDtlsService;
import com.demo.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private UserDtlsService userDtlsService;
	
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String email = request.getParameter("username");
		UserDtls userDtls = userDtlsRepo.findByEmail(email);
		
		if(userDtls.getIsEnable())
		{
			if(userDtls.getAccountNonLocked())
			{
				if(userDtls.getFailedAttempt()<AppConstant.ATTEMPT_TIME)
				{
					userDtlsService.increseFailedAttempt(userDtls);
				}else
				{
					userDtlsService.userAccountLock(userDtls);
					exception=new LockedException("Your Account is Locked || Failed Attempt 3");
				}
				
			}else
			{
				if(userDtlsService.unlockAccountTimeExpire(userDtls))
				{
					exception=new LockedException("Your Account is UnLocked || Please Try To Login");
					
				}else
				{
					exception=new LockedException("Your Account is Locked || Please Try After SomeTime");
					
				}
				
				exception=new LockedException("Your Account is Locked");
				
			}
			
			
		}else
		{
			exception=new LockedException("Your Account is Inactive");
		}
		
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}
	
	
	
	
	
}
