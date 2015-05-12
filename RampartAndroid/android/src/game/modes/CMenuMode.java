package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.EPlayerColor;
import game.animations.DefineConstants;
import game.sounds.CSounds;
import game.tilesets.CBrickTileset;
import game.tilesets.CFontTileset;
import game.tilesets.CTargetTileset;
import game.tilesets.CWallFloorTileset;
import game.tilesets.EBorderBrickType;
import game.ui.CUIElement;

/**
 * @brief Mode mainly dealing with UI and has a title
 */
public class CMenuMode extends CGameMode {
	/**
	 * @brief Makes a new menu mode with a title
	 * @param title
	 *
	 * 
	 * @brief The root UI element
	 */
	protected CUIElement DRootElement;
	/**
	 * @brief The title of the mode
	 */
	private String DTitle;

	public CMenuMode(String title) {
		DTitle = title;
		DRootElement = new CUIElement();
	}

    //        *
//         * @brief Needed to start playing menu music
//         *
//         * @param game The game to reset
//
    public void Enter(CGame game) {
        super.Enter(game);
        game.Resources().DSounds.SwitchSong(CSounds.ESongType.stMenu, 1.0f);
    }

	/**
	 * @brief Updates root UI element
	 * @param game
	 *            Game updating
	 */
	public void Update(CGame game) {
		super.Update(game);
		DRootElement.Update(game);
		game.InputState().UpdateSelectedElement(DRootElement);
	}

	/**
	 * @brief Draws the menu background, title, and root element
	 * @param game
	 *            Game drawing
	 */
	public void Draw(CGame game) {
		DrawMenuBackground(game, new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		DrawTitle(game);
		DRootElement.Draw(game);
		CTargetTileset Tileset = game.Resources().DTilesets.DTargetTileset;
		Tileset.DrawTargetTile(
				game,
				game.InputState().DMousePosition.cpy().sub(
						new Vector2(Tileset.TileWidth(), Tileset.TileHeight()).scl(0.5f)), EPlayerColor.pcBlue);
	}

	/**
	 * @brief Draws menu background with bricks
	 * @param game
	 *            Game drawing
	 * @param size
	 *            Size of background
	 */
	protected final void DrawMenuBackground(CGame game, Vector2 size) {
		CBrickTileset Bricks = game.Resources().DTilesets.DBrickTileset;

		for (int HeightOffset = 0; HeightOffset < size.y; HeightOffset += 8) {
			if ((HeightOffset & 0x8) != 0) {
				for (int WidthOffset = -(Bricks.TileWidth() / 2); WidthOffset < size.x; WidthOffset += Bricks.TileWidth()) {
					Bricks.DrawTile(game, new Vector2(WidthOffset, HeightOffset), EBorderBrickType.bbtSingle);
				}
			} else {
				for (int WidthOffset = 0; WidthOffset < size.x; WidthOffset += Bricks.TileWidth()) {
					Bricks.DrawTile(game, new Vector2(WidthOffset, HeightOffset), EBorderBrickType.bbtSingle);
				}
			}
		}
		DrawBrickFrame(game, new Vector2(), size);
	}

	/**
	 * @brief Draws the title surrounded by brick border
	 * @param game
	 *            Game drawing
	 */
	protected final void DrawTitle(CGame game) {
		CFontTileset BlackFont = game.Resources().DTilesets.DBlackFont;
		CBrickTileset Bricks = game.Resources().DTilesets.DBrickTileset;
		CWallFloorTileset WallFloorTileset = game.Resources().DTilesets.DWallFloorTileset;

		Vector2 RequiredSize = new Vector2();

		Vector2 Size = BlackFont.MeasureText(DTitle);

		RequiredSize.x = Size.x + (Bricks.TileWidth() * 3) / 2;
		RequiredSize.x = ((RequiredSize.x + Bricks.TileWidth() - 1) / Bricks.TileWidth()) * Bricks.TileWidth();
		RequiredSize.y = Size.y + 1;
		RequiredSize.y += Bricks.TileHeight() * 2;
		RequiredSize.y = ((RequiredSize.y + Bricks.TileHeight() - 1) / Bricks.TileHeight()) * Bricks.TileHeight();

		for (int TopPosition = 0, XOffset = (DefineConstants.GAME_WIDTH / 2) - ((int) RequiredSize.x / 2) - 1; TopPosition
				+ WallFloorTileset.TileHeight() <= RequiredSize.y; TopPosition += WallFloorTileset.TileHeight()) {
			WallFloorTileset.Draw2DFloorTile(game, XOffset, TopPosition, EPlayerColor.pcNone);
			WallFloorTileset.Draw2DFloorTile(game, XOffset + (int) RequiredSize.x + 2 - WallFloorTileset.TileWidth(),
					TopPosition, EPlayerColor.pcNone);
		}
		for (int LeftPosition = (DefineConstants.GAME_WIDTH / 2) - ((int) RequiredSize.x / 2) - 1, YOffset = (int) RequiredSize.y
				- WallFloorTileset.TileHeight() + 1; LeftPosition + WallFloorTileset.TileWidth() <= (DefineConstants.GAME_WIDTH / 2)
				+ (RequiredSize.x / 2) + 1; LeftPosition += WallFloorTileset.TileWidth()) {
			WallFloorTileset.Draw2DFloorTile(game, LeftPosition, YOffset, EPlayerColor.pcNone);
		}
		WallFloorTileset.Draw2DFloorTile(game, (DefineConstants.GAME_WIDTH / 2) + ((int) RequiredSize.x / 2) + 1
				- WallFloorTileset.TileWidth(), (int) RequiredSize.y - WallFloorTileset.TileHeight() + 1,
				EPlayerColor.pcNone);

		DrawBrickFrame(game, new Vector2((DefineConstants.GAME_WIDTH / 2) - (RequiredSize.x / 2), 0), new Vector2(
				RequiredSize.x, RequiredSize.y));
		Vector2 TextPosition = new Vector2((DefineConstants.GAME_WIDTH / 2) - (Size.x / 2), (RequiredSize.y / 2)
				- (Size.y / 2));
		game.Resources().DTilesets.DBlackFont.DrawText(game, TextPosition.cpy().add(new Vector2(1, 1)), DTitle);
		game.Resources().DTilesets.DWhiteFont.DrawText(game, new Vector2(TextPosition), DTitle);
	}
}
