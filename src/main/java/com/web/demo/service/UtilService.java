package com.web.demo.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

//FIXME #REFACT: 유틸리티, 서비스 전반에 필요한 유틸리티, 중복기능등을 제공하는 서비스
@Service
public class UtilService {
	public void setCookie(String key, String value, int expiration, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(expiration);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	public int toSecoundOfDay(int day) {
		return 60*60*24*day;
	}
}
