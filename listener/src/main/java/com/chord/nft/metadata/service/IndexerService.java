package com.chord.nft.metadata.service;

import com.chord.nft.metadata.dto.EventFilter;
import com.chord.nft.metadata.dto.EventParseResult;
import com.chord.nft.metadata.entity.Blockhash;
import com.chord.nft.metadata.entity.Global;
import com.chord.nft.metadata.event.factory.EventHandlerFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class IndexerService {

    private ExecutorService threadPool;

    @Value("${THREAD_POOL_SIZE}")
    private int threadPoolSize;

    @Value("${FAST_SYNC_BLOCK_LENGTH}")
    private int fastSyncBlockLength;

    @Value("${network.url}")
    private String networkURL;

    @Value("${TRANSFER_EVENT_TOPIC}")
    private String transferTopic;


    @Autowired
    EventHandlerFactory eventsHandlerFactory;

    @Autowired
    BlockchainService blockChainService;

    @Autowired
    RepositoryService repositoryService;
    private Web3j web3j;

    @PostConstruct
    private void initialize() {
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
        web3j = Web3j.build(new HttpService(networkURL));

        if (fastSyncBlockLength <= 0) fastSyncBlockLength = 1;
    }

    public void indexBlocks(Connection conn) throws Exception {
        if (eventsHandlerFactory.getConnection() == null)
            eventsHandlerFactory.setConnection(conn);

        Global global = repositoryService.getGlobalRepository().getCurrentBlock(conn);
        System.out.println("Current block in DB : " + global.getBlock() +
                " , verified block : " + global.getVerifiedBlock() +
                " , index status : " + global.isStatus() +
                " , last updatedAt : " + global.getUpdatedAt()
        );

        BigInteger latestBlkInNw = new BigInteger(blockChainService.getCurrentNetworkBlock());
        // System.out.println("Latest block in network : " + latestBlkInNw.toString());

        BigInteger currentBlkInDB = new BigInteger(global.getBlock() + ""); // long to string
        BigInteger nextBlockInDB = currentBlkInDB.add(new BigInteger("1"));

        /*
            Checking sync status is ON and next block to be synced is less than or equal to the latest block in network
        */
        if (global.isStatus() &&
                nextBlockInDB.compareTo(latestBlkInNw) <= 0) {
            BigInteger toBlock = nextBlockInDB;

            /*
                Checking if after adding fast block length, still the blockNumber is less than or equal to the latest block in Network
                Why (fastSyncBlockLength - 1) ? Because of fastBlockLength is set as 1 that means we only want to sync 1 block at a time.
                So calculation from toBlock comes as : toBlock = fromBlock + fastSyncBlockLength - 1)

                eg;
                   fromBlock = 100
                   fastBlockLength = 1
                   toBlock = 100 + (1-1) = 100

                   Hence indexer will sync fromBlock : 100, toBlock : 100
             */
            BigInteger toBlockWithFastSync = nextBlockInDB.add(new BigInteger((fastSyncBlockLength - 1) + ""));
            if (toBlockWithFastSync.compareTo(latestBlkInNw) <= 0) {
                toBlock = toBlockWithFastSync;
            }

            // index blocks from given range
            doIndex(nextBlockInDB, toBlock, conn);
        }
    }

    private void doIndex(BigInteger fromBlock, BigInteger toBlock, Connection conn) throws Exception {
        System.out.println("Index from " + fromBlock.toString() + " , to : " + toBlock.toString());
        String address[] = {};
        String topic[] = {
                transferTopic,
        };
        String topics[][] = {topic};
        EventFilter filter = new EventFilter(address, fromBlock.toString(), toBlock.toString(), topics);
        JSONArray events = blockChainService.getEvents(filter);

        System.out.println("events length : " + events.length());
        if (events.length() > 0) {
            // process events
            CountDownLatch latch = new CountDownLatch(events.length());
            List<Callable<EventParseResult>> callables = new ArrayList<>();

            for (int index = 0; index < events.length(); index++) {
                JSONObject event = events.getJSONObject(index);
                // Adding into collection to process in Thread Pool
                callables.add(new EventsProcessor(event, latch, eventsHandlerFactory, web3j,conn));
            }

            // Process all events in executor service thread pool
            List<Future<EventParseResult>> futures = threadPool.invokeAll(callables);

            //wait for latch to be decremented by all the threads
            latch.await();

            // if any previous unsaved changes then rollback to discard those items.
            if (!conn.getAutoCommit())
                conn.rollback();

            // save all processed events
            saveEvents(futures, conn);
        }

        // increment block
        int incrementBy = toBlock.subtract(fromBlock).intValue() + 1;
        System.out.println("increment by : " + incrementBy);
        repositoryService.getGlobalRepository().incrementBlock(incrementBy, conn);

        // Save all updates to DB
        conn.commit();

        Global globalAfterIncrement = repositoryService.getGlobalRepository().getCurrentBlock(conn);
        System.out.println("Current block in DB After increment : " + globalAfterIncrement.getBlock() +
                " , verified block : " + globalAfterIncrement.getVerifiedBlock() +
                " , index status : " + globalAfterIncrement.isStatus() +
                " , updatedAt : " + globalAfterIncrement.getUpdatedAt()
        );
    }

    private void saveEvents(List<Future<EventParseResult>> futures, Connection conn) throws Exception {
        for (int i = 0; i < futures.size(); i++) {
            EventParseResult res = futures.get(i).get();
            if (res.isValidEvent()) {
                res.getEvent().save(this.web3j, repositoryService, conn);
            }
            // System.out.println("Completed processing event with index : " + i);
        }
    }
}
