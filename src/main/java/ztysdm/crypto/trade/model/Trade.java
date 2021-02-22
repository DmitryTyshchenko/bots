package ztysdm.crypto.trade.model;

import java.math.BigDecimal;

import ztysdm.crypto.trade.strategy.TradeStrategy;

public class Trade extends Persisted {

	TradeStrategy tradeStrategy;
	TradeData tradeData;

	public BigDecimal profit() {
		return tradeData.profit();
	}

	public void addProfit(BigDecimal profit) {
		tradeData.addProfit(profit);
	}
}
