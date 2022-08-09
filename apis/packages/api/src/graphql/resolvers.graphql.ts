import { commons, PastLogsFilter } from '@web3-metadata/commons'
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
        }
    }
};