package com.jianmin.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jianmin.assignment.response.UserSalaryResponse;
import com.jianmin.assignment.service.UserSalaryService;

@RestController
public class UserSalaryController {
	
	@Autowired
	private UserSalaryService userSalarySvc;
	
	Logger logger = LoggerFactory.getLogger(UserSalaryController.class);
	
	@GetMapping("/users")
	public UserSalaryResponse getUsers() throws Exception {
		logger.info("GET /users invoked");
		return userSalarySvc.getUsers();
	}
}
