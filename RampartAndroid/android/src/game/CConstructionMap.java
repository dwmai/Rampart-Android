package game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import game.tilesets.C3DFloorTileset;
import game.tilesets.C3DTerrainTileset;
import game.tilesets.C3DWallTileset;
import game.tilesets.CWallFloorTileset;
import game.tilesets.EConstructionTileType;
import game.utils.CMathUtil;

public class CConstructionMap {

	/**
	 * @brief Stores the 2D array of construction
	 */
	public java.util.ArrayList<java.util.ArrayList<CConstructionTile>> DTiles = new java.util.ArrayList<java.util.ArrayList<CConstructionTile>>();
	/**
	 * @brief Stores the list of cannons
	 */
	protected java.util.ArrayList<CCannon> DCannons = new java.util.ArrayList<CCannon>();

	public static boolean TileTypeIsFloor(CConstructionTile tile) {
		EConstructionTileType type = tile.DType;
		return (EConstructionTileType.cttBlueFloor == type) || (EConstructionTileType.cttRedFloor == type)
				|| (EConstructionTileType.cttYellowFloor == type);
	}

	public static boolean TileTypeIsFloorDamaged(CConstructionTile tile) {
		EConstructionTileType type = tile.DType;
		return (EConstructionTileType.cttBlueFloorDamaged == type)
				|| (EConstructionTileType.cttRedFloorDamaged == type)
				|| (EConstructionTileType.cttYellowFloorDamaged == type);
	}

	/**
	 * @brief Clears map and cannons, and resizes the construction map for a terrain map
	 * @param map
	 *            Terrain map to match size
	 */
	public final void ResetForMap(CTerrainMap map) {
		DCannons.clear();
		DTiles = new ArrayList<ArrayList<CConstructionTile>>(map.Height());
		for (int i = 0; i < map.Height(); i++) {
			ArrayList<CConstructionTile> tiles = new ArrayList<CConstructionTile>(map.Width());
			for (int j = 0; j < map.Width(); j++) {
				tiles.add(new CConstructionTile());
			}
			DTiles.add(tiles);
		}
	}

	/**
	 * @brief Draws the walls, floors, and cannons in 2D
	 * @param game
	 *            Game to draw in
	 */
	public final void Draw2D(CGame game) {
		CTerrainMap Map = game.GameState().TerrainMap();
		CWallFloorTileset Tileset = game.Resources().DTilesets.DWallFloorTileset;
		Vector2 TileSize = Map.ConvertToScreenPosition(new Vector2(1, 1));
		for (int YIndex = 0, YPos = 0; YIndex < DTiles.size(); YIndex++, YPos += TileSize.y) {
			for (int XIndex = 0, XPos = 0; XIndex < DTiles.get(YIndex).size(); XIndex++, XPos += TileSize.x) {
				boolean IsEven = (((YIndex + XIndex) & 0x1) != 0) ? false : true;
				int WallType = 0xF;
				if (DTiles.get(YIndex).get(XIndex).IsWall()) {
					if (YIndex != 0
							&& (DTiles.get(YIndex - 1).get(XIndex).IsWall() || DTiles.get(YIndex - 1).get(XIndex).IsWallDamaged())) {
						WallType &= 0xE;
					}
					if ((XIndex + 1 < DTiles.get(YIndex).size())
							&& (DTiles.get(YIndex).get(XIndex + 1).IsWall() || DTiles.get(YIndex).get(XIndex + 1).IsWallDamaged())) {
						WallType &= 0xD;
					}
					if ((YIndex + 1 < DTiles.size())
							&& (DTiles.get(YIndex + 1).get(XIndex).IsWall() || DTiles.get(YIndex + 1).get(XIndex).IsWallDamaged())) {
						WallType &= 0xB;
					}
					if (XIndex != 0
							&& (DTiles.get(YIndex).get(XIndex - 1).IsWall() || DTiles.get(YIndex).get(XIndex - 1).IsWallDamaged())) {
						WallType &= 0x7;
					}
				}

				switch (DTiles.get(YIndex).get(XIndex).DType) {
				case cttBlueWall:
					Tileset.Draw2DWallTile(game, XPos, YPos, EPlayerColor.pcBlue.getValue(), WallType);
					break;
				case cttRedWall:
					Tileset.Draw2DWallTile(game, XPos, YPos, EPlayerColor.pcRed.getValue(), WallType);
					break;
				case cttYellowWall:
					Tileset.Draw2DWallTile(game, XPos, YPos, EPlayerColor.pcYellow.getValue(), WallType);
					break;
				case cttBlueFloor:
				case cttBlueFloorDamaged:
					Tileset.Draw2DFloorTile(game, XPos, YPos, IsEven ? EPlayerColor.pcNone : EPlayerColor.pcBlue);
					break;
				case cttRedFloor:
				case cttRedFloorDamaged:
					Tileset.Draw2DFloorTile(game, XPos, YPos, IsEven ? EPlayerColor.pcNone : EPlayerColor.pcRed);
					break;
				case cttYellowFloor:
				case cttYellowFloorDamaged:
					Tileset.Draw2DFloorTile(game, XPos, YPos, IsEven ? EPlayerColor.pcNone : EPlayerColor.pcYellow);
					break;
				default:
					break;
				}
				if (DTiles.get(YIndex).get(XIndex).IsGroundDamaged()) {
					game.Resources().DTilesets.D2DTerrainTileset.DrawDamagedGroundTile(game, new Vector2(XPos, YPos));
				}
			}
		}

		Draw2DCannons(game);
	}

	/**
	 * @brief Draws the cannons in 2D
	 * @param game
	 *            Game to draw in
	 */
	public final void Draw2DCannons(CGame game) {
		for (CCannon cannon : DCannons) {
			cannon.Draw2D(game);
		}
	}

	/**
	 * @brief Draws the walls and floors in 3D
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw3D(CGame game, int XIndex, int YIndex, int XPos, int YPos) {

		// Draw3DFloor(game);
		// Draw3DWalls(game);
	}

	/**
	 * @brief Draws the floors in 3D
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw3DFloor(CGame game, int XIndex, int YIndex, int XPos, int YPos) {
		C3DFloorTileset FloorTileset = game.Resources().DTilesets.D3DFloorTileset;

		/**
		 * For each tile, check whether tile should be a wall or floor, and if they are damaged, then draw that tile.
		 */
		switch (DTiles.get(YIndex).get(XIndex).DType) {
		case cttBlueWall:
		case cttBlueWallDamaged:
		case cttRedWall:
		case cttRedWallDamaged:
		case cttYellowWall:
		case cttYellowWallDamaged:
			if (YIndex + 1 < DTiles.size()) {
				if (!DTiles.get(YIndex + 1).get(XIndex).IsFloor()) {
					break;
				}
			} else {
				break;
			}
		case cttBlueFloor:
		case cttBlueFloorDamaged:
		case cttRedFloor:
		case cttRedFloorDamaged:
		case cttYellowFloor:
		case cttYellowFloorDamaged:
			FloorTileset.Draw3DFloorTile(game, new Vector2(XPos, YPos + FloorTileset.TileHeight()),
					game.GameState().TerrainMap().TileType(XIndex, YIndex));
			break;
		default:
			break;
		}
	}

	/**
	 * @brief Draws the walls in 3D
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw3DWalls(CGame game, int XIndex, int YIndex, int XPos, int YPos) {
		C3DWallTileset WallTileset = game.Resources().DTilesets.D3DWallTileset;
		C3DTerrainTileset TerrainTileset = game.Resources().DTilesets.D3DTerrainTileset;
		// For each tile, determine WallType, and WallOffset, and call appropriate draw function, if
		// the tile is not part of a fortress/castle, then draw the appropriate tile, i.e. grass/water/
		// smoke, etc.

		int WallType = 0xF;
		int WallOffset = 0x0;
		if (TileTypeIsWall(DTiles.get(YIndex).get(XIndex))) {
			if (YIndex != 0
					&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex)) || TileTypeIsWallDamaged(DTiles.get(
							YIndex - 1).get(XIndex)))) {
				WallType &= 0xE;
			}
			if ((XIndex + 1 < DTiles.get(YIndex).size())
					&& (TileTypeIsWall(DTiles.get(YIndex).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(YIndex).get(
							XIndex + 1)))) {
				WallType &= 0xD;
			}
			if ((YIndex + 1 < DTiles.size())
					&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex)) || TileTypeIsWallDamaged(DTiles.get(
							YIndex + 1).get(XIndex)))) {
				WallType &= 0xB;
			}
			if (XIndex != 0
					&& (TileTypeIsWall(DTiles.get(YIndex).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(YIndex).get(
							XIndex - 1)))) {
				WallType &= 0x7;
			}
			switch (WallType) {
			case 0:
				WallOffset = 0xF;
				if (YIndex != 0) {
					if ((XIndex + 1 < DTiles.get(YIndex).size())
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex + 1)))) {
						WallOffset &= 0xE;
					}
					if (XIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex - 1)))) {
						WallOffset &= 0x7;
					}
				}
				if (YIndex + 1 < DTiles.size()) {
					if ((XIndex + 1 < DTiles.get(YIndex).size())
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex + 1)))) {
						WallOffset &= 0xD;
					}
					if (XIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex - 1)))) {
						WallOffset &= 0xB;
					}
				}
				break;
			case 1:
				WallOffset = 0x3;
				if (YIndex + 1 < DTiles.size()) {
					if ((XIndex + 1 < DTiles.get(YIndex).size())
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex + 1)))) {
						WallOffset &= 0x2;
					}
					if (XIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex - 1)))) {
						WallOffset &= 0x1;
					}
				}
				break;
			case 2:
				WallOffset = 0x3;
				if (XIndex != 0) {
					if ((YIndex + 1 < DTiles.size())
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex - 1)))) {
						WallOffset &= 0x2;
					}
					if (YIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex - 1)))) {
						WallOffset &= 0x1;
					}
				}
				break;
			case 3:
				WallOffset = 1;
				if (XIndex != 0
						&& (YIndex + 1 < DTiles.size())
						&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
								YIndex + 1).get(XIndex - 1)))) {
					WallOffset = 0;
				}
				break;
			case 4:
				WallOffset = 0x3;
				if (YIndex != 0) {
					if ((XIndex + 1 < DTiles.get(YIndex).size())
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex + 1)))) {
						WallOffset &= 0x2;
					}
					if (XIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex - 1)))) {
						WallOffset &= 0x1;
					}
				}
				break;
			case 6:
				WallOffset = 1;
				if (XIndex != 0
						&& YIndex != 0
						&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex - 1)) || TileTypeIsWallDamaged(DTiles.get(
								YIndex - 1).get(XIndex - 1)))) {
					WallOffset = 0;
				}
				break;
			case 8:
				WallOffset = 0x3;
				if (XIndex + 1 < DTiles.get(YIndex).size()) {
					if (YIndex != 0
							&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex - 1).get(XIndex + 1)))) {
						WallOffset &= 0x2;
					}
					if ((YIndex + 1 < DTiles.size())
							&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
									YIndex + 1).get(XIndex + 1)))) {
						WallOffset &= 0x1;
					}
				}
				break;
			case 9:
				WallOffset = 1;
				if ((XIndex + 1 < DTiles.get(YIndex).size())
						&& (YIndex + 1 < DTiles.size())
						&& (TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
								YIndex + 1).get(XIndex + 1)))) {
					WallOffset = 0;
				}
				break;
			case 12:
				WallOffset = 1;
				if ((XIndex + 1 < DTiles.get(YIndex).size())
						&& YIndex != 0
						&& (TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex + 1)) || TileTypeIsWallDamaged(DTiles.get(
								YIndex - 1).get(XIndex + 1)))) {
					WallOffset = 0;
				}
				break;
			default:
				WallOffset = 0;
				break;
			}

			switch (DTiles.get(YIndex).get(XIndex).DType) {
			case cttBlueWall:
				WallTileset.Draw3DWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcBlue, WallType, WallOffset);
				break;
			case cttRedWall:
				WallTileset.Draw3DWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcRed, WallType, WallOffset);
				break;
			case cttYellowWall:
				WallTileset.Draw3DWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcYellow, WallType, WallOffset);
				break;
			default:
				break;
			}
		} else if (TileTypeIsWallDamaged(DTiles.get(YIndex).get(XIndex))) {
			if (YIndex != 0 && TileTypeIsWall(DTiles.get(YIndex - 1).get(XIndex))) {
				WallType &= 0xE;
			}
			if ((XIndex + 1 < DTiles.get(YIndex).size()) && TileTypeIsWall(DTiles.get(YIndex).get(XIndex + 1))) {
				WallType &= 0xD;
			}
			if ((YIndex + 1 < DTiles.size()) && TileTypeIsWall(DTiles.get(YIndex + 1).get(XIndex))) {
				WallType &= 0xB;
			}
			if (XIndex != 0 && TileTypeIsWall(DTiles.get(YIndex).get(XIndex - 1))) {
				WallType &= 0x7;
			}
			switch (DTiles.get(YIndex).get(XIndex).DType) {
			case cttBlueWallDamaged:
				WallTileset.Draw3DDamagedWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcBlue, WallType);
				break;
			case cttRedWallDamaged:
				WallTileset.Draw3DDamagedWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcRed, WallType);
				break;
			case cttYellowWallDamaged:
				WallTileset.Draw3DDamagedWallTile(game, new Vector2(XPos, YPos), EPlayerColor.pcYellow, WallType);
				break;
			default:
				break;
			}
		} else if (TileTypeIsFloorGroundDamaged(DTiles.get(YIndex).get(XIndex))) {
			TerrainTileset.DrawDamagedGroundTile(game, new Vector2(XPos, YPos + TerrainTileset.TileHeight()));
		}

	}

	public static boolean TileTypeIsWall(CConstructionTile tile) {
		EConstructionTileType type = tile.DType;
		return (EConstructionTileType.cttBlueWall == type) || (EConstructionTileType.cttRedWall == type)
				|| (EConstructionTileType.cttYellowWall == type);
	}

	public static boolean TileTypeIsWallDamaged(CConstructionTile tile) {
		EConstructionTileType type = tile.DType;
		return (EConstructionTileType.cttBlueWallDamaged == type) || (EConstructionTileType.cttRedWallDamaged == type)
				|| (EConstructionTileType.cttYellowWallDamaged == type);
	}

	public static boolean TileTypeIsFloorGroundDamaged(CConstructionTile tile) {
		EConstructionTileType type = tile.DType;
		return (EConstructionTileType.cttGroundDamaged == type) || (EConstructionTileType.cttBlueFloorDamaged == type)
				|| (EConstructionTileType.cttRedFloorDamaged == type)
				|| (EConstructionTileType.cttYellowFloorDamaged == type);
	}

	/**
	 * @brief Draws the cannons in 3D
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw3DCannons(CGame game, int XPos, int YPos) {
		for (CCannon cannon : DCannons) {
			if (cannon.DPosition.equals(new Vector2(XPos - 1, YPos - 1))) {
				cannon.Draw3D(game);
			}
		}
	}

	/**
	 * @brief Builds walls around a castle, adjusting for obstructions
	 * @param game
	 *            The game playing
	 * @param castle
	 *            The castle to surround
	 */
	public final void SurroundCastle(CGame game, Castle castle) {
		EConstructionTileType ConTileType;
		EConstructionTileType WallType;
		double LRRatio;

		int XPos = (int) castle.IndexPosition().x;
		int YPos = (int) castle.IndexPosition().y;

		if (EPlayerColor.pcBlue == castle.DColor) {
			ConTileType = EConstructionTileType.cttBlueFloor;
			WallType = EConstructionTileType.cttBlueWall;
		} else if (EPlayerColor.pcRed == castle.DColor) {
			ConTileType = EConstructionTileType.cttRedFloor;
			WallType = EConstructionTileType.cttRedWall;
		} else {
			ConTileType = EConstructionTileType.cttYellowFloor;
			WallType = EConstructionTileType.cttYellowWall;
		}

		for (int YOffset = -3; YOffset <= 4; YOffset++) {
			int YI = YPos + YOffset;
			for (int XOffset = -3; XOffset <= 4; XOffset++) {
				int XI = XPos + XOffset;
				int TileType = game.GameState().TerrainMap().TileType(XI, YI).getValue();

				if (TileType == castle.DColor.getValue() && !DTiles.get(YI).get(XI).IsGroundDamaged()) {
					DTiles.get(YI).get(XI).DType = ConTileType;
				}
			}
		}
		for (int YOffset = -3; YOffset <= 4; YOffset++) {
			int YI = YPos + YOffset;
			for (int XOffset = -3; XOffset <= 4; XOffset++) {
				int XI = XPos + XOffset;

				if (ConTileType == DTiles.get(YI).get(XI).DType) {
					boolean MakeWall = false;
					if (YI != 0) {
						if (XI != 0 && (EConstructionTileType.cttNone == DTiles.get(YI - 1).get(XI - 1).DType)) {
							MakeWall = true;
						}
						if (EConstructionTileType.cttNone == DTiles.get(YI - 1).get(XI).DType) {
							MakeWall = true;
						}
						if ((XI + 1 < DTiles.get(YI).size())
								&& (EConstructionTileType.cttNone == DTiles.get(YI - 1).get(XI + 1).DType)) {
							MakeWall = true;
						}
					} else {
						MakeWall = true;
					}
					if (XI != 0) {
						if (EConstructionTileType.cttNone == DTiles.get(YI).get(XI - 1).DType) {
							MakeWall = true;
						}
					} else {
						MakeWall = true;
					}
					if (XI + 1 < DTiles.get(YI).size()) {
						if (EConstructionTileType.cttNone == DTiles.get(YI).get(XI + 1).DType) {
							MakeWall = true;
						}
					} else {
						MakeWall = true;
					}
					if (YI + 1 < DTiles.size()) {
						if (XI != 0 && (EConstructionTileType.cttNone == DTiles.get(YI + 1).get(XI - 1).DType)) {
							MakeWall = true;
						}
						if (EConstructionTileType.cttNone == DTiles.get(YI + 1).get(XI).DType) {
							MakeWall = true;
						}
						if ((XI + 1 < DTiles.get(YI).size())
								&& (EConstructionTileType.cttNone == DTiles.get(YI + 1).get(XI + 1).DType)) {
							MakeWall = true;
						}
					} else {
						MakeWall = true;
					}
					if (MakeWall) {
						DTiles.get(YI).get(XI).DType = WallType;
					}
				}
			}
		}
	}

	/**
	 * @brief Determines if the given rectangle is open in the map for a color
	 * @param player_color
	 *            The color to be open for
	 * @param position
	 *            The top left of the rectangle
	 * @param size
	 *            The size of the rectangle
	 * @return True if space is open, otherwise false
	 */
	public final boolean IsSpaceOpenForColor(EPlayerColor player_color, Vector2 position, Vector2 size) {
		EConstructionTileType ConTileType;

		if (EPlayerColor.pcBlue == player_color) {
			ConTileType = EConstructionTileType.cttBlueFloor;
		} else if (EPlayerColor.pcRed == player_color) {
			ConTileType = EConstructionTileType.cttRedFloor;
		} else {
			ConTileType = EConstructionTileType.cttYellowFloor;
		}
		if (position.x + 1 >= DTiles.get(0).size()) {
			return false;
		}
		if (position.y + 1 >= DTiles.size()) {
			return false;
		}
		for (int YOff = 0; YOff < 2; YOff++) {
			for (int XOff = 0; XOff < 2; XOff++) {
				if (ConTileType != DTiles.get((int) position.y + YOff).get((int) position.x + XOff).DType) {
					return false;
				}
			}
		}

		return IsSpaceFreeOfCannons(new Vector2(position), new Vector2(size));
	}

	/**
	 * @brief Determines if given rectangle does not collide with cannons
	 * @param position
	 *            The top left of the rectangle
	 * @param size
	 *            The size of the rectangle
	 * @return True if no collisions, otherwise false
	 */
	public final boolean IsSpaceFreeOfCannons(Vector2 position, Vector2 size) {
		for (CCannon cannon : DCannons) {
			if (CMathUtil.DoRectanglesOverlap(cannon.DPosition, cannon.CSize, new Vector2(position), new Vector2(size))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @brief Gets the construction tile at the position
	 * @param position
	 *            Index of the tile
	 * @return Reference to tile
	 */
	public final CConstructionTile GetTileAt(Vector2 position) {
        if (position.y < 0 || position.y >= DTiles.size() ||
            position.x < 0 || position.x >= DTiles.get((int) position.y).size())
            return null;

		return DTiles.get((int) position.y).get((int) position.x);
	}

	/**
	 * @brief Getter for tiles of map
	 * @return Reference to 2D array
	 */
	public final java.util.ArrayList<java.util.ArrayList<CConstructionTile>> Tiles() {
		return DTiles;
	}

	/**
	 * @brief Getter for cannons of map
	 * @return Reference to cannon list
	 */
	public final java.util.ArrayList<CCannon> Cannons() {
		return DCannons;
	}
}
