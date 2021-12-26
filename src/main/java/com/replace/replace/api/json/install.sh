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
    "$1/Encoder.java"
    "$1/put/Put.java"
    "$1/overwritter/Overwrite.java"
    "$1/formatter/Formatter.java"
    "$1/annotation/Group.java"
    "$1/annotation/Json.java"
    "$1/annotation/JsonPut.java"
    "$1/annotation/Row.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/configuration/json"

if [ ! -d "$DIRECTORY" ]; then
    mkdir -p "$DIRECTORY"
fi

if [ -f "$DIRECTORY/GroupType.java" ]; then
    read -p "File $DIRECTORY/GroupType.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/GroupType.java" "$DIRECTORY/GroupType.java"
    fi

else
    mv "$1/GroupType.java" "$DIRECTORY/GroupType.java"
fi

sed -i "s|com.replace.replace.api.json|com.${PACKAGES}.configuration.json|" "$DIRECTORY/GroupType.java"
