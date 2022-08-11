import { makeExecutableSchema } from '@graphql-tools/schema';
import { connectToDatabase } from '@web3-metadata/commons/src/services/db.service';
import { ApolloServer } from 'apollo-server';

import { resolvers } from './graphql/resolvers.graphql';
import { typeDefs } from './graphql/schema.graphql';


const dbURL = process.env.DB_CONNECTION_URL as string;

// This function will create a new server Apollo Server instance
export const createApolloServer = async (options = { port: 4000 }) => {
  let schema = makeExecutableSchema({
    typeDefs,
    resolvers,
  });

  const server = new ApolloServer({
    schema,
    context: async ({ req }) => {
      return req;
    },
  });
  await connectToDatabase(dbURL)
  const serverInfo = await server.listen(options);

  console.log(
    `🚀 Query App endpoint ready at port ${options.port} and path ${server.graphqlPath}`
  );

  // serverInfo is an object containing the server instance and the url the server is listening on
  return serverInfo;
};