package game.modes;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.CRendering;
import game.EPlayerColor;
import game.animations.DefineConstants;
import game.players.CPlayer;
import game.sounds.CSounds;
import game.ui.CLabel;
import game.ui.CStackElement;
import game.ui.CUIElement;
import game.utils.Log;

/**
 * @brief Mode that shows who won
 */
public class CGameOverMode extends CMapMode {
	/**
	 * @brief Makes a new game over mode and sets up UI
	 * 
	 * @brief The root UI element used to draw message
	 */
	protected CUIElement DRootElement;
	/**
	 * @brief The element used to stack the labels for the message
	 */
	protected CStackElement DStackElement;
    /**
*
* @brief Stores the winner from multiplayer mode

*/
    protected int DWinnerID;
    /**
*
* @brief Whether this is a multiplayer game over

*/
    protected boolean DIsMultiplayer;

	public CGameOverMode() {
        this(-1);
    }
    public CGameOverMode(int winner_id) {
            DWinnerID = winner_id;
            DIsMultiplayer = DWinnerID != -1;
            DRootElement = new CUIElement();
            DStackElement = new CStackElement();
            DStackElement.Anchor(new Vector2(0.5f, 0));
        }
	/**
	 * @brief Layouts the message and sets up the message rendering
	 * @param game
	 *            The game entering
	 */
	public void Enter(CGame game) {
		super.Enter(game);

        CPlayer LivingPlayer;
        EPlayerColor PlayerColor;
        if(DIsMultiplayer) {
            LivingPlayer = game.GameState().GetPlayerWithColor(CPlayer.GetColorForID(DWinnerID));
            PlayerColor = LivingPlayer.DColor;
        }
        else {
            LivingPlayer = game.GameState().GetPlayersWithOwnedCastles(game).get(0);
            PlayerColor = game.GameState().GetMainPlayerColor();
        }

		String Message;
		if (EPlayerColor.pcBlue == PlayerColor) {
			Message = "BLUE";
		} else if (EPlayerColor.pcRed == PlayerColor) {
			Message = "RED";
		} else {
			Message = "YELLOW";
		}
		CUIElement S = new CUIElement();
		S.Size(new Vector2(0, 20));
		DStackElement.AddChildElement(S);
		CLabel L = new CLabel(game, Message, new Vector2(0, 0), (game.Resources().DTilesets.DWhiteFont));
		DStackElement.AddChildElement(L);

		L = new CLabel(game, "ARMY", new Vector2(0, 0), (game.Resources().DTilesets.DWhiteFont));
		DStackElement.AddChildElement(L);

        if (DIsMultiplayer || LivingPlayer.DColor == PlayerColor) {
			Message = "CONQUERS";
            game.Resources().DSounds.SwitchSong(CSounds.ESongType.stWin, 1.0f);
		} else {
            game.Resources().DSounds.SwitchSong(CSounds.ESongType.stLoss, 1.0f);
			Message = "DEFEATED";
		}
		L = new CLabel(game, Message, new Vector2(22, 0), (game.Resources().DTilesets.DWhiteFont));
		DStackElement.AddChildElement(L);

		DStackElement.AddChildElement(S);

		DStackElement.Size(new Vector2(L.Size().x, DStackElement.Size().y));
		DStackElement.Position(new Vector2(DStackElement.Size().x, 0));

		DRootElement.AddChildElement(DStackElement);

		game.Rendering().DMessagePixmap = new Pixmap((int) DStackElement.Size().x, (int) DStackElement.Size().y,
				Pixmap.Format.RGBA8888);
		CacheMessage(game);
	}

	/**
	 * @brief Cleans up the message rendering
	 * @param game
	 *            The game leaving
	 */
	public void Leave(CGame game) {
		if (game.Rendering().DMessagePixmap != null) {
			try {
				game.Rendering().DMessagePixmap.dispose();
			} catch (Exception ex) {
				Log.critical(ex, "Error while trying to dispose DMessagePixmap");
			}
			// g_object_unref(game.Rendering().DMessagePixmap);
			game.Rendering().DMessagePixmap = null;
		}
	}
	/**
    * @brief Stores the winner from multiplayer mode
    /*
    protected int DWinnerID;

	/**
	 * @brief Caches the message into the message pixmap
	 * @param game
	 *            The game to cache for
	 */
	protected final void CacheMessage(CGame game) {
		Pixmap OriginalPixmap = game.Rendering().DWorkingBufferPixmap;
		game.Rendering().DWorkingBufferPixmap = game.Rendering().DMessagePixmap;
		DrawMessage(game);
		game.Rendering().DWorkingBufferPixmap = OriginalPixmap;
	}

	/**
	 * @brief Draws the message
	 * @param game
	 *            The game drawing
	 */
	protected final void DrawMessage(CGame game) {
		DrawTextBackgroundFrame(game, DStackElement.Size());
		DrawBrickFrame(game, new Vector2(0, 0), DStackElement.Size());
		DrawMortar(game, new Vector2(0, 0), DStackElement.Size());
		DRootElement.Draw(game);
	}

	/**
	 * @brief Moves back to main menu if main player clicks
	 * @param game
	 *            The game updating
	 */
	public void Update(CGame game) {
		super.Update(game);

		if (game.GameState().GetPlayerWithColor(game.GameState().GetMainPlayerColor()).ShouldTakePrimaryAction(game)) {
            if (DIsMultiplayer) {
                // TODO: Implement networking
                // game.SwitchMode(new CMultiplayerMenuMode(game));
            } else {
                game.SwitchMode(new CMainMenuMode(game));
            }
		}
	}

	/**
	 * @brief Draws the 3D map and the message
	 * @param game
	 *            The game drawing
	 */
	public void Draw(CGame game) {
		CRendering Rendering = game.Rendering();
		super.Draw3D(game);
		super.Draw(game);
		Rendering.DWorkingBufferPixmap.drawPixmap(Rendering.DMessagePixmap,
				(int) (DefineConstants.GAME_WIDTH / 2 - DStackElement.Size().x / 2),
				(int) (DefineConstants.GAME_HEIGHT / 2 - DStackElement.Size().y / 2), 0, 0, -1, -1);
	}
}
