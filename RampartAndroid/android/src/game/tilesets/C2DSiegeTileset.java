package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


public class C2DSiegeTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D2DSiegeDirectionIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("siege-n");
        D2DSiegeDirectionIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("siege-e");
        D2DSiegeDirectionIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("siege-s");
        D2DSiegeDirectionIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("siege-w");

        return true;
    }
    public final void DrawTile(CGame game, Vector2 position, SDirection.EValue direction) {
        super.DrawTile(game, new Vector2(position), D2DSiegeDirectionIndices[direction.getValue()]);
    }
    private int[] D2DSiegeDirectionIndices = new int[SDirection.EValue.dMax.getValue()];
}
