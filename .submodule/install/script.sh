#!/bin/bash

SCRIPT_PATH="${BASE_DIR}${TARGET}/install.sh"

function sourceLogger() {
    sed -i "s|#!/bin/bash|#!/bin/bash\nsource ${BASE_DIR}/.submodule/logger/logger.sh|" "$1"
}

if [ -f "$SCRIPT_PATH" ]; then

    chmod +x "$SCRIPT_PATH"
    BASE_DEPENDENCY="${SCRIPT_PATH/install.sh/}"

    if [ "$SCRIPT_EXECUTION" == 'manual' ]; then
        warn "Do you want execute pre-script of $REPOSITORY [Y/n]"
        read -r RESPONSE

        if [ "$RESPONSE" == "Y" ] || [ "$RESPONSE" == "y" ]; then
            cd "$BASE_DEPENDENCY"
            sourceLogger "./install.sh"
            "./install.sh" "${BASE_DIR}${TARGET}" "$BASE_DIR"
            cd "$BASE_DIR"
        fi
    fi

    if [ "$SCRIPT_EXECUTION" == 'auto' ]; then
        cd "$BASE_DEPENDENCY"
        sourceLogger "./install.sh"
        "./install.sh" "${BASE_DIR}${TARGET}" "$BASE_DIR"
        cd "$BASE_DIR"
    fi

    rm "$SCRIPT_PATH"

fi