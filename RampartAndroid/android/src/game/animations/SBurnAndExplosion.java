package game.animations;


/**
 * @brief Holds enumeration for directions
 */
public class SBurnAndExplosion {

	/**
	 * @brief Enum for Explosions
	 */
	public enum EExplosionType {
		etWallExplosion0(0),
		etWallExplosion1(1),
		etWaterExplosion0(2),
		etWaterExplosion1(3),
		etGroundExplosion0(4),
		etGroundExplosion1(5),
		etMax(6);
		private static java.util.HashMap<Integer, EExplosionType> mappings;
		private int intValue;

		private EExplosionType(int value) {
			intValue = value;
			EExplosionType.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EExplosionType> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EExplosionType>();
			}
			return mappings;
		}

		public static EExplosionType forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}

	/**
	 * @brief Enum for Burns
	 */
	public enum EBurnType {
		btRubbleBurn0(0),
		btRubbleBurn1(1),
		btHoleBurn0(2),
		btHoleBurn1(3),
		btMax(4);
		private static java.util.HashMap<Integer, EBurnType> mappings;
		private int intValue;

		private EBurnType(int value) {
			intValue = value;
			EBurnType.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EBurnType> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EBurnType>();
			}
			return mappings;
		}

		public static EBurnType forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}

	/**
	 * @brief Enum for Burns
	 */
	public enum EAnimationType {
		atBurn(0),
		atExplosion(1),
		atPlume(2);
		private static java.util.HashMap<Integer, EAnimationType> mappings;
		private int intValue;

		private EAnimationType(int value) {
			intValue = value;
			EAnimationType.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EAnimationType> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EAnimationType>();
			}
			return mappings;
		}

		public static EAnimationType forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}
}
