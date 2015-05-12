package game.modes;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import game.CGame;
import game.animations.DefineConstants;
import game.ui.CButton;
import game.ui.CSpinner;
import game.ui.CStackElement;

/**
 * @brief Mode where user edits options
 */
public class CSoundOptionsMenuMode extends CMenuMode {

	// @Button for going back to main menu
	private CButton DBackButton;
    private CButton DApplyButton;
	// @List of buttons for each option
	private java.util.ArrayList<CButton> DOptions = new java.util.ArrayList<CButton>();
	// @Spinner for volume
	private CSpinner Volume;
    //@Spinner for sound effects volume
    private CSpinner SFXVolume;

	public CSoundOptionsMenuMode(CGame game) {
		super("SOUND OPTIONS");
		CStackElement S = new CStackElement();
		S.Size(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
		S.Position(new Vector2(DefineConstants.GAME_WIDTH / 2, DefineConstants.GAME_HEIGHT / 2));
		S.Anchor(new Vector2(0.5f, 0.5f));

        int LongestHorizontalSize = 0;

        ArrayList<String> options = new ArrayList<String>();
        options.add("OFF");
        options.add("1");
        options.add("2");
        options.add("3");
        options.add("4");
        options.add("5");
        options.add("6");
        options.add("7");
        options.add("8");
        options.add("9");
        options.add("MAX");
        Volume = new CSpinner(game, "MUSIC VOLUME", options);
        LongestHorizontalSize = Math.max(Volume.LongestHorizontalSize(), LongestHorizontalSize);
        for (int optionIterator = 0; optionIterator < DefineConstants.MUSIC_VOLUME; optionIterator++)
            Volume.ChangeSelectedOption(CSpinner.EPlusOrMinus.pmPlus);

        options.clear();
        options.add("OFF");
        options.add("1");
        options.add("2");
        options.add("3");
        options.add("4");
        options.add("5");
        options.add("6");
        options.add("7");
        options.add("8");
        options.add("9");
        options.add("MAX");
        SFXVolume = new CSpinner(game, "EFFECTS VOLUME", options);
        LongestHorizontalSize = Math.max(SFXVolume.LongestHorizontalSize(), LongestHorizontalSize);
        for (int optionIterator = 0; optionIterator < DefineConstants.SOUNDEFFECT_VOLUME; optionIterator++)
            SFXVolume.ChangeSelectedOption(CSpinner.EPlusOrMinus.pmPlus);

        Volume.LongestHorizontalSize(LongestHorizontalSize);
        Volume.LayoutElements();
        SFXVolume.LongestHorizontalSize(LongestHorizontalSize);
        SFXVolume.LayoutElements();

        S.AddChildElement(Volume);
        S.AddChildElement(SFXVolume);

        DRootElement.AddChildElement(S);

        DBackButton = new CButton(game, "BACK");
        DBackButton.Position(new Vector2(0, DefineConstants.GAME_HEIGHT));
        DBackButton.Anchor(new Vector2(0, 1));

        DApplyButton = new CButton(game, "Apply");
        DApplyButton.Position(new Vector2(DefineConstants.GAME_WIDTH, DefineConstants.GAME_HEIGHT));
        DApplyButton.Anchor(new Vector2(1, 1));

        DRootElement.AddChildElement(DApplyButton);
        DRootElement.AddChildElement(DBackButton);

        DBackButton.ConnectVertically(Volume.OptionName());
        Volume.OptionName().ConnectVertically(SFXVolume);
        SFXVolume.OptionName().ConnectVertically(DApplyButton);
        DApplyButton.ConnectVertically(DBackButton);
        game.InputState().DAutoSelectElement = Volume.OptionName();
	}

	public void Update(CGame game) {
		super.Update(game);

		if (DBackButton.IsPressed()) {
			game.SwitchMode(new COptionsMode(game));
		}


        if (DApplyButton.IsPressed()) {
            String VolumeString = Volume.SelectedOption();
            String SFXVolumeString = SFXVolume.SelectedOption();
            DefineConstants.MUSIC_VOLUME = Integer.parseInt((Volume.SelectedOption()));
            DefineConstants.SOUNDEFFECT_VOLUME = Integer.parseInt((SFXVolume.SelectedOption()));
//            CLuaConfig.LCInstance().UpdateFloatMapVal("DSoundEffectVolume", game.GameState().DConfig.SOUNDEFFECT_VOLUME);
//            CLuaConfig.LCInstance().UpdateFloatMapVal("DMusicVolume", game.GameState().DConfig.MUSIC_VOLUME);
//            CLuaConfig.LCInstance().UpdateConfigFile();
        }
	}
}
