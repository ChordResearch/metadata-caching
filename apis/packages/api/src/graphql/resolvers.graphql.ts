import { commons, PastLogsFilter, Metadata } from '@web3-metadata/commons'
import { NFTFilter } from 'src/types/db.types';
export const resolvers = {
    Query: {
        GetEvents: async (root: any, input: any) => {
            try {
                const filter: PastLogsFilter = {}
                let inputFilters = null
                if (input.eventFilter) {
                    inputFilters = input.eventFilter;
                }

                if (inputFilters && inputFilters.address) {
                    filter.address = inputFilters.address
                }

                if (inputFilters && inputFilters.fromBlock && inputFilters.toBlock) {
                    filter.fromBlock = inputFilters.fromBlock
                    filter.toBlock = inputFilters.toBlock
                }

                if (inputFilters && inputFilters.topics) {
                    filter.topics = inputFilters.topics
                }

                return {
                    __typename: 'EventLogs',
                    data: (await commons.blockchainService.getEvents(filter))
                }
            } catch (error) {
                console.log(`error : ${error}`)
                return {
                    __typename: 'InternalError',
                    error: 'Internal error while getting event logs',
                };
            }
        },
        GetMetadata: async (root: any, input: any) => {
            try {
                const filter: NFTFilter = {}
                if (input && input.filter && input.filter.address) {
                    filter.address = input.filter.address
                }
                if (input && input.filter && input.filter.tokenId) {
                    filter.tokenId = input.filter.tokenId
                }

                let offset: number = 0;
                let limit: number = 10;
                console.log(`input : ${JSON.stringify(input)}`)
                if (input && input.filter &&
                    input.filter.offset != undefined &&
                    input.filter.offset != null &&
                    input.filter.offset > 0
                ) {
                    offset = input.filter.offset
                }

                if (input &&
                    input.filter &&
                    input.filter.limit != undefined &&
                    input.filter.limit != null &&
                    input.filter.limit > 0
                ) {
                    limit = input.filter.limit
                }

                return {
                    __typename: 'Metadatas',
                    data: await Metadata.find(filter).skip(offset).limit(limit)
                }
            } catch (error) {
                console.log(`error : ${error}`)
                return {
                    __typename: 'InternalError',
                    error: 'Internal error while getting metadata',
                };
            }
        }
    }
};