package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import game.animations.DefineConstants;
import game.tilesets.CGraphicTileset;
import game.utils.ArrayUtil;
import game.utils.CMathUtil;
import game.utils.Log;

/**
 * CTerrainMap class, used to build game map.
 */
public class CTerrainMap {

	/**
	 * @brief Stores if cache is up to date
	 */
	public boolean DHasCached3D;
	protected CGraphicTileset D2DTileset; // !< 2D tile set.
	protected CGraphicTileset D3DTileset; // !< 3D tile set.
	protected java.util.ArrayList<java.util.ArrayList<Integer>> D2DMap = new java.util.ArrayList<java.util.ArrayList<Integer>>(); // !<
																																	// 2D
																																	// map
	protected java.util.ArrayList<java.util.ArrayList<Integer>> D3DMap = new java.util.ArrayList<java.util.ArrayList<Integer>>(); // !<
																																	// 3D
																																	// map
	protected java.util.ArrayList<java.util.ArrayList<EPlayerColor>> DTileTypeMap = new java.util.ArrayList<java.util.ArrayList<EPlayerColor>>(); // !<
																																					// Tile
																																					// type
																																					// map
	protected ArrayList<String> DStringMap = new ArrayList<String>(); // !< String map
	protected java.util.ArrayList<Castle> DCastles = new java.util.ArrayList<Castle>(); // !< Castles
	protected int DPlayerCount; // !< Player count
	/**
	 * @brief Which animation step the terrain map is at
	 */
	protected int DAnimationStep;
	protected String DMapName; // !< Map name
	protected String DMapFileName; // !< The source filename
	// The new added categories in the new map: saving paths so they can be linked in where needed
	// Give audio manager and AI manager the paths so they can load the files correctly
	protected String D2DTilesetPath; // !< Path to map specific 2D tileset
	protected String D3DTilesetPath; // !< Path to map specific 3D tileset
    protected String DVersus; //!< String for determining map's battle type
	protected String DAIPath; // !< Path for AI scripts
	protected String DSongPath; // !< Path to a song database

	/**
	 * Empty constructor, initialize map to NULL.
	 */
	public CTerrainMap() {
		D2DTileset = null;
		D3DTileset = null;
		DAnimationStep = 0;
		DHasCached3D = false;
	}

	/**
	 * Terrain Map constructor.
	 * 
	 * @param map
	 *            Used to initialized terrain map
	 */
	public CTerrainMap(CTerrainMap map) {
		D2DTileset = map.D2DTileset;
		D3DTileset = map.D3DTileset;
		D2DMap = map.D2DMap;
		D3DMap = map.D3DMap;
		DTileTypeMap = map.DTileTypeMap;
		DStringMap = map.DStringMap;
		DCastles = map.DCastles;
		DPlayerCount = map.DPlayerCount;
		DMapName = map.DMapName;
		DMapFileName = map.DMapFileName;
	}

	/**
	 * Terrain Map destructor.
	 */
	public void dispose() {

	}

	/**
	 * Operator overload =.
	 */
	public final CTerrainMap copyFrom(CTerrainMap map) {
		if (this != map) {
			D2DTileset = map.D2DTileset;
			D3DTileset = map.D3DTileset;
			D2DMap = map.D2DMap;
			D3DMap = map.D3DMap;
			DTileTypeMap = map.DTileTypeMap;
			DStringMap = map.DStringMap;
			DCastles = map.DCastles;
			DPlayerCount = map.DPlayerCount;
			DMapName = map.DMapName;
			DMapFileName = map.DMapFileName;
		}
		return this;
	}

	/**
	 * @brief Resets the castles in this terrain map
	 */
	public final void Reset() {
		for (Castle castle : DCastles) {
			castle.Reset();
		}
		DHasCached3D = false;
	}

	/**
	 * Getter function for DMapName.
	 */
	public final String MapName() {
		return DMapName;
	}

	/**
	 * @brief Get the map in string format
	 * @return
	 */
	public final String MapFileData() {
		StringBuilder mapData = new StringBuilder();
		try {
			FileHandle file = Gdx.files.internal(DMapFileName);
			String fileContents = file.readString();
			BufferedReader br = new BufferedReader(new StringReader(fileContents));
			String pngFilePathToLoad = br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				mapData.append(line);
			}
			br.close();
		} catch (Exception ex) {
			Log.critical(ex, "Failed to load/read file");
		}
		return mapData.toString();
	}

	/**
	 * Getter function for DPlayerCount.
	 */
	public final int PlayerCount() {
		return DPlayerCount;
	}

	/**
	 * @brief Getter function for DCastles
	 * @return DCastles
	 */
	public final java.util.ArrayList<Castle> Castles() {
		return DCastles;
	}

	/**
	 * @brief Getter function for D3DMap
	 * @return D3DMap
	 */
	public final ArrayList<String> StringMap() {
		return DStringMap;
	}

	/**
	 * Loads map
	 * 
	 * @param tileset2d
	 *            2D tile set
	 * @param tileset3d
	 *            3D tile set
	 * @param filename
	 *            Map to load
	 */
	public final boolean LoadMap(CGraphicTileset tileset2d, CGraphicTileset tileset3d, String filename) {
		DMapFileName = filename;
		FileHandle file = Gdx.files.internal(DMapFileName);
		String mapData = file.readString();
		return LoadMapString(tileset2d, tileset3d, mapData);
	}

	/**
	 * @brief Loads map from string
	 * @param tileset2d
	 *            2D tileset
	 * @param tileset3d
	 *            3D tileset
	 * @param data
	 *            String data to load from
	 * @return try if loaded
	 */
	public final boolean LoadMapString(CGraphicTileset tileset2d, CGraphicTileset tileset3d, String data) {
        try
        {
		BufferedReader scanner = new BufferedReader(new StringReader(data));
		ArrayList<String> WaterNames2D = new ArrayList<String>();
		ArrayList<String> WaterNames3D = new ArrayList<String>();
		String TempBuffer = new String(new char[1024]);
		int BufferSize = 1024;
		int LastChar;
		int MapWidth;
		int MapHeight;
		int CastleCount;
		boolean ReturnStatus = false;
		int PlayerColorsFound = 0;

		D2DTileset = tileset2d;
		D3DTileset = tileset3d;
		D2DMap.clear();

		// Getting the Map Title
		DMapName = scanner.readLine();


		/**
		 * Bill: 3 new lines to scan: Tileset, AI Script, and Music File
		 * 
		 * // Getting the Tileset Path
		 */
		D2DTilesetPath = scanner.readLine();
		D2DTilesetPath = D2DTilesetPath.replaceAll("\n", "");

		// Getting the Tileset Path
		D3DTilesetPath = scanner.readLine();
		D3DTilesetPath = D3DTilesetPath.replaceAll("\n", "");

        // Getting versus
        DVersus = scanner.readLine();
        DVersus = DVersus.replaceAll("\n", "");

		// Getting the AI script Path
		DAIPath = scanner.readLine();
		DAIPath = DAIPath.replaceAll("\n", "");

		// Getting the Song Path
		DSongPath = scanner.readLine();
		DSongPath = DSongPath.replaceAll("\n", "");

		// Getting the Dimensions
		String mapDimensions = scanner.readLine();
		String[] mapDimensionsSplit = mapDimensions.split(" ");
		MapWidth = Integer.valueOf(mapDimensionsSplit[0]);
		MapHeight = Integer.valueOf(mapDimensionsSplit[1]);

		// Getting the Map
		while (DStringMap.size() < MapHeight + 2) {
			String line = scanner.readLine();
            if (line == null)
                break;
			line = line.replaceAll("\\r|\\n", "");
			DStringMap.add(line);
			if (MapWidth + 2 > DStringMap.get(DStringMap.size() - 1).length()) {
				Log.critical("Map line %d too short!\n", DStringMap.size());
			}
		}

		if (MapHeight + 2 > DStringMap.size()) {
			Log.critical("Map has too few lines!");
		}
		D2DMap = new ArrayList<ArrayList<Integer>>(MapHeight);
		ArrayUtil.resize(D2DMap, MapHeight);
		D3DMap = new ArrayList<ArrayList<Integer>>(MapHeight);
		ArrayUtil.resize(D3DMap, MapHeight);
		DTileTypeMap = new ArrayList<ArrayList<EPlayerColor>>(MapHeight);
		ArrayUtil.resize(DTileTypeMap, MapHeight);
		for (int Index = 0; Index < D2DMap.size(); Index++) {
			D2DMap.set(Index, ArrayUtil.resize(new ArrayList<Integer>(), MapWidth));
			D3DMap.set(Index, ArrayUtil.resize(new ArrayList<Integer>(), MapWidth));
			DTileTypeMap.set(Index, ArrayUtil.resize(new ArrayList<EPlayerColor>(), MapWidth));
		}
		for (int Index = 0; Index < DStringMap.size(); Index++) {
			for (char c : DStringMap.get(Index).toCharArray()) {
				switch (c) {
				case 'B':
					PlayerColorsFound |= 0x01;
					break;
				case 'R':
					PlayerColorsFound |= 0x02;
					break;
				case 'Y':
					PlayerColorsFound |= 0x04;
					break;
				default:
					break;
				}
			}
		}
		switch (PlayerColorsFound) {
		case 3: // 2 player B & R
			DPlayerCount = 2;
			break;
		case 7: // 3 player B, R, & Y
			DPlayerCount = 3;
			break;
		case 5:
		case 6: // 2 player need shiftdown
			Log.critical("2 player need shiftdown not implemented yet");
			// for(int Index = 0; Index < DStringMap.size(); Index++) {
			// for (char c : DStringMap.get(Index).toCharArray()) {
			// switch (c) {
			// case 'R':
			/**
			 * Iter = 'B'; // break; // case 'Y': Iter = 'R'; // break; // default: // break; // } // } // }
			 */
			DPlayerCount = 2;
			break;
		default:
			Log.critical("Map has too few players!\n");
		}

		WaterNames2D = ArrayUtil.resize(new ArrayList<String>(), 16);
		WaterNames2D.set(0, "water");
		WaterNames2D.set(1, "shore-n");
		WaterNames2D.set(2, "shore-e");
		WaterNames2D.set(3, "shore-ne");
		WaterNames2D.set(4, "shore-s");
		WaterNames2D.set(5, "shore-n");
		WaterNames2D.set(6, "shore-se");
		WaterNames2D.set(7, "shore-e");
		WaterNames2D.set(8, "shore-w");
		WaterNames2D.set(9, "shore-nw");
		WaterNames2D.set(10, "shore-e");
		WaterNames2D.set(11, "shore-n");
		WaterNames2D.set(12, "shore-sw");
		WaterNames2D.set(13, "shore-w");
		WaterNames2D.set(14, "shore-s");
		WaterNames2D.set(15, "water");
		WaterNames3D = ArrayUtil.resize(new ArrayList<String>(), 16);
		WaterNames3D.set(0, "water-0");
		WaterNames3D.set(1, "shore-n-0");
		WaterNames3D.set(2, "shore-e-0");
		WaterNames3D.set(3, "shore-ne-0");
		WaterNames3D.set(4, "shore-s-0");
		WaterNames3D.set(5, "shore-n-0");
		WaterNames3D.set(6, "shore-se-0");
		WaterNames3D.set(7, "shore-e-0");
		WaterNames3D.set(8, "shore-w-0");
		WaterNames3D.set(9, "shore-nw-0");
		WaterNames3D.set(10, "shore-e-0");
		WaterNames3D.set(11, "shore-n-0");
		WaterNames3D.set(12, "shore-sw-0");
		WaterNames3D.set(13, "shore-w-0");
		WaterNames3D.set(14, "shore-s-0");
		WaterNames3D.set(15, "water-0");

		for (int YPos = 0; YPos < D2DMap.size(); YPos++) {
			for (int XPos = 0; XPos < D2DMap.get(YPos).size(); XPos++) {
				boolean IsEven = (((YPos + XPos) & 0x01) != 0) ? false : true;

				switch (DStringMap.get(YPos + 1).charAt(XPos + 1)) {
				case 'B':
					DTileTypeMap.get(YPos).set(XPos, EPlayerColor.pcBlue);
					break;
				case 'R':
					DTileTypeMap.get(YPos).set(XPos, EPlayerColor.pcRed);
					break;
				case 'Y':
					DTileTypeMap.get(YPos).set(XPos, EPlayerColor.pcYellow);
					break;
				default:
					DTileTypeMap.get(YPos).set(XPos, EPlayerColor.pcNone);
					break;
				}

				if (DStringMap.get(YPos + 1).charAt(XPos + 1) == ' ') {
					int WaterType = 0;

					if (DStringMap.get(YPos + 1).charAt(XPos) != ' ') {
						WaterType |= 0x8;
					}
					if (DStringMap.get(YPos).charAt(XPos + 1) != ' ') {
						WaterType |= 0x1;
					}
					if (DStringMap.get(YPos + 1).charAt(XPos + 2) != ' ') {
						WaterType |= 0x2;
					}
					if (DStringMap.get(YPos + 2).charAt(XPos + 1) != ' ') {
						WaterType |= 0x4;
					}
					if (WaterType != 0 && (WaterType != 15)) {
						D2DMap.get(YPos).set(XPos,
								D2DTileset.FindTile(WaterNames2D.get(WaterType) + (IsEven ? "-even" : "-odd")));
						D3DMap.get(YPos).set(XPos, D3DTileset.FindTile(WaterNames3D.get(WaterType)));
					} else {
						WaterType = 0;
						if (DStringMap.get(YPos).charAt(XPos) != ' ') {
							WaterType |= 0x8;
						}
						if (DStringMap.get(YPos + 2).charAt(XPos) != ' ') {
							WaterType |= 0x4;
						}
						if (DStringMap.get(YPos).charAt(XPos + 2) != ' ') {
							WaterType |= 0x1;
						}
						if (DStringMap.get(YPos + 2).charAt(XPos + 2) != ' ') {
							WaterType |= 0x2;
						}
						switch (WaterType) {
						case 1:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-ne"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-nec-0"));
							break;
						case 2:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-se"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-sec-0"));
							break;
						case 4:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-sw"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-swc-0"));
							break;
						case 8:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-nw"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-nwc-0"));
							break;
						case 5:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-nesw"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-nesw-0"));
							break;
						case 10:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("shore-nwse"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("shore-nwse-0"));
							break;
						default:
							D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("water"));
							D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("water-0"));
							break;
						}
					}
				} else {
					if (IsEven) {
						D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("grass-even"));
					} else {
						D2DMap.get(YPos).set(XPos, D2DTileset.FindTile("grass-odd"));
					}
					D3DMap.get(YPos).set(XPos, D3DTileset.FindTile("grass-0"));
				}
			}
		}

		CastleCount = Integer.valueOf(scanner.readLine());
        CAIPlayer.DNumberCastleLocations = CastleCount;
		DCastles = new ArrayList<Castle>(CastleCount);
		for (int i = 0; i < CastleCount; i++) {
			DCastles.add(new Castle());
		}
		for (int Index = 0; Index < CastleCount; Index++) {
			String castleLocations = scanner.readLine();
			String[] castleLocationsSplit = castleLocations.split(" ");
			int XPos = Integer.valueOf(castleLocationsSplit[0]);
			int YPos = Integer.valueOf(castleLocationsSplit[1]);

			DCastles.get(Index).IndexPosition(new Vector2(XPos, YPos));
			DCastles.get(Index).DColor = TileType(XPos, YPos);
		}

		SortCastlesForDrawing();

        }
        catch (Exception ex) {
            Log.critical(ex, "Error while calling LoadMapString().");
        }
		return true;
    }

	/**
	 * Gets tile type if x and y indices are within constraints of DTileTypeMap.
	 * 
	 * @param xindex
	 *            xindex
	 * @param yindex
	 *            yindex
	 */
	public final EPlayerColor TileType(int xindex, int yindex) {
		if ((0 > xindex) || (0 > yindex)) {
			return EPlayerColor.pcMax;
		}
		if (DTileTypeMap.size() <= yindex) {
			return EPlayerColor.pcMax;
		}
		if (DTileTypeMap.get(yindex).size() <= xindex) {
			return EPlayerColor.pcMax;
		}
		return DTileTypeMap.get(yindex).get(xindex);
	}

	/**
	 * @brief Sorts castles top to bottom, left to right
	 */
	protected final void SortCastlesForDrawing() {
		for (int Index = 0; Index < DCastles.size(); Index++) {
			for (int Inner = Index + 1; Inner < DCastles.size(); Inner++) {
				boolean Swap = false;
				if (DCastles.get(Index).IndexPosition().y > DCastles.get(Inner).IndexPosition().y) {
					Swap = true;
				} else if ((DCastles.get(Index).IndexPosition().y == DCastles.get(Inner).IndexPosition().y)
						&& (DCastles.get(Index).IndexPosition().x > DCastles.get(Inner).IndexPosition().x)) {
					Swap = true;
				}
				if (Swap) {
					Castle TempCastle = DCastles.get(Index);
					DCastles.set(Index, DCastles.get(Inner));
					DCastles.set(Inner, TempCastle);
				}
			}
		}
	}

	/**
	 * Draws preview of map.
	 * 
	 * @param drawable
	 *            The destination to draw the map
	 * @param gc
	 *            The graphics context to draw the map in
	 * @param xoff
	 *            Offset x
	 * @param yoff
	 *            Offset y
	 */
	public final void DrawPreviewMap(Pixmap drawable, int xoff, int yoff) {
		for (int YIndex = 0, YPos = yoff; YIndex < D2DMap.size(); YIndex++, YPos += 2) {
			for (int XIndex = 0, XPos = xoff; XIndex < D2DMap.get(YIndex).size(); XIndex++, XPos += 2) {
				D2DTileset.DrawPixelCorners(drawable, XPos, YPos, D2DMap.get(YIndex).get(XIndex));
			}
		}
	}

	/**
	 * Draws 2D version of the map
	 * 
	 * @param drawable
	 *            The destination to draw the map
	 * @param gc
	 *            The graphics context to draw the map in
	 */
	public final void Draw2DMap(CGame game) {
		int TileWidth;
		int TileHeight;

		TileWidth = D2DTileset.TileWidth();
		TileHeight = D2DTileset.TileHeight();
		for (int YIndex = 0, YPos = 0; YIndex < D2DMap.size(); YIndex++, YPos += TileHeight) {
			for (int XIndex = 0, XPos = 0; XIndex < D2DMap.get(YIndex).size(); XIndex++, XPos += TileWidth) {
				D2DTileset.DrawTile(game, new Vector2(XPos, YPos), D2DMap.get(YIndex).get(XIndex));
			}
		}

	}

	/**
	 * @brief Draws 3D version of the map
	 * @param game
	 *            Game drawing
	 */
	public final void Draw3D(CGame game) {
		if (!DHasCached3D) {
			Cache3DMap(game);
		}
		// Log.call();
		// Log.warn("This method isn't finished yet.");

		CRendering Rendering = game.Rendering();
		Pixmap pixmap = Rendering.D3DTerrainPixmaps.get((DAnimationStep / 4)
				% DefineConstants.TERRAIN_ANIMATION_TIMESTEPS);
		Rendering.DWorkingBufferPixmap.drawPixmap(pixmap, 0, 0, 0, 0, -1, -1);
		// gdk_draw_pixmap(Rendering.DWorkingBufferPixmap, Rendering.DDrawingContext,
		// Rendering.D3DTerrainPixmaps.get((DAnimationStep / 4) % DefineConstants.TERRAIN_ANIMATION_TIMESTEPS), 0, 0, 0,
		// 0, -1, -1);
	}

	/**
	 * @brief Caches the frames of the 3D version of the map
	 * @param game
	 *            Game drawing
	 */
	public final void Cache3DMap(CGame game) {
		ArrayList<Pixmap> Pixmaps = game.Rendering().D3DTerrainPixmaps;
		Pixmap OriginalPixmap = game.Rendering().DWorkingBufferPixmap;
		for (int Index = 0; Index < game.Rendering().D3DTerrainPixmaps.size(); Index++) {
			game.Rendering().DWorkingBufferPixmap = game.Rendering().D3DTerrainPixmaps.get(Index);
			Draw3DMap(game, game.GameState().DWind.DWindDirection, DefineConstants.TERRAIN_ANIMATION_TIMESTEPS, Index);
		}
		game.Rendering().DWorkingBufferPixmap = OriginalPixmap;
		DHasCached3D = true;
	}

	/**
	 * Draws 3D version of the map
	 * 
	 * @param drawable
	 *            The destination to draw the map
	 * @param gc
	 *            The graphics context to draw the map in
	 * @param winddir
	 *            Wind direction
	 * @param totalsteps
	 *            Total steps
	 * @param timestep
	 *            Time increment
	 */
	public final void Draw3DMap(CGame game, int winddir, int totalsteps, int timestep) {
		int TileWidth;
		int TileHeight;

		TileWidth = D3DTileset.TileWidth();
		TileHeight = D3DTileset.TileHeight();
		for (int YIndex = 0, YPos = 0; YIndex < D3DMap.size(); YIndex++, YPos += TileHeight) {
			for (int XIndex = 0, XPos = 0; XIndex < D3DMap.get(YIndex).size(); XIndex++, XPos += TileWidth) {
				D3DTileset.DrawTile(game, new Vector2(XPos, YPos), D3DMap.get(YIndex).get(XIndex)
						+ (winddir * totalsteps) + timestep);
			}
		}
	}

	/**
	 * @brief Draws the 2D versions of the castles
	 * @param game
	 *            Game drawing
	 */
	public final void Draw2DCastles(CGame game) {
		for (Castle castle : DCastles) {
			castle.Draw2D(game);
		}
	}

	/**
	 * @brief Draws the 3D versions of the castles
	 * @param game
	 *            Game drawing
	 */
	public final void Draw3DCastles(CGame game, int XPos, int YPos) {
		for (Castle castle : DCastles) {
			if (castle.IndexPosition().equals(new Vector2(XPos - 1, YPos - 1))) {
				castle.Draw3D(game);
			}
		}
	}

	/**
	 * @brief Converts a on screen position to a tile position
	 * @param position
	 *            On screen position (pixels)
	 * @return tile position (indices)
	 */
	public final Vector2 ConvertToTileIndex(Vector2 position) {
		return new Vector2((int) (Math.max(0, Math.min(Width() - 1, position.x / D2DTileset.TileWidth()))),
				(int) (Math.max(0, Math.min(Height() - 1, position.y / D2DTileset.TileHeight()))));
	}

	/**
	 * Gets width of map.
	 */
	public final int Width() {
		if (D2DMap.size() != 0) {
			return D2DMap.get(0).size();
		}
		return 0;
	}

	/**
	 * Gets height of map.
	 */
	public final int Height() {
		return D2DMap.size();
	}

	/**
    *
    * Gets AI path of map.

    */
    public final String AIPath() {
        return DAIPath;
    }

	/**
	 * @brief Converts a tile position to a screen position
	 * @param index
	 *            Tile position (indices)
	 * @return On screen position (pixels)
	 */
	public final Vector2 ConvertToScreenPosition(Vector2 index) {
		return new Vector2(index.x * D2DTileset.TileWidth(), index.y * D2DTileset.TileHeight());
	}

	/**
	 * @brief Determines if there are no obstructions in the defined rectangle
	 * @param position
	 *            Top left of rectangle
	 * @param size
	 *            Size of rectangle
	 * @return True if there are no castles overlapping, otherwise false
	 */
	public final boolean IsSpaceOpen(Vector2 position, Vector2 size) {
		for (Castle castle : DCastles) {
			if (CMathUtil.DoRectanglesOverlap(new Vector2(position), new Vector2(size), castle.IndexPosition(),
					castle.CSize)) {
				return false;
			}
		}
		return true;
	}

    public final boolean IsSpaceWater(Vector2 position, Vector2 size) {
        boolean result = true;
        for(int i = 0; i < size.y; i++) {
            for(int j = 0; j < size.x; j++) {
                // TODO: Don't hardcode map dimensions
                if((position.y + i >= 40) || (position.x + j >= 23))
                    return false;
                // TODO: Remove this code addition different from Linux for checking of position is below 0
                if ((int)(position.y + i) < 0 || ((int)position.x + j) < 0)
                    return false;
                result &= D2DMap.get((int)position.y + i).get((int)position.x+j) == D2DTileset.FindTile("water");
            }
        }
        return result;
    }

	/**
	 * @brief Updates the castles and the animation step of terrain map
	 * @param game
	 *            Game to update
	 */
	public final void Update(CGame game) {
		for (Castle castle : DCastles) {
			castle.Update(game);
		}
		DAnimationStep += game.GameState().DWind.DWindSpeed;
	}

	/**
	 * @brief Loads the proper tilestes for drawing the game
	 * @param game
	 *            Game to update
	 */
	public final void LoadMapTileset(CGame game) {
		D2DTileset.LoadTileset(game, D2DTilesetPath);
		D3DTileset.LoadTileset(game, D3DTilesetPath);
	}

    /**
*
* @brief Returns the map's song set path

*/
    public final String SongPath() {
        return DSongPath;
    }

    public final String GetMapType() {
        return DVersus;
    }
}
