package org.zenovy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping({"/", "/home"})
	public String home(){
		return "home";
	}
	
	@RequestMapping("/welcome")
	public String welcome(){
		return "welcome";
	}	
	
	@RequestMapping("/admin")
	public String admin(){
		return "admin";
	}
	
	@RequestMapping("/403")
	public String http403(){
		return "403";
	}	
}
