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
    "$1/Environment.java"
    "$1/EnvironmentImpl.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/configuration/environment"

if [ ! -d "$DIRECTORY" ]; then
    mkdir -p "$DIRECTORY"
fi

if [ -f "$DIRECTORY/Variable.java" ]; then
    read -p "File $DIRECTORY/Variable.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/Variable.java" "$DIRECTORY/Variable.java"
    fi

else
    mv "$1/Variable.java" "$DIRECTORY/Variable.java"
fi

sed -i "s|com.replace.replace.api.environment|com.${PACKAGES}.configuration.environment|" "$DIRECTORY/Variable.java"
