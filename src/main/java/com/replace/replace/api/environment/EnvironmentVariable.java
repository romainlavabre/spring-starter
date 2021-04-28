package com.replace.replace.api.environment;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface EnvironmentVariable {
    String SERVER_PORT          = "server.port";
    String MAIL_DOMAIN          = "mail.mailgun.domain";
    String MAIL_FROM            = "mail.from";
    String MAIL_PRIVATE_KEY     = "mail.mailgun.private-key";
    String MAIL_TEMPLATE_PATH   = "mail.template.path";
    String SMS_TWILIO_SID       = "sms.twilio.sid";
    String SMS_PRIVATE_KEY      = "sms.twilio.auth-token";
    String SMS_FROM             = "sms.from";
    String DOCUMENT_PUBLIC_KEY  = "document.aws.public-key";
    String DOCUMENT_PRIVATE_KEY = "document.aws.private-key";
    String DOCUMENT_AWS_REGION  = "document.aws.region";
    String DOCUMENT_AWS_BUCKET  = "document.aws.bucket";
    String DOCUMENT_SERVER_URL  = "document.aws.url";
    String JWT_SECRET           = "jwt.secret";
    String JWT_LIFE_TIME        = "jwt.life-time";
}
