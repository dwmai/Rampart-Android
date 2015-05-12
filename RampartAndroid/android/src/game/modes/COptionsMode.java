package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CStackElement;

/**
 * @brief Mode where user edits options
 */
public class COptionsMode extends CMenuMode {

	// @Button for going back to main menu
	private CButton DBackButton;
	// @List of buttons for each option
	private java.util.ArrayList<CButton> DOptions = new java.util.ArrayList<CButton>();

	public COptionsMode(CGame game) {
		super("OPTIONS");
		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2));
		S.Anchor(new Vector2(0.5f, 0.5f));

		CButton M2 = new CButton(game, "SOUND");
		DOptions.add(M2);
		S.AddChildElement(M2);
		DRootElement.AddChildElement(S);

		CButton M3 = new CButton(game, "NETWORK");
		DOptions.add(M3);
		S.AddChildElement(M3);
		DRootElement.AddChildElement(S);

		DBackButton = new CButton(game, "BACK");
		DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
		DBackButton.Anchor(new Vector2(0, 1));
		DRootElement.AddChildElement(DBackButton);

        M2.ConnectVertically(DBackButton);
        M3.ConnectVertically(M2);
        DBackButton.ConnectVertically(M3);
        game.InputState().DAutoSelectElement = M2;

	}

	public void Update(CGame game) {
		super.Update(game);

		if (DBackButton.IsPressed()) {
			game.SwitchMode(new CMainMenuMode(game));
		}
		// if sound option is pressed
		if (DOptions.get(0).IsPressed()) {
			game.SwitchMode(new CSoundOptionsMenuMode(game));
		}

		// if network option is pressed
		if (DOptions.get(1).IsPressed()) {
			game.SwitchMode(new CNetworkOptionsMenuMode(game));
		}

	}

}
