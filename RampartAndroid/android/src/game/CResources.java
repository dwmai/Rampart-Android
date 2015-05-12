package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import game.sounds.CSounds;
import game.tilesets.CTilesets;
import game.utils.Log;

/**
 * @brief Resources used by the game
 */
public class CResources {

	/**
	 * @brief Resource for playing clips and songs
	 */
	public CSounds DSounds;
	/**
	 * @brief The tilesets in the game
	 */
	public CTilesets DTilesets;
	/**
	 * @brief The available terrain maps
	 */
	public java.util.ArrayList<CTerrainMap> DTerrainMaps = new java.util.ArrayList<CTerrainMap>();

	/**
	 * @brief Resource for playing clips and songs // // public CSounds DSounds;
	 */
	public CResources() {
		DSounds = new CSounds();
		DTilesets = new CTilesets();
	}

	public void dispose() {
		if (DTilesets != null)
			DTilesets.dispose();
	}

	/**
	 * @brief Load calls the functions for each sub resource to initialize
	 */
	public final void Load(CGame game, float musicVolume, float sfxVolume) {
		DSounds.Load(game, musicVolume, sfxVolume);
		DTilesets.Load(game);
		LoadTerrainMaps(game);
	}

	/**
	 * @brief Loads the available terrain maps
	 * @param game
	 *            Game playing
	 * @return 0
	 */
	public final boolean LoadTerrainMaps(CGame game) {
		FileHandle[] files = Gdx.files.internal("maps/").list();
		for (FileHandle file : files) {
			if (file.extension().endsWith("map")) {
				CTerrainMap TempMap = new CTerrainMap();

				if (!TempMap.LoadMap((game.Resources().DTilesets.D2DTerrainTileset),
						(game.Resources().DTilesets.D3DTerrainTileset), file.path())) {
					Log.warn("Failed to load map '%s'", file.path());
					continue;
				}
				DTerrainMaps.add(TempMap);
			}
		}
		return true;
	}
}
