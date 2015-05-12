package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CLabel;
import game.ui.CStackElement;
import game.ui.CTextField;

public class CLoginMenuMode extends CMenuMode {

	private CButton DBackButton;
	private CButton DLoginButton;
	private CLabel DUsernameLabel;
	private CLabel DPasswordLabel;
	private CTextField DUsernameField;
	private CTextField DPasswordField;
	private CLabel DResponseLabel;
	private CLabel DErrorLabel;
	private String DPassword;

	public CLoginMenuMode(CGame game) {
		super("LOG IN");
		DBackButton = new CButton(game, "BACK");
		DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
		DBackButton.Anchor(new Vector2(0, 1));

		DLoginButton = new CButton(game, "LOG IN");
		DLoginButton.Position(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		DLoginButton.Anchor(new Vector2(1, 1));

		DErrorLabel = new CLabel(game, "");
		DErrorLabel.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT));
		DErrorLabel.Anchor(new Vector2(0.5f, 1));

		CStackElement S = new CStackElement();
		S.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2 + 10));
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Anchor(new Vector2(0.5f, 0.5f));

		DUsernameLabel = new CLabel(game, "USERNAME", new Vector2(15, 0));
		S.AddChildElement(DUsernameLabel);
		DUsernameField = new CTextField(game, "");
		DUsernameField.Size(new Vector2(300, 60));
		S.AddChildElement(DUsernameField);
		DPasswordLabel = new CLabel(game, "PASSWORD", new Vector2(15, 0));
		S.AddChildElement(DPasswordLabel);
		DPasswordField = new CTextField(game, "", true);
		DPasswordField.Size(new Vector2(300, 60));
		S.AddChildElement(DPasswordField);
		DPassword = "";

		DRootElement.AddChildElement(DBackButton);
		DRootElement.AddChildElement(DLoginButton);
		DRootElement.AddChildElement(DErrorLabel);
		DRootElement.AddChildElement(S);

        DUsernameField.ConnectVertically(DLoginButton);
        DPasswordField.ConnectVertically(DUsernameField);
        DBackButton.ConnectVertically(DPasswordField);
        DLoginButton.ConnectVertically(DBackButton);

        game.InputState().DAutoSelectElement = DUsernameField;
	}

	public void Enter(CGame game) {
		// TODO: Integrate networking
		// if(!game.GameState().Network().Connect()) {
		// DErrorLabel.Text("ERROR CONNECTING");
		// g_print("Error connecting\n");
		// }
	}

	public void Update(CGame game) {
		super.Update(game);

		if (DBackButton.IsPressed()) {
			game.SwitchMode(new CMainMenuMode(game));
		}
		if (DLoginButton.IsPressed()) {
			game.GameState().DUsername = DUsernameField.TextEntered();
			// TODO: Integrate networking
			// game.GameState().Network().LogIn(DUsernameField.TextEntered(), DPasswordField.TextEntered());
		}
	}
}
