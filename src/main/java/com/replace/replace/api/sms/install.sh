#!/bin/bash

BASE_DIR="$1"
PACKAGE_PARSER=${BASE_DIR/"$2/src/main/java/com/"/""}
PACKAGES=""

IFS='/' read -ra ARRAY <<<"$PACKAGE_PARSER"
I=0

for PART in "${ARRAY[@]}"; do
    if [ "$I" == "0" ]; then
        PACKAGES="$PART"
    fi

    if [ "$I" == "1" ]; then
        PACKAGES="${PACKAGES}.${PART}"
    fi

    I=$((I + 1))
done

CLASSES=(
    "$1/SmsSender.java"
    "$1/Twilio.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

info "You need import this dependency
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>8.9.0</version>
</dependency>"

info "You need add this variables
public interface Variable {
    String SMS_TWILIO_SID       = \"sms.twilio.sid\";
    String SMS_PRIVATE_KEY      = \"sms.twilio.auth-token\";
    String SMS_FROM             = \"sms.from\";
}"
