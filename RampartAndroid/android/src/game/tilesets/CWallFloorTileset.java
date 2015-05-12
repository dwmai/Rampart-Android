package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief Combined tileset for 2D walls and floors
 */
public class CWallFloorTileset extends CGraphicTileset {

	/**
	 * @brief Holds index of damaged ground tile
	 */
	public int D2DDamagedGroundIndex;
	/**
	 * @brief Holds indices of floor tiles for players
	 */
	private int[] D2DFloorIndices = new int[EPlayerColor.pcMax.getValue()];
	/**
	 * @brief Holds indices for wall tiles for players, including wall shape placing tiles
	 */
	private int[] D2DWallIndices = new int[EPlayerColor.pcMax.getValue() + 2];

	/**
	 * @brief Loads tileset
	 * @param game
	 *            Game loading
	 * @param filename
	 *            Filename loading from
	 * @return true
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D2DFloorIndices[EPlayerColor.pcNone.getValue()] = this.FindTile("floor-even");
		D2DFloorIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("floor-blue");
		D2DFloorIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("floor-red");
		D2DFloorIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("floor-yellow");
		D2DWallIndices[EPlayerColor.pcNone.getValue()] = this.FindTile("bad-0");
		D2DWallIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("blue-0");
		D2DWallIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("red-0");
		D2DWallIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("yellow-0");
		D2DWallIndices[EPlayerColor.pcMax.getValue()] = this.FindTile("good-0");
		D2DWallIndices[EPlayerColor.pcMax.getValue() + 1] = this.FindTile("good-alt-0");
		return true;
	}

	/**
	 * @brief Draws 2D floor tile for color
	 * @param game
	 *            Game drawing
	 * @param x
	 *            X
	 * @param y
	 *            Y
	 * @param player_color
	 *            Color drawing
	 */
	public final void Draw2DFloorTile(CGame game, int x, int y, EPlayerColor player_color) {
		super.DrawTile(game, new Vector2(x, y), D2DFloorIndices[player_color.getValue()]);
	}

	/**
	 * @brief Draws 2D wall tile for color
	 * @param game
	 *            Game drawing
	 * @param x
	 *            X
	 * @param y
	 *            Y
	 * @param player_color
	 *            Color drawing
	 * @param index
	 *            Index of wall
	 */
	public final void Draw2DWallTile(CGame game, int x, int y, int d2dWallIndicesIndex, int index) {
		super.DrawTile(game, new Vector2(x, y), D2DWallIndices[d2dWallIndicesIndex] + index);
	}
}
