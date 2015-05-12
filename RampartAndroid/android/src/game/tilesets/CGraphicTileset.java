package game.tilesets;

import java.io.BufferedReader;
import java.io.StringReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.utils.Log;

/**
 * CGraphicTileset class, used for tile set operations.
 */
public class CGraphicTileset {

	protected Pixmap DTileset; // !< Pixel Map for tile set.
	protected Pixmap DPixbufTileset; // !< Tile set for client side.
	protected java.util.HashMap<String, Integer> DMapping = new java.util.HashMap<String, Integer>(); // !< Map between
																										// tilename and
																										// index.
	protected int DTileCount; // !< Tile count.
	protected int DTileWidth; // !< Tile width.
	protected int DTileHeight; // !< Tile height.

	public CGraphicTileset() {
		DTileset = null;
		DPixbufTileset = null;
		DTileCount = 0;
		DTileWidth = 0;
		DTileHeight = 0;
	}

	/**
	 * Getter function for tile count.
	 */
	public final int TileCount() {
		return DTileCount;
	}

	/**
	 * Getter function for tile width.
	 */
	public final int TileWidth() {
		return DTileWidth;
	}

	/**
	 * Getter function for tile height.
	 */
	public final int TileHeight() {
		return DTileHeight;
	}

	/**
	 * Gets tile index if tilename is in map.
	 * 
	 * @param tilename
	 *            tile name
	 */
	public final int FindTile(String tilename) {
		Integer value = DMapping.get(tilename);
		if (value == null) {
			Log.warn("Unknown tilename %s", tilename);
			return -1;
		}
		return value;
	}

	/**
	 * Loads Tileset.
	 * 
	 * @param drawable
	 *            A GdkDrawable, used to determine default values for the new pixmap
	 * @param gc
	 *            Graphics context that holds information about how things are drawn
	 * @param filename
	 *            Name of file to load Tileset from
	 */
	public boolean LoadTileset(CGame game, String filename) {
		try {
			FileHandle file = Gdx.files.internal(filename);
			String fileContents = file.readString();
			BufferedReader br = new BufferedReader(new StringReader(fileContents));
			String pngFilePathToLoad = br.readLine();
			if (pngFilePathToLoad == null) {
				Log.critical("Expected something like `pngs/FontKingthingsWhite.png` in .dat file, but was null? Why?");
			} else {
				// We're about to load (e.g. `pngs/FontKingthingsWhite.png\r\n`), so remove the `\r\n`
				pngFilePathToLoad = pngFilePathToLoad.replaceAll("(\\r|\\n)", "");
				DPixbufTileset = new Pixmap(Gdx.files.internal(pngFilePathToLoad));
				DTileWidth = DPixbufTileset.getWidth();
				DTileHeight = DPixbufTileset.getHeight();
				if (DPixbufTileset == null) {
					Log.critical("Loaded PNG from `%s` but was null.", pngFilePathToLoad);
				}
			}
			String tileCountStr = br.readLine().replaceAll(" |\\r|\\n", "");
			DTileCount = Integer.parseInt(tileCountStr);
			DTileHeight /= DTileCount;
			for (int Index = 0; Index < DTileCount; Index++) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				line = line.replaceAll("(\\r|\\n)", "");
				DMapping.put(line, Index);
			}
			br.close();
		} catch (Exception ex) {
			Log.critical(ex, "Failed to load/read file");
		}
		return true;
	}

	/**
	 * @brief Draws tile at position
	 * @param game
	 *            The game to draw in
	 * @param x
	 *            Position to draw
	 * @param y
	 *            Position to draw
	 * @param tileindex
	 *            Tile to draw
	 */
	public void DrawTile(CGame game, int x, int y, int tileIndex) {
		DrawTile(game, new Vector2(x, y), tileIndex);
	}

	/**
	 * Draws Tile if tileindex is between 0 and DTileCount.
	 * 
	 * @param drawable
	 *            destination drawable to draw tile
	 * @param gc
	 *            Graphics context, used for clipping
	 * @param xpos
	 *            Destination X coord inside drawable
	 * @param ypos
	 *            Destination Y coord inside drawable
	 * @param tileindex
	 *            index of tile
	 */
	public void DrawTile(CGame game, Vector2 position, int tileindex) {
		if ((0 > tileindex) || (tileindex >= DTileCount)) {
			return;
		}
		game.Rendering().DWorkingBufferPixmap.drawPixmap(DPixbufTileset, 0, tileindex * DTileHeight, DTileWidth,
				DTileHeight, (int) position.x, (int) position.y, DTileWidth, DTileHeight);
		// game.getSpriteBatch().draw(
		// /* Using the specified tileset */
		// DPixbufTileset,
		// /* Draw to this on-screen X coordinate */
		// position.x,
		// /* Draw to this on-screen Y coordinate */
		// position.y,
		// /* Draw it this wide */
		// DTileWidth, /* Super-bug vaccine */
		// /* And draw it just as tall */
		// DTileHeight, /* Super-bug vaccine */
		// /* X coordinate, in the tileset, of the top-left corner of our tile. */
		// 0,
		// /* Y coordinate, in the tileset, of the top-left corner of our tile. */
		// tileindex * DTileHeight,
		// /* The width of our tile */
		// DTileWidth,
		// /* The height of our tile */
		// DTileHeight,
		// /* Flip on the x-axis. */
		// false, true
		// );
	}

	/**
	 * Draws Tile corners if tileindex is between 0 and DTileCount.
	 * 
	 * @param drawable
	 *            destination drawable to draw tile
	 * @param gc
	 *            Graphics context, used for clipping
	 * @param xpos
	 *            Destination X coord inside drawable
	 * @param ypos
	 *            Destination Y coord inside drawable
	 * @param tileindex
	 *            index of tile
	 */
	public final void DrawPixelCorners(Pixmap drawable, int xpos, int ypos, int tileindex) {
		if ((0 > tileindex) || (tileindex >= DTileCount)) {
			return;
		}

		drawable.drawPixmap(DPixbufTileset, xpos, ypos, 0, tileindex * DTileHeight, 1, 1);
		drawable.drawPixmap(DPixbufTileset, xpos + 1, ypos, DTileWidth - 1, tileindex * DTileHeight, 1, 1);
		drawable.drawPixmap(DPixbufTileset, xpos, ypos + 1, 0, tileindex * DTileHeight + DTileHeight - 1, 1, 1);
		drawable.drawPixmap(DPixbufTileset, xpos + 1, ypos + 1, DTileWidth - 1, tileindex * DTileHeight + DTileHeight
				- 1, 1, 1);
	}
}
