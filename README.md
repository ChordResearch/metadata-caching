# ETHSeoul Hackathon Project
# Multi-Chain, High-Performance NFT Metadata caching for all Web3 user cases

## About Project
This project is built from scratch to meet all the requirements related to caching and query NFT Metadata.

## Why this project?
NFT are such an important use case in crypto and metadata gives you all the details about it. Query Metadata from IPFS can add lot of latency to your project hence it helps if there are tools for caching and querying it.

## But why another NFT Metadata service when there are already so many services avaialble?
From our own personal experiences we find that many of the existing services are slow and not so efficient. They also support very few selected chains.
Hence this product is built from ground up to just meet this requirement.
It is near realtime. Utilized concurrency and it is very much extendable for all EVM chains.
With some more modification it can also support non-evm chains.

## Key features
1. Extendable
2. Support multiple chains (EVM and non-EVM)
3. Near real-time data processing and storage
4. Data consistency
5. Fault-tolerant


## Architecture
Here is the high level architecture.


![Indexer Architecture Diagram](./indexer_architecture_diagram.jpg)

## Key Components 
### 
Key components of this indexer architecture include : 
1. [Listener](./listener)
2. [Metadata consumer](./metadata-consumer)
3. [API Service](./apis/packages/api)


Steps to run this project : 
1. Setup rabbitmq
2. Setup postgresdb
3. Setup mongodb
4. Create a AWS S3 Bucket
5. Set environment properties for API service
6. Set environment properties for Listener
7. Set environment properties for Metadata consumer
8. [Run db migration script](./db)
9. Start API service followed by listener and then meta data consumer

## Setup : 
### Install RabbitMQ
> RabbitMQ can be replaced with any MQ later but for a start, in this project RabbitMQ is used.

`
docker run  -d -it  --rm --name local-rabbitmq -p 5672:5672 -p 15672:15672 -p 8080:8080 -e RABBITMQ_DEFAULT_USER=guest  -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:3.10-management
`

### Install Postgres DB
`
docker run --name postgres-local -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=rootpassword -e POSTGRES_DB:ethseoul-db -d postgres
`

You can use tools such as DBeaver or PGAdmin to connect and create database `ethseoul-db`

### Install mongodb
`
docker run --name my-local-mongo-latest -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=rootpassword -p 27017:27017 -v <path to data folder>:/etc/mongo -d mongo
`

### Install 

npm install knex -g

knex migrate:latest
knex seed:run

Connnect to local mongodb(if you have mongoshell installed) :
mongo "mongodb://root:rootpassword@localhost:27017/FNS-DB?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false"


cd packages/api
yarn start:dev