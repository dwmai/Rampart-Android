package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Tileset with reticule for each player
 */
public class CTargetTileset extends CGraphicTileset {

	/**
	 * @brief Stores indices of reticules for colors
	 */
	private int[] DTargetIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            Game loading
	 * @param filename
	 *            Filename to load from
	 * @return true
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		DTargetIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("blue-target");
		DTargetIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("red-target");
		DTargetIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("yellow-target");

		return true;
	}

	/**
	 * @brief Draws the reticule for a color
	 * @param game
	 *            Game drawing
	 * @param position
	 *            Position to draw
	 * @param player_color
	 *            Color to draw
	 */
	public final void DrawTargetTile(CGame game, Vector2 position, EPlayerColor player_color) {
		super.DrawTile(game, position, DTargetIndices[player_color.getValue()]);
	}
}
// #endif
