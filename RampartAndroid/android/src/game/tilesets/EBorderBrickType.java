package game.tilesets;

public enum EBorderBrickType {
	bbtTopCenter(0),
	bbtTopRight(1),
	bbtRight0(2),
	bbtRight1(3),
	bbtBottomRight(4),
	bbtBottomCenter(5),
	bbtBottomLeft(6),
	bbtLeft0(7),
	bbtLeft1(8),
	bbtTopLeft(9),
	bbtSingle(10),
	bbtMax(11);
	private static java.util.HashMap<Integer, EBorderBrickType> mappings;
	private int intValue;

	private EBorderBrickType(int value) {
		intValue = value;
		EBorderBrickType.getMappings().put(value, this);
	}

	private synchronized static java.util.HashMap<Integer, EBorderBrickType> getMappings() {
		if (mappings == null) {
			mappings = new java.util.HashMap<Integer, EBorderBrickType>();
		}
		return mappings;
	}

	public static EBorderBrickType forValue(int value) {
		return getMappings().get(value);
	}

	public int getValue() {
		return intValue;
	}
}
