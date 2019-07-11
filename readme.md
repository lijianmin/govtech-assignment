Govtech Assignment
==================
Created by: Jianmin (li.jianmin {at} outlook.com)

Apologies! This assignment could have been fully completed with unit test cases.

### About
This microservice application exposes one API /users and also record/update a list of entries from file.csv.

### Project Source Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── jianmin
│   │   │           └── assignment
│   │   │               ├── AssignmentApplication.java
│   │   │               ├── controller
│   │   │               │   └── UserSalaryController.java
│   │   │               ├── exception
│   │   │               │   └── UsersNotFoundException.java
│   │   │               ├── model
│   │   │               │   └── User.java
│   │   │               ├── repository
│   │   │               │   └── UserRepository.java
│   │   │               ├── response
│   │   │               │   └── UserSalaryResponse.java
│   │   │               └── service
│   │   │                   ├── UserSalaryService.java
│   │   │                   └── UserSalaryServiceImpl.java
```

### SpringBoot Maven POM.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.jianmin</groupId>
	<artifactId>assignment</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>assignment</name>
	<description>Assignment project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
	      	<groupId>mysql</groupId>
	      	<artifactId>mysql-connector-java</artifactId>
	      	<scope>runtime</scope>
	    </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
		    <groupId>com.opencsv</groupId>
		    <artifactId>opencsv</artifactId>
		    <version>4.5</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

### Application.properties
This has been intentionally left out as it contains sensitive information such as credentials to the database. In order to run this application, please create the file **application.properties** in the folder **src/main/resources**

Provide the following properties:
```
server.port=<the running port you want (optional)>
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://<hostname of database:port>/<schema name>
spring.datasource.username=<database username>
spring.datasource.password=<database user password>
```

### Unit Test Cases
COMING SOON - Got caught up with work and family hence not able to complete the test cases on time.

### Reading of file.csv
This web application reads a CSV file named file.csv from the running directory every 1 minute. This file contains columns **name** and **salary**. We assume the file structure is always correct for this assignment. The scheduled job will update the salary belonging to the name if the amount is different. This approach is used to simulate live update of the database by reading an updated copy of the file dropped into the directory after a file transfer/output operation from another system.

**Sample structure**:
```
name,salary
John,2500.05
Mary Posa, 4000.00
Mike,4001.00
Jianmin,4721.00
Stephanie,3000.00
Clement,2100.00
```

### Source Code for Reading and Recording Entries From File.csv 
```java
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
```

### Users API
This one and only API exposed by this web application returns a list of users whose salary is within the range of 0 to less than or equal (<=) 4000

**URL**: /users
**HTTP Operation Allowed**: GET
**HTTP Code Returned**: 200 OK 
**Content-Type**: application/json

**Sample Response - Users Found**:
```
{
    "results": [
        {
            "name": "John",
            "salary": 2500.05
        },
        {
            "name": "Mary Posa",
            "salary": 4000
        },
        {
            "name": "Stephanie",
            "salary": 3000
        },
        {
            "name": "Clement",
            "salary": 2100
        }
    ]
}
```

**Sample Response - Users Not Found**:
```
{
    "results": []
}
```




