package game.utils;

import com.badlogic.gdx.math.Vector2;

public class CloneUtils {

	public static Vector2 clone(Vector2 in) {
		return new Vector2(in.x, in.y);
	}
}
