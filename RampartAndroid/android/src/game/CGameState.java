package game;

import java.util.ArrayList;

import game.animations.CAnimation;
import game.players.CPlayer;
import game.utils.CRandomNumberGenerator;

public class CGameState {

	// public final CNetwork Network() {
	// return DNetwork;
	// }
	public String DUsername;
	// public CRoom DRoom = new CRoom();
	public int DTimeStep;
	public CRandomNumberGenerator DRandomNumberGenerator = new CRandomNumberGenerator();
	public ArrayList<CPlayer> DPlayers = new ArrayList<CPlayer>();
	public java.util.ArrayList<CAnimation> DAnimations = new java.util.ArrayList<CAnimation>();
	public java.util.ArrayList<CCannonball> DCannonballs = new java.util.ArrayList<CCannonball>();
	public CWind DWind = new CWind();
	/**
    *
    * @brief True if the network is updating

    */
    public boolean DIsNetworkUpdate;
	public CTimer DTimer = new CTimer();
    // TODO: Linux Config
    //public CConfig DConfig = new CConfig();
	// pointer to Terrain data: field, water, castle locations, etc
	private CTerrainMap DTerrainMap;
	private CConstructionMap DConstructionMap;
	// Stores the AI Difficulty Setting
	private EAIDifficulty DAIDifficulty;

	/**
	 * The GameState object is created in by the Game class before a match has actually started So this constructor just
	 * prepares some default values CGameState::Init() will set up the Game State object for an actual match
	 */
	public CGameState() {
		// We don’t know what map the player is playing on yet
		DTerrainMap = null;
		DConstructionMap = new CConstructionMap();
        DUnits = new CUnits();

		// pthread_attr_t ThreadAttributes = new pthread_attr_t();
		// int StackSize;
		// pthread_attr_init(ThreadAttributes);
		// pthread_attr_getstacksize(ThreadAttributes, StackSize);
		// DNetwork = new CNetwork(StackSize, true);
        DIsNetworkUpdate = false;
		DTimeStep = 0;

        // TODO: Linux Config
//        int game_width = DConfig.GAME_WIDTH;
//        CLuaConfig.LCInstance().LoadIntValue("INITIAL_MAPWIDTH", game_width);
//        int game_height = DConfig.GAME_HEIGHT;
//        CLuaConfig.LCInstance().LoadIntValue("INITIAL_MAP_HEIGHT", game_height);
//
//        float soundeffect_volume = DConfig.SOUNDEFFECT_VOLUME;
//        CLuaConfig.LCInstance().LoadFloatValue("DOSoundEffectVolume", soundeffect_volume);
//        float music_volume = DConfig.MUSIC_VOLUME;
//        CLuaConfig.LCInstance().LoadFloatValue("DMusicVolume", music_volume);


		this.Reset();
	}

	// ~CGameState();
	public final void Reset() {
		DPlayers.clear();
		if (DTerrainMap != null) {
			DTerrainMap.Reset();
			DConstructionMap.ResetForMap(DTerrainMap);
		}
        if(DUnits != null) {
            DUnits.Reset();
        }

		DAIDifficulty = EAIDifficulty.aiEasy;
	}

	// Wind functions
	// void SetWindType(EWindType); //Sets the WindType
	// EWindType GetWindType(); //Gets the WindType
	// void SetWindSpeed(int);
	// int GetWindSpeed();
	// void SetWindDirection(int);
	// int GetWindDirection():

	public final CTerrainMap TerrainMap() {
		return DTerrainMap;
	}

	// EAIDifficulty getAIDifficulty(): //Gets the AI Difficulty

	public final void TerrainMap(CTerrainMap terrain_map) {
		DTerrainMap = terrain_map;
		DTerrainMap.DHasCached3D = false;
		DConstructionMap.ResetForMap(DTerrainMap);
        DUnits.Reset();
	}

	public final CConstructionMap ConstructionMap() {
		return DConstructionMap;
	}

    public final CUnits Units() {
        return DUnits;
    }

	/** AI Settings */
	public final void SetAIDifficulty(EAIDifficulty difficulty) { // Set AI Difficulty
		DAIDifficulty = difficulty;
	}

    /** Gets the AI Difficulty */
    public final EAIDifficulty getAIDifficulty() {
        return DAIDifficulty;
    }

	public final EPlayerColor GetMainPlayerColor() {
		EPlayerColor MainColor = EPlayerColor.pcNone;
		for (CPlayer player : DPlayers) {
			if (player.DIsLocalPlayer && !(player.DIsAI)) {
				MainColor = player.DColor;
			}
		}
		return MainColor;
	}

	public final CPlayer GetPlayerWithColor(EPlayerColor color) {
		for (CPlayer player : DPlayers) {
			if (player.DColor == color) {
				return player;
			}
		}
		return null;
	}


    public final void SetWind(CWind.EWindType type) {
        DWind.DWindType = type;
    }

	// private CNetwork DNetwork;

	/**
	 * pointer to animation object we may want to make this a vector later on // // CAnimations* DAnimations;
	 */
	public final ArrayList<CPlayer> GetPlayersWithOwnedCastles(CGame game) {
		ArrayList<CPlayer> PlayersWithOwnedCastles = new ArrayList<CPlayer>();
		for (CPlayer player : DPlayers) {
			if (player.OwnedCastleCount(game) > 0) {
				PlayersWithOwnedCastles.add(player);
			}
		}
		return PlayersWithOwnedCastles;
	}

    private CUnits DUnits;
}
