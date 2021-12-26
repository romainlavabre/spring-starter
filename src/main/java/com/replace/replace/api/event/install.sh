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
    "$1/EventDispatcher.java"
    "$1/EventDispatcherImpl.java"
    "$1/EventRepository.java"
    "$1/EventRegisterImpl.java"
    "$1/EventSubscriber.java"
    "$1/exception/EventSubscriberException.java"
    "$1/exception/InvalidEventCredentialsException.java"
    "$1/exception/NotRegisteredEventException.java"
    "$1/annotation/Subscribers.java"
    "$1/annotation/UnitEvent.java"
    "$1/config/Event.java"
    "$1/config/EventConfig.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/configuration/event"

if [ ! -d "$DIRECTORY" ]; then
    mkdir -p "$DIRECTORY"
fi

if [ -f "$DIRECTORY/Event.java" ]; then
    read -p "File $DIRECTORY/Event.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/config/Event.java" "$DIRECTORY/Event.java"
    fi

else
    mv "$1/config/Event.java" "$DIRECTORY/Event.java"
fi

if [ -f "$DIRECTORY/EventConfig.java" ]; then
    read -p "File $DIRECTORY/EventConfig.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/config/EventConfig.java" "$DIRECTORY/EventConfig.java"
    fi

else
    mv "$1/config/EventConfig.java" "$DIRECTORY/EventConfig.java"
fi

sed -i "s|com.$PACKAGES.api.event.config;|com.${PACKAGES}.configuration.event;|" "$DIRECTORY/Event.java"
sed -i "s|com.$PACKAGES.api.event.config;|com.${PACKAGES}.configuration.event;|" "$DIRECTORY/EventConfig.java"

rm -Rf "$1/config"
