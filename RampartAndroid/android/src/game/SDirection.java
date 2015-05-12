package game;

/**
 * @brief Holds enumeration for directions
 */
public class SDirection {

	/**
	 * @brief Enum for directions
	 */
	public enum EValue {
		dNorth(0),
		dNorthEast(1),
		dEast(2),
		dSouthEast(3),
		dSouth(4),
		dSouthWest(5),
		dWest(6),
		dNorthWest(7),
		dMax(8);
		private static java.util.HashMap<Integer, EValue> mappings;
		private int intValue;

		private EValue(int value) {
			intValue = value;
			EValue.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EValue> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EValue>();
			}
			return mappings;
		}

		public static EValue forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}
}
