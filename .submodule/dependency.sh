#!/bin/bash

ANALYZE_PATH="$1/dependency.json"

if [ -f "$ANALYZE_PATH" ]; then

    DEPENDENCY_CONTENT=$(cat "$ANALYZE_PATH")

    for ((c = 0; c < $(jq '. | length' <<<$DEPENDENCY_CONTENT); c++)); do
        DEPENDENCY="$(jq -r .[$c].repository <<<$DEPENDENCY_CONTENT)"

        FOUND="0"

        for ((j = 0; j < $(jq '. | length' <<<$CONFIG); j++)); do
            if [ "$(jq -r .[$j].repository <<<$CONFIG)" != "$DEPENDENCY" ]; then
                continue
            fi

            if [ "$(jq -r .[$j].tag <<<$CONFIG)" != "$(jq -r .[$c].tag <<<$DEPENDENCY_CONTENT)" ]; then
                source "$BASE_DIR/.submodule/logger/warn.sh" "The $REPOSITORY require the dependency ${DEPENDENCY}:$(jq -r .[$c].tag <<<$DEPENDENCY_CONTENT). You don't have the same version"
            fi

            FOUND="1"
        done

        if [ $FOUND == "0" ]; then
            source "$BASE_DIR/.submodule/logger/error.sh" "The $REPOSITORY require the dependency ${DEPENDENCY}:$(jq -r .[$c].tag <<<$DEPENDENCY_CONTENT). You must add it in your dependencies"
        fi
    done
fi
