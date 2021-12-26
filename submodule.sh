#!/bin/bash

source "./.submodule/logger/logger.sh"

if [ ! -f "submodule.lock" ]; then
    echo "{}" > submodule.lock
fi

if [ "$1" == "--install" ] || [ "$1" == "-i" ]; then
    source ".submodule/update.sh"
fi

if [ "$1" == "--update" ] || [ "$1" == "-u" ]; then
    source ".submodule/update.sh"
fi

if [ "$1" == "--check-dependency" ] || [ "$1" == "-cd" ]; then
    source ".submodule/analyze-dependency.sh"
fi

if [ "$1" == "--modify" ] || [ "$1" == "-m" ]; then
    source ".submodule/modify.sh"
fi

if [ "$1" == "--help" ] || [ "$1" == "-h" ]; then
    info "
--install           -i      Install dependencies
--update            -u      Update dependencies based on lock file
--modify            -m      Install the environment to edit the submodule
--check-dependency  -cd     Check potential version or dependency conflicts
    "
fi

