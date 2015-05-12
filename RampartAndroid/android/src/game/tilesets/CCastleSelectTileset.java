package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Tileset containing ring for castle hovering
 */
public class CCastleSelectTileset extends CGraphicTileset {

	/**
	 * @brief Stores the starting indices for each color
	 */
	private int[] D2DSelectColorIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            The game to load in
	 * @param filename
	 *            The filename to load from
	 * @return true
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D2DSelectColorIndices[EPlayerColor.pcNone.getValue()] = 0;
		D2DSelectColorIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("blue-0");
		D2DSelectColorIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("red-0");
		D2DSelectColorIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("yellow-0");

		return true;
	}

	/**
	 * @brief Draws the tile for a color
	 * @param game
	 *            Game to draw in
	 * @param position
	 *            Position to draw at
	 * @param player_color
	 *            Color to draw
	 * @param index
	 *            Index of ring tile
	 */
	public final void Draw2DCastleSelectTile(CGame game, Vector2 position, EPlayerColor player_color, int index) {
		super.DrawTile(game, position, D2DSelectColorIndices[player_color.getValue()] + index);
	}
}
