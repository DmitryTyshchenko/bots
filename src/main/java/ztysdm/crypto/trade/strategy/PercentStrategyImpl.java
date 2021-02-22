package ztysdm.crypto.trade.strategy;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ztysdm.crypto.trade.bianance.StockExchangeGateway;
import ztysdm.crypto.trade.config.MongoConfiguration;
import ztysdm.crypto.trade.model.Order;
import ztysdm.crypto.trade.model.OrderStatus;
import ztysdm.crypto.trade.model.OrderType;
import ztysdm.crypto.trade.model.TradeData;
import ztysdm.crypto.trade.model.TradePair;
import ztysdm.crypto.trade.util.MathUtils;

public class PercentStrategyImpl implements TradeStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfiguration.class);
	
	private TradeData tradeData;
	private StockExchangeGateway stockExchangeGateWay;
	private TradePair tradePair;

	private ArrayBlockingQueue<BigDecimal> toSell = new ArrayBlockingQueue<>(1);
	private ArrayBlockingQueue<BigDecimal> toBuy = new ArrayBlockingQueue<>(1);

	private BigDecimal percentToBuy = BigDecimal.valueOf(1.5d);
	private BigDecimal percentToSell = BigDecimal.valueOf(1.5d);

	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	public PercentStrategyImpl(TradeData tradeData, TradePair tradePair, StockExchangeGateway stockExchangeGateWay) {
		this.tradeData = tradeData;
		this.tradePair = tradePair;
		this.stockExchangeGateWay = stockExchangeGateWay;
	}

	public void setPercentToBuy(BigDecimal percentToBuy) {
		this.percentToBuy = percentToBuy;
	}

	public void setPercentToSell(BigDecimal percentToSell) {
		this.percentToSell = percentToSell;
	}

	@Override
	public void trade() {
		// at first step we need to buy something so add the queue the seed
		this.toBuy.add(tradeData.seed());
		executorService.submit(new CoinSeller(this));
		executorService.submit(new CoinBuyer(this));
	}

	@Override
	public void cancel() {
		withTryCatch(() -> {
			executorService.shutdown();
			executorService.awaitTermination(1, TimeUnit.SECONDS);
		});
	}

	static class CoinBuyer implements Runnable {

		final PercentStrategyImpl context;

		public CoinBuyer(PercentStrategyImpl context) {
			this.context = context;
		}

		@Override
		public void run() {
			while (true) {
				withTryCatch(() -> {
					BigDecimal busdAmount = context.toBuy.take();
					BigDecimal currentPrice = context.stockExchangeGateWay.currentPrice(context.tradePair);
					BigDecimal goal = currentPrice.subtract(MathUtils.percentOf(currentPrice, context.percentToBuy));
					Order order = context.stockExchangeGateWay.createOrder(busdAmount, goal, context.tradePair,
							OrderType.BUY);
					waitUntilOrderIsCompleted(order, context);
					BigDecimal btcAmount = busdAmount.multiply(currentPrice);
					context.toSell.add(btcAmount);
				});
			}
		}

	}

	static class CoinSeller implements Runnable {

		final PercentStrategyImpl context;

		public CoinSeller(PercentStrategyImpl context) {
			this.context = context;
		}

		@Override
		public void run() {
			while (true) {
				withTryCatch(() -> {
					BigDecimal btcAmount = context.toSell.take();
					BigDecimal currentPrice = context.stockExchangeGateWay.currentPrice(context.tradePair);
					BigDecimal goal = currentPrice.add(MathUtils.percentOf(currentPrice, context.percentToSell));

					Order order = context.stockExchangeGateWay.createOrder(btcAmount, goal, context.tradePair,
							OrderType.SELL);
					waitUntilOrderIsCompleted(order, context);
					BigDecimal profit = btcAmount.multiply(goal);
					context.tradeData.addProfit(profit);
					context.toBuy.add(context.tradeData.seed());
				});
			}
		}
	}

	static void withTryCatch(Action action) {

		try {
			action.doAction();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.warn("Trading has been interrupted");
		}
	}

	@FunctionalInterface
	private interface Action {
		void doAction() throws InterruptedException;
	}

	@SuppressWarnings("static-access")
	static void waitUntilOrderIsCompleted(Order order, PercentStrategyImpl context) throws InterruptedException {
		OrderStatus orderStatus = context.stockExchangeGateWay.orderStatus(order);

		while (orderStatus != OrderStatus.COMPLETED) {
			Thread.currentThread().sleep(5000);
			orderStatus = context.stockExchangeGateWay.orderStatus(order);
		}
	}

}
