package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import game.animations.DefineConstants;
import game.utils.Log;

/**
 * The main game class.
 */
public class FortNitta extends ApplicationAdapter {

	Pixmap DDoubleBufferPixmap = null;
	Texture DDoubleBufferPixbuf = null;
	Texture DScaledPixbuf = null;
	Pixmap DDrawingAreaPixmap = null;
	private CGame mCGame;
	private long startTime;

	/**
	 * Called once on application initialization.
	 */
	@Override
	public void create() {
		Log.info("Running FortNitta.");
		try {
			startTime = TimeUtils.nanoTime();
			setGame(new CGame());
			configureCamera();
		} catch (Exception ex) {
			Log.critical(ex);
		}
	}

	private void configureCamera() {
		getGame().setCamera(new OrthographicCamera());
		getGame().getCamera().setToOrtho(false);
		getGame().setViewport(
				new FitViewport(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT, getGame().getCamera()));
		Log.debug("Viewport has been set to dimensions: %.0f x %.0f", getGame().getViewport().getWorldWidth(),
				getGame().getViewport().getWorldHeight());
		getGame().getCamera().position.set(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2, 0);
		Log.debug("Camera has been set to position (in screen coordinates): %s", getGame().getCamera().position);
		getGame().getCamera().update();
	}

	public CGame getGame() {
		return mCGame;
	}

	public void setGame(CGame game) {
		mCGame = game;
	}

	@Override
	public void resize(int width, int height) {
		getGame().getViewport().update(width, height);
	}

	/**
	 * Main game loop.
	 */
	@Override
	public void render() {
		// Debug.startMethodTracing("fortnittarender");
		// try {
		// Thread.sleep((long)((float)1000/30 - Gdx.graphics.getDeltaTime()));
		// } catch (InterruptedException e) {
		// Log.critical(e);
		// }
		Log.call();
		try {
			handleInput();
			getGame().Update();

			getGame().getCamera().update();
			// Tie our projection matrix to our camera
			getGame().getSpriteBatch().setProjectionMatrix(getGame().getCamera().combined);

			/* Clear the screen before drawing anything new */
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			/* Tell LibGDX we're about to make a bunch of drawing calls */

			/* Enable alpha transparency */

			/* Call draw() on all the child components of our active game state */
			Pixmap Frame = getGame().Draw();

			// Create the double buffer with the same size as the frame if needed
			if (DDoubleBufferPixmap == null) {
				int FrameWidth = Frame.getWidth();
				int FrameHeight = Frame.getHeight();
				DDoubleBufferPixmap = new Pixmap(FrameWidth, FrameHeight, Pixmap.Format.RGBA8888);
			}
			DDoubleBufferPixmap.drawPixmap(Frame, 0, 0, 0, 0, -1, -1);

			// UpdateDrawingAreaPixmap();

			if (DDoubleBufferPixmap != null) {
				Texture texture = new Texture(DDoubleBufferPixmap);
				getGame().getSpriteBatch().enableBlending();
				getGame().getSpriteBatch().begin();
				getGame().getSpriteBatch().draw(texture, 0, 0);
				getGame().getSpriteBatch().end();
				texture.dispose();
				logFps();
			}

		} catch (Exception ex) {
			Log.critical(ex);
		}
		// Debug.stopMethodTracing();
	}

	public void handleInput() {
		if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1) && Gdx.input.isTouched(2))
			getGame().InputState().ButtonPressed(CInputState.EInputButton.ibRightButton);
		else if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1))
			getGame().InputState().ButtonPressed(CInputState.EInputButton.ibLeftButton);
	}

	public void logFps() {
		if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */{
			Log.debug("FPS: " + Gdx.graphics.getFramesPerSecond());
			startTime = TimeUtils.nanoTime();
		}
	}
}
