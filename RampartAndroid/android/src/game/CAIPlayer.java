package game;

/** @brief An AI player taking input from input state
*/

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.Map;

import game.players.CPlayer;
import game.sounds.CSounds;
import game.utils.RandomNumbers;


public class CAIPlayer extends CPlayer {
	/**
	* @brief Makes a new AI player setting as local player

*/
	public static int DDifficultyLevel = 1;
	public static int DNumberCastleLocations = 0;
	public static int DTileWidth_rebuild;
	public static int DTileHeight_rebuild;
	public static int DMapHeight_rebuild;
	public static int DMapWidth_rebuild;
	public static CAIBridge DAIBridge = null;
	// Frame counting variables used to ensure the AI doesn't execute functions too quickly
	public int framecount; // *
	public int framelimit; // * // * @brief Updates cursor position based on mouse position
	/**
	* @param game Game to update


*/
	public void Update(CGame game) {
		this.DAIBridge.Update(game);
		Vector2 position = new Vector2();
		position.x = 130;
		position.y = 130;
        DDifficultyLevel = game.GameState().getAIDifficulty().getValue();
        if (DDifficultyLevel == 0)
            DDifficultyLevel = 1;

		super.Update(game);
	}

	public CAIPlayer(CGame game) {
        super();
		DAIBridge = new CAIBridge();
		DAIBridge.Update(game);
		// std::cout << "AIPlayer Created" << std::endl;
		DIsLocalPlayer = true;
		DIsAI = true;
		// L = luaL_newstate();
		init_lua();
		framelimit = 30;
		framecount = framelimit;
		DWallShape.Randomize(game.GameState().DRandomNumberGenerator.Random());

	} // *

    /** @brief Returns if left button is pressed */
	public final void init_lua() {

        CKahluaInterface KI = CKahluaInterface.getInstance();
        KI.getExposer().exposeClass(CAIBridge.class);
        KI.runStringScript("test = NewCAIBridgeObj()");
        KI.runStringScript("myrandom = newrandom()");
        KI.runFileScript("test.lua");
        KI.runFileScript("castle_select.lua");
        KI.runFileScript("cannon_placement_ai.lua");
        KI.runFileScript("battle_ai.lua");
        KI.runFileScript("rebuild_ai.lua");
	}

    /**
	* @param game Game to update
	*
	* @return true if left button pressed, false otherwise


*/
	public boolean ShouldTakePrimaryAction(CGame game) {
		return game.InputState().DButtonPressed == CInputState.EInputButton.ibLeftButton;
	}

	/** @brief Returns if right button is pressed
	*
	* @param game Game to update
	*
	* @return true if right button is pressed, false otherwise

*/
	public boolean ShouldTakeSecondaryAction(CGame game) {
		return game.InputState().DButtonPressed == CInputState.EInputButton.ibRightButton;
	}

	public void UpdateHoveredCastle(CGame game) {
		this.DAIBridge.Update(game);
		this.Update(game);
		int XTile;
		int YTile;
		CTerrainMap Map = game.GameState().TerrainMap();
		Vector2 TileIndex = Map.ConvertToTileIndex(new Vector2(DCursorPosition));
		XTile = (int) TileIndex.x;
		YTile = (int) TileIndex.y;

		int BestDistance = 999999;
		Castle BestCastle = null;

		for (Castle castle : Map.Castles()) {
			if (castle.DColor != DColor)
				continue;

			int Distance;
			int XPos = (int) castle.IndexPosition().x;
			int YPos = (int) castle.IndexPosition().y;

			Distance = (XPos - XTile) * (XPos - XTile) + (YPos - YTile) * (YPos - YTile);
			if (Distance < BestDistance) {
				BestDistance = Distance;
				BestCastle = castle;
			}
		}
        DHoveredCastle = BestCastle;

		// Simulate a castle select click
		if (!DPlacedHomeCastle)
			PlaceHomeCastle(game, DHoveredCastle);
            game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctTriumph);
	}

	public boolean TryToPlaceCannon(CGame game, Vector2 position) {
		boolean result = false;
		if (DAvailableCannons != 0) {
			// Find potential cannon positions and try to place them

			int XTile;
			int YTile;
			boolean XDir;
			boolean YDir;
			XDir = (DAvailableCannons & 0x1) != 0;
			YDir = (DAvailableCannons & 0x2) != 0;
			XTile = (int) DCursorPosition.x / 12;
			YTile = (int) DCursorPosition.y / 12;

			result = CAIScriptFunctions.getCannonLocs(DColor.getValue(), XTile, YTile, XDir, YDir, 40, 24, DAvailableCannons);

			// Temporary fix to overlaping AI cannons
			result = DAvailableCannons > 0
					&& game.GameState().ConstructionMap().IsSpaceOpenForColor(DColor, new Vector2(position),
							new Vector2(CCannon.CSize))
					&& game.GameState().TerrainMap().IsSpaceOpen(new Vector2(position), new Vector2(CCannon.CSize));
			if (result && DAvailableCannons > 0) {
				game.GameState().ConstructionMap().Cannons().add(new CCannon(game, position));
				DAvailableCannons--;
			} else {
			}
		}
		this.DAIBridge.Update(game);
		return result;
	}

	public void FireNextCannon(CGame game) {
        if (DDifficultyLevel == 1) {
            framelimit = 80;
        } else if (DDifficultyLevel == 2) {
            framelimit = 60;
        } else {
            framelimit = 45;
        }
        LinkedList<CShip> Ship = game.GameState().Units().Ship();
		if (framecount >= framelimit) {
			if (DReadyCannons.size() > 0) {
				Map<String, Double> coords = CAIScriptFunctions.getxy(DColor.getValue());
				int x = coords.get("xpos").intValue();
				int y = coords.get("ypos").intValue();
				//int x = 0, y = 0;
				// convet form squares to pixels
				x *= 12;
				y *= 12;
				DCursorPosition.x = x + 6;
				DCursorPosition.y = y + 6;
				DReadyCannons.get(0).FireAt(game, DCursorPosition);
				DReadyCannons.remove(0);
				framecount = 0;
			}

            if (DReadyShipCannons.size() > 0) {
                while (DReadyShipCannons.size() > 0 && !DReadyShipCannons.get(0).IsAlive()) {
                    DReadyShipCannons.remove(0);
                }
                if (DReadyShipCannons.size() > 0) {
                    DReadyShipCannons.get(0).FireAt(game, DCursorPosition);
                    DReadyShipCannons.remove(0);
                }
            }
		} else {
            framecount++;
        }
	}

	public boolean TryToPlaceWall(CGame game, Vector2 tile_position) {
		framelimit = 10;
		if (framecount >= framelimit) {
			// DWallShape.Randomize(game->GameState()->DRandomNumberGenerator.Random());
			// printf("Executing TryToPlaceWall()\n");
			framecount = 0;
			boolean DoMove = true;
			java.util.ArrayList<Castle> Castles = game.GameState().TerrainMap().Castles();
			java.util.Iterator<Castle> it = Castles.iterator();
			Vector2 bestpos = new Vector2();
			int mostwalls = 0;
			CConstructionMap Map = game.GameState().ConstructionMap();
			for (Castle castle : Castles) {
				if (castle.DColor != DColor || castle.DSurrounded) {
				} else {
					Vector2 p = castle.IndexPosition().cpy();
					// set the initial wall to check (upper left corner)
					p.x -= 3;
					p.y -= 3;
					int xdir = 0;
					int ydir = 0;
					xdir = 0;
					int wallcount = 0;
					// this for loop will check each tiletype in a "radius" of 2 from the given castle
					for (int i = 1; i <= 4; i++) {
						if (i == 1) {
							xdir = 1;
							ydir = 0;
						} else if (i == 2) {
							xdir = 0;
							ydir = 1;
						} else if (i == 3) {
							xdir = -1;
							ydir = 0;
						} else if (i == 4) {
							xdir = 0;
							ydir = -1;
						}
						for (int j = 1; j <= 8; j++) {
							p.x += xdir;
							p.y += ydir;
							if (Map.GetTileAt(new Vector2(p)) != null && Map.GetTileAt(new Vector2(p)).IsWall()) {
								wallcount++;
							}
						}
						// printf("\n");
					}
					if (wallcount >= mostwalls) {
						bestpos = castle.IndexPosition();
						mostwalls = wallcount;
					}
				}
			}
			DCursorPosition.x = -1;
			DCursorPosition.y = -1;
			if (0 > DCursorPosition.x) {
				int XTile;
				int YTile;
				int BestX;
				int BestY;
				int BestRotation;
				int BestCoverage;
				int BestInterference;

				XTile = (int) bestpos.x;
				YTile = (int) bestpos.y;

				DTileWidth_rebuild = 12;
				DTileHeight_rebuild = 12;
				DMapHeight_rebuild = 24-1;
				DMapWidth_rebuild = 40 - 1;
				Map<String, Double> Best = CAIScriptFunctions.findBestPlacement(DColor.getValue(), XTile, YTile);
				BestX = Best.get("X").intValue();
				BestY = Best.get("Y").intValue();
				BestRotation = Best.get("Rotation").intValue();
				BestCoverage = Best.get("BCoverage").intValue();
				BestInterference = Best.get("BInterference").intValue();
				//BestX = 0;
				//BestY = 0;
				//BestRotation = 0;

				if (0 > BestX) {
					DoMove = false;
				} else if (BestRotation != 0) {
					RotateWall(game);
					return false;
				} else {
					DCursorPosition.x = BestX * 40 + 6;
					DCursorPosition.y = BestY * 24 + 6;
					DCursorTilePosition.x = BestX;
					DCursorTilePosition.y = BestY;

					return (DWallShape.Place(game, this, new Vector2(DCursorTilePosition)));
				}
			}
		} else
			framecount++;

		return false;
	}

    /* TODO: Remove this entire section, temporary until AI team integrates fully */
    public int getenemyWall(int colorIndex) {
        if (colorIndex == 1)
            return 5;
        else
            return 2;
    }

    public Vector2 getShipMovementLocation(int colorIndex) {
       Vector2 shipMovementLocation = new Vector2(0, 0);
        int[] wallArrX = new int[23 * 39 + 2];
        int[] wallArrY = new int[23 * 39 + 2];
        int i = 1;
        int enemyWall = getenemyWall(colorIndex);
        for (int y = 0; i < 23; y++) {
            for (int x = 0; x < 39; x++) {
                if (CAIBridge.getWallType(x, y) == enemyWall) {
                    wallArrX[i] = x;
                    wallArrY[i] = y;
                    i++;
                }
            }
        }
        int j = (int)Math.floor((i - 1) * Math.random()) + 1;
        shipMovementLocation.x = wallArrX[j];
        shipMovementLocation.y = wallArrY[j];
        return shipMovementLocation;
    }

    public void TryToPlaceShipAI(CGame game, CPlayer player) {
        Vector2 spawnPosition = getSpawnPosition(game);
        Vector2 movementPosition = getMovementPosition(game, new Vector2(spawnPosition));
        int numSiegeWeapons = getNumSiegeWeapons();
        LinkedList<Vector2> siegeTarget = getSiegeTarget(numSiegeWeapons);
        game.GameState().Units().Ship().add(new CShip(spawnPosition, player, movementPosition, siegeTarget, numSiegeWeapons));
    }
    public final LinkedList<Vector2> getSiegeTarget(int numSiegeWeapons) {
        LinkedList<Vector2> siegeTarget;
        siegeTarget = new LinkedList<Vector2>();
        for(int i = 0; i < numSiegeWeapons; i++) {
            // TODO: Hi Tony, convert this to KuaLua
            Vector2 coords = getShipMovementLocation(DColor.getValue());
            //reuse getshipmovement isntead of getxy, because right now getxy has implemented inaccurateness
            siegeTarget.add(i, coords);
        }
        return siegeTarget;
    }

    public final int getNumSiegeWeapons() {
        int numSiegeWeapons;
        numSiegeWeapons = RandomNumbers.nextNumber() % 3;
        return Math.abs(numSiegeWeapons);
    }

    public final Vector2 getSpawnPosition(CGame game) {
        RandomNumbers.seed(); // should transition spawn selection into lua soon, but first get movement down
        Vector2 spawnPosition = new Vector2();
        boolean shipSpawnFound = false;
        while(!shipSpawnFound) {
            spawnPosition.x = RandomNumbers.nextNumber()%39;
            spawnPosition.y = RandomNumbers.nextNumber()%23;
            boolean factor1 = game.GameState().TerrainMap().IsSpaceWater(new Vector2(spawnPosition), new Vector2(CShip.CShipSize));
            boolean factor2 = game.GameState().Units().IsSpaceFreeOfShip(new Vector2(spawnPosition), new Vector2(CShip.CShipSize));
            boolean result = factor1&& factor2;
            if(result) {
                shipSpawnFound = true;
            }
        }
        return spawnPosition;
    }

    public final Vector2 getMovementPosition(CGame game, Vector2 spawnPosition) {
        boolean shipTargetIsWater = false;
        Vector2 movementPosition = new Vector2();
        int movementPositionX;
        int movementPositionY;
        Vector2 generatedMovementLocation = getShipMovementLocation(DColor.getValue());
        movementPositionX = (int)generatedMovementLocation.x;
        movementPositionY = (int)generatedMovementLocation.y;
        //randomly select an enemy castle wall to drive towards
        movementPosition.x = movementPositionX;
        movementPosition.y = movementPositionY;
        // convert from blocks to pixels / convert ints to Vector2
        Vector2 lineFromDestToSpawn = new Vector2();
        Vector2 checkLocation = new Vector2();
        lineFromDestToSpawn.x = movementPosition.x - spawnPosition.x;
        lineFromDestToSpawn.y = movementPosition.y - spawnPosition.y;
        //std::cout << "Initial targetted location is: " << movementPosition.x << ", " << movementPosition.y << std::endl;
        //std::cout << "Spawn Position is: " << spawnPosition.x << ", " << spawnPosition.y << std::endl;
        //std::cout << "Line from Destination to spawn: " << lineFromDestToSpawn.x << ", " << lineFromDestToSpawn.y << std::endl;
        while(!shipTargetIsWater) {
            if(lineFromDestToSpawn.x > 0 && movementPosition.x - spawnPosition.x != 0) {
                movementPosition.x--;
            } // if the wall is to the right of the ship, move right
            else if(lineFromDestToSpawn.x < 0 && movementPosition.x - spawnPosition.x != 0) {
                movementPosition.x++;
            } // if the wall is to the left of the ship, move left
            if(lineFromDestToSpawn.y > 0 && movementPosition.y - spawnPosition.y != 0) {
                movementPosition.y--;
            } // if the wall has higher Y value than the ship, lower Y value
            else if(lineFromDestToSpawn.y < 0 && movementPosition.y - spawnPosition.y != 0) {
                movementPosition.y++;
            }

            //std::cout << "Checking Location " << movementPosition.x << ", " << movementPosition.y << " for water: " << game->GameState()->TerrainMap()->IsSpaceWater(movementPosition, CShip::CShipSize) <<  std::endl;
            shipTargetIsWater = game.GameState().TerrainMap().IsSpaceWater(new Vector2(movementPosition), new Vector2(CShip.CShipSize));
        }
        //std::cout << "Location is now: " << movementLocation.x << ", " << movementLocation.y << std::endl;
        return movementPosition;
    }
}
