package game.modes;

import game.CGame;
import game.players.CPlayer;
import game.sounds.CSounds;

/**
 * @brief Mode where players select their home castle
 */
public class CSelectCastleMode extends CMapMode {

	public void dispose() {
	}

	/**
	 * @brief Places the home castles and switches to cannon mode if all placed
	 * @param game
	 *            Game updating
	 */
	public void Update(CGame game) {
		boolean AllCastlesPlaced = true;
		for (CPlayer Player : game.GameState().DPlayers) {
			if (!Player.DPlacedHomeCastle && Player.ShouldTakePrimaryAction(game)) {
				Player.PlaceHomeCastle(game, Player.DHoveredCastle);
                game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctTriumph);
			}

			if (Player.DPlacedHomeCastle) {
				Player.DHoveredCastle = null;
			} else {
				Player.UpdateHoveredCastle(game);
			}

			AllCastlesPlaced &= Player.DPlacedHomeCastle;
		}

		if (AllCastlesPlaced) {
			game.SwitchMode(new CCannonPlacementMode());
			game.SwitchMode(new CBannerTransitionMode(game, "PLACE CANNONS", this, new CCannonPlacementMode()));
		}

		super.Update(game);
	}

	/**
	 * @brief Draws the 2D map
	 * @param game
	 *            Game drawing
	 */
	public void Draw(CGame game) {
		super.Draw2D(game);
		super.Draw(game);
		super.DrawTargetCursors(game);
	}
}
