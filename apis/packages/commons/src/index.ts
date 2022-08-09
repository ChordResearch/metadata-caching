import dotenv from 'dotenv';
dotenv.config();

import { PastLogsFilter } from './types/blockchain.type';
import { BlockchainService } from './services/blockchain.service';

const networkURL = process.env.NETWORK_URL as string;
const blockchainService = new BlockchainService(networkURL);

export {
  PastLogsFilter
}

export const commons = {
  blockchainService: blockchainService,
}
