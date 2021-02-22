package ztysdm.crypto.trade.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ztysdm.crypto.trade.model.TradeData;

@Repository
public interface CryptoRepository extends MongoRepository<TradeData, String> {

}
