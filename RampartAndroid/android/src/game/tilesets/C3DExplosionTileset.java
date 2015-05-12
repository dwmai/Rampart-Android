package game.tilesets;

import game.CGame;
import game.animations.SBurnAndExplosion;

/**
 * @brief Tileset used for drawing plumes when cannons fire cannonballs
 */
public class C3DExplosionTileset extends CGraphicTileset {

	/**
	 * @brief Stores the indices to the base frames burn type
	 */
	private int[] D3DExplosionIndices = new int[SBurnAndExplosion.EExplosionType.etMax.getValue()];

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

		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etWallExplosion0.getValue()] = this.FindTile("explosion-0");
		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etWallExplosion1.getValue()] = this.FindTile("explosion-alt-0");
		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etWaterExplosion0.getValue()] = this.FindTile("water-explosion-0");
		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etWaterExplosion1.getValue()] = this.FindTile("water-explosion-alt-0");
		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etGroundExplosion0.getValue()] = this.FindTile("ground-explosion-0");
		D3DExplosionIndices[SBurnAndExplosion.EExplosionType.etGroundExplosion1.getValue()] = this.FindTile("ground-explosion-alt-0");
		return true;
	}

	/**
	 * @brief Gets the index of the base frame of the animation for a burn type
	 * @param burnType
	 *            The type of burn animation to play
	 * @return The index of the base frame
	 */
	public final int GetBaseFrame(SBurnAndExplosion.EExplosionType explosionType) {
		return D3DExplosionIndices[explosionType.getValue()];
	}
}
