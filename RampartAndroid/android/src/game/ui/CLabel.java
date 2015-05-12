package game.ui;

/** #define DEFAULT_LABEL_MARGIN Vector2(15, 15) */

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.tilesets.CFontTileset;


/**
 * @brief A UI element that renders text
 */
public class CLabel extends CUIElement {
	/**
	 * @brief Create a new label
	 * @param text
	 *            The text to show
	 * @param margin
	 *            The margin around the text for spacing
	 * @param font
	 *            The font to use
	 * 
	 * @brief The margin around the text
	 */
	protected Vector2 DMargin = new Vector2();
	/**
	 * @brief The text to show
	 */
	protected String DText;
	/**
	 * @brief Font used to draw
	 */
	protected CFontTileset DFont;

	public CLabel(CGame game, String text, Vector2 margin) {
		this(game, text, margin, null);
	}

	public CLabel(CGame game, String text) {
		this(game, text, new Vector2(15, 15), null);
	}

	public CLabel(CGame game, String text, Vector2 margin, CFontTileset font) {
		DFont = font != null ? font : game.Resources().DTilesets.DBlackFont;
		Text(text);
		Margin(new Vector2(margin));
	}

	/**
	 * @brief Sets the text and updates the size
	 * @param text
	 *            The new text
	 */
	public final void Text(String text) {
		DText = text;
		UpdateSize();
	}

	/**
	 * @brief Sets the margin and updates the size
	 * @param margin
	 *            The new margin
	 */
	public final void Margin(Vector2 margin) {
		DMargin = new Vector2(margin);
		UpdateSize();
	}

	/**
	 * @brief Draws the text into the game
	 * @param game
	 *            The game to draw in
	 * @param translation
	 *            The computed translation
	 */
	public void Draw(CGame game, Vector2 translation) {
		DFont.DrawText(game, new Vector2(Margin().x + translation.x + Translation().x, Margin().y + translation.y
				+ Translation().y), Text());

		CUIElementDraw(game, new Vector2(translation));
	}

	/**
	 * @brief The margin
	 * @return The margin
	 */
	public final Vector2 Margin() {
		return DMargin;
	}

	/**
	 * @brief The text
	 * @return The text
	 */
	public final String Text() {
		return DText;
	}

	/**
	 * @brief Used to recompute the size of the text and update the size of the label
	 */
	protected void UpdateSize() {
		int Width;
		int Height;
		Vector2 dimensions = DFont.MeasureText(Text());
		Size(new Vector2(dimensions.x, dimensions.y).add(Margin().cpy().scl(2f)));
	}
}
