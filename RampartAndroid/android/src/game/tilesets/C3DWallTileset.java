package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;

/**
 * @brief The tileset used to draw the 3D walls
 */
public class C3DWallTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices to the walls for each type and color
	 */
	private int[][] D3DWallIndices = new int[EPlayerColor.pcMax.getValue()][16];
	/**
	 * @brief Stores the indices to the damaged walls for each color
	 */
	private int[] D3DDamagedWallIndices = new int[EPlayerColor.pcMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            Game to load in
	 * @param filename
	 *            The filename to load from
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		for (int Index = 0; Index < 16; Index++) {
			String Name;
			Name = String.format("blue-%d-0", Index);
			D3DWallIndices[EPlayerColor.pcNone.getValue()][Index] = this.FindTile(Name);
			D3DWallIndices[EPlayerColor.pcBlue.getValue()][Index] = this.FindTile(Name);
			Name = String.format("red-%d-0", Index);
			D3DWallIndices[EPlayerColor.pcRed.getValue()][Index] = this.FindTile(Name);
			Name = String.format("yellow-%d-0", Index);
			D3DWallIndices[EPlayerColor.pcYellow.getValue()][Index] = this.FindTile(Name);

		}
		// sprintf(Name,"blue-damaged-0",Index);
		D3DDamagedWallIndices[EPlayerColor.pcBlue.getValue()] = this.FindTile("blue-damaged-0");
		// sprintf(Name,"red-damaged-0",Index);
		D3DDamagedWallIndices[EPlayerColor.pcRed.getValue()] = this.FindTile("red-damaged-0");
		// sprintf(Name,"yellow-damaged-0",Index);
		D3DDamagedWallIndices[EPlayerColor.pcYellow.getValue()] = this.FindTile("yellow-damaged-0");

		return true;
	}

	/**
	 * @brief Draws the specified wall tile in the game
	 * @param game
	 *            The game to draw in
	 * @param position
	 *            Position to draw at
	 * @param player_color
	 *            The color of the wall
	 * @param type
	 *            The type of the wall
	 * @param offset
	 *            Offset of the wall
	 * @see CTerrainMap
	 */
	public final void Draw3DWallTile(CGame game, Vector2 position, EPlayerColor player_color, int type, int offset) {
		super.DrawTile(game, position, D3DWallIndices[player_color.getValue()][type] + offset);
	}

	/**
	 * @brief Draws the damaged wall tile in the game
	 * @param game
	 *            The game to draw in
	 * @param position
	 *            Position to draw in
	 * @param player_color
	 *            The color of the wall
	 * @param index
	 *            The index of the wall
	 * @see CTerrainMap
	 */
	public final void Draw3DDamagedWallTile(CGame game, Vector2 position, EPlayerColor player_color, int index) {
		super.DrawTile(game, position, D3DDamagedWallIndices[player_color.getValue()] + index);
	}
}
