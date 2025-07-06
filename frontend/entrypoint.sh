#!/bin/sh
set -e
# Replace only the value, not the variable name
sed -i "s|window.__VITE_API_URL__ = \".*\";|window.__VITE_API_URL__ = \"${VITE_API_URL}\";|" /usr/share/nginx/html/env.js
exec "$@"
