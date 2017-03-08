package org.zenovy.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev")
@EnableWebSecurity
public class InMemoryWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	public InMemoryWebSecurityConfigurerAdapter() {
		// TODO Auto-generated constructor stub
	}

	public InMemoryWebSecurityConfigurerAdapter(boolean disableDefaults) {
		super(disableDefaults);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication()
			.withUser("john").password("john").roles("USER")
			.and()
			.withUser("jack").password("jack").roles("USER", "ADMIN");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()			
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/admin").hasRole("ADMIN").anyRequest().authenticated()
			.antMatchers("/welcome").hasRole("USER").anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout().permitAll();
		
		http.exceptionHandling().accessDeniedPage("/403");
	}


}
