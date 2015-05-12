package game;

import java.util.HashMap;

//Enum for player colors
public enum EPlayerColor {
	pcNone(0), // No players
	pcBlue(1), // Player color: Blue
	pcRed(2), // Player color: Red
	pcYellow(3), // Player color: Yellow
	pcMax(4); // Max player count
	private static HashMap<Integer, EPlayerColor> mappings;
	private int intValue;

	private EPlayerColor(int value) {
		intValue = value;
		EPlayerColor.getMappings().put(value, this);
	}

	private synchronized static HashMap<Integer, EPlayerColor> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Integer, EPlayerColor>();
		}
		return mappings;
	}

	public static EPlayerColor forValue(int value) {
		return getMappings().get(value);
	}

	public int getValue() {
		return intValue;
	}
}
