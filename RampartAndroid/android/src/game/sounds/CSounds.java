package game.sounds;

import game.CGame;

public class CSounds {

	/**
	 * @brief Filename of the library to use
	 */
	protected String DLibraryName;
	/**
	 * @brief Sounds mixer that plays the clips and songs
	 */
	public CSoundLibraryMixer DSoundMixer = new CSoundLibraryMixer();
	/**
	 * @brief Sound clip indexes used by DSoundMixer to play clips
	 */
	protected int[] DSoundClipIndices = new int[ESoundClipType.sctMax.getValue()];
	/**
	 * @brief Song indexes used by DSoundMixer to play clips
	 */
	protected int[] DSongIndices = new int[ESongType.stMax.getValue()];        /*
	*
     * @brief Keeps track of currently playing song
     */
    ESongType DCurrentSong;
    /**
*
* @brief Pointer to music volume setting

*/
    protected float DMusicVolume;
    /**
*
* @brief Pointer to sound effects volume setting

*/
    protected float DEffectsVolume;



	public CSounds() {
        DCurrentSong = ESongType.stNotPlaying;
		DLibraryName = "data/SoundClips.dat";
	}

	/**
	 * @brief Loads sound resources
	 * @param game
	 *            Game to load for
	 */
	public final void Load(CGame game, float musicVolume, float sfxVolume) {
		if (!DSoundMixer.LoadLibrary(DLibraryName)) {
			System.out.print("Failed to load sound clips.\n");
			System.exit(1);
		}

        DMusicVolume = musicVolume;
        DEffectsVolume = sfxVolume;

		LoadSoundClipIndices();
		LoadSongIndices();
	}

	/**
	 * The following initializes the sound indices. This allows GTK to find the right sound to play.
	 */
	protected final void LoadSoundClipIndices() {

		DSoundClipIndices[ESoundClipType.sctTick.getValue()] = DSoundMixer.FindClip("tick");
		DSoundClipIndices[ESoundClipType.sctTock.getValue()] = DSoundMixer.FindClip("tock");
		DSoundClipIndices[ESoundClipType.sctCannon0.getValue()] = DSoundMixer.FindClip("cannon0");
		DSoundClipIndices[ESoundClipType.sctCannon1.getValue()] = DSoundMixer.FindClip("cannon1");
		DSoundClipIndices[ESoundClipType.sctPlace.getValue()] = DSoundMixer.FindClip("place");
		DSoundClipIndices[ESoundClipType.sctTriumph.getValue()] = DSoundMixer.FindClip("triumph");
		DSoundClipIndices[ESoundClipType.sctExplosion0.getValue()] = DSoundMixer.FindClip("explosion0");
		DSoundClipIndices[ESoundClipType.sctExplosion1.getValue()] = DSoundMixer.FindClip("explosion1");
		DSoundClipIndices[ESoundClipType.sctExplosion2.getValue()] = DSoundMixer.FindClip("explosion2");
		DSoundClipIndices[ESoundClipType.sctExplosion3.getValue()] = DSoundMixer.FindClip("explosion3");
		DSoundClipIndices[ESoundClipType.sctGroundExplosion0.getValue()] = DSoundMixer.FindClip("groundexplosion0");
		DSoundClipIndices[ESoundClipType.sctGroundExplosion1.getValue()] = DSoundMixer.FindClip("groundexplosion1");
		DSoundClipIndices[ESoundClipType.sctWaterExplosion0.getValue()] = DSoundMixer.FindClip("waterexplosion0");
		DSoundClipIndices[ESoundClipType.sctWaterExplosion1.getValue()] = DSoundMixer.FindClip("waterexplosion1");
		DSoundClipIndices[ESoundClipType.sctWaterExplosion2.getValue()] = DSoundMixer.FindClip("waterexplosion2");
		DSoundClipIndices[ESoundClipType.sctWaterExplosion3.getValue()] = DSoundMixer.FindClip("waterexplosion3");
		DSoundClipIndices[ESoundClipType.sctReady.getValue()] = DSoundMixer.FindClip("ready");
		DSoundClipIndices[ESoundClipType.sctAim.getValue()] = DSoundMixer.FindClip("aim");
		DSoundClipIndices[ESoundClipType.sctFire.getValue()] = DSoundMixer.FindClip("fire");
		DSoundClipIndices[ESoundClipType.sctCeasefire.getValue()] = DSoundMixer.FindClip("ceasefire");
		DSoundClipIndices[ESoundClipType.sctTransition.getValue()] = DSoundMixer.FindClip("transition");

	}

	/**
	 * The following initializes the song indices. This allows GTK to find the right sound to play.
	 */
	protected final void LoadSongIndices() {

		DSongIndices[ESongType.stLoss.getValue()] = DSoundMixer.FindSong("loss");
		DSongIndices[ESongType.stWin.getValue()] = DSoundMixer.FindSong("win");
		DSongIndices[ESongType.stMenu.getValue()] = DSoundMixer.FindSong("menu");
		DSongIndices[ESongType.stRebuild.getValue()] = DSoundMixer.FindSong("rebuild");
		DSongIndices[ESongType.stPlace.getValue()] = DSoundMixer.FindSong("place");

	}

	/**
	 * @brief Plays a sound clip
	 * @param sound_clip
	 *            The clip to play
	 * @param volume
	 *            The volume to play at
	 * @param right_bias
	 *            The stereo effect to use
	 */
	public final void PlaySoundClip(ESoundClipType sound_clip, float volume, float right_bias) {
		DSoundMixer.PlayClip(DSoundClipIndices[sound_clip.getValue()], volume * (DEffectsVolume / 10), right_bias);
	}

	public final void PlaySoundClip(ESoundClipType sound_clip) {
		PlaySoundClip(sound_clip, 1, 0);
	}

    /**
*
* @brief Stops current song

*/
    public final void StopSong() {
        DSoundMixer.StopSong();
    }

    /**
*
* @brief Plays song if is different than the one already playing
* @param song The song to play
* @param volume The volume to play the song at

*/
    public final void SwitchSong(ESongType song) {
        SwitchSong(song, 1);
    }
    public final void SwitchSong(ESongType song, float volume) {
        if(song != DCurrentSong) {
            StopSong();
            PlaySong(song, volume * (DMusicVolume / 10));
            DCurrentSong = song;
        }
    }

	/**
	 * @brief Plays a song
	 * @param song
	 *            The song to play
	 * @param volume
	 *            The volume to play the song at
	 */

	public final void PlaySong(ESongType song, float volume) {
        if(song == ESongType.stNotPlaying) { /** signifies that should stop playing current song */
            StopSong();
            return;
        }
		DSoundMixer.PlaySong(DSongIndices[song.getValue()], volume * (DMusicVolume));
	}

    public final CSoundLibraryMixer SoundLibraryMixer() {
        return DSoundMixer;
    }

	/**
	 * @brief Finds songs in DSoundMixer
	 * 
	 * @brief Clips that can be played
	 */
	public enum ESoundClipType {
		sctTick(0),
		sctTock(1),
		sctCannon0(2),
		sctCannon1(3),
		sctPlace(4),
		sctTriumph(5),
		sctExplosion0(6),
		sctExplosion1(7),
		sctExplosion2(8),
		sctExplosion3(9),
		sctGroundExplosion0(10),
		sctGroundExplosion1(11),
		sctWaterExplosion0(12),
		sctWaterExplosion1(13),
		sctWaterExplosion2(14),
		sctWaterExplosion3(15),
		sctReady(16),
		sctAim(17),
		sctFire(18),
		sctCeasefire(19),
		sctTransition(20),
		sctMax(21);
		private static java.util.HashMap<Integer, ESoundClipType> mappings;
		private int intValue;

		private ESoundClipType(int value) {
			intValue = value;
			ESoundClipType.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, ESoundClipType> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, ESoundClipType>();
			}
			return mappings;
		}

		public static ESoundClipType forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}

	/**
	 * @brief Finds sound clips in DSoundMixer
	 * 
	 * @brief Songs that can be played
	 */
	public enum ESongType {
        stNotPlaying,
		stMenu,
		stLoss,
		stWin,
		stPlace,
		stRebuild,
		stMax;

		public static ESongType forValue(int value) {
			return values()[value];
		}

		public int getValue() {
			return this.ordinal();
		}
	}
}
