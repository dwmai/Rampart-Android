package game;

import com.badlogic.gdx.math.Vector2;

import game.ui.CUIElement;


/**
 * @brief Simple management of input
 */
public class CInputState {

	/**
	 * @brief Holds the position the mouse is currently at
	 */
	public Vector2 DMousePosition = new Vector2();
	/**
	 * @brief Holds the button pressed for the frame
	 */
	public EInputButton DButtonPressed;
	/**
	 * @brief The currently selected element
	 */
	public CUIElement DSelectedUIElement;
	/**
	 * @brief Whether the mouse button is down or the enter key is pressed
	 */
	public boolean DActionPressed;
	/**
	 * @brief The text entered by the user
	 */
	public String DTextEntered;
	/**
	 * @brief Whether the backspace key was pressed
	 */
	public boolean DBackSpacePressed;
    public boolean DEscapePressed;
    public CUIElement DAutoSelectElement;

    public CInputState() {
		DMousePosition.x = -1;
		DMousePosition.y = -1;
		DSelectedUIElement = null;
		DActionPressed = false;
		DTextEntered = "";
		DBackSpacePressed = false;
	}

	/**
	 * @brief Resets if the button was pressed, called per frame
	 */
	public final void Reset() {
		DActionPressed = false;
		DButtonPressed = CInputState.EInputButton.ibNone;
		DTextEntered = "";
		DBackSpacePressed = false;
	}

	/**
	 * @brief Gets the selected UI element
	 */
	public final CUIElement SelectedElement() {
		return DSelectedUIElement;
	}

	/**
	 * @brief Updates the selected UI element if necessary
	 * @param root
	 *            The root UI element to begin search
	 */
	public final void UpdateSelectedElement(CUIElement root) {
		SelectedElement(root.DetermineSelected(DMousePosition));
	}

	/**
	 * @brief Changes the selected UI element
	 * @param element
	 *            The new selected UI element
	 */
	public final void SelectedElement(CUIElement element) {
		DSelectedUIElement = element;
	}

	/**
	 * @brief Updates the state if a button is pressed
	 * @param pressed
	 *            Whether the mouse was clicked or enter key pressed
	 */
	public final void ButtonPressed(CInputState.EInputButton button) {
		DButtonPressed = button;
		DActionPressed = button == CInputState.EInputButton.ibLeftButton;
	}

	/**
	 * @brief Gets the state of if a button was pressed
	 */
	public final boolean ActionPressed() {
		return DActionPressed;
	}

	/**
	 * @brief Enum for mouse buttons, left and right
	 */
	public enum EInputButton {
		ibNone(-1),
		ibUnknownButton(0),
		ibLeftButton(1),
		ibRightButton(3);
		private static java.util.HashMap<Integer, EInputButton> mappings;
		private int intValue;

		private EInputButton(int value) {
			intValue = value;
			EInputButton.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EInputButton> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EInputButton>();
			}
			return mappings;
		}

		public static EInputButton forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}
}
