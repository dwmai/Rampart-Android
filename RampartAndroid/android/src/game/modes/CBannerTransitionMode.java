package game.modes;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.CRendering;
import game.animations.DefineConstants;
import game.sounds.CSounds;
import game.ui.CLabel;
import game.ui.CStackElement;
import game.ui.CUIElement;
import game.utils.Log;

/**
 * @brief A mode that shows a banner that falls across the screen and then goes to the next mode and performs a seamless
 *        transition
 */
public class CBannerTransitionMode extends CMenuMode {
	/**
	 * @brief Creates a new transition with the specified title and game modes transitioning from and to
	 * @param game
	 *            Game transitioning
	 * @param title
	 *            The message to show on the banner
	 * @param previous
	 *            The game mode coming from
	 * @param next
	 *            The game mode going to
	 * 
	 * @brief Pointer to previous mode transitioning from
	 */
	protected CGameMode DPreviousGameMode;
	/**
	 * @brief Pointer to mode that will be switched to after transition
	 */
	protected CGameMode DNextGameMode;
	/**
	 * @brief The position of the banner on screen
	 */
	protected int DBannerPosition;
	/**
	 * @brief The message in banner of the transition
	 */
	protected String DTitle;
	/**
	 * @brief The element used to layout the banner
	 */
	protected CStackElement DStackElement;

	public CBannerTransitionMode(CGame game, String title, CGameMode previous, CGameMode next) {
		super("");
		DPreviousGameMode = previous;
		DNextGameMode = next;

		DRootElement = new CUIElement();

		DStackElement = new CStackElement();
		DStackElement.Size(new Vector2(DefineConstants.GAME_WIDTH, 0));
		DStackElement.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, 0));
		DStackElement.Anchor(new Vector2(0.5f, 0));

		CLabel L = new CLabel(game, title, new Vector2(15, 15), (game.Resources().DTilesets.DWhiteFont));
		DStackElement.AddChildElement(L);

		DRootElement.AddChildElement(DStackElement);
	}

	/**
	 * @brief Moves the banner down and switches to next mode if banner has passed screen
	 * @param game
	 *            The game updating
	 */
	public void Update(CGame game) {
		super.Update(game);
        boolean WasBannerPositionNegative = DBannerPosition < 0;
		DBannerPosition += 5;
		if (DBannerPosition > DefineConstants.GAME_HEIGHT) {
			game.SwitchMode(DNextGameMode);
		}
        if(WasBannerPositionNegative && DBannerPosition >= 0) {
            game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctTransition);
        }
	}

	/**
	 * @brief Draws the banner, the previous mode's frame, and the next mode's frame to provide a seamless transition
	 * @param game
	 *            The game drawing in
	 */
	public void Draw(CGame game) {
        game.Resources().DSounds.StopSong();
		CRendering Rendering = game.Rendering();
		DNextGameMode.Draw(game);
		Rendering.DWorkingBufferPixmap.drawPixmap(Rendering.DPreviousWorkingBufferPixmap, 0, DBannerPosition, 0,
				DBannerPosition, -1, DefineConstants.GAME_HEIGHT - DBannerPosition);
		Rendering.DWorkingBufferPixmap.drawPixmap(Rendering.DBannerPixmap, 0, DBannerPosition, 0, 0, -1, -1);
	}

	/**
	 * @brief Sets up the rendering pixmaps necessary and caches the banner and previous mode
	 * @param game
	 *            The game entering
	 */
	public void Enter(CGame game) {
		game.Rendering().DBannerPixmap = new Pixmap((int) DStackElement.Size().x, (int) DStackElement.Size().y,
				Pixmap.Format.RGBA8888);
		CacheBanner(game);
		CachePreviousMode(game);
		DBannerPosition = -(int) DStackElement.Size().y;

        DNextGameMode.WillEnter(game);
	}

	/**
	 * @brief Cleans up the previous working pixmap
	 * @param game
	 *            The game leaving
	 */
	public void Leave(CGame game) {
		if (game.Rendering().DBannerPixmap != null) {
			try {
				game.Rendering().DBannerPixmap.dispose();
			} catch (Exception ex) {
				Log.critical(ex, "Error while disposing DBannerPixmap while leaving game mode.");
			}
			game.Rendering().DBannerPixmap = null;
		}
	}

	/**
	 * @brief Caches the banner into its pixmap to improve performance
	 * @param game
	 *            The game to cache in
	 */
	protected final void CacheBanner(CGame game) {
		Pixmap OriginalPixmap = game.Rendering().DWorkingBufferPixmap;
		game.Rendering().DWorkingBufferPixmap = game.Rendering().DBannerPixmap;
		DrawBanner(game);
		game.Rendering().DWorkingBufferPixmap = OriginalPixmap;

	}

	/**
	 * @brief Caches a frame from the previous mode to improve performance
	 * @param game
	 *            The game to cache in
	 */
	protected final void CachePreviousMode(CGame game) {
		Pixmap OriginalPixmap = game.Rendering().DWorkingBufferPixmap;
		game.Rendering().DWorkingBufferPixmap = game.Rendering().DPreviousWorkingBufferPixmap;
		DPreviousGameMode.Draw(game);
		game.Rendering().DWorkingBufferPixmap = OriginalPixmap;
	}

	/**
	 * @brief Draws the banner during the caching of the message
	 * @param game
	 *            The game to draw in
	 */
	protected final void DrawBanner(CGame game) {
		DrawTextBackgroundFrame(game, DStackElement.Size());
		DrawBrickFrame(game, new Vector2(0, 0), DStackElement.Size());
		DrawMortar(game, new Vector2(0, 0), DStackElement.Size());
		DRootElement.Draw(game);
	}

}
