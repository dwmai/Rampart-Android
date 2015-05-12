package game;

import java.util.HashMap;

public enum EAIDifficulty {
	aiEasy(0), // easy ai
	aiNormal(1), // normal ai
	aiHard(2), // hard ai
	aiInsane(3); // highest difficulty ai
	private static HashMap<Integer, EAIDifficulty> mappings;
	private int intValue;

	private EAIDifficulty(int value) {
		intValue = value;
		EAIDifficulty.getMappings().put(value, this);
	}

	private synchronized static HashMap<Integer, EAIDifficulty> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Integer, EAIDifficulty>();
		}
		return mappings;
	}

	public static EAIDifficulty forValue(int value) {
		return getMappings().get(value);
	}

	public int getValue() {
		return intValue;
	}
}
