package game;


import com.badlogic.gdx.math.Vector2;

import game.players.CPlayer;
import se.krka.kahlua.integration.annotations.LuaConstructor;
import se.krka.kahlua.integration.annotations.LuaMethod;

public class CAIBridge {

	private static CGame DGameCopy = null;

    @LuaConstructor(name ="NewCAIBridgeObj")
    public CAIBridge(){};

	// Returns DConstructionTiles[y][x], the type of tile existing at a given coordinate.
    @LuaMethod(name = "getWallType")
	public static double getWallType(double x, double y) {
		Vector2 position = new Vector2();
		position.x = (float)x;
		position.y = (float)y;
		CConstructionMap Map = DGameCopy.GameState().ConstructionMap();
		return (double)(Map.GetTileAt(position).DType.getValue());
	}

    //Returns the difficulty level of the AI.
    @LuaMethod(name = "getDifficultyLevel")
    public static double getDifficulty(){
        return (double)CAIPlayer.DDifficultyLevel;
    }

	// Returns true if a location is a valid placement for the player with the colorindex value
    @LuaMethod(name = "isValidCannonLoc")
	public static boolean isValidCannonLoc(double colorInt, double DAvailableCannons, double x, double y) {
		Vector2 position = new Vector2();
		position.x = (float)x;
		position.y = (float)y;
		EPlayerColor colorindex = EPlayerColor.forValue((int)colorInt);
		boolean result = DAvailableCannons > 0
				&& DGameCopy.GameState().ConstructionMap().IsSpaceOpenForColor(colorindex, new Vector2(position),
						new Vector2(CCannon.CSize))
				&& DGameCopy.GameState().TerrainMap().IsSpaceOpen(new Vector2(position), new Vector2(CCannon.CSize));
		return result;
	}

	// Set the target location of the cursor for the player with the colorindex value
    @LuaMethod(name = "setDAITarget")
	public static void setDAITarget(double colorInt, double x, double y) {
		Vector2 position = new Vector2();
		position.x = (float)x;
		position.y = (float)y;
		EPlayerColor colorindex = EPlayerColor.forValue((int)colorInt);
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				Player.DCursorPosition = new Vector2(position);
			}
		}
	}

	/** Returns the X-position of the cursor’s destinatcion for the player with the colorindex value */
    @LuaMethod(name = "getDAITargetX")
	public static double getDAITargetX(double colorInt) {
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue((int)colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return (double) Player.DCursorPosition.x;
			}
		}
		return -1;
	}

	/** Returns the Y-position of the cursor’s destinatcion for the player with the colorindex value */
    @LuaMethod(name = "getDAITargetY")
	public static double getDAITargetY(double colorInt) {
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue((int)colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return (double) Player.DCursorPosition.y;
			}
		}
		return -1;
	}

    //Rebuild Functions-----------------------------------------------------------------------------

    //Functions for getting CAIPlayer values
    @LuaMethod(name = "getDTileWidth_rebuild")
    public static double getDTileWidth_rebuild(){return CAIPlayer.DTileWidth_rebuild;}

    @LuaMethod(name = "getDTileHeight_rebuild")
    public static double getDTileHeight_rebuild(){return CAIPlayer.DTileHeight_rebuild;}

    @LuaMethod(name = "getDMapHeight_rebuild")
    public static double getDMapHeight_rebuild(){return CAIPlayer.DMapHeight_rebuild;}

    @LuaMethod(name = "getDMapWidth_rebuild")
    public static double getDMapWidth_rebuild(){return CAIPlayer.DMapWidth_rebuild;}

	// Returns the height of the wall piece that player with the colorindex value is currently holding
    @LuaMethod(name = "getWallShapeHeight")
	public static int getWallShapeHeight(int colorInt) {
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue(colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return Player.DWallShape.Height();
			}
		}
		return -1;
	}

	// Returns the width of the wall piece that player with the colorindex value is currently holding
    @LuaMethod(name = "getWallShapeWidth")
	public static int getWallShapeWidth(int colorInt) {
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue(colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return Player.DWallShape.Width();
			}
		}
		return -1;
	}

	// Returns true if the wall piece has a block at that location
    @LuaMethod(name = "getWallShapeIsBlock")
	public static boolean getWallShapeIsBlock(int colorInt, int x, int y) {
		Vector2 position = new Vector2();
		position.x = x;
		position.y = y;
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue(colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return Player.DWallShape.IsBlock((int) position.x, (int) position.y);
			}
		}
		return false;
	}

	// Returns true if the wall piece is placed in a valid tile location
    @LuaMethod(name = "getValidWallPlacement")
	public static boolean getValidWallPlacement(int colorInt, int x, int y) {
		Vector2 position = new Vector2();
		position.x = x;
		position.y = y;
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue(colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				return Player.DWallShape.CanBePlaced(DGameCopy, Player, new Vector2(position));
			}
		}
		return false;
	}

	// Rotate the orientation of the wall piece 90° clockwise
    @LuaMethod(name = "rotateWallShape")
	public static void rotateWallShape(int colorInt) {
		java.util.ArrayList<CPlayer> Players = DGameCopy.GameState().DPlayers;
		EPlayerColor colorindex = EPlayerColor.forValue(colorInt);
		for (java.util.Iterator<CPlayer> it = Players.iterator(); it.hasNext();) {
			CPlayer Player = it.next();
			if (Player.DColor == colorindex) {
				Player.DWallShape.Rotate();
			}
		}
	}

	public final void Update(CGame game) {
		DGameCopy = game;
		// std::cout << "Bridge Updated" << std::endl;
	}
}
