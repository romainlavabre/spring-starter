#!/bin/bash

BASE_DIR="$1"
PACKAGE_PARSER=${BASE_DIR/"$2/src/test/java/com/"/""}
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
    "$1/client/PocAssert.java"
    "$1/client/PocClient.java"
    "$1/client/PocMock.java"
    "$1/exception/HttpNotFoundException.java"
    "$1/loader/Context.java"
    "$1/loader/mock/EntityHandler.java"
    "$1/loader/mock/MockApplicationContext.java"
    "$1/loader/mock/MockRequest.java"
    "$1/loader/mock/MockRequestMappingHandlerMapping.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done
