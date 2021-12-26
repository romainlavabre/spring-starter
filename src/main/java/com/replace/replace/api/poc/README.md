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


### Setters

Obviously, POC will call your setters for you, but he has conventions :

- camelCase
- If is array, you must specify a setter that take once value (addField(String value))

Warning, in array or collection (array of values or array of relation) case, it will never clear, you can only add value, that all.

If POC cannot resolve your setter, you can use

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.Setter;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @Setter( "setMyCoolName" )
    private String name;
}
```

##### Relation

If your field is relation, POC will search parameter "field_name_id".
The value must be an id of your relation. Relation's repository will be call. (findOrFail)

If your relation field is collection or array, you can specify an array or once value. It's dynamic.

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


##### More

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

### HTTP PATCH

You must placed @Patch annotation in target field.


```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.Delete;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @EntryPoint(
            patch = {
                    @Patch
            }
    )
    private String name;
}
```

It will generate

```textmate
PATCH {{url}}/{{role}}/persons/{id:[0-9]+}/name
```

### Authentication & Authorization

By default, all HTTP endpoint are generate for all available role (see in Role.class), and all endpoint will be authenticated.

If you want released endpoint, you must set this property :

```java
authenticated = false
```

It will generate 

```textmate
{{HTTP}} {{url}}/guest/persons.*
```

If you want select specific roles, you must set this property :

```java
roles = {"ROLE_1","ROLE_2"}
```

It will generate endpoints only for these roles


### Custom constraints

POC work with your field, you are encouraged to add simple constraints inside your setters.
In few cases, you need to call another service for check value.

In this case:

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.Constraint;

@PocEnabled( repository = PersonRepository.class )
@Entity
class Person {
    @Constraint( {MyCustomContraint1.class} )
    private String name;
}
```

Your service benefit of dependency injection.

Your constraint is always call <strong>before set</strong>.

### More configuration

- All HTTP annotation contains an extended executor, it's necessary if you have specific logic to execute.

<table>
    <tr>
        <th>@Post</th>
        <td>Create.class</td>
    </tr>
    <tr>
        <th>@Put</th>
        <td>Update.class</td>
    </tr>
    <tr>
        <th>@Patch</th>
        <td>Update.class</td>
    </tr>
    <tr>
        <th>@Delete</th>
        <td>Delete.class</td>
    </tr>
</table>


- All HTTP annotation contains a "route" field.

If you fill this field, you must start after plural entity. So :

```java
route = "/my/custom/route/{id:[0-9]+}"
```

produce :

```textmate
{{url}}/{{role}}/persons/my/custom/route/{id:[0-9]+}
```

### Trigger

In one more case, you need call trigger (send mail, compute value ...)

#### CreateTrigger

Create trigger act like @Post entrypoint.

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.CreateTrigger;

@PocEnabled( repository = FriendRepository.class )
@Entity
public class Friend {

    @EntryPoint(
            createTriggers = {
                    @CreateTrigger( id = TriggerIdentifier.ATTACH_FRIEND_TO_PERSON, fields = {"name"} )
            }
    )
    private long id;
}
```

#### UpdateTrigger

Update trigger act like @Put or @Patch entrypoint.

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.UpdateTrigger;

@PocEnabled( repository = FriendRepository.class )
@Entity
public class Friend {

    @EntryPoint(
            updateTriggers = {
                    @UpdateTrigger( id = TriggerIdentifier.COMPUTE_FRIEND_NAME, fields = {"name"} )
            }
    )
    private String name;
}
```


#### DeleteTrigger

Delete trigger act like @Delete entrypoint.

```java
import com.project.project.api.poc.annotation.PocEnabled;
import com.project.project.api.poc.annotation.CreateTrigger;

@PocEnabled( repository = FriendRepository.class )
@Entity
public class Friend {

    @EntryPoint(
            deleteTriggers = {
                    @DeleteTrigger( id = TriggerIdentifier.DELETE_FRIEND)
            }
    )
    private long id;
}
```

#### Call trigger

In all HTTP entry point, you can specify a trigger to call.

```java
@Post(
    fields = {"name", "phone", "age"},
    triggers = {
        @Trigger( triggerId = TriggerIdentifier.ATTACH_FRIEND_TO_PERSON, attachToField = "friends" ),
        @Trigger( triggerId = TriggerIdentifier.ATTACH_CAR_TO_PERSON, attachToField = "car" ),
        @Trigger( triggerId = TriggerIdentifier.PERSON_CATEGORY, provideMe = true )
    }
)
```

##### On relation creation

You can fill the "attachToField" field. When your entity you be created, she will be attached to specify a field (by the setter).

##### On Update or Delete trigger

You must specify a target entity, for that, you can :

- Set the "provideMe" field to TRUE, it will have the effect of pass the actual entity.
- Set the "provideField" field with field name of actual entity, it will have the effect of pass the relation entity.
- Set the "customProvider" field with a custom provider (benefit of dependency injection), it will be call.

##### More information

- All trigger can have a custom executor
- All trigger can call other trigger


### Integration tests

##### Mock

The context start in a singleton, so, for configure mock, you must request of POC to provide you the target mock. 
All mock provided is cleared between tests.


### Requirements

- Module git@github.com:romainlavabre/spring-starter-request.git
- Module git@github.com:romainlavabre/spring-starter-security.git
- Module git@github.com:romainlavabre/spring-starter-crud.git
- Module git@github.com:romainlavabre/spring-starter-json.git
- Module git@github.com:romainlavabre/spring-starter-poc-test.git

### Versions

##### 1.0.1

- ADD Test dependency

##### 1.0.0

INITIAL