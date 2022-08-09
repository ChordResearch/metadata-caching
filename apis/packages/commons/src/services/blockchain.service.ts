import Web3 from 'web3';
import { PastLogsFilter } from '../types/blockchain.type';

export class BlockchainService {
    private web3: Web3;
    constructor(networkUri: string) {
        this.web3 = new Web3(networkUri);
    }
    getWeb3 = (): Web3 => {
        return this.web3;
    };

    getEvents = async (filter: PastLogsFilter): Promise<any | never> => {
        try {
            return await this.web3.eth.getPastLogs(filter);
        } catch (error) {
            console.error(
                `Error encountered in getEventsByBlock(addresses, fromBlock, toBlock) : ${error} `
            );
            throw new Error('Invalid filter');
        }
    };
}