package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;

/**
 * @brief Tileset for drawing the 3D terrain
 */
public class C3DTerrainTileset extends CGraphicTileset {

	/**
	 * @brief Stores the index of the damaged ground tile
	 */
	private int D3DDamagedGroundIndex;

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            The game to load in
	 * @param filename
	 *            The filename to load from
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D3DDamagedGroundIndex = this.FindTile("hole-0");

		return true;
	}

	/**
	 * @brief Draws the damaged ground tile in tileset
	 * @param game
	 *            Game to draw in
	 * @param position
	 *            Position to draw at
	 */
	public final void DrawDamagedGroundTile(CGame game, Vector2 position) {
		super.DrawTile(game, position, D3DDamagedGroundIndex);
	}
}
