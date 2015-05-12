package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


public class C3DShipsBurnTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D3DShipsIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("ship-burn-n-0");
        D3DShipsIndices[SDirection.EValue.dNorthEast.getValue()] = this.FindTile("ship-burn-ne-0");
        D3DShipsIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("ship-burn-e-0");
        D3DShipsIndices[SDirection.EValue.dSouthEast.getValue()] = this.FindTile("ship-burn-se-0");
        D3DShipsIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("ship-burn-s-0");
        D3DShipsIndices[SDirection.EValue.dSouthWest.getValue()] = this.FindTile("ship-burn-sw-0");
        D3DShipsIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("ship-burn-w-0");
        D3DShipsIndices[SDirection.EValue.dNorthWest.getValue()] = this.FindTile("ship-burn-nw-0");

        return true;
    }
    public final void DrawTile(CGame game, Vector2 position, SDirection.EValue direction, int index) {
        super.DrawTile(game, position, D3DShipsIndices[direction.getValue()] + index);
    }
    protected int[] D3DShipsIndices = new int[SDirection.EValue.dMax.getValue()];
}