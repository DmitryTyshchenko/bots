package ztysdm.crypto.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//docker run -d -p 27017:27017 mongo
@SpringBootApplication
public class CryptoTradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTradeApplication.class, args);
	}

}
