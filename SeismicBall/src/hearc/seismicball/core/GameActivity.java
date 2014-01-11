package hearc.seismicball.core;

import hearc.seismicball.framework.Screen;
import hearc.seismicball.framework.implementation.AndroidGame;

public class GameActivity extends AndroidGame {
	
	boolean firstTimeCreate = true;

	@Override
	public Screen getInitScreen() {
		if (firstTimeCreate) {
			Assets.load(this);
			
			firstTimeCreate = false;
		}
		return new SplashLoadingScreen(this);
	}

	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}
	
    @Override
    public void onResume() {
        super.onResume();
        
        // TODO relancer la musique (Assets.music.play())
    }

    @Override
    public void onPause() {
        super.onPause();
        
        // TODO couper la musique (Assets.music.pause())
    }
}
