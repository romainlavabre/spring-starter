#!/bin/bash

BASE_DIR="$PWD"
CONFIG=$(cat "$BASE_DIR/submodule.json")

for ((i = 0; i < $(jq '. | length' <<<$CONFIG); i++)); do
    NAME="$(jq -r .[$i].name <<<$CONFIG)"
    TARGET="$(jq -r .[$i].target <<<$CONFIG)"
    REPOSITORY="$(jq -r .[$i].repository <<<$CONFIG)"
    TAG="$(jq -r .[$i].tag <<<$CONFIG)"
    SCRIPT_EXECUTION="$(jq -r .[$i].scriptExecution <<<$CONFIG)"

    source "${BASE_DIR}/.submodule/dependency.sh" "${BASE_DIR}${TARGET}"
done