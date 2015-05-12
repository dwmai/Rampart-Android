package game.utils;

import com.badlogic.gdx.math.Vector2;

public class MathUtil {

	/**
	 * Returns the straight-line distance, in pixels, between two points
	 */
	public static double dist(Vector2 a, Vector2 b) {
		return Math.sqrt(Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2));
	}

	public static double slope(Vector2 a, Vector2 b) {
		try {
			return (b.y - a.y) / (b.x - a.x);
		} catch (Exception ex) {
			return -1;
		}
	}

	public static double magnitude(Vector2 x) {
		return (x.x * x.x) + (x.y * x.y);
	}

	public static final boolean IsContainedWithin(Vector2 self, Vector2 top_left, Vector2 size) {
		Vector2 BottomRight = top_left.cpy().add(size);
		return (top_left.x <= self.x && self.x <= BottomRight.x && top_left.y <= self.y && self.y <= BottomRight.y);
	}
}
