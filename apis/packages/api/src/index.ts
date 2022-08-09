import dotenv from 'dotenv';
dotenv.config();

import { createApolloServer } from './apolloServer';

const port = Number(process.env.PORT) as number;
createApolloServer({ port: port });
