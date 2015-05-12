package game.modes;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import game.CAIPlayer;
import game.CGame;
import game.CWind;
import game.EAIDifficulty;
import game.EPlayerColor;
import game.animations.DefineConstants;
import game.players.CHumanPlayer;
import game.players.CPlayer;
import game.ui.CButton;
import game.ui.CSpinner;
import game.ui.CStackElement;

/**
 * @brief Mode with menu including wind type, player color, AI difficulty
 */
public class CGameOptionsMenuMode extends CMenuMode {
	/**
	 * @brief Makes a new game options menu mode
	 * @param game
	 *            Game playing
	 * 
	 * @brief Button for going back to map select screen
	 */
	private CButton DBackButton;
	/**
	 * @brief Button for starting game
	 */
	private CButton DContinueButton;
	/**
	 * @brief Spinner for setting wind type
	 */
	private CSpinner DWindTypeSpinner;
	/**
	 * @brief Spinner for setting player color
	 */
	private CSpinner DPlayerColorSpinner;
	/**
	 * @brief Spinner for setting AI difficulty
	 */
	private CSpinner DAIDifficultySpinner;
	/**
	 * @brief Map for wind type string name to enum value
	 */
	private java.util.HashMap<String, CWind.EWindType> DWindTypeMap = new java.util.HashMap<String, CWind.EWindType>();
	/**
	 * @brief Map for player color string name to enum value
	 */
	private java.util.HashMap<String, EPlayerColor> DPlayerColorMap = new java.util.HashMap<String, EPlayerColor>();
	/**
	 * @brief Map for AI difficulty string name to enum value
	 */
	private java.util.HashMap<String, EAIDifficulty> DAIDifficultyMap = new java.util.HashMap<String, EAIDifficulty>();
    private boolean DIsMultiplayer;

	public CGameOptionsMenuMode(CGame game) {
		super("GAME SELECTION");

		DBackButton = new CButton(game, "BACK");
		DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
		DBackButton.Anchor(new Vector2(0, 1));

		DContinueButton = new CButton(game, "CONTINUE");
		DContinueButton.Position(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		DContinueButton.Anchor(new Vector2(1, 1));

		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Position(new Vector2(DefineConstants.GAME_WIDTH - 50, DefineConstants.GAME_HEIGHT / 2));
		S.Anchor(new Vector2(1f, 0.5f));

		int LongestHorizontalSize = 0;

		java.util.ArrayList<String> options = new java.util.ArrayList<String>();
		options.add("None");
		options.add("Mild");
		options.add("Moderate");
		options.add("Erratic");
		DWindTypeSpinner = new CSpinner(game, "WIND TYPE", new ArrayList<String>(options));
		DWindTypeSpinner.Anchor(new Vector2(0.5f, 0f));
		LongestHorizontalSize = Math.max(DWindTypeSpinner.LongestHorizontalSize(), LongestHorizontalSize);

		options.clear();
		options.add("Blue");
		options.add("Red");
		DPlayerColorSpinner = new CSpinner(game, "PLAYER COLOR", new ArrayList<String>(options));
		DPlayerColorSpinner.Anchor(new Vector2(0.5f, 0f));
		LongestHorizontalSize = Math.max(DPlayerColorSpinner.LongestHorizontalSize(), LongestHorizontalSize);

		options.clear();
		options.add("Easy");
		options.add("Normal");
		options.add("Hard");
		options.add("Insane");
		DAIDifficultySpinner = new CSpinner(game, "AI DIFFICULTY", new ArrayList<String>(options));
		DAIDifficultySpinner.Anchor(new Vector2(0.5f, 0f));
		LongestHorizontalSize = Math.max(DAIDifficultySpinner.LongestHorizontalSize(), LongestHorizontalSize);

		DWindTypeSpinner.LongestHorizontalSize(LongestHorizontalSize);
		DWindTypeSpinner.LayoutElements();
		DPlayerColorSpinner.LongestHorizontalSize(LongestHorizontalSize);
		DPlayerColorSpinner.LayoutElements();
		DAIDifficultySpinner.LongestHorizontalSize(LongestHorizontalSize);
		DAIDifficultySpinner.LayoutElements();

		S.AddChildElement(DWindTypeSpinner);
		S.AddChildElement(DPlayerColorSpinner);
		S.AddChildElement(DAIDifficultySpinner);

		DRootElement.AddChildElement(DBackButton);
		DRootElement.AddChildElement(DContinueButton);
		DRootElement.AddChildElement(S);

        DWindTypeSpinner.OptionName().ConnectVertically(DContinueButton);
        DPlayerColorSpinner.OptionName().ConnectVertically(DWindTypeSpinner.OptionName());
        DAIDifficultySpinner.OptionName().ConnectVertically(DPlayerColorSpinner.OptionName());
        DBackButton.ConnectVertically(DAIDifficultySpinner.OptionName());
        DContinueButton.ConnectVertically(DBackButton);

        game.InputState().DAutoSelectElement = DContinueButton;

		DWindTypeMap.put("None", CWind.EWindType.wtNone);
		DWindTypeMap.put("Mild", CWind.EWindType.wtMild);
		DWindTypeMap.put("Moderate", CWind.EWindType.wtModerate);
		DWindTypeMap.put("Erratic", CWind.EWindType.wtErratic);

		DPlayerColorMap.put("Blue", EPlayerColor.pcBlue);
		DPlayerColorMap.put("Red", EPlayerColor.pcRed);

		DAIDifficultyMap.put("Easy", EAIDifficulty.aiEasy);
		DAIDifficultyMap.put("Normal", EAIDifficulty.aiNormal);
		DAIDifficultyMap.put("Hard", EAIDifficulty.aiHard);
		DAIDifficultyMap.put("Insane", EAIDifficulty.aiInsane);
	}

	public void Update(CGame game) {
		super.Update(game);
		if (DBackButton.IsPressed()) {
			game.SwitchMode(new CMapSelectMenuMode(game));
		}
		if (DContinueButton.IsPressed()) {

			CPlayer Player = new CHumanPlayer();
			Player.DColor = DPlayerColorMap.get(DPlayerColorSpinner.SelectedOption());
			game.GameState().DPlayers.add(Player);

            if(game.IsMultiplayer()) {
                Player = new CHumanPlayer();
                Player.DColor = DPlayerColorMap.get(DPlayerColorSpinner.SelectedOption()) == EPlayerColor.pcRed ? EPlayerColor.pcBlue : EPlayerColor.pcRed;
                game.GameState().DPlayers.add(Player);

            } else if (game.GameState().TerrainMap().GetMapType().equals("againstAI")) {
                Player = new CAIPlayer(game);
                Player.DColor = DPlayerColorMap.get(DPlayerColorSpinner.SelectedOption()) == EPlayerColor.pcRed ? EPlayerColor.pcBlue : EPlayerColor.pcRed;
                game.GameState().DPlayers.add(Player);
            } else if (game.GameState().TerrainMap().GetMapType().equals("againstShips")) {
                Player = new CAIPlayer(game);
                Player.DColor = DPlayerColorMap.get(DPlayerColorSpinner.SelectedOption()) == EPlayerColor.pcRed ? EPlayerColor.pcBlue : EPlayerColor.pcRed;
                game.GameState().DPlayers.add(Player);
            }
            else {
                Player = new CAIPlayer(game);
                Player.DColor = DPlayerColorMap.get(DPlayerColorSpinner.SelectedOption()) == EPlayerColor.pcRed ? EPlayerColor.pcBlue
                        : EPlayerColor.pcRed;
                game.GameState().DPlayers.add(Player);
            }

			game.GameState().SetAIDifficulty(DAIDifficultyMap.get(DAIDifficultySpinner.SelectedOption()));

            game.GameState().DWind.DWindType = DWindTypeMap.get(DWindTypeSpinner.SelectedOption());

			game.SwitchMode(new CBannerTransitionMode(game, "SELECT CASTLE", this, new CSelectCastleMode()));
		}
	}
}
