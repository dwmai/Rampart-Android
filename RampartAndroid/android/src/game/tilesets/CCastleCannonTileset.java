package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Tileset that contains the 2D castle and cannon tiles
 */
public class CCastleCannonTileset extends CGraphicTileset {

	/**
	 * @brief Stores indices of castles for colors
	 */
	private int[] D2DCastleIndices = new int[EPlayerColor.pcMax.getValue()];
	/**
	 * @brief Stores indices of cannons for colors
	 */
	private int[] D2DCannonIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            Game to load in
	 * @param filename
	 *            The filename to load from
	 * @return true
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D2DCastleIndices[EPlayerColor.pcNone.getValue()] = this.FindTile("castle-none");
		D2DCastleIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("castle-blue");
		D2DCastleIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("castle-red");
		D2DCastleIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("castle-yellow");

		D2DCannonIndices[EPlayerColor.pcNone.getValue()] = this.FindTile("cannon");
		D2DCannonIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("cannon-blue-1");
		D2DCannonIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("cannon-red-1");
		D2DCannonIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("cannon-yellow-1");

		return true;
	}

	/**
	 * @brief Draws the castle tile for a color
	 * @param game
	 *            Game to draw in
	 * @param x
	 *            Legacy X
	 * @param y
	 *            Legacy Y
	 * @param player_color
	 *            Player color to draw in
	 */
	public final void Draw2DCastleTile(CGame game, int x, int y, EPlayerColor player_color) {
		super.DrawTile(game, new Vector2(x, y), D2DCastleIndices[player_color.getValue()]);
	}

	/**
	 * @brief Draws the cannon tile at a position
	 * @param game
	 *            Game to draw in
	 * @param position
	 *            Position to draw at
	 */
	public final void Draw2DCannonTile(CGame game, Vector2 position) {
		super.DrawTile(game, position, D2DCannonIndices[EPlayerColor.pcNone.getValue()]);
	}

	/**
	 * @brief Draws the cannon cursor with number of cannons available for a player
	 * @param game
	 *            Game to draw in
	 * @param position
	 *            Position to draw
	 * @param player_color
	 *            Color to draw
	 * @param count
	 *            Count to show on tile
	 */
	public final void Draw2DCannonToPlaceTile(CGame game, Vector2 position, EPlayerColor player_color, int count) {
		super.DrawTile(game, position, D2DCannonIndices[player_color.getValue()] + count - 1);
	}
}
