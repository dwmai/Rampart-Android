package game.modes;

import com.badlogic.gdx.math.Vector2;

import game.CGame;

/**
 * @brief Mode which draws a map
 */
public class CMapMode extends CGameMode {

	public CMapMode() {
	}

	public void dispose() {
	}

	/**
	 * @brief Same as base class
	 * @param game
	 *            Game updating
	 */
	public void Update(CGame game) {
		super.Update(game);
	}

	/**
	 * @brief Draws animations and cannonballs
	 * @param game
	 *            Game drawing
	 */
	public void Draw(CGame game) {
		super.Draw(game);
	}

	/**
	 * @brief Draws the terrain and construction maps in 2D
	 * @param game
	 *            Game drawing
	 */
	protected final void Draw2D(CGame game) {
		game.GameState().TerrainMap().Draw2DMap(game);
		game.GameState().ConstructionMap().Draw2D(game);
		game.GameState().TerrainMap().Draw2DCastles(game);
        game.GameState().Units().Draw2DShip(game);
        game.GameState().Units().Draw2DSiege(game);
	}

	/**
	 * @brief Draws the terrain and construction maps in 3D
	 * @param game
	 *            Game drawing
	 */
	protected final void Draw3D(CGame game) {
		game.GameState().TerrainMap().Draw3D(game);
		for (int YIndex = 0, YPos = -game.Resources().DTilesets.D3DTerrainTileset.TileHeight(); YIndex < game.GameState().ConstructionMap().DTiles.size(); YIndex++, YPos += game.Resources().DTilesets.D3DTerrainTileset.TileHeight()) {
			for (int XIndex = 0, XPos = 0; XIndex < game.GameState().ConstructionMap().DTiles.get(YIndex).size(); XIndex++, XPos += game.Resources().DTilesets.D3DTerrainTileset.TileWidth()) {
				/* With everything: 2 FPS */
				/* Without cannons: still 2 FPS */
				/* Without castles: 8/9 FPS */
				/* Without either cannons or castles: 9 FPS */
				/* Without 3D Terrain: 2 FPS */
				game.GameState().ConstructionMap().Draw3DFloor(game, XIndex, YIndex, XPos, YPos);
				game.GameState().ConstructionMap().Draw3DCannons(game, XIndex, YIndex);
				game.GameState().TerrainMap().Draw3DCastles(game, XIndex, YIndex);
				game.GameState().ConstructionMap().Draw3DWalls(game, XIndex, YIndex, XPos, YPos);
                game.GameState().Units().Draw3DShip(game, new Vector2(XIndex, YIndex));
                game.GameState().Units().Draw3DSiege(game, XIndex, YIndex);
				super.DrawCannonballs(game, new Vector2(XIndex, YIndex));
				super.DrawAnimations(game, new Vector2(XIndex, YIndex));
			}
		}
	}
}
