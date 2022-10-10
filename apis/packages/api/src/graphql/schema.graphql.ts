import { gql } from 'apollo-server';
import { GraphQLJSONObject } from 'graphql-type-json';

export const typeDefs = gql`
  scalar JSONObject
  type InternalError {
    error: String!
  }
  type InvalidInputParamError {
    error: String!
  }
  type EventLog {
    address: String
    data: String
    topics: [String]
    logIndex: Int
    transactionIndex: Int
    transactionHash: String
    blockHash: String
    blockNumber: Int
  }
  
  type Metadata {
    metadata: JSONObject
    tokenId: String
    address: String
    assetCDNUrl: String
  }

  type EventLogs {
    data: [EventLog]
  }
  type Metadatas {
    data: [Metadata]
  }
  union EventLogResult = EventLogs | InvalidInputParamError | InternalError
  union MetadataResult = Metadatas | InvalidInputParamError | InternalError
  input EventFilter {
    address: [String]
    fromBlock: String
    toBlock: String
    topics: [[String]]
  }
  input MetadataFilter {
    address: String
    tokenId: String
    offset: Int
    limit: Int
  }

  type Query {
    GetEvents(eventFilter: EventFilter): EventLogResult
    GetMetadata(filter: MetadataFilter): MetadataResult
  }

`;
