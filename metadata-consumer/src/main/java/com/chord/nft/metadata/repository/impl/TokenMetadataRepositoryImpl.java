package com.chord.nft.metadata.repository.impl;

import com.chord.nft.metadata.model.TokenMetadata;
import com.chord.nft.metadata.repository.TokenMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@Component
public class TokenMetadataRepositoryImpl implements TokenMetadataRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public TokenMetadata saveMetadata(TokenMetadata metadata) {
        return mongoTemplate.save(metadata);
    }

    @Override
    public TokenMetadata findTokenMetadata(String address, String tokenId) {
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("address").is(address));
        criterias.add(Criteria.where("tokenId").is(tokenId));

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

       return  mongoTemplate.findOne(query,TokenMetadata.class);
    }
}
