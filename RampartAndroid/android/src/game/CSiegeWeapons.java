package game;

import com.badlogic.gdx.math.Vector2;

import game.animations.CExplosionAnimation;
import game.animations.DefineConstants;
import game.animations.SBurnAndExplosion;
import game.players.CPlayer;
import game.utils.CMathUtil;
import game.utils.MathUtil;


public class CSiegeWeapons {
    /**
*
* @brief Creates a siege weapon at a position
*
* @param position The position of the siege weapon

*/
    public CSiegeWeapons(Vector2 position) {
        DPosition = new Vector2(position.x, position.y);
        DDirection = SDirection.EValue.dNorth;
        DHitsTaken = 0;
    }

    /**
*
* @brief Creates a siege weapon at a position
*
* @param position The position of the siege weapon
* @param Player The player who owns the siege weapon

*/
    public CSiegeWeapons(Vector2 position, CPlayer Player) {
        DPosition = new Vector2(position.x, position.y);
        DDirection = SDirection.EValue.dNorth;
        DOwner = Player;
        DHitsTaken = 0;
    }

    /**
*
* @brief Creates a siege weapon at a position
*
* @param position The position of the siege weapon
* @param target The destination positon for the siege weapon to move to
* @param game The game the siege weapon is in

*/
    public CSiegeWeapons(Vector2 position, Vector2 target, CGame game) {
        DPosition = new Vector2(position.x, position.y);
        DDirection = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        DIsMoving = true;
        Vector2 Delta = target.cpy().sub(position);
        int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
        DVelocity.x = Delta.x / Distance;
        DVelocity.y = Delta.y / Distance;
        DistanceLeft = Distance;
        DHitsTaken = 0;
        //game->GameState()->Units()->MovingSiege().add(this);
    }

    /**
*
* @brief Creates a siege weapon at a position
*
* @param position The position of the siege weapon
* @param siegetarget The position the siege weapon will attack
* @param Player The player who owns the siege weapon
* @param move Whether or not the siege weapon will move right away
* @param game The game the siege weapon is in

*/
    public CSiegeWeapons(Vector2 position, Vector2 siegetarget, CPlayer Player, boolean move, CGame game) {
        SiegeTarget = new Vector2(siegetarget);
        DPosition = new Vector2(position.x, position.y);
        Vector2 target = new Vector2();
        if(move) {
            target = new Vector2(SiegeTarget);
            //target = position + Vector2(5,0);
        }
        else {
            target = position.cpy();
        }
        DDirection = (SDirection.EValue)CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        DOwner = Player;
        DIsMoving = true;
        Vector2 Delta = target.cpy().sub(position);
        int Distance = CMathUtil.IntegerSquareRoot((int)MathUtil.magnitude(Delta));
        DVelocity.x = Delta.x / Distance;
        DVelocity.y = Delta.y / Distance;
        DistanceLeft = Distance;
        DHitsTaken = 0;
        //game->GameState()->Units()->MovingSiege().add(this);

    }

    /**
*
* @brief Empty Constructor

*/
    public CSiegeWeapons() {
    }

    /**
*
* @brief Draws the 2D tile for the siege weapon
*
* @param game The game to draw in

*/
    public final void Draw2D(CGame game) {
        game.Resources().DTilesets.D2DSiegeTileset.DrawTile(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), DDirection);
    }

    /**
*
* @brief Draws the 3D tile for the cannon
*
* @param game The game to draw in

*/
    public final void Draw3D(CGame game) {
        game.Resources().DTilesets.D3DSiegeTileset.DrawTile(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), DDirection);
    }

    /**
*
* @brief gets the player who owns the siege weapon
*
* @param game The game the siege weapon is in

*/
    public final CPlayer GetPlayerOwner(CGame game) {
        return DOwner;
    }

    /**
*
* @brief Sets a target destination for the siege weapon
*
* @param game The game the siege weapon is in
* @param target The target destination

*/
    public final void Move(CGame game, Vector2 target) {

        //std::cout << "Siege is moving" << std::endl;

        DDirection = CMathUtil.CalculateDirection(new Vector2(DPosition), new Vector2(target));
        DIsMoving = true;
        Vector2 Delta = target.cpy().sub(DPosition);
        int Distance = CMathUtil.IntegerSquareRoot((int)MathUtil.magnitude(Delta));
        DVelocity.x = Delta.x / Distance;
        DVelocity.y = Delta.y / Distance;
        DistanceLeft = Distance;
        //game->GameState()->Units()->MovingSiege().add(this);
    }

    /**
*
* @brief Moves siege weapon toward target destination
*
* @param game The game the siege weapon is in

*/
    public final void Update(CGame game) {
        //std::cout<< "Updating\n";
        // WindX and WindY are placeholders

        if (DIsMoving) {
            //Stops and destroys the first wall it runs into
            if (game.GameState().ConstructionMap().GetTileAt(new Vector2((int)DPosition.x, (int)DPosition.y)).IsWall()) {
                DistanceLeft = 0;
            }


            DPosition.x += DVelocity.x * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
            DPosition.y += DVelocity.y * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
            DistanceLeft -= (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;

            if (DistanceLeft <= 0) {
                CTerrainMap TerrainMap = game.GameState().TerrainMap();
                Vector2 IndexPosition = TerrainMap.ConvertToTileIndex(new Vector2(DPosition));
                DIsMoving = false;
                Attack(game);
            }
        }
    }

    /**
*
* @brief Returns whether or not the siege weapon is alive
*
* @return Whether or not the siege weapon is alive

*/
    public final boolean IsAlive() {
        return DHitsTaken < 1;
    }

    /**
*
* @brief Returns whether or not the siege weapon is moving
*
* @return Whether or not the siege weapon is moving

*/
    public final boolean IsMoving() {
        return DIsMoving;
    }

    /**
*
* @brief Destroys wall if on top of wall
*
* @param game The game the siege weapon is in

*/
    public final void Attack(CGame game) {
        //Vector2 position = game->GameState()->TerrainMap()->ConvertToTileIndex(Vector2((int)DPosition.x,(int)DPosition.y));
        Vector2 position = new Vector2((int)DPosition.x, (int)DPosition.y);
        if(game.GameState().ConstructionMap().GetTileAt(new Vector2(position)).IsWall()) {
            game.GameState().ConstructionMap().GetTileAt(new Vector2(position)).DestroyWall();
        }
        else {
            switch(DDirection) {
                case dNorth:
                    position.add(new Vector2(0,-1));
                    break;
                case dEast:
                    position.add(new Vector2(1,0));
                    break;
                case dSouth:
                    position.add(new Vector2(0,1));
                    break;
                case dWest:
                    position.add(new Vector2(-1,0));
                    break;
                default:
                    break;
            }

            if(game.GameState().ConstructionMap().GetTileAt(new Vector2(position)).IsWall()) {
                game.GameState().ConstructionMap().GetTileAt(new Vector2(position)).DestroyWall();
            }
        }
        System.out.print("Position.x: ");
        System.out.print(DPosition.x);
        System.out.print(" Position.y: ");
        System.out.print(DPosition.y);
        System.out.print("\n");
        game.GameState().DAnimations.add(new CExplosionAnimation(game, DPosition.cpy().sub(new Vector2(6,3)), position, (game.GameState().DRandomNumberGenerator.Random()%2 != 0) ? SBurnAndExplosion.EExplosionType.etWallExplosion0 : SBurnAndExplosion.EExplosionType.etWallExplosion1));
    }

    //        *
//         * @brief The sixe of the siege weapon: (1,1)
//
    public static Vector2 CSiegeWeaponSize = new Vector2(1, 1);

    //        *
//         * @brief The position of the siege weapon
//
    public Vector2 DPosition = new Vector2();

    //        *
//         * @brief The velocity of the siege weapon
//
    public Vector2 DVelocity = new Vector2();

    //        *
//         * @brief How far the siege weapon is from its destination
//
    public double DistanceLeft;

    //        *
//         * @brief Whether or not the siege weapon is moving
//
    public boolean DIsMoving;

    //        *
//         * @brief The siege weapon's owner
//
    public CPlayer DOwner;

    //        *
//         * @brief Number of times hit by cannonball
//
    public int DHitsTaken;

    public Vector2 SiegeTarget = new Vector2();
    //        *
//         * @brief Direction the siege weapon is facing
//
    protected SDirection.EValue DDirection;
    //Vector2 DPosition;
    //std::vector< double > DInitialVelocities;
    //SDirection::EValue CalcDirection(Vector2 targetCoords);
    //SDouble3 CalcFiringSolution(Vector2 targetCoords);
}