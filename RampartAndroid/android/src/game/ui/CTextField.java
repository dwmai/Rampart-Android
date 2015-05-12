package game.ui;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.CInputState;
import game.tilesets.CBrickTileset;
import game.tilesets.EBorderBrickType;
import game.utils.MathUtil;


/**
 * @brief Text field UI Element class
 */
public class CTextField extends CLabel {

	/**
	 * @brief Used to determine if the element is in password mode
	 */
	private boolean DPasswordMode;
	/**
	 * @brief Stores actual text entered, mainly for passwords since '*' is displayed on the screen
	 */
	private String DTextEntered;

	/**
	 * @brief CTextField constructor
	 * @param game
	 *            The game
	 * @param text
	 *            The buffer to allow the user to type into
	 * @param margin
	 *            The margin around the element
	 */
	public CTextField(CGame game, String text, boolean mode) {
		this(game, text, mode, new Vector2(15, 15));
	}

	public CTextField(CGame game, String text, boolean mode, Vector2 margin) {
		super(game, text, margin);
		DPasswordMode = mode;
		DTextEntered = text;
	}

	public CTextField(CGame game, String text) {
		this(game, text, false, new Vector2(15, 15));
	}

	/**
	 * @brief Updates the text field if necessary
	 * @param game
	 *            The game to update in
	 * @param translation
	 *            The computed translation
	 */
	public void Update(CGame game, Vector2 translation) {
		super.Update(game, new Vector2(translation));
		if (IsSelected() && game.InputState().DButtonPressed == CInputState.EInputButton.ibRightButton
				&& !(Text().length() == 0)) {
			DTextEntered = DTextEntered.substring(0, DTextEntered.length() - 1);

			/** Reset to full text to trim */
			if (PasswordMode()) {
				Text("");
				for (int i = 0; i < DTextEntered.length(); i++) {
					Text(Text() + '*');
				}
			} else {
				Text(DTextEntered);
			}
		} else if (IsSelected()) {
			if (PasswordMode() && !(game.InputState().DTextEntered.length() == 0)) {
				for (int i = 0; i < game.InputState().DTextEntered.length(); i++) {
					Text(Text() + '*');
				}
			} else {
				Text(Text() + game.InputState().DTextEntered);
			}
			DTextEntered += game.InputState().DTextEntered;
		}

		/** Trim off left until fits */
		while (true) {
			Vector2 dimensions = DFont.MeasureText(Text());
			if (dimensions.x > Size().x - Margin().x) {
				Text(Text().substring(1, Text().length()));
			} else {
				break;
			}
		}
	}

	/**
	 * @brief Gets the password mode
	 * @return Whether the password mode is on or off
	 */
	public final boolean PasswordMode() {
		return DPasswordMode;
	}

	/**
	 * @brief Determines if this element is currently being selected
	 * @param mousePosition
	 *            The position of the mouse
	 * @param translation
	 *            The computed translation
	 * @return This element if it is selected, NULL if not
	 */
	public final CUIElement DetermineSelected(Vector2 mousePosition, Vector2 translation) {
		if (MathUtil.IsContainedWithin(mousePosition, translation.cpy().add(Translation()), Size())) {
			return this;
		} else {
			return null;
		}
	}

	/**
	 * @brief Draws the text box and displays the user input
	 * @param game
	 *            The game to draw in
	 * @param translation
	 *            The computed translation
	 */
	public void Draw(CGame game, Vector2 translation) {
		CBrickTileset Bricks = game.Resources().DTilesets.DBrickTileset;
		Vector2 StartingPosition = new Vector2(translation.cpy().add(Translation()));
		int BrickType = 0;

		/** Make the horizontal size a multiple of the tile width */
		int HorizontalSize = (int) Size().x;
		for (; HorizontalSize % Bricks.TileWidth() != 0; HorizontalSize++)
			;

		/** Draws top and bottom borders */
		for (int WidthOffset = 0; WidthOffset < HorizontalSize; WidthOffset += Bricks.TileWidth()) {
			Bricks.DrawTile(game, StartingPosition.cpy().add(new Vector2(WidthOffset, 0)),
					EBorderBrickType.bbtTopCenter);
			Bricks.DrawTile(game, StartingPosition.cpy().add(new Vector2(WidthOffset, Size().y - Bricks.TileHeight())),
					EBorderBrickType.bbtBottomCenter);
		}
		/** Draws left and right borders */
		for (int HeightOffset = 0; HeightOffset < Size().y - 4; HeightOffset += Bricks.TileHeight()) {
			Bricks.DrawTile(game, StartingPosition.cpy().add(new Vector2(0, HeightOffset)),
					BrickType != 0 ? EBorderBrickType.bbtLeft1 : EBorderBrickType.bbtLeft0);
			Bricks.DrawTile(game,
					StartingPosition.cpy().add(new Vector2(HorizontalSize - Bricks.TileWidth(), HeightOffset)),
					BrickType != 0 ? EBorderBrickType.bbtRight1 : EBorderBrickType.bbtRight0);
			BrickType++;
			BrickType &= 0x1;
		}
		/** Draws the 4 corner tiles */
		Bricks.DrawTile(game, new Vector2(StartingPosition), EBorderBrickType.bbtTopLeft);
		Bricks.DrawTile(game, StartingPosition.cpy().add(new Vector2(HorizontalSize - Bricks.TileWidth(), 0)),
				EBorderBrickType.bbtTopRight);
		Bricks.DrawTile(game, StartingPosition.cpy().add(new Vector2(0, Size().y - Bricks.TileHeight())),
				EBorderBrickType.bbtBottomLeft);
		Bricks.DrawTile(
				game,
				StartingPosition.cpy().add(
						new Vector2(HorizontalSize - Bricks.TileWidth(), Size().y - Bricks.TileHeight())),
				EBorderBrickType.bbtBottomRight);
		super.Draw(game, new Vector2(translation));
	}

	protected void UpdateSize() {

	}

	/**
	 * @brief Gets the actual text entered
	 * @return The entered text
	 */
	public final String TextEntered() {
		return DTextEntered;
	}
}
