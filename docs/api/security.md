[BACK](../table.md)

---

# SECURITY

Security component is extension of [spring security](https://spring.io/projects/spring-security) and the implementation 
inspired of [this repository](https://github.com/Kaway/jwt-auth).


For configure your access:

```java
com.replace.replace.configuration.security.WebSecurityConfig;
```

# Environment

- jwt.secret={512 bits key}

You can use this [generator](https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx)

# Generate password

You can use 

```java
PasswordEncoder.class
``` 

---
[BACK](../table.md)