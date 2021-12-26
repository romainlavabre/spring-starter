#!/bin/bash

function error() {
    C='\033[0;31m'
    NC='\033[0m'

    printf "${C}$1${NC}\n"
}

function warn() {
    C='\033[0;33m'
    NC='\033[0m'

    printf "${C}$1${NC}\n"
}

function info() {
    C='\033[0;34m'
    NC='\033[0m'

    printf "${C}$1${NC}\n"
}

function success() {
    C='\033[0;32m'
    NC='\033[0m'

    printf "${C}$1${NC}\n"
}