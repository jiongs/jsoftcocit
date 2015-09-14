package com.jsoft.cocit.util;

import java.io.IOException;
import java.io.Writer;

public abstract class WriteUtil {

	public static void write(Writer out, String format, Object... args) throws IOException {
		// out.write("\n");
		if (args.length == 0)
			out.write(format);
		else
			out.write(String.format(format, args));
	}

	public static void write(StringBuffer out, String format, Object... args) throws IOException {
		// out.append("\n");
		if (args.length == 0)
			out.append(format);
		else
			out.append(String.format(format, args));
	}
}
