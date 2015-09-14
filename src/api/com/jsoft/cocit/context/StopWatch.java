package com.jsoft.cocit.context;

import java.text.DecimalFormat;

/**
 * 秒表计时器
 * 
 * @author jiongsoft
 * 
 */
public class StopWatch {
	public int count = 0;

	// 秒表计时
	public long from;

	public long prev;

	public StopWatch() {
		from = System.currentTimeMillis();
		prev = from;
	}

	public static StopWatch begin() {
		return new StopWatch();
	}

	public long getElapse() {
		return System.currentTimeMillis() - prev;
	}

	public long getTotalElapse() {
		return System.currentTimeMillis() - from;
	}

	public long getMemElepse() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	public String toString() {
		StringBuffer ret = new StringBuffer(100);
		ret.append(new DecimalFormat("000").format(++count)).append(":  ");

		ret.append("elapse(");
		ret.append(getElapse());
		ret.append("/");
		ret.append(System.currentTimeMillis() - from);

		// long totalMemery = Runtime.getRuntime().totalMemory();
		// ret.append("), M(");
		// ret.append(getMemElepse());
		// ret.append("/");
		// ret.append(totalMemery);
		// ret.append(")");

		// MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
		// MemoryUsage usage = memorymbean.getHeapMemoryUsage();
		// ret.append(", UsageMax: " + usage.getMax());
		// ret.append(", UsageUsed: " + usage.getUsed());
		// ret.append(", MXBeanUsage: " + memorymbean.getHeapMemoryUsage());
		// ret.append(", MXBeanNoneUsage: " + memorymbean.getNonHeapMemoryUsage());

		prev = System.currentTimeMillis();

		return ret.toString();
	}
}
