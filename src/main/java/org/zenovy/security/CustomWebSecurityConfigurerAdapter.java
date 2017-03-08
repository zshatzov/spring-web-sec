package org.zenovy.security;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Profile("prod")
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOG = LoggerFactory.getLogger(
				CustomWebSecurityConfigurerAdapter.class);
	
	private JdbcTemplate jdbcTemplate;
			
	public CustomWebSecurityConfigurerAdapter() {
	}

	public CustomWebSecurityConfigurerAdapter(boolean disableDefaults) {
		super(disableDefaults);
	}
	 	
	@Bean
	public CsrfTokenRepository csrfRepo(){
		HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
		repo.setParameterName("_csrf");
		repo.setHeaderName("X-CSRF-TOKEN");
		return repo;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		LOG.info("Configure jdbc authentication...");
		auth.jdbcAuthentication()
			.dataSource(jdbcTemplate.getDataSource())
		    .usersByUsernameQuery("select username, password, enabled from users where username = ?")
		    .authoritiesByUsernameQuery("select username, role from user_roles where username = ?"); 
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);	
		web.debug(true);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()			
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/admin").hasRole("ADMIN").anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout().permitAll()
			.and()
			.csrf().csrfTokenRepository(csrfRepo());
		
		
		http.exceptionHandling().accessDeniedPage("/403");
	}

	@Inject
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
