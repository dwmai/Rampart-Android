package game.tilesets;

import game.CGame;
import game.animations.SBurnAndExplosion;

/**
 * @brief Tileset used for drawing plumes when cannons fire cannonballs
 */
public class C3DBurnTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices to the base frames burn type
	 */
	private int[] D3DBurnIndices = new int[SBurnAndExplosion.EBurnType.btMax.getValue()];

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

		D3DBurnIndices[SBurnAndExplosion.EBurnType.btRubbleBurn0.getValue()] = this.FindTile("rubbleburn-0");
		D3DBurnIndices[SBurnAndExplosion.EBurnType.btRubbleBurn1.getValue()] = this.FindTile("rubbleburn-alt-0");
		D3DBurnIndices[SBurnAndExplosion.EBurnType.btHoleBurn0.getValue()] = this.FindTile("holeburn-0");
		D3DBurnIndices[SBurnAndExplosion.EBurnType.btHoleBurn1.getValue()] = this.FindTile("holeburn-alt-0");

		return true;
	}

	/**
	 * @brief Gets the index of the base frame of the animation for a burn type
	 * @param burnType
	 *            The type of burn animation to play
	 * @return The index of the base frame
	 */
	public final int GetBaseFrame(SBurnAndExplosion.EBurnType burnType) {
		return D3DBurnIndices[burnType.getValue()];
	}
}
