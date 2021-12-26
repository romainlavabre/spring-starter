# DocumentStorage

The document storage API allows of store file on external server.

```java
com.replace.replace.api.document.DocumentStorageHandler;
```

### Configure

You must add the constant values in application-{profile}.properties
```java
public interface Variable {
    String DOCUMENT_PUBLIC_KEY  = "document.aws.public-key";
    String DOCUMENT_PRIVATE_KEY = "document.aws.private-key";
    String DOCUMENT_AWS_REGION  = "document.aws.region";
    String DOCUMENT_AWS_BUCKET  = "document.aws.bucket";
    String DOCUMENT_SERVER_URL  = "document.aws.url";
}
```

### Requirements

- Module : git@github.com:romainlavabre/spring-starter-environment.git

Dependency

```xml
<dependencies>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
    </dependency>
</dependencies>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>bom</artifactId>
            <version>2.15.15</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Versions

##### 1.0.0

- INITIAL
