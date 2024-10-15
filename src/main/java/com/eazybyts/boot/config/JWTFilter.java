package com.eazybyts.boot.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eazybyts.boot.constants.CrsConstants;
import com.eazybyts.boot.dto.ApiResponseDto;
import com.eazybyts.boot.util.JwtUtility;
import com.eazybyts.boot.util.ThreadLocalUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

	@Value("${crs.open.routes}")
	private List<String> openRoutes;
	@Autowired
	private JwtUtility jwtUtility;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("==== intercepting the request to application ====");
		log.info("request uri--> {}", request.getRequestURI());
		if (!openRoutes.contains(request.getRequestURI())) {
			String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
				response.setStatus(400);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.print(
						mapper.writeValueAsString(new ApiResponseDto(List.of("Token is missing in the header!"))));
				printWriter.flush();
				return;
			}
			bearerToken = bearerToken.split(" ")[1];
			String userName = null;
			Object currentPrincipal = null;
			Object userRole = null;
			try {
				log.info("---- extracting username and princpal from token ------");
				userName = jwtUtility.extractUsername(bearerToken);
				currentPrincipal = jwtUtility.extractClaim(bearerToken, CrsConstants.ID);
				userRole = jwtUtility.extractClaim(bearerToken, CrsConstants.ROLE);
			} catch (ParseException e) {
				log.error("----- error occured while extracting username and princpal from token -----");
				response.setStatus(400);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.print(
						mapper.writeValueAsString(new ApiResponseDto(List.of("Token is either invalid or expired!"))));
				printWriter.flush();
				return;
			}
			if (Objects.nonNull(userName) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				try {
					log.info(" --------- validationg token ------------");
					if (jwtUtility.validateToken(bearerToken, userDetails.getUsername())) {
						SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						securityContext.setAuthentication(authenticationToken);
						SecurityContextHolder.setContext(securityContext);
						Map<String, Object> requestMap = new HashMap<>();
						log.info("current logged in principla----> {}", currentPrincipal);
						requestMap.put(CrsConstants.ID, currentPrincipal);
						requestMap.put(CrsConstants.ROLE, userRole);
						ThreadLocalUtility.set(requestMap);
						filterChain.doFilter(request, response);
					} else {
						response.setStatus(400);
						response.setContentType(MediaType.APPLICATION_JSON_VALUE);
						response.setCharacterEncoding("UTF-8");
						PrintWriter printWriter = response.getWriter();
						printWriter.print(mapper.writeValueAsString(
								new ApiResponseDto(List.of("Token is either invalid or expired!"))));
						printWriter.flush();
						return;
					}
				} catch (Exception e) {
					log.error("exception while token validating----> {}", e);
					response.setStatus(500);
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setCharacterEncoding("UTF-8");
					PrintWriter printWriter = response.getWriter();
					printWriter.print(mapper
							.writeValueAsString(new ApiResponseDto(List.of("Unable to authenticate the request!"))));
					printWriter.flush();
					return;
				}
			} else {
				response.setStatus(500);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("UTF-8");
				PrintWriter printWriter = response.getWriter();
				printWriter.print(
						mapper.writeValueAsString(new ApiResponseDto(List.of("Unable to validate the request!"))));
				printWriter.flush();
				return;
			}
		} else {
			log.info(" --------- it is an open request ------");
			filterChain.doFilter(request, response);
		}
	}
}
