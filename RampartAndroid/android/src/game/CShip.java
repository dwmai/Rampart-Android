package game;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;

import game.animations.CPlumeAnimation;
import game.animations.DefineConstants;
import game.players.CPlayer;
import game.utils.CMathUtil;
import game.utils.MathUtil;


public class CShip extends CCannonballOwner {
    /**
*
* @brief The size of the ship: (2,2)

*/
    public static Vector2 CShipSize = new Vector2(2, 2);
    /**
*
* @brief The position of the ship

*/
    public Vector2 DPosition = new Vector2();
    /**
*
* @brief The velocity of the ship

*/
    public Vector2 DVelocity = new Vector2(0, 0);
    /**
*
* @brief How far the ship is from its destination

*/
    public double DistanceLeft;
    /**
*
* @brief Whether or not the ship is moving

*/
    public boolean DIsMoving;
    /**
*
* @brief The ship's owner

*/
    public CPlayer DOwner;
    /**
*
* @brief Number of siege weapons the ship is holding

*/
    public int DNumSiegeWeapons;
    /**
*
* @brief Number of times the ship has been hit by cannonballs

*/
    public int DHitsTaken;
    /**
*
* @brief Animation frame this ship is on

*/
    public int DAnimationStep;
    public LinkedList<Vector2> SiegeTarget;

/**
*
* @brief Sets a target destination for the ship
*
* @param game The game the ship is in
* @param target The target destination

*/

    //void CShip::Draw3D(CGame* game){
    //    C3DShipsTileset& Tileset = game->Resources()->DTilesets->D3DShipsTileset;
    //    CTerrainMap* Map = game->GameState()->TerrainMap();
    //    Vector2 Position = Map->ConvertToScreenPosition(DIndexPosition - Vector2(0, 1));
    //    Tileset.Draw3DShipsTile(TODO);
    //
    //    for(std::vector<CShip*>::iterator it = game->GameState()->DShips.begin();
    //            it != game->GameState()->DShips.end();
    //            it++){
    //        game->Resources()->DTilesets->D3DShipsTileset.DrawTile(game,
    //            game->GameState()->TerrainMap()->ConvertToScreenPosition(DPosition),
    //            direction);
    //    }
    //}
    //        *
//         * @brief Direction the ship is facing
//
    protected SDirection.EValue DDirection;
    public SDirection.EValue Direction() { return DDirection; }
    //Vector2 DPosition;
    protected int DSinkingFramesLeft;

    /**
*
* @brief Creates a ship at a position
*
* @param position The position of the ship
* @param NumSiegeWeapons The number of siege weapons it holds

*/
    public CShip(Vector2 position, int NumSiegeWeapons) {
        DNumSiegeWeapons = NumSiegeWeapons;
        DPosition = new Vector2(position.x, position.y);
        //Log.info(String.format("DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        DDirection = SDirection.EValue.dNorth;
        DHitsTaken = 0;
        DAnimationStep = 0;
        DSinkingFramesLeft = DefineConstants.SHIP_SINKING_ANIMATION_TIMESTEPS;
    }

    /**
*
* @brief Creates a ship at a position
*
* @param position The position of the ship
* @param Player The player who owns the ship
* @param NumSiegeWeapons The number of siege weapons it holds

*/
    public CShip(Vector2 position, CPlayer Player, int NumSiegeWeapons) {
        DNumSiegeWeapons = NumSiegeWeapons;
        DPosition = new Vector2(position.x, position.y);
        //Log.info(String.format("DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        DDirection = SDirection.EValue.dNorth;
        DOwner = Player;
        DHitsTaken = 0;
        DAnimationStep = 0;
        DSinkingFramesLeft = DefineConstants.SHIP_SINKING_ANIMATION_TIMESTEPS;
    }

    /**
*
* @brief Creates a ship at a position
*
* @param position The position of the ship
* @param target The destination positon for the ship to move to
* @param game The game the ship is in
* @param NumSiegeWeapons The number of siege weapons it holds

*/
    public CShip(Vector2 position, Vector2 target, CGame game, int NumSiegeWeapons) {
        DNumSiegeWeapons = NumSiegeWeapons;
        DPosition = new Vector2(position.x, position.y);
        //Log.info(String.format("DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        DDirection = CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        if (!(position.equals(target))) {
            DIsMoving = true;
            Vector2 Delta = target.cpy().sub(position);
            int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
            DVelocity.x =  Delta.x / Distance;
            DVelocity.y =  Delta.y / Distance;
            //Log.info("DVelocity: [%.2f, %.2f]", DVelocity.x, DVelocity.y);
            DistanceLeft = Distance;
        } else {
            DIsMoving = false;
        }
        DHitsTaken = 0;
        DAnimationStep = 0;
        DSinkingFramesLeft = DefineConstants.SHIP_SINKING_ANIMATION_TIMESTEPS;
        //game->GameState()->Units()->MovingShips().add(this);
    }

    /**
*
* @brief Creates a ship at a position
*
* @param position The position of the ship
* @param Player the player who owns the ship
* @param target The destination positon for the ship to move to
* @param target The destination positon for the ship to move to
* @param NumSiegeWeapons The number of siege weapons it holds

*/
    public CShip(Vector2 position, CPlayer Player, Vector2 target, List<Vector2> siegetarget, int NumSiegeWeapons) {
        SiegeTarget = new LinkedList<Vector2>(siegetarget);
        DNumSiegeWeapons = NumSiegeWeapons;
        DPosition = new Vector2(position.x, position.y);
        //Log.info(String.format("DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        DDirection = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        if (!(position.equals(target))) {
            DIsMoving = true;
            Vector2 Delta = target.cpy().sub(position);
            int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
            DVelocity.x =  Delta.x / Distance;
            DVelocity.y =  Delta.y / Distance;
            //Log.info("DVelocity: [%.2f, %.2f]", DVelocity.x, DVelocity.y);
            DistanceLeft = Distance;
        } else {
            DIsMoving = false;
        }
        DOwner = Player;
        DHitsTaken = 0;
        DAnimationStep = 0;
        DSinkingFramesLeft = DefineConstants.SHIP_SINKING_ANIMATION_TIMESTEPS;
    }

    /**
*
* @brief Creates a ship at a position
*
* @param position The position of the ship
* @param Player The player who owns the ship
* @param move Whether or not the ship will move right away
* @param game The game the ship is in
* @param NumSiegeWeapons The number of siege weapons it holds

*/
    public CShip(Vector2 position, CPlayer Player, boolean move, CGame game, int NumSiegeWeapons) {
        DNumSiegeWeapons = NumSiegeWeapons;
        DPosition = new Vector2(position.x, position.y);
        //Log.info(String.format("DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        Vector2 target = new Vector2();
        if (move) {
            target = position.cpy().add(new Vector2(5, 0));
        } else {
            target = position.cpy();
        }
        DDirection = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        DOwner = Player;
        DIsMoving = true;
        Vector2 Delta = target.cpy().sub(position);
        int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
        DVelocity.x =  Delta.x / Distance;
        DVelocity.y =  Delta.y / Distance;
        //Log.info("DVelocity: [%.2f, %.2f]", DVelocity.x, DVelocity.y);
        DistanceLeft = Distance;
        DHitsTaken = 0;
        DAnimationStep = 0;
        DSinkingFramesLeft = DefineConstants.SHIP_SINKING_ANIMATION_TIMESTEPS;
        //game->GameState()->Units()->MovingShips().add(this);

    }

    /**
*
* @brief Empty Constructor

*/
    public CShip() {
    }

    /**
*
* @brief Draws the 2D tile for the Ship
*
* @param game The game to draw in

*/
    public final void Draw2D(CGame game) {
        game.Resources().DTilesets.D2DShipsTileset.DrawTile(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2((int)DPosition.x, (int)DPosition.y)), DDirection);
    }

    /**
*
* @brief Draws the 3D tile for the cannon
*
* @param game The game to draw in

*/
    public final void Draw3D(CGame game) {
        //SDirection::EValue direction = SDirection::dNorth;
        CPlayer Owner = GetPlayerOwner(game);
        if (!IsMoving()) {
            if (Owner != null) {
                //Vector2 CenterPosition = game->GameState()->TerrainMap()
                //    ->ConvertToScreenPosition(Vector2((int)DPosition.x, (int)DPosition.y) + Vector2(1, 1));
                DDirection = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(Owner.DCursorPosition));
            }
        }

        if (DHitsTaken == 0) {
            game.Resources().DTilesets.D3DShipsTileset.DrawTile(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), DDirection);
            //Log.info(String.format("Drawing Ship at DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        } else {
            game.Resources().DTilesets.D3DShipsBurnTileset.DrawTile(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), DDirection, (DAnimationStep / 4) % DefineConstants.SHIP_ANIMATION_TIMESTEPS);
        }
    }

    /**
*
* @brief Fires a shipcannonball at the target and adds the plume animation
*
* @param game The game to fire in
* @param target The pixel position of the target

*/
    public final void FireAt(CGame game, Vector2 target) {
        Vector2 CenterPosition = game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2((int) DPosition.x, (int) DPosition.y).add(new Vector2(1, 1)));
        game.GameState().DAnimations.add(new CPlumeAnimation(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), new Vector2(DPosition), (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(CenterPosition), new Vector2(target))));
        game.GameState().DCannonballs.add(new CCannonball(game, CenterPosition, target, this, game.GameState().DWind));
    }

    /**
*
* @brief gets the player who owns the ship
*
* @param game The game the ship is in

*/
    public final CPlayer GetPlayerOwner(CGame game) {
        return DOwner;
    }

    public final void Move(CGame game, Vector2 target) {
        DDirection = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        DIsMoving = true;
        Vector2 Delta = target.cpy().sub(DPosition);
        int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
        DVelocity.x =  Delta.x / Distance;
        DVelocity.y =  Delta.y / Distance;
        //Log.info("DVelocity: [%.2f, %.2f]", DVelocity.x, DVelocity.y);
        DistanceLeft = Distance;
        //game->GameState()->Units()->MovingShips().add(this);
    }

    /**
*
* @brief Moves ship toward target destination
*
* @param game The game the ship is in

*/
    public final void Update(CGame game) {
        //Log.info(String.format("In CShip.Update -> DPosition: [%.2f, %.2f]", DPosition.x, DPosition.y));
        DAnimationStep += 1;
        if (DIsMoving) {
            //std::cout<< "Updating\n";
            // WindX and WindY are placeholders
            Vector2 WindVector = new Vector2(game.GameState().DWind.GetVector());
            double multiplier = 1;
            boolean XMatch = (WindVector.x < 0 && DVelocity.x < 0) || DVelocity.x == 0 || (WindVector.x > 0 && DVelocity.x > 0);
            boolean YMatch = (WindVector.y < 0 && DVelocity.y < 0) || DVelocity.y == 0 || (WindVector.y > 0 && DVelocity.y > 0);
            if (XMatch && YMatch) {
                multiplier = 2;
            } else if (XMatch || YMatch) {
                multiplier = 1;
            } else {
                multiplier = .5;
            }
            double delta_x = DVelocity.x * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0 * multiplier;
            //Log.info("Adding %.2f to DPosition.x [%.2f] = %.2f", delta_x, DPosition.x, DPosition.x + delta_x);
            DPosition.x += DVelocity.x * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0 * multiplier;
            double delta_y = DVelocity.y * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0 * multiplier;
            //Log.info("Adding %.2f to DPosition.y [%.2f] = %.2f", delta_y, DPosition.y, DPosition.y + delta_y);
            DPosition.y += DVelocity.y * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0 * multiplier;
            DistanceLeft -= (double) DefineConstants.TIMEOUT_INTERVAL / 500.0 * multiplier;
        }

        if (DHitsTaken >= 2) {
            DSinkingFramesLeft--;
        }

        if (DistanceLeft <= 0) {
            CTerrainMap TerrainMap = game.GameState().TerrainMap();
            Vector2 IndexPosition = TerrainMap.ConvertToTileIndex(new Vector2(DPosition));
            DIsMoving = false;
            PlaceSiegeWeapon(game);
        }
    }

    /**
*
* @brief Places a siege weapon in front of the ship
*
* @param game The game the ship is in

*/
    public final void PlaceSiegeWeapon(CGame game) {
        if (DNumSiegeWeapons > 0) {
            //DNumSiegeWeapons--;
            Vector2 position = new Vector2(DPosition);
            switch (DDirection) {
                case dNorth:
                    position.add(new Vector2(0, -1));
                    break;
                case dNorthEast:
                    position.add(new Vector2(2, -1));
                    break;
                case dEast:
                    position.add(new Vector2(2, 0));
                    break;
                case dSouthEast:
                    position.add(new Vector2(2, 2));
                    break;
                case dSouth:
                    position.add(new Vector2(0, 2));
                    break;
                case dSouthWest:
                    position.add(new Vector2(-1, 2));
                    break;
                case dWest:
                    position.add(new Vector2(-1, 0));
                    break;
                case dNorthWest:
                    position.add(new Vector2(-1, -1));
                    break;
                default:
                    break;

            }

            //CSiegeWeapons* newSiege = new CSiegeWeapons(position, DOwner, true, game);

            for (int i = 0; i < DNumSiegeWeapons; i++) {
                Vector2 target = SiegeTarget.get(i);
                CSiegeWeapons newSiege = new CSiegeWeapons(position, target, DOwner, true, game);
                game.GameState().Units().SiegeWeapons().add(newSiege);
                //        SiegeTarget.x++;
                //        SiegeTarget.y++;
            }
            DNumSiegeWeapons = 0;
        }

    }

    /**
*
* @brief Returns whether or not the ship is alive
*
* @return Whether or not the ship is alive

*/
    public final boolean IsAlive() {
        return DSinkingFramesLeft >= 8;
    }

    /**
*
* @brief Returns whether or not the ship is moving
*
* @return Whether or not the ship is moving

*/
    public final boolean IsMoving() {
        return DIsMoving;
    }

    /**
*
* @brief pushes ship back into owner's ready ship queue when cannonball dies
*
* @param game the game the ship is in

*/
    public final void CannonballDestroyed(CGame game) {
        GetPlayerOwner(game).DReadyShipCannons.add(this);
    }
}
