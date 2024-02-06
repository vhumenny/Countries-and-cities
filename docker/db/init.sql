SELECT 'CREATE DATABASE countries_and_cities'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'countries_and_cities')\gexec

