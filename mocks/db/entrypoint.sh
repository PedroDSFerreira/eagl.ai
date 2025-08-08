#!/bin/sh
# Entrypoint script for db-cleaner container

set -e

echo "Installing dependencies..."
apk add --no-cache postgresql-client busybox-suid imagemagick imagemagick-jpeg ffmpeg

echo "Waiting for scripts to be available..."
while [ ! -f /tmp/init.sh ]; do
    sleep 1
done

echo "Making scripts executable..."
chmod +x /tmp/cleanup.sh
chmod +x /tmp/seed.sh
chmod +x /tmp/init.sh

echo "Setting up cron job..."
echo 'PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
0 * * * * /tmp/init.sh' > /etc/crontabs/root

echo "Running initial database setup..."
/bin/sh /tmp/init.sh

echo "Starting cron daemon..."
crond -f -d 8
