package game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import game.players.CPlayer;
import game.tilesets.CWallFloorTileset;
import game.tilesets.EConstructionTileType;
import game.utils.ArrayUtil;

/*  Replacement Regex
 Find: (DLocations\..et\(\d+\s*,\s*)new ArrayList\((\d+)\)\);
 Replace: DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>($2), $2));
 */
//*
/**
 * WallShape class
 */
public class CWallShape {

	protected java.util.ArrayList<java.util.ArrayList<Boolean>> DLocations = new java.util.ArrayList<java.util.ArrayList<Boolean>>(); // !<
																																		// Polyomino
																																		// square
																																		// locations
	protected int DOrientation; // !< Polyomino orientation

	/**
	 * WallShape constructor.
	 */
	public CWallShape() {
		Randomize(0);
	}

	/**
	 * Sets type and orientation of polyomino.
	 * 
	 * @param val
	 *            Seed value
	 */
	public final void Randomize(int val) {
		int PolyominoType = val % 5;

		val /= 5;
		DOrientation = val % 4;
		val /= 4;
		if (0 == PolyominoType) {
			// monomino
			DLocations = new ArrayList<ArrayList<Boolean>>(1);
			DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(1), 1));
			DLocations.get(0).set(0, true);
		} else if (1 == PolyominoType) {
			// domino
			DLocations = new ArrayList<ArrayList<Boolean>>(1);
			DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(2), 2));
			DLocations.get(0).set(0, true);
			DLocations.get(0).set(1, true);
		} else if (2 == PolyominoType) {
			// tromino
			if ((val & 0x1) != 0) {
				// XX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(1);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(2), 2));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(2), 2));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, false);
			} else {
				// XXX
				DLocations = new ArrayList<ArrayList<Boolean>>(1);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
			}
		} else if (3 == PolyominoType) {
			// tetromino
			switch (val % 7) {
			case 0: // XXXX
				DLocations = new ArrayList<ArrayList<Boolean>>(1);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				break;
			case 1: // XX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(2), 2));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(2), 2));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				break;
			case 2: // XXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				break;
			case 3: // XXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, true);
				break;
			case 4: // XXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, false);
				break;
			case 5: // XX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				break;
			default: // XX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, false);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, true);
				break;
			}
		} else {
			// pentomino
			switch (val % 18) {
			case 0: // XXXXX
				DLocations = new ArrayList<ArrayList<Boolean>>(1);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(5), 5));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(0).set(4, true);
				break;
			case 1: // XX
				// XX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, false);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, false);
				break;
			case 2: // XX
				// XX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, false);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, true);
				DLocations.get(2).set(0, false);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, false);
				break;
			case 3: // XXXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, false);
				DLocations.get(1).set(3, true);
				break;
			case 4: // XXXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, false);
				DLocations.get(1).set(3, false);
				break;
			case 5: // XXX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, true);
				break;
			case 6: // XXX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				break;
			case 7: // XXX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, false);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, true);
				DLocations.get(1).set(3, true);
				break;
			case 8: // XXX
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(1).set(3, false);
				break;
			case 9: // XXX
				// X
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, false);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, false);
				break;
			case 10: // XXX
				// X X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, true);
				break;
			case 11: // XXX
				// X
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, true);
				DLocations.get(2).set(1, false);
				DLocations.get(2).set(2, false);
				break;
			case 12: // XX
				// XX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, true);
				DLocations.get(2).set(1, false);
				DLocations.get(2).set(2, false);
				break;
			case 13: // X
				// XXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, false);
				DLocations.get(1).set(0, true);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, true);
				DLocations.get(2).set(0, false);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, false);
				break;
			case 14: // XXXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(1).set(3, false);
				break;
			case 15: // XXXX
				// X
				DLocations = new ArrayList<ArrayList<Boolean>>(2);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(4), 4));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(0).set(3, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, false);
				DLocations.get(1).set(2, true);
				DLocations.get(1).set(3, false);
				break;
			case 16: // XX
				// X
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, false);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, true);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, true);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, false);
				break;
			default: // XX
				// X
				// XX
				DLocations = new ArrayList<ArrayList<Boolean>>(3);
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.add(ArrayUtil.resize(new ArrayList<Boolean>(3), 3));
				DLocations.get(0).set(0, true);
				DLocations.get(0).set(1, true);
				DLocations.get(0).set(2, false);
				DLocations.get(1).set(0, false);
				DLocations.get(1).set(1, true);
				DLocations.get(1).set(2, false);
				DLocations.get(2).set(0, false);
				DLocations.get(2).set(1, true);
				DLocations.get(2).set(2, true);
				break;
			}
		}
	}

	/**
	 * WallShape destructor.
	 */
	public void dispose() {

	}

	/**
	 * Changes polyomino orientation.
	 */
	public final void Rotate() {
		DOrientation++;
		DOrientation &= 0x3;
	}

	/**
	 * @brief Draws wall shape for player
	 * @param game
	 *            Game drawing
	 * @param player
	 *            Player drawing for
	 * @param tile_position
	 *            Position drawing at
	 */
	public final void Draw(CGame game, CPlayer owner, Vector2 tile_position) {
		int XTile;
		int YTile;
		int WallIndexEven;
		int WallIndexOdd;
		CWallFloorTileset Tileset = game.Resources().DTilesets.DWallFloorTileset;
		CTerrainMap TerrainMap = game.GameState().TerrainMap();

		Vector2 BoundedPosition = GetBoundedPosition(game, new Vector2(tile_position));
		XTile = (int) BoundedPosition.x;
		YTile = (int) BoundedPosition.y;
		if (CanBePlaced(game, owner, new Vector2(BoundedPosition))) {
			// TODO: Changed Linux source code
			WallIndexEven = EPlayerColor.pcMax.getValue() + 1;
			WallIndexOdd = EPlayerColor.pcMax.getValue();
		} else {
			WallIndexEven = WallIndexOdd = EPlayerColor.pcNone.getValue();
		}

		// Determine whether there is a block at the given position.
		for (int WallYPos = 0; WallYPos < Height(); WallYPos++) {
			for (int WallXPos = 0; WallXPos < Width(); WallXPos++) {
				if (IsBlock(WallXPos, WallYPos)) {
					int WallType = 0xF;
					boolean IsEven = (((WallYPos + WallXPos) & 0x1) != 0) ? false : true;

					if (WallYPos != 0 && IsBlock(WallXPos, WallYPos - 1)) {
						WallType &= 0xE;
					}
					if ((WallXPos + 1 < Width()) && IsBlock(WallXPos + 1, WallYPos)) {
						WallType &= 0xD;
					}
					if ((WallYPos + 1 < Height()) && IsBlock(WallXPos, WallYPos + 1)) {
						WallType &= 0xB;
					}
					if (WallXPos != 0 && IsBlock(WallXPos - 1, WallYPos)) {
						WallType &= 0x7;
					}
					Tileset.Draw2DWallTile(game, (XTile + WallXPos) * Tileset.TileWidth(),
							(YTile + WallYPos) * Tileset.TileHeight(), IsEven ? WallIndexEven : WallIndexOdd, WallType);
				}
			}
		}
	}

	/**
	 * @brief Calculates the position bounded by the map
	 * @param game
	 *            Game updating
	 * @param tile_position
	 *            Position trying to plae
	 * @return The position after bounding to the map
	 */
	public final Vector2 GetBoundedPosition(CGame game, Vector2 tile_position) {
		int XTile;
		int YTile;
		CTerrainMap TerrainMap = game.GameState().TerrainMap();
		XTile = (int) tile_position.x;
		YTile = (int) tile_position.y;
		if (XTile + Width() >= TerrainMap.Width()) {
			XTile = TerrainMap.Width() - Width();
		}
		if (YTile + Height() >= TerrainMap.Height()) {
			YTile = TerrainMap.Height() - Height();
		}
		return new Vector2(XTile, YTile);
	}

	/**
	 * @brief Determines if wall shape can be placed by player
	 * @param game
	 *            Game updating
	 * @param player
	 *            Player placing shape
	 * @param tile_position
	 *            Position to place at
	 * @return true if can be placed, false otherwise
	 */
	public final boolean CanBePlaced(CGame game, CPlayer player, Vector2 tile_position) {
		CTerrainMap TerrainMap = game.GameState().TerrainMap();
		CConstructionMap ConstructionMap = game.GameState().ConstructionMap();
        CUnits Units = game.GameState().Units();
		int XPos;
		int YPos;
		for (int WallYPos = 0; WallYPos < Height(); WallYPos++) {
			YPos = (int) tile_position.y + WallYPos;
			if (YPos >= TerrainMap.Height()) {
				return false;
			}
			for (int WallXPos = 0; WallXPos < Width(); WallXPos++) {
				XPos = (int) tile_position.x + WallXPos;
				if (XPos >= TerrainMap.Width()) {
					return false;
				}
				if (IsBlock(WallXPos, WallYPos)) {
					if (player.DColor != TerrainMap.TileType(XPos, YPos)) {
						return false;
					}
					CConstructionTile ConstructionTile = ConstructionMap.GetTileAt(new Vector2(XPos, YPos));
					if (ConstructionTile.IsWall()) {
						return false;
					}
					if (ConstructionTile.IsFloor()) {
						return false;
					}
					if (ConstructionTile.IsFloorDamaged()) {
						return false;
					}
					if (ConstructionTile.IsGroundDamaged()) {
						return false;
					}
					if (!ConstructionMap.IsSpaceFreeOfCannons(new Vector2(XPos, YPos), new Vector2(1, 1))) {
						return false;
					}
					if (!TerrainMap.IsSpaceOpen(new Vector2(XPos, YPos), new Vector2(1, 1))) {
						return false;
					}
                    if (!Units.IsSpaceFreeOfSiege(game, new Vector2(XPos, YPos), new Vector2(1, 1))) {
                        return false;
                    }
				}
			}
		}
		return true;
	}

	/**
	 * Gets height of polyomino.
	 */
	public final int Height() {
		if ((DOrientation & 0x01) != 0) {
			return DLocations.get(0).size();
		}
		return DLocations.size();
	}

	/**
	 * Gets width of polyomino.
	 */
	public final int Width() {
		if ((DOrientation & 0x01) != 0) {
			return DLocations.size();
		}
		return DLocations.get(0).size();
	}

	/**
	 * Determines if part of the polyomino is contained at the position specified.
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @returns True if position contains part of polyomino, false if it does not
	 */
	public final boolean IsBlock(int x, int y) {
		int LookupX;
		int LookupY;

		switch (DOrientation) {
		case 1: // 90 degree rotation to the left
			LookupX = y;
			LookupY = DLocations.size() - 1 - x;
			break;
		case 2: // 180 degree rotation
			LookupX = DLocations.get(0).size() - 1 - x;
			LookupY = DLocations.size() - 1 - y;
			break;
		case 3: // 90 degree rotation to the right
			LookupX = DLocations.get(0).size() - 1 - y;
			LookupY = x;
			break;
		default: // normal orientation
			LookupX = x;
			LookupY = y;
			break;
		}

        if (LookupY >= 0 && LookupY < DLocations.size() &&
            LookupX >= 0 && LookupX < DLocations.get(LookupY).size())
		    return DLocations.get(LookupY).get(LookupX);
        else
            return false;
	}

	/**
	 * @brief Places wall shape into construction map
	 * @param game
	 *            Game updating
	 * @param player
	 *            Player placing shape
	 * @param tile_position
	 *            Position placing shape
	 * @return
	 */
	public final boolean Place(CGame game, CPlayer player, Vector2 tile_position) {
		CConstructionMap ConstructionMap = game.GameState().ConstructionMap();
		int XTile;
		int YTile;
		int XPos;
		int YPos;
		Vector2 BoundedPosition = GetBoundedPosition(game, new Vector2(tile_position));
		XTile = (int) BoundedPosition.x;
		YTile = (int) BoundedPosition.y;
		EPlayerColor RequiredTileType = player.DColor;
		EConstructionTileType WallType;
		if (EPlayerColor.pcBlue == player.DColor) {
			WallType = EConstructionTileType.cttBlueWall;
		} else if (EPlayerColor.pcRed == player.DColor) {
			WallType = EConstructionTileType.cttRedWall;
		} else {
			WallType = EConstructionTileType.cttYellowWall;
		}
		if (CanBePlaced(game, player, new Vector2(BoundedPosition))) {
			for (int WallYPos = 0; WallYPos < Height(); WallYPos++) {
				YPos = YTile + WallYPos;
				for (int WallXPos = 0; WallXPos < Width(); WallXPos++) {
					XPos = XTile + WallXPos;
					if (IsBlock(WallXPos, WallYPos)) {
						ConstructionMap.GetTileAt(new Vector2(XPos, YPos)).DType = WallType;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
