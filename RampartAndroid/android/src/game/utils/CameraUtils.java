package game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraUtils {

	public static Vector2 unproject(Camera camera, int screenX, int screenY) {
		Vector3 unprojectedCoords = camera.unproject(new Vector3(screenX, screenY, 0));
		return new Vector2((int) unprojectedCoords.x, (int) unprojectedCoords.y);
	}
}
