package game.tilesets;

public enum EConstructionTileType {
	cttNone,
	cttBlueFloor,
	cttBlueWall,
	cttBlueWallDamaged,
	cttRedFloor,
	cttRedWall,
	cttRedWallDamaged,
	cttYellowFloor,
	cttYellowWall,
	cttYellowWallDamaged,
	cttGroundDamaged,
	cttBlueFloorDamaged,
	cttRedFloorDamaged,
	cttYellowFloorDamaged;

	public static EConstructionTileType forValue(int value) {
		return values()[value];
	}

	public int getValue() {
		return this.ordinal();
	}
}
