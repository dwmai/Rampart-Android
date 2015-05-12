package game.animations;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


/**
 * @brief The animation played while a wall or floor burns
*/
public class CShipExplosionAnimation extends CAnimation {
/**
*
* @brief Makes a new animation at a position
*
* @param game The game playing
* @param position The position to play at
* @param direction The direction the ship is facing

*/

    public CShipExplosionAnimation(CGame game, Vector2 position, Vector2 index, SDirection.EValue direction) {
        super(new Vector2(position), new Vector2(index), game.Resources().DTilesets.D3DShipsExplosionTileset.GetBaseFrame(direction), game.Resources().DTilesets.D3DShipsExplosionTileset.TileCount() / 2);
        DAnimationStep = 0;
    }

    /**
*
* @brief Draws the animation using the ship explosion tileset
*
* @param game The game drawing

*/
    public void Draw(CGame game) {
        super.Draw(game, game.Resources().DTilesets.D3DShipsExplosionTileset);
    }

    /**
*
* @brief Updates the animation slowly
*
* @param game The game updating

*/
    public void Update(CGame game) {
        DFrameIndex += (DAnimationStep++ % 2);
    }

    /**
*
* @brief Used to slow down the animation

*/
    public int DAnimationStep;
}