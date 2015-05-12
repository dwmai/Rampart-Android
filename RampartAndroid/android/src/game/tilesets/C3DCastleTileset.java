package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Tileset for drawing 3D castles
 */
public class C3DCastleTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices to the tiles with each color castle
	 */
	private int[] D3DCastleIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            The game to lod in
	 * @param filename
	 *            The file to load from
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D3DCastleIndices[EPlayerColor.pcNone.getValue()] = this.FindTile("castle-none");
		D3DCastleIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("castle-blue-0");
		D3DCastleIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("castle-red-0");
		D3DCastleIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("castle-yellow-0");
		return true;

	}

	/**
	 * @brief Draws the 3D castle for a player color at a position
	 * @param game
	 *            The game to draw in
	 * @param position
	 *            The position to draw at
	 * @param player_color
	 *            The color of the castle
	 */
    public final void Draw3DCastleTile(CGame game, Vector2 position, EPlayerColor player_color) {
        Draw3DCastleTile(game, position, player_color, 0);
    }
    public final void Draw3DCastleTile(CGame game, Vector2 position, EPlayerColor player_color, int index) {
        super.DrawTile(game, position, D3DCastleIndices[player_color.getValue()] + index);
    }
}
