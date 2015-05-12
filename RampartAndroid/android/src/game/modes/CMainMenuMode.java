package game.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CStackElement;

/**
 * @brief Mode with menu including single player, multiplayer, and options
 */
public class CMainMenuMode extends CMenuMode {
	/**
	 * @brief Makes a new main menu mode with options
	 * @param game
	 *            Game playing
	 * 
	 * @brief Button for single player mode
	 */
	private CButton DSinglePlayerButton;
	private CButton DMultiplayerButton;
	private CButton DOptionsButton;
	private CButton DExitButton;

	public CMainMenuMode(CGame game) {
		super("FORT NITTA");
		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2 + 20));
		S.Anchor(new Vector2(0.5f, 0.5f));

		Vector2 Margin = new Vector2(15, 8);

		DSinglePlayerButton = new CButton(game, "SINGLEPLAYER", Margin);
		DSinglePlayerButton.Anchor(new Vector2(0.5f, 0f));

		DMultiplayerButton = new CButton(game, "MULTIPLAYER", Margin);
		DMultiplayerButton.Anchor(new Vector2(0.5f, 0f));

		DOptionsButton = new CButton(game, "OPTIONS", Margin);
		DOptionsButton.Anchor(new Vector2(0.5f, 0f));

		DExitButton = new CButton(game, "EXIT", Margin);
		DExitButton.Anchor(new Vector2(0.5f, 0f));

        DSinglePlayerButton.ConnectVertically(DExitButton);
        DMultiplayerButton.ConnectVertically(DSinglePlayerButton);
        DOptionsButton.ConnectVertically(DMultiplayerButton);
        DExitButton.ConnectVertically(DOptionsButton);

        game.InputState().DAutoSelectElement = DSinglePlayerButton;

		// test spinner element
		// std::vector<std::string> options;
		// options.push_back("None");
		// options.push_back("Mild");
		// options.push_back("Moderate");
		// options.push_back("Erratic");
		// DTestOptions = new CSpinner(game, "WIND TYPE", &options);
		// S->AddChildElement(DTestOptions);
		// DTestOptions = new CSpinner(game, "WIND TYPE", &options, DEFAULT_LABEL_MARGIN);
		// S->AddChildElement(DTestOptions);

		S.AddChildElement(DSinglePlayerButton);
		S.AddChildElement(DMultiplayerButton);
		S.AddChildElement(DOptionsButton);
		S.AddChildElement(DExitButton);
		// S->AddChildElement(DTestOptions);
		DRootElement.AddChildElement(S);
	}

	public void Update(CGame game) {
		super.Update(game);

		if (DSinglePlayerButton.IsPressed()) {
            game.DSinglePlayer = true;
			game.SwitchMode(new CMapSelectMenuMode(game));
		}
		if (DMultiplayerButton.IsPressed()) {
            game.DSinglePlayer = false;
			game.SwitchMode(new CLoginMenuMode(game));
		}
		if (DOptionsButton.IsPressed()) {
			game.SwitchMode(new COptionsMode(game));
		}
		if (DExitButton.IsPressed()) {
			Gdx.app.exit();
		}
	}
	// CSpinner* DTestOptions;
}
