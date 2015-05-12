package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.CTerrainMap;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CLabel;
import game.ui.CStackElement;

//*
/**
 * @brief Mode where user selects a map
 */
public class CMapSelectMenuMode extends CMenuMode {
	/**
	 * @brief Sets up UI and maps
	 * @param game
	 *            Game playing
	 */
	private boolean DIsMultiplayer;
	private boolean DIsBroadcast;
	/**
	 * @brief Button for going back to main menu
	 */
	private CButton DBackButton;
	/**
	 * @brief List of button for each map
	 */
	private java.util.ArrayList<CButton> DMaps = new java.util.ArrayList<CButton>();
	/**
	 * @brief The number of players supported by hovered map
	 */
	private CLabel PlayerCountLabel;
	/**
	 * @brief The size of hovered map
	 */
	private CLabel SizeLabel;
	/**
	 * @brief Position of the Map Select Buttons
	 */
	private Vector2 DText = new Vector2();
	/**
	 * @brief Index of the minimap to be drawn -1 Indicates that there is no minimap to be drawn
	 */
	private int DMapIndex;
	/**
	 * @brief Position of the Mini Map
	 */
	private Vector2 DMini = new Vector2();
	/**
	 * @brief This contains the Map Size and number of players
	 */
	private CStackElement DMapInfo;
	/**
	 * @brief This displays the Size of the highlighted map
	 */
	private CLabel DMapSize;
	/**
	 * @brief This displays the Number of Players of the highlighted map
	 */
	private CLabel DNumPlayers;

	public CMapSelectMenuMode(CGame game, boolean multiplayer) {
		this(game, multiplayer, false);
	}

	public CMapSelectMenuMode(CGame game, boolean multiplayer, boolean broadcast) {
		super("SELECT MAP");
		DIsMultiplayer = multiplayer;
		DIsBroadcast = broadcast;
		DText.x = 30;
		DText.y = 100;
		DMapIndex = -1;

        game.SetMultiplayer(multiplayer);

		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, 700));
		S.Position(new Vector2(DText.x, DText.y));
		S.Anchor(new Vector2(0, 0));

		DBackButton = new CButton(game, "BACK");
		DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
		DBackButton.Anchor(new Vector2(0, 1));

		DMapInfo = new CStackElement();
		DMapInfo.Size(new Vector2(DefineConstants.GAME_WIDTH, 700));
		DMapInfo.Anchor(new Vector2(0, 0));

		DNumPlayers = new CLabel(game, "");
		DMapInfo.AddChildElement(DNumPlayers);

		DMapSize = new CLabel(game, "");
		DMapInfo.AddChildElement(DMapSize);

		// Add all maps to map select
		for (CTerrainMap Map : game.Resources().DTerrainMaps) {
			CButton M = new CButton(game, Map.MapName());
			DMaps.add(M);
			S.AddChildElement(M);
		}

		DRootElement.AddChildElement(DBackButton);
		DRootElement.AddChildElement(S);
		DRootElement.AddChildElement(DMapInfo);

        game.InputState().DAutoSelectElement = DMaps.get(0);
        DMaps.get(0).ConnectVertically(DBackButton);
        for (int i = 1; i < DMaps.size(); i++) {
            DMaps.get(i).ConnectVertically(DMaps.get(i - 1));
        }
        DBackButton.ConnectVertically(DMaps.get(DMaps.size() - 1));
	}

	public CMapSelectMenuMode(CGame game) {
		this(game, false, false);
	}

    boolean alreadyPressed = false;
	public void Update(CGame game) {
		super.Update(game);

		if (DBackButton.IsPressed()) {
			game.SwitchMode(new CMainMenuMode(game));
		}

		// Initializing Index and Position for minimap to Default
		DMapIndex = -1;
		DMini.x = DefineConstants.GAME_WIDTH - (game.Resources().DTilesets.DBrickTileset.TileWidth() * 3) / 2;
		DMini.y = DText.y;

		DNumPlayers.Text("");
		DMapSize.Text("");

		// Get the highlighted map and modify the location for it
		for (int Index = 0; Index < game.Resources().DTerrainMaps.size(); Index++) {
			if (DMaps.get(Index).IsPressed() && !alreadyPressed) {
                alreadyPressed = true;
				game.GameState().Reset();
				game.GameState().TerrainMap(game.Resources().DTerrainMaps.get(Index));
				game.GameState().TerrainMap().LoadMapTileset(game);
                game.Resources().DSounds.SoundLibraryMixer().LoadMapLibrary(game.Resources().DTerrainMaps.get(Index).SongPath());

				if (DIsMultiplayer) {
					// TODO: Implement multiplayer
                    // game.GameState().DRoom.DIsMultiplayer = true;
					// game.GameState().DRoom.DPlayerCount = game.GameState().TerrainMap().PlayerCount();
					// game.GameState().Network().CreateRoom(game.GameState().DRoom.PlayerCount(),
					// game.GameState().DRoom.Name());
				} else {
                    //game.GameState().DRoom.DIsMultiplayer = false;
					game.SwitchMode(new CGameOptionsMenuMode(game));
				}
			} else if (DMaps.get(Index).IsSelected()) {
				DMini.x -= game.Resources().DTerrainMaps.get(Index).Width() * 2;
				DMapIndex = Index;
				UpdateMiniMap(game);
			}
		}
	}

	public void Draw(CGame game) {
		super.Draw(game);

		if (0 <= DMapIndex) {
			// Drawing the Map Preview
			game.Resources().DTerrainMaps.get(DMapIndex).DrawPreviewMap(game.Rendering().DWorkingBufferPixmap,
					(int) DMini.x, (int) DMini.y);
		}

	}

	/**
	 * @brief Drawing the selected map preview
	 */
	private void UpdateMiniMap(CGame game) {

		// game->Resources()->DTerrainMaps[0];
		Vector2 DInfo = new Vector2();

		DNumPlayers.Text(game.Resources().DTerrainMaps.get(DMapIndex).PlayerCount() + " PLAYERS");
		DMapSize.Text(game.Resources().DTerrainMaps.get(DMapIndex).Width() + " x "
				+ game.Resources().DTerrainMaps.get(DMapIndex).Height());

		// Modifying the X and Y posityions to add text below map
		DInfo.x = DMini.x + game.Resources().DTerrainMaps.get(DMapIndex).Width();
		DInfo.y = DMini.y + game.Resources().DTerrainMaps.get(DMapIndex).Height() * 2; // + TextHeight/2;

		Vector2 LabelSize = DNumPlayers.Size();
		DInfo.x = DInfo.x - LabelSize.x / 2;

		DMapInfo.Position(DInfo);

	}
}
