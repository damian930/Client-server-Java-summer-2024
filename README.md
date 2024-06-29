Create data base:   (use cmd (bash))

docker run --name store -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:16
docker exec -it store psql -U postgres

CREATE DATABASE store;
CREATE USER "user" WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE store TO "user";
GRANT CREATE ON SCHEMA public TO "user";

docker-compose up -d     
cd C:\Program Files\PostgreSQL\16\bin
psql -U user -d store
    password: password

use functions from store_db.sql

Delete data base:
    inside sql file
