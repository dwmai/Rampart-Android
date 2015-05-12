package game.animations;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;

/**
 * @brief The animation played when a cannon fires
 */
public class CPlumeAnimation extends CAnimation {
	/**
	 * @brief Makes a new animation at a position, facing a direction
	 * @param game
	 *            The game playing
	 * @param position
	 *            The position to play at
	 * @param direction
	 *            The direction cannon was facing
	 */
	public CPlumeAnimation(CGame game, Vector2 position, Vector2 index, SDirection.EValue direction) {
		super(position.cpy().add(
				new Vector2(game.Resources().DTilesets.D3DTerrainTileset.TileWidth()
						- game.Resources().DTilesets.D3DCannonPlumeTileset.TileWidth() / 2,
						-game.Resources().DTilesets.D3DTerrainTileset.TileHeight())), new Vector2(index),
				game.Resources().DTilesets.D3DCannonPlumeTileset.GetBaseFrame(direction),
				game.Resources().DTilesets.D3DCannonPlumeTileset.TileCount() / SDirection.EValue.dMax.getValue());
	}

	/**
	 * @brief Draws the animation using the cannon plume tileset
	 * @param game
	 *            The game drawing
	 */
	public void Draw(CGame game) {
		super.Draw(game, game.Resources().DTilesets.D3DCannonPlumeTileset);
	}
}
