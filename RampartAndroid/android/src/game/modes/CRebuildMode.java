package game.modes;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.Stack;

import game.CConstructionMap;
import game.CGame;
import game.CSiegeWeapons;
import game.CTerrainMap;
import game.CTimer;
import game.Castle;
import game.players.CPlayer;
import game.sounds.CSounds;
import game.tilesets.EConstructionTileType;
import game.utils.CTimeUtil;

/**
 * @brief Mode where players build walls to surround castles
 */
public class CRebuildMode extends CMapMode {
    protected boolean DSentGameOver;

    public CRebuildMode() {
        DSentGameOver = false;
    }


	/**
	 * @brief Sets up timer and updates surrounded castles
	 * @param game
	 *            Game entering
	 */
	public void Enter(CGame game) {
		CheckSurroundedCastles(game);
		super.Enter(game);
		CTimer Timer = game.GameState().DTimer;
		Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(15);
		Timer.DIsVisible = true;
		Timer.DIsAudible = true;
        game.Resources().DSounds.SwitchSong(CSounds.ESongType.stRebuild, 1.0f);
	}

	/**
    *
    * @brief Update surrounded castles
    *
    * @param game Game updating

    */
    public void WillEnter(CGame game) {
        CheckSurroundedCastles(game);
    }

	/**
	 * @brief Updates which castles are surrounded and updates the construction map
	 * @param game
	 *            Game updating
	 */
	protected final void CheckSurroundedCastles(CGame game) {
		CTerrainMap TerrainMap = game.GameState().TerrainMap();
		CConstructionMap ConstructionMap = game.GameState().ConstructionMap();
		// Check surrounding
		for (int YPos = 0; YPos < TerrainMap.Height(); YPos++) {
			for (int XPos = 0; XPos < TerrainMap.Width(); XPos++) {
				if (!(ConstructionMap.Tiles().get(YPos).get(XPos).IsWall())) {
					if ((ConstructionMap.Tiles().get(YPos).get(XPos).IsFloorDamaged() || ConstructionMap.Tiles().get(
							YPos).get(XPos).IsGroundDamaged())) {
						switch (TerrainMap.TileType(XPos, YPos)) {
						case pcBlue:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttBlueFloorDamaged;
							break;
						case pcRed:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttRedFloorDamaged;
							break;
						case pcYellow:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttYellowFloorDamaged;
							break;
						default:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttNone;
							break;
						}
					} else {
						switch (TerrainMap.TileType(XPos, YPos)) {
						case pcBlue:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttBlueFloor;
							break;
						case pcRed:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttRedFloor;
							break;
						case pcYellow:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttYellowFloor;
							break;
						default:
							ConstructionMap.DTiles.get(YPos).get(XPos).DType = EConstructionTileType.cttNone;
							break;
						}
					}
				}
			}
		}
		for (int YPos = 0; YPos < TerrainMap.Height(); YPos++) {
			for (int XPos = 0; XPos < TerrainMap.Width(); XPos++) {
				if (!(ConstructionMap.Tiles().get(YPos).get(XPos).IsWall())) {
					if (0 == XPos) {
						ExpandUnclaimed(game, XPos, YPos);
					} else if (0 == YPos) {
						ExpandUnclaimed(game, XPos, YPos);
					} else if (XPos + 1 == TerrainMap.Width()) {
						ExpandUnclaimed(game, XPos, YPos);
					} else if (YPos + 1 == TerrainMap.Height()) {
						ExpandUnclaimed(game, XPos, YPos);
					} else if (EConstructionTileType.cttNone == ConstructionMap.Tiles().get(YPos).get(XPos).DType) {
						ExpandUnclaimed(game, XPos, YPos);
					}
				}
			}
		}

		for (Castle castle : TerrainMap.Castles()) {
			castle.DSurrounded = ConstructionMap.GetTileAt(castle.IndexPosition()).IsFloor();
		}
	}

	/**
	 * @brief Used when checking surrounded castles
	 * @param game
	 *            Game updating
	 * @param xpos
	 *            X position to expand
	 * @param ypos
	 *            Y position to expand
	 */
	protected final void ExpandUnclaimed(CGame game, int xpos, int ypos) {
		CTerrainMap TerrainMap = game.GameState().TerrainMap();
		CConstructionMap ConstructionMap = game.GameState().ConstructionMap();
		boolean NValid;
		boolean EValid;
		boolean SValid;
		boolean WValid;

		Stack<GridPoint2> callStack = new Stack<GridPoint2>();
		// HashSet<GridPoint2> visitedLocations = new HashSet<GridPoint2>(DefineConstants.GAME_WIDTH *
		// DefineConstants.GAME_HEIGHT);

		callStack.add(new GridPoint2(xpos, ypos));

		while (!callStack.empty()) {

			GridPoint2 locationToCheck = callStack.pop();
			xpos = locationToCheck.x;
			ypos = locationToCheck.y;

			// if (visitedLocations.contains(locationToCheck))
			// continue;
			// else
			// visitedLocations.add(locationToCheck);

			if ((ConstructionMap.Tiles().get(ypos).get(xpos).IsGroundDamaged())) {
				ConstructionMap.Tiles().get(ypos).get(xpos).DType = EConstructionTileType.cttGroundDamaged;
			} else {
				ConstructionMap.Tiles().get(ypos).get(xpos).DType = EConstructionTileType.cttNone;
			}
			NValid = 0 < ypos;
			WValid = 0 < xpos;
			SValid = ypos + 1 < TerrainMap.Height();
			EValid = xpos + 1 < TerrainMap.Width();

			if (NValid) {
				if (WValid) {
					if ((ConstructionMap.Tiles().get(ypos - 1).get(xpos - 1).IsFloor())
							|| (ConstructionMap.Tiles().get(ypos - 1).get(xpos - 1).IsFloorDamaged())) {
						callStack.add(new GridPoint2(xpos - 1, ypos - 1));
					}
				}
				if ((ConstructionMap.Tiles().get(ypos - 1).get(xpos).IsFloor())
						|| (ConstructionMap.Tiles().get(ypos - 1).get(xpos).IsFloorDamaged())) {
					callStack.add(new GridPoint2(xpos, ypos - 1));
				}
				if (EValid) {
					if ((ConstructionMap.Tiles().get(ypos - 1).get(xpos + 1).IsFloor())
							|| (ConstructionMap.Tiles().get(ypos - 1).get(xpos + 1).IsFloorDamaged())) {
						callStack.add(new GridPoint2(xpos + 1, ypos - 1));
					}
				}
			}
			if (WValid) {
				if ((ConstructionMap.Tiles().get(ypos).get(xpos - 1).IsFloor())
						|| (ConstructionMap.Tiles().get(ypos).get(xpos - 1).IsFloorDamaged())) {
					callStack.add(new GridPoint2(xpos - 1, ypos));
				}
			}
			if (EValid) {
				if ((ConstructionMap.Tiles().get(ypos).get(xpos + 1).IsFloor())
						|| (ConstructionMap.Tiles().get(ypos).get(xpos + 1).IsFloorDamaged())) {
					callStack.add(new GridPoint2(xpos + 1, ypos));
				}
			}
			if (SValid) {
				if (WValid) {
					if ((ConstructionMap.Tiles().get(ypos + 1).get(xpos - 1).IsFloor())
							|| (ConstructionMap.Tiles().get(ypos + 1).get(xpos - 1).IsFloorDamaged())) {
						callStack.add(new GridPoint2(xpos - 1, ypos + 1));
					}
				}
				if ((ConstructionMap.Tiles().get(ypos + 1).get(xpos).IsFloor())
						|| (ConstructionMap.Tiles().get(ypos + 1).get(xpos).IsFloorDamaged())) {
					callStack.add(new GridPoint2(xpos, ypos + 1));
				}
				if (EValid) {
					if ((ConstructionMap.Tiles().get(ypos + 1).get(xpos + 1).IsFloor())
							|| (ConstructionMap.Tiles().get(ypos + 1).get(xpos + 1).IsFloorDamaged())) {
						callStack.add(new GridPoint2(xpos + 1, ypos + 1));
					}
				}
			}
		}
	}

	/**
	 * @brief Places and rotates walls, and updates surrounded castles. Moves on to next mode depending if there is a
	 *        winner or not
	 * @param game
	 *            Game updating
	 */
	public void Update(CGame game) {
		super.Update(game);
        int SurroundedBefore = 0;
        int SurroundedAfter = 0;

        LinkedList<CSiegeWeapons> SiegeWeapons = game.GameState().Units().SiegeWeapons();
        boolean Advance = true;
        for (int i = SiegeWeapons.size() - 1; i >= 0; i--) {
            CSiegeWeapons weapon = SiegeWeapons.get(i);
            if (weapon.IsAlive()) {
                weapon.Update(game);
                Advance = true;
            } else {
                game.GameState().Units().DestroySiege(weapon);
                Advance = false;
            }
            if (Advance) {
            }
        }


		for (CPlayer Player : game.GameState().DPlayers) {
			if (Player.ShouldTakePrimaryAction(game)) {
				if (Player.TryToPlaceWall(game, new Vector2(Player.DCursorTilePosition))) {
					Player.DWallShape.Randomize(Player.DRandomNumberGenerator.Random());
                    game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctPlace);
				}
			} else if (Player.DIsAI) {
				if (Player.TryToPlaceWall(game, new Vector2(Player.DCursorTilePosition))) {
					Player.DWallShape.Randomize(game.GameState().DRandomNumberGenerator.Random());
                    game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctPlace);
				}
			}
			if (Player.ShouldTakeSecondaryAction(game)) {
				Player.RotateWall(game);
			}
		}

        for (Castle castle : game.GameState().TerrainMap().Castles()) {
            if (castle.DSurrounded) {
                SurroundedBefore++;
            }
        }

		CheckSurroundedCastles(game);

        for (Castle castle : game.GameState().TerrainMap().Castles()) {
            if (castle.DSurrounded) {
                SurroundedAfter++;
            }
        }

        if (SurroundedBefore != SurroundedAfter) {
            game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctTriumph);
        }

		if (CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0) {
			int LivingPlayers = game.GameState().GetPlayersWithOwnedCastles(game).size();
			if (LivingPlayers == 0) {
				game.SwitchMode(new CRebuildMode());
			} else if (LivingPlayers == 1) {
                // TODO: Implement multiplayer
                //if(!game.GameState().DRoom.DIsMultiplayer || game.GameState().DRoom.DIsOwnedByLocalUser) {
                    game.SwitchMode(new CGameOverMode());
                //} } else if (game.GameState().DRoom.DIsOwnedByLocalUser && !DSentGameOver) {
//                int PlayerID = CPlayer.GetIDForColor(game.GameState().GetMainPlayerColor());
//                int WinnerID = CPlayer.GetIDForColor(game.GameState().GetPlayersWithOwnedCastles(game).at(0).DColor);
//                game.GameState().Network().EndGame(PlayerID, WinnerID);
//                DSentGameOver = true;
			} else {
				game.SwitchMode(new CBannerTransitionMode(game, "PLACE CANNONS", this, new CCannonPlacementMode()));
			}
		}
	}

	/**
	 * @brief Draws the 2D game and wall shape cursors for each player
	 * @param game
	 *            Game drawing
	 */
	public void Draw(CGame game) {
		super.Draw2D(game);
		super.Draw(game);

		for (CPlayer Player : game.GameState().DPlayers) {
			Player.DWallShape.Draw(game, Player, new Vector2(Player.DCursorTilePosition));
		}
	}
}
