package ztysdm.crypto.trade.model;

import java.math.BigDecimal;

public class Order extends Persisted {

	private final OrderType orderType;
	private final BigDecimal amount;
	
	public Order(OrderType orderType, BigDecimal amount) {
		this.orderType = orderType;
		this.amount = amount;
	}
	
}
