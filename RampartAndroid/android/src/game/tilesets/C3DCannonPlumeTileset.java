package game.tilesets;

import game.CGame;
import game.SDirection;

/**
 * @brief Tileset used for drawing plumes when cannons fire cannonballs
 */
public class C3DCannonPlumeTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices to the base frames for each direction
	 */
	private int[] D3DCannonPlumeIndices = new int[SDirection.EValue.dMax.getValue()];

	/**
	 * @brief Loads tileset
	 * @param game
	 *            The game to load in
	 * @param filename
	 *            The name of the dat file
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		D3DCannonPlumeIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("north-0");
		D3DCannonPlumeIndices[SDirection.EValue.dNorthEast.getValue()] = this.FindTile("northeast-0");
		D3DCannonPlumeIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("east-0");
		D3DCannonPlumeIndices[SDirection.EValue.dSouthEast.getValue()] = this.FindTile("southeast-0");
		D3DCannonPlumeIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("south-0");
		D3DCannonPlumeIndices[SDirection.EValue.dSouthWest.getValue()] = this.FindTile("southwest-0");
		D3DCannonPlumeIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("west-0");
		D3DCannonPlumeIndices[SDirection.EValue.dNorthWest.getValue()] = this.FindTile("northwest-0");

		return true;
	}

	/**
	 * @brief Gets the index of the base frame of the animation for a plume facing a direction for a cannon
	 * @param direction
	 *            The direction the cannon is facing
	 * @return The index of the base frame
	 */
	public final int GetBaseFrame(SDirection.EValue direction) {
		return D3DCannonPlumeIndices[direction.getValue()];
	}
}
