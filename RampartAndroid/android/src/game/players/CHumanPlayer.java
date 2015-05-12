package game.players;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.CInputState;
import game.CTerrainMap;
import game.Castle;


/**
 * @brief A human player taking input from input state
 */
public class CHumanPlayer extends CPlayer {

	/**
	 * @brief Makes a new human player setting as local player
	 */
	public CHumanPlayer() {
        super();
		DIsLocalPlayer = true;
	}

	/**
	 * @brief Updates cursor position based on mouse position
	 * @param game
	 *            Game to update
	 */
	public void Update(CGame game) {

		/** Note: In Android, we drag our shit */
		if (game.InputState().DMousePosition != null) {
			DCursorPosition = game.InputState().DMousePosition;
		}

		super.Update(game);
	}

	/**
	 * @brief Returns if left button is pressed
	 * @param game
	 *            Game to update
	 * @return true if left button pressed, false otherwise
	 */
	public boolean ShouldTakePrimaryAction(CGame game) {
		return game.InputState().DButtonPressed == CInputState.EInputButton.ibLeftButton;
	}

	/**
	 * @brief Returns if right button is pressed
	 * @param game
	 *            Game to update
	 * @return true if right button is pressed, false otherwise
	 */
	public boolean ShouldTakeSecondaryAction(CGame game) {
		return game.InputState().DButtonPressed == CInputState.EInputButton.ibRightButton;
	}

    public void UpdateHoveredCastle(CGame game) {
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
    }
}
