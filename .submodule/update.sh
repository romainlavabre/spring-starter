#!/bin/bash

BASE_DIR="$PWD"
CONFIG=$(cat "$BASE_DIR/submodule.json")
LOCK=$(cat "$BASE_DIR/submodule.lock")

function getLatestMatchVersion() {
    git clone --quiet $2 "$BASE_DIR/.submodule-trash" > /dev/null 2>&1
    cd "$BASE_DIR/.submodule-trash"
    git fetch --all > /dev/null 2>&1

    if [ "$1" == "SNAPSHOT" ]; then
        echo "SNAPSHOT"
    else
        git describe --match "$1" --abbrev=0 HEAD
    fi

    rm -Rf "$BASE_DIR/.submodule-trash"
    cd "$BASE_DIR"
}

for ((i = 0; i < $(jq '. | length' <<<$CONFIG); i++)); do
    NAME="$(jq -r .[$i].name <<<$CONFIG)"
    TARGET="$(jq -r .[$i].target <<<$CONFIG)"
    REPOSITORY="$(jq -r .[$i].repository <<<$CONFIG)"
    TAG="$(jq -r .[$i].tag <<<$CONFIG)"
    SCRIPT_EXECUTION="$(jq -r .[$i].scriptExecution <<<$CONFIG)"
    SELECTED_VERSION=$(getLatestMatchVersion "$TAG" "$REPOSITORY")

    if [ "$(jq -r .$NAME <<<$LOCK)" != "null" ]; then
        if [ "$TAG" != "SNAPSHOT" ] && [ "$(jq -r .$NAME.tag <<<$LOCK)" == "$SELECTED_VERSION" ]; then
            continue
        fi

        info "Deletion of $NAME..."
        rm -Rf "${BASE_DIR}${TARGET}"
    fi

    info "Installation of $NAME..."
    git clone --quiet "$REPOSITORY" "${BASE_DIR}${TARGET}" > /dev/null

    if [ "$?" != "0" ]; then
        error "This repository doesn't seem to exist"
        error "Skipping..."
        continue
    fi

    cd "${BASE_DIR}${TARGET}"
    git fetch --all -q > /dev/null

    if [ "$TAG" != "SNAPSHOT" ]; then
        info "Version : $SELECTED_VERSION"
        git -c advice.detachedHead=false checkout "tags/$SELECTED_VERSION" -q > /dev/null
    else
        info "Version : latest commit"
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

    LOCK=$(jq ".$NAME += {\"tag\": \"$SELECTED_VERSION\"}" <<<$LOCK)

    source "${BASE_DIR}/.submodule/dependency.sh" "${BASE_DIR}${TARGET}"

    success "The repository $NAME has been successfully updated"
done

echo "$LOCK" >"$PWD/submodule.lock"
