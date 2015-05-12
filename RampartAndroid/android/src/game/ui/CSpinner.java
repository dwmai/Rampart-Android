package game.ui;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import game.CGame;

/**
 * @brief Spinner UI Element class
 */
public class CSpinner extends CUIElement {

	/**
	 * @brief The name for the spinner
	 */
	private CButton DOptionName;
	/**
	 * @brief The minus button
	 */
	private CButton DMinus;
	/**
	 * @brief The plus button
	 */
	private CButton DPlus;
	/**
	 * @brief The selected option
	 */
	private CButton DSelectedOption;
	/**
	 * @brief The vector of options
	 */
	private ArrayList<String> DOptions = new ArrayList<String>();
	/**
	 * @brief Iterator pointing to the selected option in the vector
	 */
	private int DOptionsIt;
	/**
	 * @brief The horizontal size of the selected option with the longest string
	 */
	private int DLongestHorizontalSize;

	/**
	 * @brief Spinner constructor
	 * @param game
	 *            The game
	 * @param text
	 *            The option type name to be displayed
	 * @param options
	 *            Pointer to a vector of options, must be filled with at least one string when instantiating a
	 *            spinner
	 * @param margin
	 *            The margin around the text for spacing
	 */
	public CSpinner(CGame game, String text, ArrayList<String> options) {
		this(game, text, options, new Vector2(15, 15));
	}

	public CSpinner(CGame game, String text, ArrayList<String> options, Vector2 margin) {
		super();
		DOptions = options;
		DOptionName = new CButton(game, text, margin);
		DMinus = new CButton(game, "-", margin);

		/** want to leave enough room for the longest option name */
		String LongestOption = "";
		for (String option : options)
			if (option.length() > LongestOption.length())
				LongestOption = new String(option);

		DSelectedOption = new CButton(game, LongestOption, margin);
		DLongestHorizontalSize = (int) DSelectedOption.Size().x;
		/** logically would expect to see the first option initially */
		DSelectedOption.Text(DOptions.get(0));
		DOptionsIt = 0;

		DPlus = new CButton(game, "+", margin);

		Size(new Vector2(Size().x, DOptionName.Size().y));
		LayoutElements();

		AddChildElement(DMinus);
		AddChildElement(DPlus);
		AddChildElement(DOptionName);
		AddChildElement(DSelectedOption);

	}

	/**
	 * @brief Lays out the elements of the spinner horizontally
	 */
	public final void LayoutElements() {
		Vector2 NewSize = new Vector2(0, Size().y);
		DOptionName.Position(new Vector2(NewSize.x, 0));
		NewSize.add(new Vector2(DOptionName.Size().x, 0));
		DMinus.Position(new Vector2(NewSize.x, 0));
		NewSize.add(new Vector2(DMinus.Size().x, 0));

		/** find offset to center selected option text */
		int offset = (DLongestHorizontalSize - (int) DSelectedOption.Size().x) / 2;
		DSelectedOption.Position(new Vector2(NewSize.x + offset, 0));
		NewSize.add(new Vector2(DLongestHorizontalSize, 0));
		DPlus.Position(new Vector2(NewSize.x, 0));
		NewSize.add(new Vector2(DPlus.Size().x, 0));
		Size(NewSize);
	}

	/**
	 * @brief Updates the spinner and changes the selected option if necessary
	 * @param game
	 *            The game to update in
	 * @param translation
	 *            The computed translation
	 */
	public void Update(CGame game, Vector2 translation) {
		super.Update(game, new Vector2(translation));
		if (DOptionName.IsPressed() || DPlus.IsPressed() || DSelectedOption.IsPressed()) {
			ChangeSelectedOption(EPlusOrMinus.pmPlus);
		} else if (DMinus.IsPressed()) {
			ChangeSelectedOption(EPlusOrMinus.pmMinus);
		}
	}

	/**
	 * @brief Changes the selected option to next or previous option
	 * @param choice
	 *            Indicates if the spinner should cycle left (minus) or right (plus)
	 */
	public final void ChangeSelectedOption(EPlusOrMinus choice) {
		if (EPlusOrMinus.pmPlus.equals(choice)) {

            if (DOptionsIt < DOptions.size() - 1) {
                DOptionsIt++;
            }
			else if (DOptions.get(DOptions.size() - 1).equals(DOptions.get(DOptionsIt))) {
				DOptionsIt = 0;
			}
		} else {
			if (DOptions.get(0).equals(DOptions.get(DOptionsIt))) {
				DOptionsIt = DOptions.size() - 1;
			} else {
                if (DOptionsIt > 0)
				    --DOptionsIt;
			}
		}
		DSelectedOption.Text(DOptions.get(DOptionsIt));
		LayoutElements();
	}

	/**
	 * @brief Gets the selected option
	 * @return The selected option
	 */
	public final String SelectedOption() {
		return DOptions.get(DOptionsIt);
	}

	/**
	 * @brief Gets the longest horizontal sized option
	 * @return DLongestHorizontalSize
	 */
	public final int LongestHorizontalSize() {
		return DLongestHorizontalSize;
	}

	/**
	 * @brief Sets the longest horizontal sized option
	 * @param size
	 *            The new size
	 */
	public final void LongestHorizontalSize(int size) {
		DLongestHorizontalSize = size;
	}

    /**
*
* @brief Gets the option name button
*
* @return The option name button

*/
    public final CUIElement OptionName() {
        return DOptionName;
    }

	/**
	 * Enum for plus or minus click
	 */
	public enum EPlusOrMinus {
		pmMinus(0),
		pmPlus(1);
		private static java.util.HashMap<Integer, EPlusOrMinus> mappings;
		private int intValue;

		private EPlusOrMinus(int value) {
			intValue = value;
			EPlusOrMinus.getMappings().put(value, this);
		}

		private synchronized static java.util.HashMap<Integer, EPlusOrMinus> getMappings() {
			if (mappings == null) {
				mappings = new java.util.HashMap<Integer, EPlusOrMinus>();
			}
			return mappings;
		}

		public static EPlusOrMinus forValue(int value) {
			return getMappings().get(value);
		}

		public int getValue() {
			return intValue;
		}
	}
}
