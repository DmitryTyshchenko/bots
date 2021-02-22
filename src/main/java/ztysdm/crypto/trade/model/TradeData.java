package ztysdm.crypto.trade.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TradeData {

	BigDecimal seed;
	List<BigDecimal> profit = new ArrayList<>();
	Order currentOrder;
	
	public BigDecimal profit() {
		return profit.stream().reduce(BigDecimal.valueOf(0.d), BigDecimal::add);
	}
	
	public void addProfit(BigDecimal profit) {
		this.profit.add(profit);
	}
	
	public void setOrder(Order order) {
		this.currentOrder = order;
	}
	
	public Order order() {
		return this.currentOrder;
	}
	
	public BigDecimal seed() {
		return this.seed;
	}
	
}
