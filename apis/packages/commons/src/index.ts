import dotenv from 'dotenv';
dotenv.config();

import { PastLogsFilter } from './types/blockchain.type';
import { BlockchainService } from './services/blockchain.service';
import { connectToDatabase } from './services/db.service';
import Metadata from './models/token-metadata';
const networkURL = process.env.NETWORK_URL as string;
const blockchainService = new BlockchainService(networkURL);

export {
  PastLogsFilter,
  Metadata
}

export const commons = {
  blockchainService: blockchainService,
  connectToDatabase: connectToDatabase,
}
