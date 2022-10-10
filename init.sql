
create database ethseoul-db;

CREATE TABLE public."global" (
	id serial4 NOT NULL,
	block int8 NOT NULL,
	status bool NULL DEFAULT true,
	"createdAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	"updatedAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT global_pkey PRIMARY KEY (id)
);

CREATE TABLE public.nfts (
	id serial4 NOT NULL,
	"tokenAddress" varchar(255) NOT NULL,
	"tokenId" varchar(255) NOT NULL,
	"tokenURI" varchar(50000) NULL,
	"minterAddress" varchar(255) NOT NULL,
	"blockNumber" int8 NOT NULL,
	"transactionHash" varchar(255) NOT NULL,
	status bool NULL DEFAULT true,
	"createdAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	"updatedAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT nfts_pkey PRIMARY KEY ("tokenAddress", "tokenId")
);