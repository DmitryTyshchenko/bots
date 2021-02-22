package ztysdm.crypto.trade.util;

import java.math.BigDecimal;

public class MathUtils {

	private MathUtils() {}
	
	public static BigDecimal percentOf(BigDecimal amount, BigDecimal percent) {
		BigDecimal onePercent = amount.divide(BigDecimal.valueOf(100.d));
		return onePercent.multiply(percent);
	}
	
}
