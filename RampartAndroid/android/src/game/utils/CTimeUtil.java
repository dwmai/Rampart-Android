package game.utils;

/**
 * Holds routines for dealing with time
 */
public class CTimeUtil {

	/**
	 * @brief Calculates the timeout for seconds in the future
	 * @param seconds
	 *            Number of second to count down
	 * @return Timeout seconds into the future
	 */
	public static Timeval MakeTimeoutSecondsInFuture(int seconds) {
		Timeval NewTimeout = new Timeval();
		NewTimeout.seconds += seconds;
		NewTimeout.microseconds += seconds * 1000 * 1000;
		return NewTimeout;
	}

	/**
	 * Calculates the number of seconds until stage timeout
	 * 
	 * @param deadline
	 *            time of when stage will time out
	 * @return number of seconds until timeout
	 */
	public static int SecondsUntilDeadline(Timeval deadline) {
		Timeval CurrentTime = new Timeval();
		return (int) ((deadline.microseconds) - (CurrentTime.microseconds)) / 1000 / 1000;
	}

	/**
	 * Calculates the number of milliseconds until stage timeout
	 * 
	 * @param deadline
	 *            time of when stage will time out
	 * @return number of milliseconds until timeout
	 */
	public static int MilliSecondsUntilDeadline(Timeval deadline) {
		Timeval CurrentTime = new Timeval();
		return (int) ((deadline.microseconds) - (CurrentTime.microseconds)) / 1000;
	}
}
