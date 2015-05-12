package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;

/**
 * @brief Brick tileset used to draw menus and borders
 */
public class CBrickTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices of the brick types
	 */
	private int[] DBrickIndices = new int[EBorderBrickType.bbtMax.getValue()];

	/**
	 * @param game
	 *            The game loading in
	 * @param filename
	 *            The filename to load from // *
	 * @return True
	 * @brief The different brick types // *
	 * @brief Loads the tileset // *
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		DBrickIndices[EBorderBrickType.bbtTopCenter.getValue()] = this.FindTile("brick-tc");
		DBrickIndices[EBorderBrickType.bbtTopRight.getValue()] = this.FindTile("brick-tr");
		DBrickIndices[EBorderBrickType.bbtRight0.getValue()] = this.FindTile("brick-r0");
		DBrickIndices[EBorderBrickType.bbtRight1.getValue()] = this.FindTile("brick-r1");
		DBrickIndices[EBorderBrickType.bbtBottomRight.getValue()] = this.FindTile("brick-br");
		DBrickIndices[EBorderBrickType.bbtBottomCenter.getValue()] = this.FindTile("brick-bc");
		DBrickIndices[EBorderBrickType.bbtBottomLeft.getValue()] = this.FindTile("brick-bl");
		DBrickIndices[EBorderBrickType.bbtLeft0.getValue()] = this.FindTile("brick-l0");
		DBrickIndices[EBorderBrickType.bbtLeft1.getValue()] = this.FindTile("brick-l1");
		DBrickIndices[EBorderBrickType.bbtTopLeft.getValue()] = this.FindTile("brick-tl");
		DBrickIndices[EBorderBrickType.bbtSingle.getValue()] = this.FindTile("brick-single");

		return true;
	}

	/**
	 * @param game
	 *            The game to draw in
	 * @param position
	 *            The position to draw at
	 * @param brick_type
	 *            The brick type to draw
	 * @brief Draws the brick tile at the position // *
	 */
	public final void DrawTile(CGame game, Vector2 position, EBorderBrickType brick_type) {
		super.DrawTile(game, new Vector2(position), DBrickIndices[brick_type.getValue()]);
	}
}
