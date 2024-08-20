package com.web.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.web.demo.TokenAuthenticationFilter;
import com.web.demo.service.TokenProvider;
import com.web.demo.service.TokenService;
import com.web.demo.service.UtilService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private TokenProvider tokenProvider;
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/user/**") ).permitAll() )
			.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/dist/**") ).permitAll() )
			.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/plugins/**") ).permitAll() )
			.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/h2-console/**") ).permitAll() )
			.authorizeHttpRequests( ( a ) -> a.requestMatchers
				( new AntPathRequestMatcher("/**") ).authenticated() )		
			
			.csrf( (b) -> 
			b.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
			.headers( (c)-> c.addHeaderWriter(new XFrameOptionsHeaderWriter(
					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) )
			
			.formLogin((formLogin) -> formLogin
	                .loginPage("/user/login")
	                .defaultSuccessUrl("/") 
					)	
			.logout((logout) -> logout
	                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	                .logoutSuccessUrl("/user/login")
	                .invalidateHttpSession(true))	
			// TODO #0819 : 필터 기능 추가(요청이 처리되기전에 작동하는 필터)
			.addFilterBefore(new TokenAuthenticationFilter(tokenProvider), 
					UsernamePasswordAuthenticationFilter.class);
		
		;
		return http.build();
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
















					