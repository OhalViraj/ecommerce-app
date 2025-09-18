package com.demo.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private  JavaMailSender mailSender;
	
	
	
	public  Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException
	{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom("jeffhbk420@gmail.com","Shooping Cart");
		helper.setTo(reciepentEmail);
		
		String content="<p> Hello ,</p>"+ "<p>You Have Requested  to Reset Your Password. </p> "
		+"<p>Click The Link Below To Change Your Password : </p>" + "<p> <a href=\""+url+
		 "\"> Change my password </a></p>";
		
		helper.setSubject("Password Reset");
		helper.setText(content,true);
		mailSender.send(message);
		
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(),"");
		 
	}

}
