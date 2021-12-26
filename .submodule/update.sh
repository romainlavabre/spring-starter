#!/bin/bash

BASE_DIR="$PWD"
CONFIG=$(cat "$BASE_DIR/submodule.json")
LOCK=$(cat "$BASE_DIR/submodule.lock")

for ((i = 0; i < $(jq '. | length' <<<$CONFIG); i++)); do
    NAME="$(jq -r .[$i].name <<<$CONFIG)"
    TARGET="$(jq -r .[$i].target <<<$CONFIG)"
    REPOSITORY="$(jq -r .[$i].repository <<<$CONFIG)"
    TAG="$(jq -r .[$i].tag <<<$CONFIG)"
    SCRIPT_EXECUTION="$(jq -r .[$i].scriptExecution <<<$CONFIG)"

    if [ "$(jq -r .$NAME <<<$LOCK)" != "null" ]; then
        if [ "$TAG" != "SNAPSHOT" ] && [ "$(jq -r .$NAME.tag <<<$LOCK)" == "$TAG" ]; then
            continue
        fi

        info "Deletion of $NAME..."
        rm -Rf "${BASE_DIR}${TARGET}"
    fi

    info "Installation of $NAME..."
    git clone --quiet "$REPOSITORY" "${BASE_DIR}${TARGET}"

    if [ "$?" != "0" ]; then
        error "This repository doesn't seem to exist"
        error "Skipping..."
        continue
    fi

    cd "${BASE_DIR}${TARGET}"
    git fetch --all

    if [ "$TAG" != "SNAPSHOT" ]; then
        git -c advice.detachedHead=false checkout "tags/$TAG"
    fi

    if [ "$?" != "0" ]; then
        source "$BASE_DIR/.submodule/logger/error.sh" "Tag doesn't seem to exist"
        source "$BASE_DIR/.submodule/logger/error.sh" "Skipping..."
        continue
    fi

    cd "$BASE_DIR"

    source "${BASE_DIR}/.submodule/install/script.sh"

    rm -Rf "${BASE_DIR}${TARGET}/.git"

    if [ "$(jq .$NAME <<<$LOCK)" != "null" ]; then
        LOCK=$(jq "del(.$NAME)" <<<$LOCK)
    fi

    LOCK=$(jq ".$NAME += {\"tag\": \"$TAG\"}" <<<$LOCK)

    source "${BASE_DIR}/.submodule/dependency.sh" "${BASE_DIR}${TARGET}"

    success "The repository $NAME has been successfully updated"
done

echo "$LOCK" > "$PWD/submodule.lock"
