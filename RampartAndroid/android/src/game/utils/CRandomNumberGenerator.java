package game.utils;

/**
 * CRandomNumberGenerator class, used to generate a random number
 */
public class CRandomNumberGenerator {

	/**
	 * This is the seed that generates the upper half of the random number.
	 */
	protected int DRandomSeedHigh;
	/**
	 * This is the seed that generates the lower half of the random number.
	 */
	protected int DRandomSeedLow;

	/**
	 * The default constructor just initializes DRandomSeedHigh and DRandomSeedLow to fixed hex values.
	 */
	public CRandomNumberGenerator() {
		DRandomSeedHigh = 0x01234567;
		DRandomSeedLow = 0x89ABCDEF;
	}

	/**
	 * Generates a random number using the 2 seeds. This sets DRandomSeedHigh and DrandomseedLow.
	 * 
	 * @param low
	 *            the new value of DramdomseedLow
	 * @param high
	 *            the new value of DrandomseedHigh Low and high must not be the same value, and they must both exist
	 */
	public final void Seed(int high, int low) {
		if ((high != low) && low != 0 && high != 0) {
			DRandomSeedHigh = high;
			DRandomSeedLow = low;
		}
	}

	/**
	 * Changes the value of DRandomHigh and DrandomLow. Returns an int where DRandomSeedHigh affects the upper 16 bits
	 * and DRandomlow affects all 32 bits
	 */
	public final int Random() {

		/**
		 * multiplies the lower 16 digits by a set number (different for low and high) adds this to the upper 16 digits
		 * shifted right by 16 bit
		 */
		DRandomSeedHigh = 36969 * (DRandomSeedHigh & 65535) + (DRandomSeedHigh >> 16);
		DRandomSeedLow = 18000 * (DRandomSeedLow & 65535) + (DRandomSeedLow >> 16);
		int value = (DRandomSeedHigh << 16) + DRandomSeedLow;
        return Math.abs(value);
	}
}
