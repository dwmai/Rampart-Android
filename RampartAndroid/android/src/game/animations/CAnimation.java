package game.animations;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.tilesets.CGraphicTileset;


/**
 * @brief An sprite-based animation that has a frame count and a base frame to start from
 */
public abstract class CAnimation {

	public int tileType = 0;
	/**
	 * @brief The index of the first frame in the tileset
	 */
	protected int DBaseFrameIndex;
	/**
	 * @brief Which frame the animation is currently at
	 */
	protected int DFrameIndex;
	/**
	 * @brief The number of frames in the animation
	 */
	protected int DFrameCount;
	/**
	 * @brief The position of the animation
	 */
	protected Vector2 DPosition = new Vector2();
	/**
	 * @brief The tile index for the animation
	 */
	protected Vector2 DIndex = new Vector2();

	/**
	 * @brief Makes a new animation at a position and frame parameters
	 * @param position
	 *            The position to draw at
	 * @param base_frame_index
	 *            The index in tileset of the first frame
	 * @param frame_count
	 *            The number of frames in the animation
	 */
	public CAnimation(Vector2 position, Vector2 index, int base_frame_index, int frame_count) {
		DBaseFrameIndex = base_frame_index;
		DFrameIndex = 0;
		DFrameCount = frame_count;
		DPosition = new Vector2(position);
		DIndex = new Vector2(index);
	}

	/**
	 * @brief Virtual destructorr
	 */
	public void dispose() {
	}

	/**
	 * @brief Moves to the next frame
	 * @param game
	 *            Game being animated in
	 */
	public void Update(CGame game) {
		DFrameIndex++;
	}

	/**
	 * @brief Pure virtual implemented by subclasses to specify which tileset to use
	 * @param game
	 *            The game to draw in
	 */
	public abstract void Draw(CGame game);

	/**
	 * @brief Determines if animation should continue or has reached the end based on the current frame and the frame
	 *        count
	 * @return True if there are more frames to draw
	 */
	public final boolean ShouldContinuePlaying() {
		return DFrameIndex < DFrameCount;
	}

	public final Vector2 Position() {
		return DPosition;
	}

	public final Vector2 Index() {
		return DIndex;
	}

	/**
	 * @brief Internal drawing that draws the frame at the position given the tileset
	 * @param game
	 *            Game to draw in
	 * @param tileset
	 *            Tileset to draw with
	 */
	protected final void Draw(CGame game, CGraphicTileset tileset) {
		tileset.DrawTile(game, new Vector2(DPosition), DBaseFrameIndex + DFrameIndex);
	}
}
