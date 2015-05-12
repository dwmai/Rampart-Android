package game.modes;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import game.CGame;
import game.CTerrainMap;
import game.CTimer;
import game.players.CPlayer;
import game.sounds.CSounds;
import game.utils.CTimeUtil;

/**
 * @brief Mode where players place cannons on their land based on the castles they have
 */
public class CCannonPlacementMode extends CMapMode {
	/**
	 * @brief Sets up timer and number of cannons each player can place
	 * @param game
	 *            The game entering
	 */
	public void Enter(CGame game) {
		for (CPlayer player : game.GameState().DPlayers) {
			player.DAvailableCannons = 0;
		}
		for (CPlayer player : game.GameState().DPlayers) {
			player.DAvailableCannons += player.OwnedCastleCount(game) + player.DExtraCannons;
			player.DExtraCannons = 0;
		}
		CTimer Timer = game.GameState().DTimer;
		Timer.DTimeout = CTimeUtil.MakeTimeoutSecondsInFuture(15);
		Timer.DIsVisible = true;
		Timer.DIsAudible = true;
        game.Resources().DSounds.SwitchSong(CSounds.ESongType.stPlace, 1.0f);
	}

	/**
	 * @brief Places cannons if needed and moves on to battle mode if the all the cannons are placed or the timer has run
	 *        out
	 * @param game
	 *            The game updating in
	 */
	public void Update(CGame game) {
		boolean AllCannonsPlaced = true;
		ArrayList<CPlayer> Players = game.GameState().DPlayers;
		for (CPlayer Player : Players) {
			if (Player.ShouldTakePrimaryAction(game)) {
				Player.TryToPlaceCannon(game, new Vector2(Player.DCursorTilePosition));
			} else if (Player.DIsAI) {
				Player.TryToPlaceCannon(game, new Vector2(Player.DCursorTilePosition));
			}
			AllCannonsPlaced &= Player.DAvailableCannons == 0;
		}

		if (AllCannonsPlaced || CTimeUtil.SecondsUntilDeadline(game.GameState().DTimer.DTimeout) < 0) {
			game.SwitchMode(new CBannerTransitionMode(game, "PREPARE FOR BATTLE", this, new CBattleMode()));

		}
		super.Update(game);
	}

	/**
	 * @brief Draws the 2D map and the cursors for placing cannons
	 * @param game
	 *            The game drawing in
	 */
	public void Draw(CGame game) {
		super.Draw2D(game);
		super.Draw(game);
		CTerrainMap Map = game.GameState().TerrainMap();

		for (CPlayer player : game.GameState().DPlayers) {
			if (player.DAvailableCannons > 0) {
				game.Resources().DTilesets.D2DCastleCannonTileset.Draw2DCannonToPlaceTile(game,
						Map.ConvertToScreenPosition(player.DCursorTilePosition), player.DColor,
						player.DAvailableCannons);

			}
		}
	}
}
