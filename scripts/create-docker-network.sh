#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -euo pipefail

readonly RED=$(tput setaf 1)
readonly GREEN=$(tput setaf 2)
readonly BLUE=$(tput setaf 4)
readonly YELLOW=$(tput setaf 3)
readonly BOLD=$(tput bold)
readonly RESET=$(tput sgr0)

readonly NETWORK_NAME="ci-trainee-helper"

log_info() {
    echo "${BLUE}ℹ ${*}${RESET}"
}

log_success() {
    echo "${GREEN}✓ ${*}${RESET}"
}

log_warning() {
    echo "${YELLOW}⚠ ${*}${RESET}" >&2
}

log_error() {
    echo "${RED}✗ ${*}${RESET}" >&2
}

check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed or not in PATH"
        exit 1
    fi

    if ! docker info &> /dev/null; then
        log_error "Docker daemon is not running or user doesn't have permissions"
        exit 1
    fi
}

network_exists() {
    docker network inspect "$NETWORK_NAME" &> /dev/null
}

create_network() {
    log_info "Creating Docker network: ${BOLD}${NETWORK_NAME}${RESET}"

    if network_exists; then
        log_warning "Network '${NETWORK_NAME}' already exists"
        return 0
    fi

    if docker network create "$NETWORK_NAME" > /dev/null 2>&1; then
        log_success "Network '${NETWORK_NAME}' successfully created"
        return 0
    else
        log_error "Failed to create network '${NETWORK_NAME}'"
        return 1
    fi
}

show_network_info() {
    if network_exists; then
        log_info "Network information:"
        docker network inspect "$NETWORK_NAME" --format '\
Name: {{.Name}}\n
ID: {{.ID}}\n
Driver: {{.Driver}}\n
Scope: {{.Scope}}\n
Created: {{.Created}}' 2>/dev/null || true
    fi
}

main() {
    local exit_code=0

    log_info "Starting Docker network setup..."

    check_docker

    if create_network; then
        show_network_info
        log_success "Network setup completed successfully"
    else
        exit_code=1
    fi

    return $exit_code
}


if main; then
  exit 0
else
  exit 1
fi