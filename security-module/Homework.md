# Security Application Task

## Necessary Tools

Java Development Kit 11+

Apache Maven 3.6.0+

Git 2.24+

Spring 5

Spring Boot 2

##Task

(20 points)

##### 1.	Create Spring Boot MVC project.
##### 2.	Create REST endpoint "GET /info" that provide random stats (ex: "MVC application"). Test this endpoint.
##### 3.	Add Spring Security module to your project and configure it for authenticated access to all resources. Use email/password combination for it.

(20 points)

##### 4.	Use a non-embedded DB to store users.
##### 5.	Use salt and hashing to store user passwords.
##### 6.	Create additional REST endpoint "GET /about" and configure non-authenticated access to it.

(40 points)
##### 7.	Create one more REST endpoint "GET /admin".
##### 8.	Now you need to add authorised access to "GET /info" and "GET /admin", add "VIEW_INFO", "VIEW_ADMIN" permissions for it. Create 3 users with different combination of permissions.
##### 9.    Create new Login/Logout pages and configure Spring Security to use new Login/Logout.
##### 10.   Add Brute Force protector. BLock user email for 5 minute on 3 unsuccessful login.
##### 11.   Create an endpoint to show blocked users

(20 points)

##### 12.	Implement a new Spring Boot MVC application called "Secret providers". Application should provide page with text form. After sending a secret, application must generate uniq link address, to provide one-time access to secret information. After this information must be removed from application.
##### 13.   User (sender and recipient) must be authorized and have "STANDARD" permission.
##### 14.   Use docker containers to implement solution.

Create pull requests to your mentor with implemented application (-s).

## References

- [Prevent Brute Force](https://www.baeldung.com/spring-security-block-brute-force-authentication-attempts)
- [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Build a REST API with Spring](https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)
- [Password Hashing](https://www.baeldung.com/java-password-hashing)
