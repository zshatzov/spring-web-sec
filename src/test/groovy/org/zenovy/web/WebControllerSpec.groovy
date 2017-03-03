package org.zenovy.web

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
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
	WebApplicationContext webCtx	
	
	def setup(){
		mockMvc = webAppContextSetup(webCtx).build()
	}
		
	@WithMockUser(username='Jack', roles =['USER', 'ADMIN'])
	def 'make sure admin user can navigate to admin page'(){
		expect:
			mockMvc.perform(get('/admin'))
				.andExpect(status().isOk())			
				.andExpect(view().name('admin'))			
	} 
}
