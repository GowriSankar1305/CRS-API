package com.eazybyts.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.eazybyts.boot.enums.UserRoleEnum;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JWTFilter jwtFilter;
	@Autowired
	private UserDetailsService userDetailsService;
	@Value("${crs.open.routes}")
	private String[] openRoutes;
	@Autowired
	private CrsCorsConfig crsCorsConfig;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> {
			authorizeRequests.requestMatchers(openRoutes).permitAll();
			authorizeRequests.requestMatchers("/crm/admin/**").hasAuthority(UserRoleEnum.ADMIN.name());
			authorizeRequests.requestMatchers("/crm/user/**").hasAnyAuthority(UserRoleEnum.ADMIN.name(),
					UserRoleEnum.CUSTOMER.name());
			authorizeRequests.requestMatchers("/**").authenticated();
		});
		http.cors(cors -> cors.configurationSource(crsCorsConfig));
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sessionMgmt -> sessionMgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authenticationProvider(authProviderBean());
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authProviderBean() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return daoAuthenticationProvider;
	}
}
