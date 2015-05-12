package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.animations.DefineConstants;
import game.modes.CGameMode;
import game.modes.CMainMenuMode;
import game.utils.CameraUtils;
import game.utils.Log;


/*
 This is the Linux equivalent of the Game class
 Before I forget: GameState != GameMode. GameState is a helper class of variables. GameMode is what SelectCastleMode inherits from.
 * */
public class CGame implements InputProcessor {

    public boolean DSinglePlayer;
	/**
	 * @brief Holds the state of input in the game
	 */
	protected CInputState DInputState;
	/**
	 * @brief Holds pixmaps used to render frames
	 */
	protected CRendering DRendering;
	/**
	 * @brief Holds tilesets and sounds of game
	 */
	protected CResources DResources;
	/**
	 * @brief Holds the state of the game
	 */
	protected CGameState DGameState;
	/**
	 * @brief The current mode of the game
	 */
	protected CGameMode DGameMode;
	/**
	 * @brief The mode switching next to
	 */
	protected CGameMode DNextGameMode;

    protected boolean DIsMultiplayer;
	/* Framework-related data */
	private SpriteBatch mSpriteBatch;
	private OrthographicCamera mCamera;
	private Viewport mViewport;
	/* From Linux Refactoring */
	private CResources mResources;

	public CGame() {
		setSpriteBatch(new SpriteBatch());
        DIsMultiplayer = false;
		DNextGameMode = null;
		DGameMode = null;
        DSinglePlayer = true;

		DInputState = new CInputState();
		DGameState = new CGameState();
		DRendering = new CRendering(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT);
		DResources = new CResources();

        Log.info("Loading resources...");
        long startTime = System.nanoTime();
        DResources.Load(this, DefineConstants.MUSIC_VOLUME, DefineConstants.SOUNDEFFECT_VOLUME);
        long estimatedTime = System.nanoTime() - startTime;
        Log.info(String.format("Finished loading resources in %d seconds.", estimatedTime / 1000 / 1000 / 1000));
		Gdx.input.setInputProcessor(this);
		SwitchMode(new CMainMenuMode(this));
	}

	public final void SwitchMode(CGameMode new_mode) {
		DNextGameMode = new_mode;
        GameState().DTimer.DIsAudible = false;
        GameState().DTimer.DIsVisible = false;
	}

	/**
	 * @brief Handles mouse button and hands off to DInputState
	 * @param button
	 *            If the mouse button was pressed
	 */
	public final void ButtonPressed(CInputState.EInputButton button) {
		DInputState.ButtonPressed(button);
	}

	public final void Update() {
        // TODO: Escape key
        if (DInputState.DEscapePressed) {
            if (DSinglePlayer) {
                SwitchMode(new CMainMenuMode(this));
            }
        }
		if (DNextGameMode != DGameMode) {
			if (DGameMode != null) {
				DGameMode.Leave(this);
			}
            DInputState.SelectedElement(null);
			DNextGameMode.Enter(this);
			if (DGameMode != null) {
				if (DGameMode != null) {
				}
				// DGameMode.Dispose();
			}
			DGameMode = DNextGameMode;
		}

		DGameMode.Update(this);

		DInputState.Reset();
	}

	public Pixmap Draw() {
		DGameMode.Draw(this);
		return DRendering.DWorkingBufferPixmap;
	}

	/* From Linux Refactoring */
	/**
	 * @brief Getter for resources
	 * @return DResources
	 */
	public final CResources Resources() {
		return DResources;
	}

	/**
	 * @brief Getter for rendering
	 * @return DRendering
	 */
	public final CRendering Rendering() {
		return DRendering;
	}

	/**
	 * @brief Getter for game mode
	 * @return DGameMode
	 */
	public final CGameMode GameMode() {
		return DGameMode;
	}

	/**
	 * @brief Getter for the game stats
	 * @return DGameState
	 */
	public final CGameState GameState() {
		return DGameState;
	}

    public final boolean IsMultiplayer() {
        return DIsMultiplayer;
    }
    public final void SetMultiplayer(boolean Multiplayer) {
        DIsMultiplayer = Multiplayer;
    }

	/**
	 * Gets the SpriteBatch used for drawing operations.
	 */
	public SpriteBatch getSpriteBatch() {
		return mSpriteBatch;
	}

	/**
	 * Sets the current SpriteBatch.
	 */
	public void setSpriteBatch(SpriteBatch spriteBatch) {
		mSpriteBatch = spriteBatch;
	}

	/**
	 * Gets the current viewport.
	 */
	public Viewport getViewport() {
		return mViewport;
	}

	/**
	 * Sets the current viewport.
	 */
	public void setViewport(Viewport viewport) {
		mViewport = viewport;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Handled by ShouldTakePrimaryAction in HumanPlayer
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		InputState().DMousePosition = CameraUtils.unproject(getCamera(), screenX, (Gdx.graphics.getHeight() - screenY));
		InputState().DMousePosition.x -= InputState().DMousePosition.x % 12;
		InputState().DMousePosition.y -= InputState().DMousePosition.y % 12;
		return true;
	}

	/**
	 * @brief Getter for input state
	 * @return DInputState
	 */
	public final CInputState InputState() {
		return DInputState;
	}

	/**
	 * Gets the camera used to project and unproject screen-to-device coordinates.
	 */
	public OrthographicCamera getCamera() {
		return mCamera;
	}

	/**
	 * Sets the current camera.
	 */
	public void setCamera(OrthographicCamera camera) {
		mCamera = camera;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
