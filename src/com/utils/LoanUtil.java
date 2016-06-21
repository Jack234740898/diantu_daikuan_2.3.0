package com.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class LoanUtil {

	private static final String TAG = "LoanUtil";

	/*
	 * 计算总还款额
	 */
	public static String[] getHuanKuanLoan(int amount, int term) {
		double feeRate = 0.168 / 12;
		double fee = 0;
		double total = 0; // CalcUtil.
		total = getMonthRepayAmount(amount, term, feeRate).doubleValue()*term;// 总还款额
		fee = total - amount; // 总利息
		LogUtil.d(TAG, "getHuanKuanLoan()==amount is " + amount + ",term is "+ term + ",total is " + total + ",fee is " + fee);
		DecimalFormat df=new DecimalFormat("#.##");
		String totalStr = df.format(total);
		String feeStr = df.format(fee);
		return new String[] { totalStr, feeStr };
	}

	public static BigDecimal getMonthRepayAmount(double amount, int term,double rate) {
		double monthTotal = amount * rate * (Math.pow((1 + rate), term))/ (Math.pow((1 + rate), term) - 1);
		return new BigDecimal(monthTotal).setScale(2, BigDecimal.ROUND_CEILING);
	}
}
