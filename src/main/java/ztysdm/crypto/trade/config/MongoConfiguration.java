package ztysdm.crypto.trade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.WriteResultChecking;

@Configuration
public class MongoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfiguration.class);

	@Bean
	public MongoClientFactoryBean mongo() {
		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
		mongo.setHost("localhost");
		return mongo;
	}

	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory(@Autowired MongoClientFactoryBean mongoClientFactory)
			throws Exception {
		var mongoClient = mongoClientFactory.getObject();
		return new SimpleMongoClientDatabaseFactory(mongoClient, "crypto_trade_database");
	}

	@Bean
	public MongoOperations mongoTemplate(@Autowired MongoDatabaseFactory datadaseFactory) throws Exception {
		var mongoTemplate = new MongoTemplate(datadaseFactory);
		mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
		return mongoTemplate;
	}
}
