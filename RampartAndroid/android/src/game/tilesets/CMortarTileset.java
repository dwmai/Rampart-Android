package game.tilesets;

import com.badlogic.gdx.math.Vector2;

import game.CGame;

/**
 * @brief The mortar tileset
 */
public class CMortarTileset extends CGraphicTileset {

	/**
	 * @brief Stores indices of tiles
	 */
	private int[] DMortarIndices = new int[EBorderMotarType.bmtMax.getValue()];

	/**
	 * @brief Loads the tileset
	 * @param game
	 *            Game loading
	 * @param filename
	 *            Filename loading from
	 * @return True
	 */
	public final boolean LoadTileset(CGame game, String filename) {
		super.LoadTileset(game, filename);

		DMortarIndices[EBorderMotarType.bmtTopCenter.getValue()] = this.FindTile("mortar-tc");
		DMortarIndices[EBorderMotarType.bmtTopRight0.getValue()] = this.FindTile("mortar-tr0");
		DMortarIndices[EBorderMotarType.bmtTopRight1.getValue()] = this.FindTile("mortar-tr1");
		DMortarIndices[EBorderMotarType.bmtTopRight2.getValue()] = this.FindTile("mortar-tr2");
		DMortarIndices[EBorderMotarType.bmtTopLeft0.getValue()] = this.FindTile("mortar-tl0");
		DMortarIndices[EBorderMotarType.bmtTopLeft1.getValue()] = this.FindTile("mortar-tl1");
		DMortarIndices[EBorderMotarType.bmtTopLeft2.getValue()] = this.FindTile("mortar-tl2");
		DMortarIndices[EBorderMotarType.bmtBottomCenter.getValue()] = this.FindTile("mortar-bc");
		DMortarIndices[EBorderMotarType.bmtBottomRight0.getValue()] = this.FindTile("mortar-br0");
		DMortarIndices[EBorderMotarType.bmtBottomRight1.getValue()] = this.FindTile("mortar-br1");
		DMortarIndices[EBorderMotarType.bmtBottomRight2.getValue()] = this.FindTile("mortar-br2");
		DMortarIndices[EBorderMotarType.bmtBottomLeft0.getValue()] = this.FindTile("mortar-bl0");
		DMortarIndices[EBorderMotarType.bmtBottomLeft1.getValue()] = this.FindTile("mortar-bl1");
		DMortarIndices[EBorderMotarType.bmtBottomLeft2.getValue()] = this.FindTile("mortar-bl2");
		DMortarIndices[EBorderMotarType.bmtRightCenter.getValue()] = this.FindTile("mortar-rc");
		DMortarIndices[EBorderMotarType.bmtRightBottom0.getValue()] = this.FindTile("mortar-rb0");
		DMortarIndices[EBorderMotarType.bmtRightBottom1.getValue()] = this.FindTile("mortar-rb1");
		DMortarIndices[EBorderMotarType.bmtRightBottom2.getValue()] = this.FindTile("mortar-rb2");
		DMortarIndices[EBorderMotarType.bmtRightTop0.getValue()] = this.FindTile("mortar-rt0");
		DMortarIndices[EBorderMotarType.bmtRightTop1.getValue()] = this.FindTile("mortar-rt1");
		DMortarIndices[EBorderMotarType.bmtRightTop2.getValue()] = this.FindTile("mortar-rt2");
		DMortarIndices[EBorderMotarType.bmtLeftCenter.getValue()] = this.FindTile("mortar-lc");
		DMortarIndices[EBorderMotarType.bmtLeftBottom0.getValue()] = this.FindTile("mortar-lb0");
		DMortarIndices[EBorderMotarType.bmtLeftBottom1.getValue()] = this.FindTile("mortar-lb1");
		DMortarIndices[EBorderMotarType.bmtLeftBottom2.getValue()] = this.FindTile("mortar-lb2");
		DMortarIndices[EBorderMotarType.bmtLeftTop0.getValue()] = this.FindTile("mortar-lt0");
		DMortarIndices[EBorderMotarType.bmtLeftTop1.getValue()] = this.FindTile("mortar-lt1");
		DMortarIndices[EBorderMotarType.bmtLeftTop2.getValue()] = this.FindTile("mortar-lt2");

		return true;
	}

	/**
	 * @brief Draws the tile specified at the location
	 * @param game
	 *            Game drawing
	 * @param position
	 *            Position drawing at
	 * @param mortar_type
	 *            Mortar drawing
	 */
	public final void DrawTile(CGame game, Vector2 position, EBorderMotarType mortar_type) {
		super.DrawTile(game, new Vector2(position), DMortarIndices[mortar_type.getValue()]);
	}

	/**
	 * @brief Draws the tile at the location
	 * @param game
	 *            Game drawing
	 * @param x
	 *            X
	 * @param y
	 *            Y
	 * @param mortar_type
	 *            Mortar drawing
	 */
	public final void DrawTile(CGame game, int x, int y, EBorderMotarType mortar_type) {
		super.DrawTile(game, x, y, DMortarIndices[mortar_type.getValue()]);
	}

	public final void DrawTile(CGame game, float x, float y, EBorderMotarType mortar_type) {
		super.DrawTile(game, new Vector2(x, y), DMortarIndices[mortar_type.getValue()]);
	}

	/**
	 * @brief Enum of mortar types
	 */
	public enum EBorderMotarType {
		bmtTopLeft0(0),
		bmtTopLeft1(1),
		bmtTopLeft2(2),
		bmtTopCenter(3),
		bmtTopRight0(4),
		bmtTopRight1(5),
		bmtTopRight2(6),
		bmtRightTop0(7),
		bmtRightTop1(8),
		bmtRightTop2(9),
		bmtRightCenter(10),
		bmtRightBottom0(11),
		bmtRightBottom1(12),
		bmtRightBottom2(13),
		bmtBottomRight0(14),
		bmtBottomRight1(15),
		bmtBottomRight2(16),
		bmtBottomCenter(17),
		bmtBottomLeft0(18),
		bmtBottomLeft1(19),
		bmtBottomLeft2(20),
		bmtLeftTop0(21),
		bmtLeftTop1(22),
		bmtLeftTop2(23),
		bmtLeftCenter(24),
		bmtLeftBottom0(25),
		bmtLeftBottom1(26),
		bmtLeftBottom2(27),
		bmtMax(28);
		private static java.util.HashMap<Integer, EBorderMotarType> mappings;
		private int intValue;

		private EBorderMotarType(int value) {
			intValue = value;
			EBorderMotarType.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EBorderMotarType> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EBorderMotarType>();
			}
			return mappings;
		}

		public static EBorderMotarType forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}
}
