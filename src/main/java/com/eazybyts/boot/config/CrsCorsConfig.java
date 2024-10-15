package com.eazybyts.boot.config;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CrsCorsConfig implements CorsConfigurationSource {

	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");
		//configuration.addAllowedMethod(HttpMethod.POST,HttpMethod.GET);
		configuration.addAllowedMethod("*");
		configuration.addAllowedOrigin("*");
		configuration.setMaxAge(3600L);
		return configuration;
	}

}
