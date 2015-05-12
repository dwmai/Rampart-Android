package game.animations;

import com.badlogic.gdx.math.Vector2;

import game.CGame;


/**
 * @brief The animation played while a wall or floor burns
 */
public class CBurnAnimation extends CAnimation {

	/**
	 * @brief Makes a new animation at a position
	 * @param game
	 *            The game playing
	 * @param position
	 *            The position to play at
	 * @param burnType
	 *            The type of burn
	 */
	public CBurnAnimation(CGame game, Vector2 position, Vector2 index, SBurnAndExplosion.EBurnType burnType) {
		super(new Vector2(position), new Vector2(index),
				game.Resources().DTilesets.D3DBurnTileset.GetBaseFrame(burnType),
				game.Resources().DTilesets.D3DBurnTileset.TileCount() / SBurnAndExplosion.EBurnType.btMax.getValue());
	}

	/**
	 * @brief Draws the animation using the burn tileset
	 * @param game
	 *            The game drawing
	 */
	public void Draw(CGame game) {
		super.Draw(game, game.Resources().DTilesets.D3DBurnTileset);
	}
}
