package game.tilesets;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.utils.ArrayUtil;
import game.utils.Log;

/**
 * CFontTileset class, used to write text.
 */
public class CFontTileset extends CGraphicTileset {

	protected java.util.ArrayList<Integer> DCharacterWidths = new java.util.ArrayList<Integer>(); // !< vector of
																									// character widths.
	protected java.util.ArrayList<java.util.ArrayList<Integer>> DDeltaWidths = new java.util.ArrayList<java.util.ArrayList<Integer>>(); // !<
																																		// vector
																																		// or
																																		// vectors
																																		// of
																																		// delta
																																		// widths.

	public CFontTileset() {

	}

	/**
	 * Font Tileset destructor.
	 */
	public void dispose() {

	}

	/**
	 * Loads Font and Tileset.
	 * 
	 * @param drawable
	 *            A GdkDrawable, used to determine default values for the new pixmap
	 * @param gc
	 *            Graphics context that holds information about how things are drawn
	 * @param filename
	 *            Name of file to load Tileset from
	 */
	public final boolean LoadFont(CGame game, String filename) {
		if (!LoadTileset(game, filename)) {
			return false;
		}

		try {
			FileHandle file = Gdx.files.internal(filename);
			String fileContents = file.readString();
			BufferedReader scanner = new BufferedReader(new StringReader(fileContents));
			scanner.readLine(); // skip
			scanner.readLine(); // skip
			for (int Index = 0; Index < DTileCount; Index++) {
				scanner.readLine(); // skip
			}

			DCharacterWidths = null;
			DCharacterWidths = ArrayUtil.resize(DCharacterWidths, DTileCount);
			DDeltaWidths = new ArrayList<ArrayList<Integer>>(DTileCount);
			ArrayUtil.resize(DDeltaWidths, DTileCount);

			for (int Index = 0; Index < DTileCount; Index++) {
				int CurWidth;
				String widthStr = scanner.readLine();
				DCharacterWidths.set(Index, Integer.valueOf(widthStr));
			}

			for (int FromIndex = 0; FromIndex < DTileCount; FromIndex++) {
				DDeltaWidths.set(FromIndex, ArrayUtil.resize(DDeltaWidths.get(FromIndex), DTileCount));
                String widthLine = scanner.readLine();
                if (widthLine == null)
                    break;
                String[] widths = widthLine.split(" ");

				for (int ToIndex = 0; ToIndex < DTileCount; ToIndex++) {
					DDeltaWidths.get(FromIndex).set(ToIndex, Integer.parseInt(widths[ToIndex]));
				}
			}
			scanner.close();
		} catch (Exception ex) {
			Log.critical(ex, "Failed to load/read file");
		}

		return true;
	}

	/**
	 * Draws string by callign DrawTile where each letter is a tile.
	 * 
	 * @param drawable
	 *            destination drawable to draw tile
	 * @param gc
	 *            Graphics context, used for clipping
	 * @param xpos
	 *            Destination X coord inside drawable
	 * @param ypos
	 *            Destination Y coord inside drawable
	 * @param str
	 *            string to be drawn
	 */
	public final void DrawText(CGame game, Vector2 position, String str) {
		int LastChar = 0;
		int NextChar = 0;
		for (int Index = 0; Index < str.length(); Index++) {
			NextChar = str.charAt(Index) - ' ';

			if (Index != 0) {
				position.x += DCharacterWidths.get(LastChar) + DDeltaWidths.get(LastChar).get(NextChar);
			}
			DrawTile(game, position, NextChar);
			LastChar = NextChar;
		}
	}

	/**
	 * Finds the length and width of str if it were drawn.
	 * 
	 * @param str
	 *            string to be measured
	 * @param width
	 *            the gint that will be altered to match the width of the text
	 * @param height
	 *            the gint that will be altered to match the height of the text
	 */
	public final Vector2 MeasureText(String str) {
		int LastChar = 0;
		int NextChar = 0;
		int width = 0;
		for (int Index = 0; Index < str.length(); Index++) {
			NextChar = str.charAt(Index) - ' ';
			if (Index != 0) {
				width += DDeltaWidths.get(LastChar).get(NextChar);
			}
			width += DCharacterWidths.get(NextChar);
			LastChar = NextChar;
		}
		return new Vector2(width, DTileHeight);
	}
}
