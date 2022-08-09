import { gql } from 'apollo-server';

export const typeDefs = gql`
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
  type EventLogs {
    data: [EventLog]
  }
  union EventLogResult = EventLogs | InvalidInputParamError | InternalError
  input EventFilter {
    address: [String]
    fromBlock: String
    toBlock: String
    topics: [[String]]
  }
  type Query {
    GetEvents(eventFilter: EventFilter): EventLogResult
  }
  input FavoriteAssetInput {
    assetAddress: String
  }
`;
