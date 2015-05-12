package game.players;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

import game.CCannon;
import game.CGame;
import game.CShip;
import game.CTerrainMap;
import game.CWallShape;
import game.Castle;
import game.EPlayerColor;
import game.sounds.CSounds;
import game.utils.CRandomNumberGenerator;

/**
 * @brief Player with state about cursors, cannons, and castle owned, and actions that can be taken
 */
public class CPlayer {

	/**
	 * @brief The number of cannons available for placing, based on number of castles owned
	 */
	public int DAvailableCannons;
	/**
	 * @brief The number of extra cannons available given at the first round
	 */
	public int DExtraCannons;
	/**
	 * @brief The position of the cursor on screen
	 */
	public Vector2 DCursorPosition = new Vector2();
	/**
	 * @brief The position of the cursor in tile indices
	 */
	public Vector2 DCursorTilePosition = new Vector2();
	/**
	 * @brief The castle being hovered by this player
	 */
	public Castle DHoveredCastle;
	/**
	 * @brief The list of cannons ready to fire
	 */
	public LinkedList<CCannon> DReadyCannons = new LinkedList<CCannon>();
    public LinkedList<CShip> DReadyShipCannons = new LinkedList<CShip>();
	/**
	 * @brief The color of this player
	 */
	public EPlayerColor DColor;
	/**
	 * @brief Stores if player has placed a home castle
	 */
	public boolean DPlacedHomeCastle;
	/**
	 * @brief If player is local to this client
	 */
	public boolean DIsLocalPlayer;
	public CRandomNumberGenerator DRandomNumberGenerator = new CRandomNumberGenerator();
	/**
	 * @brief Username of the player
	 */
	public String DUsername;
	/**
	 * @brief The wall shape being placed
	 */
	public CWallShape DWallShape = new CWallShape();
	/**
	 * @brief Is true if the player is an AI
	 */
	public boolean DIsAI;

	/**
	 * @brief Initializes player with nothing owned
	 */
	public CPlayer() {
		DAvailableCannons = 0;
		DIsLocalPlayer = false;
		DPlacedHomeCastle = false;
		DHoveredCastle = null;
		DExtraCannons = 0;
		DIsAI = false;
		DUsername = "";
	}

	public static EPlayerColor GetColorForID(int i) {
		return EPlayerColor.forValue(i);
	}

    public static int GetIDForColor(EPlayerColor color) {
        return color.getValue();
    }

	public void Update(CGame game) {
        UpdateCursorTilePosition(game);
    }
    /**
*
* @brief Updates cursor tile position
*
* @param game

*/
    public final void UpdateCursorTilePosition(CGame game) {
		CTerrainMap Map = game.GameState().TerrainMap();
		if (Map != null) {
			DCursorTilePosition = Map.ConvertToTileIndex(DCursorPosition);
		}
	}

	/**
	 * @brief Returns if the primary action should be taken this frame
	 * @param game
	 *            Game playing
	 * @return False, subclasses would override
	 */
	public boolean ShouldTakePrimaryAction(CGame game) {
		return false;
	}

	/**
	 * @brief Returns if the secondary action should be taken this frame
	 * @param game
	 *            Game playing
	 * @return False, subclasses would override
	 */
	public boolean ShouldTakeSecondaryAction(CGame game) {
		return false;
	}

	/**
	 * @brief Updates which castle is hovered by this player by calculating the best distance
	 * @param game
	 *            The game updating
	 */
	public void UpdateHoveredCastle(CGame game) {
		/*int XTile;
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
		DHoveredCastle = BestCastle;*/
	}

	/**
	 * @brief Gets the number of castles this player has currently surrounded
	 * @param game
	 *            The game updating
	 * @return Count of castles surrounded
	 */
	public final int OwnedCastleCount(CGame game) {
		int Count = 0;
		java.util.ArrayList<Castle> Castles = game.GameState().TerrainMap().Castles();
		for (Castle castle : Castles) {
			if (castle.DColor == DColor && castle.DSurrounded)
				Count++;
		}
		return Count;
	}

	/**
	 * @brief Rotates the wall shape currently placing
	 * @param game
	 *            The game playing
	 */
	public void RotateWall(CGame game) {
		DWallShape.Rotate();
	}

	/**
	 * @brief Selects the home castle, and gets extra cannons
	 * @param game
	 *            Game updating
	 * @param castle
	 *            Castle selected
	 */
	public void PlaceHomeCastle(CGame game, Castle castle) {
		game.GameState().ConstructionMap().SurroundCastle(game, castle);
		castle.DSurrounded = true;
		DPlacedHomeCastle = true;
		DExtraCannons += 2;
	}

	/**
	 * @brief Tries to place cannon at the position
	 * @param game
	 *            The game updating
	 * @param position
	 *            The tile position to place at
	 * @return true if cannon was placed, otherwise false
	 */
	public boolean TryToPlaceCannon(CGame game, Vector2 position) {
		boolean result = DAvailableCannons > 0
				&& game.GameState().ConstructionMap().IsSpaceOpenForColor(DColor, new Vector2(position),
						new Vector2(CCannon.CSize))
				&& game.GameState().TerrainMap().IsSpaceOpen(new Vector2(position), new Vector2(CCannon.CSize));
		if (result) {
			game.GameState().ConstructionMap().Cannons().add(new CCannon(game, position));
            game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctPlace);
			DAvailableCannons--;
		}
		return result;
	}

	/**
	 * @brief Fires the next cannon in ready cannons
	 * @param game
	 *            The game updating
	 */
	public void FireNextCannon(CGame game) {
		if (DReadyCannons.size() > 0) {
			DReadyCannons.get(0).FireAt(game, DCursorPosition);
			DReadyCannons.remove(0);
		}
        if(DReadyShipCannons.size() > 0) {
            DReadyShipCannons.get(0).FireAt(game, DCursorPosition);
            DReadyShipCannons.remove(0);
        }
	}

	/**
	 * @brief Tries to place a wall shape at the position
	 * @param game
	 *            The game updating
	 * @param tile_position
	 *            The position to placea t
	 * @return true if wall was placed, otherwise false
	 */
	public boolean TryToPlaceWall(CGame game, Vector2 tile_position) {
		return DWallShape.Place(game, this, new Vector2(tile_position));
	}


    public void TryToPlaceShipAI(CGame game, CPlayer player) {
    }

}
