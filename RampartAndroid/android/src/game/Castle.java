package game;

import com.badlogic.gdx.math.Vector2;

import game.animations.DefineConstants;
import game.players.CPlayer;
import game.tilesets.C3DCastleTileset;
import game.tilesets.CCastleCannonTileset;
import game.tilesets.CCastleSelectTileset;

/**
 * Castle class, used to store information of castle.
 */
public class Castle {

	/**
	 * @brief The size of castles (2, 2)
	 */
	public static Vector2 CSize = new Vector2(2, 2);
	/**
	 * @brief The color of the castle
	 */
	public EPlayerColor DColor;
	/**
	 * @brief If the castle is surrounded by walls
	 */
	public boolean DSurrounded;
	/**
	 * @brief If the castle is being hovered by a player for selection
	 */
	public boolean DHovered;
    /**
    * @brief Which animation step the castle is at
    */
    public int DAnimationStep;
	/**
	 * @brief The position of the castle
	 */
	private Vector2 DIndexPosition = new Vector2();

	/**
	 * Castle constructor.
	 */
	public Castle() {
		DSurrounded = false;
		DHovered = false;
		DColor = EPlayerColor.pcNone;
        DAnimationStep = 0;
	}

	/**
	 * Castle destructor.
	 */
	public void dispose() {
	}

	/**
	 * @brief Sets not surrounded or hovered
	 */
	public final void Reset() {
		DSurrounded = false;
		DHovered = false;
	}

	/**
	 * Gets pos of castle.
	 * 
	 * @return Returns position of castle.
	 */
	public final Vector2 IndexPosition() {
		return DIndexPosition;
	}

	/**
	 * Sets pos of castle.
	 * 
	 * @param position
	 *            Used to set position of castle.
	 */
	public final void IndexPosition(Vector2 position) {
		DIndexPosition = new Vector2(position);
	}

	/**
	 * @brief Updates if player is hovering
	 * @param game
	 *            Game updating in
	 */
	public final void Update(CGame game) {
		DHovered = false;
        DAnimationStep += game.GameState().DWind.DWindSpeed;
		for (CPlayer player : game.GameState().DPlayers) {
			if (player.DHoveredCastle == this) {
				DHovered = true;
			}
		}
	}

	/**
	 * @brief Draws the 2D tile, and if hovered, draws the selection ring around as well
	 * @param game
	 *            Drawing in
	 */
	public final void Draw2D(CGame game) {
		CCastleCannonTileset Tileset = game.Resources().DTilesets.D2DCastleCannonTileset;
		CTerrainMap Map = game.GameState().TerrainMap();
		Vector2 Position = Map.ConvertToScreenPosition(new Vector2(DIndexPosition));
		Tileset.Draw2DCastleTile(game, (int) Position.x, (int) Position.y, (DSurrounded || DHovered) ? DColor
				: EPlayerColor.pcNone);
		if (DHovered) {
			DrawSelectionRing(game);
		}

	}

	/**
	 * @brief Draws the selection ring around the 2D tile to indicate hovering
	 * @param game
	 *            Game to draw in
	 */
	public final void DrawSelectionRing(CGame game) {
		CCastleSelectTileset Tileset = game.Resources().DTilesets.D2DCastleSelectTileset;
		CTerrainMap Map = game.GameState().TerrainMap();
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2(DIndexPosition.x, (DIndexPosition.y - 1))), DColor, 0);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 1), (DIndexPosition.y - 1))), DColor, 0);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 2), (DIndexPosition.y - 1))), DColor, 1);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 2), DIndexPosition.y)), DColor, 2);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 2), (DIndexPosition.y + 1))), DColor, 2);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 2), (DIndexPosition.y + 2))), DColor, 3);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x + 1), (DIndexPosition.y + 2))), DColor, 4);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2(DIndexPosition.x, (DIndexPosition.y + 2))), DColor, 4);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x - 1), (DIndexPosition.y + 2))), DColor, 5);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x - 1), (DIndexPosition.y + 1))), DColor, 6);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x - 1), DIndexPosition.y)), DColor, 6);
		Tileset.Draw2DCastleSelectTile(game,
				Map.ConvertToScreenPosition(new Vector2((DIndexPosition.x - 1), (DIndexPosition.y - 1))), DColor, 7);
	}

	/**
	 * @brief Draws the 3D tiles
	 * @param game
	 *            Game to draw in
	 */
	public final void Draw3D(CGame game) {
		C3DCastleTileset Tileset = game.Resources().DTilesets.D3DCastleTileset;
		CTerrainMap Map = game.GameState().TerrainMap();
		Vector2 Position = Map.ConvertToScreenPosition(DIndexPosition.cpy().sub(new Vector2(0, 1)));
        EPlayerColor Color = (DSurrounded || DHovered) ? DColor : EPlayerColor.pcNone;
        int Index = (game.GameState().DWind.DWindDirection * DefineConstants.CASTLE_ANIMATION_TIMESTEPS) + (DAnimationStep/4) % DefineConstants.CASTLE_ANIMATION_TIMESTEPS;
        Tileset.Draw3DCastleTile(game, new Vector2(Position), Color, Color == EPlayerColor.pcNone ? 0 : Index);
		Tileset.Draw3DCastleTile(game, new Vector2(Position), (DSurrounded || DHovered) ? DColor : EPlayerColor.pcNone);
	}
}
