package com.jsoft.cocit.securityengine;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface PasswordEncoder {

	String encodePassword(String rawPass, Object salt);

	boolean isValidPassword(String encPass, String rawPass, Object salt);
}
