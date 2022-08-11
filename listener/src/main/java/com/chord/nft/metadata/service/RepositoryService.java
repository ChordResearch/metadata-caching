package com.chord.nft.metadata.service;

import com.chord.nft.metadata.message.RabbitMQSender;
import com.chord.nft.metadata.repository.GlobalRepository;
import com.chord.nft.metadata.repository.NFTRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class RepositoryService {
    @Autowired
    GlobalRepository globalRepository;

    @Autowired
    private NFTRepository nftRepository;

    @Autowired
    private RabbitMQSender mqSender;
}
