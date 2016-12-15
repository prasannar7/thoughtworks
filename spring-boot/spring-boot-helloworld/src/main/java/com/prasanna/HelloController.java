package com.prasanna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prasanna.props.HelloProperties;

@RestController
public class HelloController {
	
	@Autowired
	HelloProperties helloProperties;
	
	@RequestMapping("/hello")
	public String hello(@RequestParam String name){
		return helloProperties.getGreeting()+name;
	}

}
