package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Tileset for drawing the 3D floor
 */
public class C3DFloorTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices of floors for each color
	 */
	private int[] D3DFloorIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            The game to load in
	 * @param filename
	 *            The file to load from
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D3DFloorIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("floor-blue");
		D3DFloorIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("floor-red");
		D3DFloorIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("floor-yellow");

		return true;
	}

	/**
	 * @brief Draws the floor tile for a player at a position
	 * @param game
	 *            Game to draw in
	 * @param position
	 *            Position to draw at
	 * @param player_color
	 *            The color to draw
	 */
	public final void Draw3DFloorTile(CGame game, Vector2 position, EPlayerColor player_color) {
		super.DrawTile(game, position, D3DFloorIndices[player_color.getValue()]);
	}
}
