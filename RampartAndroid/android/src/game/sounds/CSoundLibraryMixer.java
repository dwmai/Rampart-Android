package game.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import game.utils.ArrayUtil;
import game.utils.Log;

/**
 * Sound library mixer class.
 */
public class CSoundLibraryMixer {

    protected java.util.ArrayList<Sound> DSoundClips = new java.util.ArrayList<Sound>(); // !< Sound clips in library
    protected ArrayList< Float > DSoundClipVolumes = new ArrayList< Float >(); //!< Volumes for each individual sound clip
    protected java.util.HashMap<String, Integer> DMapping = new java.util.HashMap<String, Integer>(); // !< Map of sound
    // clips by name
    protected java.util.LinkedList<Sound> DClipsInProgress = new java.util.LinkedList<Sound>(); // !< Clips currently
    // being played
    protected java.util.LinkedList<Sound> DTonesInProgress = new java.util.LinkedList<Sound>(); // !< Tones currently
    // being played
    protected java.util.LinkedList<Long> DFreeClipIDs = new java.util.LinkedList<Long>(); // !< Free clip IDs
    protected java.util.LinkedList<Long> DFreeToneIDs = new java.util.LinkedList<Long>(); // !< Free tone IDs
    protected java.util.ArrayList<String> DMusicFilenames = new java.util.ArrayList<String>(); // !< Music file names in
    // library
    protected ArrayList< Float > DMusicVolumes = new ArrayList< Float >(); //!< Volumes for each individual music file
    protected java.util.HashMap<String, Integer> DMusicMapping = new java.util.HashMap<String, Integer>(); // !< Map of
    // music by
    // name
    protected float[] DSineWave; // !< Sine wave
    protected long DNextClipID; // !< Next clip id to give as identification if none free
    protected long DNextToneID; // !< Next tone id to give as identification if none free

    protected HashMap<Integer, String> DSoundSetMapping = new HashMap<Integer, String>();

    // We must map to match this file as specified by the tools team
    // *
    // * [Map Title:String]
    // *
    // * [placeMIDI:String]    place
    // * [rebuildMIDI:String]  rebuild
    // * [tapsMIDI:String]     loss
    // * [winMIDI:String]      win
    // *
    // * [aimWAV:String]              aim
    // * [cannon0WAV:String]          cannon0
    // * [cannon1WAV:String]          cannon1
    // * [ceasefireWAV:String]        ceasefire
    // * [explosion0WAV:String]       explosion0
    // * [explosion1WAV:String]       explosion1
    // * [explosion2WAV:String]       explosion2
    // * [explosion3WAV:String]       explosion3
    // * [fireWAV:String]              fire
    // * [groundexplosion0WAV:String]  groundexplosion0
    // * [groundexplosion1WAV:String]  groundexplosion1
    // * [gustWAV:String]              transition
    // * [placeWAV:String]             place
    // * [readyWAV:String]             ready
    // * [tickWAV:String]              tick
    // * [tockWAV:String]              tock
    // * [triumphWAV:String]          triumph
    // * [waterexplosion0WAV:String]  waterexplosion0
    // * [waterexplosion1WAV:String]  waterexplosion1
    // * [waterexplosion2WAV:String]  waterexplosion2
    // * [waterexplosion3WAV:String]  waterexplosion3
    // *
    //
    protected final void SetSoundSetMapping() {
        DSoundSetMapping.put(3, "place");
        DSoundSetMapping.put(4, "rebuild");
        DSoundSetMapping.put(5, "loss");
        DSoundSetMapping.put(6, "win");

        DSoundSetMapping.put(8, "aim");
        DSoundSetMapping.put(9, "cannon0");
        DSoundSetMapping.put(10, "cannon1");
        DSoundSetMapping.put(11, "ceasefire");
        DSoundSetMapping.put(12, "explosion0");
        DSoundSetMapping.put(13, "explosion1");
        DSoundSetMapping.put(14, "explosion2");
        DSoundSetMapping.put(15, "explosion3");
        DSoundSetMapping.put(16, "fire");
        DSoundSetMapping.put(17, "groundexplosion0");
        DSoundSetMapping.put(18, "groundexplosion1");
        DSoundSetMapping.put(19, "transition");
        DSoundSetMapping.put(20, "place");
        DSoundSetMapping.put(21, "ready");
        DSoundSetMapping.put(22, "tick");
        DSoundSetMapping.put(23, "tock");
        DSoundSetMapping.put(24, "triumph");
        DSoundSetMapping.put(25, "waterexplosion0");
        DSoundSetMapping.put(26, "waterexplosion1");
        DSoundSetMapping.put(27, "waterexplosion2");
        DSoundSetMapping.put(28, "waterexplosion3");

    }

    /**
     * Plays song.
     *
     * @param index
     * Index of song to play
     * @param volume
     * Volume to play song at
     */
    private Music music = null;

    /**
     * Blank constructor.
     */
    public CSoundLibraryMixer() {
        DSineWave = null;
        DNextClipID = 0;
        DNextToneID = 0;
        SetSoundSetMapping();
        // Set up 128 clip ids
        for (long Index = 0; Index < 128; Index++) {
            DFreeClipIDs.addLast(Index);
        }
    }

    /**
     * Destructor.
     */
    public void dispose() {
        if (null != DSineWave) {
            DSineWave = null;
        }
    }

    /**
     * Getter function for number of sound clips.
     */
    public final int ClipCount() {
        return DSoundClips.size();
    }

    /**
     * Finds clip.
     *
     * @param clipname Clip name to find.
     * @return id of clip if found otherwise -1
     */
    public final int FindClip(String clipname) {
        Integer clipId = DMapping.get(clipname);
        if (clipId == null)
            return -1;
        return clipId;
    }

    /**
     * Finds song.
     *
     * @param songname Song name to find.
     * @return id of song being searched if found otherwise -1
     */
    public final int FindSong(String songname) {
        Integer clipId = DMusicMapping.get(songname);
        if (clipId == null)
            return -1;
        return clipId;
    }

    /**
     * Loads music library.
     *
     * @param filename Name of file to open
     * @return True if file was opened and scanned successfully, false otherwise.
     */
    public final boolean LoadLibrary(String filename) {
        String TempBuffer = null;
        int BufferSize = 0;
        int LastChar;
        int TotalClips;
        int TotalSongs;
        FileHandle FilePointer;
        boolean ReturnStatus = false;
        String SoundFontName;

        // Load library file
        FilePointer = Gdx.files.internal(filename);
        String soundData = FilePointer.readString();
        BufferedReader scanner = new BufferedReader(new StringReader(soundData));
        try {
            if (null == FilePointer) {
                return false;
            }
            // Get number of clips
            TotalClips = Integer.parseInt(scanner.readLine());
            DSoundClips = ArrayUtil.resize(new ArrayList<Sound>(TotalClips), TotalClips);
            DSoundClipVolumes = ArrayUtil.resize(new ArrayList<Float>(TotalClips), TotalClips);
            // For each clip index
            for (int Index = 0; Index < TotalClips; Index++) {
                String soundClipDesc = scanner.readLine().replaceAll(" |\\r|\\n", "");
                String filePath = scanner.readLine().replaceAll(" |\\r|\\n", "");
                // Set mapping to index for name
                DMapping.put(soundClipDesc, Index);
                // Load the clip
                DSoundClips.set(Index, Gdx.audio.newSound(Gdx.files.internal(filePath)));
                DSoundClipVolumes.set(Index, 1.0f);
            }

            // TODO: Figure out sine wave tone generation

            String soundFontFilePath = scanner.readLine().replaceAll(" |\\r|\\n", "");
            // TODO: Load sound font file
            Log.warn("TODO: Load sound font '%s'", soundFontFilePath);
            SoundFontName = TempBuffer;
            TotalSongs = Integer.parseInt(scanner.readLine());
            DMusicFilenames = ArrayUtil.resize(new ArrayList<String>(TotalSongs), TotalSongs);
            DMusicVolumes = ArrayUtil.resize(new ArrayList<Float>(TotalSongs), TotalSongs);
            // For each song index
            for (int Index = 0; Index < TotalSongs; Index++) {
                String musicFileDesc = scanner.readLine();
                // Set mapping
                DMusicMapping.put(musicFileDesc, Index);
                String musicFileName = scanner.readLine();
                DMusicFilenames.set(Index, musicFileName);
                DMusicVolumes.set(Index, 1.0f);
            }
        }
        catch (Exception ex) {
            Log.critical(ex, "Error while calling LoadLibrary().");
        }
        return true;
    }

    /**
     * Plays clip.
     *
     * @param index     Index of clip to play
     * @param volume    Volume to set clip volume to
     * @param rightbias Right bias to set clip right bias to
     */
    public final int PlayClip(int index, float volume, float rightbias) {

        if ((0 > index) || (DSoundClips.size() <= index)) {
            return -1;
        }

        Sound sound = DSoundClips.get(index);
        if (sound == null)
            return -1;
        float soundClipRelativeVolume = DSoundClipVolumes.get(index);
        sound.play(volume * soundClipRelativeVolume, 1.0f, rightbias);
        return 1;
    }

    /**
     * Plays tone.
     *
     * @param freq        Current frequency of tone
     * @param freqdecay   Decay of frequency
     * @param volume      Volume to set tone volume to
     * @param volumedecay Decay of tone volume
     * @param rightbias   Right bias to set tone right bias to
     * @param rightshift  Right shift
     */
    public final int PlayTone(float freq, float freqdecay, float volume, float volumedecay, float rightbias,
                              float rightshift) {
        Log.warn("PlayTone was called, but is not yet implemented.");
        return -1;
    }

    /**
     * Stop tone.
     *
     * @param id ID of tone to stop.
     */
    public final void StopTone(int id) {
        Log.warn("StopTone was called, but is not yet implemented.");
    }

    /**
     * Checks if clip is completed.
     *
     * @param id ID of clip to check status of
     * @return True if clip was completed, false otherwise.
     */
    public final boolean ClipCompleted(int id) {
        Log.warn("ClipCompleted was called, but is not yet implemented.");
        return true;
    }

    public final void PlaySong(int index, float volume) {
        if ((0 > index) || (index >= DMusicFilenames.size())) {
            return;
        }
        String filename = DMusicFilenames.get(index);
        music = Gdx.audio.newMusic(Gdx.files.internal(filename));
        music.setLooping(true);
        float relativeVolume = DMusicVolumes.get(index);
        music.setVolume(volume * relativeVolume);
        music.play();
    }

    /**
     * Stops song.
     */
    public final void StopSong() {
        if (music != null)
        music.stop();
    }

    /**
     * Sets song volume.
     *
     * @param volume Volume to set song volume to
     */
    public final void SongVolume(float volume) {
        music.setVolume(volume);
    }

    //        *
//         * Loads the songs from a map's sound set file
//         *
//         * @param filename the path to the sound set file
//
    public final void LoadMapLibrary(String filename) {
        String TempBuffer = null;
        float Vol = 1.0f;
        int BufferSize = 0;
        int LastChar;
        int LibraryIndex;


        // Load library file
        FileHandle FilePointer = Gdx.files.internal(filename);
        String fileContents = FilePointer.readString();
        BufferedReader scanner = new BufferedReader(new StringReader(fileContents));
        try {
            if (null == FilePointer) {
                return;
            }

            for (int Index = 1; Index <= 28; Index++) {
                LibraryIndex = -1;

                String line = scanner.readLine();

                if (3 <= Index && 6 >= Index) {
                    line = line.replaceAll("\\r|\\n", "");
                    //If the line contains a valid path to a midi file Load It
                    // TODO: fluid_is_midifile() does not exist in Android. Determine if the file is a midi file.
                    if (line.endsWith(".mid") || line.endsWith(".midi")) {
                        LibraryIndex = FindSong(DSoundSetMapping.get(Index));
                        DMusicFilenames.set(LibraryIndex, line);
                    }
                }

                //Lines 8 - 28 are Sound files
                if (8 <= Index && 28 >= Index) {
                    line = line.replaceAll("\\r|\\n", "");
                    LibraryIndex = FindClip(DSoundSetMapping.get(Index));
                    // Load the clip
                    if (line != "") {
                        DSoundClips.set(LibraryIndex, Gdx.audio.newSound(Gdx.files.internal(line)));
                    } else {
                        //DSoundClips.set(LibraryIndex, null);
                    }

                }
            }
            //Skip line 39
            scanner.readLine();

            String floatStr = scanner.readLine();
            String[] floats = floatStr.split(" ");

            //Reading line 30 to get volume
            // Load the First Music Volume
            //first token must be  passed the original string
            LibraryIndex = FindSong(DSoundSetMapping.get(3));
            Vol = Float.parseFloat(floats[0]);
            DMusicVolumes.set(LibraryIndex, Vol);

            //Load the rest of the music files
            for (int i = 4; i <= 6; i++) {
                LibraryIndex = FindSong(DSoundSetMapping.get(i));
                DMusicVolumes.set(LibraryIndex, Vol);
            }
            //Load the sound clips
            for (int i = 8; i <= 28; i++) {
                LibraryIndex = FindClip(DSoundSetMapping.get(i));
                DSoundClipVolumes.set(LibraryIndex, Vol);
            }
        }
        catch (Exception ex) {
            Log.critical(ex, "Error while calling LoadMapLibrary().");
        }
    }
}
