package com.snap252.org.testing;

import java.text.MessageFormat;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class Timers {

	static void printTimer(final String name, final Runnable r) {
		printTimer(name, 1, r);
	}

	static void printTimer(final String name, final int cnt, final Runnable r) {
		System.err.println(MessageFormat.format("{0}: {1}ms", name, timer(() -> {
			for (int i = 0; i < cnt; i++)
				r.run();
		}) / cnt));
	}

	static long timer(final Runnable r) {
		final long l = System.nanoTime();
		r.run();
		return (System.nanoTime() - l) / 1000000L;
	}

	static <@NonNull R> R printTimer(final String name, final Supplier<R> r) {
		return printTimer(name, 1, r);
	}

	static <@NonNull R> R printTimer(final String name, final int cnt, final Supplier<R> r) {
		assert cnt > 0;
		final long l = System.nanoTime();
		@Nullable
		R ret = null;
		for (int i = 0; i < cnt; i++)
			ret = r.get();
		System.err.println(MessageFormat.format("{0}: {1}ms", name, (System.nanoTime() - l) / 1000000L / cnt));
		assert ret != null;
		return ret;
	}

}
