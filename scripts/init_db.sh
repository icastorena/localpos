#!/bin/bash
set -e

export PGUSER=localpos_user
export PGPASSWORD=localpos_pass
export PGHOST=localpos-db
export PGDATABASE=localpos_db

echo "🔄 Waiting for PostgreSQL to be ready..."
until psql -h "$PGHOST" -U "$PGUSER" -d "$PGDATABASE" -c '\q' 2>/dev/null; do
  >&2 echo "⏳ PostgreSQL is unavailable - sleeping"
  sleep 1
done
echo "✅ PostgreSQL is up!"

# Step 1: Users schema
echo "🚀 Initializing USERS schema..."
psql -v schema=users -f /scripts/sql/01-create-users-tables.sql
psql -v schema=users -f /scripts/sql/06-seed-users-schema.sql
echo "✅ Users schema initialized."

# Step 2: Products schema
echo "📦 Initializing PRODUCTS schema..."
psql -v schema=products -f /scripts/sql/02-create-products-tables.sql
psql -v schema=products -f /scripts/sql/07-seed-products-schema.sql
echo "✅ Products schema initialized."

# Step 3: Inventory schema
echo "📊 Initializing INVENTORY schema..."
psql -v schema=inventory -f /scripts/sql/03-create-inventory-tables.sql
psql -v schema=inventory -f /scripts/sql/08-seed-inventory-schema.sql
echo "✅ Inventory schema initialized."

# Step 4: Orders schema
echo "🧾 Initializing ORDERS schema..."
psql -v schema=orders -f /scripts/sql/04-create-orders-tables.sql
psql -v schema=orders -f /scripts/sql/09-seed-orders-schema.sql
echo "✅ Orders schema initialized."

# Step 5: Reporting schema
echo "📈 Initializing REPORTING schema..."
psql -v schema=reporting -f /scripts/sql/05-create-reporting-tables.sql
echo "✅ Reporting schema initialized."

echo "🎉 All schemas initialized successfully in localpos_db!"
