package com.eazybyts.boot.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 793418691644248936L;
	private final String userName;
	private final String password;
	private final String role;
	private final Long userId;
	
	public CustomUserDetails(String userName,String password,String role,Long userId)	{
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.userId = userId;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}
	
	public Long getUserId()	{
		return userId;
	}

}
