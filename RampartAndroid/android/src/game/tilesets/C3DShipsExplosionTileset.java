package game.tilesets;

import game.CGame;
import game.SDirection;
import game.utils.Log;


public class C3DShipsExplosionTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D3DLeftExplosionIndex = this.FindTile("ship-left-0");
        D3DRightExplosionIndex = this.FindTile("ship-right-0");

        return true;
    }
    public final int GetBaseFrame(SDirection.EValue direction) {
        switch(direction) {
            case dNorth:
            case dNorthEast:
            case dEast:
            case dSouthEast:
            case dSouth:
                return D3DRightExplosionIndex;
            case dSouthWest:
            case dWest:
            case dNorthWest:
            case dMax:
                return D3DLeftExplosionIndex;
        }
        Log.warn("GetBaseFrame() was given an invalid direction.");
        return 0;
    }
    protected int D3DLeftExplosionIndex;
    protected int D3DRightExplosionIndex;
}