package com.gettec.fsnip.fsn.util;

import java.math.BigDecimal;


public class NumberUtil {

	public static double scale(double value, int scale){
		BigDecimal bd = BigDecimal.valueOf(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}
