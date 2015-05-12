package game.ui;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.sounds.CSounds;
import game.tilesets.CFontTileset;
import game.utils.MathUtil;


/**
 * @brief A Button with sound effects built in
 */
public class CButton extends CLabel {

	/**
	 * @brief Used to determine if sound effect for changing options should be played
	 */
	private boolean DWasSelected;

	/**
	 * @brief Create a new button
	 * @param text
	 *            The text of the button
	 * @param margin
	 *            The margin around the text to also respond to
	 */
	public CButton(CGame game, String text) {
		this(game, text, new Vector2(15, 15));
	}

	/**
	 * @brief Updates the button and play any needed sound effects
	 * @param game
	 *            The game to update in
	 * @param translation
	 *            The computed translation
	 */
	public CButton(CGame game, String text, Vector2 margin) {
		super(game, text, margin);
		DWasSelected = false;
	}

	public void Update(CGame game, Vector2 translation) {
		super.Update(game, new Vector2(translation));
		if (IsSelected() && !DWasSelected) {
			game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctTick);
		}
		if (IsPressed()) {
			game.Resources().DSounds.PlaySoundClip(CSounds.ESoundClipType.sctPlace);
		}
		DWasSelected = IsSelected();
	}

	public CUIElement DetermineSelected(Vector2 mousePosition, Vector2 translation) {
		if (MathUtil.IsContainedWithin(mousePosition, translation.cpy().add(Translation()), Size())) {
			return this;
		} else {
			return null;
		}
	}

	/**
	 * @brief Draws the button into the game using a basic font for now
	 * @param game
	 *            The game to draw in
	 * @param translation
	 *            The computed translation
	 */
	public void Draw(CGame game, Vector2 translation) {
		Vector2 CombinedTranslation = translation.cpy().add(Translation());
		CFontTileset BackgroundFont;
		CFontTileset ForegroundFont;

		if (IsPressed()) {
			BackgroundFont = game.Resources().DTilesets.DWhiteFont;
			ForegroundFont = game.Resources().DTilesets.DBlackFont;
		} else if (IsSelected()) {
			BackgroundFont = game.Resources().DTilesets.DBlackFont;
			ForegroundFont = game.Resources().DTilesets.DWhiteFont;
		} else {
			BackgroundFont = game.Resources().DTilesets.DWhiteFont;
			ForegroundFont = game.Resources().DTilesets.DBlackFont;
		}

		BackgroundFont.DrawText(game, new Vector2(Margin().x + CombinedTranslation.x - 1, Margin().y
				+ CombinedTranslation.y - 1), Text());
		ForegroundFont.DrawText(game, new Vector2(Margin().x + CombinedTranslation.x, Margin().y
				+ CombinedTranslation.y), Text());

		CUIElementDraw(game, translation);
	}

	/**
	 * @brief Sets the previous state of the selected flag
	 * @param wasSelected
	 *            The previous state of the selected flag
	 */
	public final void WasSelected(boolean wasSelected) {
		DWasSelected = wasSelected;
	}
}
