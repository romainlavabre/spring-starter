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
    "$1/AuthenticationFilter.java"
    "$1/AuthenticationHandler.java"
    "$1/AuthenticationHandlerImpl.java"
    "$1/JwtTokenHandler.java"
    "$1/JwtTokenImpl.java"
    "$1/Security.java"
    "$1/SecurityImpl.java"
    "$1/User.java"
    "$1/UserDetailsImpl.java"
    "$1/UserRepository.java"
    "$1/config/Role.java"
    "$1/config/SecurityConfig.java"
    "$1/controller/SecurityController.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/configuration/security"

if [ ! -d "$DIRECTORY" ]; then
    mkdir -p "$DIRECTORY"
fi

if [ -f "$DIRECTORY/Role.java" ]; then
    read -p "File $DIRECTORY/Role.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/config/Role.java" "$DIRECTORY/Role.java"
    fi

else
    mv "$1/config/Role.java" "$DIRECTORY/Role.java"
fi

if [ -f "$DIRECTORY/SecurityConfig.java" ]; then
    read -p "File $DIRECTORY/SecurityConfig.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/config/SecurityConfig.java" "$DIRECTORY/SecurityConfig.java"
    fi

else
    mv "$1/config/SecurityConfig.java" "$DIRECTORY/SecurityConfig.java"
fi

sed -i "s|com.$PACKAGES.api.security.config;|com.${PACKAGES}.configuration.security;|" "$DIRECTORY/Role.java"
sed -i "s|com.$PACKAGES.api.security.config;|com.${PACKAGES}.configuration.security;|" "$DIRECTORY/SecurityConfig.java"

CONTROLLER_DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/controller"

if [ -f "$CONTROLLER_DIRECTORY/SecurityController.java" ]; then
    read -p "File $CONTROLLER_DIRECTORY/SecurityController.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/controller/SecurityController.java" "$CONTROLLER_DIRECTORY/SecurityController.java"
    fi

else
    mv "$1/controller/SecurityController.java" "$CONTROLLER_DIRECTORY/SecurityController.java"
fi

sed -i "s|com.$PACKAGES.api.security.controller;|com.${PACKAGES}.controller;|" "$CONTROLLER_DIRECTORY/SecurityController.java"

rm -Rf "$1/config"
rm -Rf "$1/controller"

info "
Yo need to add this dependencies in your pom.xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.4.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.2</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.2</version>
    <scope>runtime</scope>
</dependency>
"

info "You need to add this variable in your Variable.java
public class Variable{
    String JWT_SECRET       = \"jwt.secret\";
    String JWT_LIFE_TIME    = \"jwt.life-time\";
}
"
