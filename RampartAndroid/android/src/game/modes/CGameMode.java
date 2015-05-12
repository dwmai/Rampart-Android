package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CCannonball;
import game.CGame;
import game.EPlayerColor;
import game.animations.CAnimation;
import game.players.CPlayer;
import game.tilesets.CBrickTileset;
import game.tilesets.CMortarTileset;
import game.tilesets.CTargetTileset;
import game.tilesets.CWallFloorTileset;
import game.tilesets.EBorderBrickType;

/**
 * @brief A mode that the game can be in that updates and draws the game
 */
public class CGameMode {
	/**
	 * @brief Resets the timer
	 * @param game
	 *            The game to reset
	 */
	public void Enter(CGame game) {
		game.GameState().DTimer.DIsAudible = false;
		game.GameState().DTimer.DIsVisible = false;
	}

	/**
	 * @brief Updates the players, cannonballs, animations, and timer
	 * @param game
	 *            The game to update
	 */
	public void Update(CGame game) {
		// game.GameState().Network().Update(game);
		for (CPlayer player : game.GameState().DPlayers) {
			player.Update(game);
		}
		java.util.ArrayList<CCannonball> ContinuingCannonballs = new java.util.ArrayList<CCannonball>();
		for (CCannonball cannonball : game.GameState().DCannonballs) {
			cannonball.Update(game);
			if (cannonball.IsAlive()) {
				ContinuingCannonballs.add(cannonball);
			} else {
				cannonball = null;
			}
		}
		game.GameState().DCannonballs = ContinuingCannonballs;

		java.util.ArrayList<CAnimation> ContinuingAnimations = new java.util.ArrayList<CAnimation>();
		for (int i = 0; i < game.GameState().DAnimations.size(); i++) {
			CAnimation Animation = game.GameState().DAnimations.get(i);
			Animation.Update(game);
			if (Animation.ShouldContinuePlaying()) {
				ContinuingAnimations.add(Animation);
			} else {
				if (Animation != null) {
					Animation.dispose();
				}
			}
		}
		// TODO: Modified Linux code, just an addition
		game.GameState().DAnimations.clear();
		for (CAnimation animation : ContinuingAnimations) {
			game.GameState().DAnimations.add(animation);
		}
		ContinuingAnimations.clear();
		if (game.GameState().TerrainMap() != null) {
			game.GameState().TerrainMap().Update(game);
		}
		game.GameState().DTimer.Update(game);
        game.GameState().DTimeStep++;
	}

	/**
	 * @brief Draws the timer
	 * @param game
	 *            The game to draw
	 */
	public void Draw(CGame game) {
		game.GameState().DTimer.Draw(game);
	}

	/**
	 * @brief Does nothing
	 * @param game
	 *            Game to leave
	 */
	public void Leave(CGame game) {
	}

    /**
*
* @brief Does nothing
*
* @param game Game to enter

*/
    public void WillEnter(CGame game) {
    }

	/**
	 * @brief Draws the animations
	 * @param game
	 *            The game to draw
	 * @param position
	 *            The current position in draw loop
	 */
	protected final void DrawAnimations(CGame game, Vector2 position) {
		for (CAnimation animation : game.GameState().DAnimations) {
			if (animation.Index().equals(position)) {
				animation.Draw(game);
			}
		}
	}

	/**
	 * @brief Draws the cannonballs
	 * @param game
	 *            The game to draw
	 * @param position
	 *            The cannonballs position
	 */
	protected final void DrawCannonballs(CGame game, Vector2 position) {
		for (CCannonball cannonball : game.GameState().DCannonballs) {
			if (game.GameState().TerrainMap().ConvertToTileIndex(
					new Vector2(cannonball.Position().x, cannonball.Position().y)).equals(position)) {
				cannonball.Draw(game);
			}
		}
	}

	/**
	 * @brief Draws the floor tiles of main player to cover the specified area
	 * @param game
	 *            Game to draw
	 * @param size
	 *            Size to cover
	 */
	protected final void DrawTextBackgroundFrame(CGame game, Vector2 size) {
		CWallFloorTileset Tileset = game.Resources().DTilesets.DWallFloorTileset;
		EPlayerColor MainColor = game.GameState().GetMainPlayerColor();
		for (int HeightOffset = 0; HeightOffset < size.y; HeightOffset += Tileset.TileHeight()) {
			for (int WidthOffset = 0; WidthOffset < size.x; WidthOffset += Tileset.TileWidth()) {
				Tileset.Draw2DFloorTile(game, WidthOffset, HeightOffset, MainColor);
			}
		}
	}

	/**
	 * @brief Draws the brick border for text
	 * @param game
	 *            Game to draw
	 * @param position
	 *            Position of top left
	 * @param size
	 *            Size of border
	 */
	protected final void DrawBrickFrame(CGame game, Vector2 position, Vector2 size) {
		DrawMortar(game, new Vector2(position), new Vector2(size));
		DrawBrickOutline(game, new Vector2(position), new Vector2(size));
	}

	/**
	 * @brief Draws the mortar for text
	 * @param game
	 *            Game to draw
	 * @param position
	 *            Position of top left
	 * @param size
	 *            Size of border
	 */
	protected final void DrawMortar(CGame game, Vector2 position, Vector2 size) {
		CMortarTileset Mortar = game.Resources().DTilesets.DMortarTileset;

		/** Draws the corner mortar tiles */
		Mortar.DrawTile(game, position.x, position.y - 5, CMortarTileset.EBorderMotarType.bmtLeftTop2);
		Mortar.DrawTile(game, position.x + size.x - Mortar.TileWidth(), position.y - 5,
				CMortarTileset.EBorderMotarType.bmtRightTop2);
		Mortar.DrawTile(game, position.x, position.y + size.y - Mortar.TileHeight() + 5,
				CMortarTileset.EBorderMotarType.bmtLeftBottom2);
		Mortar.DrawTile(game, position.x + size.x - Mortar.TileWidth(), position.y + size.y - Mortar.TileHeight() + 5,
				CMortarTileset.EBorderMotarType.bmtRightBottom2);
		/**
		 * Draws mortar tiles according to frame's size.x Sets index according to index of mortars where center = 0
		 */
		for (int WidthOffset = 0; WidthOffset < size.x; WidthOffset += Mortar.TileWidth()) {
			CMortarTileset.EBorderMotarType TopIndex; /** Top index and bottom index of size.x */
			CMortarTileset.EBorderMotarType BottomIndex;
			int CenterPoint = WidthOffset + Mortar.TileWidth() / 2; /** Center point = middle of size.x */
			int BrickDistance = (int) (size.x / 2 - CenterPoint) / Mortar.TileWidth(); // Distance of bricks

			if (-3 >= BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopRight2;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomRight2;
			} else if (-2 == BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopRight1;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomRight1;
			} else if (-1 == BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopRight0;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomRight0;
			} else if (0 == BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopCenter;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomCenter;
			} else if (1 == BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopLeft0;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomLeft0;
			} else if (2 == BrickDistance) {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopLeft1;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomLeft1;
			} else {
				TopIndex = CMortarTileset.EBorderMotarType.bmtTopLeft2;
				BottomIndex = CMortarTileset.EBorderMotarType.bmtBottomLeft2;
			}

			Mortar.DrawTile(game, position.x + WidthOffset, position.y, TopIndex); // Draws a row of tiles at the top
																					// index
			Mortar.DrawTile(game, position.x + WidthOffset, position.y + size.y - Mortar.TileHeight(), BottomIndex); // Draws
																														// tiles
																														// at
																														// bottom
		}

		// Determines the left index and right index of the frame
		for (int HeightOffset = 3; HeightOffset < size.y - Mortar.TileHeight(); HeightOffset += 8) {
			CMortarTileset.EBorderMotarType LeftIndex;
			CMortarTileset.EBorderMotarType RightIndex;
			int CenterPoint = HeightOffset + Mortar.TileHeight() / 2;
			int BrickDistance = (int) (size.y / 2 - CenterPoint) / 8;

			if (3 <= BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftTop2;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightTop2;
			} else if (2 == BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftTop1;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightTop1;
			} else if (1 == BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftTop0;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightTop0;
			} else if (0 == BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftCenter;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightCenter;
			} else if (-1 == BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftBottom0;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightBottom0;
			} else if (-2 == BrickDistance) {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftBottom1;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightBottom1;
			} else {
				LeftIndex = CMortarTileset.EBorderMotarType.bmtLeftBottom2;
				RightIndex = CMortarTileset.EBorderMotarType.bmtRightBottom2;
			}

			Mortar.DrawTile(game, position.x, position.y + HeightOffset, LeftIndex); // Draws the mortar tiles on left
																						// column
			Mortar.DrawTile(game, position.x + size.x - Mortar.TileWidth(), position.y + HeightOffset, RightIndex); // Draws
																													// tile
																													// on
																													// right
																													// col

		}
	}

	/**
	 * @brief Draws the outline on the brick for border
	 * @param game
	 *            Game to draw
	 * @param position
	 *            Position of top left
	 * @param size
	 *            Size of border
	 */
	protected final void DrawBrickOutline(CGame game, Vector2 position, Vector2 size) {
		CBrickTileset Bricks = game.Resources().DTilesets.DBrickTileset;

		int BrickType = 0;
		/** Draws brick frame as long as cumulative size.x offset is less than size.x */
		for (int WidthOffset = 0; WidthOffset < size.x; WidthOffset += Bricks.TileWidth()) {
			Bricks.DrawTile(game, position.cpy().add(new Vector2(WidthOffset, 0)), EBorderBrickType.bbtTopCenter);
			Bricks.DrawTile(game, position.cpy().add(new Vector2(WidthOffset, size.y - Bricks.TileHeight())),
					EBorderBrickType.bbtBottomCenter);
		}
		/** Draws brick frame as long as cumulative size.y is less than frame size.y */
		for (int HeightOffset = 0; HeightOffset < size.y; HeightOffset += Bricks.TileHeight()) {
			Bricks.DrawTile(game, position.cpy().add(new Vector2(0, HeightOffset)),
					BrickType != 0 ? EBorderBrickType.bbtLeft1 : EBorderBrickType.bbtLeft0);
			Bricks.DrawTile(game, position.cpy().add(new Vector2(size.x - Bricks.TileWidth(), HeightOffset)),
					BrickType != 0 ? EBorderBrickType.bbtRight1 : EBorderBrickType.bbtRight0);
			BrickType++;
			BrickType &= 0x1;
		}

		/** Draws the 4 corner tiles */
		Bricks.DrawTile(game, new Vector2(position.x, 0), EBorderBrickType.bbtTopLeft);
		Bricks.DrawTile(game, position.cpy().add(new Vector2(size.x - Bricks.TileWidth(), 0)),
				EBorderBrickType.bbtTopRight);
		Bricks.DrawTile(game,
				position.cpy().add(new Vector2(size.x - Bricks.TileWidth(), size.y - Bricks.TileHeight())),
				EBorderBrickType.bbtBottomRight);
		Bricks.DrawTile(game, position.cpy().add(new Vector2(0, size.y - Bricks.TileHeight())),
				EBorderBrickType.bbtBottomLeft);
	}

	/**
	 * @brief Draws the player cursors using the target tileset
	 * @param game
	 *            The game drawing
	 */
	protected final void DrawTargetCursors(CGame game) {
		CTargetTileset Target = game.Resources().DTilesets.DTargetTileset;

		for (CPlayer player : game.GameState().DPlayers) {
			Target.DrawTargetTile(game,
					player.DCursorPosition.cpy().sub(new Vector2(Target.TileWidth(), Target.TileHeight()).scl(0.5f)),
					player.DColor);
		}
	}
}
