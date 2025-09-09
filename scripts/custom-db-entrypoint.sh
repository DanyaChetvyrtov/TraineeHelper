#!/bin/sh

docker-entrypoint.sh postgres &

echo "Waiting for PostgreSQL to start..."
while ! pg_isready -U "$POSTGRES_USER" -h localhost -p 5432; do
  sleep 1
done

if [ -f "/usr/local/bin/db-initializer.sh" ]; then
  echo "Running PostgreSQL initialization script..."
  sh /usr/local/bin/db-initializer.sh
fi

tail -f /dev/null