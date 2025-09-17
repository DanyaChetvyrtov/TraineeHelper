#!/bin/bash

set -e

echo "PostgreSQL is ready. Creating databases..."

for cur_db in "auth_user_db" "profile_db"; do
  psql -U "$POSTGRES_USER" -c "CREATE DATABASE ${cur_db};" || echo "Database '${cur_db}' already exists"
done
