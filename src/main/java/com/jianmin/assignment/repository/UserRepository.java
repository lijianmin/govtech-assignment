package com.jianmin.assignment.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jianmin.assignment.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	@Query("select u from user u where u.salary > 0 and u.salary <= 4000.00")
	public List<User> findUsersWithSalaryRange();
	
	public User findUserByName(String name);
}
