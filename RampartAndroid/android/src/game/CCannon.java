package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import game.animations.CPlumeAnimation;
import game.animations.DefineConstants;
import game.players.CPlayer;
import game.sounds.CSounds;
import game.utils.CMathUtil;

/**
 * @brief A cannon used to fire cannonballs in a construction map
 */
public class CCannon extends CCannonballOwner {

	/**
	 * @brief The size of the cannon: (2, 2)
	 */
	public static Vector2 CSize = new Vector2(2, 2);
	/**
	 * @brief The position of the cannon
	 */
	public Vector2 DPosition = new Vector2();
	/**
	 * @brief Not used
	 */
	protected SDirection.EValue DDirection;
	/**
	 * @brief Precomputed values of velocities needed to hit targets
	 */
	private java.util.ArrayList<Float> DInitialVelocities = new java.util.ArrayList<Float>();

	/**
	 * @brief Creates a new cannon at a position and initializes the initial velocities
	 * @param position
	 *            The position of the cannon
	 */
	public CCannon(CGame game, Vector2 position) {
		this.DPosition = new Vector2(position);

		// May want this initialized elsewhere or as a static member
		DInitialVelocities = new ArrayList<Float>(CMathUtil.IntegerSquareRoot(DefineConstants.GAME_WIDTH
				* DefineConstants.GAME_WIDTH + DefineConstants.GAME_HEIGHT * DefineConstants.GAME_HEIGHT) + 1);
		for (int Index = 0; Index < DInitialVelocities.size(); Index++) {
			DInitialVelocities.set(Index, (float) Math.sqrt(DefineConstants.STANDARD_GRAVITY * Index / 2));
		}
	}

	/**
	 * @brief Draws the 2D tile for the cannon
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw2D(CGame game) {
		game.Resources().DTilesets.D2DCastleCannonTileset.Draw2DCannonTile(game,
				game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)));
	}

	/**
	 * @brief Draws the 3D tile for its owner if it has one
	 * @param game
	 *            The game to draw in
	 */
	public final void Draw3D(CGame game) {
		SDirection.EValue direction = SDirection.EValue.dSouth;
		CPlayer Owner = GetPlayerOwner(game);
		if (Owner != null) {
			Vector2 CenterPosition = game.GameState().TerrainMap().ConvertToScreenPosition(
					DPosition.cpy().add(new Vector2(1, 1)));
			direction = (SDirection.EValue) CMathUtil.CalculateDirection(new Vector2(CenterPosition), new Vector2(
					Owner.DCursorPosition));
		}

		game.Resources().DTilesets.D3DCannonTileset.DrawTile(game,
				game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition)), direction.getValue());
	}

	/**
	 * @brief Gets the owner of the cannon based on the floor tile it is located on
	 * @param game
	 *            The game to search for tile
	 * @return Pointer to owner or NULL
	 */
	public final CPlayer GetPlayerOwner(CGame game) {
		CConstructionTile ConstructionTile = game.GameState().ConstructionMap().GetTileAt(new Vector2(DPosition));
		if (ConstructionTile.IsFloor()) {
			return game.GameState().GetPlayerWithColor(ConstructionTile.GetColor());
		} else {
			return null;
		}
	}

    /**
*
* @brief pushes cannon back into owner's ready cannon queue when cannonball dies
*
* @param game the game the cannon is in

*/
    public final void CannonballDestroyed(CGame game) {
        GetPlayerOwner(game).DReadyCannons.add(this);
    }

	/**
	 * @brief Fires a cannonball at the target and adds the plume animation
	 * @param game
	 *            The game to fire in
	 * @param target
	 *            The position of the target
	 */
	public final void FireAt(CGame game, Vector2 target) {
		Vector2 CenterPosition = game.GameState().TerrainMap().ConvertToScreenPosition(
				DPosition.cpy().add(new Vector2(1, 1)));

		Vector2 loc = game.GameState().TerrainMap().ConvertToScreenPosition(new Vector2(DPosition));
		game.GameState().DAnimations.add(new CPlumeAnimation(game, loc, DPosition, CMathUtil.CalculateDirection(
				CenterPosition, target)));
		game.GameState().DCannonballs.add(new CCannonball(game, CenterPosition, target, this, game.GameState().DWind));

        int mapWidth = game.GameState().TerrainMap().Width();
        float LRRatio = (( DPosition.x / (mapWidth - 1)) - 0.5f) * 2.0f;
        float soundEffectVolume = 1.0f;
        game.Resources().DSounds.PlaySoundClip((game.GameState().DRandomNumberGenerator.Random() & 0x1) !=  0 ? CSounds.ESoundClipType.sctCannon1 : CSounds.ESoundClipType.sctCannon0, soundEffectVolume, LRRatio); //play one of two firing sounds
	}

	/**
	 * @brief Does nothing
	 * @param game
	 *            The game to update in
	 */
	public final void Update(CGame game) {
		// DDirection = CalcDirection(targetCoords);
	}

	/**
	 * @brief Calculated direction to face for a target
	 * @param targetCoords
	 *            The target to fire at
	 * @return Direction should be seen facing
	 */
	private SDirection.EValue CalcDirection(Vector2 targetCoords) {
		int x1;
		int y1;
		int x2;
		int y2;

		x1 = (int) DPosition.x;
		y1 = (int) DPosition.y;
		x2 = (int) targetCoords.x;
		y2 = (int) targetCoords.y;

		return CMathUtil.CalculateDirection(x1, y1, x2, y2);
	}

	/**
	 * @brief Calculates the velocities to fire cannonball at
	 * @param targetCoords
	 *            The target to fire at
	 * @return The X/Y/Z of velocities for cannonball
	 */
	private Vector3 CalcFiringSolution(Vector2 targetCoords) {
		int CenterX;
		int CenterY;
		int DeltaX;
		int DeltaY;
		int TargetX;
		int TargetY;
		int Distance;

		Vector3 firingSolution = new Vector3();

		CenterX = ((int) DPosition.x + 1); // * DTileWidth; //not sure the purpose of these parts
		CenterY = ((int) DPosition.x + 1); // * DTileHeight;

		TargetX = (int) targetCoords.x;
		TargetY = (int) targetCoords.y; // slightly different from original, may need to cross check

		DeltaX = TargetX - CenterX;
		DeltaY = TargetY - CenterY;
		Distance = CMathUtil.IntegerSquareRoot(DeltaX * DeltaX + DeltaY * DeltaY);

		firingSolution.z = DInitialVelocities.get(Distance) + 0f;
		firingSolution.x = DeltaX * firingSolution.z / Distance;
		firingSolution.y = DeltaY * firingSolution.z / Distance;

		return firingSolution;
	}
}
