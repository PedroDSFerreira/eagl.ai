SHELL := /bin/bash

# Function to determine Docker Compose command
define docker_compose_cmd
	$(if $(shell command -v docker-compose 2> /dev/null),docker-compose,$(if $(shell command -v docker compose 2> /dev/null),docker compose,))
endef


prepare-env:
	cp .env-sample .env

up:
	$(call docker_compose_cmd) up

upd:
	$(call docker_compose_cmd) up -d

down:
	$(call docker_compose_cmd) down

stop:
	$(call docker_compose_cmd) stop

build:
	$(call docker_compose_cmd) build

build-gpu:
	$(call docker_compose_cmd) -f docker-compose-gpu.yaml build

build-no-cache:
	$(call docker_compose_cmd) build --no-cache

logs:
	$(call docker_compose_cmd) logs -f
