package game.utils;

import com.badlogic.gdx.Gdx;

public class Log {

	private static final String TAG = "fortnitta";
	private static final boolean DO_NOT_PRINT_STACK_FRAMES = false;

	/*
	 * Sets the minimum log level to log. If set to `android.util.Log.VERBOSE`, all log messages (from verbose to
	 * critical) are logged. If set to `android.util.Log.ERROR`, only error and critical messages are logged.
	 */
	public static int MINIMUM_LOG_LEVEL = android.util.Log.DEBUG;

	/* Logs the class name, method name, and filename of the caller */
	public static void call() {
		if (MINIMUM_LOG_LEVEL > android.util.Log.VERBOSE)
			return;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int currentIndex = 3; /* Wind up the stack frame 3 levels, just happens to be the right number */

		String fullClassName = stackTrace[currentIndex].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = stackTrace[currentIndex].getMethodName();
		String lineNumber = String.valueOf(stackTrace[currentIndex].getLineNumber());

		android.util.Log.v(TAG, "Entering " + className + "." + methodName + "（） (" + className + ".java:" + lineNumber
				+ ")");
	}

	public static void verbose(String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.VERBOSE)
			return;
		android.util.Log.v(TAG, String.format(message, args) + getNavigatableStackFrameOrigin());
	}

	/** Plagiarized from: http://stackoverflow.com/a/10599298 */
	private static String getNavigatableStackFrameOrigin() {
		return getNavigatableStackFrameOrigin(1);
	}

	private static String getNavigatableStackFrameOrigin(int unwindThisManyFrames) {
		if (DO_NOT_PRINT_STACK_FRAMES)
			return "";

		unwindThisManyFrames += 4;

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		String fullClassName = stackTrace[unwindThisManyFrames].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = stackTrace[unwindThisManyFrames].getMethodName();
		String lineNumber = String.valueOf(stackTrace[unwindThisManyFrames].getLineNumber());

		return String.format(" (%s.java:%s)", className, lineNumber);
	}

	public static void debug(String message, Object... args) {
		debug(1, message, args);
	}

	public static void debug(int additionalStackLevel, String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.DEBUG)
			return;
		android.util.Log.d(TAG, String.format(message, args) + getNavigatableStackFrameOrigin(additionalStackLevel));
	}

	public static void info(String message, Object... args) {
		info(1, message, args);
	}

	public static void info(int additionalStackLevel, String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.INFO)
			return;
		android.util.Log.i(TAG, String.format(message, args) + getNavigatableStackFrameOrigin(additionalStackLevel));
	}

	public static void warn(String message, Object... args) {
		warn(1, message, args);
	}

	public static void warn(int additionalStacklevel, String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.WARN)
			return;
		android.util.Log.w(TAG, String.format(message, args) + getNavigatableStackFrameOrigin(additionalStacklevel));
	}

	public static void error(String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ERROR)
			return;
		android.util.Log.e(TAG, String.format(message, args) + getNavigatableStackFrameOrigin());
	}

	public static void error(Throwable ex, String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ERROR)
			return;
		android.util.Log.e(TAG, String.format(message, args) + getNavigatableStackFrameOrigin(), ex);
	}

	public static void error(Throwable ex) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ERROR)
			return;
		android.util.Log.e(TAG, "" + getNavigatableStackFrameOrigin(), ex);
	}

	public static void critical(String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ASSERT)
			return;
		android.util.Log.wtf(TAG, String.format(message, args) + getNavigatableStackFrameOrigin());
		Gdx.app.exit();
	}

	public static void critical(Throwable ex, String message, Object... args) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ASSERT)
			return;
		android.util.Log.wtf(TAG, String.format(message, args) + getNavigatableStackFrameOrigin(), ex);
		Gdx.app.exit();
	}

	public static void critical(Throwable ex) {
		if (MINIMUM_LOG_LEVEL > android.util.Log.ASSERT)
			return;
		android.util.Log.wtf(TAG, "" + getNavigatableStackFrameOrigin(), ex);
		Gdx.app.exit();
	}
}
