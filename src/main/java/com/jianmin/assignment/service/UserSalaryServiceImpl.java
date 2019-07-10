package com.jianmin.assignment.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jianmin.assignment.exception.UsersNotFoundException;
import com.jianmin.assignment.model.User;
import com.jianmin.assignment.repository.UserRepository;
import com.jianmin.assignment.response.UserSalaryResponse;

import com.opencsv.*;

@Service
@EnableScheduling
public class UserSalaryServiceImpl implements UserSalaryService {
	
	@Autowired
	private UserRepository userRepo;
	
	Logger logger = LoggerFactory.getLogger(UserSalaryServiceImpl.class);
	
	public UserSalaryResponse getUsers() throws Exception {
		UserSalaryResponse response = new UserSalaryResponse();
		
		List<User> users = userRepo.findUsersWithSalaryRange();
		
		if(users == null) {
			logger.error("Users not found");
			throw new UsersNotFoundException("Users not found");
		}
		
		response.setResults(users);
		
		return response;
	}
	
	@Scheduled(fixedRate=60000)
	private void readAndInsertCSVIntoDB() {
		try {
			
			// Assumption made here is the file structure is always correct for the input program
			
			String filename = "file.csv";
			CSVReader csvReader = new CSVReader(new FileReader(filename));
			
			String[] line;
			
			//Skip header
			csvReader.readNext();
			
		    while ((line = csvReader.readNext()) != null) {
		    	
		    	User user = userRepo.findUserByName(line[0]);
		    	
		    	// if user with that name is not found
		    	if (user == null) {
		    		
		    		userRepo.save(new User(line[0], new Float(line[1])));
		    		logger.info("Inserted record " + line[0] + " " + line[1] + " into database");
		    	
		    	// if user with that name is found but salary is different
		    	} else if (user.getSalary() != new Float(line[1])){
		    		
		    		user.setSalary(new Float(line[1]));
		    		userRepo.save(user);
		    		logger.info("Updated record for name " + line[0] + " with new salary " + line[1] + " in database");
		    	
		    	} else {
		    		
		    		logger.info("No change in record. Skipped");
		    	}
		    	
		    }
		    
		    //reader.close();
		    csvReader.close();
			
		} catch (Exception e) {
			logger.error("File not found");
		}
	}
}
