#!/bin/sh
set -e
# Replace placeholder with env var
sed -i "s|__VITE_API_URL__|${VITE_API_URL}|g" /usr/share/nginx/html/env.js
exec "$@"
