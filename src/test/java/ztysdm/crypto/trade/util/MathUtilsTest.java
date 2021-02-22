package ztysdm.crypto.trade.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static ztysdm.crypto.trade.util.MathUtils.*;

import java.math.BigDecimal;

public class MathUtilsTest {

	@Test
	public void testPercentOf() throws Exception {
		BigDecimal percent =  percentOf(BigDecimal.valueOf(100.d), BigDecimal.valueOf(10.d));
		Assertions.assertTrue(percent.equals(BigDecimal.valueOf(10.d)));
	}
	
}
