package game.utils;

import com.badlogic.gdx.math.Vector2;

import game.SDirection;

/**
 * MathUtil class
 */
public class CMathUtil {

	/**
	 * Calculates integer square root of integer passed in, code from
	 * http://www.codecodex.com/wiki/Calculate_an_integer_square_root
	 * 
	 * @param x
	 *            Number to take square root of
	 * @return Integer portion of the square root of the parameter
	 * 
	 *         // Code from http://www.codecodex.com/wiki/Calculate_an_integer_square_root
	 */
	public static int IntegerSquareRoot(int x) {
		int Op;
		int Result;
		int One;
		Op = x;
		Result = 0;
		One = 1 << (4 * 8 - 2);
		while (One > Op) {
			One >>= 2;
		}
		while (0 != One) {
			if (Op >= Result + One) {
				Op -= Result + One;
				Result += One << 1; /** <-- faster than 2 * one */
			}
			Result >>= 1;
			One >>= 2;
		}
		return Result;
	}

	/**
	 * Compares positions of two sprites
	 * 
	 * @param first
	 *            const reference to a SpriteState
	 * @param second
	 *            const reference to a SpriteState
	 * @return True if first's position is greater than second's position, false if first's position is greater than
	 *         second's position; checked in order of z position, x position
	 */
	public static boolean SpriteCompare(Vector2 first, Vector2 second) {
		if (first.y < second.y) {
			return true;
		}
		if (first.y > second.y) {
			return false;
		}
		if (first.x < second.x) {
			return true;
		}
		return false;
	}

	/**
	 * @brief Calculates the direction between two SInt2
	 * @param a
	 *            First point
	 * @param b
	 *            Second point
	 * @return Direction
	 */
	public static SDirection.EValue CalculateDirection(Vector2 a, Vector2 b) {
		return CalculateDirection((int) a.x, (int) a.y, (int) b.x, (int) b.y);
	}

	/**
	 * Calculates direction of vector between two points
	 * 
	 * @param x1
	 *            x coordinate of first point
	 * @param y1
	 *            y coordinate of first point
	 * @param x2
	 *            x coordinate of second point
	 * @param y2
	 *            y coordinate of second point
	 * @return enum value representing direction
	 */
	public static SDirection.EValue CalculateDirection(int x1, int y1, int x2, int y2) {
		int XDistance = x2 - x1;
		int YDistance = y2 - y1;
		boolean NegativeX = XDistance < 0;
		boolean NegativeY = YDistance > 0; /** Top of screen is 0 */
		double SinSquared;
		XDistance *= XDistance;
		YDistance *= YDistance;
		if (0 == (XDistance + YDistance)) {
			return SDirection.EValue.dNorth;
		}
		SinSquared = (double) YDistance / (XDistance + YDistance);
		if (0.1464466094 > SinSquared) {
			/** East or West */
			if (NegativeX) {
				return SDirection.EValue.dWest; /** West */
			} else {
				return SDirection.EValue.dEast; /** East */
			}
		} else if (0.85355339059 > SinSquared) {
			/** NE, SE, SW, NW */
			if (NegativeY) {
				if (NegativeX) {
					return SDirection.EValue.dSouthWest; /** SW */
				} else {
					return SDirection.EValue.dSouthEast; /** SE */
				}
			} else {
				if (NegativeX) {
					return SDirection.EValue.dNorthWest; /** NW */
				} else {
					return SDirection.EValue.dNorthEast; /** NE */
				}
			}
		} else {
			/** North or South */
			if (NegativeY) {
				return SDirection.EValue.dSouth; /** South */
			} else {
				return SDirection.EValue.dNorth; /** North */
			}
		}
	}

	/**
	 * @brief Calculates if rectangles defined overlap
	 * @param position1
	 *            The top left of first rectangle
	 * @param size1
	 *            Size of first rectangle
	 * @param position2
	 *            The top left of second rectangle
	 * @param size2
	 *            Size of second rectangle
	 * @return true if overlap, false otherwise
	 */
	public static boolean DoRectanglesOverlap(Vector2 position1, Vector2 size1, Vector2 position2, Vector2 size2) {
		Vector2 P1 = position1;
		Vector2 P2 = position1.cpy().add(size1);
		Vector2 P3 = position2;
		Vector2 P4 = position2.cpy().add(size2);
		return !(P2.y <= P3.y || P1.y >= P4.y || P2.x <= P3.x || P1.x >= P4.x);
	}
}
