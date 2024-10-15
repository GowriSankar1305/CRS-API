package com.eazybyts.boot.util;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eazybyts.boot.constants.CrsConstants;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtility {
	
	@Value("${jwt.secret.key}")
	private String jwtSecret;

	public String generateToken(String userName, Long principalId,String role) throws JOSEException {
		// Create HMAC signer with shared secret
		JWSSigner signer = new MACSigner(jwtSecret);
		// Prepare JWT with claims set
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(userName).issuer("CRS")
				.expirationTime(new Date(new Date().getTime() + 3600 * 1000))
				.claim(CrsConstants.ID, principalId)
				.claim(CrsConstants.ROLE, role)
				.build();
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
		// Apply the HMAC protection
		signedJWT.sign(signer);
		// Serialize to compact form
		return signedJWT.serialize();
	}

	public boolean validateToken(String token, String userName) throws JOSEException, ParseException {
		SignedJWT signedJWT = SignedJWT.parse(token);
		JWSVerifier verifier = new MACVerifier(jwtSecret);

		boolean signatureValid = signedJWT.verify(verifier);
		boolean usernameValid = signedJWT.getJWTClaimsSet().getSubject().equals(userName);
		boolean tokenExpired = signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
		log.info("signatureValid--------> {}", signatureValid);
		log.info("usernameValid---------> {}", usernameValid);
		log.info("tokenExpired--------> {}", tokenExpired);
		return signatureValid && usernameValid && !tokenExpired;
	}

	public String extractUsername(String token) throws ParseException {
		return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
	}

	public Object extractClaim(String token, String claimName) throws ParseException {
		return SignedJWT.parse(token).getJWTClaimsSet().getClaim(claimName);
	}
}
