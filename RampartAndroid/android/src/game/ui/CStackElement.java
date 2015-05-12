package game.ui;

import com.badlogic.gdx.math.Vector2;

/**
 * @brief A UI container that stacks its children verticaly
 */
public class CStackElement extends CUIElement {

	/**
	 * @brief Create a new empty stack element
	 */
	public CStackElement() {
		super();
	}

	/**
	 * @brief Adds a new child element and recomputes the layout
	 * @param child
	 *            The new child
	 */
	public void AddChildElement(CUIElement child) {
		super.AddChildElement(child);
		LayoutChildren();
	}

	/**
	 * @brief Removes the child element and recomputes the layout
	 * @param child
	 *            The child to remove
	 */
	public void RemoveChildElement(CUIElement child) {
		super.RemoveChildElement(child);
		LayoutChildren();
	}

	/**
	 * @brief Removes all the children and recomputes the layout
	 */
	public void ClearChildren() {
		super.ClearChildren();
		LayoutChildren();
	}

	/**
	 * @brief Computes the layout and places the children vertically, and updates the size of the container
	 */
	public final void LayoutChildren() {
		Vector2 NewSize = new Vector2(Size().x, 0);
		for (CUIElement child : Children()) {
			child.Position(new Vector2(Anchor().x * NewSize.x, NewSize.y));
			child.Anchor(new Vector2(Anchor().x, 0));
			NewSize.add(new Vector2(0, child.Size().y));
		}
		Size(NewSize);
	}
}
