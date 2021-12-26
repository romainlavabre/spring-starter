#!/bin/bash

BASE_DIR="$PWD"
CONFIG=$(cat "$BASE_DIR/submodule.json")
LOCK=$(cat "$BASE_DIR/submodule.lock")

for ((i = 0; i < $(jq '. | length' <<<$CONFIG); i++)); do
    NAME="$(jq -r .[$i].name <<<$CONFIG)"
    TARGET="$(jq -r .[$i].target <<<$CONFIG)"
    REPOSITORY="$(jq -r .[$i].repository <<<$CONFIG)"

    if [ "$NAME" != "$2" ]; then
        continue
    fi

    warn "Opening edition..."
    cd "${BASE_DIR}${TARGET}"
    git init --quiet
    git remote add origin "$REPOSITORY"
    git pull --quiet origin master

    CLOSE_CODE=$(uuidgen)
    RESPONSE=""

    while [ "$CLOSE_CODE" != "$RESPONSE" ]; do
        read -p "Enter this code when it's ok [ $CLOSE_CODE ] " -r RESPONSE

        if [ "$CLOSE_CODE" == "$RESPONSE" ]; then
            break
        fi
    done

    rm -Rf .git

    success "Your edition was closed"
done