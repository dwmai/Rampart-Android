package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CLabel;
import game.ui.CStackElement;
import game.ui.CTextField;

/**
 * @brief Mode where user edits options
 */
public class CNetworkOptionsMenuMode extends CMenuMode {

	// @Button for going back to main menu
	private CButton DBackButton;
	// @List of buttons for each option
	private java.util.ArrayList<CButton> DOptions = new java.util.ArrayList<CButton>();
	/**
	 * @brief The connection string for the network
	 */
	private CTextField DConnectionStringField;

	public CNetworkOptionsMenuMode(CGame game) {
		super("NETWORK OPTIONS");
		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2));
		S.Anchor(new Vector2(0.5f, 0.5f));

		DBackButton = new CButton(game, "BACK");
		DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
		DBackButton.Anchor(new Vector2(0, 1));

		CLabel ConnectionStringLabel = new CLabel(game, "SERVER");
		S.AddChildElement(ConnectionStringLabel);

		// TODO: Integrate networking
		// DConnectionStringField = new CTextField(game, game.GameState().Network().GetConnectionString());
		DConnectionStringField = new CTextField(game, "TODO");
		DConnectionStringField.Size(new Vector2(300, 60));
		S.AddChildElement(DConnectionStringField);

		DRootElement.AddChildElement(S);
		DRootElement.AddChildElement(DBackButton);

        DBackButton.ConnectVertically(DConnectionStringField);
        DConnectionStringField.ConnectVertically(DBackButton);

        game.InputState().DAutoSelectElement = DBackButton;
	}

	public void Update(CGame game) {
		super.Update(game);

		if (DBackButton.IsPressed()) {
			// TODO: Integrate networking
			// game.GameState().Network().SetConnectionString(DConnectionStringField.TextEntered());
			game.SwitchMode(new COptionsMode(game));
		}

	}

}
