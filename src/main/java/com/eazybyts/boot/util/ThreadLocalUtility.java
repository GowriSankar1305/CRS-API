package com.eazybyts.boot.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ThreadLocalUtility {
	
	private static final ThreadLocal<Map<String, Object>> userSession = new ThreadLocal<>();

	public static void set(Map<String, Object> dataMap) {
		userSession.set(dataMap);
	}

	public static Map<String, Object> get() {
		return userSession.get();
	}

	public static void clear() {
		userSession.remove();
	}
}
