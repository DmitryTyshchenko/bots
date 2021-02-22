package ztysdm.crypto.trade.bianance;

import java.math.BigDecimal;

import ztysdm.crypto.trade.model.Order;
import ztysdm.crypto.trade.model.OrderStatus;
import ztysdm.crypto.trade.model.OrderType;
import ztysdm.crypto.trade.model.TradePair;

public interface StockExchangeGateway {

	Order createOrder(BigDecimal amount, BigDecimal goal, TradePair pair, OrderType orderType);

	OrderStatus orderStatus(Order order);

	BigDecimal currentPrice(TradePair traidePair);

}
