package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


public class C3DSiegeTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D3DSiegeIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("siege-3d-n");
        D3DSiegeIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("siege-3d-e");
        D3DSiegeIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("siege-3d-s");
        D3DSiegeIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("siege-3d-w");

        return true;
    }
    public final void DrawTile(CGame game, Vector2 position, SDirection.EValue direction) {
        super.DrawTile(game, new Vector2(position), D3DSiegeIndices[direction.getValue()]);
    }
    protected int[] D3DSiegeIndices = new int[SDirection.EValue.dMax.getValue()];
}