package com.snap252.vaadin.pivot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl;
import com.vaadin.sass.internal.tree.BlockNode;
import com.vaadin.sass.internal.tree.ConsoleMessageNode;
import com.vaadin.sass.internal.tree.ExtendNode;
import com.vaadin.sass.internal.tree.FunctionCall;
import com.vaadin.sass.internal.tree.MixinNode;
import com.vaadin.sass.internal.tree.controldirective.IfElseDefNode;
import com.vaadin.sass.internal.visitor.ImportNodeHandler;

@RunWith(Parameterized.class)
public class ScssTest {

	@Parameters
	public static Collection<String> data() {
		return Arrays.asList("/VAADIN/addons/pivot/pivot.scss", "/VAADIN/themes/demo/styles.scss");
	}

	@Parameter
	public String scssResourceName;

	private static final class ThrowExceptionHandler extends Handler {
		@Override
		public void publish(final LogRecord record) {
			if (record.getLevel() != Level.INFO) {
				final Throwable thrown = record.getThrown();
				throw new AssertionError(record.getLevel() + ": " + (thrown != null ? thrown.getMessage() : ""),
						thrown);
			}
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() throws SecurityException {
		}
	}

	private static final boolean PRINT_CSS = false;

	@Test
	public void doSingleTest() throws Exception {
		testSingleFile(scssResourceName);
	}

	private static void testSingleFile(final String scssResourceName) throws IOException, Exception {
		assert ScssTest.class.getResource(scssResourceName) != null : scssResourceName;

		final ScssStylesheet scss = ScssStylesheet.get(scssResourceName);
		assert scss != null : scssResourceName;

		final ThrowExceptionHandler handler = new ThrowExceptionHandler();
		final ArrayList<Logger> loggers = new ArrayList<>();
		for (final Class<?> handlerClass : new Class[] { SCSSDocumentHandlerImpl.class, ImportNodeHandler.class,
				ConsoleMessageNode.class, MixinNode.class, BlockNode.class, ConsoleMessageNode.class, ExtendNode.class,
				FunctionCall.class, IfElseDefNode.class, ImportNodeHandler.class, ScssStylesheet.class,
				SCSSDocumentHandlerImpl.class }) {
			final Logger logger = Logger.getLogger(handlerClass.getName());
			loggers.add(logger);
			logger.addHandler(handler);
		}
		scss.compile();

		if (PRINT_CSS) {
			System.err.println("===============================");
			System.err.println(scssResourceName + " : " + scss.printState().length());
			System.err.println("===============================");
			System.err.println(scss.printState());
			System.err.println("===============================");
		}

		// DO NOT DELETE OR INLINE THIS - otherwise GC can collect the instance
		// - and scss errors would not be shown!
		loggers.size();
	}

}
