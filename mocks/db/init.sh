#!/bin/bash
# Initialize the database by cleaning it and then seeding it with sample data
# This script calls cleanup.sh followed by seed.sh

set -e

echo "Initializing database..."

# First, clean the database
echo "Step 1: Cleaning database..."
/bin/sh /tmp/cleanup.sh

# Then, seed it with data
echo "Step 2: Seeding database..."
cd /tmp && /bin/sh /tmp/seed.sh

echo "Database initialization complete!"
