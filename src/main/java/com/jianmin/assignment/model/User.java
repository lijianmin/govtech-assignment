package com.jianmin.assignment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name="user")
@JsonIgnoreProperties(value= { "id" })
public class User {
	
	// Primary Key
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private long id;
	private String name;
	private float salary;
	
	protected User() {
		
	}
	
	public User (String name, float salary) {
		this.name = name;
		this.salary = salary;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	
}
