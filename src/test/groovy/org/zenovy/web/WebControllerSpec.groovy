package org.zenovy.web

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

import javax.servlet.Filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import org.zenovy.SpringWebSecApplication

import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=[SpringWebSecApplication])
class WebControllerSpec extends Specification{
	
	@Shared
	MockMvc mockMvc
	
	@Autowired
	Filter springSecurityFilterChain
	
	@Autowired
	WebApplicationContext webCtx	
	
	def setup(){
		mockMvc = webAppContextSetup(webCtx)
		 	.addFilters(springSecurityFilterChain).build()		
	}
		
	def 'user jack is authenticated'(){
			when:
				def res = mockMvc.perform(formLogin("/login").user("jack").password("jack"))
						
			then:
				res.andExpect(authenticated())			
	}
	
	def 'user bruce should not be authenticated'(){
			when:
				def res = mockMvc.perform(formLogin("/login").user("bruce"))
				
			then:
				res.andExpect(unauthenticated())
	}
	
	@WithMockUser(username='joe', password= 'doe', roles= ['USER', 'ADMIN'])
	def 'user with role ADMIN should be able to access admin page'(){
		given:
			Authentication authentication = SecurityContextHolder.context.authentication
		
		expect:
			mockMvc.perform(get('/admin').with(user(authentication.principal)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_HTML.toString()+';charset=UTF-8'))
				.andExpect(view().name('admin'))
	}

	@WithMockUser(username='frank', password= 'doe', roles= ['USER'])
	def 'user with role USER should not be able to access admin page'(){
		given:
			Authentication authentication = SecurityContextHolder.context.authentication
		
		expect:
			mockMvc.perform(get('/admin').with(user(authentication.principal)))
				.andExpect(status().isForbidden())
	}
}
