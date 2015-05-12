package game.animations;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.sounds.CSounds;
import game.tilesets.C3DTerrainTileset;


/**
 * @brief The animation played when a cannonball explodes
 */
public class CExplosionAnimation extends CAnimation {

	public int tileType;

	/**
	 * @brief Makes a new animation at a position
	 * @param game
	 *            The game playing
	 * @param position
	 *            The position to play at
	 * @param explosionType
	 *            The type of explosion
	 */
	public CExplosionAnimation(CGame game, Vector2 position, Vector2 index,
			SBurnAndExplosion.EExplosionType explosionType) {
		super(new Vector2((float) ((index.x - 0.5) * game.Resources().DTilesets.D3DTerrainTileset.TileWidth()),
				(float) ((index.y - 1.5) * game.Resources().DTilesets.D3DTerrainTileset.TileHeight())), new Vector2(
				index), game.Resources().DTilesets.D3DExplosionTileset.GetBaseFrame(explosionType),
				game.Resources().DTilesets.D3DExplosionTileset.TileCount()
						/ SBurnAndExplosion.EExplosionType.etMax.getValue());
		switch (explosionType) {
		case etGroundExplosion0:
		case etGroundExplosion1:
			tileType = 0;
			break;
		case etWaterExplosion0:
		case etWaterExplosion1:
			tileType = 1;
			break;
		case etWallExplosion0:
		case etWallExplosion1:
			tileType = 2;
			break;
		default:
			break;
		}

    /**
     * Log.info("A new CExplosionAnimation has been made.");
     */
        PlayExplosionClip(game);
	}

	/**
	 * @brief Updates the animation and spawns the appropriate animation if destroyed a wall or hit the ground
	 * @param game
	 *            Game updating
	 */
	public void Update(CGame game) {
		super.Update(game);

		if (!ShouldContinuePlaying()) {
			C3DTerrainTileset TerrainTileset = game.Resources().DTilesets.D3DTerrainTileset;
			if (tileType == 0) {
				game.GameState().DAnimations.add(new CBurnAnimation(
						game,
						new Vector2((DIndex.x) * TerrainTileset.TileWidth(), (DIndex.y) * TerrainTileset.TileHeight()),
						DIndex,
						game.GameState().DRandomNumberGenerator.Random() % 2 != 0 ? SBurnAndExplosion.EBurnType.btHoleBurn0
								: SBurnAndExplosion.EBurnType.btHoleBurn1));
			} else if (tileType == 2) {
				game.GameState().DAnimations.add(new CBurnAnimation(
						game,
						new Vector2((DIndex.x) * TerrainTileset.TileWidth(), (DIndex.y) * TerrainTileset.TileHeight()),
						DIndex,
						game.GameState().DRandomNumberGenerator.Random() % 2 != 0 ? SBurnAndExplosion.EBurnType.btRubbleBurn0
								: SBurnAndExplosion.EBurnType.btRubbleBurn1));
			}
		}
	}
    /**
    *
    * @brief plays the appropriate explosion clip for the impacted object
    * @param game the game being updated
    */
    public final void PlayExplosionClip(CGame game) {
        int randInt = game.GameState().DRandomNumberGenerator.Random();
        int mapWidth = game.GameState().TerrainMap().Width();
        float LRRatio = (( DPosition.x / (mapWidth - 1)) - 0.5f) * 2.0f;
        float soundEffectVolume = 0.25f;

        CSounds.ESoundClipType clipType;

        CSounds.ESoundClipType[] groundExpClips = {CSounds.ESoundClipType.sctGroundExplosion0, CSounds.ESoundClipType.sctGroundExplosion1};

        CSounds.ESoundClipType[] waterExpClips = {CSounds.ESoundClipType.sctWaterExplosion0, CSounds.ESoundClipType.sctWaterExplosion1, CSounds.ESoundClipType.sctWaterExplosion2, CSounds.ESoundClipType.sctWaterExplosion3};

        CSounds.ESoundClipType[] wallExpClips = {CSounds.ESoundClipType.sctExplosion0, CSounds.ESoundClipType.sctExplosion1, CSounds.ESoundClipType.sctExplosion2, CSounds.ESoundClipType.sctExplosion3};

        switch (tileType) {
            case 0:
    /**
     * randomly pick ground clip
     */
                clipType = groundExpClips[randInt % 2];
                break;
            case 1:
    /**
     * randomly pick water clip
     */
                clipType = waterExpClips[randInt % 4];
                break;
            case 2:
    /**
     * randomly pick wall clip
     */
                clipType = wallExpClips[randInt % 4];
                break;
            default:
                return;
        }

    /**
     * Log.info("Finally playing explosion clip.");
     */
        game.Resources().DSounds.PlaySoundClip(clipType, soundEffectVolume, LRRatio);
    }

	/**
	 * @brief Draws the animation using the burn tileset
	 * @param game
	 *            The game drawing
	 */
	public void Draw(CGame game) {
		super.Draw(game, game.Resources().DTilesets.D3DExplosionTileset);
	}
}
