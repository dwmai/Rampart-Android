package game.ui;

import com.badlogic.gdx.math.Vector2;

import game.CGame;
import game.utils.MathUtil;


/**
 * @brief A UI element that can be placed via its position, anchor, and size, and can render child elements in a
 *        heirarchy
 */
public class CUIElement {

	/**
	 * @brief The children of this element
	 */
	private java.util.LinkedList<CUIElement> DChildren = new java.util.LinkedList<CUIElement>();
	/**
	 * @brief Updates the element given its computed translation. Derived classes should override this. Also updates its
	 *        children and hovered/clicked status
	 * @param game
	 *            The game to update in
	 * @param translation
	 *            The computed translation to base updating on
	 * @brief Stores if the element is currently hovered
	 */
	private boolean DIsHovered;
	/**
	 * @brief Stores if the element is currently clicked
	 */
	private boolean DIsPressed;
	/**
	 * @brief Stores the anchor to position the element by
	 */
	private Vector2 DAnchor = new Vector2();
	/**
	 * @brief Stores the computed translation
	 */
	private Vector2 DTranslation = new Vector2();
	/**
	 * @brief Stores the position
	 */
	private Vector2 DPosition = new Vector2();
	/**
	 * @brief Stores the size of the element
	 */
	private Vector2 DSize = new Vector2();
	/**
	 * @brief The element below
	 */
	private CUIElement DDownElement;
	/**
	 * @brief The element above
	 */
	private CUIElement DUpElement;
	/**
	 * @brief The element to the left
	 */
	private CUIElement DLeftElement;
	/**
	 * @brief The element to the right
	 */
	private CUIElement DRightElement;
	/**
	 * @brief Stores if the element is currently highlighted
	 */
	private boolean DIsSelected;

	/**
	 * @brief CUIElement constructor to initialize element pointers
	 */
	public CUIElement() {
		DDownElement = null;
		DUpElement = null;
		DLeftElement = null;
		DRightElement = null;
        DIsPressed = false;
        DIsHovered = false;
        DIsSelected = false;
	}

	public void Update(CGame game) {
		Update(game, new Vector2());
	}

	public void Update(CGame game, Vector2 translation) {
		DIsHovered = MathUtil.IsContainedWithin(game.InputState().DMousePosition, translation.cpy().add(Translation()),
				Size());
		DIsSelected = (this == game.InputState().SelectedElement());
		DIsPressed = DIsSelected && game.InputState().ActionPressed();
		for (CUIElement element : DChildren) {
			element.Update(game, translation.cpy().add(Translation()));
		}
	}

	/**
	 * @brief Draws the element given its computed translation. Derived classes should override this. Also draws its
	 *        children.
	 * @param game
	 * @param translation
	 *
	 */
	public void Draw(CGame game) {
		Draw(game, new Vector2());
	}

	public void Draw(CGame game, Vector2 translation) {
		for (CUIElement element : DChildren) {
			element.Draw(game, translation.cpy().add(Translation()));
		}
	}

	public void CUIElementDraw(CGame game, Vector2 translation) {
		for (CUIElement element : DChildren) {
			element.Draw(game, translation.cpy().add(Translation()));
		}
	}

	/**
	 * @brief Sets the anchor (0, 0) -> Top Left, (1, 1) -> Bottom Right
	 * @param anchor
	 *            The new anchor
	 */
	public final void Anchor(Vector2 anchor) {
		DAnchor = new Vector2(anchor);
		UpdateTranslation();
	}

	/**
	 * @brief Recomputes the translation given the size, position, and anchor
	 */
	public final void UpdateTranslation() {
		DTranslation = DPosition.cpy().sub(DAnchor.cpy().scl(DSize));
	}

	/**
	 * @brief Sets the position
	 * @param position
	 *            The new position
	 */
	public final void Position(Vector2 position) {
		DPosition = new Vector2(position);
		UpdateTranslation();
	}

	/**
	 * @brief Sets the size
	 * @param size
	 *            The new size
	 */
	public final void Size(Vector2 size) {
		DSize = new Vector2(size);
		UpdateTranslation();
	}

	/**
	 * @brief The Anchor
	 * @return
	 */
	public final Vector2 Anchor() {
		return DAnchor;
	}

	/**
	 * @brief The Position
	 * @return
	 */
	public final Vector2 Position() {
		return DPosition;
	}

	/**
	 * @brief The Size
	 * @return
	 */
	public final Vector2 Size() {
		return DSize;
	}

	/**
	 * @brief The computed translation
	 * @return
	 */
	public final Vector2 Translation() {
		return DTranslation;
	}

	/**
	 * @brief If the mouse is over this element
	 * @return TRUE if the mouse is over this element
	 */
	public final boolean IsHovered() {
		return DIsHovered;
	}

	/**
	 * @brief If the mouse or enter key pressed this element
	 * @return TRUE if the mouse or enter key pressed this element
	 */
	public final boolean IsPressed() {
		return DIsPressed;
	}

	/**
	 * @brief Adds a child element to this element
	 * @param child
	 *            The child to add
	 */
	public void AddChildElement(CUIElement child) {
		DChildren.addLast(child);
	}

	/**
	 * @brief Removes a child from this element
	 * @param child
	 *            The child to remove
	 */
	public void RemoveChildElement(CUIElement child) {
		DChildren.remove(child);
	}

	/**
	 * @brief Removes all children from this element
	 */
	public void ClearChildren() {
		DChildren.clear();
	}

	/**
	 * @brief Gets the children of this element
	 * @return
	 */
	public final java.util.LinkedList<CUIElement> Children() {
		return DChildren;
	}

	/*
	 * { CUIElement selected; for (CUIElement element : DChildren) { if (element.DetermineSelected(mousePosition,
	 * translation.cpy().add(Translation()))) return element; } return null; }
	 */

	/**
	 * @brief Gets down pointer
	 * @return
	 */
	public final CUIElement DownElement() {
		return DDownElement;
	}

	/**
	 * @brief Gets up pointer
	 * @return
	 */
	public final CUIElement UpElement() {
		return DUpElement;
	}

	/**
	 * @brief Sets left pointer to the element left
	 * @param element
	 *            The element left
	 */
	public final void LeftElement(CUIElement element) {
		DLeftElement = element;
	}

	/**
	 * @brief Gets left pointer
	 * @return
	 */
	public final CUIElement LeftElement() {
		return DLeftElement;
	}

	/**
	 * @brief Sets right pointer to the element right
	 * @param element
	 *            The element right
	 */
	public final void RightElement(CUIElement element) {
		DRightElement = element;
	}

	/**
	 * @brief Gets right pointer
	 * @return
	 */
	public final CUIElement RightElement() {
		return DRightElement;
	}

	/**
	 * @brief If the element has been selected
	 * @return TRUE if the element has been selected
	 */
	public final boolean IsSelected() {
		return DIsSelected;
	}

	/**
	 * @brief Sets this's up pointer to other, other's down pointer to this
	 */
	public final void ConnectVertically(CUIElement other) {
		this.UpElement(other);
		other.DownElement(this);
	}

	/**
	 * @brief Sets up pointer to the element above
	 * @param element
	 *            The element above
	 */
	public final void UpElement(CUIElement element) {
		DUpElement = element;
	}

	/**
	 * @brief Sets down pointer to the element below
	 * @param element
	 *            The element below
	 */
	public final void DownElement(CUIElement element) {
		DDownElement = element;
	}

	/**
	 * @brief Determines which UI element is selected given the computed translation
	 * @param mousePosition
	 *            Position of the mouse
	 * @param parent
	 *            Parent of UI element
	 * @return The selected UI element
	 */
	public CUIElement DetermineSelected(Vector2 mousePosition) {
		return DetermineSelected(mousePosition, new Vector2());
	}

	public CUIElement DetermineSelected(Vector2 mousePosition, Vector2 translation) {
		CUIElement selected;
        for (CUIElement element : DChildren) {
            if ((selected = element.DetermineSelected(mousePosition, translation.cpy().add(Translation()))) != null) {
                return selected;
            }
        }
		return null;
	}

}
