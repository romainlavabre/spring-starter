[BACK](../table.md)

---

# POC

This framework aims to save you time in your work. Please consider the warning below.


Warning, this framework is intended for proof of concept and will not give your software a beautiful architecture.
Thus, when your project will be stabilized, it will eventually be necessary to consider a rewrite.

### Features

- Dynamic routing
- Dynamic crud & triggers
- Inversion of control
- Authentication & Authorization support
- Integration testing framework

### Subscribe to POC framework

Create your entity

```java
import com.project.project.api.poc.annotation.PocEnabled;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    ...
}
``` 

You must specify her repository and you can specify the prefix of entity (to plural) for routing

```java
@PocEnabled( repository = PersonRepository.class, suffixPlural = "persons" )
```

By default, POC add an "s", but with few words, it can be a problem (address).

### Entry point

All configuration is placed in the @Entrypoint annotation.

### Request parameters name

By default, the json payload will be parsed with this convention :

```textmate
{{object_name}}_{{field_name}} = value
```

In snackcase format.

So  

```json
{
    "person": {
        "name": "Paul",
        "weight": 80.0
    }   
}
```

Will be produce


```textmate
person_name=Paul
person_weight=80.0
```

If this convention does not suit you, you can explicitly fill like that :

```java
import com.project.project.api.poc.annotation.RequestParameter;

class Person {
    
    @RequestParameter( name = "personName" )
    private String name;

    @RequestParameter( name = "personWeight" )
    private double weight;
}
``` 

### HTTP GET

By convention, you add this annotation to the id field.

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.GetOne;
import com.project.project.api.poc.annotation.GetAll;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
                getOne = @GetOne( enabled = true ),
                getAll = @GetAll( enabled = true)
    )
    private long id;
}
```

This configuration will produce

```textmate
GET {{url}}/{{role}}/persons/{id:[0-9]+}
GET {{url}}/{{role}}/persons
```

The response payload will be build with entity that return by the repository and encoded with the endpoint role.
For sample, if role is ROLE_ADMIN, the entity with encoded with group ADMIN.


#### More

In few cases, you want return entity by owner relation, you can specify 


```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.GetOneBy;
import com.project.project.api.poc.annotation.GetAllBy;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
                getOneBy = {@GetOneBy( entity = Friend.class ), @GetOneBy( entity = Car.class )},
                getAllBy = {@GetAllBy( entity = Other.class)}
    )
    private long id;
}
```

And in your repository, POC will search this method

She should throw a 404 Exception if not found

```java
Person findOrFailByFriend( Friend friend );
```

And for @GetAllBy

```java
List< Friend > findByPerson( Person person );
```

If your method has another name, you must specify it 

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.GetOneBy;
import com.project.project.api.poc.annotation.GetAllBy;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
                getOneBy = {@GetOneBy( entity = Friend.class, method = "anotherName" )},
                getAllBy = {@GetAllBy( entity = Other.class, method = "anotherName" )}
    )
    private long id;
}
```

### HTTP POST

By convention, you add this annotation to the id field.

Sample :

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.Post;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
            post = {
                    @Post(
                            fields = {"name", "phone", "age"}
                    )
            }
    )
    private long id;
}
```

- You can specify more than one @Post entry point 
- You must explicitly fill fields to set

It will generate

```textmate
POST {{url}}/{{role}}/persons
```

### HTTP PUT

By convention, you add this annotation to the id field.

It's identical of HTTP POST

It will generate

```textmate
PUT {{url}}/{{role}}/persons
```

### HTTP DELETE

By convention, you add this annotation to the id field.

Very simple

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.Delete;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
            delete = {
                    @Delete
            }
    )
    private long id;
}
```

It will generate

```textmate
DELETE {{url}}/{{role}}/persons/{id:[0-9]+}
```



--- 

[BACK](../table.md)