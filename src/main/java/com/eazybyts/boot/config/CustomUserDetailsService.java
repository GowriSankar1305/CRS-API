package com.eazybyts.boot.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.eazybyts.boot.entity.CrsUser;
import com.eazybyts.boot.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CrsUser appUser = userRepository.findByUserName(username);
		if(Objects.isNull(appUser))	{
			throw new UsernameNotFoundException("User not found!");
		}
		return new CustomUserDetails(appUser.getUserName() , 
				appUser.getPassword(), appUser.getUserRole().name(),appUser.getUserId());
	}

}
