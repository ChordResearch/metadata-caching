
CREATE TABLE global (
	id serial4 NOT NULL,
	block int8 NOT NULL,
	status bool NULL DEFAULT true,
	"createdAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	"updatedAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT global_pkey PRIMARY KEY (id)
);

CREATE TABLE nfts (
	id serial4 NOT NULL,
	"tokenAddress" varchar(255) NOT NULL,
	"tokenId" varchar(255) NOT NULL,
	"tokenURI" text NULL,
	"minterAddress" varchar(255) NOT NULL,
	"blockNumber" int8 NOT NULL,
	"transactionHash" varchar(255) NOT NULL,
	status bool NULL DEFAULT true,
	"createdAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	"updatedAt" timestamptz NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT nfts_pkey PRIMARY KEY ("tokenAddress", "tokenId")
);

INSERT INTO global
(block, status)
VALUES(7743360, false);
