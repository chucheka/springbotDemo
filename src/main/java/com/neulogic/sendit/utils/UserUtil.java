package com.neulogic.sendit.utils;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neulogic.sendit.models.User;
import com.neulogic.sendit.repository.UserRepository;

@Component
public class UserUtil {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public User getCurrentUser(HttpServletRequest req) {
		String headerAuth = req.getHeader("Authorization");
		String token = headerAuth.substring(7,headerAuth.length());
		User user = userRepo.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
		return user;
	}
	
}
