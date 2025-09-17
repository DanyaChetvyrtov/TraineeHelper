#!/bin/sh

# Start vault
vault server -dev -dev-listen-address=0.0.0.0:8200 -dev-root-token-id=myroot &

# Wait for vault
while ! nc -z 0.0.0.0 8200; do
  sleep 1
done

# Execute initialization script
if [ -f "/usr/local/bin/vault-initializer.sh" ]; then
  sh /usr/local/bin/vault-initializer.sh
else echo "File initializer not found"
fi


tail -f /dev/null