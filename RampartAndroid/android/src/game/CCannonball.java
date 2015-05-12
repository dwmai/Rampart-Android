package game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import game.animations.CExplosionAnimation;
import game.animations.CShipExplosionAnimation;
import game.animations.DefineConstants;
import game.animations.SBurnAndExplosion;
import game.players.CPlayer;
import game.tilesets.CGraphicTileset;
import game.tilesets.EConstructionTileType;
import game.utils.ArrayUtil;
import game.utils.CMathUtil;
import game.utils.Log;
import game.utils.MathUtil;

/**
 * Class representing Cannonball
 */
public class CCannonball {

	/**
	 * @brief Stores the precomputed velocities need to head to target
	 */
	public static java.util.ArrayList<Float> CInitialVelocities = new java.util.ArrayList<Float>();
	// these will either be initialized in constructor or passed in with something else
	protected boolean DIsAlive;
	/**
	 * @brief Position of the ball
	 */
	protected Vector3 DPosition = new Vector3();
	/**
	 * @brief Velocity of the ball
	 */
	protected Vector3 DVelocity = new Vector3();
	/**
	 * @brief The originating cannon used for reloading
	 */
	protected CCannonballOwner DCannonballOwner;
	/**
	 * @brief Not used yet
	 */
	protected int DToneID;
	/**
	 * @brief Stores the wind properties on this cannonball
	 */
	protected CWind DWind = new CWind();

	/**
	 * @brief Makes cannonball headed to a target fired from a cannon
	 * @param position
	 *            The position starting from
	 * @param target
	 *            The target headed to
	 * @param cannon
	 *            The originating cannon, for reloading
	 * @param wind
	 *            The wind for this cannonball
	 */
	public CCannonball(CGame game, Vector2 position, Vector2 target, CCannonballOwner owner, CWind wind) {
        DToneID = -1; //So we know the tone hasn't been played yet
		LoadInitialVelocities(game);
		DPosition = new Vector3(position.x, position.y, 0);
		DWind = new CWind(wind);
		Vector2 Delta = target.cpy().sub(position);
		int Distance = CMathUtil.IntegerSquareRoot((int) MathUtil.magnitude(Delta));
		DVelocity.z = CInitialVelocities.get(Distance);
		DVelocity.x = Delta.x * DVelocity.z / Distance;
		DVelocity.y = Delta.y * DVelocity.z / Distance;
		DCannonballOwner = owner;
		DIsAlive = true;
	}

	/**
	 * @brief The load precomputed velocities needed to head to target
	 */
	public static void LoadInitialVelocities(CGame game) {
		if (CInitialVelocities.size() == 0) {
			int size = CMathUtil.IntegerSquareRoot(DefineConstants.GAME_WIDTH * DefineConstants.GAME_WIDTH
					+ DefineConstants.GAME_HEIGHT * DefineConstants.GAME_HEIGHT) + 1;
			CInitialVelocities = ArrayUtil.resize(new ArrayList<Float>(size), size);
			for (int Index = 0; Index < CInitialVelocities.size(); Index++) {
				CInitialVelocities.set(Index, (float) Math.sqrt(DefineConstants.STANDARD_GRAVITY * Index / 2));
			}
		}
	}

	/**
	 * Compares positions of two cannonballs
	 * 
	 * @param first
	 *            const reference to a CannonBall
	 * @param second
	 *            const reference to a CannonBall
	 * @return True if first's position is greater than second's position, false if first's position is greater than
	 *         second's position; checked in order of z position, y position, x position
	 */
	public static boolean TrajectoryCompare(CCannonball first, CCannonball second) {
		if (first.DPosition.z < second.DPosition.z) {
			return true;
		}
		if (first.DPosition.z > second.DPosition.z) {
			return false;
		}
		if (first.DPosition.y < second.DPosition.y) {
			return true;
		}
		if (first.DPosition.y > second.DPosition.y) {
			return false;
		}
		if (first.DPosition.x < second.DPosition.x) {
			return true;
		}
		return false;
	}

	/**
	 * @brief Returns if cannonball should continue its existence
	 * @return DIsAlive
	 */
	public final boolean IsAlive() {
		return DIsAlive;
	}


	/**
	 * Getter for position of the Cannonball
	 */
	public final Vector3 Position() {
		return DPosition;
	}


	/**
	 * Setter for position of the Cannonball
	 */
	public final void Position(Vector3 PositionIn) {
		DPosition = new Vector3(PositionIn);
	}


	/**
	 * Getter for velocity of the Cannonball
	 */
	public final Vector3 Velocity() {
		return DVelocity;
	}


	/**
	 * Setter for Velocity of the Cannonball
	 */
	public final void Velocity(Vector3 VelocityIn) {
		DVelocity = new Vector3(VelocityIn);
	}


	/**
	 * Getter for the Cannonball's cannon
	 */
	public final CCannonballOwner Owner() {
		return DCannonballOwner;
	}


	/**
	 * Draw the Cannonball Will probably have to pass in a pointer to game or at least the drawing context
	 */
	public final void Draw(CGame game) {
		CGraphicTileset Tileset = game.Resources().DTilesets.D3DCannonballTileset;

		Vector2 loc = new Vector2((int) DPosition.x, (int) DPosition.y).sub(
				new Vector2(Tileset.TileWidth(), Tileset.TileHeight()).scl(0.5f)).sub(new Vector2(0, DPosition.z));
		Tileset.DrawTile(game, loc, CalculateCannonballSize());
	}

	/**
	 * Calculates cannonball size depending on z position
	 * 
	 * @param z
	 *            z position of cannonball
	 * @return cannonball size
	 */
	public final int CalculateCannonballSize() {

		if (100.1499064 < DPosition.z) {
			return 11;
		}
		if (97.5217589 < DPosition.z) {
			return 10;
		}
		if (95.41127261 < DPosition.z) {
			return 9;
		}
		if (92.81657303 < DPosition.z) {
			return 8;
		}
		if (89.55578929 < DPosition.z) {
			return 7;
		}
		if (85.34315122 < DPosition.z) {
			return 6;
		}
		if (79.70240953 < DPosition.z) {
			return 5;
		}
		if (71.77635988 < DPosition.z) {
			return 4;
		}
		if (59.85065264 < DPosition.z) {
			return 3;
		}
		return 2;
	}

	/**
	 * @brief Moves cannonball in air and calculates if collided and destroys walls. Updates if it is alive, and reloads
	 *        the cannon if not.
	 * @param game
	 *            The game updating in
	 */
	public final void Update(CGame game) {
		try {
			Vector2 wind2 = game.GameState().DWind.GetVector();
			Vector3 WindVector = new Vector3(wind2.x, wind2.y, 0);
			DVelocity.add(WindVector);

			DPosition.x += DVelocity.x * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
			DPosition.y += DVelocity.y * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
			DPosition.z += DVelocity.z * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
			DVelocity.z -= DefineConstants.STANDARD_GRAVITY * (double) DefineConstants.TIMEOUT_INTERVAL / 500.0;
			boolean WasAlive = DIsAlive;

			if (DVelocity.z < 0) {
                PlayFallingTone(game);

				CTerrainMap TerrainMap = game.GameState().TerrainMap();
				Vector2 IndexPosition = TerrainMap.ConvertToTileIndex(new Vector2(DPosition.x, DPosition.y));
				if (CMathUtil.DoRectanglesOverlap(new Vector2(IndexPosition), new Vector2(), new Vector2(),
						new Vector2(TerrainMap.Width(), TerrainMap.Height()))) {
					if ((DPosition.z < 2 * TerrainMap.ConvertToScreenPosition(new Vector2(0, 1)).y)
							&& game.GameState().ConstructionMap().GetTileAt(new Vector2(IndexPosition)).IsWall()) {
						if (10 <= ++game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
								(int) IndexPosition.x).DHitsTaken) {
							if (game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
									(int) IndexPosition.x).IsFloor()) {
								if (game.GameState().DRandomNumberGenerator.Random() % 5 == 0) {
									switch (game.GameState().TerrainMap().TileType((int) IndexPosition.y,
											(int) IndexPosition.x)) {
									case pcBlue:
										game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
												(int) IndexPosition.x).DType = EConstructionTileType.cttBlueFloorDamaged;
										break;
									case pcRed:
										game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
												(int) IndexPosition.x).DType = EConstructionTileType.cttRedFloorDamaged;
										break;
									case pcYellow:
										game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
												(int) IndexPosition.x).DType = EConstructionTileType.cttYellowFloorDamaged;
										break;
									default:
										game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
												(int) IndexPosition.x).DType = EConstructionTileType.cttNone;
										break;
									}
								}
							}
						}
						game.GameState().ConstructionMap().GetTileAt(new Vector2(IndexPosition)).DestroyWall();
						game.GameState().DAnimations.add(new CExplosionAnimation(
								game,
								new Vector2(DPosition.x, DPosition.y).cpy().sub(new Vector2(6, 3)),
								IndexPosition,
								game.GameState().DRandomNumberGenerator.Random() % 2 != 0 ? SBurnAndExplosion.EExplosionType.etWallExplosion0
										: SBurnAndExplosion.EExplosionType.etWallExplosion1));
						DIsAlive = false;
					}
                    else if((DPosition.z < TerrainMap.ConvertToScreenPosition(new Vector2(0, 2)).y) && !game.GameState().Units().IsSpaceFreeOfShip(new Vector2(IndexPosition), new Vector2(1, 1))) {
                        CShip DHitShip = game.GameState().Units().GetShipAt(new Vector2(IndexPosition));
                        DHitShip.DHitsTaken++;
                        //Log.info("DHitShip.DHitsTaken is now %d", DHitShip.DHitsTaken);
                        if(DHitShip.DHitsTaken == 2) {
                            //Log.info("Drawing explosion animation at DHitShip.Position[%.2f, %.2f]", DHitShip.DPosition.x, DHitShip.DPosition.y);
                            game.GameState().DAnimations.add(new CShipExplosionAnimation(game, game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2((int)DHitShip.DPosition.x, (int)DHitShip.DPosition.y)), new Vector2((int)DHitShip.DPosition.x, (int)DHitShip.DPosition.y), DHitShip.Direction()));
                        }
                        DIsAlive = false;
                    }
                    else if((DPosition.z <= TerrainMap.ConvertToScreenPosition(new Vector2(0, 1)).y) && !game.GameState().Units().IsSpaceFreeOfSiege(game, new Vector2(IndexPosition), new Vector2(1,1))) {
//C++ TO JAVA CONVERTER TODO TASK: Pointer arithmetic is detected on this variable, so pointers on this variable are left unchanged.
                        CSiegeWeapons DHitSiege = game.GameState().Units().GetSiegeAt(game, new Vector2(IndexPosition));
                        if(1 <= ++DHitSiege.DHitsTaken) {
                            game.GameState().DAnimations.add(new CExplosionAnimation(game, new Vector2((int)DPosition.x, (int)DPosition.y).sub(new Vector2(6, 3)), IndexPosition, (game.GameState().DRandomNumberGenerator.Random() % 2 != 0) ? SBurnAndExplosion.EExplosionType.etWallExplosion0 : SBurnAndExplosion.EExplosionType.etWallExplosion1));
                        }
                        DIsAlive = false;
                    }
                    else if (DPosition.z <= 0.0) {
                        game.Resources().DSounds.DSoundMixer.StopTone(DToneID); //Stop tone once it hits the ground
						char compare = game.GameState().TerrainMap().StringMap().get((int) IndexPosition.y + 1).charAt(
								(int) IndexPosition.x + 1);
						if (compare == ' ') {
							game.GameState().DAnimations.add(new CExplosionAnimation(
									game,
									new Vector2(DPosition.x, DPosition.y).cpy().sub(new Vector2(18, 12)),
									IndexPosition,
									game.GameState().DRandomNumberGenerator.Random() % 2 != 0 ? SBurnAndExplosion.EExplosionType.etWaterExplosion0
											: SBurnAndExplosion.EExplosionType.etWaterExplosion1));
						} else {
							if (5 <= ++game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
									(int) IndexPosition.x).DHitsTaken) {
								CPlayer CannonballOwner = DCannonballOwner.GetPlayerOwner(game);
								if (game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
										(int) IndexPosition.x).IsFloor()) {
									if (CannonballOwner.DRandomNumberGenerator.Random() % 5 == 0) {
										switch (game.GameState().TerrainMap().TileType((int) IndexPosition.y,
												(int) IndexPosition.x)) {
										case pcBlue:
											game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
													(int) IndexPosition.x).DType = EConstructionTileType.cttBlueFloorDamaged;
											break;
										case pcRed:
											game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
													(int) IndexPosition.x).DType = EConstructionTileType.cttRedFloorDamaged;
											break;
										case pcYellow:
											game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
													(int) IndexPosition.x).DType = EConstructionTileType.cttYellowFloorDamaged;
											break;
										default:
											game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
													(int) IndexPosition.x).DType = EConstructionTileType.cttNone;
											break;
										}
									}
								} else if (game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
										(int) IndexPosition.x).DType == EConstructionTileType.cttNone) {
									if (0 == (CannonballOwner.DRandomNumberGenerator.Random() % 5)) {
										game.GameState().ConstructionMap().DTiles.get((int) IndexPosition.y).get(
												(int) IndexPosition.x).DType = EConstructionTileType.cttGroundDamaged;
									}
								}
							}
							game.GameState().DAnimations.add(new CExplosionAnimation(
									game,
									new Vector2((int) DPosition.x, (int) DPosition.y).sub(new Vector2(18, 6)),
									IndexPosition,
									game.GameState().DRandomNumberGenerator.Random() % 2 != 0 ? SBurnAndExplosion.EExplosionType.etGroundExplosion0
											: SBurnAndExplosion.EExplosionType.etGroundExplosion1));
						}
						DIsAlive = false;
					}
				} else {
					DIsAlive = false;
				}
			}

			if (WasAlive && !DIsAlive) {
                DCannonballOwner.CannonballDestroyed(game);
			}
		} catch (Exception ex) {
			Log.critical(ex, "Cannonball Error");
		}
	}

    /**
*
* Play tone for falling cannonball
* @param game The game we're updating

*/
    public final void PlayFallingTone(CGame game) {
        if(DToneID != -1) //only want to play tone once
            return;
        float DSoundEffectVolume = 1;

        int CannonBallSize = CalculateCannonballSize();

        float freq = (float)(2500.0 + (CannonBallSize / 12.0) * 500.0);
        float freqdecay = -500.0f;
        float volume = (float)(DSoundEffectVolume * CannonBallSize / 36.0);
        float volumedecay = (float)(-0.1 * DSoundEffectVolume);
        float rightbias = (float)((((double)DPosition.x / DefineConstants.GAME_WIDTH) - 0.5) * 2.0);
        float rightshift = (float)(DVelocity.x / (2.0 * DefineConstants.GAME_WIDTH));

        DToneID = game.Resources().DSounds.DSoundMixer.PlayTone(freq, freqdecay, volume, volumedecay, rightbias, rightshift);
    }
}
