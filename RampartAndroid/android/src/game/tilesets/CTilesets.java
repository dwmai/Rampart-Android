package game.tilesets;

import game.CGame;

/**
 * @brief Groups the tilesets together
 */
public class CTilesets {

	/**
	 * @brief 2D terrain tileset
	 */
	public C2DTerrainTileset D2DTerrainTileset = new C2DTerrainTileset();
	/**
	 * @brief 3D terrain tileset
	 */
	public C3DTerrainTileset D3DTerrainTileset = new C3DTerrainTileset();
	/**
	 * @brief 3D floor tileset
	 */
	public C3DFloorTileset D3DFloorTileset = new C3DFloorTileset();
	/**
	 * @brief 3D wall tileset
	 */
	public C3DWallTileset D3DWallTileset = new C3DWallTileset();
	/**
	 * @brief 3D cannon tileset
	 */
	public CGraphicTileset D3DCannonTileset = new CGraphicTileset();
	/**
	 * @brief 3D castle tileset
	 */
	public C3DCastleTileset D3DCastleTileset = new C3DCastleTileset();
	/**
	 * @brief 3D cannonball tileset
	 */
	public CGraphicTileset D3DCannonballTileset = new CGraphicTileset();
	/**
	 * @brief 3D explosion tileset
	 */
	public C3DExplosionTileset D3DExplosionTileset = new C3DExplosionTileset();
	/**
	 * @brief 3D burn tileset
	 */
	public C3DBurnTileset D3DBurnTileset = new C3DBurnTileset();
	/**
	 * @brief 3D cannon plume tileset
	 */
	public C3DCannonPlumeTileset D3DCannonPlumeTileset = new C3DCannonPlumeTileset();
	/**
	 * @brief 3D castle and cannon combined tileset
	 */
	public CCastleCannonTileset D2DCastleCannonTileset = new CCastleCannonTileset();
	/**
	 * @brief 2D ring select tileset
	 */
	public CCastleSelectTileset D2DCastleSelectTileset = new CCastleSelectTileset();
	/**
	 * @brief Digits tileset for timer
	 */
	public CGraphicTileset DDigitTileset = new CGraphicTileset();
	/**
	 * @brief Combined 2D wall and floor tileset
	 */
	public CWallFloorTileset DWallFloorTileset = new CWallFloorTileset();
	/**
	 * @brief Brick tileset for menu
	 */
	public CBrickTileset DBrickTileset = new CBrickTileset();
	/**
	 * @brief Mortar tileset for menu
	 */
	public CMortarTileset DMortarTileset = new CMortarTileset();
	/**
	 * @brief Target tileset for battle
	 */
	public CTargetTileset DTargetTileset = new CTargetTileset();
	/**
	 * @brief Black font
	 */
	public CFontTileset DBlackFont = new CFontTileset();
	/**
	 * @brief White font
	 */
	public CFontTileset DWhiteFont = new CFontTileset();
    /**
*
* @brief Ship tileset for Ship mode

*/
    public C2DShipsTileset D2DShipsTileset = new C2DShipsTileset();
    /**
*
* @brief 3D Ship tileset for battle

*/
    public C3DShipsTileset D3DShipsTileset = new C3DShipsTileset();
    /**
*
* @brief 3D Ship tileset for burning ships

*/
    public C3DShipsBurnTileset D3DShipsBurnTileset = new C3DShipsBurnTileset();
    /**
*
* @brief 3D Ship tileset for sinking ships

*/
    public C3DShipsExplosionTileset D3DShipsExplosionTileset = new C3DShipsExplosionTileset();
    /**
*
* @brief 2D Siege tileset

*/
    public C2DSiegeTileset D2DSiegeTileset = new C2DSiegeTileset();
    /**
*
* @broef 3D Siege tileset for battle

*/
    public C3DSiegeTileset D3DSiegeTileset = new C3DSiegeTileset();

	public CTilesets() {

	}

	public void dispose() {

	}

	/**
	 * @brief Loads the tilesets
	 * @param game
	 *
	 */
	public final void Load(CGame game) {
		DWhiteFont.LoadFont(game, "data/FontKingthingsWhite.dat");
		DBlackFont.LoadFont(game, "data/FontKingthingsBlack.dat");
		DBrickTileset.LoadTileset(game, "data/Bricks.dat");
		DMortarTileset.LoadTileset(game, "data/Mortar.dat");
		DWallFloorTileset.LoadTileset(game, "data/2DWallFloor.dat");
		D2DTerrainTileset.LoadTileset(game, "data/2DTerrain.dat");
		D3DTerrainTileset.LoadTileset(game, "data/3DTerrain.dat");
		D2DCastleCannonTileset.LoadTileset(game, "data/2DCastleCannon.dat");
		D2DCastleSelectTileset.LoadTileset(game, "data/2DCastleSelect.dat");
		DDigitTileset.LoadTileset(game, "data/Digits.dat");
		D3DCastleTileset.LoadTileset(game, "data/3DCastle.dat");
		D3DCannonTileset.LoadTileset(game, "data/3DCannon.dat");
		D3DWallTileset.LoadTileset(game, "data/3DWall.dat");
		D3DFloorTileset.LoadTileset(game, "data/3DFloor.dat");
		DTargetTileset.LoadTileset(game, "data/Target.dat");
		D3DCannonPlumeTileset.LoadTileset(game, "data/3DCannonPlume.dat");
		D3DCannonballTileset.LoadTileset(game, "data/3DCannonball.dat");
		D3DBurnTileset.LoadTileset(game, "data/3DBurn.dat");
		D3DExplosionTileset.LoadTileset(game, "data/3DExplosion.dat");
        //Add Ships Tilesets
        D2DShipsTileset.LoadTileset(game, "data/2DShips.dat");
        D3DShipsTileset.LoadTileset(game, "data/3DShips.dat");
        D3DShipsBurnTileset.LoadTileset(game, "data/3DShipsBurn.dat");
        D3DShipsExplosionTileset.LoadTileset(game, "data/3DShipsExplosion.dat");
        D2DSiegeTileset.LoadTileset(game, "data/2DSiege.dat");
        D3DSiegeTileset.LoadTileset(game, "data/3DSiege.dat");
	}
}
