package com.snap252.org;

import java.text.MessageFormat;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class Timers {

	static void printTimer(String name, Runnable r) {
		printTimer(name, 1, r);
	}

	static void printTimer(String name, int cnt, Runnable r) {
		System.err.println(MessageFormat.format("{0}: {1}ms", name, timer(() -> {
			for (int i = 0; i < cnt; i++)
				r.run();
		}) / cnt));
	}

	static long timer(Runnable r) {
		final long l = System.nanoTime();
		r.run();
		return (System.nanoTime() - l) / 1000000L;
	}

	static <@NonNull R> R printTimer(String name, Supplier<R> r) {
		return printTimer(name, 1, r);
	}

	static <@NonNull R> R printTimer(String name, int cnt, Supplier<R> r) {
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
