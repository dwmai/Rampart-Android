package game;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

import game.utils.CMathUtil;

public class CUnits {

    public final void Reset() {
        DShip.clear();
        DSiegeWeapons.clear();
    }
    public final void Draw2DShip(CGame game) {
        //std::cout<<"Ship Size: " << DShip.size() << std::endl;
        for (CShip ship : DShip) {
            ship.Draw2D(game);
        }
    }
    public final void Draw3DShip(CGame game, Vector2 position) {
        //std::cout<<"Ship Size: " << DShip.size() << std::endl;
        for (CShip ship : DShip) {
            if (position.equals(new Vector2((int)ship.DPosition.x, (int)ship.DPosition.y))) {
                ship.Draw3D(game);
            }
        }
    }
    public final void Draw2DSiege(CGame game) {
        //std::cout<<"Ship Size: " << DShip.size() << std::endl;
        for (CSiegeWeapons siegeWeapon : DSiegeWeapons) {
            siegeWeapon.Draw2D(game);
        }
    }
    public final void Draw3DSiege(CGame game, int XPos, int YPos) {
        //std::cout<<"Ship Size: " << DShip.size() << std::endl;
        for (CSiegeWeapons siegeWeapon : DSiegeWeapons) {
            if (new Vector2((int)siegeWeapon.DPosition.x, (int)siegeWeapon.DPosition.y).equals(new Vector2(XPos, YPos))) {
                siegeWeapon.Draw3D(game);
            }
        }
    }
    public final boolean IsSpaceFreeOfShip(Vector2 position, Vector2 size) {
        for (CShip ship : DShip) {
            if (CMathUtil.DoRectanglesOverlap(ship.DPosition, ship.CShipSize, position, size)) {
                return false;
            }
        }
        return true;
    }
    public final boolean IsSpaceFreeOfSiege(CGame game, Vector2 position, Vector2 size) {
        for (CSiegeWeapons siegeWeapon : DSiegeWeapons) {
            if (CMathUtil.DoRectanglesOverlap(siegeWeapon.DPosition, siegeWeapon.CSiegeWeaponSize, position, size)) {
                return false;
            }
            CTerrainMap TerrainMap = game.GameState().TerrainMap();
            float XPos = siegeWeapon.DPosition.x;
            float YPos = siegeWeapon.DPosition.y;
            boolean IsBetweenXTiles = 0 != XPos - (int) XPos && TerrainMap.Width() - 2 >= XPos;
            boolean IsBetweenYTiles = 0 != YPos - (int) YPos && TerrainMap.Height() - 2 >= YPos;
            if (IsBetweenXTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos + 1f, YPos), siegeWeapon.CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return false;
                }
            }
            if (IsBetweenYTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos, YPos + 1), siegeWeapon.CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return false;
                }
            }
            if (IsBetweenXTiles && IsBetweenYTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos + 1, YPos + 1), siegeWeapon.CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return false;
                }
            }
        }
        return true;
    }
    public final void DestroyShip(CShip ship) {
        boolean Advance = true;
        for (int i = DShip.size() - 1; i >= 0 && Advance; i--) {
            CShip currShip = DShip.get(i);
            if (currShip == ship) {
                DShip.remove(i);
                Advance = false;
            }
        }
    }
    public final void DestroySiege(CSiegeWeapons siege) {
        boolean Advance = true;
        for (int i = DSiegeWeapons.size() - 1; i >= 0 && Advance; i--) {
            CSiegeWeapons currSiegeWeapon = DSiegeWeapons.get(i);
            if (currSiegeWeapon == siege) {
                DSiegeWeapons.remove(i);
                Advance = false;
            }
        }
    }
    public final CShip GetShipAt(Vector2 position) {
        Vector2 size = new Vector2(CShip.CShipSize);
        for (CShip ship : DShip) {
            if (CMathUtil.DoRectanglesOverlap(ship.DPosition, ship.CShipSize, position, size)) {
                return ship;
            }
        }
        return null;
    }
    public final CSiegeWeapons GetSiegeAt(CGame game, Vector2 position) {
        Vector2 size = new Vector2(CSiegeWeapons.CSiegeWeaponSize);
        for (CSiegeWeapons siegeWeapon : DSiegeWeapons) {
            if (CMathUtil.DoRectanglesOverlap(siegeWeapon.DPosition, siegeWeapon.CSiegeWeaponSize, position, size)) {
                return siegeWeapon;
            }
            CTerrainMap TerrainMap = game.GameState().TerrainMap();
            float XPos = (siegeWeapon).DPosition.x;
            float YPos = (siegeWeapon).DPosition.y;
            boolean IsBetweenXTiles = 0 != XPos - (int) XPos && TerrainMap.Width() - 2 >= XPos;
            boolean IsBetweenYTiles = 0 != YPos - (int) YPos && TerrainMap.Height() - 2 >= YPos;
            if (IsBetweenXTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos + 1, YPos), (siegeWeapon).CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return siegeWeapon;
                }
            }
            if (IsBetweenYTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos, YPos + 1), (siegeWeapon).CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return siegeWeapon;
                }
            }
            if (IsBetweenXTiles && IsBetweenYTiles) {
                if (CMathUtil.DoRectanglesOverlap(new Vector2(XPos + 1, YPos + 1), (siegeWeapon).CSiegeWeaponSize, new Vector2(position), new Vector2(size))) {
                    return siegeWeapon;
                }
            }
        }
        return null;
    }
    public final LinkedList<CShip> Ship() {
        return DShip;
    }
    public final LinkedList<CSiegeWeapons> SiegeWeapons() {
        return DSiegeWeapons;
    }
    public final LinkedList<CSiegeWeapons> MovingSiege() {
        return DMovingSiege;
    }
    public LinkedList<CShip> DShip = new LinkedList<CShip>();
    public LinkedList<CSiegeWeapons> DSiegeWeapons = new LinkedList<CSiegeWeapons>();
    public LinkedList<CSiegeWeapons> DMovingSiege = new LinkedList<CSiegeWeapons>();
}