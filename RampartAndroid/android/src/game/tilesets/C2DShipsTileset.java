package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.SDirection;


public class C2DShipsTileset extends CGraphicTileset {
    public final boolean LoadTileset(CGame game, String filename) {
        super.LoadTileset(game, filename);

        D2DShipsDirectionIndices[SDirection.EValue.dNorth.getValue()] = this.FindTile("ship-n");
        D2DShipsDirectionIndices[SDirection.EValue.dNorthEast.getValue()] = this.FindTile("ship-ne");
        D2DShipsDirectionIndices[SDirection.EValue.dEast.getValue()] = this.FindTile("ship-e");
        D2DShipsDirectionIndices[SDirection.EValue.dSouthEast.getValue()] = this.FindTile("ship-se");
        D2DShipsDirectionIndices[SDirection.EValue.dSouth.getValue()] = this.FindTile("ship-s");
        D2DShipsDirectionIndices[SDirection.EValue.dSouthWest.getValue()] = this.FindTile("ship-sw");
        D2DShipsDirectionIndices[SDirection.EValue.dWest.getValue()] = this.FindTile("ship-w");
        D2DShipsDirectionIndices[SDirection.EValue.dNorthWest.getValue()] = this.FindTile("ship-nw");
        System.out.print("SDirection North: ");
        System.out.print(SDirection.EValue.dNorth);
        System.out.print("\n");
        return true;
    }
    public final int GetBaseFrame(SDirection.EValue direction) {
        return D2DShipsDirectionIndices[direction.getValue()];
    }
    public final void DrawTile(CGame game, Vector2 position, SDirection.EValue direction) {
        super.DrawTile(game, new Vector2(position), D2DShipsDirectionIndices[direction.getValue()]);
    }
    private int[] D2DShipsDirectionIndices = new int[SDirection.EValue.dMax.getValue()];
}
