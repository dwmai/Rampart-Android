package game;

import game.sounds.CSounds;
import game.tilesets.CGraphicTileset;
import game.utils.CTimeUtil;
import game.utils.Timeval;

/**
 * @brief Timer that plays tick tock and draws on screen
 */
public class CTimer {

	/**
	 * @brief Timeout that timer is counting down to
	 */
	public Timeval DTimeout = new Timeval();
	/**
	 * @brief Stores last time sound effect was played
	 */
	public Timeval DLastTickTockTime = new Timeval();
	/**
	 * @brief Stores if timer should draw on screen
	 */
	public boolean DIsVisible;
	/**
	 * @brief Stores if timer should play sound effects
	 */
	public boolean DIsAudible;

	/**
	 * @brief Initializes timer to be invisible
	 */
	public CTimer() {
		DIsVisible = false;
		DIsAudible = false;
	}

	/**
	 * @brief Draws on screen if visible
	 * @param game
	 *            Game drawing
	 */
	public final void Draw(CGame game) {
		if (DIsVisible) {
			int SecondsLeft = Math.max(0, CTimeUtil.SecondsUntilDeadline(DTimeout));
			CGraphicTileset Digits = game.Resources().DTilesets.DDigitTileset;

			Digits.DrawTile(game, 0, 0, (SecondsLeft / 10) % 10);
			Digits.DrawTile(game, Digits.TileWidth(), 0, SecondsLeft % 10);
		}
	}

	/**
	 * @brief Updates timer, playing sound effects if audible
	 * @param game
	 *            Game updating
	 */
	public final void Update(CGame game) {
		if (DIsAudible) {
			PlayTickTockSound(game);
		}
	}

	/**
	 * @brief Plays the sound effects if needed
	 * @param game
	 *            Game updating
	 */
	public final void PlayTickTockSound(CGame game) {
		int MSUntilDeadline = CTimeUtil.MilliSecondsUntilDeadline(DTimeout) - 3000;
		int LastTickTockMS = -CTimeUtil.MilliSecondsUntilDeadline(DLastTickTockTime);
		boolean PlaySound = false;
		boolean PlayTick = true;

		if (MSUntilDeadline >= 5000) {
			if ((MSUntilDeadline / 1000) != ((MSUntilDeadline + LastTickTockMS) / 1000)) {
				PlaySound = true;
				PlayTick = ((MSUntilDeadline / 1000) & 0x01) != 0;
			}
		} else if (MSUntilDeadline >= 0) {
			if ((5000 < MSUntilDeadline + LastTickTockMS)
					|| ((MSUntilDeadline / 500) != ((MSUntilDeadline + LastTickTockMS) / 500))) {
				PlaySound = true;
				PlayTick = ((MSUntilDeadline / 500) & 0x01) != 0;
			}
		} else {
			if ((0 < MSUntilDeadline + LastTickTockMS)
					|| ((MSUntilDeadline / 250) != ((MSUntilDeadline + LastTickTockMS) / 250))) {
				PlaySound = true;
				PlayTick = ((MSUntilDeadline / 500) & 0x01) != 0;
			}
		}
		if (PlaySound) {
			game.Resources().DSounds.PlaySoundClip(PlayTick ? CSounds.ESoundClipType.sctTick
					: CSounds.ESoundClipType.sctTock, 1, 0.0f);
			DLastTickTockTime.update();
		}

	}

}
