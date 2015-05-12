package game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;

import game.animations.DefineConstants;


/**
 * @brief Stores the pixmaps and graphics state of the game
 */
public class CRendering {
	/**
	 * @brief Initializes pixmaps and buffers
	 * 
	 * @brief Used for the game's everyday drawing needs
	 */
	public Pixmap DWorkingBufferPixmap;
	/**
	 * @brief Used for banner transition as the last frame of previous mode
	 */
	public Pixmap DPreviousWorkingBufferPixmap;
	/**
	 * @brief Used to cache the terrain map's 2D version
	 */
	public Pixmap D2DTerrainPixmap;
	/**
	 * @brief Used to cache the animation of the terrain map's 3D versions
	 */
	public ArrayList<Pixmap> D3DTerrainPixmaps = new ArrayList<Pixmap>();
	/**
	 * @brief Used to cache the banner in the transition mode
	 */
	public Pixmap DBannerPixmap;
	/**
	 * @brief Used to cache the message in the game over mode
	 */
	public Pixmap DMessagePixmap;

	public CRendering(int width, int height) {
		DWorkingBufferPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		DPreviousWorkingBufferPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		for (int Index = 0; Index < DefineConstants.TERRAIN_ANIMATION_TIMESTEPS; Index++) {
			D3DTerrainPixmaps.add(new Pixmap(width, height, Pixmap.Format.RGBA8888));
		}
		D2DTerrainPixmap = null;
		DBannerPixmap = null;
		DMessagePixmap = null;
	}

}
