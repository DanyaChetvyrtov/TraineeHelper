#!/bin/bash

set -e

echo "PostgreSQL is ready. Creating databases..."

psql -U "$POSTGRES_USER" -c "CREATE DATABASE auth_user_db;" || echo "Database 'auth_user_db' already exists"
