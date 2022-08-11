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


## Architecture
Here is the high level architecture.


![Indexer Architecture Diagram](./indexer_architecture_diagram.jpg)


Documentation

Setup : 

npm install knex -g

knex migrate:latest
knex seed:run

Connnect to local mongodb(if you have mongoshell installed) :
mongo "mongodb://root:rootpassword@localhost:27017/FNS-DB?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false"


cd packages/api
yarn start:dev