package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


public class C3DShipsTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D3DShipsIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("ship-3d-n");
        D3DShipsIndices[SDirection.EValue.dNorthEast.getValue()] = this.FindTile("ship-3d-ne");
        D3DShipsIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("ship-3d-e");
        D3DShipsIndices[SDirection.EValue.dSouthEast.getValue()] = this.FindTile("ship-3d-se");
        D3DShipsIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("ship-3d-s");
        D3DShipsIndices[SDirection.EValue.dSouthWest.getValue()] = this.FindTile("ship-3d-sw");
        D3DShipsIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("ship-3d-w");
        D3DShipsIndices[SDirection.EValue.dNorthWest.getValue()] = this.FindTile("ship-3d-nw");

        return true;
    }
    public final void DrawTile(CGame game, Vector2 position, SDirection.EValue direction) {
        super.DrawTile(game, new Vector2(position), D3DShipsIndices[direction.getValue()]);
    }
    protected int[] D3DShipsIndices = new int[SDirection.EValue.dMax.getValue()];
}