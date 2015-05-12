package game;

import game.tilesets.EConstructionTileType;

/**
 * @brief A tile in a construction map with a type and number of hits taken
 */
public class CConstructionTile {

	/**
	 * @brief The type of this tile
	 */
	public EConstructionTileType DType;
	/**
	 * @brief The number of hits tile has taken
	 */
	public int DHitsTaken;

	public CConstructionTile() {
		Reset();
	}

	/**
	 * @brief Resets hits and sets to no construction
	 */
	public final void Reset() {
		DHitsTaken = 0;
		DType = EConstructionTileType.cttNone;
	}

	/**
	 * @brief Returns if the type is a floor tile
	 * @return true if floor, otherwise false
	 */
	public final boolean IsFloor() {
		return (EConstructionTileType.cttBlueFloor == DType) || (EConstructionTileType.cttRedFloor == DType)
				|| (EConstructionTileType.cttYellowFloor == DType);
	}

	/**
	 * @brief Returns if type is a damaged floor
	 * @return true if damaged floor, otherwise false
	 */
	public final boolean IsFloorDamaged() {
		return (EConstructionTileType.cttBlueFloorDamaged == DType)
				|| (EConstructionTileType.cttRedFloorDamaged == DType)
				|| (EConstructionTileType.cttYellowFloorDamaged == DType);
	}

	/**
	 * @brief Returns if type is a wall
	 * @return true if wall, otherwise false
	 */
	public final boolean IsWall() {
		return (EConstructionTileType.cttBlueWall == DType) || (EConstructionTileType.cttRedWall == DType)
				|| (EConstructionTileType.cttYellowWall == DType);
	}

	/**
	 * @brief Returns if type is a damaged wall
	 * @return true if damaged wall, otherwise false
	 */
	public final boolean IsWallDamaged() {
		return (EConstructionTileType.cttBlueWallDamaged == DType)
				|| (EConstructionTileType.cttRedWallDamaged == DType)
				|| (EConstructionTileType.cttYellowWallDamaged == DType);
	}

	/**
	 * @brief Returns if ground damaged
	 * @return true if damaged ground, otherwise false
	 */
	public final boolean IsGroundDamaged() {
		return (EConstructionTileType.cttGroundDamaged == DType)
				|| (EConstructionTileType.cttBlueFloorDamaged == DType)
				|| (EConstructionTileType.cttRedFloorDamaged == DType)
				|| (EConstructionTileType.cttYellowFloorDamaged == DType);
	}

	/**
	 * @brief Turns wall into damaged wall
	 */
	public final void DestroyWall() {
		switch (GetColor()) {
		case pcBlue:
			DType = EConstructionTileType.cttBlueWallDamaged;
			break;
		case pcRed:
			DType = EConstructionTileType.cttRedWallDamaged;
			break;
		case pcYellow:
			DType = EConstructionTileType.cttYellowWallDamaged;
			break;
		default:
			DType = EConstructionTileType.cttNone;
			break;
		}
	}

	/**
	 * @brief Gets the color of tile
	 * @return The player color of this tile
	 */
	public final EPlayerColor GetColor() {
		switch (DType) {
		case cttBlueFloor:
		case cttBlueWall:
			return EPlayerColor.pcBlue;

		case cttRedFloor:
		case cttRedWall:
			return EPlayerColor.pcRed;

		case cttYellowFloor:
		case cttYellowWall:
			return EPlayerColor.pcYellow;

		default:
			break;
		}
		return EPlayerColor.pcNone;
	}
}
