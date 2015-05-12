package com.fortnitta.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import game.FortNitta;

public class AndroidLauncher extends AndroidApplication {

    
    private AndroidApplicationConfiguration config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		config = new AndroidApplicationConfiguration();
        initialize(new FortNitta(), config);
	}


}
