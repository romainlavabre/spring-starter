[BACK](../table.md)

---

# Environment

The environment permit accessing to properties.

```java
com.replace.replace.api.environment.Environment;
```

You can create your variable here:

```java
public interface EnvironmentVariable {
    String SERVER_PORT          = "server.port";
    String MAIL_DOMAIN          = "mail.mailgun.domain";
    String MAIL_FROM            = "mail.from";
    String MAIL_PRIVATE_KEY     = "mail.mailgun.private-key";
    String SMS_TWILIO_SID       = "sms.twilio.sid";
    String SMS_PRIVATE_KEY      = "sms.twilio.auth-token";
    String SMS_FROM             = "sms.from";
    String DOCUMENT_PUBLIC_KEY  = "document.aws.public-key";
    String DOCUMENT_PRIVATE_KEY = "document.aws.private-key";
    String DOCUMENT_AWS_REGION  = "document.aws.region";
    String DOCUMENT_AWS_BUCKET  = "document.aws.bucket";
    String DOCUMENT_SERVER_URL  = "document.aws.url";

}
```