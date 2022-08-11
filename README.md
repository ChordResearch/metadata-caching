# metadata-caching

Documentation

Setup : 

npm install knex -g

knex migrate:latest
knex seed:run

Connnect to local mongodb(if you have mongoshell installed) :
mongo "mongodb://root:rootpassword@localhost:27017/FNS-DB?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false"


cd packages/api
yarn start:dev